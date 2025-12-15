#!/bin/bash
############################################################
# PS-BE 生产环境配置验证脚本
# 用于验证配置文件的完整性和正确性
############################################################

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 配置文件路径
CONFIG_FILE="${1:-/opt/ps/config/application-prod.yml}"

# 检查结果计数
PASS_COUNT=0
FAIL_COUNT=0
WARN_COUNT=0

echo -e "${BLUE}============================================${NC}"
echo -e "${BLUE}  PS-BE 生产环境配置验证${NC}"
echo -e "${BLUE}============================================${NC}"
echo ""
echo -e "${YELLOW}配置文件: $CONFIG_FILE${NC}"
echo ""

# 检查函数
check_pass() {
    echo -e "${GREEN}✓${NC} $1"
    ((PASS_COUNT++))
}

check_fail() {
    echo -e "${RED}✗${NC} $1"
    ((FAIL_COUNT++))
}

check_warn() {
    echo -e "${YELLOW}⚠${NC} $1"
    ((WARN_COUNT++))
}

# 1. 验证文件存在
echo -e "${BLUE}[1/10] 检查配置文件是否存在${NC}"
if [ -f "$CONFIG_FILE" ]; then
    check_pass "配置文件存在"
else
    check_fail "配置文件不存在: $CONFIG_FILE"
    echo ""
    echo -e "${RED}错误：配置文件不存在，请先创建配置文件${NC}"
    exit 1
fi
echo ""

# 2. 验证文件权限
echo -e "${BLUE}[2/10] 检查文件权限${NC}"
FILE_PERM=$(stat -c "%a" "$CONFIG_FILE" 2>/dev/null || stat -f "%A" "$CONFIG_FILE" 2>/dev/null)
if [ "$FILE_PERM" = "600" ]; then
    check_pass "文件权限正确 (600)"
else
    check_fail "文件权限不正确 ($FILE_PERM)，应该是 600"
    echo -e "   ${YELLOW}修复命令: chmod 600 $CONFIG_FILE${NC}"
fi

FILE_OWNER=$(stat -c "%U" "$CONFIG_FILE" 2>/dev/null || stat -f "%Su" "$CONFIG_FILE" 2>/dev/null)
if [ "$FILE_OWNER" = "root" ]; then
    check_pass "文件所有者正确 (root)"
else
    check_warn "文件所有者不是 root ($FILE_OWNER)"
    echo -e "   ${YELLOW}建议命令: chown root:root $CONFIG_FILE${NC}"
fi
echo ""

# 3. 验证 YAML 语法
echo -e "${BLUE}[3/10] 检查 YAML 语法${NC}"
if command -v python3 &> /dev/null; then
    if python3 -c "import yaml; yaml.safe_load(open('$CONFIG_FILE'))" 2>/dev/null; then
        check_pass "YAML 语法正确"
    else
        check_fail "YAML 语法错误"
        echo -e "   ${RED}请检查配置文件的 YAML 格式${NC}"
    fi
else
    check_warn "未安装 Python3，跳过 YAML 语法检查"
    echo -e "   ${YELLOW}建议安装 Python3: apt-get install python3${NC}"
fi
echo ""

# 4. 检查 TODO 标记
echo -e "${BLUE}[4/10] 检查未完成的 TODO 项${NC}"
TODO_COUNT=$(grep -c "TODO" "$CONFIG_FILE" 2>/dev/null || echo "0")
if [ "$TODO_COUNT" -eq 0 ]; then
    check_pass "没有未完成的 TODO 项"
else
    check_fail "发现 $TODO_COUNT 个未完成的 TODO 项"
    echo -e "   ${RED}请修改以下配置项:${NC}"
    grep -n "TODO" "$CONFIG_FILE" | while read -r line; do
        echo -e "   ${YELLOW}$line${NC}"
    done
fi
echo ""

# 5. 检查示例值
echo -e "${BLUE}[5/10] 检查示例密码和密钥${NC}"
EXAMPLE_VALUES=(
    "YourActual"
    "example.com"
    "your-monitor-server"
)

EXAMPLE_FOUND=0
for value in "${EXAMPLE_VALUES[@]}"; do
    if grep -q "$value" "$CONFIG_FILE"; then
        check_fail "发现示例值: $value"
        ((EXAMPLE_FOUND++))
    fi
done

if [ "$EXAMPLE_FOUND" -eq 0 ]; then
    check_pass "没有发现示例值"
else
    echo -e "   ${RED}请将示例值替换为实际值${NC}"
fi
echo ""

# 6. 检查必需配置项
echo -e "${BLUE}[6/10] 检查必需配置项${NC}"
REQUIRED_KEYS=(
    "server.port"
    "app.database.datasource.url"
    "app.database.datasource.username"
    "app.database.datasource.password"
    "app.cache.redis.host"
    "app.cache.redis.password"
    "app.security.keycloak.server-url"
    "app.security.keycloak.sso.client-secret"
    "app.security.jwt.secret"
    "spring.datasource.url"
    "spring.redis.host"
)

MISSING_KEYS=0
for key in "${REQUIRED_KEYS[@]}"; do
    # 将点号转换为 YAML 层级检查（简化版）
    KEY_SIMPLE=$(echo "$key" | sed 's/\..*$//')
    if grep -q "$KEY_SIMPLE:" "$CONFIG_FILE"; then
        :  # 键存在，不输出
    else
        check_fail "缺少必需配置: $key"
        ((MISSING_KEYS++))
    fi
done

if [ "$MISSING_KEYS" -eq 0 ]; then
    check_pass "所有必需配置项都存在"
fi
echo ""

# 7. 检查密码强度
echo -e "${BLUE}[7/10] 检查密码强度${NC}"

# 检查数据库密码
DB_PASSWORD=$(grep -A 2 "app:" "$CONFIG_FILE" | grep -A 50 "database:" | grep "password:" | head -1 | awk '{print $2}')
if [ -n "$DB_PASSWORD" ] && [ "$DB_PASSWORD" != "YourActualDatabasePassword" ]; then
    if [ ${#DB_PASSWORD} -ge 8 ]; then
        check_pass "数据库密码长度符合要求 (>= 8)"
    else
        check_warn "数据库密码长度不足 (< 8)"
    fi
else
    check_fail "数据库密码未设置或使用示例值"
fi

# 检查 Redis 密码
REDIS_PASSWORD=$(grep -A 2 "redis:" "$CONFIG_FILE" | grep "password:" | head -1 | awk '{print $2}')
if [ -n "$REDIS_PASSWORD" ] && [ "$REDIS_PASSWORD" != "YourActualRedisPassword" ]; then
    if [ ${#REDIS_PASSWORD} -ge 8 ]; then
        check_pass "Redis 密码长度符合要求 (>= 8)"
    else
        check_warn "Redis 密码长度不足 (< 8)"
    fi
else
    check_fail "Redis 密码未设置或使用示例值"
fi
echo ""

# 8. 检查安全配置
echo -e "${BLUE}[8/10] 检查安全配置${NC}"

# 检查 Druid 监控是否禁用
if grep -q "stat-view-servlet:" "$CONFIG_FILE"; then
    if grep -A 1 "stat-view-servlet:" "$CONFIG_FILE" | grep -q "enabled: false"; then
        check_pass "Druid 监控界面已禁用"
    else
        check_fail "Druid 监控界面未禁用"
    fi
fi

# 检查 SQL 防火墙是否启用
if grep -q "wall:" "$CONFIG_FILE"; then
    if grep -A 2 "wall:" "$CONFIG_FILE" | grep -q "enabled: true"; then
        check_pass "SQL 防火墙已启用"
    else
        check_warn "SQL 防火墙未启用"
    fi
fi

# 检查 DELETE 操作是否禁用
if grep -q "delete-allow:" "$CONFIG_FILE"; then
    if grep "delete-allow:" "$CONFIG_FILE" | grep -q "false"; then
        check_pass "DELETE 操作已禁用"
    else
        check_warn "DELETE 操作未禁用"
    fi
fi

# 检查 DevTools 是否禁用
if grep -q "devtools:" "$CONFIG_FILE"; then
    if grep -A 2 "devtools:" "$CONFIG_FILE" | grep -q "enabled: false"; then
        check_pass "DevTools 已禁用"
    else
        check_warn "DevTools 未禁用"
    fi
fi
echo ""

# 9. 检查日志配置
echo -e "${BLUE}[9/10] 检查日志配置${NC}"

# 检查日志级别
if grep -q "root: WARN" "$CONFIG_FILE"; then
    check_pass "根日志级别设置为 WARN"
else
    check_warn "根日志级别未设置为 WARN"
fi

# 检查日志文件路径
if grep -q "name: /opt/ps/logs" "$CONFIG_FILE"; then
    check_pass "日志文件路径正确"
else
    check_warn "日志文件路径可能不正确"
fi
echo ""

# 10. 检查监控配置
echo -e "${BLUE}[10/10] 检查监控配置${NC}"

# 检查 Actuator 端点
if grep -q "include: health,prometheus" "$CONFIG_FILE"; then
    check_pass "Actuator 端点配置正确"
else
    check_warn "Actuator 端点配置可能不正确"
fi

# 检查健康检查详情
if grep -q "show-details: never" "$CONFIG_FILE"; then
    check_pass "健康检查详情已隐藏"
else
    check_warn "健康检查详情未隐藏"
fi
echo ""

# 输出检查结果统计
echo -e "${BLUE}============================================${NC}"
echo -e "${BLUE}  检查结果统计${NC}"
echo -e "${BLUE}============================================${NC}"
echo ""
echo -e "${GREEN}通过: $PASS_COUNT${NC}"
echo -e "${YELLOW}警告: $WARN_COUNT${NC}"
echo -e "${RED}失败: $FAIL_COUNT${NC}"
echo ""

# 最终结论
if [ "$FAIL_COUNT" -eq 0 ]; then
    if [ "$WARN_COUNT" -eq 0 ]; then
        echo -e "${GREEN}✓ 配置验证完全通过，可以部署！${NC}"
        exit 0
    else
        echo -e "${YELLOW}⚠ 配置基本通过，但有 $WARN_COUNT 个警告项需要注意${NC}"
        exit 0
    fi
else
    echo -e "${RED}✗ 配置验证失败，发现 $FAIL_COUNT 个错误，请修复后重新验证${NC}"
    exit 1
fi
