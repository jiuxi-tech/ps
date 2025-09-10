/**
 * datetime - 日期时间工具函数集
 * (c) 2020 lincong1987
 */

import { isArray, isString, isNumber } from 'lodash-es'
import dayjs from 'dayjs'

/**
 * 日期时间工具函数集合
 */

/**
 * 时间格式化函数
 * 
 * @param time - 要格式化的时间
 * @param format - 格式化字符串
 * @returns 返回格式化后的时间字符串
 * @example
 * // 格式化当前时间
 * timeFormat(new Date(), 'YYYY-MM-DD HH:mm:ss');
 * 
 * // 格式化时间戳
 * timeFormat(1609459200000, 'YYYY-MM-DD');
 * 
 * // 格式化时间字符串
 * timeFormat('2021-01-01', 'YYYY年MM月DD日');
 */
export function timeFormat(
  time?: Date | string | number,
  format?: string
): string;

/**
 * 是否为同一天
 * 
 * @param date1 - 第一个日期
 * @param date2 - 第二个日期
 * @returns 如果是同一天则返回 true
 * @example
 * // 检查两个日期是否为同一天
 * isSameDay(new Date(), new Date('2021-01-01')); // false
 * isSameDay(new Date('2021-01-01'), new Date('2021-01-01 12:00:00')); // true
 */
export function isSameDay(date1: Date, date2: Date): boolean;

/**
 * 比较日期
 * 
 * @param date1 - 第一个日期
 * @param date2 - 第二个日期
 * @param format - 比较格式，默认为 'YYYY-MM-DD HH:mm:ss'
 * @returns 大于返回1，等于返回0，小于返回-1
 * @example
 * // 比较两个日期
 * contrastDate(new Date('2021-01-02'), new Date('2021-01-01')); // 1
 * contrastDate(new Date('2021-01-01'), new Date('2021-01-01')); // 0
 * contrastDate(new Date('2021-01-01'), new Date('2021-01-02')); // -1
 */
export function contrastDate(
  date1: Date,
  date2: Date,
  format?: string
): number;

/**
 * 是否为空
 * 
 * @param val - 要检查的值
 * @returns 如果为空则返回 true
 * @example
 * // 检查值是否为空
 * isEmpty(null); // true
 * isEmpty([]); // true
 * isEmpty([1, 2]); // false
 * isEmpty(''); // true
 */
export function isEmpty(val: any): boolean;

/**
 * 判断两个值是否相等
 * 
 * @param mode - 模式 ('single' 或其他)
 * @param val - 第一个值
 * @param dates - 第二个值
 * @returns 如果相等则返回 true
 * @example
 * // 判断单个日期是否相等
 * isEqual('single', '2021-01-01', '2021-01-01'); // true
 * 
 * // 判断多个日期是否相等
 * isEqual('multiple', ['2021-01-01', '2021-01-02'], ['2021-01-01', '2021-01-02']); // true
 */
export function isEqual(
  mode: string,
  val: any,
  dates: any
): boolean;

/**
 * 判断两个日期是否在同一个面板中
 * 
 * @param dates - 日期数组
 * @param format - 格式化字符串
 * @returns 如果在同一个面板中则返回 true
 * @example
 * // 判断两个日期是否在同一个面板中
 * inOnePanel([new Date('2021-01-01'), new Date('2021-01-15')], 'YYYY-MM-DD'); // true
 * inOnePanel([new Date('2021-01-01'), new Date('2021-02-01')], 'YYYY-MM-DD'); // false
 */
export function inOnePanel(
  dates: [Date, Date],
  format: string
): boolean;

/**
 * DateTime 默认导出
 */
export interface DateTime {
  timeFormat: typeof timeFormat;
}

declare const dateTime: DateTime;

export default dateTime;