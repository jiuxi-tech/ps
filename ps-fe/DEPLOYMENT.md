# PS-BMP 管理端 UI 部署指南

## 概述

本文档详细介绍了 PS-BMP 管理端 UI 项目的部署流程、环境配置和运维指南。项目支持多种部署方式，包括传统服务器部署、容器化部署和云平台部署。

## 部署架构

### 系统架构图
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   用户浏览器    │    │   负载均衡器    │    │   Web服务器     │
│                 │────│   (Nginx)       │────│   (Nginx)       │
│                 │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                                        │
                                                        ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   静态资源CDN   │    │   API网关       │    │   后端服务      │
│                 │    │                 │    │                 │
│                 │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### 部署环境

| 环境 | 域名 | 服务器 | 用途 |
|------|------|--------|------|
| 开发环境 | dev.ps-bmp.com | 192.168.1.100 | 开发测试 |
| 测试环境 | test.ps-bmp.com | 192.168.1.101 | 功能测试 |
| 预生产环境 | pre.ps-bmp.com | 192.168.1.102 | 上线前验证 |
| 生产环境 | ps-bmp.com | 192.168.90.77 | 正式环境 |

## 环境准备

### 服务器要求

#### 最低配置
- **CPU**: 2核
- **内存**: 4GB
- **存储**: 50GB SSD
- **网络**: 10Mbps
- **操作系统**: CentOS 7+ / Ubuntu 18.04+ / Windows Server 2016+

#### 推荐配置
- **CPU**: 4核
- **内存**: 8GB
- **存储**: 100GB SSD
- **网络**: 100Mbps
- **操作系统**: CentOS 8+ / Ubuntu 20.04+ / Windows Server 2019+

### 软件依赖

#### 必需软件
- **Node.js**: >= 14.x
- **npm**: >= 6.x
- **Git**: >= 2.x
- **Web服务器**: Nginx >= 1.16 或 Apache >= 2.4

#### 可选软件
- **Docker**: >= 20.x (容器化部署)
- **PM2**: >= 5.x (进程管理)
- **Redis**: >= 6.x (缓存)

## 构建流程

### 1. 代码获取
```bash
# 克隆代码仓库
git clone https://github.com/your-org/ps-bmp-admin-ui.git
cd ps-bmp-admin-ui

# 切换到指定分支
git checkout main

# 查看最新提交
git log --oneline -5
```

### 2. 依赖安装
```bash
# 安装项目依赖
npm install

# 或者使用 yarn
yarn install

# 清理缓存（如果需要）
npm cache clean --force
```

### 3. 环境配置

#### 开发环境配置
```bash
# 复制环境配置文件
cp .env.dev .env.local

# 编辑配置文件
vim .env.local
```

```bash
# .env.local
NODE_ENV=development
VUE_APP_URL=http://127.0.0.1:8088/
VUE_REPORT_URL=http://127.0.0.1:8077/
VUE_APP_REPORT_URL=''
```

#### 生产环境配置
```bash
# .env.prod
NODE_ENV=production
VUE_APP_URL=http://192.168.90.77:8080/
VUE_REPORT_URL=http://192.168.90.77:8077/
VUE_APP_REPORT_URL=http://192.168.90.77:8077/
```

### 4. 项目构建
```bash
# 构建生产版本
npm run build

# 构建完成后检查输出
ls -la dist/

# 预览构建结果
npm run preview
```

### 5. 构建优化

#### 启用 Gzip 压缩
```javascript
// rsbuild.config.js
export default {
  output: {
    // 启用 Gzip 压缩
    compress: 'gzip'
  }
}
```

#### 代码分割
```javascript
// 路由懒加载
const routes = [
  {
    path: '/user',
    component: () => import('@/views/User.vue')
  }
]
```

#### 资源优化
```javascript
// 图片压缩和格式转换
export default {
  tools: {
    imageOptimize: {
      jpeg: { quality: 80 },
      png: { quality: 80 },
      webp: { quality: 80 }
    }
  }
}
```

## 部署方式

### 方式一：传统服务器部署

#### 1. 文件上传
```bash
# 使用 scp 上传文件
scp -r dist/* user@server:/var/www/ps-bmp-admin/

# 或使用 rsync
rsync -avz --delete dist/ user@server:/var/www/ps-bmp-admin/
```

#### 2. Nginx 配置
```nginx
# /etc/nginx/sites-available/ps-bmp-admin
server {
    listen 80;
    server_name ps-bmp.com www.ps-bmp.com;
    root /var/www/ps-bmp-admin;
    index index.html;
    
    # 启用 Gzip 压缩
    gzip on;
    gzip_vary on;
    gzip_min_length 1024;
    gzip_types
        text/plain
        text/css
        text/xml
        text/javascript
        application/javascript
        application/xml+rss
        application/json;
    
    # 处理 Vue Router 的 history 模式
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    # 静态资源缓存
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2|ttf|eot)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
        add_header Vary Accept-Encoding;
    }
    
    # API 代理
    location /api/ {
        proxy_pass http://192.168.90.77:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # 超时设置
        proxy_connect_timeout 30s;
        proxy_send_timeout 30s;
        proxy_read_timeout 30s;
    }
    
    # 安全头
    add_header X-Frame-Options DENY;
    add_header X-Content-Type-Options nosniff;
    add_header X-XSS-Protection "1; mode=block";
    add_header Referrer-Policy "strict-origin-when-cross-origin";
    
    # 访问日志
    access_log /var/log/nginx/ps-bmp-admin.access.log;
    error_log /var/log/nginx/ps-bmp-admin.error.log;
}

# HTTPS 配置
server {
    listen 443 ssl http2;
    server_name ps-bmp.com www.ps-bmp.com;
    root /var/www/ps-bmp-admin;
    index index.html;
    
    # SSL 证书配置
    ssl_certificate /etc/ssl/certs/ps-bmp.com.crt;
    ssl_certificate_key /etc/ssl/private/ps-bmp.com.key;
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-GCM-SHA384;
    ssl_prefer_server_ciphers off;
    
    # HSTS
    add_header Strict-Transport-Security "max-age=63072000" always;
    
    # 其他配置同 HTTP
    include /etc/nginx/snippets/ps-bmp-common.conf;
}

# HTTP 重定向到 HTTPS
server {
    listen 80;
    server_name ps-bmp.com www.ps-bmp.com;
    return 301 https://$server_name$request_uri;
}
```

#### 3. 启用配置
```bash
# 创建软链接
sudo ln -s /etc/nginx/sites-available/ps-bmp-admin /etc/nginx/sites-enabled/

# 测试配置
sudo nginx -t

# 重载配置
sudo systemctl reload nginx

# 查看状态
sudo systemctl status nginx
```

### 方式二：Docker 容器化部署

#### 1. Dockerfile
```dockerfile
# 多阶段构建
# 构建阶段
FROM node:16-alpine AS builder

WORKDIR /app

# 复制依赖文件
COPY package*.json ./
RUN npm ci --only=production

# 复制源码
COPY . .

# 构建应用
RUN npm run build

# 生产阶段
FROM nginx:alpine

# 复制构建结果
COPY --from=builder /app/dist /usr/share/nginx/html

# 复制 Nginx 配置
COPY nginx.conf /etc/nginx/nginx.conf
COPY default.conf /etc/nginx/conf.d/default.conf

# 暴露端口
EXPOSE 80

# 启动命令
CMD ["nginx", "-g", "daemon off;"]
```

#### 2. Docker Compose
```yaml
# docker-compose.yml
version: '3.8'

services:
  ps-bmp-admin:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: ps-bmp-admin
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx/conf.d:/etc/nginx/conf.d
      - ./nginx/ssl:/etc/nginx/ssl
      - ./logs:/var/log/nginx
    environment:
      - NODE_ENV=production
    restart: unless-stopped
    networks:
      - ps-bmp-network

  # Redis 缓存（可选）
  redis:
    image: redis:6-alpine
    container_name: ps-bmp-redis
    ports:
      - "6379:6379"
    volumes:
      - redis-data:/data
    restart: unless-stopped
    networks:
      - ps-bmp-network

volumes:
  redis-data:

networks:
  ps-bmp-network:
    driver: bridge
```

#### 3. 部署命令
```bash
# 构建镜像
docker build -t ps-bmp-admin:latest .

# 运行容器
docker-compose up -d

# 查看日志
docker-compose logs -f ps-bmp-admin

# 更新部署
docker-compose pull
docker-compose up -d --force-recreate
```

### 方式三：云平台部署

#### 阿里云 ECS 部署
```bash
# 安装阿里云 CLI
wget https://aliyuncli.alicdn.com/aliyun-cli-linux-latest-amd64.tgz
tar -xzf aliyun-cli-linux-latest-amd64.tgz
sudo mv aliyun /usr/local/bin/

# 配置访问密钥
aliyun configure

# 创建 ECS 实例
aliyun ecs CreateInstance \
  --RegionId cn-hangzhou \
  --ImageId ubuntu_20_04_x64_20G_alibase_20210420.vhd \
  --InstanceType ecs.t5-lc1m2.small \
  --SecurityGroupId sg-xxxxxxxxx \
  --VSwitchId vsw-xxxxxxxxx
```

#### 腾讯云 COS 静态网站托管
```bash
# 安装腾讯云 CLI
pip install coscmd

# 配置 COS
coscmd config -a <SecretId> -s <SecretKey> -b <BucketName> -r <Region>

# 上传文件
coscmd upload -r dist/ /

# 设置静态网站
coscmd putbucketwebsite -i index.html -e error.html
```

## 自动化部署

### CI/CD 流水线

#### GitHub Actions
```yaml
# .github/workflows/deploy.yml
name: Deploy to Production

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    
    steps:
    - name: Checkout code
      uses: actions/checkout@v3
      
    - name: Setup Node.js
      uses: actions/setup-node@v3
      with:
        node-version: '16'
        cache: 'npm'
        
    - name: Install dependencies
      run: npm ci
      
    - name: Run tests
      run: npm test
      
    - name: Build application
      run: npm run build
      env:
        NODE_ENV: production
        VUE_APP_URL: ${{ secrets.API_URL }}
        
    - name: Deploy to server
      uses: appleboy/ssh-action@v0.1.5
      with:
        host: ${{ secrets.HOST }}
        username: ${{ secrets.USERNAME }}
        key: ${{ secrets.SSH_KEY }}
        script: |
          cd /var/www/ps-bmp-admin
          git pull origin main
          npm ci
          npm run build
          sudo systemctl reload nginx
```

#### GitLab CI/CD
```yaml
# .gitlab-ci.yml
stages:
  - test
  - build
  - deploy

variables:
  NODE_VERSION: "16"

cache:
  paths:
    - node_modules/

test:
  stage: test
  image: node:$NODE_VERSION
  script:
    - npm ci
    - npm run test
  only:
    - merge_requests
    - main

build:
  stage: build
  image: node:$NODE_VERSION
  script:
    - npm ci
    - npm run build
  artifacts:
    paths:
      - dist/
    expire_in: 1 hour
  only:
    - main

deploy_production:
  stage: deploy
  image: alpine:latest
  before_script:
    - apk add --no-cache rsync openssh
    - eval $(ssh-agent -s)
    - echo "$SSH_PRIVATE_KEY" | tr -d '\r' | ssh-add -
    - mkdir -p ~/.ssh
    - chmod 700 ~/.ssh
  script:
    - rsync -avz --delete dist/ $DEPLOY_USER@$DEPLOY_HOST:/var/www/ps-bmp-admin/
    - ssh $DEPLOY_USER@$DEPLOY_HOST "sudo systemctl reload nginx"
  only:
    - main
  when: manual
```

### 部署脚本

#### 自动部署脚本
```bash
#!/bin/bash
# deploy.sh

set -e

# 配置变量
APP_NAME="ps-bmp-admin"
DEPLOY_DIR="/var/www/$APP_NAME"
BACKUP_DIR="/var/backups/$APP_NAME"
GIT_REPO="https://github.com/your-org/ps-bmp-admin-ui.git"
BRANCH="main"

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

log() {
    echo -e "${GREEN}[$(date +'%Y-%m-%d %H:%M:%S')] $1${NC}"
}

warn() {
    echo -e "${YELLOW}[$(date +'%Y-%m-%d %H:%M:%S')] WARNING: $1${NC}"
}

error() {
    echo -e "${RED}[$(date +'%Y-%m-%d %H:%M:%S')] ERROR: $1${NC}"
    exit 1
}

# 检查依赖
check_dependencies() {
    log "检查系统依赖..."
    
    command -v node >/dev/null 2>&1 || error "Node.js 未安装"
    command -v npm >/dev/null 2>&1 || error "npm 未安装"
    command -v git >/dev/null 2>&1 || error "Git 未安装"
    command -v nginx >/dev/null 2>&1 || error "Nginx 未安装"
    
    log "依赖检查完成"
}

# 创建备份
create_backup() {
    if [ -d "$DEPLOY_DIR" ]; then
        log "创建备份..."
        BACKUP_NAME="$APP_NAME-$(date +%Y%m%d-%H%M%S)"
        mkdir -p "$BACKUP_DIR"
        cp -r "$DEPLOY_DIR" "$BACKUP_DIR/$BACKUP_NAME"
        log "备份创建完成: $BACKUP_DIR/$BACKUP_NAME"
    fi
}

# 部署应用
deploy_app() {
    log "开始部署应用..."
    
    # 创建部署目录
    mkdir -p "$DEPLOY_DIR"
    cd "$DEPLOY_DIR"
    
    # 克隆或更新代码
    if [ -d ".git" ]; then
        log "更新代码..."
        git fetch origin
        git reset --hard origin/$BRANCH
    else
        log "克隆代码..."
        git clone -b $BRANCH "$GIT_REPO" .
    fi
    
    # 安装依赖
    log "安装依赖..."
    npm ci --production=false
    
    # 构建应用
    log "构建应用..."
    npm run build
    
    # 设置权限
    chown -R www-data:www-data "$DEPLOY_DIR/dist"
    chmod -R 755 "$DEPLOY_DIR/dist"
    
    log "应用部署完成"
}

# 重启服务
restart_services() {
    log "重启服务..."
    
    # 测试 Nginx 配置
    nginx -t || error "Nginx 配置测试失败"
    
    # 重载 Nginx
    systemctl reload nginx || error "Nginx 重载失败"
    
    log "服务重启完成"
}

# 健康检查
health_check() {
    log "执行健康检查..."
    
    # 检查网站是否可访问
    HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/)
    
    if [ "$HTTP_CODE" = "200" ]; then
        log "健康检查通过"
    else
        error "健康检查失败，HTTP状态码: $HTTP_CODE"
    fi
}

# 清理旧备份
cleanup_backups() {
    log "清理旧备份..."
    
    # 保留最近7天的备份
    find "$BACKUP_DIR" -name "$APP_NAME-*" -type d -mtime +7 -exec rm -rf {} \;
    
    log "备份清理完成"
}

# 主函数
main() {
    log "开始部署 $APP_NAME"
    
    check_dependencies
    create_backup
    deploy_app
    restart_services
    health_check
    cleanup_backups
    
    log "部署完成！"
}

# 错误处理
trap 'error "部署过程中发生错误"' ERR

# 执行主函数
main "$@"
```

#### 回滚脚本
```bash
#!/bin/bash
# rollback.sh

set -e

APP_NAME="ps-bmp-admin"
DEPLOY_DIR="/var/www/$APP_NAME"
BACKUP_DIR="/var/backups/$APP_NAME"

# 列出可用备份
list_backups() {
    echo "可用备份:"
    ls -la "$BACKUP_DIR" | grep "$APP_NAME-" | awk '{print $9}'
}

# 回滚到指定备份
rollback() {
    BACKUP_NAME="$1"
    
    if [ -z "$BACKUP_NAME" ]; then
        echo "请指定备份名称"
        list_backups
        exit 1
    fi
    
    BACKUP_PATH="$BACKUP_DIR/$BACKUP_NAME"
    
    if [ ! -d "$BACKUP_PATH" ]; then
        echo "备份不存在: $BACKUP_PATH"
        exit 1
    fi
    
    echo "回滚到备份: $BACKUP_NAME"
    
    # 创建当前状态的备份
    CURRENT_BACKUP="$APP_NAME-rollback-$(date +%Y%m%d-%H%M%S)"
    cp -r "$DEPLOY_DIR" "$BACKUP_DIR/$CURRENT_BACKUP"
    
    # 恢复备份
    rm -rf "$DEPLOY_DIR"
    cp -r "$BACKUP_PATH" "$DEPLOY_DIR"
    
    # 重启服务
    systemctl reload nginx
    
    echo "回滚完成"
}

if [ "$1" = "list" ]; then
    list_backups
else
    rollback "$1"
fi
```

## 监控和运维

### 系统监控

#### Nginx 日志分析
```bash
# 实时查看访问日志
tail -f /var/log/nginx/ps-bmp-admin.access.log

# 分析访问统计
awk '{print $1}' /var/log/nginx/ps-bmp-admin.access.log | sort | uniq -c | sort -nr | head -10

# 分析响应时间
awk '{print $NF}' /var/log/nginx/ps-bmp-admin.access.log | sort -n | tail -10

# 分析状态码
awk '{print $9}' /var/log/nginx/ps-bmp-admin.access.log | sort | uniq -c | sort -nr
```

#### 系统资源监控
```bash
# CPU 使用率
top -p $(pgrep nginx)

# 内存使用情况
free -h

# 磁盘使用情况
df -h

# 网络连接
netstat -an | grep :80
```

### 性能优化

#### 启用 HTTP/2
```nginx
server {
    listen 443 ssl http2;
    # 其他配置...
}
```

#### 启用 Brotli 压缩
```nginx
# 安装 nginx-module-brotli
apt-get install nginx-module-brotli

# 配置
load_module modules/ngx_http_brotli_filter_module.so;
load_module modules/ngx_http_brotli_static_module.so;

http {
    brotli on;
    brotli_comp_level 6;
    brotli_types
        text/plain
        text/css
        application/json
        application/javascript
        text/xml
        application/xml
        application/xml+rss
        text/javascript;
}
```

#### CDN 配置
```nginx
# 设置缓存头
location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
    expires 1y;
    add_header Cache-Control "public, immutable";
    add_header CDN-Cache-Control "public, max-age=31536000";
}
```

### 安全配置

#### SSL/TLS 配置
```nginx
# 强制 HTTPS
server {
    listen 80;
    server_name ps-bmp.com;
    return 301 https://$server_name$request_uri;
}

# SSL 配置
ssl_protocols TLSv1.2 TLSv1.3;
ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-GCM-SHA384;
ssl_prefer_server_ciphers off;
ssl_session_cache shared:SSL:10m;
ssl_session_timeout 10m;

# HSTS
add_header Strict-Transport-Security "max-age=63072000; includeSubDomains; preload" always;
```

#### 防火墙配置
```bash
# UFW 配置
sudo ufw allow ssh
sudo ufw allow 'Nginx Full'
sudo ufw enable

# iptables 配置
iptables -A INPUT -p tcp --dport 22 -j ACCEPT
iptables -A INPUT -p tcp --dport 80 -j ACCEPT
iptables -A INPUT -p tcp --dport 443 -j ACCEPT
iptables -A INPUT -j DROP
```

#### 访问控制
```nginx
# IP 白名单
location /admin {
    allow 192.168.1.0/24;
    allow 10.0.0.0/8;
    deny all;
}

# 限制请求频率
limit_req_zone $binary_remote_addr zone=api:10m rate=10r/s;
location /api/ {
    limit_req zone=api burst=20 nodelay;
}
```

## 故障排除

### 常见问题

#### 1. 页面无法访问
```bash
# 检查 Nginx 状态
sudo systemctl status nginx

# 检查端口占用
sudo netstat -tlnp | grep :80

# 检查配置文件
sudo nginx -t

# 查看错误日志
sudo tail -f /var/log/nginx/error.log
```

#### 2. 静态资源加载失败
```bash
# 检查文件权限
ls -la /var/www/ps-bmp-admin/dist/

# 修复权限
sudo chown -R www-data:www-data /var/www/ps-bmp-admin/
sudo chmod -R 755 /var/www/ps-bmp-admin/
```

#### 3. API 请求失败
```bash
# 检查后端服务
curl -I http://192.168.90.77:8080/health

# 检查代理配置
nginx -T | grep -A 10 "location /api/"
```

#### 4. 构建失败
```bash
# 清理缓存
npm cache clean --force
rm -rf node_modules package-lock.json
npm install

# 检查 Node.js 版本
node --version
npm --version
```

### 日志分析

#### 应用日志
```bash
# 查看构建日志
npm run build 2>&1 | tee build.log

# 查看运行时错误
grep -i error /var/log/nginx/ps-bmp-admin.error.log
```

#### 系统日志
```bash
# 查看系统日志
journalctl -u nginx -f

# 查看内核日志
dmesg | tail
```

## 备份和恢复

### 数据备份

#### 自动备份脚本
```bash
#!/bin/bash
# backup.sh

BACKUP_DIR="/var/backups/ps-bmp-admin"
SOURCE_DIR="/var/www/ps-bmp-admin"
DATE=$(date +%Y%m%d-%H%M%S)
BACKUP_NAME="ps-bmp-admin-$DATE"

# 创建备份目录
mkdir -p "$BACKUP_DIR"

# 创建备份
tar -czf "$BACKUP_DIR/$BACKUP_NAME.tar.gz" -C "$(dirname $SOURCE_DIR)" "$(basename $SOURCE_DIR)"

# 删除7天前的备份
find "$BACKUP_DIR" -name "*.tar.gz" -mtime +7 -delete

echo "备份完成: $BACKUP_DIR/$BACKUP_NAME.tar.gz"
```

#### 定时备份
```bash
# 添加到 crontab
crontab -e

# 每天凌晨2点备份
0 2 * * * /usr/local/bin/backup.sh
```

### 数据恢复

#### 恢复脚本
```bash
#!/bin/bash
# restore.sh

BACKUP_FILE="$1"
RESTORE_DIR="/var/www/ps-bmp-admin"

if [ -z "$BACKUP_FILE" ]; then
    echo "请指定备份文件"
    exit 1
fi

if [ ! -f "$BACKUP_FILE" ]; then
    echo "备份文件不存在: $BACKUP_FILE"
    exit 1
fi

# 停止服务
sudo systemctl stop nginx

# 备份当前状态
sudo mv "$RESTORE_DIR" "$RESTORE_DIR.bak.$(date +%Y%m%d-%H%M%S)"

# 恢复备份
sudo tar -xzf "$BACKUP_FILE" -C "$(dirname $RESTORE_DIR)"

# 启动服务
sudo systemctl start nginx

echo "恢复完成"
```

## 版本管理

### 版本发布流程

1. **开发阶段**
   - 在 `develop` 分支进行开发
   - 提交代码并推送到远程仓库

2. **测试阶段**
   - 创建 `release/x.x.x` 分支
   - 部署到测试环境
   - 执行测试用例

3. **发布阶段**
   - 合并到 `main` 分支
   - 创建 Git 标签
   - 部署到生产环境

4. **维护阶段**
   - 监控系统运行状态
   - 处理用户反馈
   - 修复紧急问题

### 版本回滚策略

1. **快速回滚**
   - 使用备份文件快速恢复
   - 适用于严重问题

2. **代码回滚**
   - 回滚到上一个稳定版本
   - 重新构建和部署

3. **热修复**
   - 在当前版本基础上修复
   - 适用于小问题

---

**更新时间**: 2024年12月
**文档版本**: v1.0.0

> 本文档将随着项目的发展持续更新，请关注最新版本。如有问题，请联系运维团队。