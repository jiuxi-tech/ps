#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
依赖检查工具 (T1.3.3)
功能：检测循环依赖和违规依赖，确保DDD分层架构合规性
作者：DDD重构工具
版本：1.0
创建时间：2025-09-16
"""

import os
import re
from pathlib import Path
from typing import List, Dict, Tuple, Set
import logging
import json
from datetime import datetime
from collections import defaultdict, deque

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler('dependency-check.log', encoding='utf-8'),
        logging.StreamHandler()
    ]
)

class DependencyChecker:
    """依赖检查工具类"""
    
    def __init__(self, project_root: str):
        self.project_root = Path(project_root)
        self.src_root = self.project_root / "src" / "main" / "java"
        self.module_root = self.src_root / "com" / "jiuxi" / "module"
        
        # DDD分层架构层级定义（数字越小层级越高）
        self.layer_hierarchy = {
            "intf": 1,        # 接口适配器层（最上层）
            "app": 2,         # 应用服务层  
            "domain": 3,      # 领域层（核心）
            "infra": 4        # 基础设施层（最下层）
        }
        
        # 依赖检查报告
        self.check_report = {
            "timestamp": datetime.now().isoformat(),
            "modules_analyzed": [],
            "dependency_graph": {},
            "circular_dependencies": [],
            "layer_violations": [],
            "statistics": {
                "total_files_analyzed": 0,
                "total_dependencies": 0,
                "circular_dependency_count": 0,
                "layer_violation_count": 0,
                "modules_count": 0
            }
        }
        
        # 依赖图
        self.dependency_graph = defaultdict(set)  # class -> set of dependencies
        self.reverse_dependency_graph = defaultdict(set)  # class -> set of dependents
        self.class_to_layer = {}  # class -> layer
        self.class_to_module = {}  # class -> module
        
    def find_modules(self) -> List[str]:
        """查找所有模块"""
        modules = []
        if not self.module_root.exists():
            logging.warning(f"模块根目录不存在: {self.module_root}")
            return modules
            
        for module_dir in self.module_root.iterdir():
            if module_dir.is_dir() and not module_dir.name.startswith('.'):
                modules.append(module_dir.name)
                
        logging.info(f"找到 {len(modules)} 个模块: {modules}")
        self.check_report["statistics"]["modules_count"] = len(modules)
        return modules
    
    def get_layer_from_path(self, file_path: Path) -> str:
        """从文件路径获取DDD层级"""
        path_parts = file_path.parts
        
        # 查找层级关键词
        for part in path_parts:
            if part in self.layer_hierarchy:
                return part
                
        # 如果没有明确的层级，根据路径推断
        path_str = str(file_path).lower()
        if "controller" in path_str or "web" in path_str:
            return "intf"
        elif "service" in path_str and "domain" not in path_str:
            return "app" 
        elif "domain" in path_str or "entity" in path_str or "valueobject" in path_str:
            return "domain"
        elif "mapper" in path_str or "persistence" in path_str or "repository" in path_str:
            return "infra"
        else:
            return "unknown"
    
    def get_module_from_path(self, file_path: Path) -> str:
        """从文件路径获取模块名"""
        try:
            module_index = file_path.parts.index("module")
            if module_index + 1 < len(file_path.parts):
                return file_path.parts[module_index + 1]
        except (ValueError, IndexError):
            pass
        return "unknown"
    
    def parse_java_file(self, file_path: Path) -> Tuple[str, List[str], str]:
        """解析Java文件，提取类名、依赖关系和包声明"""
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
            
            # 提取package声明
            package_match = re.search(r'^package\s+([\w.]+)\s*;', content, re.MULTILINE)
            package_name = package_match.group(1) if package_match else ""
            
            # 提取类名
            class_name = file_path.stem
            full_class_name = f"{package_name}.{class_name}" if package_name else class_name
            
            # 提取import依赖
            import_matches = re.findall(r'^import\s+(?:static\s+)?([\w.*]+)\s*;', content, re.MULTILINE)
            
            # 过滤出项目内部依赖
            internal_imports = []
            for import_path in import_matches:
                if (import_path.startswith("com.jiuxi.module.") and 
                    not import_path.endswith(".*") and
                    import_path != full_class_name):  # 排除自己
                    internal_imports.append(import_path)
            
            return full_class_name, internal_imports, package_name
            
        except Exception as e:
            logging.error(f"解析Java文件失败 {file_path}: {e}")
            return "", [], ""
    
    def build_dependency_graph(self) -> bool:
        """构建依赖图"""
        modules = self.find_modules()
        if not modules:
            return False
            
        java_files = []
        for module in modules:
            module_path = self.module_root / module
            for java_file in module_path.rglob("*.java"):
                if java_file.is_file():
                    java_files.append(java_file)
        
        logging.info(f"找到 {len(java_files)} 个Java文件")
        self.check_report["statistics"]["total_files_analyzed"] = len(java_files)
        
        # 解析每个文件
        for file_path in java_files:
            try:
                class_name, dependencies, package_name = self.parse_java_file(file_path)
                if not class_name:
                    continue
                    
                # 记录类的层级和模块信息
                layer = self.get_layer_from_path(file_path)
                module = self.get_module_from_path(file_path)
                
                self.class_to_layer[class_name] = layer
                self.class_to_module[class_name] = module
                
                # 构建依赖图
                for dep in dependencies:
                    self.dependency_graph[class_name].add(dep)
                    self.reverse_dependency_graph[dep].add(class_name)
                    
                self.check_report["statistics"]["total_dependencies"] += len(dependencies)
                
            except Exception as e:
                logging.error(f"处理文件失败 {file_path}: {e}")
                continue
        
        logging.info(f"构建依赖图完成: {len(self.dependency_graph)} 个类")
        return True
    
    def find_circular_dependencies(self) -> List[List[str]]:
        """查找循环依赖"""
        circular_deps = []
        visited = set()
        rec_stack = set()
        path = []
        
        def dfs(node: str) -> bool:
            """深度优先搜索检测循环"""
            if node in rec_stack:
                # 找到循环，提取循环路径
                cycle_start = path.index(node)
                cycle = path[cycle_start:] + [node]
                circular_deps.append(cycle)
                return True
                
            if node in visited:
                return False
                
            visited.add(node)
            rec_stack.add(node)
            path.append(node)
            
            for neighbor in self.dependency_graph.get(node, set()):
                if dfs(neighbor):
                    return True
                    
            rec_stack.remove(node)
            path.pop()
            return False
        
        # 检查所有节点
        for node in self.dependency_graph:
            if node not in visited:
                dfs(node)
        
        # 去重相似的循环
        unique_cycles = []
        for cycle in circular_deps:
            # 简单去重：检查是否已有相同的循环（可能起点不同）
            is_duplicate = False
            for existing_cycle in unique_cycles:
                if set(cycle) == set(existing_cycle) and len(cycle) == len(existing_cycle):
                    is_duplicate = True
                    break
            if not is_duplicate:
                unique_cycles.append(cycle)
        
        logging.info(f"发现 {len(unique_cycles)} 个循环依赖")
        self.check_report["statistics"]["circular_dependency_count"] = len(unique_cycles)
        
        return unique_cycles
    
    def check_layer_violations(self) -> List[Dict]:
        """检查分层架构违规"""
        violations = []
        
        for class_name, dependencies in self.dependency_graph.items():
            source_layer = self.class_to_layer.get(class_name, "unknown")
            source_module = self.class_to_module.get(class_name, "unknown")
            
            if source_layer == "unknown":
                continue
                
            for dep_class in dependencies:
                target_layer = self.class_to_layer.get(dep_class, "unknown")
                target_module = self.class_to_module.get(dep_class, "unknown")
                
                if target_layer == "unknown":
                    continue
                    
                # 检查是否违反分层架构原则
                violation_type = self.check_dependency_violation(
                    source_layer, target_layer, source_module, target_module
                )
                
                if violation_type:
                    violation = {
                        "type": violation_type,
                        "source_class": class_name,
                        "target_class": dep_class,
                        "source_layer": source_layer,
                        "target_layer": target_layer,
                        "source_module": source_module,
                        "target_module": target_module,
                        "description": self.get_violation_description(violation_type, source_layer, target_layer)
                    }
                    violations.append(violation)
        
        logging.info(f"发现 {len(violations)} 个分层架构违规")
        self.check_report["statistics"]["layer_violation_count"] = len(violations)
        
        return violations
    
    def check_dependency_violation(self, source_layer: str, target_layer: str, 
                                 source_module: str, target_module: str) -> str:
        """检查依赖是否违规"""
        
        # 获取层级权重
        source_weight = self.layer_hierarchy.get(source_layer, 999)
        target_weight = self.layer_hierarchy.get(target_layer, 999)
        
        # 违规类型1: 下层依赖上层（违反依赖方向）
        if source_weight > target_weight:
            return "LAYER_INVERSION"
            
        # 违规类型2: domain层依赖infra层（违反DDD原则）
        if source_layer == "domain" and target_layer == "infra":
            return "DOMAIN_DEPENDS_ON_INFRA"
            
        # 违规类型3: domain层依赖intf层（违反DDD原则）
        if source_layer == "domain" and target_layer == "intf":
            return "DOMAIN_DEPENDS_ON_INTF"
            
        # 违规类型4: 跨模块的不当依赖（需要进一步分析）
        if (source_module != target_module and 
            source_layer == "domain" and target_layer == "domain"):
            return "CROSS_MODULE_DOMAIN_DEPENDENCY"
            
        return None
    
    def get_violation_description(self, violation_type: str, source_layer: str, target_layer: str) -> str:
        """获取违规描述"""
        descriptions = {
            "LAYER_INVERSION": f"{source_layer}层不应该依赖{target_layer}层（违反依赖方向）",
            "DOMAIN_DEPENDS_ON_INFRA": "领域层不应该依赖基础设施层（违反DDD原则）",
            "DOMAIN_DEPENDS_ON_INTF": "领域层不应该依赖接口适配器层（违反DDD原则）",
            "CROSS_MODULE_DOMAIN_DEPENDENCY": "跨模块的领域层依赖需要仔细评估（可能需要重新设计模块边界）"
        }
        return descriptions.get(violation_type, "未知违规类型")
    
    def analyze_module_dependencies(self) -> Dict[str, Dict]:
        """分析模块间依赖关系"""
        module_deps = defaultdict(lambda: defaultdict(int))
        
        for class_name, dependencies in self.dependency_graph.items():
            source_module = self.class_to_module.get(class_name, "unknown")
            
            for dep_class in dependencies:
                target_module = self.class_to_module.get(dep_class, "unknown")
                if source_module != target_module and target_module != "unknown":
                    module_deps[source_module][target_module] += 1
        
        # 转换为普通字典便于序列化
        result = {}
        for source_module, targets in module_deps.items():
            result[source_module] = dict(targets)
            
        return result
    
    def generate_dependency_matrix(self) -> Dict:
        """生成依赖矩阵"""
        layers = list(self.layer_hierarchy.keys())
        matrix = {}
        
        for source_layer in layers:
            matrix[source_layer] = {}
            for target_layer in layers:
                count = 0
                for class_name, dependencies in self.dependency_graph.items():
                    if self.class_to_layer.get(class_name) == source_layer:
                        for dep_class in dependencies:
                            if self.class_to_layer.get(dep_class) == target_layer:
                                count += 1
                matrix[source_layer][target_layer] = count
                
        return matrix
    
    def run_dependency_check(self) -> bool:
        """运行依赖检查"""
        logging.info("开始依赖检查分析...")
        
        # 1. 构建依赖图
        if not self.build_dependency_graph():
            logging.error("构建依赖图失败")
            return False
            
        # 2. 查找循环依赖
        circular_deps = self.find_circular_dependencies()
        self.check_report["circular_dependencies"] = [
            {
                "cycle": cycle,
                "length": len(cycle) - 1,
                "modules": list(set(self.class_to_module.get(cls, "unknown") for cls in cycle))
            }
            for cycle in circular_deps
        ]
        
        # 3. 检查分层架构违规
        layer_violations = self.check_layer_violations()
        self.check_report["layer_violations"] = layer_violations
        
        # 4. 分析模块间依赖
        module_dependencies = self.analyze_module_dependencies()
        self.check_report["module_dependencies"] = module_dependencies
        
        # 5. 生成依赖矩阵
        dependency_matrix = self.generate_dependency_matrix()
        self.check_report["dependency_matrix"] = dependency_matrix
        
        # 6. 设置分析的模块列表
        self.check_report["modules_analyzed"] = self.find_modules()
        
        logging.info("依赖检查分析完成")
        return True
    
    def save_check_report(self):
        """保存检查报告"""
        report_file = self.project_root / "dependency-check-report.json"
        with open(report_file, 'w', encoding='utf-8') as f:
            json.dump(self.check_report, f, ensure_ascii=False, indent=2)
        logging.info(f"依赖检查报告已保存: {report_file}")
    
    def print_summary(self):
        """打印检查摘要"""
        stats = self.check_report["statistics"]
        print(f"\n📊 依赖检查摘要:")
        print(f"   - 分析文件数: {stats['total_files_analyzed']}")
        print(f"   - 分析模块数: {stats['modules_count']}")
        print(f"   - 总依赖关系: {stats['total_dependencies']}")
        print(f"   - 循环依赖: {stats['circular_dependency_count']}")
        print(f"   - 分层违规: {stats['layer_violation_count']}")
        
        if stats['circular_dependency_count'] > 0:
            print(f"\n🔄 循环依赖详情:")
            for i, cycle_info in enumerate(self.check_report["circular_dependencies"][:3], 1):
                cycle = cycle_info["cycle"]
                print(f"   {i}. {' -> '.join(cycle[:3])}{'...' if len(cycle) > 3 else ''}")
                
        if stats['layer_violation_count'] > 0:
            print(f"\n⚠️ 分层违规详情:")
            violation_types = {}
            for violation in self.check_report["layer_violations"]:
                vtype = violation["type"]
                violation_types[vtype] = violation_types.get(vtype, 0) + 1
                
            for vtype, count in violation_types.items():
                print(f"   - {vtype}: {count}个")

def main():
    """主函数"""
    # 项目根目录
    project_root = r"D:\keycloak_sb_sso_new0910_claude\ps\ps-be"
    
    print("=" * 60)
    print("依赖检查工具 v1.0")
    print("功能: 检测循环依赖和违规依赖，确保DDD分层架构合规性")
    print("=" * 60)
    
    # 创建依赖检查工具实例
    checker = DependencyChecker(project_root)
    
    # 执行检查
    try:
        success = checker.run_dependency_check()
        
        if success:
            checker.print_summary()
            checker.save_check_report()
            
            stats = checker.check_report["statistics"]
            if stats['circular_dependency_count'] == 0 and stats['layer_violation_count'] == 0:
                print("\n✅ 依赖检查通过！没有发现循环依赖和分层违规。")
            else:
                print("\n⚠️ 发现依赖问题，请查看详细报告进行修复。")
        else:
            print("\n❌ 依赖检查失败，请检查日志")
            
    except Exception as e:
        logging.error(f"依赖检查过程出现错误: {e}")
        print(f"\n❌ 检查失败: {e}")
        
    print("\n检查完成，详细信息请查看 dependency-check.log 和 dependency-check-report.json")

if __name__ == "__main__":
    main()