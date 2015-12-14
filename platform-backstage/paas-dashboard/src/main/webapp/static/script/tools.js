//jQuery扩展
$.extend({
    // 改写$.get方法，对Url添加时间戳，确保每次请求，数据都是最新的
    // 添加没有权限的处理
    get: function(url, data, callback, load, type) {

        //弹出加载提示
        if (load == 0 || load == "" || load == null) {
            var ll = layer.load('加载中^_^');
        }
        // shift arguments if data argument was omited
        if (jQuery.isFunction(data)) {
            type = type || callback;
            callback = data;
            data = null;
        }
        if (url.indexOf("?") > 0) {
            url += "&Timestamp=" + this.getTime();
        } else {
            url += "?Timestamp=" + this.getTime();
        }
        return jQuery.ajax({
            type: "GET",
            url: url.replace(/\+/g, "%2B"),
            data: data,
            success: function(rdata) {

                //关闭加载提示
                if (load == 0 || load == "" || load == null) {
                    layer.close(ll);
                }
                if (rdata.result == 404) {
                    art.dialog({
                        id: 'SB',
                        content: rdata.content,
                        close: function() {
                            window.location.href = "/Account/LogOn.html";
                        },
                        ok: function() {
                            window.location.href = "/Account/LogOn.html";
                        }
                    });
                } else {
                    try {
                        callback(rdata);
                    } catch (e) {

                    }

                }
            },
            dataType: type
        });
    },

    // 改写$.post方法，添加没有权限的处理
    post: function(url, data, callback, load, type) {

        //弹出加载提示
        if (load == 0 || load == "" || load == null) {
            var ll = layer.load('加载中^_^');
        }
        // shift arguments if data argument was omited
        if (jQuery.isFunction(data)) {
            type = type || callback;
            callback = data;
            data = {};
        }

        return jQuery.ajax({
            type: "POST",
            url: url,
            data: data,
            success: function(rdata) {
                if (load == 0 || load == "" || load == null) {
                    //关闭加载提示
                    layer.close(ll);
                }
                if (rdata.result == 404) {
                    art.dialog({
                        id: 'SB',
                        content: rdata.content,
                        close: function() {
                            window.location.href = "/Login/Index?backUrl=" + encodeURIComponent(window.location.href);
                        },
                        ok: function() {
                            window.location.href = "/Login/Index?backUrl=" + encodeURIComponent(window.location.href);
                        }
                    });
                } else {
                    try {
                        callback(rdata);
                    } catch (e) {

                    }
                }
            },
            dataType: type
        });
    },

    // jQuery.dataTable插件的回调函数
    // 对内容过长的列添加addtitle样式，自动设置td的Title属性为内容
    completeTdTitle: function(tabid) {
        var tab = $("#" + tabid);
        $(tab).find("td.addtitle").each(function() {
            // $(this).html("<div class='curd'
            // title='"+$(this).text()+"'>"+$(this).html()+"</div>");
            $(this).attr("title", $(this).text());
        });
    },

    // 字符串的与C# String.Format类似的format方法
    format: function(source, params) {
        // / <summary>
        // / 格式化字符串
        // / <param name="source">要格式化的字符串</param>
        // / <param name="params">参数列表</param>
        // / </summary>
        if (arguments.length == 1) {
            return function() {
                var args = $.makeArray(arguments);
                args.unshift(source);
                return $.format.apply(this, args);
            };
        }
        if (arguments.length > 2 && params.constructor != Array) {
            params = $.makeArray(arguments).slice(1);
        }
        if (params.constructor != Array) {
            params = [params];
        }
        $.each(params, function(i, n) {
            source = source.replace(new RegExp("\\{" + i + "}", "g"), n);
        });
        return source;
    },

    // 返回时间的数值表示形式
    getTime: function() {
        // / <summary>
        // / 获取时间戳
        // / </summary>
        return (new Date()).valueOf();
    },

    // Array的操作
    arrayRemove: function(array, obj) {
        var index = -1;
        for (var i = 0; i < array.length; i++) {
            if (array[i] == obj) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            $.arrayRemoveAt(array, index);
        }
    },
    arrayClear: function(array) {
        array.length = 0;
    },
    arrayRemoveAt: function(array, index) {
        array.splice(index, 1);
    },
    arrayInsertAt: function(array, index, obj) {
        array.splice(index, 0, obj);
    },
    // 入队（将元素添加到数组）
    Array$enqueue: function(array, item) {
        // / <summary>入队（将元素添加到数组）</summary>
        // / <param name="array" type="Array" elementMayBeNull="true"></param>
        // / <param name="item" mayBeNull="true"></param>
        var e = Function._validateParams(arguments, [{
            name: "array",
            type: Array,
            elementMayBeNull: true
        }, {
            name: "item",
            mayBeNull: true
        }]);
        if (e)
            throw e;
        array[array.length] = item;
    },
    // 添加一组数据
    Array$addRange: function(array, items) {
        // / <summary locid="M:J#Array.addRange" />
        // / <param name="array" type="Array" elementMayBeNull="true"></param>
        // / <param name="items" type="Array" elementMayBeNull="true"></param>
        var e = Function._validateParams(arguments, [{
            name: "array",
            type: Array,
            elementMayBeNull: true
        }, {
            name: "items",
            type: Array,
            elementMayBeNull: true
        }]);
        if (e)
            throw e;
        array.push.apply(array, items);
    },
    Array$clear: function(array) {
        // / <summary locid="M:J#Array.clear" />
        // / <param name="array" type="Array" elementMayBeNull="true"></param>
        var e = Function._validateParams(arguments, [{
            name: "array",
            type: Array,
            elementMayBeNull: true
        }]);
        if (e)
            throw e;
        array.length = 0;
    },
    Array$clone: function(array) {
        // / <summary locid="M:J#Array.clone" />
        // / <param name="array" type="Array" elementMayBeNull="true"></param>
        // / <returns type="Array" elementMayBeNull="true"></returns>
        var e = Function._validateParams(arguments, [{
            name: "array",
            type: Array,
            elementMayBeNull: true
        }]);
        if (e)
            throw e;
        if (array.length === 1) {
            return [array[0]];
        } else {
            return Array.apply(null, array);
        }
    },
    Array$contains: function(array, item) {
        // / <summary locid="M:J#Array.contains" />
        // / <param name="array" type="Array" elementMayBeNull="true"></param>
        // / <param name="item" mayBeNull="true"></param>
        // / <returns type="Boolean"></returns>
        var e = Function._validateParams(arguments, [{
            name: "array",
            type: Array,
            elementMayBeNull: true
        }, {
            name: "item",
            mayBeNull: true
        }]);
        if (e)
            throw e;
        return (Sys._indexOf(array, item) >= 0);
    },
    Array$dequeue: function(array) {
        // / <summary locid="M:J#Array.dequeue" />
        // / <param name="array" type="Array" elementMayBeNull="true"></param>
        // / <returns mayBeNull="true"></returns>
        var e = Function._validateParams(arguments, [{
            name: "array",
            type: Array,
            elementMayBeNull: true
        }]);
        if (e)
            throw e;
        return array.shift();
    },
    Array$forEach: function(array, method, instance) {
        // / <summary locid="M:J#Array.forEach" />
        // / <param name="array" type="Array" elementMayBeNull="true"></param>
        // / <param name="method" type="Function"></param>
        // / <param name="instance" optional="true" mayBeNull="true"></param>
        var e = Function._validateParams(arguments, [{
            name: "array",
            type: Array,
            elementMayBeNull: true
        }, {
            name: "method",
            type: Function
        }, {
            name: "instance",
            mayBeNull: true,
            optional: true
        }]);
        if (e)
            throw e;
        for (var i = 0, l = array.length; i < l; i++) {
            var elt = array[i];
            if (typeof(elt) !== 'undefined')
                method.call(instance, elt, i, array);
        }
    },
    Array$indexOf: function(array, item, start) {
        // / <summary locid="M:J#Array.indexOf" />
        // / <param name="array" type="Array" elementMayBeNull="true"></param>
        // / <param name="item" optional="true" mayBeNull="true"></param>
        // / <param name="start" optional="true" mayBeNull="true"></param>
        // / <returns type="Number"></returns>
        var e = Function._validateParams(arguments, [{
            name: "array",
            type: Array,
            elementMayBeNull: true
        }, {
            name: "item",
            mayBeNull: true,
            optional: true
        }, {
            name: "start",
            mayBeNull: true,
            optional: true
        }]);
        if (e)
            throw e;
        return Sys._indexOf(array, item, start);
    },
    Array$insert: function(array, index, item) {
        // / <summary locid="M:J#Array.insert" />
        // / <param name="array" type="Array" elementMayBeNull="true"></param>
        // / <param name="index" mayBeNull="true"></param>
        // / <param name="item" mayBeNull="true"></param>
        var e = Function._validateParams(arguments, [{
            name: "array",
            type: Array,
            elementMayBeNull: true
        }, {
            name: "index",
            mayBeNull: true
        }, {
            name: "item",
            mayBeNull: true
        }]);
        if (e)
            throw e;
        array.splice(index, 0, item);
    },
    Array$parse: function(value) {
        // / <summary locid="M:J#Array.parse" />
        // / <param name="value" type="String" mayBeNull="true"></param>
        // / <returns type="Array" elementMayBeNull="true"></returns>
        var e = Function._validateParams(arguments, [{
            name: "value",
            type: String,
            mayBeNull: true
        }]);
        if (e)
            throw e;
        if (!value)
            return [];
        var v = eval(value);
        if (!Array.isInstanceOfType(v))
            throw Error.argument('value', Sys.Res.arrayParseBadFormat);
        return v;
    },
    Array$remove: function(array, item) {
        // / <summary locid="M:J#Array.remove" />
        // / <param name="array" type="Array" elementMayBeNull="true"></param>
        // / <param name="item" mayBeNull="true"></param>
        // / <returns type="Boolean"></returns>
        var e = Function._validateParams(arguments, [{
            name: "array",
            type: Array,
            elementMayBeNull: true
        }, {
            name: "item",
            mayBeNull: true
        }]);
        if (e)
            throw e;
        var index = Sys._indexOf(array, item);
        if (index >= 0) {
            array.splice(index, 1);
        }
        return (index >= 0);
    },
    Array$removeAt: function(array, index) {
        // / <summary locid="M:J#Array.removeAt" />
        // / <param name="array" type="Array" elementMayBeNull="true"></param>
        // / <param name="index" mayBeNull="true"></param>
        var e = Function._validateParams(arguments, [{
            name: "array",
            type: Array,
            elementMayBeNull: true
        }, {
            name: "index",
            mayBeNull: true
        }]);
        if (e)
            throw e;
        array.splice(index, 1);
    }
});

// 验证中华人民共和国居民身份证号码的正确性

function checkID(num) {
        var len = num.length,
            re;
        if (len == 15)
            re = new RegExp(/^(\d{6})()?(\d{2})(\d{2})(\d{2})(\d{3})$/);
        else if (len == 18)
            re = new RegExp(/^(\d{6})()?(\d{4})(\d{2})(\d{2})(\d{3})(\d)$/);
        else {
            return false;
        }
        var a = num.match(re);
        if (a != null) {
            if (len == 15) {
                var D = new Date("19" + a[3] + "/" + a[4] + "/" + a[5]);
                var B = D.getYear() == a[3] && (D.getMonth() + 1) == a[4] && D.getDate() == a[5];
            } else {
                var D = new Date(a[3] + "/" + a[4] + "/" + a[5]);
                var B = D.getFullYear() == a[3] && (D.getMonth() + 1) == a[4] && D.getDate() == a[5];
            }
            if (!B) {
                return false;
            }
        }
        return true;;
    }
    // function checkID(idcard) {
    // // debugger;
    // var area = { 11: "北京", 12: "天津", 13: "河北", 14: "山西", 15: "内蒙古", 21: "辽宁", 22:
    // "吉林", 23: "黑龙江", 31: "上海", 32: "江苏", 33: "浙江", 34: "安徽", 35: "福建", 36: "江西",
    // 37: "山东", 41: "河南", 42: "湖北", 43: "湖南", 44: "广东", 45: "广西", 46: "海南", 50:
    // "重庆", 51: "四川", 52: "贵州", 53: "云南", 54: "西藏", 61: "陕西", 62: "甘肃", 63: "青海",
    // 64: "宁夏", 65: "新疆", 71: "台湾", 81: "香港", 82: "澳门", 91: "国外" }
    // var Y, JYM;
    // var S, M;
    // var idcardArray = idcard.split("");
    // var ereg; //检验用到的正则表达式
    // //地区检验
    // if (area[parseInt(idcard.substr(0, 2))] == null) return false;
    // //身份号码位数及格式检验
    // switch (idcard.length) {
    // case 15:
    // if ((parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0 ||
    // ((parseInt(idcard.substr(6, 2)) + 1900) % 100 == 0 &&
    // (parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0)) {
    // ereg =
    // /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;
    // //测试出生日期的合法性
    // } else {
    // ereg =
    // /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;
    // //测试出生日期的合法性
    // }
    // return ereg.test(idcard);
    // case 18:
    // //18位身份号码检测
    // //出生日期的合法性检查
    // //闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
    // //平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
    // if (parseInt(idcard.substr(6, 4)) % 4 == 0 || (parseInt(idcard.substr(6, 4))
    // % 100 == 0 && parseInt(idcard.substr(6, 4)) % 4 == 0)) {
    // ereg =
    // /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;
    // //闰年出生日期的合法性正则表达式
    // } else {
    // ereg =
    // /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;
    // //平年出生日期的合法性正则表达式
    // }
    // if (ereg.test(idcard)) {//测试出生日期的合法性
    // //计算校验位
    // S = (parseInt(idcardArray[0]) + parseInt(idcardArray[10])) * 7
    // + (parseInt(idcardArray[1]) + parseInt(idcardArray[11])) * 9
    // + (parseInt(idcardArray[2]) + parseInt(idcardArray[12])) * 10
    // + (parseInt(idcardArray[3]) + parseInt(idcardArray[13])) * 5
    // + (parseInt(idcardArray[4]) + parseInt(idcardArray[14])) * 8
    // + (parseInt(idcardArray[5]) + parseInt(idcardArray[15])) * 4
    // + (parseInt(idcardArray[6]) + parseInt(idcardArray[16])) * 2
    // + parseInt(idcardArray[7]) * 1
    // + parseInt(idcardArray[8]) * 6
    // + parseInt(idcardArray[9]) * 3;
    // Y = S % 11;
    // M = "F";
    // JYM = "10X98765432";
    // M = JYM.substr(Y, 1); //判断校验位
    // if (M == idcardArray[17]) return true; //检测ID的校验位
    // else return false;
    // }
    // else return false;
    // default:
    // return false;
    // }
    // }

(function($) {
    $.fn.bgIframe = $.fn.bgiframe = function(s) {
        if ($.browser.msie && /6.0/.test(navigator.userAgent)) {
            s = $.extend({
                top: 'auto',
                left: 'auto',
                width: 'auto',
                height: 'auto',
                src: 'javascript:false;'
            }, s || {});
            var prop = function(n) {
                return n && n.constructor == Number ? n + 'px' : n;
            };
            return this
                .each(function() {
                    if ($('> iframe.bgiframe', this).length == 0) {
                        var iframe = $(
                                '<iframe frameborder="0" tabindex="-1"></iframe>')
                            .addClass("bgiframe")
                            .css({
                                display: 'block',
                                position: 'absolute',
                                zIndex: '-1',
                                opacity: 0,
                                top: s.top === 'auto' ? ((this.clientTop || 0) * -1 + 'px') : prop(s.top),
                                left: s.left === 'auto' ? ((this.clientLeft || 0) * -1 + 'px') : prop(s.left),
                                width: s.width === 'auto' ? (this.offsetWidth + 'px') : prop(s.width),
                                height: s.height === 'auto' ? (this.offsetHeight + 'px') : prop(s.height)
                            }).insertBefore(this.firstChild);
                    }
                });
        }
        return this;
    };
})(jQuery);

// 用于jQuery.dataTable fnDrawCallback回调函数内，传入Table的Id
// 实现集合CheckBox选定，达到全选的效果
function SetCheckBox(objId) {
    var tbodycks = $("#" + objId + " tbody input[type='checkbox']");
    var theadck = $("#" + objId + " thead input[type='checkbox']");
    tbodycks.each(function() {
        if (!$(this).attr("checked") && !$(this).attr("disabled")) {
            theadck.attr("checked", false);
        }
        $(this).bind("click", function() {
            var flag = true;
            tbodycks.each(function() {
                if (!$(this).attr("checked") && flag && !$(this).attr("disabled"))
                    flag = false;
            });
            theadck.attr("checked", flag);

        });
    });
    theadck.eq(0).bind("change", function() {
        tbodycks.each(function() {
            if (!$(this).attr("disabled")) {
                $(this)[0].checked = theadck.eq(0)[0].checked;
                //$(this).attr("checked", theadck.eq(0)[0].checked);
            }
        });
    });
}

// 获取Table内被选定的Checkbox的所有Id，返回格式为{id1},{id2},{id3}....
function GetChecked(objId) {
    debugger;
    var str = '';
    $(
        "#" + objId + " input[type='checkbox'],#" + objId + " input[type='radio']").each(
        function() {
            if ($(this).prop("checked")) {
                if (this.type == "radio") {
                    str = $(this).prop("id") + "," + $(this).prop("username");
                } else {
                    if (("," + str + ",").indexOf("," + $(this).attr("id") + ",") < 0) {
                        str += str.length > 0 ? (',' + $(this).attr("id")) : $(this).attr("id");
                    }
                }
            }
        });
    return str;
}

function InitCheckedStatus(objId,checkedIds){
    var str = '';
    $(
        "#" + objId + " input[type='checkbox'],#" + objId + " input[type='radio']").each(
        function() {
            var uuid = $(this).attr("uuid");
            if(checkedIds.indexOf(uuid)>=0){
                $(this).prop("checked","checked");
                $(this).prop("disabled","disabled");
        }
    });
}
// 获取Table内被选定的Checkbox的所有Value，返回格式为{Value1},{Value2},{Value3}....
function GetCheckedValue(objId) {
    var str = '';
    $(
        "#" + objId + " input[type='checkbox'],#" + objId + " input[type='radio']").each(
        function() {
            if ($(this).prop("checked")) {
                if (this.type == "radio") {
                    str = $(this).val();
                } else {
                    if (("," + str + ",")
                        .indexOf("," + $(this).val() + ",") < 0) {
                        str += str.length > 0 ? (',' + $(this).val()) : $(
                            this).val();
                    }
                }
            }
        });
    return str;
}

// 大小图切换
function ChangeList(obj) {
    if ($("#view_list") && $("#view_pic")) {
        if (obj.str == "list") {
            $("#view_pic").addClass('pic').removeClass('picOn');
            $("#view_list").addClass('listOn').removeClass('list');
        } else {
            $("#view_pic").addClass('picOn').removeClass('pic');
            $("#view_list").addClass('list').removeClass('listOn');
        }
        obj.fun();
    }
}

// 获取当前页面Url中的参数
function getUrlParam(name) {
    var vars = [],
        hash;
    var hashes = window.location.href.slice(
        window.location.href.indexOf('?') + 1).split('&');
    for (var i = 0; i < hashes.length; i++) {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars[name];
}

// 长度限制
function AutoIndent(objID) {
    var count = 0;
    $("#" + objID + " thead tr th")
        .each(
            function() {
                if ($(this).attr("class").indexOf('autoIndent') >= 0) {
                    var width = $(this).css("width").replace("px", "");
                    var wordNumber = parseInt(parseInt(width) / 12) - 2; // 要显示的字的个数
                    $("#" + objID + " tbody tr")
                        .each(
                            function() {
                                var str = $(this).find("td")
                                    .eq(count).html();
                                $(this).find("td").eq(count)
                                    .attr("title", str);
                                $(this)
                                    .find("td")
                                    .eq(count)
                                    .html(
                                        str.length > wordNumber ? (str
                                            .substring(
                                                0,
                                                wordNumber) + '…') : str);
                            });
                }
                count++;
            });
}

function AutocompleteSearch(url, objId) {
    var availableTags = "";
    $.getJSON(url, function(data) {
        availableTags = data;
    });

    function split(val) {
        return val.split(/,\s*/);
    }

    function extractLast(term) {
        return split(term).pop();
    }

    $("#" + objId).bind(
        "keydown",
        function(event) {
            if (event.keyCode === $.ui.keyCode.TAB && $(this).data("autocomplete").menu.active) {
                event.preventDefault();
            }
        }).autocomplete({
        minLength: 0,
        source: function(request, response) {
            response($.ui.autocomplete.filter(availableTags,
                extractLast(request.term)));
        },
        focus: function() {
            return false;
        },
        select: function(event, ui) {
            var terms = split(this.value);
            terms.pop();
            terms.push(ui.item.value);
            this.value = terms;
            return false;
        }
    });
}

function ShowPages(objId, index) {
    var obj = "#" + objId;
    var currentpage = index; // 当前页号
    var totalCount = 0; // 总条数
    if ($(obj).find("tbody tr").length > 1)
        totalCount = $(obj).find("tbody tr").length;
    else {
        if ($(obj).find("tbody tr td").length == 1)
            totalCount = 0;
        else
            totalCount = 1;
    }

    // 显示数据
    for (var i = 1; i <= $(obj).find("tbody tr").length; i++) {
        if (i < ((index - 1) * 10 + 1) || i > index * 10) {
            $(obj).find("tbody tr").eq(i - 1).hide();
        } else {
            $(obj).find("tbody tr").eq(i - 1).show();
        }
    }

    $(obj).find("span[id='singledatacount']").eq(0).html(totalCount); // 总条数

    // 分页
    var pagecount = totalCount % 10 == 0 ? (parseInt(totalCount / 10)) : (parseInt(totalCount / 10) + 1);
    var pageStr = '';
    for (var i = 1; i <= pagecount; i++) {
        if (i == 1 && index > 2) {
            pageStr += '…';
        }
        if ((i >= parseInt(index) - 1 && i <= parseInt(index) + 1 && parseInt(index) >= 2 && parseInt(index) <= pagecount - 1) || (parseInt(index) == 1 && i <= 3) || (parseInt(index) == pagecount && i >= pagecount - 2)) {
            pageStr += '<a ';
            if (i == parseInt(index)) {
                pageStr += ' style="margin:0px 5px; font-size:12px; background-color:#FFF; color:Gray; text-decoration:none;" ';
            } else {
                pageStr += ' style="margin:0px 5px; font-size:12px; background-color:#FFF; color:#000;" onclick="ShowPages(\'' + objId + '\',' + i + ');"';
            }
            pageStr += '>' + i;
            pageStr += '</a>';
        }
        if (i == pagecount && index < pagecount - 1) {
            pageStr += '…';
        }
    }

    if (parseInt(index) == 1 || totalCount == 0) {
        $(obj + " #singlefirst," + obj + " #singleprevious").addClass(
            "ui-state-disabled");
    } else {
        $(obj + " #singlefirst," + obj + " #singleprevious").removeClass(
            "ui-state-disabled");
    }
    if (parseInt(index) == pagecount || totalCount == 0) {
        $(obj + " #singlenext," + obj + " #singlelast").addClass(
            "ui-state-disabled");
    } else {
        $(obj + " #singlenext," + obj + " #singlelast").removeClass(
            "ui-state-disabled");
    }
    $(obj + " #fromnumber").html(totalCount > 0 ? ((index - 1) * 10 + 1) : 0);
    $(obj + " #tonumber").html(
        totalCount > 0 ? (totalCount - index * 10 > 0 ? index * 10 : totalCount) : 0);
    $(obj + " #singleprevious").attr("value", index == 1 ? 1 : index - 1);
    $(obj + " #singlenext").attr("value",
        index == pagecount ? pagecount : (parseInt(index) + 1));
    $(obj + " #singlelast").attr("value", pagecount);

    $(obj + " #singlepages").html('').append(pageStr);
    $(
            obj + " #singlefirst," + obj + " #singleprevious," + obj + " #singlenext," + obj + " #singlelast").unbind("click")
        .bind("click", function() {
            ShowPages(objId, $(this).attr("value"));
        });
}

// 获取时间戳

function getTimestamp() {
    var date = new Date();
    return date.valueOf();
}

function threeBackground(id, fun) {
    if (id != '0') {
        $("#navigation a[id='" + id + "']").eq(0).addClass("bgc3c red");
    }
    // 选择后定位
    $("#navigation a").unbind("click").bind("click", function() {
        $("#navigation a").removeClass("bgc3c red");
        $(this).addClass("bgc3c red");
        try {
            fun();
        } catch (e) {

        }
    });
}

/* 获取浏览器类型 */

function identifyApp() {
    var appName;
    if (navigator.userAgent.indexOf("MSIE 7.0") >= 0) {
        appName = "IE7";
    } else if (navigator.userAgent.indexOf("MSIE 8.0") >= 0) {
        appName = "IE8";
    } else if (navigator.userAgent.indexOf("MSIE 9.0") >= 0) {
        appName = "IE9";
    } else if (navigator.userAgent.indexOf("MSIE 6.0") >= 0) {
        appName = "IE6";
    } else if (navigator.userAgent.indexOf("Firefox") >= 0) {
        appName = "Firefox";
    } else if (navigator.userAgent.indexOf("Chrome") >= 0) {
        appName = "Chrome";
    } else if (navigator.userAgent.indexOf("Opera") >= 0) {
        appName = "Opera";
    } else if (navigator.userAgent.indexOf("Safari") >= 0) {
        appName = "Safari";
    } else {
        appName = "Other"; // Safari, Chrome, ...
    }
    return appName;
}

// 获取页面高度

function GetScreenHeight() {
    if (navigator.userAgent.indexOf("MSIE 7.0") >= 0) {
        return window.screen.height;
    } else if (navigator.userAgent.indexOf("MSIE 8.0") >= 0) {
        return window.screen.height;
    } else if (navigator.userAgent.indexOf("MSIE 9.0") >= 0) {
        return window.screen.height;
    } else if (navigator.userAgent.indexOf("MSIE 6.0") >= 0) {
        return window.screen.height;
    } else if (navigator.userAgent.indexOf("Firefox") >= 0) {
        return window.screen.height;
    } else if (navigator.userAgent.indexOf("Chrome") >= 0) {
        return window.screen.height;
    } else if (navigator.userAgent.indexOf("Opera") >= 0) {
        return window.screen.height;
    } else if (navigator.userAgent.indexOf("Safari") >= 0) {
        return window.screen.height;
    } else {
        return window.screen.height;
    }
}

// 打开模式窗口

function winModalFullScreen(strURL) {
    if (identifyApp() != "Opera") {
        var sheight = window.screen.height;
        var swidth = window.screen.width;

        var winoption = "dialogHeight:" + sheight + "px;dialogWidth:" + swidth + "px;status:yes;scroll:yes;resizable:yes;center:yes;fullscreen=1";
        var tmp = window.showModalDialog(strURL, window, winoption);
        return tmp;
    } else {
        art.dialog({
            title: "小提示",
            lock: true,
            fixed: true,
            width: 350,
            height: 100,
            time: false,
            content: '暂不支持Opera浏览器，请使用其他浏览器',
            time: 3000
        });
    }
}

// //去掉数组中重复的值
// Array.prototype.unique = function () {
// var data = this || [];
// var a = {}; //声明一个对象，javascript的对象可以当哈希表用
// for (var i = 0; i < data.length; i++) {
// a[data[i]] = true; //设置标记，把数组的值当下标，这样就可以去掉重复的值
// }
// data.length = 0;
// for (var i in a) { //遍历对象，把已标记的还原成数组
// this[data.length] = i;
// }
// return data;
// };

// Array.prototype.clear = function () {
// this.length = 0;
// };
// Array.prototype.insertAt = function (index, obj) {
// this.splice(index, 0, obj);
// };
// Array.prototype.removeAt = function (index) {
// this.splice(index, 1);
// };
// Array.prototype.remove = function (obj) {
// var index = -1;
// for (var i = 0; i < this.length; i++) {
// if (this[i] == obj) {
// index = i;
// break;
// }
// }
// if (index >= 0) {
// this.removeAt(index);
// }
// };

// 去掉左右两边空格
String.prototype.trim = function() {
    return this.replace(/(^\s*)|(\s*$)/g, "");
};

// 去掉左边空格
String.prototype.lTrim = function() {
    return this.replace(/(^\s*)/g, "");
};

// 去掉右边空格
String.prototype.rTrim = function() {
    return this.replace(/(\s*$)/g, "");
};

// 获取光标在输入框的位置

function getPosition(obj) {
    var result = 0;
    if (obj.setSelectionRange) { // 非IE浏览器
        result = obj.selectionStart;
    } else { // IE
        var rng;
        if (obj.tagName == "TEXTAREA") { // 如果是文本域
            rng = event.srcElement.createTextRange();
            rng.moveToPoint(event.x, event.y);
        } else { // 输入框
            rng = document.selection.createRange();
        }
        rng.moveStart("character", -event.srcElement.value.length);
        result = rng.text.length;
    }
    return result;
}

// 设置光标在输入框的位置

function setLocation(elm, n) {
    if (n > elm.value.length)
        n = elm.value.length;
    if (elm.createTextRange) { // IE
        var textRange = elm.createTextRange();
        textRange.moveStart('character', n);
        textRange.collapse();
        textRange.select();
    } else if (elm.setSelectionRange) { // Firefox
        elm.setSelectionRange(n, n);
        elm.focus();
    }
}

// textarea的长度限制

function TextearaMaxlength(obj) {
    var iMaxLen = parseInt($(obj).attr('maxlength'));
    var iCurLen = $(obj).val().length;
    if ($(obj).attr('maxlength') && iCurLen > iMaxLen) {
        $(obj).val($(obj).val().substring(0, iMaxLen));
    }
}

function turnToNext(obj, rediction) {
    // 图片的集合
    var pictureCollection = $(obj).parent().parent().find(
        "#imageCollection img");
    // 当前显示的第几个
    var index = $(obj).parent().parent().find("#imageCollection input").eq(0)
        .val();
    if (rediction == "left") {
        // 向左
        if (parseInt(index) != 1) {
            pictureCollection.css("display", "none");
            pictureCollection.eq(index - 2).animate({
                opacity: 'show'
            }, 50);
            $(obj).parent().parent().find("#imageCollection input").eq(0).val(
                parseInt(index) - 1);
        } else {
            pictureCollection.css("display", "none");
            pictureCollection.eq(pictureCollection.length - 1).animate({
                opacity: 'show'
            }, 50);
            $(obj).parent().parent().find("#imageCollection input").eq(0).val(
                pictureCollection.length);
        }
    } else {
        // 向左
        if (parseInt(index) != pictureCollection.length) {
            pictureCollection.css("display", "none");
            pictureCollection.eq(index).animate({
                opacity: 'show'
            }, 50);
            $(obj).parent().parent().find("#imageCollection input").eq(0).val(
                parseInt(index) + 1);
        } else {
            pictureCollection.css("display", "none");
            pictureCollection.eq(0).animate({
                opacity: 'show'
            }, 50);
            $(obj).parent().parent().find("#imageCollection input").eq(0)
                .val(1);
        }
    }
}

// 验证时间的正确性

function checkDate(startDateID, endDateID, title, message) {
    var startDate = $("#" + startDateID).val();
    var endDate = $("#" + endDateID).val();
    if (startDate != "" && endDate != "") {
        var startLength = new Date(startDate.replace(/-/g, '/')).getTime();
        var endLength = new Date(endDate.replace(/-/g, '/')).getTime();
        if (startLength > endLength) {
            $("#" + startDateID).val("");
            $("#" + endDateID).val("");
            if (title != "" && title != undefined)
                art.dialog({
                    title: title,
                    content: message,
                    width: 300,
                    height: 60,
                    fixed: true,
                    lock: true,
                    time: 3000
                });
            return false;
        }
    }
    return true;
}

/**/
/*******************************************************************************
 * | 函数名称： setCookie | | 函数功能： 设置cookie函数 | | 入口参数： name：cookie名称；value：cookie值 |
 ******************************************************************************/

function setCookie(name, value) {
    var argv = setCookie.arguments;
    var argc = setCookie.arguments.length;
    var expires = (argc > 2) ? argv[2] : null;
    if (expires != null) {
        var largeExpDate = new Date();
        largeExpDate.setTime(largeExpDate.getTime() + (expires * 1000 * 3600 * 24));
    }
    document.cookie = name + "=" + escape(value) + ((expires == null) ? "" : ("; expires=" + largeExpDate
        .toGMTString()));
}

/**/
/*******************************************************************************
 * | 函数名称： getCookie | | 函数功能： 读取cookie函数 | | 入口参数： name：cookie名称 |
 ******************************************************************************/

function getCookie(name) {
    var search = name + "=";
    if (document.cookie.length > 0) {
        var offset = document.cookie.indexOf(search);
        if (offset != -1) {
            offset += search.length;
            var end = document.cookie.indexOf(";", offset);
            if (end == -1)
                end = document.cookie.length;
            return unescape(document.cookie.substring(offset, end));
        } else
            return "";
    }
    return "";
}

/**/
/*******************************************************************************
 * | 函数名称： deleteCookie | | 函数功能： 删除cookie函数 | | 入口参数： Name：cookie名称 |
 ******************************************************************************/

function deleteCookie(name) {
    var expdate = new Date();
    expdate.setTime(expdate.getTime() - (86400 * 1000 * 1));
    setCookie(name, "", expdate);
}