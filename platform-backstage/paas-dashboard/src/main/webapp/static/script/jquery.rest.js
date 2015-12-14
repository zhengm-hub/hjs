"use strict";
/**
 * JQuery Rest Client Plugin v1.3 https://git.oschina.net/anylain/RestClient
 *
 * Copyright 2014 Pan Ying Released under the Apache 2.0 License
 */
(function ($) {

    $.RestClient = function (options) {

        var version = '1.3';
        var currOptions = null;
        var _this = this;

        // 获取当前版本
        this.getVersion = function () {
            return version;
        };

        // 替换字符串中的 {key} 内容, 一个简单的小模板引擎
        var _gsub = function (source, pattern, replacement) {
            var result = '', match;
            while (source.length > 0) {
                match = source.match(pattern);
                if (match) {
                    result += source.slice(0, match.index);
                    result += replacement(match).toString();
                    source = source.slice(match.index + match[0].length);
                } else {
                    result += source;
                    source = '';
                }
            }
            return result;
        };

        // 克隆对象, 这个方法只能克隆简单对象
        this._clone = function (obj) {
            return $.extend(true, {}, obj);
        };

        // 更新 或者 叠加 options
        this._updateOptions = function (source, newValue, force) {
            var result;
            if (source === undefined) {
                result = newValue;
            } else if (source !== null && $.type(source) === 'object') {
                result = {};
                for (var key in source) {
                    if (!source.hasOwnProperty(key))
                        continue;
                    result[key] = source[key];
                }
                for (var key in newValue) {
                    if (!newValue.hasOwnProperty(key))
                        continue;
                    result[key] = this._updateOptions(source[key],
                        newValue[key], force);
                }
            } else if (force) {
                result = newValue;
            } else {
                result = source;
            }
            return result;
        };

        // 兼容性处理,当浏览器不支持 HTTP 1.1 进行的一些方法替换方案
        this._compatibleHandler = function (method, request) {
            if (!request.compatible) {
                request.type = method;
                return;
            }

            if ($.isFunction(request.compatible)) {
                request.compatible(method, request);
                return;
            }

            switch (request.compatible) {
                case 'url':
                    request.type = 'POST';
                    request.urlParams = request.urlParams || {};
                    request.urlParams['__method'] = method;
                    break;

                case 'x-method':
                    request.type = 'POST';
                    request.headers = request.headers || {};
                    request.headers['x-method-override'] = method;
                    break;

                default:
                    throw new Error('Unsupport rest compatible mode "'
                        + request.compatible + '"');
            }
        };

        // 更新当前配置
        this.updateOptions = function (newOptions) {
            currOptions = this._updateOptions(currOptions, newOptions, true);
            return this;
        };

        // 使用新配置替换当前配置
        this.setOptions = function (newOptions) {
            currOptions = newOptions;
            return this;
        };

        // 获取当前配置
        this.getOptions = function () {
            return currOptions;
        };

        // 构造请求地址
        this.buildUrl = function (sourceUrl, urlParams, pathParams, queryParams,
                                  ext) {
            pathParams = $.extend(true, {}, urlParams, pathParams);
            var usedPathParams = [];
            var result = !sourceUrl ? "" : _gsub(sourceUrl, /\{(.+?)\}/,
                function (match) {
                    var key = match[1];
                    var value = pathParams[key];
                    if (value === undefined || value === null) {
                        $.error("Can't found '" + key
                            + "' from pathParams or urlParams.");
                    }
                    usedPathParams.push(key);
                    return encodeURIComponent(value);
                });

            if (ext) {
                result += ext;
            }

            queryParams = queryParams ? this._clone(queryParams) : {};
            if (urlParams) {
                $.each(urlParams, function (key, value) {
                    if ($.inArray(key, usedPathParams) === -1
                        && queryParams[key] === undefined) {
                        queryParams[key] = value;
                    }
                });
            }
            var queryString = this.buildQueryString(queryParams);
            if (queryString) {
                result += '?' + queryString;
            }

            return result;
        };

        // 构造queryString
        this.buildQueryString = function (queryParams) {
            return $.param(queryParams);
        };

        // 构造请求对象, 该对象可以直接用于$.ajax方法
        // 该方法在构造失败时会调用$.error抛出异常
        this.buildRequest = function (method, request, addon) {
            var result = {};

            result = this._clone(request);

            if (addon) {
                result = this._updateOptions(result, addon, true);
            }

            result = this._updateOptions(result, currOptions);

            var successHandler = result.success;
            var errorHandler = result.error;

            // 序列化请求体
            if (result.serializeRequestBody) {
                try {
                    result.data = result.serializeRequestBody(result);
                } catch (e) {
                    $.error('Serialize request body fault. ' + e.message);
                }
            }

            // 请求成功回调包装方法, 会尝试反序列化返回体
            result.success = function (data, textStatus, jqXHR) {
                if (result.deserializeResponseBody) {
                    try {
                        data = result.deserializeResponseBody(data, result);
                    } catch (e) {
                        $
                            .error('Deserialize response body fault. '
                            + e.message);
                    }
                }
                if (successHandler)
                    successHandler(data, textStatus, jqXHR);
            };

            // 请求失败回调包装方法, 会尝试反序列化返回体
            result.error = function (jqXHR, textStatus, errorThrown) {
                var message;
                if (result.deserializeError) {
                    message = result.deserializeError(jqXHR, textStatus,
                        errorThrown, result);
                } else {
                    message = (jqXHR && jqXHR.responseText) || errorThrown
                        || textStatus;
                }
                if (errorHandler) {
                    errorHandler(message, jqXHR, textStatus, errorThrown);
                }
            };

            // 构造请求地址
            result.url = this.buildUrl(result.url, result.urlParams,
                result.pathParams, result.queryParams, result.ext);
            if (result.baseUrl) {
                result.url = result.baseUrl + result.url;
            }

            // 兼容性处理
            this._compatibleHandler(method, result);

            return result;
        };

        // 立刻发送一个请求, 该请求会捕获构造期异常, 转而调用请求失败回调方法
        this.sendRequest = function (method, request, addon) {

            try {
                var r = this.buildRequest(method, request, addon);
                return $.ajax(r);
            } catch (e) {
                var tryCallComplete = function () {
                    var completeHandler = (addon && addon.complete)
                        || (request && request.complete)
                        || _this.getOptions().complete;
                    if (completeHandler)
                        completeHandler();
                };
                var errorHandler = (addon && addon.error)
                    || (request && request.error)
                    || _this.getOptions().error;
                if (errorHandler) {
                    errorHandler(e.message, null, null, e);
                    tryCallComplete();
                } else {
                    $.error(e.message);
                }
            }
        };

        this.get = function (request, addon) {
            return this.sendRequest('GET', request, addon);
        };

        this.post = function (request, addon) {
            return this.sendRequest('POST', request, addon);
        };

        this.put = function (request, addon) {
            return this.sendRequest('PUT', request, addon);
        };

        this.del = function (request, addon) {
            return this.sendRequest('DELETE', request, addon);
        };

        this.setOptions(options || $.RestClient.options.defaults);

    };

    // 配置模板容器
    $.RestClient.options = {};

    // json 默认配置模板
    $.RestClient.options.json = {
        baseUrl: null,
        compatible: null,
        dataType: "json",
        contentType: "application/json",
        headers: {
            accept: "application/json"
        },
        // ext: ".json", // 资源后缀，如果目标API以URL后缀区分资源类型可以加上这个参数
        serializeRequestBody: function (request) {
            if (JSON && JSON.stringify) {
                return JSON.stringify(request.data);
            } else if ($.toJSON) {
                return $.toJSON(request.data);
            }
            throw new Error(
                'Need jQuery json plugin to serialize request body.');
        },
        deserializeResponseBody: null,
        deserializeError: null,
        // 默认的错误处理
        error: function (message, jqXHR, textStatus, errorThrown) {
            alert(message);
        }

    };

    // xml默认配置模板
    $.RestClient.options.xml = {
        dataType: "xml",
        headers: {
            accept: "application/xml"
        },
        // ext: ".xml",
        contentType: "application/xml"
    };

    // 文本类型默认配置模板
    $.RestClient.options.text = {
        dataType: "text",
        headers: {
            accept: "text/plain"
        }
    };

    // 使用 json 默认配置
    $.RestClient.options.defaults = $.RestClient.options.json;

    // 初始化默认实例
    $.rest = new $.RestClient();

})(jQuery);