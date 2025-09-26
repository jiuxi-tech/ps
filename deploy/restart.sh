#!/bin/bash

# ==============================
# Spring Boot 一键重启脚本
# 项目：ps-be
# 目录：/root/jiuxi/lab/ps
# ==============================

APP_DIR="/root/jiuxi/lab/ps"
JAR_NAME="ps-be.jar"
LOG_FILE="ps.log"
PID_FILE="ps.pid"
PROFILE="prod"

# 🔐 敏感配置（请按实际修改！）
# export DB_PASS="你的数据库密码"
# export REDIS_PASS="你的Redis密码"  # 如有需要可取消注释

cd "$APP_DIR"

# 1. 停止旧进程
if [ -f "$PID_FILE" ]; then
    PID=$(cat "$PID_FILE")
    if ps -p "$PID" > /dev/null 2>&1; then
        echo "正在停止旧进程 (PID: $PID)..."
        kill "$PID"
        sleep 3
        # 强制杀死（如果还在）
        if ps -p "$PID" > /dev/null 2>&1; then
            kill -9 "$PID"
        fi
    fi
    rm -f "$PID_FILE"
else
    # 尝试通过 jar 名查找并杀死
    PID=$(pgrep -f "$JAR_NAME")
    if [ -n "$PID" ]; then
        echo "检测到运行中的进程 (PID: $PID)，正在停止..."
        kill -9 "$PID" 2>/dev/null
        sleep 2
    fi
fi

echo "旧进程已停止。"

# 2. 启动新进程
echo "正在启动 $JAR_NAME ..."
nohup java -jar "$JAR_NAME" \
  --spring.profiles.active="$PROFILE" \
  > "$LOG_FILE" 2>&1 &

NEW_PID=$!
echo $NEW_PID > "$PID_FILE"

echo "✅ 应用已启动！PID: $NEW_PID"
echo "📄 日志文件: $APP_DIR/$LOG_FILE"
echo "🔍 查看日志: tail -f $LOG_FILE"