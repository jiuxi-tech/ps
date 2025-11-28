@echo off
chcp 65001 >nul
echo ========================================
echo 回滚清理操作 - 恢复已删除的部门数据
echo ========================================
echo.

:: 设置数据库连接信息（请根据实际情况修改）
set DB_HOST=localhost
set DB_PORT=3306
set DB_USER=root
set DB_NAME=ps-bmp

echo 当前配置:
echo 主机: %DB_HOST%
echo 端口: %DB_PORT%
echo 用户: %DB_USER%
echo 数据库: %DB_NAME%
echo.

echo [警告] 即将从备份表恢复数据
echo [警告] 请确认备份表 tp_dept_basicinfo_deleted_backup 存在
echo.

set /p confirm="确认执行回滚操作？(输入 YES 继续): "

if /i not "%confirm%"=="YES" (
    echo 操作已取消
    pause
    exit /b 0
)

echo.
echo 正在执行回滚操作...
echo.

:: 创建临时回滚脚本
echo -- 回滚操作 > rollback_temp.sql
echo -- 从备份表恢复数据 >> rollback_temp.sql
echo. >> rollback_temp.sql
echo -- 检查备份表是否存在 >> rollback_temp.sql
echo SELECT '检查备份表' as 操作, COUNT(*) as 备份记录数 FROM tp_dept_basicinfo_deleted_backup; >> rollback_temp.sql
echo. >> rollback_temp.sql
echo -- 恢复数据 >> rollback_temp.sql
echo INSERT INTO tp_dept_basicinfo >> rollback_temp.sql
echo SELECT * FROM tp_dept_basicinfo_deleted_backup; >> rollback_temp.sql
echo. >> rollback_temp.sql
echo -- 验证恢复 >> rollback_temp.sql
echo SELECT '恢复后统计' as 操作, COUNT(*) as 逻辑删除部门数 FROM tp_dept_basicinfo WHERE ACTIVED = 0; >> rollback_temp.sql

mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p %DB_NAME% < rollback_temp.sql > 回滚结果.txt 2>&1

if %errorlevel% neq 0 (
    echo [错误] 回滚失败，请查看回滚结果.txt
    del rollback_temp.sql
    pause
    exit /b 1
)

del rollback_temp.sql

echo [成功] 回滚完成，数据已恢复
echo.
echo 结果已保存到: 回滚结果.txt
echo.
echo 请验证:
echo 1. 查看回滚结果.txt确认恢复数量
echo 2. 登录系统验证部门数据
echo.

pause
