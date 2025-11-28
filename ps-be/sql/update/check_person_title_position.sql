-- 检查职务职级和职称字段及字典数据

-- 1. 人员基本信息表有OFFICE字段（存储职务）
SELECT '1. 人员基本信息表中的职务字段(OFFICE)' as 说明;

-- 2. 人员扩展信息表有POSITION和TITLE_CODE字段
SELECT '2. 人员扩展信息表字段' as 说明;
SELECT '字段说明:' as 说明;
SELECT 'POSITION - 职务' as 字段;
SELECT 'TITLE_CODE - 职称编码(对应字典)' as 字段;

-- 3. 查询字典类型，找出职称相关的
SELECT '3. 查找职称相关的字典类型' as 说明;
SELECT 
    PDIC_ID as 父字典ID,
    DIC_CODE as 字典编码,
    DIC_NAME as 字典名称,
    DIC_TYPE as 字典类型
FROM tp_dictionary 
WHERE DIC_NAME LIKE '%职称%'
  AND ACTIVED = 1
  AND PDIC_ID IS NOT NULL
LIMIT 10;

-- 4. 查询所有顶级字典分类
SELECT '4. 顶级字典分类' as 说明;
SELECT 
    DIC_ID,
    DIC_CODE,
    DIC_NAME,
    DIC_TYPE
FROM tp_dictionary 
WHERE PDIC_ID IS NULL OR PDIC_ID = ''
  AND ACTIVED = 1
ORDER BY ORDER_INDEX
LIMIT 30;

-- 5. 检查是否有"副教授"
SELECT '5. 搜索"副教授"' as 说明;
SELECT 
    DIC_ID,
    DIC_CODE,
    DIC_NAME,
    PDIC_ID
FROM tp_dictionary 
WHERE DIC_NAME LIKE '%副教授%'
  AND ACTIVED = 1;

-- 6. 检查是否有"四级调研员"
SELECT '6. 搜索"四级调研员"或"调研员"' as 说明;
SELECT 
    DIC_ID,
    DIC_CODE,
    DIC_NAME,
    PDIC_ID
FROM tp_dictionary 
WHERE (DIC_NAME LIKE '%调研员%' OR DIC_NAME LIKE '%职级%')
  AND ACTIVED = 1
LIMIT 20;

-- 7. 查看人员扩展信息表实际数据
SELECT '7. 人员扩展信息实际数据示例' as 说明;
SELECT 
    PERSON_ID,
    POSITION as 职务,
    TITLE_CODE as 职称编码
FROM tp_person_exinfo 
LIMIT 5;
