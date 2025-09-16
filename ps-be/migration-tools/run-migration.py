#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
DDD迁移工具统一执行脚本
功能：按顺序执行所有迁移工具，提供完整的重构流程
作者：DDD重构工具
版本：1.0
创建时间：2025-09-16
"""

import sys
import os
import subprocess
import time
from pathlib import Path
from typing import Dict, List, Tuple
import logging
import json
from datetime import datetime

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler('migration-execution.log', encoding='utf-8'),
        logging.StreamHandler()
    ]
)

class MigrationExecutor:
    """迁移执行器"""
    
    def __init__(self, project_root: str):
        self.project_root = Path(project_root)
        self.tools_dir = self.project_root / "migration-tools"
        
        # 工具执行顺序
        self.execution_order = [
            {
                "name": "package_migration",
                "script": "package-migration-script.py",
                "description": "1. 包迁移脚本 - 创建DDD目录结构",
                "required": True,
                "timeout": 300
            },
            {
                "name": "import_update", 
                "script": "import-update-tool.py",
                "description": "2. Import更新工具 - 修正包路径引用",
                "required": True,
                "timeout": 600
            },
            {
                "name": "dependency_check",
                "script": "dependency-checker.py", 
                "description": "3. 依赖检查工具 - 验证架构合规性",
                "required": False,
                "timeout": 300
            }
        ]
        
        self.execution_report = {
            "timestamp": datetime.now().isoformat(),
            "project_root": str(project_root),
            "execution_results": {},
            "overall_status": "UNKNOWN",
            "total_execution_time": 0,
            "summary": {
                "total_tools": len(self.execution_order),
                "successful_tools": 0,
                "failed_tools": 0,
                "skipped_tools": 0
            }
        }
    
    def check_prerequisites(self) -> bool:
        """检查执行前提条件"""
        print("🔍 检查执行前提条件...")
        
        # 检查项目目录
        if not self.project_root.exists():
            logging.error(f"项目目录不存在: {self.project_root}")
            return False
            
        # 检查工具目录
        if not self.tools_dir.exists():
            logging.error(f"工具目录不存在: {self.tools_dir}")
            return False
            
        # 检查所有工具脚本
        missing_tools = []
        for tool in self.execution_order:
            script_path = self.tools_dir / tool["script"]
            if not script_path.exists():
                missing_tools.append(tool["script"])
        
        if missing_tools:
            logging.error(f"缺少工具脚本: {missing_tools}")
            return False
            
        # 检查Python版本
        if sys.version_info < (3, 6):
            logging.error(f"Python版本过低: {sys.version}, 需要Python 3.6+")
            return False
            
        print("✅ 前提条件检查通过")
        return True
    
    def create_backup(self) -> bool:
        """创建全项目备份"""
        print("💾 创建项目备份...")
        
        try:
            backup_dir = self.project_root / "backup" / f"full-backup-{datetime.now().strftime('%Y%m%d_%H%M%S')}"
            backup_dir.parent.mkdir(parents=True, exist_ok=True)
            
            # 备份src目录
            src_dir = self.project_root / "src"
            if src_dir.exists():
                import shutil
                shutil.copytree(src_dir, backup_dir / "src")
                logging.info(f"项目备份创建成功: {backup_dir}")
                print(f"✅ 备份已创建: {backup_dir}")
                return True
            else:
                logging.warning("src目录不存在，跳过备份")
                return True
                
        except Exception as e:
            logging.error(f"创建备份失败: {e}")
            return False
    
    def execute_tool(self, tool_config: Dict) -> Tuple[bool, Dict]:
        """执行单个工具"""
        tool_name = tool_config["name"]
        script_name = tool_config["script"]
        description = tool_config["description"]
        timeout = tool_config.get("timeout", 300)
        
        print(f"\n🔧 {description}")
        logging.info(f"开始执行工具: {tool_name}")
        
        execution_result = {
            "tool_name": tool_name,
            "script_name": script_name,
            "start_time": datetime.now().isoformat(),
            "success": False,
            "execution_time": 0,
            "return_code": -1,
            "stdout": "",
            "stderr": "",
            "error_message": ""
        }
        
        try:
            start_time = time.time()
            script_path = self.tools_dir / script_name
            
            # 切换到项目目录执行
            original_cwd = os.getcwd()
            os.chdir(self.project_root)
            
            try:
                # 执行工具
                result = subprocess.run([
                    sys.executable, str(script_path)
                ], capture_output=True, text=True, timeout=timeout)
                
                execution_result["return_code"] = result.returncode
                execution_result["stdout"] = result.stdout
                execution_result["stderr"] = result.stderr
                execution_result["success"] = (result.returncode == 0)
                
                if execution_result["success"]:
                    print(f"   ✅ {tool_name} 执行成功")
                    logging.info(f"工具执行成功: {tool_name}")
                else:
                    print(f"   ❌ {tool_name} 执行失败 (返回码: {result.returncode})")
                    logging.error(f"工具执行失败: {tool_name}, 返回码: {result.returncode}")
                    if result.stderr:
                        logging.error(f"错误输出: {result.stderr}")
                
            finally:
                os.chdir(original_cwd)
                
            execution_result["execution_time"] = time.time() - start_time
            execution_result["end_time"] = datetime.now().isoformat()
            
        except subprocess.TimeoutExpired:
            execution_result["error_message"] = f"工具执行超时 (>{timeout}秒)"
            print(f"   ⏰ {tool_name} 执行超时")
            logging.error(f"工具执行超时: {tool_name}")
        except Exception as e:
            execution_result["error_message"] = str(e)
            print(f"   ❌ {tool_name} 执行异常: {e}")
            logging.error(f"工具执行异常: {tool_name}: {e}")
        
        return execution_result["success"], execution_result
    
    def execute_migration(self, skip_optional: bool = False) -> bool:
        """执行完整迁移流程"""
        print(f"\n🚀 开始执行DDD迁移流程...")
        print(f"   项目路径: {self.project_root}")
        print(f"   跳过可选工具: {skip_optional}")
        
        start_time = time.time()
        overall_success = True
        
        for tool_config in self.execution_order:
            tool_name = tool_config["name"]
            is_required = tool_config["required"]
            
            # 跳过可选工具
            if skip_optional and not is_required:
                print(f"\n⏭️ 跳过可选工具: {tool_config['description']}")
                self.execution_report["execution_results"][tool_name] = {
                    "tool_name": tool_name,
                    "status": "SKIPPED",
                    "reason": "用户选择跳过可选工具"
                }
                self.execution_report["summary"]["skipped_tools"] += 1
                continue
            
            # 执行工具
            success, result = self.execute_tool(tool_config)
            self.execution_report["execution_results"][tool_name] = result
            
            if success:
                self.execution_report["summary"]["successful_tools"] += 1
            else:
                self.execution_report["summary"]["failed_tools"] += 1
                
                if is_required:
                    print(f"\n⚠️ 必需工具 {tool_name} 执行失败，终止迁移流程")
                    overall_success = False
                    break
                else:
                    print(f"\n⚠️ 可选工具 {tool_name} 执行失败，继续执行")
        
        self.execution_report["total_execution_time"] = time.time() - start_time
        self.execution_report["overall_status"] = "SUCCESS" if overall_success else "FAILED"
        
        return overall_success
    
    def generate_execution_summary(self):
        """生成执行摘要"""
        print("\n" + "="*60)
        print("DDD迁移执行摘要")
        print("="*60)
        
        print(f"执行时间: {self.execution_report['timestamp']}")
        print(f"项目路径: {self.execution_report['project_root']}")
        print(f"总体状态: {self.execution_report['overall_status']}")
        print(f"总执行时间: {self.execution_report['total_execution_time']:.2f}秒")
        
        summary = self.execution_report["summary"]
        print(f"\n📊 执行统计:")
        print(f"   总工具数: {summary['total_tools']}")
        print(f"   成功: {summary['successful_tools']}")
        print(f"   失败: {summary['failed_tools']}")
        print(f"   跳过: {summary['skipped_tools']}")
        
        print(f"\n📋 工具执行详情:")
        for tool_name, result in self.execution_report["execution_results"].items():
            if result.get("status") == "SKIPPED":
                print(f"   ⏭️ {tool_name}: 已跳过 - {result.get('reason', '')}")
            elif result.get("success", False):
                exec_time = result.get("execution_time", 0)
                print(f"   ✅ {tool_name}: 成功 (耗时: {exec_time:.2f}s)")
            else:
                error_msg = result.get("error_message", "未知错误")
                print(f"   ❌ {tool_name}: 失败 - {error_msg}")
        
        # 生成文件检查报告
        print(f"\n📁 生成的文件:")
        output_files = [
            "migration.log", "migration-report.json",
            "import-update.log", "import-update-report.json",
            "dependency-check.log", "dependency-check-report.json"
        ]
        
        for file_name in output_files:
            file_path = self.project_root / file_name
            if file_path.exists():
                file_size = file_path.stat().st_size
                print(f"   ✅ {file_name} ({file_size} bytes)")
            else:
                print(f"   ❌ {file_name} (未生成)")
        
        # 建议
        print(f"\n💡 后续建议:")
        if self.execution_report["overall_status"] == "SUCCESS":
            print("   1. 检查生成的报告文件，确认迁移结果")
            print("   2. 运行编译测试: mvn clean compile")
            print("   3. 运行单元测试验证功能完整性")
            print("   4. 提交代码前进行代码审查")
        else:
            print("   1. 查看详细错误日志排查问题")
            print("   2. 修复失败的工具后重新运行")
            print("   3. 如需帮助请查看工具文档")
    
    def save_execution_report(self):
        """保存执行报告"""
        report_file = self.project_root / "migration-execution-report.json"
        with open(report_file, 'w', encoding='utf-8') as f:
            json.dump(self.execution_report, f, ensure_ascii=False, indent=2)
        logging.info(f"执行报告已保存: {report_file}")

def main():
    """主函数"""
    # 项目根目录  
    project_root = r"D:\keycloak_sb_sso_new0910_claude\ps\ps-be"
    
    print("=" * 60)
    print("DDD迁移工具统一执行脚本 v1.0")
    print("功能: 按顺序执行所有迁移工具，提供完整的重构流程")
    print("=" * 60)
    
    # 解析命令行参数
    skip_optional = "--skip-optional" in sys.argv
    no_backup = "--no-backup" in sys.argv
    
    # 创建执行器
    executor = MigrationExecutor(project_root)
    
    try:
        # 1. 检查前提条件
        if not executor.check_prerequisites():
            print("❌ 前提条件检查失败，无法继续执行")
            return
        
        # 2. 用户确认
        print(f"\n📋 即将执行的工具:")
        for i, tool in enumerate(executor.execution_order, 1):
            required_mark = "[必需]" if tool["required"] else "[可选]"
            skip_mark = " (将跳过)" if skip_optional and not tool["required"] else ""
            print(f"   {i}. {tool['description']} {required_mark}{skip_mark}")
        
        print(f"\n⚠️ 注意事项:")
        print(f"   - 迁移过程会修改项目文件")
        print(f"   - 建议在干净的Git工作树中执行")
        print(f"   - 备份: {'跳过' if no_backup else '将自动创建'}")
        
        user_input = input(f"\n是否继续执行迁移? (y/N): ").lower()
        if user_input != 'y':
            print("迁移取消。")
            return
        
        # 3. 创建备份
        if not no_backup:
            if not executor.create_backup():
                user_input = input("备份失败，是否继续执行迁移? (y/N): ").lower()
                if user_input != 'y':
                    print("迁移取消。")
                    return
        
        # 4. 执行迁移
        success = executor.execute_migration(skip_optional=skip_optional)
        
        # 5. 生成报告
        executor.generate_execution_summary()
        executor.save_execution_report()
        
        if success:
            print(f"\n🎉 DDD迁移流程执行完成！")
        else:
            print(f"\n⚠️ 迁移过程中出现问题，请查看日志进行排查。")
        
    except KeyboardInterrupt:
        print(f"\n⏹️ 用户中断执行")
        logging.info("用户中断执行")
    except Exception as e:
        print(f"\n❌ 迁移执行失败: {e}")
        logging.error(f"迁移执行失败: {e}")
    
    print(f"\n详细信息请查看 migration-execution.log 和 migration-execution-report.json")

if __name__ == "__main__":
    main()