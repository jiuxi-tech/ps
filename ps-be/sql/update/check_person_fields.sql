-- 检查人员表中职务职级和职称相关字段

-- 1. 查看人员基本信息表结构
SELECT '1. 人员基本信息表(tp_person_basicinfo)字段' as 说明;
SHOW COLUMNS FROM tp_person_basicinfo;

-- 2. 查看人员扩展信息表结构
SELECT '2. 人员扩展信息表(tp_person_exinfo)字段' as 说明;
SHOW COLUMNS FROM tp_person_exinfo;

-- 3. 查询字典表中的职称数据(SYS12)
SELECT '3. 职称字典数据(SYS12)' as 说明;
SELECT 
    DIC_CODE as 字典编码,
    DIC_NAME as 字典名称,
    TYPE_CODE as 类型编码,
    ACTIVED as 是否激活
FROM tp_dictionary 
WHERE TYPE_CODE = 'SYS12' 
  AND ACTIVED = 1
ORDER BY ORDER_INDEX;

-- 4. 检查是否有"副教授"
SELECT '4. 检查职称"副教授"' as 说明;
SELECT 
    DIC_CODE,
    DIC_NAME,
    TYPE_CODE
FROM tp_dictionary 
WHERE TYPE_CODE = 'SYS12' 
  AND DIC_NAME LIKE '%副教授%'
  AND ACTIVED = 1;

-- 5. 查询所有职务职级相关的字典类型
SELECT '5. 职务职级相关字典类型' as 说明;
SELECT DISTINCT
    TYPE_CODE,
    TYPE_NAME
FROM tp_dictionary 
WHERE (TYPE_NAME LIKE '%职务%' OR TYPE_NAME LIKE '%职级%' OR TYPE_CODE LIKE '%POS%' OR TYPE_CODE LIKE '%RANK%')
  AND ACTIVED = 1
ORDER BY TYPE_CODE;

-- 6. 查看所有字典类型
SELECT '6. 所有字典类型列表' as 说明;
SELECT DISTINCT
    TYPE_CODE as 类型编码,
    TYPE_NAME as 类型名称
FROM tp_dictionary 
WHERE ACTIVED = 1
ORDER BY TYPE_CODE
LIMIT 50;

-- 7. 检查人员扩展信息表中POSITION字段的使用情况
SELECT '7. POSITION字段使用示例' as 说明;
SELECT 
    PERSON_ID,
    POSITION as 职务,
    TITLE_CODE as 职称编码
FROM tp_person_exinfo 
WHERE POSITION IS NOT NULL OR TITLE_CODE IS NOT NULL
LIMIT 10;
