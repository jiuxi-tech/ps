/*!
 * datetime - 日期时间工具函数集
 * (c) 2020 lincong1987
 */

import { isArray, isString, isNumber } from 'lodash-es'
import dayjs from 'dayjs'

/**
 * @namespace DateTime
 * @desc 日期时间工具函数集合
 */

/**
 * @desc 时间格式化函数
 * @param {Date|String|Number} time - 要格式化的时间
 * @param {String} format - 格式化字符串
 * @returns {String} 返回格式化后的时间字符串
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
export function timeFormat (time, format) {

	if (!time) {
		time = new Date()
	}

	if (isNumber(time)) {
		time = new Date(time)
	}

	if (isString(time)) {
		time = dayjs(time).toDate()
	}

	if (!time instanceof Date) {
		time = new Date()
	}

	const year = time.getFullYear()
	const month = time.getMonth()
	const day = time.getDate()
	const hours24 = time.getHours()
	const hours = hours24 % 12 === 0 ? 12 : hours24 % 12
	const minutes = time.getMinutes()
	const seconds = time.getSeconds()
	const milliseconds = time.getMilliseconds()
	const dd = t => (`0${t}`).slice(-2)
	const map = {
		YYYY: year,
		MM: dd(month + 1),
		MMMM: `${month + 1}月`,
		M: month + 1,
		DD: dd(day),
		D: day,
		HH: dd(hours24),
		H: hours24,
		hh: dd(hours),
		h: hours,
		mm: dd(minutes),
		m: minutes,
		ss: dd(seconds),
		s: seconds,
		S: milliseconds,
	}
	return (format).replace(
		/Y+|M+|D+|H+|h+|m+|s+|S+/g, str => map[str])
}

/**
 * @desc 是否为同一天
 * @param {Date} date1 - 第一个日期
 * @param {Date} date2 - 第二个日期
 * @returns {Boolean} 如果是同一天则返回 true
 * @example
 * // 检查两个日期是否为同一天
 * isSameDay(new Date(), new Date('2021-01-01')); // false
 * isSameDay(new Date('2021-01-01'), new Date('2021-01-01 12:00:00')); // true
 */
export function isSameDay (date1, date2) {
	return timeFormat(date1, 'YYYY-MM-DD') === timeFormat(date2, 'YYYY-MM-DD')
}

/**
 * @desc 比较日期
 * @param {Date} date1 - 第一个日期
 * @param {Date} date2 - 第二个日期
 * @param {String} format - 比较格式，默认为 'YYYY-MM-DD HH:mm:ss'
 * @returns {Number} 大于返回1，等于返回0，小于返回-1
 * @example
 * // 比较两个日期
 * contrastDate(new Date('2021-01-02'), new Date('2021-01-01')); // 1
 * contrastDate(new Date('2021-01-01'), new Date('2021-01-01')); // 0
 * contrastDate(new Date('2021-01-01'), new Date('2021-01-02')); // -1
 */
export function contrastDate (date1, date2, format = 'YYYY-MM-DD HH:mm:ss') {
	const t1 = timeFormat(date1, format)
	const t2 = timeFormat(date2, format)
	if (t1 > t2) return 1
	if (t1 === t2) return 0
	return -1
}

/**
 * @desc 是否为空
 * @param {*} val - 要检查的值
 * @returns {Boolean} 如果为空则返回 true
 * @example
 * // 检查值是否为空
 * isEmpty(null); // true
 * isEmpty([]); // true
 * isEmpty([1, 2]); // false
 * isEmpty(''); // true
 */
export function isEmpty (val) {
	if (!val) return true
	if (isArray(val)) {
		return val.length === 0 || !val.some((item => item))
	}
	return false
}

/**
 * @desc 判断两个值是否相等
 * @param {String} mode - 模式 ('single' 或其他)
 * @param {*} val - 第一个值
 * @param {*} dates - 第二个值
 * @returns {Boolean} 如果相等则返回 true
 * @example
 * // 判断单个日期是否相等
 * isEqual('single', '2021-01-01', '2021-01-01'); // true
 * 
 * // 判断多个日期是否相等
 * isEqual('multiple', ['2021-01-01', '2021-01-02'], ['2021-01-01', '2021-01-02']); // true
 */
export function isEqual (mode, val, dates) {
	const emptyVal = isEmpty(val)
	const emptyDates = isEmpty(dates)
	if (emptyVal && emptyDates) return true
	if (emptyVal && !emptyDates || !emptyVal && emptyDates) return false
	if (mode === 'single') {
		return contrastDate(new Date(dayjs(dates).toDate()), new Date(dayjs(val).toDate())) === 0
	}
	if (val.length !== dates.length) return false
	return val.every((timestamp, i) => contrastDate(new Date(dayjs(dates[i]).toDate()),
		new Date(dayjs(timestamp).toDate())) === 0)
}

/**
 * @desc 判断两个日期是否在同一个面板中
 * @param {Array} dates - 日期数组
 * @param {String} format - 格式化字符串
 * @returns {Boolean} 如果在同一个面板中则返回 true
 * @example
 * // 判断两个日期是否在同一个面板中
 * inOnePanel([new Date('2021-01-01'), new Date('2021-01-15')], 'YYYY-MM-DD'); // true
 * inOnePanel([new Date('2021-01-01'), new Date('2021-02-01')], 'YYYY-MM-DD'); // false
 */
export function inOnePanel (dates, format) {
	const [start, end] = dates
	if (start && end) {
		if (format.indexOf('D') !== -1) {
			if (timeFormat(start, 'YYYYMM') === timeFormat(end, 'YYYYMM')) {
				return true
			}
		} else if (format.indexOf('M') !== -1) {
			if (start.getFullYear() === end.getFullYear()) {
				return true
			}
		} else if (Number.parseInt(start.getFullYear() / 10) ===
			Number.parseInt(end.getFullYear() / 10)) {
			return true
		}
	}
	return false
}

export default {timeFormat}