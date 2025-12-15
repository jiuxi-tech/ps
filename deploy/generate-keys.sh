#!/bin/bash
############################################################
# PS-BE 生产环境配置密钥生成工具
# 用于生成配置文件中所需的各种随机密钥
############################################################

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}============================================${NC}"
echo -e "${BLUE}  PS-BE 生产环境配置密钥生成工具${NC}"
echo -e "${BLUE}============================================${NC}"
echo ""

# 生成随机密钥函数
generate_key() {
    local length=$1
    local key=$(openssl rand -base64 $((length * 3 / 4)) | tr -d '\n' | head -c $length)
    echo "$key"
}

# 生成随机密码函数（包含大小写、数字、特殊字符）
generate_password() {
    local length=${1:-16}
    local password=$(LC_ALL=C tr -dc 'A-Za-z0-9!@#$%^&*()_+=' < /dev/urandom | head -c $length)
    echo "$password"
}

echo -e "${GREEN}正在生成配置密钥...${NC}"
echo ""

# 生成各种密钥
DB_PASSWORD=$(generate_password 16)
REDIS_PASSWORD=$(generate_password 16)
KEYCLOAK_CLIENT_SECRET=$(generate_key 32)
KEYCLOAK_ADMIN_PASSWORD=$(generate_password 16)
JWT_SECRET=$(generate_key 64)
AES_KEY=$(generate_key 32)
MAIL_PASSWORD=$(generate_password 16)

# 输出密钥
echo -e "${YELLOW}┌─────────────────────────────────────────────────────────────┐${NC}"
echo -e "${YELLOW}│ 生成的密钥（请妥善保管）                                    │${NC}"
echo -e "${YELLOW}└─────────────────────────────────────────────────────────────┘${NC}"
echo ""

echo -e "${GREEN}1. 数据库密码:${NC}"
echo "   $DB_PASSWORD"
echo ""

echo -e "${GREEN}2. Redis 密码:${NC}"
echo "   $REDIS_PASSWORD"
echo ""

echo -e "${GREEN}3. Keycloak 客户端密钥:${NC}"
echo "   $KEYCLOAK_CLIENT_SECRET"
echo ""

echo -e "${GREEN}4. Keycloak 管理员密码:${NC}"
echo "   $KEYCLOAK_ADMIN_PASSWORD"
echo ""

echo -e "${GREEN}5. JWT 密钥:${NC}"
echo "   $JWT_SECRET"
echo ""

echo -e "${GREEN}6. AES 密钥:${NC}"
echo "   $AES_KEY"
echo ""

echo -e "${GREEN}7. 邮件密码（示例，实际使用邮箱提供的应用专用密码）:${NC}"
echo "   $MAIL_PASSWORD"
echo ""

# 生成配置模板
echo -e "${YELLOW}┌─────────────────────────────────────────────────────────────┐${NC}"
echo -e "${YELLOW}│ 配置文件模板（复制到 application-prod.yml）                 │${NC}"
echo -e "${YELLOW}└─────────────────────────────────────────────────────────────┘${NC}"
echo ""

cat << EOF
# 数据库配置
app:
  database:
    datasource:
      url: jdbc:mariadb://192.168.1.100:3306/ps-bmp?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
      username: ps_user
      password: $DB_PASSWORD

# Redis 配置
  cache:
    redis:
      host: 192.168.1.101
      port: 6379
      password: $REDIS_PASSWORD

# Keycloak SSO 配置
  security:
    keycloak:
      server-url: https://sso.example.com
      sso:
        client-secret: $KEYCLOAK_CLIENT_SECRET
      admin:
        username: admin
        password: $KEYCLOAK_ADMIN_PASSWORD

# JWT 配置
    jwt:
      secret: $JWT_SECRET

# 加密配置
    encryption:
      aes-key: $AES_KEY

# 邮件配置
spring:
  mail:
    host: smtp.example.com
    username: notify@example.com
    password: $MAIL_PASSWORD
EOF

echo ""
echo -e "${YELLOW}┌─────────────────────────────────────────────────────────────┐${NC}"
echo -e "${YELLOW}│ 安全提示                                                    │${NC}"
echo -e "${YELLOW}└─────────────────────────────────────────────────────────────┘${NC}"
echo ""
echo -e "${RED}⚠️  重要安全提示:${NC}"
echo "1. 请将生成的密钥妥善保管，不要泄露给他人"
echo "2. 不要将包含密钥的配置文件提交到版本控制系统"
echo "3. 定期更换密钥（建议每 3-6 个月）"
echo "4. 配置文件权限必须设置为 600: chmod 600 application-prod.yml"
echo "5. 邮件密码请使用邮箱提供的应用专用密码，而非登录密码"
echo ""

# 询问是否保存到文件
echo -e "${BLUE}是否将生成的密钥保存到文件？(y/n)${NC}"
read -r SAVE_TO_FILE

if [ "$SAVE_TO_FILE" = "y" ] || [ "$SAVE_TO_FILE" = "Y" ]; then
    TIMESTAMP=$(date +%Y%m%d_%H%M%S)
    FILENAME="generated-keys-${TIMESTAMP}.txt"
    
    cat > "$FILENAME" << EOF
PS-BE 生产环境配置密钥
生成时间: $(date)

1. 数据库密码:
   $DB_PASSWORD

2. Redis 密码:
   $REDIS_PASSWORD

3. Keycloak 客户端密钥:
   $KEYCLOAK_CLIENT_SECRET

4. Keycloak 管理员密码:
   $KEYCLOAK_ADMIN_PASSWORD

5. JWT 密钥:
   $JWT_SECRET

6. AES 密钥:
   $AES_KEY

7. 邮件密码（示例）:
   $MAIL_PASSWORD

配置文件模板:
================

# 数据库配置
app:
  database:
    datasource:
      url: jdbc:mariadb://192.168.1.100:3306/ps-bmp?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
      username: ps_user
      password: $DB_PASSWORD

# Redis 配置
  cache:
    redis:
      host: 192.168.1.101
      port: 6379
      password: $REDIS_PASSWORD

# Keycloak SSO 配置
  security:
    keycloak:
      server-url: https://sso.example.com
      sso:
        client-secret: $KEYCLOAK_CLIENT_SECRET
      admin:
        username: admin
        password: $KEYCLOAK_ADMIN_PASSWORD

# JWT 配置
    jwt:
      secret: $JWT_SECRET

# 加密配置
    encryption:
      aes-key: $AES_KEY

# 邮件配置
spring:
  mail:
    host: smtp.example.com
    username: notify@example.com
    password: $MAIL_PASSWORD

安全提示:
=========
1. 请将此文件妥善保管，不要泄露给他人
2. 使用完毕后请立即删除此文件
3. 不要将此文件提交到版本控制系统
4. 配置文件权限必须设置为 600
EOF

    # 设置文件权限为 600
    chmod 600 "$FILENAME"
    
    echo ""
    echo -e "${GREEN}✓ 密钥已保存到文件: $FILENAME${NC}"
    echo -e "${RED}⚠️  请立即将此文件移动到安全位置，使用完毕后立即删除！${NC}"
    echo -e "${YELLOW}文件权限已自动设置为 600（仅所有者可读写）${NC}"
fi

echo ""
echo -e "${BLUE}============================================${NC}"
echo -e "${BLUE}  密钥生成完成${NC}"
echo -e "${BLUE}============================================${NC}"
