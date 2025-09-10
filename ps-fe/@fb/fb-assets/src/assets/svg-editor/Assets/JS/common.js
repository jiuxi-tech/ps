var currFormLinkFormIndex;
var currFormLinkFormResult;
var currFormLink;

var mouseX;
var mouseY;
var sX;
var sY;

var btDropdownCurrent;


if (!Array.prototype.forEach) {
    Array.prototype.forEach = function forEach(callback, thisArg) {
        var T, k;
        if (this == null) {
            throw new TypeError("this is null or not defined");
        }
        var O = Object(this);
        var len = O.length >>> 0;
        if (typeof callback !== "function") {
            throw new TypeError(callback + " is not a function");
        }
        if (arguments.length > 1) {
            T = thisArg;
        }
        k = 0;
        while (k < len) {
            var kValue;
            if (k in O) {
                kValue = O[k];
                callback.call(T, kValue, k, O);
            }
            k++;
        }
    };
}
$(function () {
    $.extend(String.prototype, {
        equals: function (str) {
            return this == str;
        },
        contains: function (searchstr) {
            return this.indexOf(searchstr) != -1;
        },
        startWith: function (str) {
            var reg = new RegExp("^" + str);
            return reg.test(this);
        },
        endWith: function (str) {
            var reg = new RegExp(str + "$");
            return reg.test(this);
        },
        toDate: function () {
            return new Date(Date.parse(this.replace(/-/g, "/")));
        },
        formatDateTime: function () {
            return this._formatDate("yyyy-MM-dd hh:mm:ss");
        },
        formatDateMinute: function () {
            return this._formatDate("yyyy-MM-dd hh:mm");
        },
        formatDate: function () {
            return this._formatDate("yyyy-MM-dd");
        },
        formatDateRange: function (date1, date2, seperate) {
            seperate = seperate || "至";
            if (date1 && date2) {
                if (date1 == date2) {
                    return date1.formatDate();
                }
                else {
                    return date1.formatDate() + " " + seperate + " " + date2.formatDate();
                }
            }
            return "";
        },
        formatDateMinuteRange: function (date1, date2, seperate) {
            seperate = seperate || "至";
            if (date1 && date2) {
                if (date1 == date2) {
                    return date1.formatDateMinute();
                }
                else {
                    return date1.formatDateMinute() + " " + seperate + " " + date2.formatDateMinute();
                }
            }
            return "";
        },
        _formatDate: function (format) {
            var str = this;
            if (this && this.length > 0) {
                if (str.indexOf("T") > 0) {
                    str = str.replace("T", " ");
                }
                str = str.toDate();
                return str.format(format);
            }
            return "";
        },
        replaceAll: function (searchValue, replaceValue) {
            var reg = new RegExp(searchValue, "g");
            return this.replace(reg, replaceValue);
        },
        isInt: function () {
            var reg = /^(0|\+?[1-9][0-9]*)$/;
            return reg.test(this);
        },
        isDecimal: function () {
            var reg = /^(([0-9]+)|([0-9]+\.[0-9]{1,2}))$/;
            return reg.test(this);
        },
        format: function (args) {
            var result = this;
            if (arguments.length > 0) {
                if (arguments.length == 1 && typeof (args) == "object") {
                    for (var key in args) {
                        if (args[key] != undefined) {
                            var reg = new RegExp("({" + key + "})", "g");
                            result = result.replace(reg, args[key]);
                        }
                    }
                }
                else {
                    for (var i = 0; i < arguments.length; i++) {
                        if (arguments[i] != undefined) {
                            var reg = new RegExp("({[" + i + "]})", "g");
                            result = result.replace(reg, arguments[i]);
                        }
                    }
                }
            }
            return result;
        },
        formatNumber: function (decimals, dec_point, thousands_sep) {
            var number = this;
            number = (number + '').replace(/[^0-9+-Ee.]/g, '');
            var n = !isFinite(+number) ? 0 : +number,
                prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
                sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
                dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
                s = '',
                toFixedFix = function (n, prec) {
                    var k = Math.pow(10, prec);
                    return '' + Math.floor((n * k).toFixed()) / k;
                };
            s = (prec ? toFixedFix(n, prec) : '' + Math.floor(n)).split('.');
            var re = /(-?\d+)(\d{3})/;
            while (re.test(s[0])) {
                s[0] = s[0].replace(re, "$1" + sep + "$2");
            }
            if ((s[1] || '').length < prec) {
                s[1] = s[1] || '';
                s[1] += new Array(prec - s[1].length + 1).join('0');
            }
            return s.join(dec);
        },
        formatMoney: function () {
            return this.formatNumber(2)
        },
        trimStart: function (c) {
            if (c == null || c == "") {
                var str = this.replace(/^s*/, '');
                return str;
            }
            else {
                var rg = new RegExp("^" + c + "*");
                var str = this.replace(rg, '');
                return str;
            }
        },
        trimEnd: function (c) {
            if (c == null || c == "") {
                var str = this;
                var rg = /s/;
                var i = str.length;
                while (rg.test(str.charAt(--i)));
                return str.slice(0, i + 1);
            }
            else {
                var str = this;
                var rg = new RegExp(c);
                var i = str.length;
                while (rg.test(str.charAt(--i)));
                return str.slice(0, i + 1);
            }
        }
        //,
        //hasValue: function () {
        //    var val = this;
        //    if (val === undefined || val === "" || $.trim(val) === "" || val === null)
        //        return true;
        //    else
        //        return false;
        //}
    })

    $.extend(Array.prototype, {
        contains: function (elem) {
            for (var i = 0; i < this.length; i++) {
                if (this[i] == elem) {
                    return true;
                }
            }
            return false;
        },
        containsWithKey: function (elem, key) {
            for (var i = 0; i < this.length; i++) {
                if (this[i][key] == elem[key]) {
                    return true;
                }
            }
            return false;
        },
        containsWithFunc: function (func) {
            for (var i = 0; i < this.length; i++) {
                if (func(this[i])) {
                    return true;
                }
            }
            return false;
        },
        getIndexWithKey: function (elem, key) {
            for (var i = 0; i < this.length; i++) {
                if (this[i][key] == elem[key]) {
                    return i;
                }
            }
            return -1;
        },
        removeWithKey: function (key, value) {
            for (var i = 0; i < this.length; i++) {
                if (this[i][key] == value) {
                    var item = this[i];
                    this.splice(i, 1);
                    return item;
                }
            }
        },
        joinKeyWith: function (key, seperate, removerepeat) {
            var array = [];
            for (var i = 0; i < this.length; i++) {
                if (removerepeat == true) {
                    if ($.inArray(this[i][key], array) < 0) {
                        array.push(this[i][key]);
                    }
                }
                else {
                    array.push(this[i][key]);
                }
            }
            return array.join(seperate);
        },
        joinDisplayWith: function (value, key, display, seperate, sourceseperate) {
            seperate = seperate || ",";
            sourceseperate = sourceseperate || ",";
            var array = [];
            if (value) {
                var valueArr = value.split(sourceseperate);
                for (var i = 0; i < valueArr.length; i++) {
                    for (var j = 0; j < this.length; j++) {
                        if (valueArr[i] == this[j][key]) {
                            array.push(this[j][display]);
                            break;
                        }
                    }
                }
            }
            return array.join(seperate);

        },
        remove: function (item, all) {
            var result, isType = Object.prototype.toString, i, len, start, hasLast = arguments[2];
            start = 0, len = this.length;
            for (i = start; i < len;) {
                var isPass = true, inx;
                if (!hasLast) {
                    inx = i;
                } else {
                    inx = len - 1;
                }
                if (isType.call(item) == '[object Array]') {
                    for (var ii = 0, iimax = item.length; ii < iimax; ii++) {
                        if (this[inx] === item[ii]) {
                            isPass = false;
                            break;
                        }
                    }
                } else if (this[inx] === item) {
                    isPass = false;
                }
                if (!isPass) {
                    result = true;
                    this.splice(inx, 1);
                    if (!all) {
                        break;
                    }
                } else if (!hasLast) {
                    len = this.length;
                    i++;
                } else {
                    len--;
                }
            }
            return result ? this : void 0;
        },
        pushAll: function (data) {
            if (data) {
                for (var i = 0; i < data.length; i++) {
                    this.push(data[i]);
                }
            }
        },
        find: function (func) {
            var arr = this;
            for (var i = 0; i < arr.length; i++) {
                if (func(arr[i])) {
                    return arr[i];
                }
            }
        },
        where: function (func) {
            var arr = this;
            var r = [];
            for (var i = 0; i < arr.length; i++) {
                if (func(arr[i])) {
                    r.push(arr[i]);
                }
            }
            return r;
        },
        insert: function (index, elem) {
            this.splice(index, 0, elem);
        }
    })

    $.extend(Date.prototype, {
        format: function (fmt) {
            var o = {
                "M+": this.getMonth() + 1, //月份 
                "d+": this.getDate(), //日 
                "h+": this.getHours(), //小时 
                "m+": this.getMinutes(), //分 
                "s+": this.getSeconds(), //秒 
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
                "S": this.getMilliseconds() //毫秒 
            };
            if (/(y+)/.test(fmt))
                fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o) {
                if (new RegExp("(" + k + ")").test(fmt)) {
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
                }
            }
            return fmt;
        },
        getDateDiff: function () {
            var minute = 1000 * 60;
            var hour = minute * 60;
            var day = hour * 24;
            var halfamonth = day * 15;
            var month = day * 30;
            var now = new Date().getTime();
            var diffValue = now - this;
            if (diffValue < 0) {
                //若日期不符则弹出窗口告之  
                return "结束日期不能小于开始日期！";
            }
            var monthC = diffValue / month;
            var weekC = diffValue / (7 * day);
            var dayC = diffValue / day;
            var hourC = diffValue / hour;
            var minC = diffValue / minute;
            if (monthC >= 1) {
                result = parseInt(monthC) + " 个月前";
            }
            else if (weekC >= 1) {
                result = parseInt(weekC) + " 周前";
            }
            else if (dayC >= 1) {
                result = parseInt(dayC) + " 天前";
            }
            else if (hourC >= 1) {
                result = parseInt(hourC) + " 小时前";
            }
            else if (minC >= 1) {
                result = parseInt(minC) + " 分钟前";
            } else
                result = "刚刚发表";
            return result;
        },
        toISOString: function () {
            function pad(n) { return n < 10 ? '0' + n : n }
            return this.getUTCFullYear() + '-'
                + pad(this.getUTCMonth() + 1) + '-'
                + pad(this.getUTCDate()) + 'T'
                + pad(this.getUTCHours()) + ':'
                + pad(this.getUTCMinutes()) + ':'
                + pad(this.getUTCSeconds()) + '.'
                + pad(this.getUTCMilliseconds()) + 'Z';
        },
        addHours: function (hours) {
            var tempDate = new Date(this.valueOf() + 60 * 60 * 1000 * hours);
            return tempDate;
        }
    })

    $.extend({
        showSuccess: function (text, callback) {
            var layer = $.getRootLayer();
            var index = layer.alert(text, {
                icon: 1,
                shade: 0.3,
                time: 0
            }, function () {
                if (callback) {
                    callback();
                }
                layer.close(index);
            });
        },
        showError: function (text, callback) {
            if (text) {
                var layer = $.getRootLayer();
                var index = layer.alert(text, {
                    icon: 2,
                    shade: 0.3,
                    time: 0
                }, function () {
                    if (callback) {
                        callback();
                    }
                    layer.close(index);
                });
            } else {
                if (callback) {
                    callback();
                }
            }
        },
        showWarning: function (text, callback) {
            var layer = $.getRootLayer();
            var index = layer.alert(text, {
                icon: 5,
                shade: 0.3,
                time: 0
            }, function () {
                if (callback) {
                    callback();
                }
                layer.close(index);
            });
        },
        showNote: function (text, title) {
            var layer = $.getRootLayer();
            layer.alert(text, {
                title: title || "备注",
                shade: 0.3,
                time: 0
            });
        },
        showAlert: function (text) {
            $.showWarning(text);
        },
        showConfirm: function (text, callback, yesText, cancelText) {
            yesText = yesText || '确定';
            cancelText = cancelText || '取消';
            var index = $.getRootLayer().confirm(text, {
                btn: [yesText, cancelText] //按钮
            }, function () {
                callback(true);
                $.getRootLayer().close(index);
            }, function () {
                callback(false);
                $.getRootLayer().close(index);
            });
        },
        showConfirm2: function (text, callback, yesText, cancelText, closeText) {
            yesText = yesText || '确定';
            cancelText = cancelText || '取消';
            closeText = closeText || '关闭';
            var index = $.getRootLayer().confirm(text, {
                btn: [yesText, cancelText, closeText] //按钮
            }, function () {
                callback(true);
                $.getRootLayer().close(index);
            }, function () {
                callback(false);
                $.getRootLayer().close(index);
            }, function () {
                $.getRootLayer().close(index);
            });
        },
        showPrompt: function (text, value, func) {
            $.getRootLayer().prompt({
                title: text,
                value: value
            }, function (pass, index) {
                var result = func(pass);
                if (result != false) {
                    $.getRootLayer().close(index);
                }
            });
        },
        showLoading: function (text) {
            if (!text) {
                text = '加载中';
            }
            return $.getRootLayer().msg(text, {
                icon: 16,
                shade: 0.3,
                time: 0
            });
        },
        showPrev: function (src, start) {
            start = start || 0;
            if (src) {
                var photos = [];
                src.split(";").forEach(function (e) {
                    photos.push({
                        src: e
                    })
                });
                var data = {
                    "title": "图册",
                    "start": start,
                    "data": photos
                }
                try {
                    $.getRootLayer().photos({
                        photos: data,
                        anim: 5,
                        shade: 0.4
                    });
                } catch (e) {
                    var result = {
                        src: window.location.origin + src,
                        type: "preview"
                    }
                    window.parent.postMessage(result, '*')
                }
            }
        },
        showMessage: function (text, callback) {
            var index = $.getRootLayer().alert(text, {
                icon: 3,
                shade: 0.3,
                time: 0
            }, function () {
                if (callback) {
                    callback();
                }
                $.getRootLayer().close(index);
            });
        },

        closeLoading: function (index) {
            $.getRootLayer().close(index);
        },
        checkLogin: function () {
            return true;
        },
        formatDateTime: function (date) {
            if (date)
                return date.formatDateTime();
            return "";
        },
        formatDateMinute: function (date) {
            if (date)
                return date.formatDateMinute();
            return "";
        },
        formatDate: function (date) {
            if (date)
                return date.formatDate();
            return "";
        },
        formatDateRange: function (date1, date2, seperate) {
            seperate = seperate || "至";
            if (date1 && date2) {
                if (date1 == date2) {
                    return date1.formatDate();
                }
                else {
                    return date1.formatDate() + " " + seperate + " " + date2.formatDate();
                }
            }
            return "";
        },
        formatDateMinuteRange: function (date1, date2, seperate) {
            seperate = seperate || "至";
            if (date1 && date2) {
                if (date1 == date2) {
                    return date1.formatDateMinute();
                }
                else {
                    return date1.formatDateMinute() + " " + seperate + " " + date2.formatDateMinute();
                }
            }
            return "";
        },
        formatNumber: function (number, decimals) {
            if (!isNaN(number) && number != null)
                return number.toString().formatNumber(decimals);
            return "";
        },
        formatMoney: function (number) {
            if (!isNaN(number) && number != null)
                return number.toString().formatMoney();
            return "";
        },
        formatPercent: function (number) {
            if (!isNaN(number) && number != null)
                return number.toString() + '%';
            return "";
        },
        nullOrEmptyDefault: function (val, str) {
            if (!val || val == "" || $.trim(val) == "")
                return str;
            else
                return val;
        },
        numberValidate: function (obj) {
            var tempValue = obj.value;
            tempValue = tempValue.replace(/[^\d.]/g, ""); //清除"数字"和"."以外的字符
            tempValue = tempValue.replace(/^\./g, ""); //验证第一个字符是数字
            tempValue = tempValue.replace(/\.{2,}/g, "."); //只保留第一个, 清除多余的
            tempValue = tempValue.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
            tempValue = tempValue.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); //只能输入两个小数
            obj.value = tempValue;
        },
        intnumberValidate: function (obj) {
            var tempValue = obj.value;
            tempValue = tempValue.replace(/[^\d]/g, ""); //清除"数字"和"."以外的字符
            obj.value = tempValue;
        },
        isNullOrEmpty: function (val) {
            if (val === undefined || val === "" || $.trim(val) === "" || val === null)
                return true;
            else
                return false;
        },
        keepOneDecimalFull: function (num) {
            var result = parseFloat(num);
            if (isNaN(result)) {
                //alert("传递参数错误,请检查!");
                return 0;
            }
            result = Math.round(num * 100) / 100;
            var s_x = result.toString();
            var pos_decimal = s_x.indexOf('.');
            if (pos_decimal < 0) {
                pos_decimal = s_x.length;
                s_x += '.';
            }
            while (s_x.length <= pos_decimal + 1) {
                s_x += '0';
            }
            return s_x;
        },
        keepTwoDecimalFull: function (num) {
            var result = parseFloat(num);
            if (isNaN(result)) {
                //alert("传递参数错误,请检查!");
                return 0;
            }
            result = Math.round(num * 100) / 100;
            var s_x = result.toString();
            var pos_decimal = s_x.indexOf('.');
            if (pos_decimal < 0) {
                pos_decimal = s_x.length;
                s_x += '.';
            }
            while (s_x.length <= pos_decimal + 2) {
                s_x += '0';
            }
            return s_x;
        },
        conditionJudge: function (condition, trueValue, falseValue) {
            var isTrue = $.parseBool(condition.toString());
            if (isTrue) {
                return trueValue;
            }
            return falseValue;
        },

        loadingPost: function (url, parameter, callback, contentType) {
            if ($.checkLogin()) {
                var layerIndex = $.showLoading();
                var successFunc = function (data) {
                    $.closeLoading(layerIndex);
                    if (data.success) {
                        if (callback != null) {
                            callback(data);
                        }
                        else {
                            if (data.info) {
                                $.alert(data.info, function () {
                                    $.loadUrlOrRefresh(data.url)
                                });
                            }
                            else {
                                $.loadUrlOrRefresh(data.url)
                            }
                        }
                    }
                    else {
                        if (data.errorType == "5") {
                            //打开自定义流程界面
                            var array = data.errorText.split("|");
                            var url = "/CommonAudit/CustomSubmit";
                            url = $.updateUrlParam(url, "id", array[0]);
                            url = $.updateUrlParam(url, "opController", array[1]);
                            url = $.updateUrlParam(url, "operate", array[2]);
                            url = $.updateUrlParam(url, "defineID", array[3]);
                            var idx = $(this).formLink({
                                url: url,
                                callback: callback
                            })
                        }
                        else if (data.errorText) {
                            $.showWarning(data.errorText, function () {
                                $.loadUrl(data.url)
                            });
                        }
                        else {
                            $.loadUrl(data.url)
                        }
                    }
                }
                var errorFunc = function (result, errorStatus, errorText) {
                    $.closeLoading(layerIndex);
                    $.showError(errorText);
                    if (callback != null) {
                        callback({
                            success: false
                        });
                    }
                }
                $.ajax({
                    url: url,
                    type: "post",
                    data: parameter,
                    dataType: "json",
                    contentType: contentType,
                    success: successFunc,
                    error: errorFunc
                })
            }
        },
        loadingGet: function (url, parameter, callback) {
            var layerIndex = $.showLoading();
            url = $.updateUrl(url);
            $.get(url, parameter, function (data) {
                $.closeLoading(layerIndex);
                if (callback != null) {
                    callback(data);
                }
                else {
                    if (data.success) {
                        if (data.info) {
                            $.alert(data.info, function () {
                                $.loadUrlOrRefresh(data.url)
                            });
                        }
                        else {
                            $.loadUrlOrRefresh(data.url)
                        }
                    }
                    else {
                        if (data.error) {
                            $.alert(data.error, function () {
                                $.loadUrl(data.url)
                            });
                        }
                        else {
                            $.loadUrl(data.url)
                        }
                    }
                }
            }).error(function (result, errorStatus, errorText) {
                $.closeLoading(layerIndex);
                $.showError(errorText);
            });
        },
        get2: function (url, data, callback) {
            url = $.updateUrl(url);
            return $.get(url, data, callback).error(function (result, errorStatus, errorText) {
                if (result.readyState != 0) {
                    $.showError(errorText);
                }
            });

        },
        post2: function (url, data, callback) {
            return $.post(url, data, callback).error(function (result, errorStatus, errorText) {
                $.showError(errorText);
            });
        },
        loadUrlOrRefresh: function (url) {
            if (url) {
                window.location.href = url;
            }
            else {
                window.location.href = $.updateUrl(window.location.href)
            }
        },
        loadUrl: function (url) {
            if (url) {
                window.location.href = url;
            }
        },
        openUrl: function (url, target) {
            if (url) {
                if (!target) {
                    target = "_blank";
                }
                window.open(url, target);
            }
        },
        openDownload: function (url, name) {
            if (url) {
                try {
                    var httpRequest = new XMLHttpRequest();
                    var pointIndex = url.lastIndexOf(".");
                    var ext = url.substr(pointIndex);
                    if (name && !name.contains('.')) {
                        name = name + ext;
                    }
                    if (ext.endWith(".pdf")) {
                        httpRequest.responseType = 'arraybuffer';
                        httpRequest.open('GET', url, true);
                        httpRequest.onload = function () {
                            if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                                var content = httpRequest.response;
                                var blob = new Blob([content]);
                                var elink = document.createElement('a');
                                elink.download = name;
                                elink.style.display = 'none';
                                elink.href = URL.createObjectURL(blob);
                                document.body.appendChild(elink);
                                elink.click();
                                document.body.removeChild(elink);
                            }
                        };
                        httpRequest.send();
                    }
                    else {
                        var link = document.createElement('a')
                        link.style.display = 'none'
                        link.href = url;
                        link.target = "_blank";
                        if (name) {
                            link.setAttribute(
                                'download',
                                name
                            )
                        }
                        document.body.appendChild(link)
                        link.click()
                    }
                } catch (e) {
                    var link = document.createElement('a')
                    link.style.display = 'none'
                    link.href = url;
                    link.target = "_blank";
                    if (name) {
                        link.setAttribute(
                            'download',
                            name
                        )
                    }
                    document.body.appendChild(link)
                    link.click()
                }
            }
        },
        openAttachDownload: function (url, name) {
            $.openDownload(url, name);
        },
        openUrlWithTarget: function (url, target) {
            if (url && target) {
                window.open(url, target);
            }
        },
        openMainUrl: function (url, target) {
            $(window.top.document.getElementById("iframecontent")).attr("src", url);
            $.getRootLayer().closeAll();
        },
        updateUrl: function (url, key) {
            var key = (key || 'timestamp') + '=';  //默认是"t"
            var reg = new RegExp(key + '\\d+');  //正则：t=1472286066028
            var timestamp = +new Date();
            if (url.indexOf(key) > -1) { //有时间戳，直接更新
                return url.replace(reg, key + timestamp);
            } else {  //没有时间戳，加上时间戳
                if (url.indexOf('\?') > -1) {
                    var urlArr = url.split('\?');
                    if (urlArr[1]) {
                        return urlArr[0] + '?' + key + timestamp + '&' + urlArr[1];
                    } else {
                        return urlArr[0] + '?' + key + timestamp;
                    }
                } else {
                    if (url.indexOf('#') > -1) {
                        return url.split('#')[0] + '?' + key + timestamp + location.hash;
                    } else {
                        return url + '?' + key + timestamp;
                    }
                }
            }
        },
        updateUrlParam: function (url, key, value) {
            var params = "";
            if (url.indexOf("?") >= 0) {
                params = url.substring(url.indexOf("?") + 1);
                url = url.substring(0, url.indexOf("?"));
            }
            if (params.indexOf('#') > -1) {
                params = params.substring(0, params.indexOf('#'));
            }
            if (params) {
                params = params.split('&');
            }
            else {
                params = [];
            }
            var flag = false;
            for (var i = 0; i < params.length; i++) {
                if (params[i].startWith(key + "=")) {
                    params[i] = key + "=" + (value || "");
                    flag = true;
                    break;
                }
            }
            if (!flag)
                params.push(key + "=" + (value || ""));
            url = url + "?" + params.join("&");
            return url;
        },
        formLinkCore: function (url, callback) {
            var me = $(this);
            var config = {
                width: 300,
                height: 300,
                cancel: function (index, lay) {
                    var cancelCallback = me.data("cancel-callback");
                    var cancelConfirmText = me.data("cancel-confirm-text");
                    if (cancelCallback) {
                        if (cancelConfirmText) {
                            $.showConfirm(cancelConfirmText, function (result) {
                                if (result) {
                                    $.getRootLayer().close(index);
                                    var func = window[cancelCallback];
                                    if (func) {
                                        func.call(me);
                                    }
                                }
                            })
                        }
                        else {
                            $.getRootLayer().close(index);
                            var func = window[cancelCallback];
                            if (func) {
                                func.call(me);
                            }
                        }
                        return false;
                    }
                    else {
                        return true;
                    }

                }
            }
            if (typeof url == "string") {
                config.url = url;
                config.callback = callback;
            }
            else {
                $.extend(config, url);
            }
            var windowCacheKey;
            if (config.url) {
                windowCacheKey = "window_" + (config.url.indexOf("?") > 0 ? config.url.substring(0, config.url.indexOf("?")) : config.url);
                var cache = localStorage.getItem(windowCacheKey);
                if (cache) {
                    var array = cache.split(",");
                    config.width = parseInt(array[0]);
                    config.height = parseInt(array[1]);
                }
            }
            var buttonFunction = function (index, lay, buttonIndex) {
                var flag = false;
                var text = this.btn[buttonIndex];
                if (config.callback && text != "关闭") {
                    flag = config.callback.call(me, index, lay, buttonIndex, text);
                }
                if (flag) {
                    $.getRootLayer().close(index);
                }
                else if (text == "关闭") {
                    var result = config.cancel.call(this, index, lay);
                    if (result) {
                        $.getRootLayer().close(index);
                    }
                }
                return flag;
            }
            currFormLink = $(this);
            try {
                var index = $.getRootLayer().open({
                    type: 2,
                    title: "　",
                    fixed: true,
                    maxmin: true,
                    shadeClose: false,
                    skin: 'layui-layer-rim', //加上边框
                    area: me.hasClass("full-window") ? ['100%', '100%'] : [config.width + "px", config.height + "px"],
                    content: config.url,
                    btn: ["", "", "", "", "关闭"],
                    btn2: function (index, lay) {
                        buttonFunction.call(this, index, lay, 1);
                        return false;
                    },
                    btn3: function (index, lay) {
                        buttonFunction.call(this, index, lay, 2);
                        return false;
                    },
                    btn4: function (index, lay) {
                        buttonFunction.call(this, index, lay, 3);
                        return false;
                    },
                    btn5: function (index, lay) {
                        buttonFunction.call(this, index, lay, 4);
                        return false;
                    },
                    yes: function (index, lay) {
                        buttonFunction.call(this, index, lay, 0);
                        return false;
                    },
                    cancel: function (index, lay) {
                        buttonFunction.call(this, index, lay, 4);
                        return false;
                    },
                    success: function (lay) {
                        var win = $.getRootWindow();
                        var frame = lay.find("iframe")[0];
                        try {
                            var title = frame.contentWindow.document.title;
                            lay.find(".layui-layer-title").text(title);
                            var windowConfig = frame.contentWindow.$("#windowConfig");
                            if (windowConfig.length > 0) {
                                if (!me.hasClass("full-window")) {
                                    var width = windowConfig.data("width");
                                    var height = windowConfig.data("height");
                                    if (windowCacheKey) {
                                        localStorage.setItem(windowCacheKey, width + "," + height);
                                    }

                                    var isfull = windowConfig.data("isfull");
                                    var windowButtons = windowConfig.data("windowbuttons");
                                    var windowButtonsArray = [];
                                    if (windowButtons) {
                                        windowButtonsArray = windowButtons.split(",");
                                    }
                                    if (windowButtonsArray.length > 0) {
                                        lay.find(".layui-layer-btn").css("display", "block");
                                        var windowSkin = windowConfig.data("windowskin");
                                        if (windowSkin) {
                                            lay.find(".layui-layer-btn").addClass(windowSkin);
                                        }
                                        lay.find(".layui-layer-btn a").each(function (index) {
                                            if (windowButtonsArray.length > index + 1)
                                                $(this).text(windowButtonsArray[index]);
                                            else if ($(this).text() != "关闭")
                                                $(this).css("display", "none");
                                        })
                                    }
                                    var left = $(win).width() / 2 - width / 2;
                                    var top = ($(win).height()) / 2 - height / 2;
                                    lay.width(width);
                                    lay.height(height);
                                    $(frame).height(height - (windowButtonsArray.length ? 98 : 0));
                                    lay.css("left", left);
                                    lay.css("top", top);
                                    if (isfull && isfull == 1) {
                                        layer.full(index);
                                    }
                                }
                                frame.contentWindow.$(".btn-confirm").click(function (e) {
                                    e.preventDefault();
                                    config.callback.call(this, index, lay, $(this).data("index"));
                                })
                            }
                            var cusWindowTool = frame.contentWindow.$("#cusWindowTool");
                            if (cusWindowTool.length > 0) {
                                var div = $("<div class='cus-window-tool'/>");
                                div.append(cusWindowTool);
                                lay.find(".layui-layer-btn").append(div);
                            }
                            if (windowConfig.data("autoclose")) {
                                $.getRootLayer().close(index);
                            }
                        } catch (e) {
                            frame.contentWindow.postMessage("", '*')
                        }

                    }
                });
            } catch (e) {
                var result = {
                    type: "open",
                    url: config.url
                }
                window.parent.postMessage(result, '*')
            }
        },
        noTitleLink: function (url) {
            var me = $(this);
            var width = me.data("width") || 300;
            var height = me.data("height") || 300;
            var url = me.attr("href");
            var config = {
                type: 2,
                title: false,
                area: [width + "px", height + "px"],
                content: url
            };
            $.getRootLayer().open(config);
        },
        closeCurrentFormLink: function (i) {
            if (currFormLinkFormIndex >= 0) {
                currFormLinkFormResult = i;
                $.getRootLayer().close(currFormLinkFormIndex);
                currFormLinkFormIndex = -1;
                currFormLink = null;
            }
        },
        loadScript: function (url, callback) {
            $scripts = $("script");
            var flag = false;
            $scripts.each(function () {
                if (url == $(this).attr("src")) {
                    flag = true;
                }
            })
            if (flag) {
                //临时处理，防止同一个页面多个form 加载未完成就执行
                setTimeout(function () {
                    callback();
                }, 100);
            }
            else {
                var script = document.createElement("script");
                script.type = "text/javascript";
                if (typeof (callback) != "undefined") {
                    if (script.readyState) {
                        script.onreadystatechange = function () {
                            if (script.readyState == "loaded" || script.readyState == "complete") {
                                script.onreadystatechange = null;
                                callback();
                            }
                        };
                    } else {
                        script.onload = function () {
                            callback();
                        };
                    }
                }
                script.src = url;
                document.body.appendChild(script);
            }
        },
        loadCss: function (url, callback) {
            $links = $("link");
            var flag = false;
            $links.each(function () {
                if (url == $(this).attr("href")) {
                    flag = true;
                }
            })
            if (flag) {
                callback();
            }
            else {
                var link = document.createElement("link");
                link.type = "text/css";
                link.rel = "stylesheet";
                link.href = url;
                document.body.appendChild(link);
                setTimeout(function () {
                    callback();
                }, 100);
            }
        },
        loadAssets: function (config, callback) {
            var array = [];
            if (config.css)
                for (var i = 0; i < config.css.length; i++) {
                    array.push(config.css[i]);
                }
            if (config.scripts)
                for (var i = 0; i < config.scripts.length; i++) {
                    array.push(config.scripts[i]);
                }
            if (array.length > 0) {
                var index = 0;
                var loadFunc = function () {
                    var item = array[index];
                    var callbackFunc = function () {
                        index++;
                        if (index < array.length) {
                            loadFunc()
                        }
                        else if (callback) {
                            callback();
                        }
                    }
                    if (item.endWith(".css")) {
                        $.loadCss(item, callbackFunc)
                    }
                    else {
                        $.loadScript(item, callbackFunc)
                    }
                }
                loadFunc();
            }
        },
        renderByParams: function (key, params) {
            if (params) {
                for (var i = 0; i < params.length; i++) {
                    if (params[i]["ID"] == key) {
                        return params[i]["Name"];
                    }
                }
            }
            return key;
        },
        template: function (template, d) {
            if (template) {
                var a = template.replace(new RegExp(/\{\{([^}]+)\}\}/g), function (match, field, index, text) {
                    var value = eval(field);
                    if (field && field.startWith("d.")) {
                        value = $.escape(value);
                    }
                    return value;
                });
                return a.replace(/undefined/g, "").replace(/null/g, "")
            }
            return "";
        },
        escape: function (value) {
            if (typeof (value) == "string") {
                value = value == undefined || value == null ? "" : value;
                value = value.replace(/\</, "&lt;")
                    .replace(/\>/, "&gt;")
                    .replace(/\"/, "&quot;");
            }
            return value;
        },
        validateInput: function (value) {
            //var illegalString = "[=<>\"";
            //if (illegalString.indexOf(value) >= 0) {
            //    return false;
            //}
            return true;
        },
        getRootWindow: function () {
            var win = window;
            if (win.parent != window && win.parent.layer) {
                win = win.parent;
            }
            return win;
        },
        getRootLayer: function () {
            var lay = layer;
            var win = window;
            var parentHost = $.getParentHost(win);
            while (win.parent && parentHost == win.window.location.host && win.parent != win && win.parent.layer) {
                win = win.parent;
                lay = win.layer;
                parentHost = $.getParentHost(win);
            }
            return lay;
        },
        getParentHost: function (window) {
            try {
                if (window.parent) {
                    return window.parent.window.location.host;
                }
            } catch (e) {

            }
            return "";
        },
        getAbsolutePosition: function (obj) {
            position = new Object();
            position.x = 0;
            position.y = 0;
            var tempobj = obj;
            while (tempobj != null && tempobj != document.body) {
                position.x += tempobj.offsetLeft + tempobj.clientLeft;
                position.y += tempobj.offsetTop + tempobj.clientTop;
                tempobj = tempobj.offsetParent
            }
            position.x += document.body.scrollLeft;
            position.y += document.body.scrollTop;
            return position;
        },
        checkIE: function () {
            if (navigator.userAgent.indexOf("MSIE") > 0) {
                if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
                    return 6;
                }
                if (navigator.userAgent.indexOf("MSIE 7.0") > 0) {
                    return 7;
                }
                if (navigator.userAgent.indexOf("MSIE 8.0") > 0 && !window.innerWidth) {//这里是重点，你懂的
                    return 8;
                }
                if (navigator.userAgent.indexOf("MSIE 9.0") > 0) {
                    return 9;
                }
            }
            return 9;
        },
        isChrome: function () {
            return navigator.userAgent.indexOf('Chrome/') >= 0;
        },
        renderHead: function (head) {
            if (head) {
                return head;
            }
            return "/Assets/Images/default-head.png";
        },
        renderImg: function (img, ext) {
            if (img) {
                return img;
            }
            if (ext == ".cou") {
                return "/Assets/Images/default-cou.jpg";
            }
            return "/Assets/Images/default-picture.png";
        },
        renderResStateClass: function (finishState) {
            if (finishState == "2") {
                return "finished";
            } else if (finishState == "1") {
                return "progressing";
            } else {
                return "notstart";
            }
        },
        renderResStateText: function (finishState) {
            if (finishState == "2") {
                return "已完成";
            } else if (finishState == "1") {
                return "学习中";
            } else {
                return "未学习";
            }
        },
        fileSize: function (size) {
            var suffix = "B";
            var size2 = size;
            if (size2 > 1024) {
                suffix = "KB";
                size2 /= 1024;
            }
            if (size2 > 1024) {
                suffix = "MB";
                size2 /= 1024;
            }
            if (size2 > 1024) {
                suffix = "GB";
                size2 /= 1024;
            }
            return Math.round(size2, 2) + " " + suffix;
        },
        getChineseNumber: function (number) {
            var chineseNumber = ["一", "二", "三", "四", "五", "六", "七", "八", "九", "拾"];
            return chineseNumber[number - 1];
        },
        parseBool: function (value) {
            if (value == "true" || value == "True") {
                return true;
            }
            return false;
        },
        parseJsonString: function (value) {
            if (typeof (value) == "string") {
                var val = value ? JSON.parse(value.replace(/\'/g, "\"").replaceAll("&quot;", "\"").replace(/\r\n/g, '').replace(/\n/, "").replace(/\n/g, "\\n").replace(/\r/g, "\\r")) : [];
                return val;
            }
            return value || [];
        },
        getDateDiffs: function (days) {
            var tag = "";
            if (days < 0) {
                tag = "剩";
                days = -days;
            }
            var yearLevelValue = 365;
            var monthLevelValue = 31;
            var year = parseInt((parseInt(days)) / yearLevelValue);
            var month = parseInt((days - year * yearLevelValue) / monthLevelValue);
            var day = parseInt(days - year * yearLevelValue - month * monthLevelValue);
            var result = "";
            if (year != 0) result = result + year + "年";
            if (month != 0) result = result + month + "月";
            if (day != 0)
                result = result + day + "天";
            return tag + result;
        },
        dateAdd: function (interval, number, date) {
            date = new Date(date);
            switch (interval) {
                case "y": {
                    var year = date.getFullYear() + number;
                    date.setFullYear(year);
                    return date;
                }
                case "q": {
                    date.setMonth(date.getMonth() + number * 3);
                    return date;
                }
                case "m": {
                    date.setMonth(date.getMonth() + number);
                    return date;
                }
                case "w": {
                    date.setDate(date.getDate() + number * 7);
                    return date;
                }
                case "d": {
                    date.setDate(date.getDate() + number);
                    return date;
                }
                case "h": {
                    date.setHours(date.getHours() + number);
                    return date;
                }
                case "m": {
                    date.setMinutes(date.getMinutes() + number);
                    return date;
                }
                case "s": {
                    date.setSeconds(date.getSeconds() + number);
                    return date;
                }
                default: {
                    date.setDate(d.getDate() + number);
                    return date;
                }
            }
        },
        newGuid: function () {
            var guid = "";
            for (var i = 1; i <= 32; i++) {
                var n = Math.floor(Math.random() * 16.0).toString(16);
                guid += n;
                if ((i == 8) || (i == 12) || (i == 16) || (i == 20))
                    guid += "-";
            }
            return guid;
        },
        getUrlParamArray: function (url) {
            var vars = [], hash;
            var hashes = url.slice(url.indexOf('?') + 1).split('&');
            for (var i = 0; i < hashes.length; i++) {
                hash = hashes[i].split('=');
                //vars.push(hash[0]);
                //vars[hash[0]] = hash[1];
                vars.push(hash[0]);
                vars[hash[0]] = hashes[i].slice(hashes[i].indexOf('=') + 1);
            }
            return vars;
        },
        getUrlParam: function (url, name) {
            return $.getUrlParamArray(url)[name];
        },
        attachsUploader: function (name) {
            return $("input[name=" + name + "]").parents(".bt-attachs-uploader-group").find(".bt-attachs-uploader").data("attachsuploader");
        }
    })

    $.fn.extend({
        serializeParams: function () {
            var form = this;
            var data = this.serializeArray();
            var params = {};
            for (var i = 0; i < data.length; i++) {
                var item = data[i];
                var name = item.name;
                if (params.hasOwnProperty(name) && item.name != "ID") {
                    params[name] = params[name] + "," + item.value;
                }
                else {
                    params[name] = item.value;
                }
            }
            //如果有明细 加上明细
            this.find(".form-details").each(function () {
                var table = $(this);
                var detailField = "Details";
                for (var cnt = 2; cnt < 14; cnt++) {
                    if (table.hasClass("form-details" + cnt)) {
                        detailField = "Details" + cnt;
                    }
                }
                var detailName = table.data("detail-name");
                if (!$.isNullOrEmpty(detailName)) {
                    detailField = detailName;
                }
                var allData = table.find("table").bootstrapTable('getData');
                if (allData.length > 0) {
                    for (var i = 0; i < allData.length; i++) {
                        var data = allData[i];
                        for (var field in data) {
                            var name = detailField + "[" + i + "]." + field;
                            if (!field.startWith("_") && !params.hasOwnProperty(name))
                                params[detailField + "[" + i + "]." + field] = data[field];
                        }
                    }
                }
                //else {
                //    params[detailField + "[0]"] = null;
                //}
            })
            return params;
        },
        loadUrlOrRefresh: function (url) {
            var tableCt = $(this).parents(".bt-table-ct");
            if (tableCt.length > 0) {
                var tables = tableCt.find(".bt-table");
                for (var i = 0; i < tables.length; i++) {
                    var table = tables[i];
                    if (table.id) {
                        document.defaultView.$("#" + table.id).bootstrapTable('refresh');
                    }
                }
                return;
            }
            var gridCt = $(this).parents(".bt-grid-ct");
            if (gridCt.length > 0) {
                var grid = gridCt.find(".bt-grid");
                document.defaultView.$("#" + grid.attr("id")).data('grid').refresh();
                return;
            }
            var btRadio = $(this).parents(".bt-radio");
            if (btRadio.length > 0) {
                btRadio.data("radio").loadUrl();
                return;
            }
            var btCheckboxGroup = $(this).parents(".bt-checkboxgroup");
            if (btCheckboxGroup.length > 0) {
                btCheckboxGroup.data("btCheckBoxGroup").loadUrl();
                return;
            }
            $.loadUrlOrRefresh();
        },
        changeUrl: function (value) {
            var url = $(this).data("url");
            if (url) {
                $(this).data("url", value);
            }
            else {
                url = $(this).attr("href");
                if (url) {
                    $(this).attr("href", url);
                }
            }
        },
        changeUrlParam: function (key, value) {
            var url = $(this).data("url");
            if (url) {
                url = $.updateUrlParam(url, key, value);
                $(this).data("url", url);
            }

            var url2 = $(this).attr("href");
            if (url2) {
                url2 = $.updateUrlParam(url2, key, value);
                $(this).attr("href", url2);
            }
            return $(this);
        },
        toggleDisabled: function (flag) {
            if (flag) {
                this.attr('disabled', 'disabled')
            }
            else {
                this.removeAttr("disabled");
            }
        },
        toggleReadonly: function (flag) {
            if (flag) {
                this.attr('readonly', 'readonly')
            }
            else {
                this.removeAttr("readonly");
            }
        },
        autoHeight: function () {
            function autoHeight(elem) {
                elem.style.minHeight = '25px';
                elem.scrollTop = 0; //防抖动  
                //console.log(elem.style.width);
                //elem.style.width = '600px';
                elem.style.height = 'auto';
                elem.style.marginBottom = 0;
                elem.style.paddingBottom = 0;
                elem.style.height = elem.scrollHeight + 2 + 'px';
            }
            this.each(function () {
                autoHeight(this);
                $(this).on('keyup', function () {
                    autoHeight(this);
                });
            });
        }
    })

    $.fn.extend({
        formLink: function (config) {
            var me = $(this);
            me.trigger("formLink.beforeLink", config);
            $.formLinkCore.call(this, {
                url: config.url,
                callback: function (index, lay, btnIndex) {
                    var frame = lay.find("iframe")[0];
                    var btn = $(lay.find(".layui-layer-btn a")[btnIndex]);
                    if (btn.attr("disabled") == undefined) {
                        var post = function () {
                            var forms = frame.contentWindow.$("form");
                            if (forms.length > 0) {
                                forms.each(function () {
                                    var form = frame.contentWindow.$(this);
                                    var action = form.attr('action');
                                    if (action) {
                                        var formCallbackFunc = form.data("callbackfunc");
                                        var bv = form.data('bootstrapValidator');
                                        try {
                                            form.bootstrapValidator('resetForm');
                                        } catch (e) {
                                        }
                                        form.off('success.form.bv')
                                            .on('success.form.bv', function (e) {
                                                e.preventDefault();
                                                var validatedCallback = form.data("validated-callbackfunc");
                                                if (validatedCallback && frame.contentWindow[validatedCallback]) {
                                                    var result = frame.contentWindow[validatedCallback].call(form);
                                                    if (result) {
                                                        var func = window[config.callback];
                                                        if (func)
                                                            func.call(me, result);
                                                        $.getRootLayer().close(index)
                                                    }
                                                }
                                                else {
                                                    btn.attr("disabled", "disabled");
                                                    btn.addClass("disabled");
                                                    var params = form.serializeParams();
                                                    var post = function () {
                                                        $.post2(action, params, function (result) {
                                                            if (result.success) {
                                                                function after(result) {
                                                                    var flag = true;
                                                                    var successFunc = function () {
                                                                        if (config.callback) {
                                                                            if (typeof (config.callback) == "string") {
                                                                                if (config.callback.startWith("refresh")) {
                                                                                    me.loadUrlOrRefresh();
                                                                                    $.getRootLayer().close(index)
                                                                                }
                                                                                else {
                                                                                    var func = window[config.callback];
                                                                                    if (func)
                                                                                        flag = func.call(me, result);
                                                                                    if (flag)
                                                                                        $.getRootLayer().close(index)
                                                                                }
                                                                            }
                                                                            else {
                                                                                flag = config.callback(result);
                                                                                if (flag)
                                                                                    $.getRootLayer().close(index)
                                                                            }
                                                                        }
                                                                    }
                                                                    var formFunc = frame.contentWindow[formCallbackFunc];
                                                                    if (formFunc)
                                                                        flag = formFunc(result, btnIndex, successFunc);
                                                                    if (flag) {
                                                                        successFunc()
                                                                    }
                                                                    btn.removeAttr("disabled");
                                                                    btn.removeClass("disabled");
                                                                    bv.disableSubmitButtons(false);
                                                                }
                                                                if (typeof result.data == "string" && result.data) {
                                                                    $.showSuccess(result.data, function () {
                                                                        after(result);
                                                                    });
                                                                }
                                                                else {
                                                                    after(result);
                                                                }
                                                            }
                                                            else {
                                                                btn.removeAttr("disabled");
                                                                btn.removeClass("disabled");
                                                                bv.disableSubmitButtons(false);

                                                                if (result.errorType == "5") {
                                                                    //打开自定义流程界面
                                                                    var array = result.errorText.split("|");
                                                                    var url = "/CommonAudit/CustomSubmit";
                                                                    url = $.updateUrlParam(url, "id", array[0]);
                                                                    url = $.updateUrlParam(url, "opController", array[1]);
                                                                    url = $.updateUrlParam(url, "operate", array[2]);
                                                                    url = $.updateUrlParam(url, "defineID", array[3]);
                                                                    $(this).formLink({
                                                                        url: url,
                                                                        callback: function () {
                                                                            me.loadUrlOrRefresh();
                                                                            $.getRootLayer().close(index)
                                                                            return true;
                                                                        },
                                                                        cancel: function () {
                                                                            me.loadUrlOrRefresh();
                                                                            $.getRootLayer().close(index)
                                                                            return true;
                                                                        }
                                                                    })
                                                                }
                                                                else if (result.errorText) {
                                                                    $.showWarning(result.errorText, function () {
                                                                        $.loadUrl(result.url)
                                                                    });
                                                                }
                                                            }
                                                        }, 'json');
                                                    }
                                                    var checkResult = true;
                                                    var beforecheckfunc = form.data("beforecheckfunc");
                                                    if (beforecheckfunc) {
                                                        var checkFunc = frame.contentWindow[beforecheckfunc];
                                                        if (checkFunc) {
                                                            checkResult = checkFunc.call(frame.contentWindow, params, btnIndex, post);
                                                        }
                                                    }
                                                    if (checkResult) {
                                                        var onjoinparamfunc = form.data("joinparamfunc")
                                                        if (onjoinparamfunc) {
                                                            var joinFunc = frame.contentWindow[onjoinparamfunc];
                                                            if (joinFunc) {
                                                                joinFunc.call(frame.contentWindow, params, btnIndex);
                                                            }
                                                        }
                                                        post();
                                                    } else {
                                                        btn.removeAttr("disabled");
                                                        btn.removeClass("disabled");
                                                        bv.disableSubmitButtons(false);
                                                    }
                                                }
                                            })
                                            .off('error.form.bv')
                                            .on('error.form.bv', function (e) {
                                                var field = $(bv.$invalidFields[0]);
                                                field = field.next("input").length > 0 ? field.next("input") : field;
                                                field.focus();
                                            });
                                        var fields = bv.options.fields;
                                        if (btn.text().contains("暂存"))
                                            bv.options.fields = {};
                                        bv.validate();
                                        bv.options.fields = fields;
                                    }
                                })
                            }
                        }

                        var formCallback = frame.contentWindow.window["formCallback"];
                        var flag = true;

                        if (formCallback) {
                            flag = formCallback.call(me, index, lay, btnIndex, post);
                        }
                        if (flag) {
                            post();
                        }
                    }
                    return false;
                },
                cancel: config.cancel
            })
            return false;
        },
        windowLink: function (config) {
            var me = $(this);
            me.trigger("windowLink.beforeLink", config);
            $.formLinkCore.call(this, {
                url: config.url,
                callback: function (index, lay, btnIndex) {
                    var frame = lay.find("iframe")[0];
                    var windowCallback = frame.contentWindow.window["windowCallback"];
                    var data = null;
                    var afterWindowCallback = function (data2) {
                        if (config.callback) {
                            if (typeof (config.callback) == "string") {
                                if (config.callback.startWith("refresh")) {
                                    me.loadUrlOrRefresh();
                                }
                                else {
                                    var func = window[config.callback];
                                    if (func) {
                                        if (func.call(me, data2 || data) === false)
                                            return false;
                                    }
                                }
                            }
                            else {
                                config.callback.call(me, data2 || data);
                            }
                        }
                        $.getRootLayer().close(index);
                    }
                    if (windowCallback)
                        data = windowCallback(index, lay, btnIndex, afterWindowCallback);
                    if (data !== false) {
                        afterWindowCallback();
                    }
                    return false;
                }
            });
        },
        ajaxLink: function (config) {
            var me = this;
            var url = config.url;
            var callback = config.callback;
            var contentType = config.contentType;
            var dataHandler = config.dataHandler;
            var linkCallback = config.linkCallback;
            if (url) {
                var data = null;
                var datahandlerFunc = window[dataHandler];
                if (datahandlerFunc) {
                    data = datahandlerFunc.call(me);
                }
                if (data !== false) {
                    var post = function () {
                        $.loadingPost(url, data, function (result) {
                            if (result.success) {
                                var after = function () {
                                    if (linkCallback) {
                                        linkCallback.call(me, result);
                                    }
                                    else if (callback) {
                                        if (typeof (callback) == "string") {
                                            if (callback.startWith("refresh")) {
                                                me.loadUrlOrRefresh();
                                            }
                                            else {
                                                var func = window[callback];
                                                if (func)
                                                    func.call(me, result);
                                            }
                                        }
                                        else {
                                            callback.call(me, result);
                                        }
                                    }
                                    else if (result.url) {
                                        $.loadUrlOrRefresh(result.url);
                                    }
                                }
                                if (result.data && typeof result.data == "string") {
                                    $.showSuccess(result.data, function () {
                                        after();
                                    });
                                }
                                else {
                                    after();
                                }
                            }
                            else {
                                if (result.errorText) {
                                    $.showError(result.errorText);
                                }
                            }
                            return true;
                        }, contentType)
                    }
                    if (config.confirm) {
                        $.showConfirm(config.confirm, function (result) {
                            if (result) {
                                post();
                            }
                        })
                    }
                    else {
                        post();
                    }
                }
            }
        },
        noTitleLink: function () {
            var me = this;
            $.noTitleLink.call(this)
        }
    })

    if ($.fn.bootstrapValidator) {
        $.fn.bootstrapValidator.validators.custom = {
            validate: function (validator, $field, options) {
                var callback = $field.data("bv-custom-callback");
                var message = message;
                if (callback && window[callback]) {
                    message = window[callback].call(this, validator, $field);
                }
                if (message) {
                    return {
                        valid: false,
                        message: message
                    }
                }
                return true;
            }
        };
        $.fn.bootstrapValidator.validators.attach = {
            validate: function (validator, $field, options) {
                var id = $field.attr("id");
                var value = $field.val();
                if (!$.isNullOrEmpty(value)) {
                    var convertings = $("#" + id).parents(".bt-attachs-uploader-group").find(".converting");
                    if (convertings.length > 0) {
                        return {
                            valid: false,
                            message: "存在转换中的文件,请稍等"
                        }
                    }
                    var converterrors = $("#" + id).parents(".bt-attachs-uploader-group").find(".converterror");
                    if (converterrors.length > 0) {
                        return {
                            valid: false,
                            message: "存在转换失败的文件,请删除"
                        }
                    }
                }
                return true;
            }
        };
        $.fn.bootstrapValidator.validators.notEmpty = {
            enableByHtml5: function ($field) {
                var required = $field.attr('required') + '';
                return ('required' === required || 'true' === required);
            },

            /**
             * Check if input value is empty or not
             *
             * @param {BootstrapValidator} validator The validator plugin instance
             * @param {jQuery} $field Field element
             * @param {Object} options
             * @returns {Boolean}
             */
            validate: function (validator, $field, options) {
                var text = validator.$submitButton ? validator.$submitButton.text() : "";
                if (text && text.contains("暂存")) {
                    return true;
                }
                if (!$field.parents(".form-group").hasClass("hidden") && !$field.parents(".form-group").hasClass("hide")
                    && !$field.parents(".bt-ct").hasClass("hide") && !$field.parents(".bt-ct").hasClass("hidden")
                    && !$field.parents(".form-control-left").hasClass("hide") && !$field.parents(".form-control-left").hasClass("hidden")) {
                    var type = $field.attr('type');
                    if ('radio' === type || 'checkbox' === type) {
                        return validator
                            .getFieldElements($field.attr('data-bv-field'))
                            .filter(':checked')
                            .length > 0;
                    }
                    if ('number' === type && $field.get(0).validity && $field.get(0).validity.badInput === true) {
                        return true;
                    }
                    return $.trim($field.val()) !== '';
                }
                return true;
            }
        };
    }

    $(document).on("mousewheel DOMMouseScroll", ".layui-layer-phimg img", function (e) {
        var delta = (e.originalEvent.wheelDelta && (e.originalEvent.wheelDelta > 0 ? 1 : -1)) || // chrome & ie
            (e.originalEvent.detail && (e.originalEvent.detail > 0 ? -1 : 1)); // firefox
        var imagep = $(".layui-layer-phimg").parent().parent();
        var image = $(".layui-layer-phimg").parent();
        var h = image.height();
        var w = image.width();
        if (delta > 0) {
            //if (h < (window.innerHeight)) {
            h = h * 1.05;
            w = w * 1.05;
            //}
        } else if (delta < 0) {
            if (h > 100) {
                h = h * 0.95;
                w = w * 0.95;
            }
        }
        imagep.css("top", (window.innerHeight - h) / 2);
        imagep.css("left", (window.innerWidth - w) / 2);
        image.height(h);
        image.width(w);
        imagep.height(h);
        imagep.width(w);
    });

    $(document).on("click", ".form-link", function (e) {
        e.preventDefault();
        var me = $(this);
        me.blur();
        if (me.attr("disabled") != "disabled") {
            var url = me.attr("href");
            var beforecheck = me.data("beforecheck");
            var beforecheckFunc = window[beforecheck];
            var showForm = true;
            if (beforecheckFunc) {
                showForm = beforecheckFunc.call(me);
            }
            if (showForm) {
                var callback = me.data("callback");
                me.formLink({
                    url: url,
                    callback: callback
                })
            }
        }
        if ($(this).parents(".dropdown-menu").length > 0) {
            $(this).parents(".dropdown").removeClass("open");
        }
        return false;
    });

    $(document).on("click", ".window-link", function (e) {
        e.preventDefault();
        var me = $(this);
        me.blur();
        if (me.attr("disabled") != "disabled") {
            var url = me.attr("href");
            var datahandler = me.data("datahandler");
            var datahandlerFunc = window[datahandler];
            var showWindow = false;
            var data;
            if (datahandlerFunc) {
                data = datahandlerFunc.call(me);
                showWindow = data !== false;
            } else {
                showWindow = true;
            }
            if (showWindow) {
                if (data) {
                    var value = JSON.stringify(data)
                    me.data("value", value);
                    url = $.updateUrlParam(url, "value", encodeURI(value));
                }
                var callback = me.data("callback");
                me.windowLink({
                    url: url,
                    callback: callback
                })
            }
        }
        if ($(this).parents(".dropdown-menu").length > 0) {
            $(this).parents(".dropdown").removeClass("open");
        }
        return true;
    })

    $(document).on("click", ".notitle-link", function (e) {
        e.preventDefault();
        var me = $(this);
        if (me.attr("disabled") != "disabled") {
            me.noTitleLink();
        }
        if ($(this).parents(".dropdown-menu").length > 0) {
            $(this).parents(".dropdown").removeClass("open");
        }
        return false;
    });

    $(document).on("click", ".ajax-link", function (e) {
        e.preventDefault();
        var me = $(this);
        if (me.attr("disabled") != "disabled") {
            var url = me.attr("href");
            var confirm = me.data("confirm");
            var callback = me.data("callback");
            var contentType = me.data("contenttype");
            var datahandler = me.data("datahandler");
            var linkcallback = me.data("linkcallback");
            if (me.hasClass("ajax-link-delete")) {
                confirm = confirm || "您确定要删除当前行吗？";
            }
            me.ajaxLink({
                url: url,
                callback: callback,
                confirm: confirm,
                contentType: contentType,
                dataHandler: datahandler,
                linkCallback: linkcallback
            })
        }
        if ($(this).parents(".dropdown-menu").length > 0) {
            $(this).parents(".dropdown").removeClass("open");
        }
        return false;
    })

    $(document).on("click", ".common-link", function (e) {
        e.preventDefault();
        var me = $(this);
        if (me.attr("disabled") != "disabled" && e.result !== false) {
            var url = $(this).attr("href");
            if (url) {
                var callback = me.data("callback");
                if (url.startWith("formlink:")) {
                    me.formLink({
                        url: url.substring(9),
                        callback: callback
                    })
                }
                else if (url.startWith("windowlink:")) {
                    me.windowLink({
                        url: url.substring(11),
                        callback: callback
                    })
                }
                else {
                    if (me.attr("target") == "_blank")
                        $.openUrl(url, me.attr("target"));
                    else
                        $.loadUrlOrRefresh(url);
                }
            }
        }

        if ($(this).parents(".dropdown-menu").length > 0) {
            $(this).parents(".dropdown").removeClass("open");
        }

        return false;
    });

    $(".dropdown-menu").find(".dropdown-column-item").each(function () {
        var me = $(this);
        var $checkbox = me.find("input[type=checkbox]");
        var callback = me.data("callback");
        me.on("click", function (e) {
            if ($(e.target).is("input")) {
                return;
            }
            else {
                $checkbox.trigger("click");
                return false;
            }
        })
        me.on("change", "input[type=checkbox]", function (e) {
            var tableCt = me.parents(".bt-table-ct");
            if (tableCt.length > 0) {
                var tables = tableCt.find(".bt-table");
                for (var i = 0; i < tables.length; i++) {
                    var table = tables[i];
                    if (table.id) {
                        var clName = $checkbox.val();
                        if ($checkbox.prop("checked")) {
                            document.defaultView.$("#" + table.id).bootstrapTable('showColumn', clName);
                        }
                        else {
                            document.defaultView.$("#" + table.id).bootstrapTable('hideColumn', clName);
                        }

                        if (callback) {
                            var func = window[callback];
                            if (func)
                                func();
                        }
                    }
                }
            }
            return false;
        });
    });


    $(document).on("click", ".res-study-link-sys", function (e) {
        e.preventDefault();
        var me = $(this);
        var idValue = me.data("id");
        var type = me.data("type");

        var defaultAction = "DetailSys";
        var action = me.data("action");
        if ($.isNullOrEmpty(action)) {
            action = defaultAction;
        }
        $.loadUrl("/TrainResStudy/" + action + "/" + type + "?id=" + idValue);
    });

    $(document).on("click", ".res-study-link", function (e) {
        e.preventDefault();
        var me = $(this);
        var idValue = me.data("id");
        var sidValue = me.data("sid");
        var stypeValue = me.data("stype");

        if (sidValue == undefined)
            sidValue = 0;
        if (stypeValue == undefined)
            stypeValue = 'C';

        var type = me.data("type");
        var defaultAction = "Detail";
        var action = me.data("action");
        if ($.isNullOrEmpty(action))
            action = defaultAction;

        $.loadUrl("/TrainResStudy/" + action + "/" + type + "?id=" + idValue + "&sid=" + sidValue + "&stype=" + stypeValue);
    });

    $(document).on("click", ".res-study-link-sys", function (e) {
        e.preventDefault();
        var me = $(this);
        var idValue = me.data("id");
        var type = me.data("type");

        var defaultAction = "DetailSys";
        var action = me.data("action");
        if ($.isNullOrEmpty(action)) {
            action = defaultAction;
        }
        $.loadUrl("/TrainResStudy/" + action + "/" + type + "?id=" + idValue);
    });

    $(document).on("click", ".file-preview-link", function (e) {
        e.preventDefault();
        var me = $(this);
        var host = '';
        if (me.data("host") != undefined) {
            host = me.data("host");
        }
        var code = me.data("code");
        var type = me.data("type");
        var ext = me.data("ext");
        if (ext) {
            $(this).windowLink({
                url: host + "/FilePreview/Window?v=1&code=" + code
            });
        }
        else {
            var hidden = me.closest(".bt-attachs-uploader-group").find(".bt-attachs-uploader");
            if (hidden.length > 0) {
                var emptyAttExtLinkUrl = hidden.data("empty-attext-linkurl")
            }
            else {
                var emptyAttExtLinkUrl = me.data("empty-attext-linkurl")
            }
            if (emptyAttExtLinkUrl) {
                emptyAttExtLinkUrl = $.template(emptyAttExtLinkUrl, {
                    code: code
                });
                $(this).windowLink({
                    url: emptyAttExtLinkUrl
                });
            } else {
                $(this).windowLink({
                    url: host + "/FilePreview/Window?v=1&code=" + code
                });
            }
        }
    });

    $(document).on("mousemove", function (e) {
        //if (window.parent != window) {
        //    mouseX = e.screenX - window.parent.sX;
        //    mouseY = e.screenY - window.parent.sY;
        //}
        //else {
        //    mouseX = e.pageX;
        //    mouseY = e.pageY;
        //    sX = e.screenX + $(window).scrollLeft() - e.pageX;
        //    sY = e.screenY + $(window).scrollTop() - e.pageY;
        //}
    });

    $.fn.extend({
        bttag: function () {
            var Tag = function (tag) {
                var me = this;
                this.tag = tag;
                this.tagCt = tag.find("div");
                this.hidden = tag.find("input[type=hidden]");
                tag.on("click", "label i", function () {
                    var label = $(this).parent();
                    var val = label.attr("value");
                    if (me.onRemoveEvent)
                        me.onRemoveEvent(val);
                    label.remove();
                    me._refreshValue();
                })
                var onRemove = tag.data("onremove");
                this.onRemoveEvent = window[onRemove]
                this.displayMode = tag.data("displaymode");
                this.displaySplit = tag.data("displaysplit");
            }
            $.extend(Tag.prototype, {
                addTag: function (value, name) {
                    var val = this.hidden.val() || "";
                    var vals = val ? val.split(";") : [];
                    var valuePair = name == value ? name : (value + "|" + name);
                    if (!vals.contains(valuePair)) {
                        vals.push(valuePair);

                        var label = $("<label value='" + value + "' name='" + name + "'>" + this._getDisplay(value, name) + "<i class='glyphicon glyphicon-remove'></i></label>")
                        this.tagCt.append(label);
                    }
                    this._refreshValue();
                },
                addTags: function (values, names) {
                    var val = this.hidden.val() || "";
                    var vals = val ? val.split(";") : [];
                    for (var i = 0; i < values.length; i++) {
                        var value = values[i];
                        var name = names[i];
                        var valuePair = name == value ? name : (value + "|" + name);
                        if (!vals.contains(valuePair)) {
                            vals.push(valuePair);
                            var label = $("<label value='" + value + "' name='" + name + "'>" + this._getDisplay(value, name) + "<i class='glyphicon glyphicon-remove'></i></label>")
                            this.tagCt.append(label);
                        }
                    }
                    this._refreshValue();
                },
                removeTag: function (value) {
                    var me = this;
                    this.tag.find("label").each(function () {
                        var val = $(this).attr("value");
                        if (value == val) {
                            $(this).remove();
                            me._refreshValue();
                        }
                    })
                },
                removeTags: function (values) {
                    var me = this;
                    this.tag.find("label").each(function () {
                        var val = $(this).attr("value");
                        if (values.contains(val)) {
                            $(this).remove();
                        }
                    })
                    me._refreshValue();
                },
                getValue: function () {
                    var val = this.hidden.val() || "";
                    return val;
                },
                getArrayValue: function () {
                    var val = this.hidden.val() || "";
                    var vals = val.split(";");
                    var returnVals = [];
                    for (var i = 0; i < vals.length; i++) {
                        if (vals[i]) {
                            var value = vals[i].indexOf("|") > 0 ? vals[i].substring(0, vals[i].indexOf("|")) : vals[i];
                            returnVals.push(value);
                        }
                    }
                    return returnVals;
                },
                getNameValue: function () {
                    var val = this.hidden.val() || "";
                    var vals = val.split(";");
                    var returnVals = [];
                    var returnNames = [];
                    for (var i = 0; i < vals.length; i++) {
                        if (vals[i]) {
                            var value = vals[i].indexOf("|") > 0 ? vals[i].substring(0, vals[i].indexOf("|")) : vals[i];
                            var name = vals[i].indexOf("|") > 0 ? vals[i].substring(vals[i].indexOf("|") + 1) : vals[i];
                            returnVals.push(value);
                            returnNames.push(name);
                        }
                    }
                    return {
                        name: returnNames.join(","),
                        value: returnVals.join(",")
                    }
                },
                getItemLength: function () {
                    var length = this.hidden.data("length");
                    return parseInt(length);
                },
                _refreshValue: function () {
                    var contents = [];
                    this.tag.find("label").each(function () {
                        var value = $(this).attr("value");
                        var name = $(this).attr("name");
                        contents.push(name == value ? name : (value + "|" + name));
                    });
                    this.hidden.val(contents.join(";"));
                    this.hidden.data("length", contents.length);
                },
                _getDisplay: function (value, name) {
                    if (this.displayMode == 0) {
                        return name;
                    }
                    return value + "-" + name;
                }
            })
            $(this).data("tag", new Tag($(this)));
        }
    })

    $(".bt-tag").bttag();

    var btTab = function (tab) {
        var me = this;
        this.tab = tab;
        this.id = tab.attr("id");
        this.tabUl = this.tab.find(">.nav-tabs");
        this.closable = this.tabUl.hasClass("nav-closable");
        this.tabCnt = this.tabUl.find(">.nav-item").length;
        this.tabContent = this.tab.find(">.tab-content")
        this.closeCallback = this.tabUl.data("close-callback");
        this.tabChangedCallback = this.tabUl.data("tabchanged-callback");
        this.autoRefreshTable = this.tabUl.data("auto-refresh-table");
        this.tabUl.on("shown.bs.tab", "a[data-toggle='tab']", function (e) {
            var pane = $($(e.target).attr("href"));
            if (me.tabChangedCallback && window[me.tabChangedCallback]) {
                window[me.tabChangedCallback]($(e.target).parent().attr("id"), pane, e);
            }
            else if (me.autoRefreshTable) {
                pane.find(".bt-table").bootstrapTable("refresh");
            }
        })

        this.tabUl.on("click", ">.nav-item>.fa-close", function (e) {
            var that = $(this);
            var id = $(this).prev().attr("href");
            var func = function () {
                that.parent().remove();
                me.tabContent.find(id).remove();
                me.tabUl.find(".nav-item>a:last").tab("show");
            }
            if (me.closeCallback && window[me.closeCallback]) {
                window[me.closeCallback].call(me, that.parent(), func);
            }
            else {
                func();
            }
        })
    }

    $.extend(btTab.prototype, {
        addTab: function (name, content, id) {
            var key = this.id + "_" + (this.tabCnt + 1);
            var closeContent = "";
            if (this.closable) {
                closeContent = "<i class='fa fa-close'/>"
            }
            this.tabUl.append("<li class=\"nav-item\" id=" + id + "><a data-toggle='tab' href='#" + key + "'>" + name + "</a>" + closeContent + "</li>");
            this.tabContent.append("<div class='tab-pane' id='" + key + "'>" + content + "</div>");
            this.tabCnt = this.tabCnt + 1;
            this.tabUl.find(".nav-item>a:last").tab("show");
        },
        showBadge: function (id, cnt) {
            var li = this.tabUl.find("#" + id);
            var badge = li.find(".badge");
            if (badge.length == 0) {
                li.append("<i class='badge'></i>")
                badge = li.find(".badge")
            }
            if (cnt > 0) {
                badge.removeClass("hidden")
            }
            else {
                badge.addClass("hidden")
            }
            badge.text(cnt);
        }
    })

    var btDropdown = function (dropdown) {
        var me = this;
        this.dropdown = $(dropdown);
        this.name = this.dropdown.data("name");
        this.value = this.dropdown.data("value");
        this.data = this.dropdown.data("data");
        this.keyField = "ID";
        this.displayField = "Name";
        if (typeof this.data == 'string') {
            this.data = $.parseJsonString(this.data);
        }
        this.textSplit = this.dropdown.data("text-split") || ",";
        this.multi = this.dropdown.data("multi");
        if (this.multi)
            this.dropdown.addClass("bt-dropdown-multi")
        this.selection = $("<div class='bt-dropdown-selection'><i class='fa fa-angle-down'/>")
        this.holder = $("<div class='bt-dropdown-placeholder'>")
        this.holder.text("--请选择--");
        this.selectedText = $("<div class='bt-dropdown-selected-text'>")
        this.selectedValue = $("<input type='hidden' class='bt-dropdown-selected-value'>")
        this.selectedValue.attr("name", this.name);
        this.setValue(this.value);
        this.selection.append(this.selectedText);
        this.selection.append(this.selectedValue);
        this.down = $("<div class='bt-dropdown-down' data-toggle='buttons'>")
        this.down.css("height", "0px");
        this.down.css("display", "none");
        this.needDown = true;
        this.checkedChangedFunc = this.dropdown.data("checked-changed-func");
        if (btDropdownCurrent && btDropdownCurrent.data("dropdown")) {
            btDropdownCurrent.data("dropdown").lostFocus();
        }
        btDropdownCurrent = this.dropdown;
        this.selection.click(function (e) {
            me.down.stop();
            if (me.needDown) {
                me.show();
            }
            else {
                me.hide();
            }
        })
        this.selection.append(this.holder);
        this.dropdown.append(this.selection);
        this.dropdown.append(this.down);
        this.down.on("change", ".checkbox-input", function () {
            var value = "";
            if (me.multi) {
                me.down.find("input:checked").each(function () {
                    value = (value ? (value + ',') : '') + $(this).val();
                })
            }
            else {
                value = $(this).val();
                me.hide();
            }
            me.setValue(value);
            me.dropdown.trigger("change");
            if (me.checkedChangedFunc && window[me.checkedChangedFunc]) {
                window[me.checkedChangedFunc](value);
            }
        })
    }

    $.extend(btDropdown.prototype, {
        show: function () {
            var me = this;
            this.down.show();
            this.dropdown.addClass("bt-dropdown-show");
            var data = this.data;
            this.down.html("");
            btDropdownCurrent = this.dropdown;
            if (data) {
                var values = this.value.split(",");
                var template = "<label class='btn {{d.active }}'><input class='checkbox-input' type='checkbox' {{d.checked}} value='{{d.ID}}'/><span>{{d.Name}}</span></label>"
                for (var i = 0; i < data.length; i++) {
                    var item = data[i];
                    if (this.keyField != "ID") item.ID = item[this.keyField];
                    if (this.displayField != "Name") item.Name = item[this.displayField];
                    var contains = values.contains(item.ID);
                    item.checked = contains ? "checked" : "";
                    item.active = contains ? "active" : "";
                    this.down.append($.template(template, item))
                }
                this.down.css("min-width", this.dropdown.width());
                var height = Math.min(data.length, 8) * 31 + 12;
                this.down.animate({ height: height }, 300, function () {
                    me.needDown = false;
                });
            }
        },
        hide: function () {
            var me = this;
            if (!this.needDown) {
                this.down.animate({ height: "0px" }, 30, function () {
                    me.down.hide();
                    me.dropdown.removeClass("bt-dropdown-show");
                });
                this.needDown = true;
            }
        },
        setValue: function (value) {
            value = value || "";
            value = value.toString();
            var text = "";
            if (this.data) text = this.data.joinDisplayWith(value, this.keyField, this.displayField, this.textSplit);
            this.selectedValue.val(value);
            this.selectedText.html(text);
            if (value) {
                this.holder.hide();
                this.selectedText.show();
            }
            else {
                this.holder.show();
                this.selectedText.hide();
            }
            this.value = value;
        },
        getValue: function () {
            return this.selectedValue.val();
        },
        getText: function () {
            return this.selectedText.html();
        },
        lostFocus: function () {
            this.hide();
            this.dropdown.trigger("blur");
        },
        setData: function (data, value, keyField, displayField) {
            this.keyField = keyField || this.keyField;
            this.displayField = displayField || this.displayField;
            this.data = data;
            this.setValue(value);
        }
    })

    $(document).on("click", function (e) {
        if (btDropdownCurrent
            && e.target
            && $(e.target).parent().length > 0
            && ($(e.target).parents(".bt-dropdown").length == 0
                || $(e.target).parents(".bt-dropdown")[0] != btDropdownCurrent[0])
            && btDropdownCurrent.data("dropdown")) {
            btDropdownCurrent.data("dropdown").lostFocus();
            btDropdownCurrent = null;
        }
    })

    var btRadioButtonGroup = function (radiobuttongroup) {

        var me = this;
        this.group = $(radiobuttongroup);
        this.currentChangedCallback = this.group.data("currentchangedcallback");
        var jsonData = this.group.data("data");
        this.url = this.group.data("url");
        this.level = parseInt(this.group.data("level"));
        this.showLevelSpan = $.parseBool(this.group.data("showlevelspan"));
        this.levelLength = parseInt(this.group.data("levellength"));
        this.radioName = this.group.data("name");
        this.showEmpty = $.parseBool(this.group.data("showempty"));
        this.emptyText = this.group.data("emptytext");
        this.value = this.group.data("value");
        this.baseLength = parseInt(this.group.data("baselength"));

        if (!$.isNullOrEmpty(jsonData)) {
            var data = $.parseJsonString(jsonData);
            //document.createDocumentFragment();
            me.draw(data);
            me.loadEvent();
        }
        if (!$.isNullOrEmpty(this.url)) {
            me.loadUrl(this.url);
        }
    }

    $.extend(btRadioButtonGroup.prototype, {
        draw: function (data) {
            var me = this;
            me.group.html("");
            var recordNo = "";
            var recordPNo = "";
            var recordLevel = "";

            for (var currentLevel = 1; currentLevel <= me.level; currentLevel++) {
                var levelGroup = document.createElement("div");
                levelGroup.className = "radio-button-group-level";
                levelGroup.setAttribute('data-level', currentLevel);
                if (currentLevel != 1) {
                    levelGroup.className = "radio-button-group-level hidden";
                }

                if (me.showLevelSpan == true) {

                    var levelGroupLabel = document.createElement("div");
                    levelGroupLabel.className = "radio-button-group-level-label";
                    var labelLevelTag = document.createElement("label");
                    labelLevelTag.className = "btn-level";

                    var levelspan = document.createElement("span");
                    levelspan.innerHTML = $.getChineseNumber(currentLevel) + "级";
                    labelLevelTag.appendChild(levelspan);
                    levelGroupLabel.appendChild(labelLevelTag);
                    levelGroup.appendChild(levelGroupLabel);
                }


                var levelGroupContent = document.createElement("div");
                if (me.showLevelSpan == true) {
                    levelGroupContent.className = "radio-button-group-level-content";
                } else {
                    levelGroupContent.className = "radio-button-group-level-content-nocaption";
                }
                levelGroupContent.setAttribute('data-toggle', 'buttons');

                var tempList = data.where(function (item) {
                    if (item.PID.length == currentLevel * me.levelLength) {
                        return true;
                    }
                    return false;
                });
                if (tempList.length > 0) {
                    var name2 = me.radioName;
                    var strValue = me.value;
                    var tempEmptyText = me.emptyText == null ? '不限' : me.emptyText;
                    if (me.showEmpty && currentLevel == 1) {
                        var item = { ID: "", Name: tempEmptyText, Extra: "", PID: "" };
                        tempList.insert(0, item);
                    }
                    for (var i = 0; i < tempList.length; i++) {
                        var item = tempList[i];

                        var labelTag = document.createElement("label");
                        labelTag.className = "btn";
                        labelTag.setAttribute('data-pno', item.PID);
                        labelTag.setAttribute('data-no', item.ID);
                        labelTag.setAttribute('data-level', currentLevel);

                        if (currentLevel != 1) {
                            labelTag.className = "btn hidden";
                        }

                        var inputTag = document.createElement("input");
                        inputTag.setAttribute('type', 'radio');
                        inputTag.setAttribute('name', name2);
                        inputTag.setAttribute('value', item.Extra);
                        inputTag.setAttribute('title', item.Name);
                        if (strValue == item.Extra) {

                            inputTag.setAttribute('checked', 'true');
                            labelTag.className = "btn active";
                            recordNo = item.ID;
                            recordLevel = currentLevel;
                            recordPNo = item.PID;
                        }
                        var span = document.createElement("span");
                        span.innerHTML = item.Name;
                        span.setAttribute('title', item.Name);
                        labelTag.appendChild(inputTag);
                        labelTag.appendChild(span);

                        levelGroupContent.appendChild(labelTag);
                    }
                }
                levelGroup.appendChild(levelGroupContent);

                var levelGroupClear = document.createElement("div");
                levelGroupClear.className = "clear";
                levelGroup.appendChild(levelGroupClear);
                var btnGroup = this.group[0];
                btnGroup.appendChild(levelGroup);
            }
            if (!$.isNullOrEmpty(recordNo)
                && !$.isNullOrEmpty(recordPNo)
                && !$.isNullOrEmpty(recordLevel)
            ) {
                var recordShowLevels = new Array();

                this.group.find(".btn").each(function () {
                    var pno = $(this).data("pno");
                    var no = $(this).data("no");
                    var tempLevel = parseInt($(this).data("level"));

                    if (tempLevel < recordLevel) {
                        if (!recordShowLevels.contains(tempLevel)) {
                            recordShowLevels.push(tempLevel);
                        }

                        if (!$.isNullOrEmpty(no)
                            && (recordPNo.substring(0, me.baseLength) == no.substring(0, me.baseLength))) {
                            $(this).removeClass("hidden");
                            if (recordPNo.startWith(no)) {
                                $(this).addClass("active");
                            }
                        }
                    }
                    if (tempLevel == recordLevel) {
                        if (pno == recordPNo) {
                            $(this).removeClass("hidden");
                        }
                        if (!recordShowLevels.contains(tempLevel)) {
                            recordShowLevels.push(tempLevel);
                        }
                    }

                    if (tempLevel > recordLevel) {
                        $(this).removeClass("active");
                        if (pno == recordNo) {
                            $(this).removeClass("hidden");
                            if (!recordShowLevels.contains(tempLevel)) {
                                recordShowLevels.push(tempLevel);
                            }
                        }
                    }
                })

                this.group.find(".radio-button-group-level").each(function () {
                    var tempLevel = parseInt($(this).data("level"));
                    if (recordShowLevels.contains(tempLevel)) {
                        $(this).removeClass("hidden");
                    } else {
                        $(this).addClass("hidden");
                    }
                })
            }
        },
        loadEvent: function () {
            var group = this.group;
            group.find(".btn").click(function () {
                var me = $(this);
                group.find("input").removeAttr("checked");
                me.find("input").attr("checked", "checked");
                me.find("input").prop("checked", "checked");

                if (group.currentChangedCallback) {
                    var func = window[group.currentChangedCallback];
                    if (func) {
                        func.call(group, $(this).find("input").val());
                    }
                }
                //group.find(".btn").removeClass("active");
                me.addClass("active");
                var no = me.data("no");
                var level = parseInt(me.data("level"));
                var showLevels = new Array();
                group.find(".btn").each(function () {
                    var pno = $(this).data("pno");
                    var tempLevel = parseInt($(this).data("level"));
                    if (tempLevel > level) {
                        $(this).removeClass("active");
                        if (pno == no) {
                            $(this).removeClass("hidden");
                            if (!showLevels.contains(tempLevel)) {
                                showLevels.push(tempLevel);
                            }

                        } else {
                            $(this).addClass("hidden");
                        }
                    }
                })

                group.find(".radio-button-group-level").each(function () {
                    var tempLevel = parseInt($(this).data("level"));
                    if (tempLevel > level) {
                        if (showLevels.contains(tempLevel)) {
                            $(this).removeClass("hidden");
                        } else {
                            $(this).addClass("hidden");
                        }
                    }
                })
                var form = group.parents(".bt-form");
                if (form && form.parent().hasClass("bt-condition-form-ct")) {
                    form.find("button[type=submit]:last").click();
                }
            });

        },
        loadUrl: function (url) {
            var me = this;
            $.get2(url, "", function (result) {
                if (result.success && result.data) {
                    var data = result.data;
                    if (data && data.length > 0) {
                        me.draw(data);
                        me.loadEvent();
                    } else {
                        me.group.html("");
                    }
                }
            })
        }
    })

    var btchooseblock = function (chooseblock) {
        var me = this;
        this.chooseblock = $(chooseblock);
        me.draw("");
    }

    $.extend(btchooseblock.prototype, {
        draw: function (url2) {
            var me = $(this);
            var hidden = this.chooseblock.find(".bt-hidden");
            var chooser = this.chooseblock.find(".bt-choose-block-button");
            var blocklist = this.chooseblock.find(".bt-choose-block-list");

            var url = "";
            if ($.isNullOrEmpty(url2)) {
                url = chooser.data("url");
            } else {
                url = url2;
            }
            var callback = chooser.data("callback");
            var idfield = chooser.data("idfield");
            var namefield = chooser.data("namefield");
            var template = chooser.data("template");

            var onItemAdd = chooser.data("onitemadd");
            var onItemDeleted = chooser.data("onitemdeleted");

            var readonly = me.attr('readonly');
            if (readonly != "readonly") {
                chooser.off("click");
                chooser.click(function () {
                    url = $.updateUrlParam(url, "type", "100");
                    $.formLinkCore.call(chooser, {
                        url: url,
                        callback: function (index, lay) {
                            var frame = lay.find("iframe")[0];
                            var windowCallback = frame.contentWindow.window["windowCallback"];
                            var data = null;
                            if (windowCallback)
                                data = windowCallback();
                            if (data) {
                                if (idfield && namefield) {
                                    var oldVal = hidden.val();
                                    var val = oldVal ? JSON.parse(oldVal.replace(/\'/g, "\"")) : [];
                                    for (var i = 0; i < data.length; i++) {
                                        var idValue = data[i][idfield];
                                        var nameValue = data[i][namefield];
                                        var isHas = val.containsWithKey({ ID: idValue }, "ID");
                                        if (!isHas) {
                                            var item = { ID: idValue, Name: nameValue };
                                            val.push(item);
                                            var str = $.template(template, item);
                                            blocklist.append(str);
                                        }
                                    }
                                    hidden.val(JSON.stringify(val));
                                    try {
                                        form.bootstrapValidator('revalidateField', hidden);
                                    } catch (e) {

                                    }
                                }
                                if (callback) {
                                    var func = window[callback];
                                    if (func)
                                        func(data);
                                }
                                return true;
                            }
                            return false;
                        }
                    });
                    return false;
                })
            }

            blocklist.on("click", ".bt-choose-block-item-delete", function (e) {
                e.preventDefault();
                var item = $(this).parents(".bt-choose-block-item");
                var index = item.parent().children(".bt-choose-block-item").index(item);
                var hidden = blocklist.parent().find(".bt-hidden");
                var oldVal = hidden.val();
                var vals = oldVal ? JSON.parse(oldVal.replaceAll(/\'/g, "\"")) : [];
                var data = vals[index];
                vals.splice(index, 1);
                if (vals.length > 0) {
                    hidden.val(JSON.stringify(vals));
                } else {
                    hidden.val("");
                }
                item.remove();
                if (onItemDeleted) {
                    var func = window[onItemDeleted];
                    if (func) {
                        func(data);
                    }
                }
                return false;
            })

        },
        loadUrl: function (url) {
            var me = this;
            me.draw(url);
        }
    })

    var btattachsuploader = function (attachsuploader) {
        if (!attachsuploader.data("attachsuploader")) {
            var me = this;
            attachsuploader.data("attachsuploader", this)
            this.attachsuploader = $(attachsuploader);
            this.btnLibrary = this.attachsuploader.parents(".bt-attachs-uploader-group").find(".attach-library");
            this.template = '<div class="uploader-file-item" data-code="{{d.AttCode}}" data-status="{{d.Status}}"><a class="file-preview-link" data-code="{{d.AttCode}}" data-ext="{{d.AttExt}}">{{d.AttName+(d.AttExt||"")}}</a><a class="uploader-file-delete"><i class="glyphicon glyphicon-remove"></i></a></div>';
            this.max = this.attachsuploader.data("max");
            this.uploadQueue = this.attachsuploader.parent().find(".uploader-queue");
            this.uploadHidden = this.uploadQueue.find(".uploader-hidden");
            this.onValueChanged = this.attachsuploader.data("onvaluechanged");
            this.attachLibraryExtra = this.attachsuploader.data("attachlibraryextra");
            me.draw();
        }
    }

    $.extend(btattachsuploader.prototype, {
        draw: function () {
            var me = this;
            var form = me.attachsuploader.parents(".bt-form");
            var id = me.attachsuploader.attr("id");
            var buttontext = me.attachsuploader.data("buttontext");
            var typedesc = me.attachsuploader.data("typedesc");
            var typeexts = me.attachsuploader.data("typeexts");
            var allowext = me.attachsuploader.data("allowext");
            var sizelimit = me.attachsuploader.data("sizelimit");
            var multi = me.attachsuploader.data("multi");
            var width = me.attachsuploader.data("width");
            var uploadmode = me.attachsuploader.data("uploadmode");
            var template2 = '<div class="uploader-file-item converting" data-code="{{d.AttCode}}" data-status="{{d.Status}}"><a data-code="{{d.AttCode}}" data-ext="{{d.AttExt}}">{{d.AttName+(d.AttExt||"")}}</a><span class="state" data-status="{{d.Status}}">{{d.StatusName}}</span><i class="fa-li fa fa-spinner fa-spin"></i></div>';
            var valuemode = me.attachsuploader.data("valuemode");
            var onFileUploaded = me.attachsuploader.data("onfileuploaded");
            var onFileDeleted = me.attachsuploader.data("onfiledeleted");
            var onfileerror = me.attachsuploader.data("fileerror");
            var afterFileUploaded = me.attachsuploader.data("afterfileuploaded");
            var onfilebatchselected = me.attachsuploader.data("filebatchselected");
            var uploadPercent = me.attachsuploader.parent().find(".uploader-percent");
            var url = me.attachsuploader.data("url");
            var uncompress = me.attachsuploader.data("uncompress");
            var companyCode = me.attachsuploader.data("companycode");
            var libraryLinkExtendParam = me.attachsuploader.data("librarylink-extendparam");
            if (uncompress) {
                url += (url.contains("?") ? "&" : "?") + "uncompress=true";
            }
            //me.removeClass("bt-attachs-uploader");
            if (me.uploadHidden.val()) {
                if (uploadmode == "append" && valuemode == "json") {
                    var hiddenVal = me.uploadHidden.val();
                    var hiddenVals = JSON.parse(hiddenVal.replace(/\'/g, "\""));
                    hiddenVals.forEach(function (item) {
                        me.uploadQueue.append($.template(me.template, item))
                    });
                }
            }

            if (me.attachsuploader.uploadify) {
                me.attachsuploader.uploadify({
                    height: 34,
                    checkExisting: false,
                    swf: '/Plugins/uploadify/uploadify.swf',
                    uploader: url,
                    width: width,
                    buttonText: buttontext,
                    buttonImg: null,
                    fileSizeLimit: sizelimit,
                    fileTypeDesc: typedesc,
                    fileTypeExts: typeexts,
                    multi: multi,
                    uploadLimit: 999,
                    wmode: "transparent",
                    removeCompleted: false,
                    formData: { CompanyCode: companyCode },
                    onSWFReady: function () {
                        //me.uploadify("disable", uploadQueue.children().length >= max + 1);
                        me.checkAttachCanUpload();
                    },
                    onSelect: function (file) {
                    },
                    onSelectError: function (file, errorCode, errorMsg) {
                        if (errorCode == -100) {
                            $.showError("上传数量达到最大限制");
                        }
                        else if (errorCode == -110) {
                            $.showError("选择的文件超过了最大限制" + sizelimit);
                        }
                        else if (errorCode == -120) {
                            $.showError("不能上传空文件");
                        }
                        else {
                            alert(errorCode);
                        }
                    },
                    onUploadProgress: function (file, fileBytesLoaded, fileTotalBytes,
                        queueBytesLoaded, swfuploadifyQueueUploadSize) {
                        var rate = Math.round(fileBytesLoaded * 100 / fileTotalBytes);
                        if (uploadmode == "update") {
                            var queueItem = $("#queue_" + file.id);
                            if (queueItem.length > 0) {
                                queueItem.find(".uploader-state").text(rate + "%");
                            }
                        }
                        uploadPercent.text(rate + "%");
                    },
                    onUploadSuccess: function (file, data, response) {
                        var left = me.max - me.uploadQueueChildrenCnt();
                        data = JSON.parse(data);
                        if (data.success && left >= 0) {
                            data = data.data;
                            var hidden = me.uploadQueue.find(".uploader-hidden");
                            if (uploadmode == "append") {
                                var oldVal = hidden.val();
                                var val2 = oldVal ? JSON.parse(oldVal.replace(/\'/g, "\"")) : [];
                                val2.push({ AttCode: data.AttCode, AttName: data.AttName, AttExt: data.AttExt });
                                hidden.val(JSON.stringify(val2)).trigger("change");

                                var str2 = "";
                                if (data.Status == 10) {
                                    str2 = $.template(me.template, data);
                                } else {
                                    str2 = $.template(template2, data);
                                }
                                if (!$.isNullOrEmpty(str2)) {
                                    me.uploadQueue.append(str2);
                                }
                            }
                            me.checkAttachCanUpload();
                            if (onFileUploaded) {
                                var func = window[onFileUploaded];
                                if (func) {
                                    func(data);
                                }
                            }
                            if (me.onValueChanged) {
                                var func2 = window[me.onValueChanged];
                                if (func2) {
                                    func2(id, hidden.val());
                                }
                            }
                            me.queueData.files[file.id].data = data;
                            try {

                                form.bootstrapValidator('revalidateField', me.uploadHidden.attr("name"));
                            } catch (e) {

                            }
                            checkData();
                        }
                    },
                    overrideEvents: ["onUploadError"],
                    onUploadComplete: function (file) {
                        me.queueData.files[file.id].uploaded = true;
                    },
                    onUploadError: function (file, errorCode, errorMsg, errorString) {
                        uploadPercent.text("上传失败：" + errorMsg)
                    },
                    onError: function (event, queueId, fileObj, errorObj) {
                    },
                    onComplete: function () {
                    }
                });
            }
            else {
                me.attachsuploader.removeAttr("name");
                var fileId = 1;
                var extensions = typeexts.split(";");
                var newExtensions = [];
                if (extensions && extensions.length > 0) {
                    for (var i = 0; i < extensions.length; i++) {
                        var ext = extensions[i];
                        if (ext && ext.indexOf(".") >= 0) {
                            newExtensions.push(ext.substring(ext.indexOf(".") + 1));
                        }
                    }
                }
                me.attachsuploader.fileinput({
                    browseLabel: buttontext,
                    language: 'zh',
                    uploadUrl: url,
                    showUpload: false,
                    showCaption: false,
                    showPreview: false,
                    //showUploadedThumbs:false,
                    showRemove: false,
                    showCancel: false,
                    dropZoneEnabled: false,
                    allowedFileExtensions: newExtensions
                }).on("filebatchselected", function (event, files) {
                    if (onfilebatchselected) {
                        var func = window[onfilebatchselected];
                        if (func) {
                            func();
                        }
                    }
                    $(this).fileinput("upload");
                }).on("fileerror", function (event, data, msg) {
                    $.showWarning(msg);
                    if (onfileerror) {
                        var func = window[onfileerror];
                        if (func) {
                            func();
                        }
                    }
                }).on("fileunlock", function (event, data, msg) {
                    me.checkAttachCanUpload();
                }).on("filepreupload", function (event, data, previewId, index) {
                    var form = data.form, files = data.files, extra = data.extra,
                        response = data.response, reader = data.reader;
                    console.log('File pre upload triggered');

                }).on("fileuploaded", function (event, data, previewId, index) {
                    var left = me.max - me.uploadQueueChildrenCnt();
                    if (data.response.success && left >= 0) {
                        var fileData = data.response.data;
                        var hidden = me.uploadQueue.find(".uploader-hidden");
                        if (uploadmode == "append") {
                            var oldVal = hidden.val();
                            var val2 = oldVal ? JSON.parse(oldVal.replace(/\'/g, "\"")) : [];
                            val2.push({ AttCode: fileData.AttCode, AttName: fileData.AttName, AttExt: fileData.AttExt })
                            hidden.val(JSON.stringify(val2)).trigger("change");
                            var str2 = "";
                            if (fileData.Status == 10) {
                                str2 = $.template(me.template, fileData);
                            } else {
                                str2 = $.template(template2, fileData);
                            }
                            if (!$.isNullOrEmpty(str2)) {
                                me.uploadQueue.append(str2);
                            }
                        }
                        if (onFileUploaded) {
                            var func = window[onFileUploaded];
                            if (func) {
                                func(fileData);
                            }
                        }
                        if (me.onValueChanged) {
                            var func2 = window[me.onValueChanged];
                            if (func2) {
                                func2(hidden.val());
                            }
                        }
                        try {
                            form.bootstrapValidator('resetField', me.uploadHidden.attr("name"));
                        } catch (e) {

                        }
                        checkData();
                        if (afterFileUploaded) {
                            var func = window[afterFileUploaded];
                            if (func) {
                                func(fileData);
                            }
                        }
                    }
                });
                me.attachsuploader.parents(".btn-file").removeClass("btn-primary").addClass("btn-default");
            }

            me.checkAttachCanUpload();
            me.btnLibrary.off('click').on("click", function (e) {
                e.preventDefault();
                if (me.btnLibrary.attr("disabled") != "disabled" && e.result !== false) {
                    var url = "/CommonAttach/Window?type=100&multi=" + (me.max + 1 - me.uploadQueueChildrenCnt() > 1 ? true : false) + "&allowext=" + allowext;
                    if (me.attachLibraryExtra) {
                        url = $.updateUrlParam(url, "extra", me.attachLibraryExtra);
                    }
                    if (!$.isNullOrEmpty(libraryLinkExtendParam)) {
                        var objArray = libraryLinkExtendParam.split(",");
                        if (objArray && objArray.length > 0) {
                            for (var i = 0; i < objArray.length; i++) {
                                var objItem = objArray[i];
                                var propArray = objItem.split("|");
                                if (propArray && propArray.length == 2) {
                                    var key = propArray[0];
                                    var value = propArray[1];
                                    url = $.updateUrlParam(url, key, value);
                                }
                            }
                        }
                    }
                    $(this).windowLink({
                        url: url,
                        callback: function (results) {
                            results.forEach(function (item1) {
                                if (me.uploadQueueChildrenCnt() < me.max + 1) {
                                    var hidden = me.uploadQueue.find(".uploader-hidden");
                                    var oldVal = hidden.val();
                                    var val2 = oldVal ? JSON.parse(oldVal.replace(/\'/g, "\"")) : [];

                                    var flag = val2.containsWithFunc(function (item2) {
                                        return item1.AttCode == item2.AttCode
                                    });
                                    if (!flag) {
                                        var item = {
                                            AttCode: item1.AttCode,
                                            AttName: item1.AttName,
                                            AttExt: item1.AttExt
                                        };
                                        val2.push(item);
                                        hidden.val(JSON.stringify(val2)).trigger("change");
                                        var str2 = $.template(me.template, item1);
                                        me.uploadQueue.append(str2);
                                        me.checkAttachCanUpload();
                                        if (me.onValueChanged) {
                                            var func2 = window[me.onValueChanged];
                                            if (func2) {
                                                func2(hidden.val());
                                            }
                                        }
                                        try {

                                            form.bootstrapValidator('revalidateField', hidden.attr("name"));
                                        } catch (e) {

                                        }
                                    }
                                }
                            })
                        }
                    })
                }
            })
            me.uploadQueue.on("click", ".uploader-file-delete", function (e) {
                e.preventDefault();
                var item = $(this).parents(".uploader-file-item");
                var status = item.data("status");
                function removeItem() {
                    var index = item.parent().children(".uploader-file-item").index(item);
                    var hidden = me.uploadQueue.find(".uploader-hidden");
                    var oldVal = hidden.val();
                    var vals = oldVal ? JSON.parse(oldVal.replace(/\'/g, "\"")) : [];
                    vals.splice(index, 1);
                    var val = JSON.stringify(vals);
                    hidden.val(val == "[]" ? "" : val).trigger("change");
                    item.remove();
                    if (onFileDeleted) {
                        var func = window[onFileDeleted];
                        if (func) {
                            func();
                        }
                    }
                    if (me.onValueChanged) {
                        var func2 = window[me.onValueChanged];
                        if (func2) {
                            func2(hidden.val());
                        }
                    }

                    try {

                        form.bootstrapValidator('revalidateField', me.uploadHidden.attr("name"));
                    } catch (e) {

                    }
                    me.checkAttachCanUpload();
                }

                if (status == "-1") {
                    var code = item.data("code");
                    $.ajax({
                        type: 'post',
                        dataType: "json",
                        url: "/CommonAttach/RemoveAttachByCode",
                        data: { code: code },
                        async: false,
                        success: function (result) {
                            if (result.success) {
                                removeItem();

                            }
                        }
                    });
                } else {
                    removeItem();
                }
                return false;
            })

        },

        uploadQueueChildrenCnt: function () {
            return this.uploadQueue.children().length;
        },

        checkAttachCanUpload: function () {
            var isNotCan = this.uploadQueueChildrenCnt() >= this.max + 1;
            if (isNotCan) {
                this.btnLibrary.toggleDisabled(true);
                if (this.attachsuploader.uploadify) {
                    this.attachsuploader.uploadify("disable", true);
                } else {
                    this.attachsuploader.fileinput("disable");
                }
            } else {
                this.btnLibrary.toggleDisabled(false);
                if (this.attachsuploader.uploadify) {
                    this.attachsuploader.uploadify("disable", false);
                } else {
                    this.attachsuploader.fileinput("enable");
                }
            }
        },
        appendData: function (data) {
            var me = this;
            if (!$.isNullOrEmpty(data)) {
                var results = data;
                if (typeof results == 'string') {
                    results = $.parseJsonString(data);
                }
                results.forEach(function (item1) {
                    if (me.uploadQueueChildrenCnt() < me.max + 1) {
                        var hidden = me.uploadQueue.find(".uploader-hidden");
                        var oldVal = hidden.val();
                        var val2 = oldVal ? JSON.parse(oldVal.replace(/\'/g, "\"")) : [];

                        var flag = val2.containsWithFunc(function (item2) {
                            return item1.AttCode == item2.AttCode
                        });
                        if (!flag) {
                            var item = {
                                AttCode: item1.AttCode,
                                AttName: item1.AttName,
                                AttExt: item1.AttExt
                            };
                            val2.push(item);
                            hidden.val(JSON.stringify(val2)).trigger("change");
                            var str2 = $.template(me.template, item1);
                            me.uploadQueue.append(str2);
                            me.checkAttachCanUpload();
                            if (me.onValueChanged) {
                                var func2 = window[me.onValueChanged];
                                if (func2) {
                                    func2(hidden.val());
                                }
                            }
                        }
                    }
                })
            }
        },
        resetData: function (data) {
            var me = this;
            me.uploadQueue.find(".uploader-file-item").remove();
            me.uploadHidden.val("");
            if (!$.isNullOrEmpty(data)) {
                me.appendData(data);
            } else {
                me.uploadHidden.trigger("change");
            }
        },
        removeData: function () {
            var me = this;
            me.uploadQueue.find(".uploader-file-item").remove();
            var hidden = me.uploadQueue.find(".uploader-hidden");
            hidden.val("").trigger("change");
        }
    })


    var btsignuploader = function (signuploader) {
        if (!signuploader.data("signuploader")) {
            var me = this;
            signuploader.data("signuploader", this);
            this.signuploader = $(signuploader);

            this.unique = this.signuploader.data("unique");
            this.time = this.signuploader.data("time");
            this.template = this.signuploader.data("template");
            this.codetype = this.signuploader.data("codetype");
            this.title = this.signuploader.data("title");
            this.onValueChanged = this.signuploader.data("onvaluechanged");

            this.btSign = this.signuploader.parents(".bt-sign");
            this.signQrCode = this.btSign.find(".sign-qrcode");
            this.signExpire = this.btSign.find(".sign-cover-expire");

            this.uploadQueue = this.signuploader.parent().find(".uploader-queue");
            this.uploadHidden = this.uploadQueue.find(".uploader-hidden");

            this.current_uuid = "";
            me.draw();
        }
    }

    var signQRCodeHub;
    $.extend(btsignuploader.prototype, {
        draw: function () {
            var me = this;
            var form = me.signuploader.parents(".bt-form");
            if (me.uploadHidden.val()) {
                var hiddenVal = me.uploadHidden.val();
                if (me.codetype == "Sign") {
                    var hiddenVals = JSON.parse(hiddenVal.replace(/\'/g, "\""));
                    hiddenVals.forEach(function (item) {
                        me.uploadQueue.append($.template(me.template, item));
                    });
                } else if (me.codetype == "SignMulti") {
                    var hiddenVals = hiddenVal.split(";");
                    hiddenVals.forEach(function (item) {
                        me.uploadQueue.append($.template(me.template, { Url: item }));
                    });
                }
            }
            me.signQrCode.on('click', function (e) {
                e.preventDefault();
                var array = [];
                array.push($(this).attr("src"));
                $.showPrev(array.join(";"), 0);
            })
            me.uploadQueue.on("click", ".uploader-image-prev", function (e) {
                e.preventDefault();
                var array = [];
                var idx = 0;
                var item = $(this).parents(".uploader-image-item");
                me.uploadQueue.find(".uploader-image-item").each(function (index) {
                    if (item[0] == this) {
                        idx = index;
                    }
                    array.push($(this).find(".uploader-image").attr("src"));
                })
                $.showPrev(array.join(";"), idx);
            })
            me.uploadQueue.on("click", ".uploader-image-delete", function (e) {
                e.preventDefault();
                var item = $(this).parents(".uploader-image-item");
                var index = item.parent().children(".uploader-image-item").index(item);
                var oldVal = me.uploadHidden.val();
                if (me.codetype == "Sign") {
                    var vals = oldVal ? JSON.parse(oldVal.replace(/\'/g, "\"")) : [];
                    vals.splice(index, 1);
                    var val = JSON.stringify(vals);
                    me.uploadHidden.val(val == "[]" ? "" : val).trigger("change");
                } else if (me.codetype == "SignMulti") {
                    var vals = oldVal ? oldVal.split(";") : [];
                    vals.splice(index, 1);
                    var val = vals.join(";");
                    me.uploadHidden.val(val).trigger("change");
                }
                item.remove();
                if (me.onValueChanged) {
                    var func = window[me.onValueChanged];
                    if (func) {
                        func(me.uploadHidden.val());
                    }
                }
                try {
                    form.bootstrapValidator('revalidateField', me.uploadHidden.attr("name"));
                } catch (e) {

                }
            })
            signQRCodeHub = $.connection.qRCodeHub;
            signQRCodeHub.client.sendSuccessCallback = function (unique, uuid, data) {
                $("input[name=" + unique + "]").parents(".bt-sign").find(".bt-sign-uploader").data("signuploader").appendData(unique, uuid, data);
            };

            signQRCodeHub.client.sendUUIDCallback = function (unique, uuid, code) {
                $("input[name=" + unique + "]").parents(".bt-sign").find(".bt-sign-uploader").data("signuploader").setSignQrcode(unique, uuid, code);
            };
            $.connection.hub.start().done(function () {
                signQRCodeHub.server.setSignQRCode(parseInt(me.time), me.unique, me.codetype, me.title);
            }).fail(function (error) { console.log(error); });

            me.btSign.on("click", ".sign-cover-expire", function () {
                $(this).hide();
                signQRCodeHub.server.refreshSignQRCode(parseInt(me.time), me.unique, me.codetype, me.title);
                showcode();
            })
            var showcode = function () {
                setTimeout(function () {
                    me.signExpire.show();
                }, parseInt(me.time) * 1000);
            }
            showcode();
        },
        appendData: function (unique, uuid, data) {
            var me = this;
            if (me.unique == unique && me.current_uuid == uuid) {
                var results;
                if (!$.isNullOrEmpty(data)) {
                    if (me.codetype == "Sign") {
                        var item = $.parseJsonString(data);
                        var oldVal = me.uploadHidden.val();
                        var val2 = oldVal ? JSON.parse(oldVal.replace(/\'/g, "\"")) : [];

                        var exists = val2.containsWithFunc(function (item2) {
                            return item.UserName == item2.UserName
                        });
                        if (exists) {
                            val2.removeWithKey("UserName", item.UserName);
                            me.uploadQueue.find(".uploader-image-item").each(function (index) {
                                var itemImage = $(this);
                                var uploaderImage = $(itemImage.find(".uploader-image")[0]);
                                var userName = uploaderImage.data("username");
                                if (item.UserName == userName) {
                                    itemImage.remove();
                                }
                            })
                        }
                        val2.push(item);
                        var valResult = JSON.stringify(val2);
                        //console.log(unique + ":" + valResult);
                        me.uploadHidden.val(valResult).trigger("change");
                        var str2 = $.template(me.template, item);
                        me.uploadQueue.append(str2);
                    } else if (me.codetype == "SignMulti") {
                        var oldVal = me.uploadHidden.val();
                        var oldVals = oldVal ? oldVal.split(";") : [];
                        var newVals = data.split(";");
                        newVals.forEach(function (item) {
                            oldVals.push(item);
                            me.uploadQueue.append($.template(me.template, { Url: item }));
                        });
                        var valResult = oldVals.join(";");
                        me.uploadHidden.val(valResult).trigger("change");
                    }
                    if (me.onValueChanged) {
                        var func2 = window[me.onValueChanged];
                        if (func2) {
                            func2(me.uploadHidden.val());
                        }
                    }
                }
            }
        },
        setSignQrcode: function (unique, uuid, code) {
            var me = this;
            if (me.unique == unique && me.current_uuid != uuid) {
                me.current_uuid = uuid;
                me.signQrCode.attr("src", "/Public/GetQRCode?uuid=" + code).removeClass("hidden");
            }
        }
    })

    $.fn.extend({
        bttab: function () {
            var tab = new btTab($(this));
            $(this).data("tab", tab);
        },
        btdropdown: function () {
            var dropdown = new btDropdown(this);
            $(this).data("dropdown", dropdown);
            return dropdown;
        },
        btradiobuttongroup: function () {
            var radiobuttongroup = new btRadioButtonGroup(this);
            $(this).data("radiobuttongroup", radiobuttongroup);
            return radiobuttongroup;
        },
        btchooseblock: function () {
            var chooseblock = new btchooseblock(this);
            $(this).data("chooseblock", chooseblock);
            return chooseblock;
        },
        btattachsuploader: function () {
            var attachsuploader = new btattachsuploader(this);
            return attachsuploader;
        },
        btsignuploader: function () {
            var signuploader = new btsignuploader(this);
            return signuploader;
        }
    });

    $(".bt-nav-tabs-ct").each(function () {
        $(this).bttab();
    })

    $(".bt-dropdown").each(function () {
        $(this).btdropdown();
    })

    $(".radio-button-group").each(function () {
        $(this).btradiobuttongroup();
    })

    $(".bt-choose-block").each(function () {
        $(this).btchooseblock();
    })

    $(".bt-attachs-uploader").each(function () {
        $(this).btattachsuploader();
    })

    $(".bt-sign-uploader").each(function () {
        $(this).btsignuploader();
    })

    var btSignPreviewFunc = function () {
        var me = $(this);

        var uploadHidden = me.find(".uploader-hidden");
        var template = uploadHidden.data("template");
        var codetype = uploadHidden.data("codetype");

        var uploadQueue = me.find(".uploader-queue");
        if (uploadHidden.val()) {
            var hiddenVal = uploadHidden.val();
            if (codetype == "Sign") {
                var hiddenVals = JSON.parse(hiddenVal.replace(/\'/g, "\""));
                hiddenVals.forEach(function (item) {
                    uploadQueue.append($.template(template, item));
                });
            } else if (codetype == "SignMulti") {
                var hiddenVals = hiddenVal.split(";");
                hiddenVals.forEach(function (item) {
                    uploadQueue.append($.template(template, { Url: item }));
                });
            }
        }
        uploadQueue.on("click", ".uploader-image-prev", function (e) {
            e.preventDefault();
            var array = [];
            var idx = 0;
            var item = $(this).parents(".uploader-image-item");
            uploadQueue.find(".uploader-image-item").each(function (index) {
                if (item[0] == this) {
                    idx = index;
                }
                array.push($(this).find(".uploader-image").attr("src"));
            })
            $.showPrev(array.join(";"), idx);
        })
    }
    window.btSignPreviewFunc = btSignPreviewFunc;
    $(".bt-sign-preview").each(btSignPreviewFunc);

    var btImagePreviewFunc = function () {
        var me = $(this);
        if (me.children().length == 0) {
            var width = me.data("img-width");
            var height = me.data("img-height");
            var value = me.data("value");
            var template = "<div class='uploader-image-item' style='width: {{d.width}}px; height: {{d.height}}px; '>" +
                "<div class='uploader-image-hoder' style='line-height: 100px'>" +
                "   <a class='uploader-image-prev' href='#'>预览</a>" +
                "</div>" +
                "<img class='uploader-image' src='{{d.value}}'>" +
                "</div>"

            if (value) {
                value.split(";").forEach(function (item) {
                    var previewItem = $.template(template, { width: width, height: height, value: item });
                    me.append(previewItem)
                })
            }
            me.find(".uploader-image-prev").click(function (e) {
                e.preventDefault();
                var array = [];
                var idx = 0;
                var item = $(this).parents(".uploader-image-item")
                me.find(".uploader-image-item").each(function (index) {
                    if (item[0] == this) {
                        idx = index;
                    }
                    array.push($(this).find(".uploader-image").attr("src"));
                })
                $.showPrev(array.join(";"), idx);
            })
        }
    }
    window.btImagePreviewFunc = btImagePreviewFunc;

    $(".bt-img-preview").each(btImagePreviewFunc);

    $(document).on("click", ".img-preview", function (e) {
        var me = $(e.target);
        var value = me.data("value") || me.attr("src");
        $.showPrev(value);
        e.preventDefault();
    });

    $(document).on("click", ".uploader-file-link", function (e) {
        var me = $(e.target);
        var url = me.data("value") || me.attr("href");
        var name = me.data("name");
        $.openDownload(url, name);
        e.preventDefault();
    });

    $.fn.extend({
        btform: function () {
            var form = $(this);
            if ($(this).bootstrapValidator) {
                $(this).bootstrapValidator({
                    message: '无效',
                    live: "submitted",
                    group: ".control-ct",
                    excluded: [],
                    submitButtons: 'button[type="submit"]'
                }).on("keydown", function (e) {
                    if (e.keyCode == 13 && e.target && e.target.type != "textarea") {
                        var submit = form.find("button[type=submit]");
                        if (submit.length == 1)
                            return false;
                    }
                }).on('success.form.bv', function (e) {
                    var me = $(this);
                    var layerIndex = $.showLoading();
                    e.preventDefault();
                    var validatedCallback = form.data("validated-callback");
                    if (validatedCallback && window[validatedCallback]) {
                        var flag = window[validatedCallback].call(form);
                        if (flag) {
                            if (callbackFunc) {
                                var func = window[callbackFunc];
                                if (func) {
                                    func(result);
                                }
                            }
                            $.closeLoading(layerIndex);
                        }
                    }
                    else {

                        form.find(".bt-error-summary").css("display", "none");
                        var action = form.attr('action');
                        var params = form.serializeParams();
                        var target;
                        var bv = form.data('bootstrapValidator');
                        bv.disableSubmitButtons(true);
                        if (bv.$submitButton && bv.$submitButton.length > 0) {
                            target = bv.$submitButton[0];
                            var url = bv.$submitButton.data("url");
                            if (url)
                                action = url;
                        }
                        var checkResult = true;
                        var post = function () {
                            var onjoinparamfunc = form.data("joinparamfunc");
                            if (onjoinparamfunc) {
                                window[onjoinparamfunc](params, target);
                            }
                            var returnUrl = form.data("returnurl");
                            var callbackFunc = form.data("callbackfunc");
                            $.post2(action, params, function (result) {
                                bv.disableSubmitButtons(false);
                                function after(result) {
                                    if (callbackFunc) {
                                        var func = window[callbackFunc];
                                        if (func) {
                                            func(result);
                                            return;
                                        }
                                    }
                                    $.loadUrlOrRefresh(returnUrl || result.url);
                                }
                                if (result.success) {
                                    if (result.data && typeof result.data == "string") {
                                        $.showSuccess(result.data, function () {
                                            after(result);
                                        });
                                    }
                                    else {
                                        after(result);
                                    }
                                }
                                else if (result.errorText) {
                                    if (result.errorType == "5") {
                                        //打开自定义流程界面
                                        var array = result.errorText.split("|");
                                        var url = "/CommonAudit/CustomSubmit";
                                        url = $.updateUrlParam(url, "id", array[0]);
                                        url = $.updateUrlParam(url, "opController", array[1]);
                                        url = $.updateUrlParam(url, "operate", array[2]);
                                        url = $.updateUrlParam(url, "defineID", array[3]);
                                        var index = $(this).formLink({
                                            url: url,
                                            callback: function () {
                                                after();
                                                $.getRootLayer().close(index)
                                                return true;
                                            },
                                            cancel: function () {
                                                after();
                                                $.getRootLayer().close(index)
                                                return true;
                                            }
                                        })
                                    }
                                    else {
                                        var errorSummary = form.find(".bt-error-summary");
                                        if (errorSummary.length > 0) {
                                            errorSummary.find(".bt-error-text").text(result.errorText)
                                            errorSummary.css("display", "block");
                                            if ($("#safecode").length > 0) {
                                                $("#safecode").attr('src', "/Public/VerifyCode?code=" + Math.random());
                                            }
                                            if (!$.isNullOrEmpty(result.url)) {
                                                $.loadUrl(result.url);
                                            }
                                        }
                                        else {
                                            $.showError(result.errorText, function () {
                                                $.loadUrl(result.url);
                                            });
                                        }
                                    }
                                }
                                $.closeLoading(layerIndex);
                            }, 'json').error(function () {
                                bv.disableSubmitButtons(false);
                            });
                        }
                        var beforecheckfunc = form.data("beforecheckfunc");
                        if (beforecheckfunc) {
                            checkResult = window[beforecheckfunc](params, target, post);
                        }
                        if (checkResult == true) {
                            post();
                        } else {
                            bv.disableSubmitButtons(false);
                        }
                    }
                })
            }

            form.find(".bt-input").each(function () {
                var me = $(this);
                var thinkUrl = $(this).data("think-url");
                if (thinkUrl) {
                    var thinkNameField = $(this).data("think-name-field");
                    me.after("<ul class='dropdown-menu'></ul>")
                    var ul = me.next();
                    var thinkData = [];
                    var isCompositionend = true;
                    var inputFunc = function () {
                        if (isCompositionend) {
                            console.log("query");
                            var value = $(this).val();
                            if (value && value.length >= 2) {
                                $.get2(thinkUrl, { keywords: value }, function (result) {
                                    var data = result.data.List;
                                    thinkData = data;
                                    ul.html("");
                                    if (data.length > 0) {
                                        data.forEach(function (item, index) {
                                            ul.append("<li><a href='#' data-index='" + index + "'>" + item[thinkNameField] + "</a></li>")
                                        })
                                        ul.show();
                                    }
                                    else {
                                        ul.hide();
                                    }
                                })
                            }
                            else {
                                ul.html("");
                                ul.hide();
                            }
                        }
                    }
                    me.on("compositionstart", function () {
                        isCompositionend = false;
                        console.log("false");
                    })
                    me.on("compositionend", function () {
                        isCompositionend = true;
                        console.log("true");
                        if ($.isChrome()) {
                            inputFunc.call(me);
                        }
                    })
                    me.bind("input", function (e) {
                        inputFunc.call(this);
                    })

                    me.on("keydown", function (e) {
                        if (ul.is(":visible")) {
                            if (e.keyCode == 40) {
                                e.preventDefault();
                                var items = ul.find("li");
                                var activeIndex = -1;
                                items.each(function (item) {
                                    if ($(this).hasClass("active")) {
                                        activeIndex = item;
                                    }
                                })
                                if (activeIndex + 1 < items.length) {
                                    items.removeClass("active");
                                    $(items[activeIndex + 1]).addClass("active");
                                    me.val($(items[activeIndex + 1]).text());
                                    var item = thinkData[activeIndex + 1];
                                    me.trigger("btinput.thinkchoose", item);
                                }
                            }
                            else if (e.keyCode == 38) {
                                e.preventDefault();
                                var items = ul.find("li");
                                var activeIndex = -1;
                                items.each(function (item) {
                                    if ($(this).hasClass("active")) {
                                        activeIndex = item;
                                    }
                                })
                                if (activeIndex - 1 >= 0) {
                                    items.removeClass("active");
                                    $(items[activeIndex - 1]).addClass("active");
                                    me.val($(items[activeIndex - 1]).text());
                                    var item = thinkData[activeIndex - 1];
                                    me.trigger("btinput.thinkchoose", item);
                                }
                            } else if (e.keyCode == 13) {
                                ul.hide();
                            }
                        }
                    })
                    ul.on("click", "a", function (e) {
                        e.preventDefault();
                        var index = $(this).data("index");
                        var name = $(this).text();
                        var item = thinkData[index];
                        me.val(name);
                        ul.hide();
                        me.trigger("btinput.thinkchoose", item);
                    })
                }
                me.on("keypress", function (e) {
                    if (!$.validateInput(e.key)) {
                        e.preventDefault();
                        return false;
                    }
                })
            })

            form.find(".bt-textarea").each(function () {
                var me = $(this);
                me.on("keypress", function (e) {
                    if (!$.validateInput(e.key)) {
                        e.preventDefault();
                        return false;
                    }
                })
            })

            form.find("textarea.auto-height").click(function () {
                $(this).autoHeight();
            });


            form.find(".control-label .fa-question-circle-o").click(function () {
                var callback = $(this).data("callback");
                var func = window[callback];
                if (func) {
                    func.call(this);
                }
            });

            form.find(".bt-form-ct .btn-close").click(function () {
                if (window.parent != null)
                    window.parent.$.closeCurrentFormLink(0);
            })

            var dates = form.find(".bt-date");
            if (dates.length > 0) {
                $.loadAssets({
                    scripts: ["/Plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js",
                        "/Plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"],
                    css: ["/Plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css"]
                }, function () {
                    dates.each(function () {
                        var me = $(this);
                        var showclear = me.data("showclear") == "True";
                        var startdate = me.data("startdate");
                        var enddate = me.data("enddate");
                        var startview = me.data("startview");
                        var minview = me.data("minview");
                        var dateformat = me.data("format");
                        var changedCallback = me.data("changedcallback");
                        if (me.datetimepicker) {
                            if (!startview) {
                                startview = 2;
                            }
                            if (!minview) {
                                minview = 2;
                            }
                            if (!dateformat) {
                                dateformat = "yyyy-mm-dd";
                            }
                            me.datetimepicker({
                                format: dateformat,
                                language: "zh-CN",
                                minView: minview,
                                startView: startview,
                                autoclose: true,
                                clearBtn: showclear,
                                todayHighlight: true,
                                zIndex: 198910160,

                            }).on("changeDate", function (ev) {  //值改变事件
                                try {
                                    var form = me.parents(".bt-form");
                                    try {
                                        form.bootstrapValidator('revalidateField', $(this).find("input"));
                                    } catch (e) {

                                    }
                                    var value = $(this).find("input").val();
                                    if (changedCallback) {
                                        var func = window[changedCallback];
                                        if (func) {
                                            func.call(me, value);
                                        }
                                    } else {
                                        if (me.parent().parent().hasClass("start-date")) {
                                            form.find(".end-date").each(function () {
                                                var endMe = $(this);
                                                endMe.find(".bt-date").each(function () {
                                                    var input = $(this).find("input");
                                                    var endTime = input.val();
                                                    if (endTime < value) {
                                                        input.val(value);
                                                    }
                                                    $(this).datetimepicker('setStartDate', value.toDate());
                                                })
                                            })
                                        }
                                    }

                                    if (form && form.parent().hasClass("bt-condition-form-ct")) {
                                        form.find("button[type=submit]:last").click();
                                    }

                                } catch (e) {
                                }
                            });
                            if (startdate) {
                                me.datetimepicker('setStartDate', startdate.toDate());
                            }
                            if (enddate) {
                                me.datetimepicker('setEndDate', enddate.toDate());
                            }
                        }
                    })
                })
            }


            var datetimes = form.find(".bt-datetime");
            if (datetimes.length > 0) {
                $.loadAssets({
                    scripts: ["/Plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js",
                        "/Plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"],
                    css: ["/Plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css"]
                }, function () {
                    datetimes.each(function () {
                        var me = $(this);
                        var showclear = me.data("showclear") == "True";
                        var startdate = me.data("startdate");
                        var enddate = me.data("enddate");
                        var showsecond = me.data("showsecond") == "True";
                        var format = "yyyy-mm-dd hh:ii";
                        if (showsecond) {
                            format = "yyyy-mm-dd hh:ii:ss";
                        }
                        var changedCallback = me.data("changedcallback");
                        me.datetimepicker({
                            format: format,
                            language: "zh-CN",
                            autoclose: true,
                            clearBtn: showclear,
                            todayHighlight: true,
                            zIndex: 198910160
                        }).on("changeDate", function (ev) {  //值改变事件
                            try {
                                var form = me.parents(".bt-form");
                                try {
                                    form.bootstrapValidator('revalidateField', $(this).find("input"));
                                } catch (e) {

                                }
                                var value = $(this).find("input").val();
                                if (me.parent().parent().hasClass("start-datetime")) {
                                    form.find(".end-datetime").each(function () {
                                        var endMe = $(this);
                                        endMe.find(".bt-datetime").each(function () {
                                            var input = $(this).find("input");
                                            var endTime = input.val();
                                            if (endTime < value) {
                                                input.val(value);
                                            }
                                            $(this).datetimepicker('setStartDate', value);
                                        })
                                    })
                                }

                                if (changedCallback) {
                                    var func = window[changedCallback];
                                    if (func) {
                                        func.call(me, value);
                                    }
                                }
                                if (form && form.parent().hasClass("bt-condition-form-ct")) {
                                    form.find("button[type=submit]:last").click();
                                }

                            } catch (e) {

                            }
                        });
                        if (startdate) {
                            me.datetimepicker('setStartDate', startdate.toDate());
                        }
                        if (enddate) {
                            me.datetimepicker('setEndDate', enddate.toDate());
                        }
                    })
                })
            }

            var times = form.find(".bt-time");
            if (times.length > 0) {
                $.loadAssets({
                    scripts: ["/Plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js",
                        "/Plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"],
                    css: ["/Plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css"]
                }, function () {
                    times.each(function () {
                        var me = $(this);
                        var showclear = me.data("showclear") == "True";
                        var starttime = me.data("starttime");
                        var endtime = me.data("endtime");
                        var changedCallback = me.data("changedcallback");
                        me.datetimepicker({
                            format: "hh:ii",
                            language: "zh-CN",
                            startView: "hour",
                            maxView: "hour",
                            autoclose: true,
                            clearBtn: showclear,
                            todayHighlight: true,
                            zIndex: 198910160
                        }).on("changeDate", function (ev) {  //值改变事件
                            try {
                                var form = me.parents(".bt-form");
                                try {
                                    form.bootstrapValidator('revalidateField', $(this).find("input"));
                                } catch (e) {

                                }
                                var value = $(this).find("input").val();
                                if (me.parent().parent().hasClass("start-time")) {
                                    form.find(".end-time").each(function () {
                                        var endMe = $(this);
                                        endMe.find(".bt-time").each(function () {
                                            var input = $(this).find("input");
                                            var endTime = input.val();
                                            if (endTime < value) {
                                                input.val(value);
                                            }
                                            $(this).datetimepicker('setStartDate', value);
                                        })
                                    })
                                }
                                if (changedCallback) {
                                    var func = window[changedCallback];
                                    if (func) {
                                        func.call(me, value);
                                    }
                                }
                                if (form && form.parent().hasClass("bt-condition-form-ct")) {
                                    form.find("button[type=submit]:last").click();
                                }

                            } catch (e) {

                            }
                        });
                        if (startdate) {
                            me.datetimepicker('setStartDate', startdate.toDate());
                        }
                        if (enddate) {
                            me.datetimepicker('setEndDate', enddate.toDate());
                        }
                    })
                })
            }

            var datetimeSelects = form.find(".bt-datetime-range");
            if (datetimeSelects.length > 0) {
                $.loadAssets({
                    scripts: ["/Plugins/bootstrap-datetimerangepicker/moment.js",
                        "/Plugins/bootstrap-datetimerangepicker/daterangepicker.js"],
                    css: ["/Plugins/bootstrap-datetimerangepicker/daterangepicker-bs3.css"]
                }, function () {
                    datetimeSelects.each(function () {
                        var me = $(this);
                        var connect = me.data("connect");
                        var format = 'YYYY-MM-DD';
                        var changedCallback = me.data("changedcallback");
                        var startDate = $(me.find("input[type=hidden]")[0]).val() || moment().day(-7).format(format);
                        var endDate = $(me.find("input[type=hidden]")[1]).val() || moment().day(-7).format(format);
                        var input = me.find("input[type=text]");
                        input.daterangepicker({
                            separator: connect,
                            singleDatePicker: false,
                            format: format
                            ,
                            startDate: startDate,
                            endDate: endDate
                        }, null).on("apply.daterangepicker", function (ev, picker) {
                            var start = picker.startDate.format(format);
                            var end = picker.endDate.format(format);
                            if (start && end) {
                                $(me.find("input[type=hidden]")[0]).val(start).trigger("change");
                                $(me.find("input[type=hidden]")[1]).val(end).trigger("change");
                                if (changedCallback) {
                                    var func = window[changedCallback];
                                    if (func) {
                                        func.call(me, start, end);
                                    }
                                }
                            }
                        });
                        me.on("click", ".icon-group-calendar", function (e) {
                            e.preventDefault();
                            input.data("daterangepicker").toggle();
                        })
                        me.on("click", ".icon-group-remove", function (e) {
                            e.preventDefault();
                            input.val("").trigger("change");
                            $(me.find("input[type=hidden]")[0]).val("").trigger("change");
                            $(me.find("input[type=hidden]")[1]).val("").trigger("change");
                        })
                    })
                })
            }

            var dateSelects = form.find(".bt-date-select");
            if (dateSelects.length > 0) {

                $.loadAssets({
                    css: ["/Assets/Css/TitleSelect/titleSelect.css"],
                    scripts: ["/Assets/JS/DateSelect/dateSelect.js"]
                }, function () {
                    dateSelects.each(function () {
                        $(this).dateSelect({
                            className: "date",
                            form: form
                        });
                    })

                })
            }

            var citys = form.find(".bt-city");
            if (citys.length > 0) {
                $.loadAssets({
                    scripts: ["/Plugins/city-picker/js/city-picker-new.js"],
                    css: ["/Plugins/city-picker/css/city-picker.css"]
                }, function () {
                    citys.each(function () {
                        var me = $(this);
                        var input = me.find("input");
                        var val = input.val();
                        var level = me.data("level");
                        var showclear = $.parseBool(me.data("showclear"));
                        var currentchangedcallback = me.data("currentchangedcallback");
                        me.citypicker({
                            placeholder: '请选择地区',
                            level: level,
                            showclear: showclear,
                            currentchangedcallback: currentchangedcallback
                        });
                        me.citypicker("setValue", val);
                    })
                })
            }

            form.find(".bt-checkbox").each(function () {
                var me = $(this);
                var hidden = me.find("input[type='hidden']");
                var inputs = me.find("input[type='checkbox']");
                var currentChangedCallback = me.data("currentchangedcallback");
                inputs.on("click", function (e) {
                    if ($(this).attr("readonly")) {
                        return false;
                    }
                })
                inputs.change(function () {
                    var value = this.checked;
                    hidden.val(value);
                    if (currentChangedCallback) {
                        var func = window[currentChangedCallback];
                        if (func) {
                            func.call(me, value);
                        }
                    }
                });
            })

            form.find(".bt-keywords").each(function () {
                var me = $(this);
                var input = me.find("input");
                me.on("click", ".bt-keyword-item", function () {
                    var array = [];
                    var text = $(this).text();
                    var val = input.val();
                    if (val) {
                        array = val.split(";");
                    }
                    if (!array.contains(text)) {
                        array.push(text);
                    }
                    input.val(array.join(";"));
                })
            })

            form.find(".bt-checkboxgroup").each(function () {
                var me = $(this);
                var btCheckBoxGroup = function (ct) {
                    var me = $(ct);
                    this.checkbox = me;
                    this.separator = me.data("separator");
                    this.hidden = me.find("input[type='hidden']");
                    this.mode = me.data("mode");
                    this.url = me.data("url");
                    this.name = this.hidden.attr("name");
                    this.mustInput = me.data("mustinput");
                    this.mustInputText = me.data("mustinputtext");
                    this.currentChangedCallback = me.data("currentchangedcallback");
                    this.value = me.data("value");
                    this.labelEditUrl = me.data("label-edit-url");
                    this.value = me.data("value");
                    this.otherKey = this.hidden.data("other-key");
                    this.init();
                    this.loadUrl(this.url);
                }
                $.extend(btCheckBoxGroup.prototype, {
                    init: function () {
                        var $that = this;
                        var me = this.checkbox;
                        me.on("click", "input[type=checkbox]", function (e) {
                            if ($(this).attr("readonly")) {
                                return false;
                            }
                        })
                        me.on("change", "input[type=checkbox]", function (e) {
                            e.preventDefault();
                            var readOnly = me.attr("readonly");
                            if (!readOnly) {
                                var readOnly = me.attr("readonly");
                                if (!readOnly) {
                                    var checkboxs = me.find("input[type=checkbox]");
                                    if ($that.mode == 0) {
                                        var currValue = $that.hidden.val();
                                        var array = [];
                                        checkboxs.each(function () {
                                            if ($(this).prop("checked")) {
                                                array.push($(this).val());
                                            }
                                        })
                                        var value = array.join($that.separator);
                                        if ($that.currentChangedCallback) {
                                            var func = window[$that.currentChangedCallback];
                                            if (func) {
                                                func.call(me, value);
                                            }
                                        }
                                        if ($that.otherKey) {
                                            var checkInput = me.find(".other-input");
                                            if (value && value.contains($that.otherKey)) {
                                                checkInput.removeClass("hidden");
                                            } else {
                                                checkInput.addClass("hidden");
                                            }
                                        }
                                    }
                                    else if ($that.mode == 1) {
                                        var value = 0n;
                                        checkboxs.each(function () {
                                            if ($(this).prop("checked")) {
                                                value |= BigInt($(this).val())
                                            }
                                        })
                                    }
                                    else if ($that.mode == 2) { //解析json值
                                        debugger;
                                        var nameSuffix = "chk_" + $that.name + ".";
                                        var valueArray = [];
                                        checkboxs.each(function () {
                                            if ($(this).prop("checked")) {
                                                var item = {};
                                                item.Key = $(this).val();
                                                var ct = $(this).siblings("input");
                                                ct.each(function () {
                                                    var inputKey = $(this).attr("name").replace(nameSuffix, "");
                                                    item[inputKey] = $(this).val();
                                                })
                                                valueArray.push(item);
                                            }
                                        })
                                        var value = JSON.stringify(valueArray);
                                    }
                                    $that.hidden.val(value).trigger("change");
                                    if (form.parent().hasClass("bt-condition-form-ct")) {
                                        form.find("button[type=submit]:last").click();
                                    }
                                    try {
                                        form.bootstrapValidator('revalidateField', $that.hidden);
                                    } catch (e) {

                                    }
                                } else {
                                    e.preventDefault();
                                }
                            }
                        })
                    },
                    setValue: function (value) {
                        var me = this.checkbox;
                        var checkboxs = me.find("input[type=checkbox]");
                        checkboxs.removeAttr("checked");
                        checkboxs.parent().removeClass("active");
                        if (value) {
                            var list = value.split(",");
                            list.forEach(function (item) {
                                me.find("input[value=" + item + "]").prop('checked', 'checked').parent().addClass("active");
                            })
                        }
                        checkboxs.trigger("change");
                    },
                    loadUrl: function (url) {
                        url = url || this.url;
                        if (url) {
                            var me = this;
                            $.get2(url, "", function (result) {
                                if (result.success && result.data) {
                                    var data = result.data.List;
                                    if (data) {
                                        me.loadData(data);
                                    }
                                }
                            })
                        }
                    },
                    loadData: function (data, selectFirst) {
                        if (data) {
                            this.clear();
                            var value = this.hidden.val();
                            var values = value ? value.split(this.separator) : [];
                            var html = "";
                            for (var i = 0; i < data.length; i++) {
                                var val = data[i];
                                var id = (this.name + "_" + val.ID);
                                var name = this.name;
                                if (i > 0) selectFirst = false;
                                var contains = values.contains(val.ID);
                                html += "<label class='checkbox-inline btn checkbox-inline-horitzontal " + (contains ? "active" : "") + " ' for='" + id + "' >" +
                                    "<input id='" + id + "' name='" + name + "' title ='" + val.Name + "' type = 'checkbox' value ='" + val.ID + "' " + (contains ? "checked" : "") + " data-bv-notempty='" + (this.mustInput || 'false') + "' data-bv-notempty-message='" + (this.mustInputText || '') + "'>" +
                                    "<span>" + val.Name + "</span></label >";
                            }
                            if (this.labelEditUrl != null) {
                                html += "<div><a href='" + this.labelEditUrl + "' class='c-blue-dark window-link' data-callback='refresh'>编辑项</a></div>";
                            }
                            this.checkbox.prepend(html);
                        }
                    },
                    clear: function () {
                        this.checkbox.children().not(".bt-hidden").not(".remark").remove();
                    }
                })
                me.data("btCheckBoxGroup", new btCheckBoxGroup(me));
            })

            form.find(".bt-verify-mobile").each(function () {
                var sender = $(this).find(".btn-send");
                sender.click(function (e) {
                    e.preventDefault();
                    var me = $(this);
                    if (me.attr("disabled") == null) {
                        var mobile = $("#Mobile").val();
                        if (!mobile) {
                            $.showError("请输入手机号");
                        }
                        else {
                            var url = me.attr("href");
                            me.attr("disabled", "true");
                            var time = 60;
                            $.post2(url, {
                                Mobile: $("#Mobile").val()
                            }, function (result) {
                                if (!result.success) {
                                    $.showError(result.errorText);
                                }
                            })
                            var func = function () {
                                time--;
                                me.text(time + "秒后可再次发送");
                                if (time == 0) {
                                    me.removeAttr("disabled");
                                    me.text("点击发送验证码");
                                }
                                else {
                                    setTimeout(func, 1000)
                                }
                            };
                            setTimeout(func, 1000)
                        }
                    }
                })
            });
            form.find(".bt-radio").each(function () {
                var me = $(this);
                var btRadio = function (ct) {
                    var me = $(ct);
                    this.radio = me;
                    this.url = me.data("url");
                    this.name = me.attr("for");
                    this.mustInput = me.data("mustinput");
                    this.mustInputText = me.data("mustinputtext");
                    this.currentChangedCallback = me.data("currentchangedcallback");
                    this.value = me.data("value");
                    this.labelEditUrl = me.data("label-edit-url");
                    this.value = me.data("value");
                    this.showEmpty = me.data("show-empty");
                    this.init();
                    this.loadUrl(this.url);
                }
                $.extend(btRadio.prototype, {
                    init: function () {
                        var $that = this;
                        var me = this.radio;
                        me.on("click", ".radio-inline", function (e) {
                            if ($(this).find("input[type=radio]").attr("readonly")) {
                                return false;
                            }
                        })
                        me.on("change", "input[type=radio]", function (e) {
                            e.preventDefault();
                            var readOnly = me.attr("readonly");
                            if (!readOnly) {
                                if (form.parent().hasClass("bt-condition-form-ct")) {
                                    form.find("button[type=submit]:last").click();
                                }
                                if ($that.currentChangedCallback) {
                                    var func = window[$that.currentChangedCallback];
                                    if (func) {
                                        func.call(me, $(this).val(), $(this).attr("title"));
                                    }
                                }
                            }
                        })
                        me.on("input propertychange", ".custom input", function () {
                            var radio = $(this).parent().parent().find("input[type=hidden]");

                            radio.attr("checked", true);
                            radio.prop("checked", true);
                            var text = $(this).val() || "";
                            text = text.trim();
                            var val = "Custom";
                            if (text) {
                                val = val + "-" + text;
                            }
                            radio.val(val);
                            if ($that.currentChangedCallback) {
                                var func = window[$that.currentChangedCallback];
                                if (func) {
                                    func.call(me, val, radio.attr("title"));
                                }
                            }
                        })
                    },
                    loadUrl: function (url, selectFirst) {
                        url = url || this.url;
                        if (url) {
                            var me = this;
                            $.get2(url, "", function (result) {
                                if (result.success && result.data) {
                                    var data = result.data.List;
                                    if (data) {
                                        me.loadData(data);
                                        if (selectFirst) {
                                            me.selectFirst();
                                        }
                                    }
                                }
                            })
                        }
                        return this;
                    },
                    loadData: function (data) {
                        if (data) {
                            var value = this.getValue() || this.value;
                            var selectFirst = false;
                            this.clear();
                            if (this.showEmpty) {
                                data.insert(0, { ID: "", Name: "不限" });
                                selectFirst = true;
                            }
                            for (var i = data.length - 1; i >= 0; i--) {
                                var val = data[i];
                                var id = (this.name + "_" + val.ID);
                                if (i > 0) selectFirst = false;
                                this.radio.prepend("<label class='radio-inline btn radio-inline-horitzontal " + (value == val.ID || selectFirst ? "active" : "") + "' for='" + id + "'>" +
                                    "<input id='" + id + "' name='" + this.name + "' title ='" + val.Name + "' type = 'radio' value ='" + val.ID + "' " + (value == val.ID || selectFirst ? "checked" : "") + " data-bv-notempty='" + (this.mustInput || 'false') + "' data-bv-notempty-message='" + (this.mustInputText || '') + "'>" +
                                    "<span>" + val.Name + "</span></label >");
                            }
                            if (this.labelEditUrl != null) {
                                this.radio.append("<div><a href='" + this.labelEditUrl + "' class='c-blue-dark window-link' data-callback='refresh'>编辑项</a></div>");
                            }
                        }
                        this.data = data;
                    },
                    clear: function () {
                        this.radio.children().not(".remark").remove();
                    },
                    getData: function () {
                        return this.data;
                    },
                    getValue: function () {
                        return me.find("input:checked").val();
                    },
                    setValue: function (value) {
                        var inputs = me.find("input");
                        inputs.removeAttr("checked");
                        inputs.parent().removeClass("active");
                        if (value) {
                            var list = value.split(",");
                            list.forEach(function (item) {
                                me.find("input[value=" + item + "]").prop('checked', 'checked').parent().addClass("active");
                            })
                        }
                        inputs.trigger("change");
                        return this;
                    },
                    selectFirst: function () {
                        var value = me.find("input:first-child").val();
                        this.setValue(value);
                        return this;
                    }
                })
                var btRadio = new btRadio(this);
                $(this).data("radio", btRadio);
            })

            selectBindFunc = function () {
                var me = $(this);
                var url = me.data("url");
                var showEmpty = me.data("show-empty");
                var autoselect = me.data("auto-select");
                var showEmptyText = me.data("show-empty-text");
                var value = me.data("value");
                var currentChangedCallback = me.data("currentchangedcallback");
                var data = me.data("data");
                var bindData = function (data) {
                    if (showEmpty) {
                        me.append("<option value=\"\">" + (showEmptyText || "--请选择--") + "</option>");
                    }
                    if (data) {
                        if (typeof data == "string") {
                            data = JSON.parse(data.replace(/\'/g, "\""));
                        }
                        me.data("data", data);
                        $.each(data, function (index, val) {
                            me.append("<option value=\"" + val.ID + "\" " + (value == val.ID ? "selected" : "") + " >" + val.Name + "</option>");
                        });
                    }
                }
                if (url && (!data || data.length == 0)) {
                    $.get2(url, "", function (result) {
                        if (result.success && result.data) {
                            var data = result.data.List;
                            if (data) {
                                bindData(data);
                            }
                        }
                    })
                }
                else {
                    bindData(data);
                }
                me.change(function () {
                    var value = me.val();
                    var text = me.find("option:selected").text();
                    if (currentChangedCallback) {
                        var func = window[currentChangedCallback];
                        if (func) {
                            func.call(me, value, text);
                        }
                    }
                    if (autoselect != false) {
                        if (form.parent().hasClass("bt-condition-form-ct")) {
                            form.find("button[type=submit]:last").click();
                        }
                    }

                    try {
                        form.bootstrapValidator('revalidateField', hidden);
                    } catch (e) {

                    }
                })
            }

            selectImageBindFunc = function () {
                var me = $(this);
                var url = me.data("url");
                var showEmpty = me.data("show-empty");
                var autoselect = me.data("auto-select");
                var showEmptyText = me.data("show-empty-text");
                var value = me.data("value");
                var currentChangedCallback = me.data("currentchangedcallback");
                var data = me.data("data");
                var bindData = function (data) {
                    if (showEmpty) {
                        me.append("<option value=\"\">" + (showEmptyText || "--请选择--") + "</option>");
                    }
                    if (data) {
                        if (typeof data == "string") {
                            data = JSON.parse(data.replace(/\'/g, "\""));
                        }
                        me.data("data", data);
                        $.each(data, function (index, val) {
                            me.append("<option value=\"" + val.ID + "\" " + (value == val.ID ? "selected" : "") + " >" + val.ID + "</option>");
                        });
                    }
                }
                if (url && (!data || data.length == 0)) {
                    $.get2(url, "", function (result) {
                        if (result.success && result.data) {
                            var data = result.data.List;
                            if (data) {
                                bindData(data);
                            }
                        }
                    })
                }
                else {
                    bindData(data);
                }
                me.change(function () {
                    var value = me.val();
                    if (currentChangedCallback) {
                        var func = window[currentChangedCallback];
                        if (func) {
                            func.call(me, value);
                        }
                    }
                    if (autoselect != false) {
                        if (form.parent().hasClass("bt-condition-form-ct")) {
                            form.find("button[type=submit]:last").click();
                        }
                    }
                    try {
                        form.bootstrapValidator('revalidateField', hidden);
                    } catch (e) {

                    }
                })
            }

            form.find(".bt-select").each(selectBindFunc)
            form.find(".bt-select-image").each(selectImageBindFunc)

            var selectPickers = form.find(".bt-select-picker");
            if (selectPickers.length > 0) {
                $.loadAssets({
                    scripts: ["/Plugins/boostrap-select/js/bootstrap-select.min.js"],
                    css: ["/Plugins/boostrap-select/css/bootstrap-select.min.css"]
                }, function () {
                    selectPickers.each(function () {
                        var me = $(this);
                        selectBindFunc.call(this);
                        me.selectpicker({
                            liveSearch: true,
                            maxOptions: 1,
                            size: 10,
                            noneSelectedText: "--请选择--",
                            noneResultsText: "没找到选项"
                        });
                    })
                });
            }

            var doubleboxs = form.find(".bt-doublebox");
            if (doubleboxs.length > 0) {
                $.loadAssets({
                    scripts: ["/Plugins/doublebox-boostrap/js/doublebox-bootstrap.js"],
                    css: ["/Plugins/doublebox-boostrap/css/doublebox-bootstrap.css"]
                }, function () {
                    doubleboxs.each(function () {
                        var me = $(this);
                        var vals = [];
                        var allData = me.data("data");
                        var vals = allData;
                        var value = me.data("value");
                        var selected = !$.isNullOrEmpty(value) ? value.toString().split(",") : [];
                        var nonSelectedList = [];
                        var selectedList = [];
                        if (selected.length > 0) {
                            nonSelectedList = vals.where(function (item) {
                                var isHas = selected.contains(item.ID);
                                return !isHas;
                            });
                            selectedList = vals.where(function (item) {
                                var isHas = selected.contains(item.ID);
                                return isHas;
                            });
                        } else {
                            nonSelectedList = vals;
                        }
                        me.doublebox({
                            nonSelectedListLabel: '未选',
                            selectedListLabel: '已选',
                            filterPlaceHolder: '搜索',
                            moveSelectedLabel: '选择',
                            moveAllLabel: '全部选择',
                            removeSelectedLabel: '移除',
                            removeAllLabel: '全部移除',
                            preserveSelectionOnMove: 'moved',
                            selectorMinimalHeight: 170,
                            moveOnSelect: false,
                            nonSelectedList: nonSelectedList,
                            selectedList: selectedList,
                            optionValue: "ID",
                            optionText: "Name",
                            doubleMove: false
                        });
                    })
                })
            }

            form.find(".bt-choose").each(function () {
                var btChoose = function (ct) {
                    var me = $(ct);
                    this.choose = me;
                    this.input = me.find(".form-control");
                    this.hidden = me.find("input[type=hidden]");
                    this.chooser = me.find(".icon-group-th-list");
                    this.clearer = me.find(".icon-group-remove");
                    this.showkey = $.parseBool(this.input.data("showkey"));
                    this.callback = this.input.data("callback");
                    this.idfield = this.input.data("idfield");
                    this.namefield = this.input.data("namefield");
                    this.firstdataurl = this.input.data("firstdataurl");
                    this.checkhandler = this.input.data("checkhandler");
                    this.checkfirstdatahandler = this.input.data("checkfirstdatahandler");
                    this.bothchangecallback = this.input.data("bothchangecallback");
                }
                $.extend(btChoose.prototype, {
                    init: function () {
                        var me = this;
                        var choosetype = this.input.data("choosetype");
                        if (choosetype == "both") {
                            this.input.change(function () {
                                var value = $(this).val();
                                me.hidden.val(value).trigger("change");

                                var func = window[me.bothchangecallback];
                                if (func)
                                    func.call(me.input);
                            });
                        }
                        var readonly = this.choose.attr('readonly');
                        if (readonly != "readonly") {
                            this.chooser.click(function (e) {
                                if (!e.isPropagationStopped()) {
                                    var checkResult = true;
                                    if (me.checkhandler) {
                                        var func = window[me.checkhandler];
                                        if (func) {
                                            checkResult = func.call(me);
                                        }
                                    }
                                    if (checkResult == true) {
                                        var val = me.hidden.val();
                                        var url = me.input.data("url");
                                        if (val && me.showkey)
                                            url = $.updateUrlParam(url, "key", me.hidden.val());
                                        var urlData = { url: url }
                                        me.input.trigger("btchoose.beforeChoose", urlData);
                                        if (urlData.cancel !== true) {
                                            url = urlData.url;
                                            $.formLinkCore.call(me.input, {
                                                url: url,
                                                callback: function (index, lay, buttonIndex) {
                                                    var frame = lay.find("iframe")[0];
                                                    var windowCallback = frame.contentWindow.window["windowCallback"];
                                                    var data = null;
                                                    if (windowCallback)
                                                        data = windowCallback.call(this, index, lay, buttonIndex);
                                                    if (data) {
                                                        if (me.idfield && me.namefield) {
                                                            var ids = [];
                                                            var names = [];
                                                            for (var i = 0; i < data.length; i++) {
                                                                ids.push(data[i][me.idfield]);
                                                                names.push(data[i][me.namefield]);
                                                            }
                                                            me.input.val(names.join(","));
                                                            me.hidden.val(ids.join(",")).trigger("change");
                                                            var form = me.choose.parents(".bt-form");
                                                            if (form && form.parent().hasClass("bt-condition-form-ct")) {
                                                                form.find("button[type=submit]:last").click();
                                                            }
                                                        }
                                                        if (me.callback) {
                                                            var func = window[me.callback];
                                                            if (func)
                                                                func.call(me.input, data);
                                                        }
                                                        try {
                                                            form.bootstrapValidator('revalidateField', me.hidden);
                                                        } catch (e) {

                                                        }
                                                        return true;
                                                    }
                                                    return false;
                                                }
                                            });
                                        }
                                    }
                                    return false;
                                }
                                else {
                                    return false;
                                }
                            })
                        }
                        me.clearer.click(function () {
                            me.input.val("");
                            me.hidden.val("").trigger("change");
                            var form = me.input.parents(".bt-form");
                            if (form && form.parent().hasClass("bt-condition-form-ct")) {
                                form.find("button[type=submit]:last").click();
                            }
                            if (me.callback) {
                                var func = window[me.callback];
                                if (func)
                                    func.call(me.input, "");
                            }
                        })
                        this.getFirstData();
                    },
                    setValue: function (value) {
                        this.hidden.val(value)
                        this.input.val(value);
                        this.getFirstData();
                    },
                    getFirstData: function () {
                        var me = this;
                        var value = this.input.val();
                        if (this.firstdataurl && value) {
                            var checkfirstdataResult = true;
                            if (me.checkfirstdatahandler) {
                                var func = window[me.checkfirstdatahandler];
                                if (func) {
                                    checkfirstdataResult = func.call(me, value);
                                }
                            }
                            if (checkfirstdataResult == true) {
                                $.get(me.firstdataurl, {
                                    value: value
                                }, function (result) {
                                    if (result.success)
                                        me.input.val(result.data);
                                })
                            }
                        }
                    }
                })

                var btCt = new btChoose(this);
                btCt.hidden.data("choose", btCt);
                btCt.init();
            })

            form.find(".bt-choose-block-link").each(function () {
                var me = $(this);
                var hidden = me.find(".bt-hidden");
                var chooser = me.find(".bt-choose-block-link-button");
                var blocklist = me.find(".bt-choose-block-link-list");

                var url = chooser.data("url");
                var callback = chooser.data("callback");
                var idfield = chooser.data("idfield");
                var namefield = chooser.data("namefield");
                var extrafield = chooser.data("extrafield");
                var template = chooser.data("template");

                var onItemAdd = chooser.data("onitemadd");
                var onItemDeleted = chooser.data("onitemdeleted");
                var itemLinkCallback = chooser.data("itemlinkcallback");

                var readonly = me.attr('readonly');
                if (readonly != "readonly") {
                    chooser.click(function () {
                        url = $.updateUrlParam(url, "type", "100");
                        $.formLinkCore.call(chooser, {
                            url: url,
                            callback: function (index, lay) {
                                var frame = lay.find("iframe")[0];
                                var windowCallback = frame.contentWindow.window["windowCallback"];
                                var data = null;
                                if (windowCallback)
                                    data = windowCallback();
                                if (data) {
                                    if (idfield && namefield) {
                                        var oldVal = hidden.val();
                                        var val = oldVal ? JSON.parse(oldVal.replace(/\'/g, "\"")) : [];
                                        for (var i = 0; i < data.length; i++) {
                                            var idValue = data[i][idfield];
                                            var nameValue = data[i][namefield];

                                            var isHas = val.containsWithKey({ ID: idValue }, "ID");
                                            if (!isHas) {
                                                if (extrafield) {
                                                    var extraValue = data[i][extrafield];
                                                    var item = { ID: idValue, Name: nameValue, Extra: extraValue };
                                                    val.push(item);
                                                    var str = $.template(template, item);
                                                    blocklist.append(str);
                                                }
                                                else {
                                                    var item = { ID: idValue, Name: nameValue };
                                                    val.push(item);
                                                    var str = $.template(template, item);
                                                    blocklist.append(str);
                                                }
                                            }
                                        }
                                        hidden.val(JSON.stringify(val));
                                        try {
                                            form.bootstrapValidator('revalidateField', hidden);
                                        } catch (e) {

                                        }
                                    }
                                    if (callback) {
                                        var func = window[callback];
                                        if (func)
                                            func(data);
                                    }
                                    return true;
                                }
                                return false;
                            }
                        });
                        return false;
                    })
                }

                blocklist.on("click", ".bt-choose-block-link-item>span", function (e) {
                    e.preventDefault();
                    var itemValue = $(this).data("value");
                    if (itemLinkCallback) {
                        var func = window[itemLinkCallback];
                        if (func) {
                            func.call(me, itemValue);
                        }
                    }
                    return false;
                })

                blocklist.on("click", ".bt-choose-block-link-item-delete", function (e) {
                    e.preventDefault();
                    var item = $(this).parents(".bt-choose-block-link-item");
                    var index = item.parent().children(".bt-choose-block-link-item").index(item);
                    var hidden = blocklist.parent().find(".bt-hidden");
                    var oldVal = hidden.val();
                    var vals = oldVal ? JSON.parse(oldVal.replaceAll(/\'/g, "\"")) : [];
                    var data = vals[index];
                    vals.splice(index, 1);
                    if (vals.length > 0) {
                        hidden.val(JSON.stringify(vals));
                    } else {
                        hidden.val("");
                    }
                    item.remove();
                    if (onItemDeleted) {
                        var func = window[onItemDeleted];
                        if (func) {
                            func(data);
                        }
                    }
                    return false;
                })
            })

            form.find(".bt-choose-block-link-preview").each(function () {
                var me = $(this);
                var hidden = me.find(".bt-hidden");
                var chooser = me.find(".bt-choose-block-link-button");
                var blocklist = me.find(".bt-choose-block-link-list");
                var itemLinkCallback = chooser.data("itemlinkcallback");

                blocklist.on("click", ".bt-choose-block-link-item>span", function (e) {
                    e.preventDefault();
                    var itemValue = $(this).data("value");
                    if (itemLinkCallback) {
                        var func = window[itemLinkCallback];
                        if (func) {
                            func.call(me, itemValue);
                        }
                    }
                    return false;
                })
            })

            form.find(".bt-radio-button").each(function () {
                var me = $(this);
                var currentChangedCallback = me.data("currentchangedcallback");
                $(this).find(".btn").click(function () {
                    me.find(".btn").each(function () {
                        $(this).removeClass($(this).data("active-class"));
                    });
                    $(this).parent().find("input").removeAttr("checked");
                    $(this).find("input").attr("checked", "checked");
                    $(this).find("input").prop("checked", "checked");
                    $(this).addClass($(this).data("active-class"));
                    if (currentChangedCallback) {
                        var func = window[currentChangedCallback];
                        if (func) {
                            func.call(me, $(this).find("input").val());
                        }
                    }
                })
            })

            form.find(".radio-tab-button").each(function () {
                var me = $(this);
                var currentChangedCallback = me.data("currentchangedcallback");
                $(this).find(".btn").click(function () {
                    $(this).parent().find("input").removeAttr("checked")
                    $(this).find("input").attr("checked", "checked");
                    $(this).find("input").prop("checked", "checked");
                    if (currentChangedCallback) {
                        var func = window[currentChangedCallback];
                        if (func) {
                            func.call(me, $(this).find("input").val());
                        }
                    }
                    if (form.parent().hasClass("bt-condition-form-ct")) {
                        form.find("button[type=submit]:last").click();
                    }
                })
            })

            form.find(".bt-img-preview").each(btImagePreviewFunc);

            form.find(".bt-star-comment").each(function () {
                var starcomment = $(this).find(".star-comment");
                var hidden = $(this).find("input[type=hidden]");
                var callback = function (score) {
                    hidden.val(score);
                }
                starcomment.data("callback", callback);
            })

            form.find(".bt-map").each(function () {
                var me = $(this);
                var chooser = me.find(".input-group-addon");
                var readonly = me.attr('readonly');
                var input = me.find(".form-control");
                var hiddenName = me.find("input[field=name]");
                var hiddenLng = me.parents("form").find("input[field=Lng]");
                var hiddenLat = me.parents("form").find("input[field=Lat]");
                var hiddenAds = me.find("input[field=ads]");
                if (readonly != "readonly") {
                    chooser.click(function (e) {
                        e.preventDefault();
                        var url = "/Home/Map";
                        var lng = hiddenLng.val();
                        if (lng)
                            url = $.updateUrlParam(url, "lng", lng);
                        var lat = hiddenLat.val();
                        if (lat)
                            url = $.updateUrlParam(url, "lat", lat);
                        $.formLinkCore.call(input, {
                            url: url,
                            callback: function (index, lay) {
                                var frame = lay.find("iframe")[0];
                                var windowCallback = frame.contentWindow.window["windowCallback"];
                                var data = null;
                                if (windowCallback)
                                    data = windowCallback();
                                if (data) {
                                    hiddenName.val(data.name);
                                    hiddenLng.val(data.lng);
                                    hiddenLat.val(data.lat);
                                    input.val(data.name);
                                    hiddenAds.val(data.address);
                                    return true;
                                }
                                return false;
                            }
                        })
                        return false;
                    })
                }
            })

            form.find(".bt-map-district-design").each(function () {
                var me = $(this);
                var chooser = me.find(".input-group-addon");
                var readonly = me.attr('readonly');
                var input = me.find(".form-control");

                var hiddenName = me.find("input[field=name]");
                var hiddenLng = me.parents("form").find("input[field=lng]");
                var hiddenLat = me.parents("form").find("input[field=lat]");
                var hiddenZoom = me.parents("form").find("input[field=zoom]");
                var hiddenPath = me.parents("form").find("input[field=path]");

                if (readonly != "readonly") {
                    chooser.click(function (e) {
                        e.preventDefault();
                        var url = "/Home/MapDistrict";
                        var lng = hiddenLng.val();
                        if (lng)
                            url = $.updateUrlParam(url, "lng", lng);
                        var lat = hiddenLat.val();
                        if (lat)
                            url = $.updateUrlParam(url, "lat", lat);
                        var zoom = hiddenZoom.val();
                        if (zoom)
                            url = $.updateUrlParam(url, "zoom", zoom);
                        var path = hiddenPath.val();
                        if (path)
                            url = $.updateUrlParam(url, "path", encodeURI(path));

                        $.formLinkCore.call(input, {
                            url: url,
                            callback: function (index, lay) {
                                var frame = lay.find("iframe")[0];
                                var windowCallback = frame.contentWindow.window["windowCallback"];
                                var data = null;
                                if (windowCallback)
                                    data = windowCallback();
                                if (data) {
                                    hiddenName.val(data.name);
                                    hiddenLng.val(data.lng);
                                    hiddenLat.val(data.lat);
                                    hiddenZoom.val(data.zoom);
                                    hiddenPath.val(data.path);
                                    input.val(data.name);
                                    return true;
                                }
                                return false;
                            }
                        })
                        return false;
                    })
                }
            })

            var ueditors = form.find(".bt-ueditor");
            if (ueditors.length > 0) {
                $.loadAssets({
                    scripts: ["/Plugins/ueditor/ueditor.config.js",
                        "/Plugins/ueditor/ueditor.all.js"]
                }, function () {
                    ueditors.each(function () {
                        var textarea = $(this);
                        var height = textarea.data("height");
                        var ue = new baidu.editor.ui.Editor();
                        ue.render(textarea.attr("id"));

                        ue.ready(function () {
                            if (height) {
                                ue.setHeight(height);

                            }
                            this.addListener('contentChange', function (editor) {
                                try {
                                    form.bootstrapValidator('resetField', textarea.attr("name"));
                                } catch (e) {

                                }
                            });
                        })
                    })
                })
            }

            var btUploads = form.find(".bt-uploader");
            if (btUploads.length > 0) {
                var btUpload = function (template, uploadmode, valuemode, uploadQueue) {
                    this.template = template;
                    this.uploadmode = uploadmode;
                    this.valuemode = valuemode;
                    this.uploadQueue = uploadQueue;
                }
                $.extend(btUpload.prototype, {
                    reloadData: function (url) {
                        this.clearData();
                        var me = this;
                        var urls = url ? url.split(";") : [];
                        urls.forEach(function (item) {
                            me.loadData({ FilePath: item });
                        })
                    },
                    loadData: function (fileData) {
                        var url = fileData.FilePath;
                        var hidden = this.uploadQueue.find(".uploader-hidden");
                        if (this.uploadmode == "replace") {
                            this.uploadQueue.find(":not(input[type=hidden])").remove();
                            if (this.valuemode == "url") {
                                hidden.val(url).change();
                            }
                            var str = $.template(this.template, fileData);
                            this.uploadQueue.append(str);
                        }
                        else if (this.uploadmode == "append") {
                            if (this.valuemode == "url") {
                                var val = hidden.val() || "";
                                val = (val ? (val + ";") : "") + url;
                                hidden.val(val).change();
                            }
                            else {
                                var oldVal = hidden.val();
                                var val2 = oldVal ? JSON.parse(oldVal.replace(/\'/g, "\"")) : [];
                                val2.push({ FileName: fileData.FileName + fileData.FileExt, FilePath: fileData.FilePath })
                                hidden.val(JSON.stringify(val2)).trigger("change");
                            }

                            var str2 = $.template(this.template, fileData);
                            this.uploadQueue.append(str2);
                        }
                        else if (this.uploadmode == "update") {
                            var queueItem = $("#queue_" + file.id);
                            if (queueItem.length > 0) {
                                queueItem.find(".uploader-filepath").val(fileData.FilePath);
                                queueItem.find(".uploader-filepath-link").attr("href", fileData.FilePath);
                                queueItem.find(".uploader-state").text("已上传");
                            }
                        }
                    },
                    clearData: function () {
                        this.uploadQueue.find(":not(input[type=hidden])").remove();
                        this.uploadQueue.find(".uploader-hidden").val();
                    }
                })
                btUploads.each(function () {
                    var me = $(this);
                    var id = me.attr("id");
                    var buttontext = me.data("buttontext");
                    var typedesc = me.data("typedesc");
                    var typeexts = me.data("typeexts");
                    var sizelimit = me.data("sizelimit");
                    var multi = me.data("multi");
                    var width = me.data("width");
                    var uploadmode = me.data("uploadmode");
                    var template = me.data("template").replace(/\\{/g, "{").replace(/\\}/g, "}");
                    var valuemode = me.data("valuemode");
                    var maxQueueCnt = me.data("maxqueuecnt");
                    var onFileUploaded = me.data("onfileuploaded");
                    var onFileDeleted = me.data("onfiledeleted");
                    var uploadQueue = me.parent().find(".uploader-queue");
                    var uploadPercent = me.parent().find(".uploader-percent");
                    var uploadHidden = uploadQueue.find(".uploader-hidden");
                    var url = me.data("url");
                    var uncompress = me.data("uncompress");
                    var onValueChanged = me.data("onvaluechanged");
                    var companyCode = me.data("companycode");
                    if (uncompress) {
                        url += (url.contains("?") ? "&" : "?") + "uncompress=true";
                    }
                    me.removeClass("bt-uploader");
                    if (uploadHidden.val()) {
                        if (uploadmode == "append" && valuemode == "url") {
                            var hiddenVal = uploadHidden.val();
                            var hiddenVals = hiddenVal.split(";");
                            for (var i = 0; i < hiddenVals.length; i++) {
                                var str2 = $.template(template, {
                                    FilePath: hiddenVals[i]
                                });
                                uploadQueue.append(str2);
                            }
                        }
                    }
                    if (me.uploadify) {
                        me.uploadify({
                            height: 34,
                            checkExisting: false,
                            swf: '/Plugins/uploadify/uploadify.swf',
                            uploader: url,
                            width: width,
                            buttonText: buttontext,
                            button_placeholder: buttontext,
                            buttonImg: null,
                            fileSizeLimit: sizelimit,
                            fileTypeDesc: typedesc,
                            fileTypeExts: typeexts,
                            multi: multi,
                            uploadLimit: 999,
                            wmode: "transparent",
                            removeCompleted: false,
                            formData: { CompanyCode: companyCode },
                            onSWFReady: function () {
                                me.uploadify("disable", uploadQueue.children().length >= maxQueueCnt + 1);
                            },
                            onSelect: function (file) {
                                if (uploadmode == "update") {
                                    var fileNoExtName = file.name.indexOf(".") >= 0 ? file.name.substring(0, file.name.indexOf(".")) : file.name;
                                    var str = $.template(template, {
                                        FileID: "queue_" + file.id,
                                        FileIndex: uploadQueue.children().length,
                                        FileExt: file.type,
                                        FileSize: file.size,
                                        FileSizeZ: $.fileSize(file.size),
                                        FileName: fileNoExtName,
                                        Order: uploadQueue.children().length + 1
                                    });
                                    uploadQueue.append(str);
                                }
                                else if (uploadmode == "replace") {
                                    uploadPercent.css("display", "inline-block");
                                }
                            },
                            onSelectError: function (file, errorCode, errorMsg) {
                                if (errorCode == -100) {
                                    $.showError("上传数量达到最大限制");
                                }
                                else if (errorCode == -110) {
                                    $.showError("选择的文件超过了最大限制" + sizelimit);
                                }
                                else if (errorCode == -120) {
                                    $.showError("不能上传空文件");
                                }
                                else {
                                    alert(errorCode);
                                }
                            },
                            onUploadProgress: function (file, fileBytesLoaded, fileTotalBytes,
                                queueBytesLoaded, swfuploadifyQueueUploadSize) {
                                var rate = Math.round(fileBytesLoaded * 100 / fileTotalBytes);
                                if (uploadmode == "update") {
                                    var queueItem = $("#queue_" + file.id);
                                    if (queueItem.length > 0) {
                                        queueItem.find(".uploader-state").text(rate + "%");
                                    }
                                }
                                uploadPercent.text(rate + "%");
                            },
                            onUploadSuccess: function (file, data, response) {
                                data = JSON.parse(data);
                                if (data.success) {
                                    data = data.data;
                                    var url = data.FilePath;
                                    var hidden = uploadQueue.find(".uploader-hidden");
                                    if (uploadmode == "replace") {
                                        uploadQueue.find(":not(input[type=hidden])").remove();
                                        if (valuemode == "url")
                                            hidden.val(url).change();
                                        var str = $.template(template, data);
                                        uploadQueue.append(str);
                                    }
                                    else if (uploadmode == "append") {
                                        if (valuemode == "url") {
                                            var val = hidden.val() || "";
                                            val = (val ? (val + ";") : "") + url;
                                            hidden.val(val).change();
                                        }
                                        else {
                                            var oldVal = hidden.val();
                                            var val2 = oldVal ? JSON.parse(oldVal.replace(/\'/g, "\"")) : [];
                                            val2.push({ FileName: data.FileName, FilePath: data.FilePath })
                                            hidden.val(JSON.stringify(val2)).trigger("change");
                                        }

                                        var str2 = $.template(template, data);
                                        uploadQueue.append(str2);
                                    }
                                    else if (uploadmode == "update") {
                                        var queueItem = $("#queue_" + file.id);
                                        if (queueItem.length > 0) {
                                            queueItem.find(".uploader-filepath").val(data.FilePath);
                                            queueItem.find(".uploader-filepath-link").attr("href", data.FilePath);
                                            queueItem.find(".uploader-state").text("已上传");
                                        }
                                    }
                                    if (onFileUploaded) {
                                        var func = window[onFileUploaded];
                                        if (func) {
                                            func(data);
                                        }
                                    }
                                    if (onValueChanged) {
                                        var func2 = window[onValueChanged];
                                        if (func2) {
                                            func2(id, hidden.val());
                                        }
                                    }
                                    me.uploadify("disable", uploadQueue.children().length >= maxQueueCnt);
                                    this.queueData.files[file.id].data = data;

                                    try {
                                        form.bootstrapValidator('revalidateField', uploadHidden.attr("name"));
                                    } catch (e) {

                                    }
                                }
                            },
                            overrideEvents: ["onUploadError"],
                            onUploadComplete: function (file) {
                                this.queueData.files[file.id].uploaded = true;
                            },
                            onUploadError: function (file, errorCode, errorMsg, errorString) {
                                uploadPercent.text("上传失败：" + errorMsg)
                            },
                            onError: function (event, queueId, fileObj, errorObj) {
                            },
                            onComplete: function () {
                            }
                        });
                    }
                    else {
                        var fileId = 1;
                        var extensions = typeexts.split(";");
                        var newExtensions = [];
                        if (extensions && extensions.length > 0) {
                            for (var i = 0; i < extensions.length; i++) {
                                var ext = extensions[i];
                                if (ext && ext.indexOf(".") >= 0) {
                                    newExtensions.push(ext.substring(ext.indexOf(".") + 1));
                                }
                            }
                        }



                        me.fileinput({
                            browseLabel: buttontext,
                            language: 'zh',
                            uploadUrl: url,
                            showUpload: false,
                            showCaption: false,
                            showPreview: false,
                            //showUploadedThumbs:false,
                            showRemove: false,
                            showCancel: false,
                            dropZoneEnabled: false,
                            allowedFileExtensions: newExtensions
                        }).on("filebatchselected", function (event, files) {
                            var fileInput = $(this).data("fileinput");
                            if (fileInput.$container.find(".kv-fileinput-error").length == 0) {
                                var item = fileInput.$container.prepend("<div class='file-error-message kv-fileinput-error'></div>");
                                fileInput.$errorContainer = item.children().first();
                            }
                            files.forEach(function (file) {
                                if (uploadmode == "update") {
                                    file.id = fileId++;
                                    var fileNoExtName = file.name.lastIndexOf(".") >= 0 ? file.name.substring(0, file.name.lastIndexOf(".")) : file.name;
                                    var fileExtName = file.name.lastIndexOf(".") >= 0 ? file.name.substring(file.name.lastIndexOf(".")) : "";
                                    var str = $.template(template, {
                                        FileID: "queue_" + file.id,
                                        FileIndex: uploadQueue.children().length,
                                        FileExt: fileExtName,
                                        FileSize: file.size,
                                        FileSizeZ: $.fileSize(file.size),
                                        FileName: fileNoExtName,
                                        Order: uploadQueue.children().length + 1
                                    });
                                    str = str.replaceAll("0%", "上传中");
                                    uploadQueue.append(str);
                                }
                                else if (uploadmode == "replace") {
                                    uploadPercent.css("display", "inline-block");
                                }
                            })
                            $(this).fileinput("upload");
                        }).on("fileerror", function (event, data, msg) {
                            $.showWarning(msg);
                        }).on("fileunlock", function (event, data, msg) {
                            var isMaxCnt = uploadQueue.find(".uploader-image-item").length >= maxQueueCnt;
                            if (isMaxCnt && uploadmode == "append") {
                                $(this).fileinput("disable");
                            }
                            else {
                                $(this).fileinput("enable");
                            }
                        }).on("fileuploaded", function (event, data, previewId, index) {
                            var left = maxQueueCnt - uploadQueue.find(".uploader-image-item").length;
                            if (data.response && (left > 0 || uploadmode != "append")) {
                                var file = data.files[index];
                                var fileData = data.response.data;
                                var url = fileData.FilePath;
                                var hidden = uploadQueue.find(".uploader-hidden");
                                if (uploadmode == "replace") {
                                    uploadQueue.find(":not(input[type=hidden])").remove();
                                    if (valuemode == "url") {
                                        hidden.val(url).change();
                                    }
                                    var str = $.template(template, fileData);
                                    uploadQueue.append(str);
                                }
                                else if (uploadmode == "append") {
                                    if (valuemode == "url") {
                                        var val = hidden.val() || "";
                                        val = (val ? (val + ";") : "") + url;
                                        hidden.val(val).change();
                                    }
                                    else {
                                        var oldVal = hidden.val();
                                        var val2 = oldVal ? JSON.parse(oldVal.replace(/\'/g, "\"")) : [];
                                        val2.push({ FileName: fileData.FileName + fileData.FileExt, FilePath: fileData.FilePath })
                                        hidden.val(JSON.stringify(val2)).trigger("change");
                                    }

                                    var str2 = $.template(template, fileData);
                                    uploadQueue.append(str2);
                                }
                                else if (uploadmode == "update") {
                                    var queueItem = $("#queue_" + file.id);
                                    if (queueItem.length > 0) {
                                        queueItem.find(".uploader-filepath").val(fileData.FilePath);
                                        queueItem.find(".uploader-filepath-link").attr("href", fileData.FilePath);
                                        queueItem.find(".uploader-state").text("已上传");
                                    }
                                }
                                if (onFileUploaded) {
                                    var func = window[onFileUploaded];
                                    if (func) {
                                        func(fileData);
                                    }
                                }
                                if (onValueChanged) {
                                    var func2 = window[onValueChanged];
                                    if (func2) {
                                        func2(id, hidden.val());
                                    }
                                }
                                try {
                                    form.bootstrapValidator('revalidateField', uploadHidden.attr("name"));
                                } catch (e) {

                                }
                            }
                        });
                        me.parents(".btn-file").removeClass("btn-primary").addClass("btn-default");
                        var uploader = new btUpload(template, uploadmode, valuemode, uploadQueue);
                        me.data("uploader", uploader);
                    }
                    uploadQueue.on("click", ".uploader-image-prev", function (e) {
                        e.preventDefault();
                        var array = [];
                        var idx = 0;
                        var item = $(this).parents(".uploader-image-item");
                        uploadQueue.find(".uploader-image-item").each(function (index) {
                            if (item[0] == this) {
                                idx = index;
                            }
                            array.push($(this).find(".uploader-image").attr("src"));
                        })
                        $.showPrev(array.join(";"), idx);
                    })

                    uploadQueue.on("click", ".uploader-image-delete", function (e) {
                        e.preventDefault();
                        var item = $(this).parents(".uploader-image-item");
                        var url = item.find(".uploader-image").attr("src");
                        var hidden = uploadQueue.find(".uploader-hidden");
                        var val = hidden.val();
                        if (hidden.hasClass("single")) {
                            var defaultvalue = hidden.data("default");
                            hidden.val("");
                            item.find(".uploader-image").attr("src", defaultvalue);
                        } else {
                            var vals = val.split(";");
                            for (var i = 0; i < vals.length; i++) {
                                if (vals[i] == url) {
                                    vals.splice(i, 1);
                                    break;
                                }
                            }
                            hidden.val(vals.join(";")).trigger("change");
                            item.remove();
                            if (onFileDeleted) {
                                var func = window[onFileDeleted];
                                if (func) {
                                    func();
                                }
                            }
                            if (onValueChanged) {
                                var func2 = window[onValueChanged];
                                if (func2) {
                                    func2(id, hidden.val());
                                }
                            }
                        }

                        if (me.uploadify) {
                            me.uploadify("disable", false);
                        } else {
                            me.fileinput("enable");
                        }
                        return false;
                    })

                    uploadQueue.on("click", ".uploader-file-delete", function (e) {
                        e.preventDefault();
                        var item = $(this).parents(".uploader-file-item");
                        var index = item.parent().children(".uploader-file-item").index(item);
                        var hidden = uploadQueue.find(".uploader-hidden");
                        var oldVal = hidden.val();
                        var vals = oldVal ? JSON.parse(oldVal.replace(/\'/g, "\"")) : [];
                        vals.splice(index, 1)
                        var val = JSON.stringify(vals);
                        hidden.val(val == "[]" ? "" : val).trigger("change");
                        item.remove();
                        if (onFileDeleted) {
                            var func = window[onFileDeleted];
                            if (func) {
                                func();
                            }
                        }
                        if (me.uploadify) {
                            me.uploadify("disable", false);
                        } else {
                            me.fileinput("enable");
                        }
                        return false;
                    })

                    uploadQueue.on("click", ".uploader-delete", function (e) {
                        e.preventDefault();
                        var item = $(this).parents(".uploader-item");
                        item.remove();
                        if (me.uploadify) {
                            me.uploadify("disable", false);
                        } else {
                            me.fileinput("enable");
                        }
                        return false;
                    })

                    uploadQueue.on("click", ".uploader-delete-hidden", function (e) {
                        e.preventDefault();
                        var item = $(this).parents(".uploader-item");
                        item.css("display", "none");
                        item.find(".uploader-deletesign").val("true");

                        if (me.uploadify) {
                            me.uploadify("disable", false);
                        } else {
                            me.fileinput("enable");
                        }
                        return false;
                    })
                })
            }

            //form.find(".bt-input-image-uploader").each(function () {
            //    var me = $(this);
            //    var uploadQueue = me.find(".uploader-queue");
            //    var hidden = uploadQueue.find(".uploader-hidden");
            //    var button = me.find(".uploader-button");
            //    var input = button.find("input");
            //    var template = me.data("template");
            //    var maxCnt = me.data("max-cnt");
            //    var onPreview = me.data("onpreview");
            //    button.on("change", "input", function (e) {
            //        var imagesCnt = uploadQueue.find(".uploader-image-item").length;
            //        var leftCnt = maxCnt - imagesCnt;
            //        $(this).css("display", "none");
            //        this.name = "files";
            //        var form = document.createElement("form");
            //        form.appendChild(this);
            //        var formData = new FormData(form);
            //        $.ajax({
            //            url: "/Attachment/UploadFiles",
            //            type: "post",
            //            data: formData,
            //            processData: false,
            //            contentType: false,
            //            success: function (data) {
            //                var val = hidden.val();
            //                if (data.success) {
            //                    var vals = val.split(";");
            //                    for (var i = 0; i < data.data.length; i++) {
            //                        if (leftCnt > 0) {
            //                            val = (val ? (val + ";") : "") + data.data[i].FilePath;
            //                            uploadQueue.append($.template(template, data.data[i]));
            //                            leftCnt--;
            //                        }
            //                    }
            //                }
            //                hidden.val(val).trigger("change");
            //                if (leftCnt == 0) {
            //                    button.css("display", "none");
            //                }
            //            },
            //            error: function (e) {
            //                $.showWarning("上传图片失败");
            //            }
            //        });
            //        this.name = undefined;
            //        var file = document.createElement("input");
            //        file.type = this.type;
            //        file.accept = this.accept;
            //        file.multiple = this.multiple;
            //        file.className = this.className;
            //        button.prepend(file);
            //    })

            //    uploadQueue.on("click", ".uploader-image-item", function (e) {
            //        e.preventDefault();
            //        var imageDiv = $(this).find(".uploader-image");
            //        var images = [];
            //        var image;
            //        uploadQueue.find(".uploader-image").each(function () {
            //            images.push($(this).attr("src"));
            //            if (imageDiv[0] == this) {
            //                image = $(this).attr("src");
            //            }
            //        })
            //        if (onPreview && window[onPreview]) {
            //            window[onPreview].call(me, image, images);
            //        }
            //        else {
            //            $.showPrev(image);
            //        }
            //    })

            //    uploadQueue.on("click", ".uploader-image-delete", function (e) {
            //        e.preventDefault();
            //        var item = $(this).parents(".uploader-image-item");
            //        var url = item.find(".uploader-image").attr("src");
            //        var hidden = uploadQueue.find(".uploader-hidden");
            //        var val = hidden.val();
            //        var vals = val.split(";");
            //        for (var i = 0; i < vals.length; i++) {
            //            if (vals[i] == url) {
            //                vals.splice(i, 1);
            //                break;
            //            }
            //        }
            //        hidden.val(vals.join(";")).trigger("change");
            //        item.remove();
            //        button.css("display", "");
            //        return false;
            //    })
            //})
            $(".bt-attachs-uploader").each(function () {
                $(this).btattachsuploader();
            })
        }
    })

    $(".bt-form").each(function () {
        $(this).btform();
    })

    $(".bt-ct").each(function () {
        var container_form = $(this);
        container_form.find(".bt-display").each(function () {
            var me = $(this);
            var firstdataurl = me.data("firstdataurl");
            if (firstdataurl) {
                var value = me.text();
                if (value) {
                    $.get(firstdataurl, {
                        value: value
                    }, function (result) {
                        if (result.success)
                            me.text(result.data);
                    })
                }
            }
        })


        container_form.find(".bt-attachs").each(function () {
            var me = $(this);
            var hidden = me.find("input");
            var allowext = me.data("allowext");
            var callback = me.data("callback");
            var val = hidden.val();
            var button = me.find(".btn");
            var exttip = me.find(".exttip");
            var max = me.data("max") || 99;
            var data = [];
            try {
                if (val) {
                    data = JSON.parse(val.replace(/\'/g, "\""));
                    //处理单引号
                    val = val.replace(/\'/g, "\"");
                    hidden.val(val);
                }
            } catch (e) {

            }
            me.prepend("<div class='attachs-ct'/>");
            var container = me.find(".attachs-ct");
            var template = '<div class="uploader-file-item"><a class="file-preview-link" data-code="{{d.AttCode}}" data-ext="{{d.AttExt}}">{{d.AttName+(d.AttExt||"")}}</a><a class="uploader-file-delete"><i class="glyphicon glyphicon-remove"></i></a></div>';
            data.forEach(function (item) {
                container.append($.template(template, item))
            });
            function checkCanUpload() {
                button.css("display", data.length >= max ? "none" : "inline-block");
                if (!$.isNullOrEmpty(allowext)) {
                    exttip.css("display", data.length >= max ? "none" : "inline-block");
                }
            }
            checkCanUpload();
            me.on("click", ".uploader-file-delete", function (e) {
                e.preventDefault();
                var item = $(this).parents(".uploader-file-item");
                var index = item.parent().children(".uploader-file-item").index(item);
                data.splice(index, 1)
                var val = JSON.stringify(data);
                hidden.val(val == "[]" ? "" : val);
                item.remove();
                checkCanUpload();
            })
            button.off('click').on("click", function (e) {
                e.preventDefault();
                if (me.attr("disabled") != "disabled" && e.result !== false) {
                    $(this).windowLink({
                        url: "/CommonAttach/Window?type=100&multi=" + (max - data.length > 1 ? true : false) + "&allowext=" + allowext,
                        callback: function (results) {
                            var resultArray = new Array();
                            results.forEach(function (item1) {
                                if (data.length < max) {
                                    var flag = data.containsWithFunc(function (item2) {
                                        return item1.AttCode == item2.AttCode
                                    });
                                    if (!flag) {
                                        var item = {
                                            AttCode: item1.AttCode,
                                            AttName: item1.AttName,
                                            AttExt: item1.AttExt
                                        };
                                        data.push(item);
                                        resultArray.push(item);
                                        hidden.val(JSON.stringify(data));
                                        container.append($.template(template, item))
                                        checkCanUpload();
                                    }
                                }
                            })
                            var func = window[callback];
                            if (func)
                                func.call(me, resultArray);
                        }
                    })
                }
            })
        })

        container_form.find(".bt-attachs-preview").each(function () {
            var me = $(this);
            var hidden = me.find("input");
            var val = hidden.val();
            var data = [];
            try {
                if (val) data = JSON.parse(val.replace(/\'/g, "\""))
            } catch (e) {

            }
            var imgExts = [".jpg", ".png", ".gif", ".jpeg"];

            var imgCodes = [];
            for (var i = 0; i < data.length; i++) {
                var item = data[i];
                if (item.AttExt != undefined
                    && item.AttExt != null
                    && item.AttExt != ""
                    && imgExts.contains(item.AttExt)) {
                    imgCodes.push(item.AttCode);
                }
            }
            me.prepend("<div class='attachs-ct attach-block'/>");
            var container = me.find(".attachs-ct");
            var template = '<div class="uploader-file-item"><a class="file-preview-link" data-code="{{d.AttCode}}" data-ext="{{d.AttExt}}">{{d.AttName+(d.AttExt||"")}}</a></div>';
            var draw = function (data, imgData) {
                data.forEach(function (item) {
                    if (imgData && imgExts.contains(item.AttExt)) {
                        var imgItem = imgData.find(function (obj) {
                            return obj.ID == item.AttCode;
                        });
                        template = '<div class="uploader-file-item"><a class="attach-block-img"  data-src="' + imgItem.Name + '">{{d.AttName+(d.AttExt||"")}}</a></div>';

                    } else {
                        template = '<div class="uploader-file-item"><a class="file-preview-link" data-code="{{d.AttCode}}" data-ext="{{d.AttExt}}">{{d.AttName+(d.AttExt||"")}}</a></div>';
                    }
                    container.append($.template(template, item))
                });
            }

            if (imgCodes && imgCodes.length > 0) {
                var codes = imgCodes.join(",");
                $.ajax({
                    type: 'post',
                    dataType: "json",
                    url: "/CommonAttach/GetImgAtts",
                    data: { codes: codes },
                    async: false,
                    success: function (result) {
                        if (result.success) {
                            var imgData = result.data;
                            draw(data, imgData);
                        }
                    }
                });
            } else {
                draw(data);
            }
        })
    })


    var timerCountDown;
    var validtimeSpan;
    function CountDown() {
        validtimeSpan = 2;
        if (timerCountDown) {
            clearInterval(timerCountDown);
        }
        timerCountDown = setInterval(function () {
            var template = '<div class="uploader-file-item" data-code="{{d.AttCode}}" data-status="{{d.Status}}"><a class="file-preview-link" data-code="{{d.AttCode}}" data-ext="{{d.AttExt}}">{{d.AttName+(d.AttExt||"")}}</a><a class="uploader-file-delete"><i class="glyphicon glyphicon-remove"></i></a></div>';
            var template2 = '<div class="uploader-file-item converting" data-code="{{d.AttCode}}" data-status="{{d.Status}}"><a data-code="{{d.AttCode}}">{{d.AttName+(d.AttExt||"")}}</a><span class="state" data-status="{{d.Status}}">{{d.StatusName}}</span><i class="fa-li fa fa-spinner fa-spin"></i></div>';
            var template3 = '<div class="uploader-file-item converterror" data-code="{{d.AttCode}}" data-status="{{d.Status}}"><a data-code="{{d.AttCode}}">{{d.AttName+(d.AttExt||"")}}</a><span class="state" data-status="{{d.Status}}">{{d.StatusName}}</span><a class="uploader-file-delete"><i class="glyphicon glyphicon-remove"></i></a></div>';
            if (validtimeSpan == 0 && convertArray.length > 0) {
                var codes = convertArray.join(",");
                $.ajax({
                    type: 'post',
                    dataType: "json",
                    url: "/CommonAttach/CheckAtt",
                    data: { codes: codes },
                    async: false,
                    success: function (result) {
                        if (result.success && result.data) {
                            var data = result.data;
                            if (data && data.length > 0) {

                                var convertingDiv = $("div.converting");
                                for (var i = 0; i < data.length; i++) {
                                    var item = data[i];
                                    for (var j = 0; j < convertingDiv.length; j++) {
                                        var convertingItem = $(convertingDiv[j]);
                                        if (convertingItem.data("code") == item.AttCode) {
                                            if (item.Status == 10) {
                                                convertingItem.replaceWith($.template(template, item));
                                            } else if (item.Status == -1) {
                                                convertingItem.replaceWith($.template(template3, item));
                                            }
                                            //else {
                                            //    convertingItem.replaceWith($.template(template2, item))
                                            //}
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
                checkData();
            }
            validtimeSpan--;
        }, 1000);
    }
    var convertArray;
    function checkData() {
        convertArray = new Array();
        var convertingDiv = $("div.converting");
        for (var i = 0; i < convertingDiv.length; i++) {
            var convertingItem = $(convertingDiv[i]);
            if (convertingItem.data("status") == "1") {
                convertArray.push(convertingItem.data("code"));
            }
        }
        if (convertArray.length > 0) {
            CountDown();
        }
    }


    $.fn.extend({
        mergeCells: function (data, fieldNames, isMergeEmpty) {
            var me = $(this);
            if (data.length == 0) {
                return;
            }
            if (fieldNames.length > 0) {
                var arrMerge = new Array(data.length);
                for (var m = 0; m < fieldNames.length; m++) {
                    var fieldName = fieldNames[m];
                    var numArr = [];
                    var value = data[0][fieldName];
                    var num = 0;
                    var isMerge = true;
                    for (var i = 0; i < data.length; i++) {

                        if (m > 0) {
                            isMerge = arrMerge[i];
                        }
                        if ($.isArray(value) && isMerge && JSON.stringify(value) == JSON.stringify(data[i][fieldName])) {
                            num++;
                            arrMerge[i] = true;
                            if ((i + 1) == data.length) {
                                numArr.push(num);
                            }
                        }
                        else if (isMerge && value == data[i][fieldName] && (isMergeEmpty || value != '')) {
                            num++;
                            arrMerge[i] = true;
                            if ((i + 1) == data.length) {
                                numArr.push(num);
                            }
                        }
                        else {
                            arrMerge[i] = false;
                            numArr.push(num);
                            value = data[i][fieldName];
                            num = 1;
                            continue;
                        }

                    }
                    var merIndex = 0;
                    for (var i = 0; i < numArr.length; i++) {
                        if (numArr[i] > 1) {
                            me.bootstrapTable('mergeCells', { index: merIndex, field: fieldName, colspan: 1, rowspan: numArr[i] })
                        }
                        merIndex += numArr[i];
                    }
                }

            }
        }
    })
    if ($.fn.bootstrapTable) {
        $.fn.bootstrapTable.Constructor.prototype.refreshOptions = function (options) {
            this.options = $.extend(this.options, options);
        };
    }


    $.fn.extend({
        bttable: function () {
            var me = $(this);
            var url = me.data("url");
            var rowstylefunc = me.data("rowstylefunc");
            var footerstylefunc = me.data("footerstylefunc");
            var tempUrl = me.data("tempurl");
            var data = me.data("data");
            var toolbar = me.prevAll(".bs-bars");
            var conditions = null;
            var limit = me.data("limit");
            var height = me.data("height");
            var pagination = me.data("pagination") == "True";
            var showcurrentrow = me.data("showcurrentrow") == "True";
            var mergecolumns = me.data("mergecolumns");
            var emptyImageKey = me.data("emptyimagekey");
            var pageposition = me.data("pageposition") || "bottom";
            var detailTemplate = me.data("detailtemplate");
            var sidePagination = me.data("side-pagination") || "server";
            var detailFormatter = detailTemplate != null ? (me.attr("id") + "_detailFormatter") : "";
            var showfooter = me.data("showfooter") == "True";

            var pagelistStr = me.data("pagelist");
            var pageListArray = new Array();
            if (!$.isNullOrEmpty(pagelistStr)) {
                if (pagelistStr.toString().indexOf(",") > 0) {
                    pageListArray = pagelistStr.split(",");
                } else {
                    pageListArray.push(pagelistStr);
                }
                pageListArray = pagelistStr.split(",");
            } else {
                pageListArray.push(20);
                pageListArray.push(50);
                pageListArray.push(100);
                pageListArray.push(1000);
                pageListArray.push("所有");
            }
            var options = {
                url: url,
                classes: "table " + (me.data("border") ? '' : 'table-no-bordered'),
                pagination: pagination,
                pageSize: limit,
                undefinedText: "",
                sidePagination: sidePagination,
                paginationHAlign: "left",
                paginationDetailHAlign: "right",
                pageList: pageListArray,
                paginationVAlign: pageposition,
                detailView: detailTemplate != null,
                detailFormatter: detailFormatter,
                cache: false,
                height: height + 40,
                showFooter: showfooter,
                smartDisplay: false,
                queryParams: function (params) {
                    if (pagination) {
                        params.page = params.limit == 0 ? 0 : (params.offset / params.limit);
                    } else {
                        params.page = 0;
                    }
                    var $form = toolbar.find("form");
                    var array = $form.serializeArray();
                    var paramList = [];
                    for (var i = 0; i < array.length; i++) {
                        var param = {};
                        var name = array[i].name;
                        if (!name.startWith("chk_") && name != "Temp") {
                            var suffixIndex = name.indexOf("|");
                            if (suffixIndex > 0) {
                                var suffix = name.substring(suffixIndex + 1);
                                var connectorIndex = suffix.indexOf("-");
                                param.Operator = suffix.substring(0, connectorIndex);
                                param.DataType = suffix.substring(connectorIndex + 1);
                                name = name.substring(0, suffixIndex);
                            }
                            param.Name = name;
                            param.Value = array[i].value;
                            paramList.push(param);
                        }
                    }
                    conditions = paramList;
                    if (conditions)
                        params.conditions = JSON.stringify(conditions);
                    return params;
                },
                responseHandler: function (res) {
                    res.rows = res.data;
                    delete res.data;
                    me.trigger("onResponseHandler", [res])
                    return res;
                },
                formatNoMatches: function () {
                    emptyImageKey = emptyImageKey || "data";
                    return "<img class='empty-view' src='/Assets/Images/Empty/" + emptyImageKey + ".png'/>";
                },
                rowStyle: function (row, index) {
                    if (rowstylefunc && window[rowstylefunc]) {
                        return window[rowstylefunc].call(this, row, index);
                    }
                    return {};
                },
                footerStyle: function (column) {
                    if (footerstylefunc && window[footerstylefunc]) {
                        return window[footerstylefunc].call(this, column);
                    }
                    return {};
                },
            }
            if (data && data.length > 0) {
                $.extend(options, {
                    data: JSON.parse(data.replace(/\\\"/g, "\"").replace(/\\\'/g, "\""))
                })
            }
            var onCheckFunc = me.data("oncheckfunc");
            if (onCheckFunc && window[onCheckFunc]) {
                options.onCheck = window[onCheckFunc];
            }
            var onUnCheckFunc = me.data("onuncheckfunc");
            if (onUnCheckFunc && window[onUnCheckFunc]) {
                options.onUncheck = window[onUnCheckFunc];
            }
            var onCheckAllFunc = me.data("oncheckallfunc");
            if (onCheckAllFunc && window[onCheckAllFunc]) {
                options.onCheckAll = window[onCheckAllFunc];
            }
            var onUnCheckAllFunc = me.data("onuncheckallfunc");
            if (onUnCheckAllFunc && window[onUnCheckAllFunc]) {
                options.onUncheckAll = window[onUnCheckAllFunc];
            }
            var onExpandRowFunc = me.data("onexpandrowfunc");
            if (onExpandRowFunc && window[onExpandRowFunc]) {
                options.onExpandRow = window[onExpandRowFunc];
            }
            var onCheckButtonState = me.data("oncheckbuttonstate");
            var onRowDeleted = me.data("onrowdeleted");
            var onButtonChecked = me.data("onbuttonchecked");


            window[me.attr("id") + "_buttonFormatter"] = function (value, row, index) {
                row._index = index;
                var template = this.searchFormatter;
                if (template) {
                    template = $.template(template, row);
                    var buttons = $(template);
                    var func = function (button, row) {
                        var flag = 0;
                        if (onCheckButtonState) {
                            flag = window[onCheckButtonState](button, button.attr("event"), row);
                        }
                        else {
                            flag = me.triggerHandler("onCheckButtonState", [button, button.attr("event"), row])
                        }
                        if (flag == -1) {
                            button.attr('disabled', 'disabled')
                        }
                        else if (flag == -2) {
                            button.addClass('hide')
                        }
                    }
                    for (var i = 0; i < buttons.length; i++) {
                        var button = $(buttons[i]);
                        func(button, row);
                        if (button.hasClass("dropdown")) {
                            var childButtons = button.find("a");
                            for (var j = 0; j < childButtons.length; j++) {
                                func($(childButtons[j]), row);
                            }
                        }
                    }
                    return $("<p>").append(buttons).html()
                }
                return "";
            }
            if (detailTemplate != null) {
                window[detailFormatter] = function (value, row, index) {
                    var result = $.template(detailTemplate, row);
                    return result;
                }
            }
            window[me.attr("id") + "_booleanFormatter"] = function (value, row, index) {
                return "<input type='checkbox' " + ((value == true) ? "checked" : "") + " onclick='return false'/>"
            }
            window[me.attr("id") + "_paramsFormatter"] = function (value, row, index) {
                if (!$.isNullOrEmpty(value)) {
                    var paramNameArr = [];
                    if (this.params) {
                        if (typeof this.params == "string") {
                            this.params = JSON.parse(this.params.replace(/\'/g, "\""));
                        }
                        for (var i = 0; i < this.params.length; i++) {
                            if ((',' + value + ',').indexOf(',' + this.params[i]["ID"] + ',') >= 0) {
                                paramNameArr.push(this.params[i]["Name"]);
                            }
                        }
                    }
                    if (paramNameArr && paramNameArr.length > 0) {
                        return paramNameArr.join('，');
                    } else {
                        if (typeof value == "number") {
                            value += '';
                        }
                        return value.replace("Custom-", "").replace("Custom", "");
                    }
                }
                return "";
            }

            window[me.attr("id") + "_rowTemplateFormatter"] = function (value, row, index) {
                row._index = index;
                var template = this.searchFormatter;
                if (template) {
                    template = $.template(template, row);
                    return template;
                }
                return "";
            }
            window[me.attr("id") + "_rownoFormatter"] = function (value, row, index) {
                return index + 1;
            }
            window[me.attr("id") + "_textEditorFormatter"] = function (value, row, index) {
                var target = $(this);
                return "<input type='text' data-name='" + target[0].field + "' data-index='" + index + "' class='form-control bt-table-editor-text' value='" + ($.isNullOrEmpty(value) ? "" : value) + "'/>";
            }
            window[me.attr("id") + "_numberEditorFormatter"] = function (value, row, index) {
                var target = $(this);
                return '<input type="text" data-name="' + target[0].field + '" data-index="' + index + '" class="form-control bt-table-editor-text" onkeyup="$.numberValidate(this)" onblur="$.numberValidate(this)"  value="' + ($.isNullOrEmpty(value) ? '' : value) + '"   />';
            }
            window[me.attr("id") + "_intnumberEditorFormatter"] = function (value, row, index) {
                var target = $(this);
                return '<input type="text" data-name="' + target[0].field + '" data-index="' + index + '" class="form-control bt-table-editor-text" onkeyup="$.intnumberValidate(this)" onblur="$.intnumberValidate(this)"  value="' + ($.isNullOrEmpty(value) ? '' : value) + '"   />';
            }
            window[me.attr("id") + "_textareaEditorFormatter"] = function (value, row, index) {
                var target = $(this);
                return "<textarea data-name='" + target[0].field + "' data-index='" + index + "' class='form-control bt-table-editor-textarea'>" + ($.isNullOrEmpty(value) ? "" : value) + "</textarea>";
            }
            window[me.attr("id") + "_checkEditorFormatter"] = function (value, row, index) {
                var target = $(this);
                return "<input type='checkbox' data-name='" + target[0].field + "' data-index='" + index + "' class='bt-table-editor-check' " + (value ? "checked" : "") + "/>";
            }
            window[me.attr("id") + "_chooseEditorFormatter"] = function (value, row, index) {
                var target = $(this);
                return "<div class='bt-table-editor-choose'>"
                    + "<input type='text' data-name='" + target[0].field + "' data-index='" + index + "' class='form-control' value='" + ($.isNullOrEmpty(value) ? "" : value) + "' readonly='true' data-url='" + this.searchformatter + "'/>"
                    + "<i class='fa fa-search'></i>"
                    + "</div>";
            }
            window[me.attr("id") + "_dateEditorFormatter"] = function (value, row, index) {
                var target = $(this);
                var date = "<div class='bt-table-editor-date date'>"
                    + "<input type='text' data-name='" + target[0].field + "' data-index='" + index + "' class='form-control' value='" + ($.isNullOrEmpty(value) ? "" : $.formatDate(value)) + "' readonly='true'/>"
                    + "<span class='input-group-addon'><i class='fa fa-calendar'></i></span>"
                    + "</div>";
                return date;
            }
            window[me.attr("id") + "_selectEditorFormatter"] = function (value, row, index) {
                var target = $(this);
                var text = "<div class='bt-table-editor-select'>"
                    + "<select data-name='" + target[0].field + "' data-index='" + index + "' class='form-control' value='" + ($.isNullOrEmpty(value) ? "" : value) + "'>";
                if (this.params) {
                    if (typeof this.params == "string") {
                        this.params = JSON.parse(this.params.replace(/\'/g, "\""));
                    }
                    text += "<option value =''>-请选择-</option>";
                    for (var i = 0; i < this.params.length; i++) {
                        text += "<option value =\"" + this.params[i].ID + "\" " + (value == this.params[i].ID ? "selected" : "") + " >" + this.params[i].Name + "</option>";
                    }
                }
                text += "</select></div>";
                return text;
            }
            //this.onReorderRowsDragF = me.data("onReorderRowsDrag");
            //this.onReorderRowsDropF = me.data("onReorderRowsDrop");
            //this.onReorderRowF = me.data("onReorderRow");
            //options.onReorderRow = function (newData) {
            //    
            //};
            //options.onReorderRowsDrop = function (table, row) {
            //    
            //};
            var table = me.bootstrapTable(options);
            if (tempUrl) {
                me.bootstrapTable("refreshOptions", { url: tempUrl });
            }

            table.on('click-row.bs.table', function (e, row, element) {
                if (showcurrentrow) {
                    table.find('.success').removeClass('success');
                    $(element).addClass('success');
                }
                var onRowClickFunc = me.data("onrowclickfunc");
                if (onRowClickFunc) {
                    var func = window[onRowClickFunc];
                    if (func)
                        func(row);
                }
            });

            table.on('load-success.bs.table', function (event, data) {
                if (mergecolumns) {
                    var bootData = table.bootstrapTable('getData', true);
                    if (bootData.length == 0) {
                        return;
                    }
                    var mergecolumnsArray = mergecolumns.split(',');
                    if (mergecolumnsArray.length > 0) {
                        var arrMerge = new Array(bootData.length);
                        for (var m = 0; m < mergecolumnsArray.length; m++) {
                            var fieldName = mergecolumnsArray[m];
                            var numArr = [];
                            var value = bootData[0][fieldName];
                            var num = 0;
                            var isMerge = true;
                            for (var i = 0; i < bootData.length; i++) {
                                if (m > 0) {
                                    isMerge = arrMerge[i];
                                }
                                if (isMerge && value == bootData[i][fieldName]) {
                                    num++;
                                    arrMerge[i] = true;
                                    if ((i + 1) == bootData.length) {
                                        numArr.push(num);
                                    }
                                }
                                else {
                                    arrMerge[i] = false;
                                    numArr.push(num);
                                    value = bootData[i][fieldName];
                                    num = 1;
                                    continue;
                                }
                            }
                            var merIndex = 0;
                            for (var i = 0; i < numArr.length; i++) {
                                table.bootstrapTable('mergeCells', { index: merIndex, field: fieldName, colspan: 1, rowspan: numArr[i] })
                                merIndex += numArr[i];
                            }
                        }
                    }
                }
                if (data.success && data.rows) {
                    for (var i = 0; i < data.rows.length; i++) {
                        if (data.rows[i]._expand) {
                            table.bootstrapTable('expandRow', i);
                        }
                    }
                }
                var onLoadSuccessFunc = me.data("onloadsuccessfunc");
                if (onLoadSuccessFunc && window[onLoadSuccessFunc]) {
                    window[onLoadSuccessFunc].call(this, event, data);
                }
            });

            table.on('post-body.bs.table', function (data) {
                var input = $(this).parents(".bt-table-ct").find(".stringFilter");
                var filterValue = input.val();
                if (!$.isNullOrEmpty(filterValue)) {
                    filterValue = filterValue.replaceAll("<", "").replaceAll(">", "");
                    var columns = $(this).data('bootstrap.table').columns;
                    var filterIndexs = [];
                    var filterColumns = input.attr("name").split(',');
                    if (filterColumns) {
                        for (var i = 0; i < filterColumns.length; i++) {
                            var item = filterColumns[i];
                            var index = columns.getIndexWithKey({ field: item }, "field");
                            if (index != -1) {
                                filterIndexs.push(index);
                            }
                        }
                    }
                    var domTable = $(this)[0];
                    if (filterIndexs) {
                        for (var i = 1; i < domTable.rows.length; i++) {
                            var row = domTable.rows[i];
                            if (!$(row).hasClass("no-records-found")) {
                                for (var j = 0; j < filterIndexs.length; j++) {
                                    var col = filterIndexs[j];
                                    var original = row.cells[col].innerHTML;
                                    if (original && original.indexOf("<") < 0) {
                                        if (filterValue.indexOf(' ') != -1) {
                                            var filterValueArray = filterValue.split(' ');
                                            for (var k = 0; k < filterValueArray.length; k++) {
                                                var tempValue = filterValueArray[k];
                                                if (!$.isNullOrEmpty(tempValue)) {
                                                    var reg = new RegExp(tempValue, 'ig');
                                                    var displayColor = '<span style="background-color: #FFFF00;font-weight: bold;">' + tempValue + '</span>';
                                                    original = original.replace(reg, displayColor);
                                                }
                                            }
                                            domTable.rows[i].cells[col].innerHTML = original;
                                        } else {
                                            var reg = new RegExp(filterValue, 'ig');
                                            var displayColor = '<span style="background-color: #FFFF00;font-weight: bold;">' + filterValue + '</span>';
                                            original = original.replace(reg, displayColor);
                                            domTable.rows[i].cells[col].innerHTML = original;
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            })
            table.on('pre-body.bs.table', function (event, data) {
                var onPreBody = me.data("onprebody");
                if (onPreBody && window[onPreBody]) {
                    window[onPreBody].call(this, event, data);
                }
            });
            if (toolbar && toolbar.length > 0) {
                toolbar.find("form")
                    .off('success.form.bv')
                    .on('success.form.bv', function (e) {
                        e.preventDefault();
                        var $form = $(e.target);
                        if (sidePagination == "server") {
                            table.bootstrapTable('refresh', {
                                pageNumber: 1
                            });
                        }
                        else {
                            var conditions = $form.serializeArray();
                            var name = conditions[0].name;
                            var value = conditions[0].value;
                            var data = me.bootstrapTable("getData");
                            var results = [];
                            data.forEach(function (item) {
                                var flag = true;
                                if (value) {
                                    flag = item[name].indexOf(value) >= 0;
                                }
                                if (flag) {
                                    results.push(item);
                                }
                            })
                            me.bootstrapTable("load", results);
                        }
                        var bv = $form.data('bootstrapValidator');
                        bv.disableSubmitButtons(false);
                    });

                toolbar.find(".refresh").click(function () {
                    if (toolbar.find("form").length > 0) {
                        toolbar.find("form").find("button[type=submit]").last().click();
                    }
                    else {
                        table.bootstrapTable('refresh');
                    }
                })

                toolbar.find(".export").click(function (e) {
                    e.preventDefault();
                    var text = $(this).data("export-title");
                    if (!window.txtArea1) {
                        var element = document.createElement("iframe");
                        element.id = "txtArea1";
                        element.className = "hidden";
                        document.body.appendChild(element);
                    }
                    $.loadScript("/plugins/table2excel/jquery.table2excel.js?v=1", function () {
                        table.table2excel({
                            name: "Excel Document Name",
                            filename: text + "-" + new Date().format("yyyyMMddhhmmss") + ".xls",
                            fileext: ".xls",
                            exclude_img: true,
                            exclude_links: true,
                            exclude_inputs: true
                        });
                    })
                })
                toolbar.find(".btn-shrink").click(function (e) {
                    if (!$(this).hasClass("btn-shrink-down")) {
                        $(this).addClass("btn-shrink-down")
                        $(this).parents(".bt-condition-form-ct").find(".bt-form-shrink").removeClass("hidden");
                        toolbar.trigger("condition.shrink", [false]);
                    }
                    else {
                        $(this).removeClass("btn-shrink-down")
                        $(this).parents(".bt-condition-form-ct").find(".bt-form-shrink").addClass("hidden");
                        toolbar.trigger("condition.shrink", [true]);
                    }
                })
            }
            table.on("click", ".add-row", function (e) {
                e.preventDefault();
                onRowAdded = $(this).data("onrowadded");
                if ($(this).attr("disabled") != "disabled") {
                    var allData = table.bootstrapTable('getData');
                    var index = $(this).data("index");
                    var oldRow = allData[index];
                    var row = {};
                    var func = window[onRowAdded];
                    if (func)
                        row = func(row, allData, oldRow);
                    else {
                        row = table.triggerHandler("onDetailRowAdded", [row, allData, oldRow]) || row;
                    }
                    allData.insert(index + 1, row);
                    table.bootstrapTable('load', allData);
                }
            });
            table.on("click", ".delete-row", function (e) {
                e.preventDefault();
                onRowDeleted = $(this).data("onrowdeleted");
                var afterDeleted = $(this).data("afterdeleted");
                if ($(this).attr("disabled") != "disabled") {
                    var allData = table.bootstrapTable('getData');
                    var index = $(this).data("index");
                    for (var i = 0; i < allData.length; i++) {
                        if (allData[i]._index == index) {
                            var row = allData[i];
                            allData.splice(i, 1);
                            var func = window[onRowDeleted];
                            if (func)
                                func(row, allData.length);
                            else {
                                row = table.triggerHandler("onDetailRowDeleted", [row, allData.length]);
                            }
                            break;
                        }
                    }
                    table.bootstrapTable('load', allData);
                    var afterfunc = window[afterDeleted];
                    if (afterfunc)
                        afterfunc();
                }
            });
            table.on("click", ".move-up", function (e) {
                e.preventDefault();
                var allData = table.bootstrapTable('getData');
                var index = $(this).data("index");
                for (var i = 0; i < allData.length; i++) {
                    if (allData[i]._index == index) {
                        if (i > 0) {
                            var tmp = allData[i];
                            allData[i] = allData[i - 1];
                            allData[i - 1] = tmp;
                            break;
                        }
                    }
                }
                table.bootstrapTable('load', allData);
            });
            table.on("click", ".move-down", function (e) {
                e.preventDefault();
                var allData = table.bootstrapTable('getData');
                var index = $(this).data("index");
                for (var i = 0; i < allData.length; i++) {
                    if (allData[i]._index == index) {
                        if (i < allData.length - 1) {
                            var tmp = allData[i];
                            allData[i] = allData[i + 1];
                            allData[i + 1] = tmp;
                            break;
                        }
                    }
                }
                table.bootstrapTable('load', allData);
            });
            table.on("click", ".link", function (e) {
                e.preventDefault();
                if (e.target.nodeName != "A") {
                    var href = $(this).attr("href");
                    $.loadUrlOrRefresh(href);
                }
            });

            table.on("click", ".link-blank", function (e) {
                e.preventDefault();
                if (e.target.nodeName != "A") {
                    var href = $(this).attr("href");
                    $.openUrl(href, $(this).attr("target"));
                }
            });

            table.on("click", ".btn-check", function (e) {
                e.preventDefault();
                var me = $(this);
                var checked = me.data("checked");
                if (checked) {
                    me.data("checked", false);
                    me.removeClass(me.data("checked-cls"));
                }
                else {
                    me.siblings(".btn-check").each(function () {
                        $(this).data("checked", false);
                        $(this).removeClass($(this).data("checked-cls"));
                    });
                    me.data("checked", true);
                    me.addClass(me.data("checked-cls"));
                }
                if (onButtonChecked != null) {
                    var func = window[onButtonChecked];
                    if (func)
                        func(me, !checked, table);
                }
            });

            table.on("click", ".bt-img-preview", function (e) {
                e.preventDefault();
                var data = $(this).data("value");
                $.showPrev(data);
            });
            table.on("input propertychange", ".bt-table-editor-text", function (e) {
                var me = $(e.target);
                var index = me.data("index");
                var name = me.data("name");
                var allData = $(e.delegateTarget).bootstrapTable('getData');
                allData[index][name] = me.val();
            })
            table.on("input propertychange", ".bt-table-editor-textarea", function (e) {
                var me = $(e.target);
                var index = me.data("index");
                var name = me.data("name");
                var allData = $(e.delegateTarget).bootstrapTable('getData');
                allData[index][name] = me.val();
            })
            table.on("change", ".bt-table-editor-check", function (e) {
                var me = $(e.target);
                var index = me.data("index");
                var name = me.data("name");
                var allData = $(e.delegateTarget).bootstrapTable('getData');
                allData[index][name] = me.prop("checked");
            })
            table.on("change", ".bt-table-editor-select", function (e) {
                var me = $(e.target);
                var index = me.data("index");
                var name = me.data("name");
                var allData = $(e.delegateTarget).bootstrapTable('getData');
                allData[index][name] = me.val();
            })
            table.on("click", ".bt-table-editor-choose .fa", function (e) {
                e.preventDefault();
                var table = $(e.delegateTarget);
                var input = $(this).prev();
                var index = input.data("index");
                var name = input.data("name");
                var allData = table.bootstrapTable('getData');
                var url = input.data("url");
                var row = allData[index];
                url = table.triggerHandler("beforeChoose", [url, row]) || url;
                url = $.template(url, row);
                $.formLinkCore.call(input, {
                    url: url,
                    callback: function (index, lay, buttonIndex) {
                        var frame = lay.find("iframe")[0];
                        var windowCallback = frame.contentWindow.window["windowCallback"];
                        var data = null;
                        if (windowCallback)
                            data = windowCallback.call(this, index, lay, buttonIndex);
                        if (data) {
                            var ids = [];
                            var names = [];
                            for (var i = 0; i < data.length; i++) {
                                ids.push(data[i]["value"]);
                                names.push(data[i]["name"]);
                            }
                            input.val(names.join(";"));
                            row = table.triggerHandler("onChooseCallback", [row, data, ids.join(";"), names.join(";"), name]);
                            table.bootstrapTable("load", allData);
                            return true;
                        }
                    }
                })
            })
            table.on("click", ".bt-table-editor-date .fa", function (e) {
                e.preventDefault();
                var table = $(e.delegateTarget);
                var input = $(this).parent().prev();
                var index = input.data("index");
                var allData = table.bootstrapTable('getData');
                var name = input.data("name");
                var row = allData[index];
                var picker = input.data("datetimepicker");
                if (!picker) {
                    $.loadAssets({
                        scripts: ["/Plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js",
                            "/Plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"],
                        css: ["/Plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css"]
                    }, function () {

                        input.datetimepicker({
                            format: "yyyy-mm-dd",
                            language: "zh-CN",
                            minView: 2,
                            startView: 2,
                            autoclose: true,
                            todayHighlight: true,
                            zIndex: 198910160,
                        }).on("changeDate", function (ev) {  //值改变事件
                            var value = input.val();
                            row[name] = value;
                        });
                        input.data("datetimepicker").show();
                    })
                }
                else {
                    picker.show();
                }
            })
        }
    })
    window.dateFormatter = function (value) {
        return $.formatDate(value);
    }
    window.dateMinuteFormatter = function (value) {
        return $.formatDateMinute(value);
    }

    window.dateDateTimeFormatter = function (value) {
        return $.formatDateTime(value);
    }

    window.moneyFormatter = function (value) {
        return $.formatMoney(value);
    }

    window.numberFormatter = function (value) {
        return $.formatNumber(value, this.decimals);
    }

    window.percentFormatter = function (value) {
        return $.formatPercent(value);
    }
    window.boolFormatter = function (value) {
        if (value == true) {
            return "是";
        } else {
            return "否";
        }
    }
    window.remarksFormatter = function (value, row, index) {
        if (value) {
            var result = "<div class='remarks-line'>" + value + "</div>";
            if (value.length > 3) {
                result += "<a href='#' data-title='" + this.title + "' class='c-blue show-note'>查看全部</a>"
            }
            return result;
        }
        return "";
    }
    window.attachFormatter = function (value) {
        if (!$.isNullOrEmpty(value)) {
            var array = value.split(";");
            return "<img class='bt-img-preview' src='" + array[0] + "' style='max-width:100px;' width='60px' height='60px' data-value='" + value + "'>"
        } else {
            return "";
        }
    }
    window.mapFormatter = function (value, row) {
        if (!$.isNullOrEmpty(value) && row.Lng && row.Lat) {
            return "<a class='window-link' href='/Home/MapPreview?lat=" + row.Lat + "&lng=" + row.Lng + "&address=" + value + "'>" + value + "</a>";
        } else {
            return value;
        }
    }
    window.attachFormatterFile = function (value) {
        var html = "<div class='attach-block'>";
        try {
            if (!$.isNullOrEmpty(value)) {
                var imgExts = [".jpg", ".png", ".gif", ".jpeg"];
                var reg = /'/g;
                value = value.replace(reg, "\"");
                var data = JSON.parse(value);
                var template = "";

                var imgCodes = [];
                //for (var i = 0; i < data.length; i++) {
                //    var item = data[i];
                //    if (item.AttExt != undefined
                //        && item.AttExt != null
                //        && item.AttExt != ""
                //        && imgExts.contains(item.AttExt)) {
                //        imgCodes.push(item.AttCode);
                //    }
                //}
                var draw = function (data, imgData) {
                    for (var i = 0; i < data.length; i++) {
                        var item = data[i];
                        if (item.AttExt != undefined && item.AttExt != null && item.AttExt != "") {
                            var attExt = item.AttExt.substring(1);
                            if (imgData && imgExts.contains(item.AttExt)) {
                                var imgItem = imgData.find(function (obj) {
                                    return obj.ID == item.AttCode;
                                });
                                template = '<a target="_blank" class="attach-block-img" data-src="' + imgItem.Name + '" ><img style="width:16px;margin-right:5px;" src="/Assets/Images/Kn/icon/' + attExt + '.png">{{ d.AttName}}</a></br>';
                            } else {
                                template = '<a target="_blank" class="file-preview-link" data-code="{{ d.AttCode }}" data-ext="' + attExt + '" ><img style="width:16px;margin-right:5px;" src="/Assets/Images/Kn/icon/' + attExt + '.png">{{ d.AttName}}</a></br>';
                            }
                        }
                        else {
                            template = '<a target="_blank" class="file-preview-link" data-code="{{ d.AttCode }}" data-ext="' + attExt + '" >{{ d.AttName}}</a></br>';
                        }
                        html = html + $.template(template, data[i]);
                    }
                }
                if (imgCodes && imgCodes.length > 0) {
                    var codes = imgCodes.join(",");
                    $.ajax({
                        type: 'post',
                        dataType: "json",
                        url: "/CommonAttach/GetImgAtts",
                        data: { codes: codes },
                        async: false,
                        success: function (result) {
                            if (result.success) {
                                var imgData = result.data;
                                draw(data, imgData);
                            }
                        }
                    });
                } else {
                    draw(data);
                }
            }
        } catch (e) {
        }
        html = html + "</div>";
        return html;
    }

    window.totalFooterFormat = function (data) {
        return '总计:';
    }

    window.sumFooterFormat = function (data) {
        var field = this.field;
        return data.map(function (row) {
            return + row[field];
        }).reduce(function (sum, i) {
            return sum + i
        }, 0);
    }

    $(document).on("click", ".attach-block-img", function (e) {
        e.preventDefault();
        var me = $(this);
        var value = me.data("src");
        var array = [];
        var idx = 0;
        var item = me.parents(".attach-block");
        item.find(".attach-block-img").each(function (index) {
            var img = $(this);
            var imgsrc = img.data("src");
            if (imgsrc == value) {
                idx = index;
            }
            array.push(imgsrc);
        })
        $.showPrev(array.join(";"), idx);
    });

    $(document).on("click", ".show-note", function (e) {
        e.preventDefault();
        $.showNote($(this).prev().html().replace("\r\n", "\n").replaceAll("\n", "<br/>"), $(this).data("title"));
    });


    $(".bt-table-ct .sort-refresh").click(function () {
        var me = $(this);
        var sort = me.data("sort");
        var order = "asc";
        var buttons = me.siblings().not(this);
        buttons.each(function () {
            var button = $(this);
            if (button.hasClass("sort-refresh")) {
                button.removeClass("btn-primary").children("i").remove();
            }
        });
        if (me.find("i").hasClass("glyphicon-arrow-up")) {
            me.find("i").removeClass("glyphicon-arrow-up").addClass("glyphicon-arrow-down");
            order = "desc";
        }
        else {
            if (me.children("i").length > 0) {
                me.find("i").removeClass("glyphicon-arrow-down").addClass("glyphicon-arrow-up");
                order = "asc";
            }
            else {
                me.append("<i class='glyphicon glyphicon-arrow-down'></i>");
                order = "desc";
            }
        }
        var bttable = me.parents(".bt-table-ct").find(".bt-table");
        var url = bttable.data('url')
        if (sort) {
            url = $.updateUrlParam(url, "sort", sort);
        }
        if (order) {
            url = $.updateUrlParam(url, "order", order);
        }
        bttable.bootstrapTable('refresh', {
            url: url
        });
        me.addClass("btn-primary");
    });

    $(".bt-table-ct .sort-dropdown").each(function () {
        var me = $(this);
        var tableCt = me.parents(".bt-table-ct");
        var table = tableCt.find(".bt-table");
        var dropdowns = me.siblings(".dropdown-menu").find("a");
        dropdowns.click(function (e) {
            e.preventDefault();
            var sort = $(this).data("sort");
            var order = "asc";
            var url = table.data('url')
            if (sort) {
                url = $.updateUrlParam(url, "sort", sort);
            }
            if (order) {
                url = $.updateUrlParam(url, "order", order);
            }
            table.bootstrapTable('refresh', {
                url: url
            });
            me.text($(this).text());
        })
    });


    $(".bt-table").each(function () {
        $(this).bttable();
    });

    var btGrid = function (grid) {
        var me = this;
        this.grid = grid;
        this.conditions = null;
        this.url = grid.data("url");
        this.pagination = grid.data("pagination");
        this.limit = grid.data("limit");
        this.currentPage = 0;
        this.template = grid.data("template");
        grid.removeAttr("data-template");
        this.bodyView = grid.find(".bt-grid-body");
        this.emptyView = grid.find(".bt-grid-empty");
        this.loadingView = grid.find(".bt-grid-loading");
        this.pagerView = grid.find(".bt-grid-pager");
        this.onDataResponsed = grid.data("ondatarespnsed");
        this.onCheckButtonState = grid.data("oncheckbuttonstate");
        this.emptyImageKey = grid.data("emptyimagekey");
        this.pageposition = grid.data("pageposition");
        this.toolbar = grid.prevAll(".bs-bars");
        if (this.toolbar && this.toolbar.length > 0) {
            this.toolbar.find("form")
                .off('success.form.bv')
                .on('success.form.bv', function (e) {
                    e.preventDefault();
                    var $form = $(e.target);
                    me.currentPage = 0;
                    me.refresh();
                    var bv = $form.data('bootstrapValidator');
                    bv.disableSubmitButtons(false);
                });

            this.toolbar.find(".refresh").click(function () {
                if (me.toolbar.find("form").length > 0) {
                    me.toolbar.find("form").find("button[type=submit]").click();
                }
                else {
                    me.refresh();
                }
            })
        }
        grid.on("click", ".link", function (e) {
            e.preventDefault();
            if (e.target.nodeName != "A") {
                var href = $(this).attr("href");
                $.loadUrlOrRefresh(href);
            }
            return true;
        });
        grid.on("click", ".link-blank", function (e) {
            e.preventDefault();
            if (e.target.nodeName != "A") {
                var href = $(this).attr("href");
                $.openUrl(href, $(this).attr("target"));
            }
            return true;
        });
        this.emptyImageKey = this.emptyImageKey || "data";
        this.emptyView.append("<img class='empty-view' src='/Assets/Images/Empty/" + this.emptyImageKey + ".png' />");
    }

    $.extend(btGrid.prototype, {
        loadData: function (page) {
            var me = this;
            var params = {};
            if (this.pagination) {
                params.page = page;
                params.limit = this.limit;
            } else {
                params.page = 0;
                params.limit = 10000;
            }
            params.conditions = JSON.stringify(me.getConditions());
            this.loadingView.show();
            this.emptyView.hide();
            this.bodyView.hide();
            this.pagerView.hide();
            $.get(this.url, params, function (result) {
                me.loadingView.hide();
                if (result.success && result.data && result.data.length) {
                    me.currentPage = page;
                    var oFragment = document.createDocumentFragment();
                    for (var i = 0; i < result.data.length; i++) {
                        var item = result.data[i];
                        item._index = i;
                        var templateText = $.template(me.template, item);
                        var gridItem = document.createElement("div");
                        $(gridItem).addClass("bt-grid-item");
                        $(gridItem).append(templateText);
                        oFragment.appendChild(gridItem);

                        var buttons = $(gridItem).find("a.btn");
                        buttons.each(function () {
                            var button = $(this);
                            var flag = 0;
                            if (me.onCheckButtonState) {
                                flag = window[me.onCheckButtonState](button, button.attr("lay-event"), item);
                            }
                            else {
                                flag = me.grid.trigger("onCheckButtonState", button, button.attr("lay-event"), item) || true;
                            }
                            if (flag == -1) {
                                button.attr('disabled', 'disabled')
                            }
                            else if (flag == -2) {
                                button.addClass('hide')
                            }
                        });

                    }
                    me.bodyView.html("");
                    me.bodyView.append(oFragment);
                    me.bodyView.show();
                    if (me.onDataResponsed) {
                        window[me.onDataResponsed].call(me, result.data, result);
                    }
                    me.pagerView.html("");
                    if (result.total > 0) {
                        if (result.total > me.limit) {
                            var pageLeft = $(document.createElement("div"));
                            pageLeft.addClass("pull-left");

                            var pageUl = $(document.createElement("ul"));
                            pageUl.addClass("pagination");
                            pageUl.append("<li class='page-pre'><a href='#'>‹</a></li>");

                            var totalPages = Math.ceil(result.total / me.limit);
                            var from = 0;
                            var to = 0;
                            if (totalPages < 5) {
                                from = 0;
                                to = totalPages - 1;
                            }
                            else {
                                from = page - 2;
                                to = from + 4;
                                if (from < 0) {
                                    from = 0;
                                    to = 4;
                                }
                                if (to >= totalPages) {
                                    to = totalPages - 1;
                                    from = to - 4;
                                }
                            }
                            if (totalPages >= 6) {
                                if (page >= 2) {
                                    pageUl.append("<li class='page-number'><a href='#'>1</a></li>");
                                    from++;
                                }
                                if (page >= 3) {
                                    if (page == 3 || totalPages == 6 || totalPages == 7) {
                                        from--;
                                    } else {
                                        pageUl.append("<li class='page-number'><a href='#'>...</a></li>");
                                    }
                                    to--;
                                }
                            }
                            if (totalPages == 6) {
                                if (page >= (totalPages - 3)) {
                                    to++;
                                }
                            } else if (totalPages >= 7) {
                                if (page >= (totalPages - 3)) {
                                    from--;
                                }
                                if (totalPages == 7 || page >= (totalPages - 4)) {
                                    to++;
                                }
                            }
                            for (i = from; i <= to; i++) {
                                pageUl.append("<li class='page-number " + (i == page ? 'active' : '') + "' ><a href='#'>" + (i + 1) + "</a></li>");
                            }
                            if (totalPages >= 8) {
                                if (page <= (totalPages - 5)) {
                                    pageUl.append("<li class='page-number'><a href='#'>...</a></li>");
                                }
                            }
                            if (totalPages >= 6) {
                                if (page <= (totalPages - 4)) {
                                    pageUl.append("<li class='page-number'><a href='#'>" + totalPages + "</a></li>");
                                }
                            }
                            pageUl.append("<li class='page-next'><a href='#'>›</a></li>");
                            pageUl.find(".page-number a").click(function (e) {
                                e.preventDefault();
                                var text = $(this).html();
                                if (text != "...") {
                                    me.loadData(parseInt($(this).html()) - 1);
                                }
                            })
                            pageUl.find(".page-pre a").click(function (e) {
                                e.preventDefault();
                                me.loadData(me.currentPage <= 0 ? 0 : (me.currentPage - 1));
                            })
                            pageUl.find(".page-next a").click(function (e) {
                                e.preventDefault();
                                me.loadData(me.currentPage >= (pages - 1) ? (pages - 1) : (me.currentPage + 1));
                            })
                            pageLeft.append(pageUl);

                            me.pagerView.append(pageLeft);
                        }
                        var pageRight = $(document.createElement("div"));
                        pageRight.addClass("pull-right");
                        pageRight.append("<span class='page-info'>显示第 " + (page * me.limit + 1) + " 到第 " + (page * me.limit + result.data.length) + " 条记录，总共 " + result.total + " 条记录</span>")
                        me.pagerView.append(pageRight);
                        var pageClear = $(document.createElement("div"));
                        pageClear.addClass("clear");
                        me.pagerView.append(pageClear);
                    }
                    me.pagerView.show();
                }
                else {
                    me.emptyView.show();
                }


            }).error(function (result, errorStatus, errorText) {
                me.loadingView.hide();
            });
        },
        refresh: function (url) {
            if (url)
                this.url = url;
            this.loadData(this.currentPage);
        },
        getConditions: function () {
            var $form = this.toolbar.find("form");
            var array = $form.serializeArray();
            var params = [];
            for (var i = 0; i < array.length; i++) {
                var param = {};
                var name = array[i].name;
                var suffixIndex = name.indexOf("|");
                if (suffixIndex > 0) {
                    var suffix = name.substring(suffixIndex + 1);
                    var connectorIndex = suffix.indexOf("-");
                    param.Operator = suffix.substring(0, connectorIndex);
                    param.DataType = suffix.substring(connectorIndex + 1);
                    name = name.substring(0, suffixIndex);
                }
                param.Name = name;
                param.Value = array[i].value;
                params.push(param);
            }
            return params;
        }
    })

    $.fn.extend({
        btgrid: function () {
            var grid = new btGrid($(this));
            grid.refresh();
            $(this).data("grid", grid);
        }
    });

    $(".bt-grid-ct .sort-refresh").click(function () {
        var me = $(this);
        var sort = me.data("sort");
        var order = "asc";
        var buttons = me.siblings().not(this);
        buttons.each(function () {
            var button = $(this);
            if (button.hasClass("sort-refresh")) {
                button.removeClass("btn-primary").children("i").remove();
            }
        });

        if (me.find("i").hasClass("glyphicon-arrow-up")) {
            me.find("i").removeClass("glyphicon-arrow-up").addClass("glyphicon-arrow-down");
            order = "desc";
        }
        else {
            if (me.children("i").length > 0) {
                me.find("i").removeClass("glyphicon-arrow-down").addClass("glyphicon-arrow-up");
                order = "asc";
            }
            else {
                me.append("<i class='glyphicon glyphicon-arrow-down'></i>");
                order = "desc";
            }
        }
        var grid = $(".bt-grid").data("grid");
        var url = grid.url;
        if (sort) {
            url = $.updateUrlParam(url, "sort", sort);
        }
        if (order) {
            url = $.updateUrlParam(url, "order", order);
        }
        grid.refresh(url);
        me.addClass("btn-primary");
    });

    $(".bt-grid").each(function () {
        $(this).btgrid();
    });

    var btAppGrid = function (grid) {
        var me = this;
        this.grid = grid;
        this.conditions = null;
        this.url = grid.data("url");
        this.pagination = grid.data("pagination");
        this.limit = grid.data("limit");
        this.currentPage = 0;
        this.template = grid.data("template");
        grid.removeAttr("data-template");
        this.bodyView = grid.find(".bt-app-grid-body");
        this.emptyView = grid.find(".bt-app-grid-empty");
        this.loadingView = grid.find(".bt-app-grid-loading");
        this.bottomView = grid.find(".bt-app-grid-bottom");
        //this.pagerView = grid.find(".bt-app-grid-pager");
        this.onDataResponsed = grid.data("ondatarespnsed");
        this.onCheckButtonState = grid.data("oncheckbuttonstate");
        this.emptyImageKey = grid.data("emptyimagekey");

        this.pageposition = grid.data("pageposition");

        var toolbar = grid.prevAll(".bs-bars");
        if (toolbar && toolbar.length > 0) {
            toolbar.find("form")
                .off('success.form.bv')
                .on('success.form.bv', function (e) {
                    e.preventDefault();
                    var $form = $(e.target);
                    var array = $form.serializeArray();

                    var params = [];
                    for (var i = 0; i < array.length; i++) {
                        var param = {};
                        var name = array[i].name;
                        var suffixIndex = name.indexOf("|");
                        if (suffixIndex > 0) {
                            var suffix = name.substring(suffixIndex + 1);
                            var connectorIndex = suffix.indexOf("-");
                            param.Operator = suffix.substring(0, connectorIndex);
                            param.DataType = suffix.substring(connectorIndex + 1);
                            name = name.substring(0, suffixIndex);
                        }
                        param.Name = name;
                        param.Value = array[i].value;
                        params.push(param);
                    }
                    me.conditions = params;
                    me.refresh();
                    var bv = $form.data('bootstrapValidator');
                    bv.disableSubmitButtons(false);
                });

            toolbar.find(".refresh").click(function () {
                if (toolbar.find("form").length > 0) {
                    toolbar.find("form").find("button[type=submit]").click();
                }
                else {
                    me.refresh();
                }
            })
        }
        grid.on("click", ".link", function (e) {
            e.preventDefault();
            if (e.target.nodeName != "A") {
                var href = $(this).attr("href");
                $.loadUrlOrRefresh(href);
            }
            return true;
        });
        grid.on("click", ".link-blank", function (e) {
            e.preventDefault();
            if (e.target.nodeName != "A") {
                var href = $(this).attr("href");
                $.openUrl(href, $(this).attr("target"));
            }
            return true;
        });
        this.emptyImageKey = this.emptyImageKey || "data";
        this.emptyView.append("<img class='empty-view' src='/Assets/Images/Empty/" + this.emptyImageKey + ".png' />");
    }

    $.extend(btAppGrid.prototype, {
        loadData: function (page) {
            var me = this;
            var params = {};
            if (this.pagination) {
                params.page = page;
                params.limit = this.limit;
            } else {
                params.page = 0;
                params.limit = 10000;
            }
            if (this.conditions)
                params.conditions = JSON.stringify(this.conditions);
            this.loadingView.show();
            this.emptyView.hide();
            //this.bodyView.hide();
            //this.pagerView.hide();
            $.get(this.url, params, function (result) {
                me.loadingView.hide();
                if (result.success && result.data && result.data.length) {
                    me.currentPage = page;
                    var oFragment = document.createDocumentFragment();
                    for (var i = 0; i < result.data.length; i++) {
                        var item = result.data[i];
                        item._index = i;
                        var templateText = $.template(me.template, item);
                        var gridItem = document.createElement("div");
                        $(gridItem).addClass("bt-grid-item");
                        $(gridItem).append(templateText);
                        oFragment.appendChild(gridItem);
                    }
                    me.bodyView.append(oFragment);
                    if (me.onDataResponsed) {
                        window[me.onDataResponsed].call(me, result.data, result);
                    }
                    if (result.hasnextpage != true) {
                        me.bottomView.show();
                    }
                }
                else {
                    //me.emptyView.show();
                }
            }).error(function (result, errorStatus, errorText) {
                me.loadingView.hide();
            });
        },
        refresh: function (url) {
            if (url)
                this.url = url;
            this.loadData(this.currentPage);
        }
    })

    $.fn.extend({
        btappgrid: function () {
            var grid = new btAppGrid($(this));
            grid.refresh();
            $(this).data("grid", grid);
        }
    });

    $(".bt-app-grid").each(function () {
        $(this).btappgrid();
    });

    $(window).scroll(function () {
        $(".bt-app-grid").each(function () {
            var me = $(this);
            var grid = me.data("grid");
            var height = me.height();
            var scrollTop = $(window).scrollTop();
            var windowHeight = $(window).height() + scrollTop;
            if (height <= windowHeight) {
                var loadingView = grid.loadingView;
                var bottomView = grid.bottomView;
                if (bottomView.css("display") == "none" && loadingView.css("display") == "none") {
                    var currentPage = grid.currentPage;
                    grid.loadData(currentPage + 1);
                }
            }
        });
    });

    $.fn.extend({
        titleSelect: function () {
            var me = $(this);
            $.loadAssets({
                scripts: ["/Assets/JS/DateSelect/dateSelect.js"],
                css: ["/Assets/Css/TitleSelect/titleSelect.css"]
            }, function () {
                me.dateSelect({
                    className: "title"
                });
            });
        }
    });

    $(".title-select").each(function () {
        $(this).titleSelect();
    });

    $(".form-group .input-group .input-group-btn").on("click", "a", function (e) {
        e.preventDefault();
        var me = $(this);
        var name = me.text();
        var id = me.data("key");
        var button = me.parents(".dropdown-menu").prev();
        button.text(name);
        button.append("<span class='caret'></span>")
        var input = me.parents(".input-group-btn").next();
        input.attr("name", id);
    })

    $("#safecode").on("click", function (e) {
        e.preventDefault();
        var me = $(this);
        me.attr('src', "/Public/VerifyCode?code=" + Math.random());
    })

    var zTreeCt = $(".ztree-ct");
    if (zTreeCt.length > 0) {
        var ZTree = function (tree) {
            var me = this;
            this.tree = tree;
            this.treeId = tree.attr("id");
            this.url = tree.data("url");
            this.controller = tree.data("controller");
            this.extra = tree.data("extra");
            this.showEdit = tree.data("showedit");
            if (this.extra) this.extra = $.parseJsonString(this.extra);
            this.autoload = tree.data("autoload") == "True";
            this.rootname = tree.data("rootname");
            this.showCheck = tree.data("showcheck");
            this.unionCheck = tree.data("unioncheck");
            this.chkBoxTypeY = tree.data("chkboxtypey");
            this.chkBoxTypeN = tree.data("chkboxtypen");
            this.currentKey = tree.data("currentkey");
            this.onClickFunc = tree.data("onclickfunc");
            this.onSelectedFunc = tree.data("onselectedfunc");
            this.onCheckFunc = tree.data("oncheckfunc");
            this.beforeCheckFunc = tree.data("beforecheckfunc");
            this.onRemoteLoadedFunc = tree.data("onremoteloadedfunc");
            this.onCheckButtonStateFunc = tree.data("oncheckbuttonstatefunc");
            this.onButtonClickFunc = tree.data("onbuttonclickfunc");
            this.onDefaultOpenFunc = tree.data("ondefaultopenfunc");
            this.onSetFontCssFunc = tree.data("onsetfontcssfunc");
            this.setting = {
                data: {
                    simpleData: {
                        enable: true,
                        rootPId: "-1"
                    }
                },
                callback: {
                    onClick: function (e, treeId, treeNode) {
                        if (!treeNode.open) {
                            me.treeObj.expandNode(treeNode);
                        }
                        else {
                            me.treeObj.expandNode(treeNode, false, false);
                        }
                        if (window[this.onClickFunc]) {
                            window[this.onClickFunc].call(this)
                        }
                    },
                    onSelected: window[this.onSelectedFunc],
                    onCheck: window[this.onCheckFunc],
                    beforeCheck: window[this.beforeCheckFunc]
                }
            };
            if (this.showCheck) {
                this.setting.check = {
                    enable: true,
                    chkboxType: { "Y": this.unionCheck ? this.chkBoxTypeY : "", "N": this.unionCheck ? this.chkBoxTypeN : "" }
                }
            }
            if (this.showEdit) {
                function addHoverDom(treeId, treeNode) {
                    var sObj = $("#" + treeNode.tId + "_span");
                    if (treeNode.editNameFlag || sObj.next(".button").length > 0)
                        return;
                    var str = $.template("<span class='button button-tool add' data-event='add' title='新增'></span>"
                        + "<span class='button button-tool edit' data-event='edit' title='编辑'></span>"
                        + "<span class='button button-tool remove' data-event='remove' title='删除'></span>", treeNode);

                    str += me.tree.triggerHandler("customToolButtons", [treeNode]) || "";

                    sObj.after(str);

                    sObj.parent().find(".button-tool").each(function () {
                        var hide = false;
                        var event = $(this).data("event");
                        var hide = checkButtonStateDefault(event, treeNode);
                        if (me.onCheckButtonStateFunc && window[me.onCheckButtonStateFunc]) {
                            var flag = window[me.onCheckButtonStateFunc]($(this), event, treeNode);
                            if (flag != undefined) {
                                hide = flag == -2;
                            }
                        }
                        if (hide) {
                            $(this).addClass('hide');
                        }
                        $(this).bind("click", function (e) {
                            e.stopPropagation();
                            var event = $(this).data("event");
                            var func = buttonClickDefault;
                            if (me.onButtonClickFunc && window[me.onButtonClickFunc]) {
                                func = window[me.onButtonClickFunc];
                            }
                            func($(this), event, treeNode);
                        })
                    })
                }

                function checkButtonStateDefault(event, node) {
                    switch (event) {
                        case "edit": return node.pId == "-1";
                        case "remove": return node.pId == "-1";
                    }
                }

                function buttonClickDefault(button, event, node) {
                    if (event == "add") {
                        var addUrl = "/" + me.controller + "/Add?parentID=" + node.id;
                        if (me.extra) {
                            for (var item in me.extra) {
                                addUrl = $.updateUrlParam(addUrl, item, me.extra[item]);
                            }
                        }
                        button.formLink({
                            url: addUrl,
                            callback: function (result) {
                                if (result.data) me.loadTree(result.data[me.keyField]);
                                return true;
                            }
                        })
                    } else if (event == "edit") {
                        var editUrl = "/" + me.controller + "/Edit?id=" + node.data.ID;
                        button.formLink({
                            url: editUrl,
                            callback: function (result) {
                                if (result.data) me.loadTree(result.data[me.keyField]);
                                return true;
                            }
                        })
                    } else if (event == "remove") {
                        var removeUrl = "/" + me.controller + "/DeletePost?id=" + node.data.ID;
                        button.ajaxLink({
                            url: removeUrl,
                            confirm: "您确定要删除当前节点吗?",
                            callback: function (result) {
                                me.loadTreeByDeleteNode(node);
                            }
                        })
                    }
                }

                function removeHoverDom(treeId, treeNode) {
                    $("#" + treeNode.tId + "_a").find(".button-tool").unbind().remove();
                };

                function setFontCss(treeId, treeNode) {
                    var fontCss = {};
                    if (me.onSetFontCssFunc && window[me.onSetFontCssFunc]) {
                        var tempfontCss = window[me.onSetFontCssFunc](me, treeNode);
                        if (tempfontCss) {
                            fontCss = tempfontCss;
                        }
                    }
                    return fontCss;
                };

                this.setting.view = {
                    addHoverDom: addHoverDom,
                    removeHoverDom: removeHoverDom,
                    selectedMulti: false,
                    fontCss: setFontCss
                }
            }
        }

        $.extend(ZTree.prototype, {
            loadTree: function (currentKey, url) {
                var me = this;
                url = url || this.url;
                if (me.extra) {
                    for (var item in me.extra) {
                        url = $.updateUrlParam(url, item, me.extra[item]);
                    }
                }
                this.currentKey = currentKey || this.currentKey;
                $.get2(url, "", function (result) {
                    if (result.success && result.data) {
                        me.url = url;
                        me.keyField = result.data.KeyField;
                        var data = result.data.List;
                        var idField = result.data.IDField;
                        var nameField = result.data.NameField;
                        var parentField = result.data.ParentField;
                        var keyField = result.data.KeyField;
                        var checkedField = result.data.CheckedField;

                        me.loadData(data, idField, nameField, parentField, keyField, checkedField);
                    }
                });
            },
            loadData: function (data, idField, nameField, parentField, keyField, checkedField) {
                var me = this;
                var datas = [];
                for (var i = 0; i < data.length; i++) {
                    var item = data[i];
                    var id = item[idField];
                    var pid = item[parentField];
                    var isRoot = true;
                    for (var j = 0; j < data.length; j++) {
                        var id2 = data[j][idField];
                        if (id2.length < id.length && pid != "-1") {
                            isRoot = false;
                            break;
                        }
                    }
                    var itemData = {
                        id: id,
                        pId: pid,
                        name: isRoot && me.rootname ? me.rootname : item[nameField],
                        key: item[keyField],
                        open: (this.onDefaultOpenFunc && window[this.onDefaultOpenFunc] ? window[this.onDefaultOpenFunc](item, data) : isRoot),
                        checked: item[checkedField],
                        data: item
                    }
                    datas.push(itemData);
                }
                if (me.showCheck && me.currentKey) {
                    var currentKeys = me.currentKey.toString().split(",");
                    for (var i = 0; i < datas.length; i++) {
                        datas[i].checked = currentKeys.contains(datas[i].key);
                    }
                }
                if (me.onRemoteLoadedFunc && window[me.onRemoteLoadedFunc]) {
                    window[me.onRemoteLoadedFunc].call(me, me.treeId, datas);
                }
                me.treeObj = $.fn.zTree.init(me.tree, me.setting, datas);
                if (!me.showCheck) {
                    if (me.currentKey) {
                        node = me.treeObj.getNodeByParam("key", me.currentKey.toString())
                        if (node) {
                            me.treeObj.selectNode(node, false, false);
                        }
                    }
                    else {
                        var nodes = me.treeObj.getNodes();
                        if (nodes.length > 0) {
                            me.treeObj.selectNode(nodes[0], false, false);
                            me.currentKey = nodes[0].key;
                            if (nodes[0].children && nodes[0].children.length > 0) {
                                for (var i = 0; i < nodes[0].children.length; i++) {
                                    var childrennodes = nodes[0].children[i];
                                    me.treeObj.expandNode(childrennodes, false, false);
                                }
                            }
                        }
                    }
                }
                scrollTo(0, 0);
            },
            loadTreeByDeleteNode: function (node) {
                var ntNode = node.getNextNode() || node.getPreNode() || node.getParentNode();
                this.loadTree(ntNode ? ntNode.key : null);
            },
            getSelectedNode: function () {
                var nodes = this.treeObj.getSelectedNodes();
                if (nodes.length > 0)
                    return nodes[0];
            },
            getSelectedNodes: function () {
                if (this.showCheck) {
                    var nodes = this.treeObj.getCheckedNodes();
                }
                else {
                    var nodes = this.treeObj.getSelectedNodes();
                }
                return nodes;
            },
            checkNode: function (key, value, checked) {
                if (this.treeObj) {
                    var me = this;
                    var nodes = this.treeObj.getNodes();
                    var checkNodes = function (nodes, key, value) {
                        for (var i = 0; i < nodes.length; i++) {
                            if (nodes[i][key] == value) {
                                me.treeObj.checkNode(nodes[i], checked);
                            }
                            if (nodes[i].children)
                                checkNodes(nodes[i].children, key, value);
                        }
                    }
                    checkNodes(nodes, key, value);
                }
            },
            clear: function () {
                this.treeObj = $.fn.zTree.init(this.tree, this.setting, []);
            },
            refresh: function () {
                var node = this.getSelectedNode();
                this.loadTree(node ? node.key : "0");
            }
        })

        zTreeCt.each(function () {
            var me = $(this);
            var tree = me.find(".ztree");
            var ztree = tree.data("ztree");
            if (!ztree) {
                ztree = new ZTree(tree);
                tree.data("ztree", ztree);
            }
            if (ztree.autoload) {
                ztree.loadTree();
            }
        })
    }

    $.fn.extend({
        starComment: function (value) {
            $(this).each(function () {
                var me = $(this);
                var val = value == undefined ? me.data("value") : 0;
                var valueDiv = me.find(".star-comment-value");
                var refresh = function (score) {
                    valueDiv.css("width", (score * 14) + "px");
                }
                var editable = me.data("editable");
                if (editable) {
                    me.bind(
                        {
                            mousemove: function (e) {
                                var v = Math.ceil(e.offsetX / 14);
                                refresh(v > 5 ? 5 : v);
                            },
                            mouseleave: function () {
                                refresh(val);
                            },
                            click: function (e) {
                                var v = Math.ceil(e.offsetX / 14);
                                v = v > 5 ? 5 : v;
                                var callback = me.data("callback");
                                if (callback)
                                    callback(v);
                                val = v;
                                refresh(v);
                            }
                        });
                }
                refresh(val);
            })
        }
    });

    $(".star-comment").starComment();

    $(".image-toggle").each(function () {
        var me = $(this);
        var a = $(this).find("a");
        var img = $(this).find("i");
        var on = a.data("on");
        var type = a.data("type");
        var recordid = a.data("recordid");
        var url = a.attr("href");
        var callback = a.data("callback")
        var onImage = a.data("image-on");
        var image = a.data("image");

        var updateUrl = function (on, type, recordid) {
            url = $.updateUrlParam(url, "on", on ? "false" : "true");
            if (type)
                url = $.updateUrlParam(url, "Type", type);
            if (recordid)
                url = $.updateUrlParam(url, "RecordID", recordid);
            a.attr("href", url);
        }
        updateUrl(on, type, recordid);
        var updateImg = function (on) {
            img.removeAttr("class");
            img.addClass("fa " + (on ? onImage : image));
        }
        var linkcallback = function () {
            var on = a.data("on");
            var type = a.data("type");
            var recordid = a.data("recordid");
            a.data("on", !on);
            updateUrl(!on, type, recordid);
            updateImg(!on);
            if (callback) {
                var func = window[callback];
                if (func) {
                    func(me, !on);
                }
            }
        }
        a.data("linkcallback", linkcallback);
    })

    $.fn.extend({
        reSetSelectOptions: function (url, param, value) {
            var me = $(this);
            $.post(url, param, function (result) {
                if (result.success && result.data) {
                    var idField = result.data.IDField;
                    var nameField = result.data.NameField;
                    var data = result.data.List;
                    me.empty();
                    if (data) {
                        me.prepend("<option value=\"\">--请选择--</option>");
                        $.each(data, function (index, val) {
                            if (value == val.ID) {
                                me.append("<option value=\"" + val.ID + "\" selected>" + val.Name + "</option>");
                            }
                            else {
                                me.append("<option value=\"" + val.ID + "\">" + val.Name + "</option>");
                            }

                        });
                    }
                }
            })
        }
    });
})