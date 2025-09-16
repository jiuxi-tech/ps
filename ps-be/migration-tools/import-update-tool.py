#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Import更新工具 (T1.3.2)
功能：自动更新import语句，处理包路径变更
作者：DDD重构工具
版本：1.0
创建时间：2025-09-16
"""

import os
import re
import shutil
from pathlib import Path
from typing import List, Dict, Tuple, Set
import logging
import json
from datetime import datetime

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler('import-update.log', encoding='utf-8'),
        logging.StreamHandler()
    ]
)

class ImportUpdateTool:
    """Import更新工具类"""
    
    def __init__(self, project_root: str):
        self.project_root = Path(project_root)
        self.src_root = self.project_root / "src" / "main" / "java"
        self.update_report = {
            "timestamp": datetime.now().isoformat(),
            "updated_files": [],
            "failed_updates": [],
            "statistics": {
                "total_files_scanned": 0,
                "total_files_updated": 0,
                "total_imports_updated": 0,
                "total_package_declarations_updated": 0
            }
        }
        
        # 包路径映射规则
        self.package_mappings = self.get_package_mappings()
        
    def get_package_mappings(self) -> Dict[str, str]:
        """获取包路径映射规则"""
        return {
            # interfaces -> intf 重命名
            r"\.interfaces\.": ".intf.",
            r"\.interfaces$": ".intf",
            
            # repository -> repo 重命名  
            r"\.repository\.": ".repo.",
            r"\.repository$": ".repo",
            
            # valueobject -> vo 重命名
            r"\.valueobject\.": ".vo.",
            r"\.valueobject$": ".vo",
            
            # infrastructure -> infra 重命名
            r"\.infrastructure\.": ".infra.",
            r"\.infrastructure$": ".infra",
            
            # 常见DDD包路径标准化
            r"\.domain\.model\.entity\.": ".domain.entity.",
            r"\.domain\.model\.vo\.": ".domain.vo.", 
            r"\.domain\.model\.valueobject\.": ".domain.vo.",
            r"\.domain\.repository\.": ".domain.repo.",
            
            # Controller CQRS分离
            r"\.controller\.([A-Za-z]+Controller)": r".controller.query.\1",  # 默认放到query
        }
    
    def find_java_files(self) -> List[Path]:
        """查找所有Java文件"""
        java_files = []
        if not self.src_root.exists():
            logging.warning(f"源目录不存在: {self.src_root}")
            return java_files
            
        for java_file in self.src_root.rglob("*.java"):
            if java_file.is_file():
                java_files.append(java_file)
                
        logging.info(f"找到 {len(java_files)} 个Java文件")
        self.update_report["statistics"]["total_files_scanned"] = len(java_files)
        return java_files
    
    def backup_file(self, file_path: Path) -> bool:
        """备份文件"""
        try:
            backup_dir = self.project_root / "backup" / "import-updates" / datetime.now().strftime('%Y%m%d_%H%M%S')
            backup_dir.mkdir(parents=True, exist_ok=True)
            
            # 保持相对路径结构
            relative_path = file_path.relative_to(self.src_root)
            backup_file = backup_dir / relative_path
            backup_file.parent.mkdir(parents=True, exist_ok=True)
            
            shutil.copy2(file_path, backup_file)
            return True
        except Exception as e:
            logging.error(f"备份文件失败 {file_path}: {e}")
            return False
    
    def parse_java_file(self, file_path: Path) -> Tuple[str, List[str], str]:
        """解析Java文件，提取package声明和import语句"""
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
                
            # 提取package声明
            package_match = re.search(r'^package\s+([\w.]+)\s*;', content, re.MULTILINE)
            package_declaration = package_match.group(1) if package_match else ""
            
            # 提取所有import语句
            import_matches = re.findall(r'^import\s+(static\s+)?([\w.*]+)\s*;', content, re.MULTILINE)
            imports = [match[1] for match in import_matches]  # match[1]是import的包名
            
            return content, imports, package_declaration
            
        except Exception as e:
            logging.error(f"解析Java文件失败 {file_path}: {e}")
            return "", [], ""
    
    def update_package_path(self, package_path: str) -> Tuple[str, bool]:
        """更新包路径"""
        original_path = package_path
        updated_path = package_path
        changed = False
        
        for pattern, replacement in self.package_mappings.items():
            new_path = re.sub(pattern, replacement, updated_path)
            if new_path != updated_path:
                updated_path = new_path
                changed = True
                
        return updated_path, changed
    
    def update_java_file_content(self, file_path: Path, content: str, package_declaration: str, imports: List[str]) -> Tuple[str, int, int]:
        """更新Java文件内容"""
        updated_content = content
        import_updates = 0
        package_updates = 0
        
        try:
            # 更新package声明
            if package_declaration:
                new_package, package_changed = self.update_package_path(package_declaration)
                if package_changed:
                    updated_content = re.sub(
                        r'^package\s+' + re.escape(package_declaration) + r'\s*;',
                        f'package {new_package};',
                        updated_content,
                        flags=re.MULTILINE
                    )
                    package_updates += 1
                    logging.debug(f"更新package: {package_declaration} -> {new_package}")
            
            # 更新import语句
            for import_path in imports:
                new_import, import_changed = self.update_package_path(import_path)
                if import_changed:
                    # 使用更精确的正则表达式来匹配import语句
                    pattern = r'^import\s+(static\s+)?' + re.escape(import_path) + r'\s*;'
                    replacement = r'import \1' + new_import + ';'
                    
                    updated_content = re.sub(
                        pattern,
                        replacement.replace(r'\1', r'\g<1>'),  # 处理optional group
                        updated_content,
                        flags=re.MULTILINE
                    )
                    import_updates += 1
                    logging.debug(f"更新import: {import_path} -> {new_import}")
            
            return updated_content, import_updates, package_updates
            
        except Exception as e:
            logging.error(f"更新文件内容失败 {file_path}: {e}")
            return content, 0, 0
    
    def should_update_file(self, imports: List[str], package_declaration: str) -> bool:
        """检查文件是否需要更新"""
        # 检查package声明
        if package_declaration:
            _, package_changed = self.update_package_path(package_declaration)
            if package_changed:
                return True
                
        # 检查import语句
        for import_path in imports:
            _, import_changed = self.update_package_path(import_path)
            if import_changed:
                return True
                
        return False
    
    def update_file(self, file_path: Path) -> bool:
        """更新单个文件"""
        try:
            # 解析文件
            content, imports, package_declaration = self.parse_java_file(file_path)
            if not content:
                return False
                
            # 检查是否需要更新
            if not self.should_update_file(imports, package_declaration):
                return True  # 不需要更新也算成功
                
            # 备份文件
            if not self.backup_file(file_path):
                logging.warning(f"备份失败，跳过更新: {file_path}")
                return False
                
            # 更新内容
            updated_content, import_updates, package_updates = self.update_java_file_content(
                file_path, content, package_declaration, imports
            )
            
            # 写回文件
            if updated_content != content:
                with open(file_path, 'w', encoding='utf-8') as f:
                    f.write(updated_content)
                    
                # 记录更新信息
                update_info = {
                    "file": str(file_path.relative_to(self.project_root)),
                    "import_updates": import_updates,
                    "package_updates": package_updates,
                    "total_updates": import_updates + package_updates
                }
                self.update_report["updated_files"].append(update_info)
                self.update_report["statistics"]["total_imports_updated"] += import_updates
                self.update_report["statistics"]["total_package_declarations_updated"] += package_updates
                
                logging.info(f"已更新: {file_path.relative_to(self.project_root)} (imports: {import_updates}, package: {package_updates})")
                
            return True
            
        except Exception as e:
            logging.error(f"更新文件失败 {file_path}: {e}")
            self.update_report["failed_updates"].append({
                "file": str(file_path.relative_to(self.project_root)),
                "error": str(e)
            })
            return False
    
    def update_all_files(self) -> bool:
        """更新所有文件"""
        java_files = self.find_java_files()
        
        if not java_files:
            logging.info("没有找到Java文件")
            return True
            
        logging.info(f"开始更新 {len(java_files)} 个Java文件的import语句")
        
        success_count = 0
        for file_path in java_files:
            if self.update_file(file_path):
                success_count += 1
                
        self.update_report["statistics"]["total_files_updated"] = len(self.update_report["updated_files"])
        
        success_rate = success_count / len(java_files) * 100
        logging.info(f"更新完成: {success_count}/{len(java_files)} 成功 ({success_rate:.1f}%)")
        logging.info(f"实际更新文件数: {self.update_report['statistics']['total_files_updated']}")
        logging.info(f"总计更新import: {self.update_report['statistics']['total_imports_updated']}")
        logging.info(f"总计更新package: {self.update_report['statistics']['total_package_declarations_updated']}")
        
        # 保存更新报告
        self.save_update_report()
        
        return success_count == len(java_files)
    
    def save_update_report(self):
        """保存更新报告"""
        report_file = self.project_root / "import-update-report.json"
        with open(report_file, 'w', encoding='utf-8') as f:
            json.dump(self.update_report, f, ensure_ascii=False, indent=2)
        logging.info(f"更新报告已保存: {report_file}")
    
    def validate_imports(self) -> Dict[str, List[str]]:
        """验证import语句，检查是否有无效引用"""
        validation_results = {
            "valid_imports": [],
            "invalid_imports": [],
            "missing_classes": []
        }
        
        java_files = self.find_java_files()
        
        # 构建所有类的映射
        class_locations = {}
        for file_path in java_files:
            try:
                _, _, package_declaration = self.parse_java_file(file_path)
                if package_declaration:
                    class_name = file_path.stem  # 文件名去掉扩展名
                    full_class_name = f"{package_declaration}.{class_name}"
                    class_locations[full_class_name] = file_path
            except:
                continue
                
        # 验证每个文件的import
        for file_path in java_files:
            try:
                _, imports, _ = self.parse_java_file(file_path)
                for import_path in imports:
                    if import_path.endswith(".*"):
                        # 跳过包级别的import
                        continue
                        
                    if import_path in class_locations:
                        validation_results["valid_imports"].append({
                            "file": str(file_path.relative_to(self.project_root)),
                            "import": import_path,
                            "target": str(class_locations[import_path].relative_to(self.project_root))
                        })
                    else:
                        # 检查是否是JDK或第三方库的类
                        if (import_path.startswith("java.") or 
                            import_path.startswith("javax.") or
                            import_path.startswith("org.springframework.") or
                            import_path.startswith("com.baomidou.") or
                            import_path.startswith("org.apache.")):
                            continue  # 跳过标准库和常用第三方库
                            
                        validation_results["invalid_imports"].append({
                            "file": str(file_path.relative_to(self.project_root)),
                            "import": import_path
                        })
                        
            except:
                continue
                
        return validation_results

def main():
    """主函数"""
    # 项目根目录
    project_root = r"D:\keycloak_sb_sso_new0910_claude\ps\ps-be"
    
    print("=" * 60)
    print("Import更新工具 v1.0")
    print("功能: 自动更新import语句，处理包路径变更")
    print("=" * 60)
    
    # 创建更新工具实例
    update_tool = ImportUpdateTool(project_root)
    
    # 执行更新
    try:
        print("\n🔍 开始扫描和更新import语句...")
        success = update_tool.update_all_files()
        
        if success:
            print("\n✅ 所有文件import更新成功!")
        else:
            print("\n⚠️ 部分文件import更新失败，请检查日志")
            
        # 可选：执行import验证
        print("\n🔍 正在验证import语句...")
        validation_results = update_tool.validate_imports()
        
        invalid_count = len(validation_results["invalid_imports"])
        if invalid_count > 0:
            print(f"\n⚠️ 发现 {invalid_count} 个可能无效的import语句")
            for invalid in validation_results["invalid_imports"][:5]:  # 显示前5个
                print(f"  - {invalid['file']}: {invalid['import']}")
            if invalid_count > 5:
                print(f"  ... 和其他 {invalid_count - 5} 个")
        else:
            print("\n✅ 所有import语句验证通过!")
            
    except Exception as e:
        logging.error(f"更新过程出现错误: {e}")
        print(f"\n❌ 更新失败: {e}")
        
    print("\n更新完成，详细信息请查看 import-update.log 和 import-update-report.json")

if __name__ == "__main__":
    main()