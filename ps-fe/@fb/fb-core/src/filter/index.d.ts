/**
 * 过滤器模块
 * @module filter/index
 */

/**
 * 日期格式化
 * @param timestamp - 时间戳
 * @param format - 格式化字符串
 * @returns 格式化后的日期字符串
 */
export function date(timestamp: number, format?: string): string;

/**
 * 资金格式化
 * @param value - 数值
 * @returns 格式化后的资金字符串
 */
export function money(value: number): string;

/**
 * 银行卡格式化
 * @param value - 银行卡号
 * @returns 格式化后的银行卡号字符串
 */
export function card(value: string): string;

/**
 * 安全处理字符串（中间加***）
 * @param value - 原始字符串
 * @param frontLen - 前面保留的字符数
 * @param backLen - 后面保留的字符数
 * @returns 处理后的字符串
 */
export function safety(value: string, frontLen?: number, backLen?: number): string;

/**
 * 映射数据字典中的值到文本
 * @param value - 值
 * @param arr - 数据字典数组
 * @returns 对应的文本
 */
export function map(value: any, arr: Array<{ value: any; text: string }>): string;

/**
 * 过滤掉数据中的值
 * @param value - 原始数组
 * @param arr - 要过滤的值数组
 * @returns 过滤后的数组
 */
export function allow(value: Array<any>, arr: Array<any>): Array<any>;

/**
 * 首字母大写
 * @param text - 文本
 * @returns 首字母大写的文本
 */
export function capitalize(text: string): string;

/**
 * 转换为大写
 * @param text - 文本
 * @returns 大写的文本
 */
export function uppercase(text: string): string;

/**
 * 转换为小写
 * @param text - 文本
 * @returns 小写的文本
 */
export function lowercase(text: string): string;

/**
 * 默认值处理
 * @param value - 值
 * @param def - 默认值
 * @param blank - 是否将空字符串视为默认值
 * @returns 处理后的值
 */
export function defaults(value: any, def: any, blank?: boolean): any;

/**
 * HTML解码
 * @param value - HTML编码的字符串
 * @returns 解码后的字符串
 */
export function decodeHTML(value: string): string;

/**
 * HTML编码
 * @param value - 原始字符串
 * @returns HTML编码后的字符串
 */
export function encodeHTML(value: string): string;

/**
 * 驼峰转下划线
 * @param str - 驼峰字符串
 * @param line - 分隔符
 * @returns 下划线格式的字符串
 */
export function toLowerLine(str: string, line?: string): string;

/**
 * 下划线转驼峰
 * @param str - 下划线字符串
 * @param line - 分隔符
 * @returns 驼峰格式的字符串
 */
export function toCamel(str: string, line?: string): string;

/**
 * 值是否在数组中，不在则返回默认值
 * @param value - 值
 * @param defaults - 默认值
 * @param array - 数组
 * @returns 处理后的值
 */
export function oneOf(value: any, defaults: any, array?: Array<any>): any;

/**
 * 去除字符串两端空格
 * @param value - 字符串
 * @returns 去除空格后的字符串
 */
export function trim(value: string): string;

/**
 * 连接字符串
 * @param value - 字符串
 * @param catValue - 要连接的值
 * @returns 连接后的字符串
 */
export function cat(value: string, catValue: string): string;

/**
 * 转换为小写
 * @param value - 字符串
 * @returns 小写字符串
 */
export function lower(value: string): string;

/**
 * 转换为大写
 * @param value - 字符串
 * @returns 大写字符串
 */
export function upper(value: string): string;

/**
 * 换行符转<br>标签
 * @param value - 字符串
 * @returns 替换后的字符串
 */
export function nl2br(value: string): string;

/**
 * 换行符转<p>标签
 * @param value - 字符串
 * @returns 替换后的字符串
 */
export function n2p(value: string): string;

/**
 * 数组连接成字符串
 * @param value - 数组
 * @param string - 连接符
 * @returns 连接后的字符串
 */
export function join(value: Array<any>, string: string): string;

/**
 * 时间差计算
 * @param time - 时间
 * @returns 时间差描述
 */
export function timeDiff(time: Date | number | string): string;