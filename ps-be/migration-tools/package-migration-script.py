#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
包迁移脚本 (T1.3.1)
功能：批量移动类文件，支持DDD分层架构重构
作者：DDD重构工具
版本：1.0
创建时间：2025-09-16
"""

import os
import shutil
import re
from pathlib import Path
from typing import List, Dict, Tuple
import logging
import json
from datetime import datetime

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler('migration.log', encoding='utf-8'),
        logging.StreamHandler()
    ]
)

class PackageMigrationTool:
    """包迁移工具类"""
    
    def __init__(self, project_root: str):
        self.project_root = Path(project_root)
        self.src_root = self.project_root / "src" / "main" / "java" / "com" / "jiuxi" / "module"
        self.migration_report = {
            "timestamp": datetime.now().isoformat(),
            "migrated_modules": [],
            "failed_migrations": [],
            "statistics": {
                "total_files_moved": 0,
                "directories_created": 0,
                "directories_deleted": 0
            }
        }
        
    def get_target_ddd_structure(self, module_name: str) -> Dict[str, str]:
        """获取目标DDD结构映射"""
        return {
            # 应用层 (Application Layer)
            "app/service": "app/service",
            "app/impl": "app/impl", 
            "app/assembler": "app/assembler",
            "app/command/handler": "app/command/handler",
            "app/command/dto": "app/command/dto",
            "app/query/handler": "app/query/handler", 
            "app/query/dto": "app/query/dto",
            "app/orchestrator": "app/orchestrator",
            "app/dto": "app/dto",
            
            # 领域层 (Domain Layer)
            "domain/model/aggregate": "domain/model/aggregate",
            "domain/model/entity": "domain/entity",
            "domain/model/vo": "domain/valueobject",
            "domain/event": "domain/event",
            "domain/service": "domain/service", 
            "domain/impl": "domain/impl",
            "domain/repo": "domain/repo",
            "domain/repository": "domain/repo",
            "domain/gateway": "domain/gateway",
            "domain/policy": "domain/policy",
            
            # 基础设施层 (Infrastructure Layer)
            "infra/persistence/entity": "infra/persistence/entity",
            "infra/persistence/mapper": "infra/persistence/mapper",
            "infra/persistence/repo": "infra/persistence/repo",
            "infra/persistence/repository": "infra/persistence/repo",
            "infra/persistence/assembler": "infra/persistence/assembler",
            "infra/gateway": "infra/gateway",
            "infra/cache": "infra/cache",
            "infra/messaging": "infra/messaging",
            
            # 接口适配器层 (Interface Adapters) - 关键重命名
            "interfaces": "intf",
            "interfaces/web": "intf/web",
            "interfaces/web/controller": "intf/web/controller",
            "intf/web/controller": "intf/web/controller",
            "intf/web/dto": "intf/web/dto",
            "intf/web/assembler": "intf/web/assembler"
        }
    
    def find_modules_to_migrate(self) -> List[str]:
        """查找需要迁移的模块"""
        modules = []
        if not self.src_root.exists():
            logging.warning(f"源目录不存在: {self.src_root}")
            return modules
            
        for module_dir in self.src_root.iterdir():
            if module_dir.is_dir() and not module_dir.name.startswith('.'):
                # 检查是否有interfaces目录需要重命名
                interfaces_dir = module_dir / "interfaces"
                intf_dir = module_dir / "intf"
                
                if interfaces_dir.exists() and not intf_dir.exists():
                    modules.append(module_dir.name)
                    logging.info(f"发现需要迁移的模块: {module_dir.name}")
                    
        return modules
    
    def backup_module(self, module_name: str) -> bool:
        """备份模块"""
        try:
            source_dir = self.src_root / module_name
            backup_dir = self.project_root / "backup" / f"{module_name}_{datetime.now().strftime('%Y%m%d_%H%M%S')}"
            
            backup_dir.parent.mkdir(parents=True, exist_ok=True)
            shutil.copytree(source_dir, backup_dir)
            logging.info(f"模块 {module_name} 已备份到: {backup_dir}")
            return True
        except Exception as e:
            logging.error(f"备份模块 {module_name} 失败: {e}")
            return False
    
    def migrate_interfaces_to_intf(self, module_name: str) -> bool:
        """将interfaces目录重命名为intf"""
        try:
            module_dir = self.src_root / module_name
            interfaces_dir = module_dir / "interfaces"
            intf_dir = module_dir / "intf"
            
            if interfaces_dir.exists() and not intf_dir.exists():
                # 重命名interfaces为intf
                shutil.move(str(interfaces_dir), str(intf_dir))
                logging.info(f"模块 {module_name}: interfaces -> intf 重命名成功")
                self.migration_report["statistics"]["directories_created"] += 1
                return True
            elif intf_dir.exists():
                logging.info(f"模块 {module_name}: intf目录已存在，跳过")
                return True
            else:
                logging.warning(f"模块 {module_name}: 未找到interfaces目录")
                return False
                
        except Exception as e:
            logging.error(f"模块 {module_name} interfaces->intf 迁移失败: {e}")
            return False
    
    def create_ddd_directories(self, module_name: str) -> bool:
        """创建标准DDD目录结构"""
        try:
            module_dir = self.src_root / module_name
            
            # 标准DDD目录结构
            ddd_dirs = [
                # 应用层
                "app/service",
                "app/impl", 
                "app/assembler",
                "app/command/handler",
                "app/command/dto", 
                "app/query/handler",
                "app/query/dto",
                "app/orchestrator",
                
                # 领域层
                "domain/model/aggregate",
                "domain/entity", 
                "domain/valueobject",
                "domain/event/publisher",
                "domain/event/subscriber",
                "domain/service",
                "domain/impl",
                "domain/repo",
                "domain/gateway",
                "domain/policy",
                
                # 基础设施层
                "infra/persistence/entity",
                "infra/persistence/mapper",
                "infra/persistence/repo",
                "infra/persistence/assembler",
                "infra/gateway/client",
                "infra/gateway/dto",
                "infra/gateway/assembler",
                "infra/cache/config",
                "infra/cache/strategy",
                "infra/messaging/producer",
                "infra/messaging/consumer",
                "infra/messaging/config",
                
                # 接口适配器层
                "intf/web/controller/command",
                "intf/web/controller/query", 
                "intf/web/dto/request",
                "intf/web/dto/response",
                "intf/web/assembler",
                "intf/web/interceptor",
                "intf/facade/api",
                "intf/facade/impl", 
                "intf/facade/dto",
                "intf/event/listener",
                "intf/event/publisher"
            ]
            
            created_count = 0
            for dir_path in ddd_dirs:
                target_dir = module_dir / dir_path
                if not target_dir.exists():
                    target_dir.mkdir(parents=True, exist_ok=True)
                    
                    # 创建package-info.java文件
                    package_info_path = target_dir / "package-info.java"
                    if not package_info_path.exists():
                        self.create_package_info(package_info_path, module_name, dir_path)
                    
                    created_count += 1
                    
            logging.info(f"模块 {module_name}: 创建了 {created_count} 个DDD目录")
            self.migration_report["statistics"]["directories_created"] += created_count
            return True
            
        except Exception as e:
            logging.error(f"模块 {module_name} 创建DDD目录失败: {e}")
            return False
    
    def create_package_info(self, file_path: Path, module_name: str, dir_path: str):
        """创建package-info.java文件"""
        package_name = f"com.jiuxi.module.{module_name}.{dir_path.replace('/', '.')}"
        layer_name = self.get_layer_description(dir_path)
        
        content = f'''/**
 * {layer_name}
 * <p>
 * DDD {module_name}模块 - {dir_path} 包
 * 遵循领域驱动设计(DDD)分层架构原则
 * </p>
 * 
 * @author DDD重构工具
 * @since 1.0
 */
package {package_name};
'''
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
    
    def get_layer_description(self, dir_path: str) -> str:
        """获取层级描述"""
        if dir_path.startswith("app"):
            return "应用服务层 (Application Layer)"
        elif dir_path.startswith("domain"):
            return "领域层 (Domain Layer)"  
        elif dir_path.startswith("infra"):
            return "基础设施层 (Infrastructure Layer)"
        elif dir_path.startswith("intf"):
            return "接口适配器层 (Interface Adapters)"
        else:
            return "业务层"
    
    def cleanup_empty_directories(self, module_name: str) -> int:
        """清理空目录"""
        module_dir = self.src_root / module_name
        deleted_count = 0
        
        try:
            # 从最深层开始检查
            for root, dirs, files in os.walk(module_dir, topdown=False):
                for dir_name in dirs:
                    dir_path = Path(root) / dir_name
                    try:
                        # 检查目录是否为空（除了可能存在的package-info.java）
                        contents = list(dir_path.iterdir())
                        if not contents or (len(contents) == 1 and contents[0].name == "package-info.java"):
                            # 如果只有package-info.java，也认为是空目录并删除
                            if contents:
                                contents[0].unlink()
                            dir_path.rmdir()
                            logging.info(f"删除空目录: {dir_path}")
                            deleted_count += 1
                    except OSError:
                        # 目录不为空，跳过
                        pass
                        
        except Exception as e:
            logging.error(f"清理空目录时出错: {e}")
            
        self.migration_report["statistics"]["directories_deleted"] += deleted_count
        return deleted_count
    
    def migrate_module(self, module_name: str) -> bool:
        """迁移单个模块"""
        logging.info(f"开始迁移模块: {module_name}")
        
        try:
            # 1. 备份模块
            if not self.backup_module(module_name):
                return False
                
            # 2. interfaces -> intf 重命名
            if not self.migrate_interfaces_to_intf(module_name):
                logging.warning(f"模块 {module_name}: interfaces->intf 迁移有问题，但继续进行")
                
            # 3. 创建标准DDD目录结构
            if not self.create_ddd_directories(module_name):
                return False
                
            # 4. 清理空目录
            deleted_count = self.cleanup_empty_directories(module_name)
            
            # 5. 记录迁移成功
            self.migration_report["migrated_modules"].append({
                "module": module_name,
                "status": "success",
                "deleted_empty_dirs": deleted_count
            })
            
            logging.info(f"模块 {module_name} 迁移完成")
            return True
            
        except Exception as e:
            logging.error(f"模块 {module_name} 迁移失败: {e}")
            self.migration_report["failed_migrations"].append({
                "module": module_name,
                "error": str(e)
            })
            return False
    
    def migrate_all_modules(self) -> bool:
        """迁移所有需要迁移的模块"""
        modules = self.find_modules_to_migrate()
        
        if not modules:
            logging.info("没有找到需要迁移的模块")
            return True
            
        logging.info(f"找到 {len(modules)} 个需要迁移的模块: {modules}")
        
        success_count = 0
        for module in modules:
            if self.migrate_module(module):
                success_count += 1
                
        success_rate = success_count / len(modules) * 100
        logging.info(f"迁移完成: {success_count}/{len(modules)} 成功 ({success_rate:.1f}%)")
        
        # 保存迁移报告
        self.save_migration_report()
        
        return success_count == len(modules)
    
    def save_migration_report(self):
        """保存迁移报告"""
        report_file = self.project_root / "migration-report.json"
        with open(report_file, 'w', encoding='utf-8') as f:
            json.dump(self.migration_report, f, ensure_ascii=False, indent=2)
        logging.info(f"迁移报告已保存: {report_file}")

def main():
    """主函数"""
    # 项目根目录
    project_root = r"D:\keycloak_sb_sso_new0910_claude\ps\ps-be"
    
    print("=" * 60)
    print("DDD包迁移脚本 v1.0")
    print("功能: 批量移动类文件，支持DDD分层架构重构")
    print("=" * 60)
    
    # 创建迁移工具实例
    migration_tool = PackageMigrationTool(project_root)
    
    # 执行迁移
    try:
        success = migration_tool.migrate_all_modules()
        if success:
            print("\n✅ 所有模块迁移成功!")
        else:
            print("\n⚠️ 部分模块迁移失败，请检查日志")
            
    except Exception as e:
        logging.error(f"迁移过程出现错误: {e}")
        print(f"\n❌ 迁移失败: {e}")
        
    print("\n迁移完成，详细信息请查看 migration.log 和 migration-report.json")

if __name__ == "__main__":
    main()