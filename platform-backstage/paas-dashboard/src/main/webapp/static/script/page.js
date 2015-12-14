/**
 * [简单的分页插件，需要jquery以及jsrender]
 * @auther 舒敏
 */
(function ($) {
    /**
     * [JsRenderData JsRender扩展]
     * @param {[json]} args [参数]
     */
    $.fn.JsRenderData = function (args) {
        me = this;
        if (me.length == 0) {
            return me;
        }
        var tmplEngine = 1; // 模板引擎：1-JsRender；2-Laytpl；3-artTemplate
        var isPage = true; //是否分页
        var pageSize = 10; //每页的个数
        var pageIndex = 1; //请求的页数
        var method = "get"; //请求方式(get/post) Add By 舒敏 2014.1.14
        var parameter = {}; //参数
        var dataType = "json"; //请求返回的数据类型,1:json,返回json格式;2:text,json返回json字符串
        var templateId; //自定义模板
        var sourceUrl; //请求的数据源连接
        var pagingCallback = null;
        var orderBy; //默认排序字段
        var orderingRule; //默认排序方式
        var recordCount; //数据的总数
        var funCallback; //回调函数
        var dataSource; //定义数据源
        if (arguments.length > 0) { //绑定对象的ID
            tmplEngine = arguments[0].tmplEngine == undefined ? tmplEngine : arguments[0].tmplEngine; // 模板引擎：1-JsRender；2-Laytpl；3-artTemplate
            isPage = arguments[0].isPage == undefined ? isPage : arguments[0].isPage; //是否分页
            pageSize = arguments[0].pageSize == undefined ? pageSize : arguments[0].pageSize; //每页的个数
            pageIndex = arguments[0].pageIndex == undefined ? pageIndex : arguments[0].pageIndex; //请求的页数
            method = arguments[0].method == undefined ? method : arguments[0].method; //请求方式
            parameter = arguments[0].parameter == undefined ? parameter : arguments[0].parameter; //请求参数
            dataType = arguments[0].dataType == undefined ? dataType : arguments[0].dataType; //请求返回的数据类型
            templateId = arguments[0].templateId == undefined ? null : arguments[0].templateId; //自定义模板
            sourceUrl = arguments[0].sourceUrl == undefined ? null : arguments[0].sourceUrl; //请求的数据源连接

            recordCount = arguments[0].recordCount == undefined ? null : arguments[0].recordCount; //数据的总数
            dataSource = arguments[0].dataSource == undefined ? null : arguments[0].dataSource; //定义数据源
            funCallback = arguments[0].funCallback == undefined ? null : arguments[0].funCallback; //回调函数
            pagingCallback = arguments[0].pagingCallback == undefined ? null : arguments[0].pagingCallback;
            orderBy = arguments[0].orderBy == undefined ? null : arguments[0].orderBy; //默认排序字段
            orderingRule = arguments[0].orderingRule == undefined ? " ASC" : arguments[0].orderingRule; //默认排序方式
        }
        // 如果请求地址不为空则发送Ajax请求取数据
        if (sourceUrl != null) {
            switch (method) {
                case 'get':
                    if (sourceUrl.indexOf("?") > 0) {
                        var p = sourceUrl.split("?")[1];
                        parameter[p.split("=")[0]] = p.split("=")[1];
                    }
                    // 	sourceUrl += "?";
                    // 	sourceUrl += "&timeSpan=" + (new Date()).valueOf();
                    // 	sourceUrl += "&pageIndex=" + pageIndex + "&pageSize=" + pageSize;
                    // } else {
                    // 	sourceUrl += "&timeSpan=" + (new Date()).valueOf();
                    // 	sourceUrl = sourceUrl.replace(/&?(pageIndex|pageSize|_)=([^&]*)/gi, ""); //过滤url的翻页参数
                    // 	sourceUrl += "&pageIndex=" + pageIndex + "&pageSize=" + pageSize;
                    // }
                    // if (orderBy != null && orderBy != undefined && orderBy != "") {
                    // 	sourceUrl += "&orderBy=" + orderBy + (orderingRule == " ASC" ? " ASC " : " DESC ");
                    // };
                    parameter.timeSpan = (new Date()).valueOf(); //时间戳
                    parameter.pageIndex = pageIndex; //当前页
                    parameter.pageSize = pageSize; //
                    if (orderBy != null && orderBy != undefined && orderBy != "") {
                        parameter.orderBy = orderBy + ' ' + (orderingRule.trim().toUpperCase() == "ASC" ? "ASC " : "DESC ");
                    }
                    $.get(sourceUrl, parameter, function (data) {
                        //请求返回的数据类型是json字符串而不是json格式
                        if (dataType != "json") {
                            data.dataList = JSON.parse(data.dataList);
                        }

                        dataSource = data;

                        me.buildBaseDom();

                        me.JsRenderBody(tmplEngine, me, dataSource, templateId);
                        if (isPage) {
                            me.JsRenderPaging(tmplEngine, me, dataSource, isPage, pageSize, pageIndex, sourceUrl, method, templateId, funCallback, parameter, pagingCallback, dataType);
                        }
                        fnTableBindSort(tmplEngine, me, dataSource, isPage, pageSize, pageIndex, sourceUrl, method, templateId, funCallback, orderingRule, dataType, parameter);
                        //排序标志
                        $(me).parent().find("thead th").each(function () {
                            var sort = $(this).attr("orderBy");
                            if (sort != null && sort != undefined) {
                                var str = $(this).text();
                                $(this).css("cursor", "pointer").html('<div class="sort">' + str + '<div class="sort-do"><i class="s-up"></i><i class="s-down"></i></div></div>');
                            }
                        });
                        if (funCallback != null && funCallback != undefined)
                            funCallback(data, pageIndex, pageSize);
                    });
                    break;
                case 'post':
                    if (sourceUrl.indexOf("?") > 0) {
                        var p = sourceUrl.split("?")[1];
                        parameter[p.split("=")[0]] = p.split("=")[1];
                        sourceUrl = sourceUrl.split("?")[0];
                    }
                    parameter.pageIndex = pageIndex;
                    parameter.pageSize = pageSize;

                    // 处理排序
                    if (orderBy != null && orderBy != undefined && orderBy != "") {
                        parameter.orderBy = orderBy + ' ' + (orderingRule.trim().toUpperCase() == "ASC" ? "ASC " : "DESC ");
                    }
                    ;
                    $.post(sourceUrl, parameter, function (data) {
                        if (dataType != "json") {
                            data.dataList = JSON.parse(data.dataList);
                        }

                        dataSource = data;

                        me.buildBaseDom();

                        //生成主体部分
                        me.JsRenderBody(tmplEngine, me, dataSource, templateId);
                        //生成分页部分
                        if (isPage) {
                            me.JsRenderPaging(tmplEngine, me, dataSource, isPage, pageSize, pageIndex, sourceUrl, method, templateId, funCallback, parameter, pagingCallback, dataType);
                        }
                        //绑定排序
                        fnTableBindSort(tmplEngine, me, dataSource, isPage, pageSize, pageIndex, sourceUrl, method, templateId, funCallback, orderingRule, dataType, parameter);

                        //排序标志
                        $(me).parent().find("thead th").each(function () {
                            var sort = $(this).attr("orderBy");
                            if (sort != null && sort != undefined) {
                                var str = $(this).text();
                                if (sort == orderBy && orderingRule == "ASC") {
                                    $(this).css("cursor", "pointer").html('<div class="sortUp">' + str + '</div>');
                                } else if (sort == orderBy && orderingRule == "DESC") {
                                    $(this).css("cursor", "pointer").html('<div class="sortDown">' + str + '</div>');
                                } else {
                                    $(this).css("cursor", "pointer").html('<div class="sort">' + str + '</div>');
                                }
                            }
                        });

                        //执行回调
                        if (funCallback != null && funCallback != undefined)
                            funCallback(data, pageIndex, pageSize);
                    });
                    break;
                default:
                    break;
            }
        }
        // 如果请求地址为空数据源不为空
        else {
            if (dataSource != null && dataSource != undefined) {
                if ($(me).parent().find("tbody").length == 0)
                    $(me).html("<div id='listbody'></div><div id='list-page' class='fn-clear'></div>");
                me.JsRenderBody(tmplEngine, me, dataSource, templateId);
                if (isPage) {
                    me.JsRenderPaging(tmplEngine, me, dataSource, isPage, pageSize, pageIndex, sourceUrl, method, templateId, funCallback, parameter, pagingCallback, dataType);
                }
            }
        }

    };

    // 生成基础的Dom框架
    $.fn.buildBaseDom = function () {
        if ($(me).parent().find("tbody").length == 0) {
            if ($(me).find("#listbody").length == 0) {
                $(me).append("<div id='listbody' class='ln-pic-list fn-clear'></div>");
            }
            if ($(me).find("#list-page").length == 0) {
                $(me).append("<div id='list-page' class='fn-clear'></div>");
            }
        } else {
            $(me).parent().find("tfoot").html("<tr><td colspan='" + $(me).parent().find("thead tr th").length + "'><div id='list-page' class='fn-clear clearfix'></div></td></tr>");
        }
    }

    //生成主体部分
    $.fn.JsRenderBody = function (tmplEngine, obj, dataS, datatemplateId) {
        if (dataS.dataList.length > 0) {
            if ($(obj).parent().find("tbody").length == 0) {
                var html = '';
                switch (tmplEngine) {
                    case 1:
                        html = $("#" + datatemplateId).render(dataS.dataList);
                        break;
                    case 2:
                        var tpl = document.getElementById(datatemplateId).innerHTML;
                        laytpl(tpl).render(dataS.dataList, function (data) {
                            html = data;
                        });
                        break;
                    case 3:
                        html = template(datatemplateId, dataS);
                        break;
                    default:
                        break;
                }
                ;
                $(obj).find("#listbody").eq(0).html(html);
                //$(obj).find("#listbody").eq(0).html($("#" + datatemplateId).render(dataS.dataList));
                //鼠标悬浮事件
                $(obj).find("div#listbody").eq(0).append('<div class="clear"></div>');
                //$(obj).find("ul#listbody").eq(0).append('<li class="clear"></li>');
                $(obj).find("#listbody dl").bind("mouseover", function () {
                    $(this).find(".Doing").show();
                }).bind("mouseleave", function () {
                    $(this).find(".Doing").hide();
                });
            } else {
                // var tmpl = $.templates("#" + datatemplateId);
                // var html = tmpl.render(dataS.dataList);
                // $(obj).html(html);
                var html = '';
                switch (tmplEngine) {
                    case 1:
                        html = $("#" + datatemplateId).render(dataS.dataList);
                        break;
                    case 2:
                        // html = laytpl($("#" + datatemplateId).innerHTML()).render(dataS.dataList);
                        var tpl = document.getElementById(datatemplateId).innerHTML;
                        laytpl(tpl).render(dataS.dataList, function (data) {
                            html = data;
                        });
                        break;
                    case 3:
                        html = template(datatemplateId, dataS);
                        break;
                    default:
                        break;
                }
                ;
                $(obj).html(html);
            }
        } else {
            if ($(obj).parent().find("tbody").length == 0) {
                $(obj).find("#listbody").eq(0).html("<div class='tc c38 line_h30'>暂无数据</div>");
            } else {
                $(obj).parent().find("tbody").eq(0).html("<tr><td colspan='" + $(obj).parent().find("thead tr th").length + "'><div class='tc c38 line_h30'>暂无数据</div></td></tr>");
            }
        }
    };

    //公共的分页部分
    $.fn.PageHtml = function (recordCount, pageCount, pageSize, pageIndex) {
        var html = '';
        html += '<div class="num pull-left"><span class="pull-left">每页显示：</span><ul class="chose-num pull-left"><li><span>10</span></li><li><span>25</span></li><li><span>50</span></li><li><span>100</span></li></ul></div>';
        pageCount = pageCount == 0 ? 1 : pageCount;
        if (parseInt(pageIndex) > pageCount)
            pageIndex = pageCount;
        html += '<div class="pull-right"><div class="total pull-left">共<strong>' + recordCount + '</strong>条记录</div>';


        if (parseInt(pageIndex) == 1) {
            html += "<ul class='pagination pull-left' style='margin:-5px 0;'><li class='first status_disabled '><a>首页</a></li>";
            html += "<li class='previous disabled'><span class='previous status_disabled'><</span></li>";
        } else {
            html += "<ul  class='pagination pull-left' style='margin:-5px 0;'><li  ><a class='first status-default pull-left' index='first' href=\"#1\">首页</a></li>";
            html += "<li ><a index='previous' class='previous' href=\"#" + (pageIndex - 1) + "\"><</a></li>";
        }
        if (true) {
            var maxIndex = 1; //当前要显示的最大索引
            if (pageCount <= 5)
                maxIndex = pageCount;
            else if (parseInt(pageIndex) + 2 <= 5)
                maxIndex = 5;
            else if (parseInt(pageIndex) + 2 >= pageCount)
                maxIndex = pageCount;
            else
                maxIndex = parseInt(pageIndex) + 2;

            for (var i = 4; i >= 0; i--) {
                if (maxIndex - i == parseInt(pageIndex))
                    html += "<li class='active status_disabled status-on'><a>" + (maxIndex - i) + "</a></li>";
                else if (maxIndex - i > 0)
                    html += "<li><a index='" + (maxIndex - i) + "' class='status-default' href='#" + (maxIndex - i) + "'>&nbsp;" + (maxIndex - i) + "&nbsp;</a></li>";
            }
        }
        if (pageCount == parseInt(pageIndex)) {
            html += "<li class='previous disabled'><a class='previous status_disabled'>></a><li>";
            html += "<li><a class='last status_disabled '>末页</a></li></ul>";
        } else {
            html += "<li><a index='next' class='next status-default' href=\"#" + (parseInt(pageIndex, 10) + 1) + "\">></a><li>";
            html += "<li><a index='last' class='last status-default pull-left' href=\"#" + pageCount + "\">&nbsp;末页&nbsp;</a></li></ul>";
        }
        //html += '<span class="c38">第' + pageIndex + '页/共' + pageCount + '页(共' + recordCount + '条)</span>';
        html += '<div class="pagination jump pull-left">去第&nbsp;<input type="text" id="txt-page-goto" class="form-control" style="width:35px;display:inline-block;height:25px" />&nbsp;页&nbsp;&nbsp;<a class="btn btn-sm btn-primary" id="btn-page-goto">跳转</a></div></div>';
        return html;
    };

    //生成分页部分
    $.fn.JsRenderPaging = function (tmplEngine, obj, dataS, isPage, pageSize, pageIndex, sourceUrl, method, datatemplateId, funCallback, parameter, pagingCallback, dataType) {
        var recordCount = parseInt(dataS.recordCount || 0);
        var pageCount = Math.ceil(recordCount / pageSize);
        var html = this.PageHtml(recordCount, pageCount, pageSize, pageIndex);
        if ($(obj).parent().find("tfoot").length == 0) {
            $(obj).find("#list-page").eq(0).show().html(html);
        } else {
            $(obj).parent().find("#list-page").eq(0).show().html(html);
        }

        //翻页需要存储数据到table的属性中
        this.setListPageLink(tmplEngine, sourceUrl, method, funCallback, isPage, pageSize, datatemplateId, parameter, pagingCallback, dataType); //设置分页的链接

        this.setListGotoPageLink(tmplEngine, sourceUrl, method, funCallback, isPage, pageSize, datatemplateId, pageCount, parameter, pagingCallback, dataType); //设置'转向'分页的事件

        this.setPageSizeLink(tmplEngine, sourceUrl, method, funCallback, isPage, pageSize, pageIndex, datatemplateId, parameter, pagingCallback, dataType);
        return this;
    };

    // 设置分页大小链接
    $.fn.setPageSizeLink = function (tmplEngine, sourceUrl, method, funCallback, isPage, pageSize, pageIndex, templateId, parameter, pagingCallback, dataType) {
        var me = this;
        $(me).parent().find(".num li").unbind("click").bind("click", function () {
            var lnk = $(this);
            var newPageSize = lnk.text();
            lnk.siblings().removeClass("active");
            lnk.addClass("active");
            me.JsRenderData({
                sourceUrl: sourceUrl,
                tmplEngine: tmplEngine,
                pageIndex: 1,
                pageSize: newPageSize,
                isPage: isPage,
                templateId: templateId,
                funCallback: funCallback,
                parameter: parameter,
                method: method,
                pagingCallback: pagingCallback,
                dataType: dataType
            });
            if ($.isFunction(pagingCallback)) {
                pagingCallback(1, newPageSize);
            }
        });
        $(me).parent().find(".num li").each(function (idx, item) {
            if ($(item).text() == pageSize) {
                $(item).addClass("active");
            }
        });
    };

    //设置分页连接
    $.fn.setListPageLink = function (tmplEngine, sourceUrl, method, funCallback, isPage, pageSize, templateId, parameter, pagingCallback, dataType) {
        var me = this;
        $(me).parent().find("#list-page a").unbind("click").click(function () {
            var thisPageIndex = $(this).attr("href");
            debugger;
            thisPageIndex = thisPageIndex.slice(thisPageIndex.lastIndexOf("#") + 1);
            me.JsRenderData({
                tmplEngine: tmplEngine,
                sourceUrl: sourceUrl,
                pageIndex: thisPageIndex,
                pageSize: pageSize,
                isPage: isPage,
                templateId: templateId,
                funCallback: funCallback,
                parameter: parameter,
                pagingCallback: pagingCallback,
                method: method,
                dataType: dataType
            });
            if ($.isFunction(pagingCallback)) {
                pagingCallback(thisPageIndex, pageSize);
            }
        });
        return me;
    };

    //设置转向连接
    $.fn.setListGotoPageLink = function (tmplEngine, sourceUrl, method, funCallback, isPage, pageSize, templateId, pageCount, parameter, pagingCallback, dataType) {
        var me = this;
        $(me).parent().find("#list-page #btn-page-goto").unbind("click").click(function () {
            var o = $(me).parent().find("#list-page #txt-page-goto").eq(0);
            if (o.val() != "") {
                var pattern = /^[0-9]+$/;
                if (pattern.test(o.val())) {
                    var thisGotoIndex = parseInt(o.val());
                    if (thisGotoIndex <= pageCount && thisGotoIndex > 0) {
                        me.JsRenderData({
                            tmplEngine: tmplEngine,
                            sourceUrl: sourceUrl,
                            pageIndex: thisGotoIndex,
                            pageSize: pageSize,
                            isPage: isPage,
                            templateId: templateId,
                            funCallback: funCallback,
                            parameter: parameter,
                            method: method,
                            dataType: dataType
                        });
                        if ($.isFunction(pagingCallback)) {
                            pagingCallback(thisGotoIndex, pageSize);
                        }
                    }
                }
            }
        });
        return me;
    };

    // 绑定排序
    function fnTableBindSort(tmplEngine, me, data, isPage, pageSize, pageIndex, sourceUrl, method, templateId, funCallback, orderingRule, dataType, parameter) {
        $(me).parent().find('th').each(function () {
            var sort = $(this).attr("orderBy");
            if (sort != null && sort != undefined) {
                $(this).attr("sort", $(this).attr("sort").trim() == "asc" ? "desc" : "asc");
                $(this).unbind("click");
                $(this).bind("click", function () {

                    // sourceUrl = sourceUrl.replace(/&?(orderBy|orderingRule)=([^&]*)/gi, ""); //过滤url的排序参数
                    // sourceUrl += "&orderBy=" + sort + ' ' + $(this).attr("sort");

                    orderBy = $(this).attr("orderBy").trim();
                    orderingRule = ($(this).attr("sort").trim() == "asc" ? "desc" : "asc").toUpperCase();

                    me.JsRenderData({
                        tmplEngine: tmplEngine,
                        sourceUrl: sourceUrl,
                        pageIndex: pageIndex,
                        pageSize: pageSize,
                        isPage: isPage,
                        templateId: templateId,
                        funCallback: funCallback,
                        method: method,
                        dataType: dataType,
                        parameter: parameter,
                        orderBy: orderBy,
                        orderingRule: orderingRule
                    });

                    // var removestr = $(this).attr("sort") == "desc" ? "do-down" : "do-up";
                    // var addstr = $(this).attr("sort") == "desc" ? "do-up" : "do-down";
                    // $(this).find(".sort-do").eq(0).removeClass(removestr).addClass(addstr);

                    // var removestr = $(this).attr("sort") == "desc" ? "sortDown" : "sortUp";
                    // var addstr = $(this).attr("sort") == "desc" ? "sortUp" : "sortDown";
                    // $(this).children("div.sort").removeClass(removestr).addClass(addstr).removeClass('sort');
                });
            }
        });
    };

})(jQuery);