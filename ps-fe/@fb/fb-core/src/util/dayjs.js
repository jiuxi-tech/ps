/*!
 * dayjs
 * (c) 2021 lincong1987
 */


import dayjs from 'dayjs'
import isLeapYear from 'dayjs/plugin/isLeapYear'
import advancedFormat from 'dayjs/plugin/advancedFormat'
import isBetween from 'dayjs/plugin/isBetween'
import isSameOrAfter from 'dayjs/plugin/isSameOrAfter'
import isSameOrBefore from 'dayjs/plugin/isSameOrBefore'
import isToday from 'dayjs/plugin/isToday'
import isTomorrow from 'dayjs/plugin/isTomorrow'
import isYesterday from 'dayjs/plugin/isYesterday'
import objectSupport from 'dayjs/plugin/objectSupport'
import relativeTime from 'dayjs/plugin/relativeTime'
import toArray from 'dayjs/plugin/toArray'
import toObject from 'dayjs/plugin/toObject'
import weekOfYear from 'dayjs/plugin/weekOfYear'
import weekYear from 'dayjs/plugin/weekYear'
import customParseFormat from 'dayjs/plugin/customParseFormat'
import dayOfYear from 'dayjs/plugin/dayOfYear'
import duration from 'dayjs/plugin/duration'

import 'dayjs/locale/zh-cn' // import locale


dayjs.extend(isLeapYear)
dayjs.extend(advancedFormat)
dayjs.extend(isBetween)
dayjs.extend(isSameOrAfter)
dayjs.extend(isSameOrBefore)
dayjs.extend(isToday)
dayjs.extend(isTomorrow)
dayjs.extend(isYesterday)
dayjs.extend(objectSupport)
dayjs.extend(relativeTime)
dayjs.extend(toArray)
dayjs.extend(toObject)
dayjs.extend(weekOfYear)
dayjs.extend(weekYear)
dayjs.extend(customParseFormat)
dayjs.extend(dayOfYear)
dayjs.extend(duration)
dayjs.locale('zh-cn')

export default dayjs
