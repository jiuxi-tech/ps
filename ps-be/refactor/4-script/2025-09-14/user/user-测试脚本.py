#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
User模块接口测试脚本

文档生成时间：2025-09-14
模块路径：D:\keycloak_sb_sso_new0910_claude\ps\ps-be\src\main\java\com\jiuxi\module\user
脚本版本：v1.0

使用方法：
1. 安装依赖：pip install requests colorama
2. 配置测试环境参数
3. 运行脚本：python user-测试脚本.py

测试覆盖：
- 现代RESTful用户管理接口
- 账户管理接口
- 传统兼容性接口
- 异常场景测试
"""

import requests
import json
import time
import uuid
from datetime import datetime
from colorama import Fore, Back, Style, init

# 初始化colorama用于彩色输出
init()

class UserApiTester:
    def __init__(self, base_url="http://localhost:8080", jwt_token=""):
        """
        初始化测试器
        
        Args:
            base_url: 应用基础URL
            jwt_token: JWT认证令牌
        """
        self.base_url = base_url.rstrip('/')
        self.jwt_token = jwt_token
        self.session = requests.Session()
        
        # 测试数据配置
        self.test_config = {
            "tenant_id": "TEST_TENANT_001",
            "dept_id": "DEPT_001", 
            "admin_person_id": "ADMIN_001",
            "test_person_id": "PERSON_001",
            "test_username": f"testuser_{int(time.time())}",
            "test_password": "Test123456!",
            "test_email": f"test_{int(time.time())}@jiuxi.com",
            "test_phone": "13800138000",
            "city_code": "320100"
        }
        
        # 设置默认请求头
        self.default_headers = {
            "Content-Type": "application/json",
            "Authorization": f"Bearer {self.jwt_token}",
            "X-Tenant-Id": self.test_config["tenant_id"],
            "X-User-Person-Id": self.test_config["admin_person_id"]
        }
        
        # 测试结果统计
        self.test_results = {
            "total": 0,
            "passed": 0,
            "failed": 0,
            "errors": []
        }
    
    def log_info(self, message):
        """输出信息日志"""
        print(f"{Fore.BLUE}[INFO]{Style.RESET_ALL} {message}")
    
    def log_success(self, message):
        """输出成功日志"""
        print(f"{Fore.GREEN}[PASS]{Style.RESET_ALL} {message}")
    
    def log_error(self, message):
        """输出错误日志"""
        print(f"{Fore.RED}[FAIL]{Style.RESET_ALL} {message}")
    
    def log_warning(self, message):
        """输出警告日志"""
        print(f"{Fore.YELLOW}[WARN]{Style.RESET_ALL} {message}")
    
    def assert_response(self, response, expected_status=200, test_name=""):
        """
        验证响应结果
        
        Args:
            response: HTTP响应对象
            expected_status: 期望的状态码
            test_name: 测试用例名称
        
        Returns:
            bool: 是否通过测试
        """
        self.test_results["total"] += 1
        
        try:
            # 验证状态码
            if response.status_code != expected_status:
                self.log_error(f"{test_name} - 状态码错误: 期望 {expected_status}, 实际 {response.status_code}")
                self.test_results["failed"] += 1
                self.test_results["errors"].append(f"{test_name} - 状态码错误")
                return False
            
            # 验证响应内容
            try:
                response_data = response.json()
                
                # 检查基本响应格式
                if "success" in response_data:
                    if response_data.get("success"):
                        self.log_success(f"{test_name} - 测试通过")
                        self.test_results["passed"] += 1
                        return True
                    else:
                        self.log_error(f"{test_name} - 业务失败: {response_data.get('message', '未知错误')}")
                        self.test_results["failed"] += 1
                        self.test_results["errors"].append(f"{test_name} - 业务失败")
                        return False
                else:
                    self.log_success(f"{test_name} - 测试通过")
                    self.test_results["passed"] += 1
                    return True
                    
            except json.JSONDecodeError:
                self.log_error(f"{test_name} - 响应不是有效的JSON格式")
                self.test_results["failed"] += 1
                self.test_results["errors"].append(f"{test_name} - JSON解析错误")
                return False
                
        except Exception as e:
            self.log_error(f"{test_name} - 异常: {str(e)}")
            self.test_results["failed"] += 1
            self.test_results["errors"].append(f"{test_name} - 异常: {str(e)}")
            return False
    
    def test_get_current_user_info(self):
        """测试获取当前用户信息接口"""
        self.log_info("测试用例 1.1：获取当前用户信息")
        
        headers = {
            **self.default_headers,
            "X-User-Dept-Id": self.test_config["dept_id"],
            "X-User-Person-Id": self.test_config["test_person_id"],
            "X-User-Role-Ids": "ROLE_001,ROLE_002",
            "X-User-City-Code": self.test_config["city_code"]
        }
        
        try:
            response = self.session.get(
                f"{self.base_url}/api/v1/users/me",
                headers=headers
            )
            self.assert_response(response, 200, "获取当前用户信息")
            
            if response.status_code == 200:
                data = response.json()
                self.log_info(f"返回用户信息: {json.dumps(data, ensure_ascii=False, indent=2)}")
                
        except requests.RequestException as e:
            self.log_error(f"获取当前用户信息 - 网络错误: {str(e)}")
            self.test_results["total"] += 1
            self.test_results["failed"] += 1
    
    def test_create_user(self):
        """测试创建用户接口"""
        self.log_info("测试用例 2.1：创建用户")
        
        user_data = {
            "username": self.test_config["test_username"],
            "password": self.test_config["test_password"],
            "personName": "测试用户001",
            "phone": self.test_config["test_phone"],
            "email": self.test_config["test_email"],
            "deptId": self.test_config["dept_id"],
            "idCard": "320101199001011111"
        }
        
        try:
            response = self.session.post(
                f"{self.base_url}/api/v1/users",
                headers=self.default_headers,
                json=user_data
            )
            
            success = self.assert_response(response, 200, "创建用户")
            
            if success and response.status_code == 200:
                data = response.json()
                if data.get("success") and data.get("data"):
                    created_person_id = data["data"]
                    self.test_config["created_person_id"] = created_person_id
                    self.log_info(f"创建的用户ID: {created_person_id}")
                
        except requests.RequestException as e:
            self.log_error(f"创建用户 - 网络错误: {str(e)}")
            self.test_results["total"] += 1
            self.test_results["failed"] += 1
    
    def test_update_user(self):
        """测试更新用户接口"""
        if not hasattr(self, 'test_config') or not self.test_config.get("created_person_id"):
            self.log_warning("跳过更新用户测试 - 缺少已创建的用户ID")
            return
        
        self.log_info("测试用例 3.1：更新用户信息")
        
        person_id = self.test_config["created_person_id"]
        update_data = {
            "personId": person_id,
            "personName": "更新后的测试用户",
            "phone": "13900139002",
            "email": f"updated_{int(time.time())}@jiuxi.com"
        }
        
        try:
            response = self.session.put(
                f"{self.base_url}/api/v1/users/{person_id}",
                headers=self.default_headers,
                json=update_data
            )
            self.assert_response(response, 200, "更新用户信息")
            
        except requests.RequestException as e:
            self.log_error(f"更新用户信息 - 网络错误: {str(e)}")
            self.test_results["total"] += 1
            self.test_results["failed"] += 1
    
    def test_get_user_by_id(self):
        """测试根据ID查询用户"""
        if not self.test_config.get("created_person_id"):
            self.log_warning("跳过查询用户测试 - 缺少已创建的用户ID")
            return
        
        self.log_info("测试用例 4.1：根据用户ID查询")
        
        person_id = self.test_config["created_person_id"]
        
        try:
            response = self.session.get(
                f"{self.base_url}/api/v1/users/{person_id}",
                headers=self.default_headers
            )
            self.assert_response(response, 200, "根据用户ID查询")
            
        except requests.RequestException as e:
            self.log_error(f"根据用户ID查询 - 网络错误: {str(e)}")
            self.test_results["total"] += 1
            self.test_results["failed"] += 1
    
    def test_get_user_by_username(self):
        """测试根据用户名查询用户"""
        self.log_info("测试用例 4.2：根据用户名查询")
        
        username = self.test_config["test_username"]
        
        try:
            response = self.session.get(
                f"{self.base_url}/api/v1/users/username/{username}",
                headers=self.default_headers
            )
            self.assert_response(response, 200, "根据用户名查询")
            
        except requests.RequestException as e:
            self.log_error(f"根据用户名查询 - 网络错误: {str(e)}")
            self.test_results["total"] += 1
            self.test_results["failed"] += 1
    
    def test_search_users(self):
        """测试分页查询用户列表"""
        self.log_info("测试用例 4.3：分页查询用户列表")
        
        search_data = {
            "tenantId": self.test_config["tenant_id"],
            "deptId": self.test_config["dept_id"],
            "personName": "测试",
            "current": 1,
            "size": 10
        }
        
        try:
            response = self.session.post(
                f"{self.base_url}/api/v1/users/search",
                headers=self.default_headers,
                json=search_data
            )
            
            success = self.assert_response(response, 200, "分页查询用户列表")
            
            if success and response.status_code == 200:
                data = response.json()
                if data.get("success") and data.get("data"):
                    result = data["data"]
                    self.log_info(f"查询结果 - 总数: {result.get('total', 0)}, 当前页: {result.get('current', 1)}")
                
        except requests.RequestException as e:
            self.log_error(f"分页查询用户列表 - 网络错误: {str(e)}")
            self.test_results["total"] += 1
            self.test_results["failed"] += 1
    
    def test_activate_user(self):
        """测试激活用户"""
        if not self.test_config.get("created_person_id"):
            self.log_warning("跳过激活用户测试 - 缺少已创建的用户ID")
            return
        
        self.log_info("测试用例 6.1：激活用户")
        
        person_id = self.test_config["created_person_id"]
        
        try:
            response = self.session.put(
                f"{self.base_url}/api/v1/users/{person_id}/activate",
                headers=self.default_headers
            )
            self.assert_response(response, 200, "激活用户")
            
        except requests.RequestException as e:
            self.log_error(f"激活用户 - 网络错误: {str(e)}")
            self.test_results["total"] += 1
            self.test_results["failed"] += 1
    
    def test_deactivate_user(self):
        """测试停用用户"""
        if not self.test_config.get("created_person_id"):
            self.log_warning("跳过停用用户测试 - 缺少已创建的用户ID")
            return
        
        self.log_info("测试用例 6.2：停用用户")
        
        person_id = self.test_config["created_person_id"]
        
        try:
            response = self.session.put(
                f"{self.base_url}/api/v1/users/{person_id}/deactivate",
                headers=self.default_headers
            )
            self.assert_response(response, 200, "停用用户")
            
        except requests.RequestException as e:
            self.log_error(f"停用用户 - 网络错误: {str(e)}")
            self.test_results["total"] += 1
            self.test_results["failed"] += 1
    
    def test_reset_password(self):
        """测试重置密码"""
        if not self.test_config.get("created_person_id"):
            self.log_warning("跳过重置密码测试 - 缺少已创建的用户ID")
            return
        
        self.log_info("测试用例 7.1：管理员重置密码")
        
        person_id = self.test_config["created_person_id"]
        new_password = "NewPassword123!"
        
        try:
            response = self.session.put(
                f"{self.base_url}/api/v1/user-accounts/{person_id}/reset-password",
                headers=self.default_headers,
                params={"newPassword": new_password}
            )
            self.assert_response(response, 200, "重置密码")
            
        except requests.RequestException as e:
            self.log_error(f"重置密码 - 网络错误: {str(e)}")
            self.test_results["total"] += 1
            self.test_results["failed"] += 1
    
    def test_change_password(self):
        """测试用户自助修改密码"""
        self.log_info("测试用例 7.2：用户自助修改密码")
        
        password_data = {
            "newPassword": "NewUserPass123!",
            "confirmPassword": "NewUserPass123!"
        }
        
        headers = {
            **self.default_headers,
            "X-User-Person-Id": self.test_config["test_person_id"]
        }
        
        try:
            response = self.session.put(
                f"{self.base_url}/api/v1/user-accounts/change-password",
                headers=headers,
                json=password_data
            )
            self.assert_response(response, 200, "用户自助修改密码")
            
        except requests.RequestException as e:
            self.log_error(f"用户自助修改密码 - 网络错误: {str(e)}")
            self.test_results["total"] += 1
            self.test_results["failed"] += 1
    
    def test_lock_account(self):
        """测试锁定账户"""
        if not self.test_config.get("created_person_id"):
            self.log_warning("跳过锁定账户测试 - 缺少已创建的用户ID")
            return
        
        self.log_info("测试用例 8.1：锁定账户")
        
        person_id = self.test_config["created_person_id"]
        
        try:
            # 锁定账户
            response = self.session.put(
                f"{self.base_url}/api/v1/user-accounts/{person_id}/lock",
                headers=self.default_headers,
                params={"locked": "true"}
            )
            self.assert_response(response, 200, "锁定账户")
            
            # 解锁账户
            response = self.session.put(
                f"{self.base_url}/api/v1/user-accounts/{person_id}/lock",
                headers=self.default_headers,
                params={"locked": "false"}
            )
            self.assert_response(response, 200, "解锁账户")
            
        except requests.RequestException as e:
            self.log_error(f"账户锁定/解锁 - 网络错误: {str(e)}")
            self.test_results["total"] += 2
            self.test_results["failed"] += 2
    
    def test_traditional_api_compatibility(self):
        """测试传统API兼容性"""
        self.log_info("测试用例 9.1：传统用户信息接口兼容性")
        
        params = {
            "jwtdid": self.test_config["dept_id"],
            "jwtpid": self.test_config["test_person_id"],
            "jwtrids": "ROLE_001,ROLE_002",
            "jwtCityCode": self.test_config["city_code"]
        }
        
        try:
            response = self.session.post(
                f"{self.base_url}/sys/person/getUserInfo",
                headers=self.default_headers,
                data=params  # 使用form data而不是JSON
            )
            self.assert_response(response, 200, "传统用户信息接口")
            
        except requests.RequestException as e:
            self.log_error(f"传统用户信息接口 - 网络错误: {str(e)}")
            self.test_results["total"] += 1
            self.test_results["failed"] += 1
    
    def test_error_scenarios(self):
        """测试异常场景"""
        self.log_info("测试用例 11.1：异常场景测试")
        
        # 测试访问不存在的用户
        try:
            response = self.session.get(
                f"{self.base_url}/api/v1/users/NON_EXISTENT_USER",
                headers=self.default_headers
            )
            # 期望返回404或业务错误
            if response.status_code in [404, 200]:
                if response.status_code == 200:
                    data = response.json()
                    if not data.get("success"):
                        self.log_success("异常场景 - 用户不存在处理正确")
                        self.test_results["total"] += 1
                        self.test_results["passed"] += 1
                    else:
                        self.log_error("异常场景 - 用户不存在但返回成功")
                        self.test_results["total"] += 1
                        self.test_results["failed"] += 1
                else:
                    self.log_success("异常场景 - 用户不存在返回404")
                    self.test_results["total"] += 1
                    self.test_results["passed"] += 1
            else:
                self.log_error(f"异常场景 - 预期404或200，实际{response.status_code}")
                self.test_results["total"] += 1
                self.test_results["failed"] += 1
                
        except requests.RequestException as e:
            self.log_error(f"异常场景测试 - 网络错误: {str(e)}")
            self.test_results["total"] += 1
            self.test_results["failed"] += 1
    
    def test_cleanup(self):
        """清理测试数据"""
        if not self.test_config.get("created_person_id"):
            self.log_info("无需清理 - 没有创建测试用户")
            return
        
        self.log_info("清理测试数据：删除创建的测试用户")
        
        person_id = self.test_config["created_person_id"]
        
        try:
            response = self.session.delete(
                f"{self.base_url}/api/v1/users/{person_id}",
                headers=self.default_headers
            )
            
            if response.status_code == 200:
                self.log_success("测试数据清理成功")
            else:
                self.log_warning(f"测试数据清理失败，状态码: {response.status_code}")
                
        except requests.RequestException as e:
            self.log_warning(f"测试数据清理 - 网络错误: {str(e)}")
    
    def run_all_tests(self):
        """运行所有测试用例"""
        start_time = datetime.now()
        
        print(f"\n{Fore.CYAN}{'='*50}")
        print(f"🧪 开始执行User模块接口测试")
        print(f"📅 测试时间: {start_time.strftime('%Y-%m-%d %H:%M:%S')}")
        print(f"🔗 测试环境: {self.base_url}")
        print(f"{'='*50}{Style.RESET_ALL}\n")
        
        # 执行测试用例
        test_methods = [
            self.test_get_current_user_info,
            self.test_create_user,
            self.test_update_user,
            self.test_get_user_by_id,
            self.test_get_user_by_username,
            self.test_search_users,
            self.test_activate_user,
            self.test_deactivate_user,
            self.test_reset_password,
            self.test_change_password,
            self.test_lock_account,
            self.test_traditional_api_compatibility,
            self.test_error_scenarios
        ]
        
        for test_method in test_methods:
            try:
                test_method()
                time.sleep(0.5)  # 短暂延迟避免请求过快
            except Exception as e:
                self.log_error(f"{test_method.__name__} - 执行异常: {str(e)}")
                self.test_results["total"] += 1
                self.test_results["failed"] += 1
        
        # 清理测试数据
        self.test_cleanup()
        
        # 输出测试报告
        end_time = datetime.now()
        duration = (end_time - start_time).total_seconds()
        
        print(f"\n{Fore.CYAN}{'='*50}")
        print("📊 测试结果统计")
        print(f"{'='*50}{Style.RESET_ALL}")
        print(f"⏱️  总耗时: {duration:.2f}秒")
        print(f"📈 总测试数: {self.test_results['total']}")
        print(f"{Fore.GREEN}✅ 通过数: {self.test_results['passed']}{Style.RESET_ALL}")
        print(f"{Fore.RED}❌ 失败数: {self.test_results['failed']}{Style.RESET_ALL}")
        
        if self.test_results["total"] > 0:
            success_rate = (self.test_results["passed"] / self.test_results["total"]) * 100
            print(f"📊 通过率: {success_rate:.1f}%")
        
        # 输出失败详情
        if self.test_results["errors"]:
            print(f"\n{Fore.RED}❌ 失败详情:")
            for i, error in enumerate(self.test_results["errors"], 1):
                print(f"   {i}. {error}")
            print(f"{Style.RESET_ALL}")
        
        # 总体结论
        if self.test_results["failed"] == 0:
            print(f"{Fore.GREEN}🎉 所有测试通过！{Style.RESET_ALL}")
        else:
            print(f"{Fore.YELLOW}⚠️  部分测试失败，需要检查接口实现{Style.RESET_ALL}")


def main():
    """主函数"""
    print("🚀 User模块接口测试脚本")
    print("=" * 50)
    
    # 测试配置（需要根据实际环境调整）
    BASE_URL = "http://localhost:8080"
    JWT_TOKEN = "your_jwt_token_here"  # 替换为实际的JWT Token
    
    # 询问用户是否配置测试环境
    print("⚠️  请确保已配置以下测试环境参数：")
    print(f"   - 应用地址: {BASE_URL}")
    print(f"   - JWT Token: {'已配置' if JWT_TOKEN != 'your_jwt_token_here' else '❌未配置'}")
    print()
    
    if JWT_TOKEN == "your_jwt_token_here":
        print("❌ 请先在脚本中配置有效的JWT_TOKEN")
        return
    
    # 询问是否继续
    user_input = input("🤔 是否继续执行测试？(y/N): ").strip().lower()
    if user_input not in ['y', 'yes']:
        print("👋 测试取消")
        return
    
    # 创建测试器并执行测试
    tester = UserApiTester(base_url=BASE_URL, jwt_token=JWT_TOKEN)
    
    try:
        tester.run_all_tests()
    except KeyboardInterrupt:
        print(f"\n{Fore.YELLOW}⚠️  测试被用户中断{Style.RESET_ALL}")
    except Exception as e:
        print(f"\n{Fore.RED}💥 测试执行出现异常: {str(e)}{Style.RESET_ALL}")


if __name__ == "__main__":
    main()