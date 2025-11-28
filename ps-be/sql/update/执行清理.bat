@echo off
chcp 65001 >nul
echo ========================================
echo 清理逻辑删除的部门数据
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

pause

echo.
echo ========================================
echo 步骤1: 执行数据分析
echo ========================================
echo 正在分析逻辑删除的部门数据...
echo.

mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p %DB_NAME% < clean_deleted_departments.sql > 分析结果.txt 2>&1

if %errorlevel% neq 0 (
    echo [错误] 分析失败，请检查数据库连接信息
    pause
    exit /b 1
)

echo [成功] 分析完成，结果已保存到: 分析结果.txt
echo.
echo 请查看分析结果，确认是否继续执行清理操作
echo.

pause

echo.
echo ========================================
echo 步骤2: 执行数据清理
echo ========================================
echo.
echo [警告] 即将执行物理删除操作，此操作不可逆！
echo [警告] 请确认已备份数据库
echo.

set /p confirm="确认执行清理操作？(输入 YES 继续): "

if /i not "%confirm%"=="YES" (
    echo 操作已取消
    pause
    exit /b 0
)

echo.
echo 正在执行清理操作...
echo.

mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p %DB_NAME% < clean_deleted_departments_execute.sql > 清理结果.txt 2>&1

if %errorlevel% neq 0 (
    echo [错误] 清理失败，请查看清理结果.txt
    pause
    exit /b 1
)

echo [成功] 清理完成，结果已保存到: 清理结果.txt
echo.
echo ========================================
echo 操作完成
echo ========================================
echo.
echo 请验证以下内容:
echo 1. 查看清理结果.txt确认删除数量
echo 2. 登录系统测试部门树功能
echo 3. 测试人员管理功能
echo 4. 确认数据一致性
echo.
echo 备份表名: tp_dept_basicinfo_deleted_backup
echo 如需回滚，请执行回滚脚本
echo.

pause
