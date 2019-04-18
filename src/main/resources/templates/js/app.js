// 协议 http: https:
function getProtocol() {
    return window.location.protocol;
}

// IP 127.0.0.1
function getHost() {
    return window.location.host;
}

// 项目访问根路径 /springBootDemo
function getPathName() {
    var pathName = window.location.pathname;
    return pathName.substring(0, pathName.substr(1).indexOf("/") + 1);
}

// 访问地址 https://127.0.0.1/springBootDemo
function getProjectPath() {
    return getProtocol() + "//" + getHost() + getPathName();
}

// 请求service层服务
function requestService(serviceName, funcName) {
    var argsCount = arguments.length;
    if (argsCount < 2) {
        alert('参数个数错误');
        return false;
    }
    var callback = undefined;
    var url = getProjectPath() + '/service/' + serviceName + '/' + funcName;
    var params = [];
    var data = undefined;
    for (var i = 2; i < argsCount; i++) {
        var arg = arguments[i];
        if (i === argsCount - 1 && typeof (arg) === 'function') {
            // 最后一个参数为 callback 回调
            callback = arg;
        } else {
            params.push(arg);
        }
    }
    // 请求参数封装
    if (params.length > 0) {
        data = {};
        for (var j = 0; j < params.length; j++) {
            data['arg' + j] = JSON.stringify(params[j]);
        }
    }
    // 发送请求
    sendPost(url, data, callback);
}

// 发送POST请求
function sendPost(url, data, callback) {
    sendAjax(url, 'POST', data, callback);
}

// 发送GET请求
function sendGet(url, data, callback) {
    sendAjax(url, 'GET', data, callback);
}

// Ajax POST请求
function sendAjax(url, method, data, callback) {
    // jquery ajax
    $.ajax({
        url: url, // 请求地址
        type: method, // 请求方式
        data: data, // 发送到服务器的数据 将自动转换为请求字符串格式
        processData: true, // 默认true 默认情况下，通过data选项传递进来的数据，如果是一个对象(技术上讲只要不是字符串)，都会处理转化成一个查询字符串，以配合默认内容类型 "application/x-www-form-urlencoded"。如果要发送 DOM 树信息或其它不希望转换的信息，请设置为 false
        dataType: 'text', // 预期服务器返回的数据类型 "json": 返回JSON数据, "text": 返回纯文本字符串
        async: true, // 默认值: true 默认设置下，所有请求均为异步请求。如果需要发送同步请求，请将此选项设置为 false
        cache: false, // 默认值: true 设置为false将不缓存此页面
        timeout: 10000, // 设置请求超时时间（毫秒）
        beforeSend: function (xhr) {
        }, // 发送请求前可修改XMLHttpRequest对象的函数，如添加自定义HTTP头
        success: function (data, status, xhr) {
            // var errorCode = parseInt(xhr.getResponseHeader('error_code'));
            if (typeof (callback) === 'function') {
                var resultData = undefined;
                if (data) {
                    resultData = JSON.parse(data);
                }
                callback(resultData);
            }
        },
        error: function (xhr, status, exception) {
            if (status === 'timeout') {
                alert("error:响应超时");
                console.log("error:响应超时", xhr, status, exception);
            } else {
                alert("error:后台错误");
                console.log("error:后台错误", xhr, status, exception);
            }
        }
    })

    // angularjs $http
    /*$http({
        // 请求地址
        url: url,
        // 请求方式
        method: method,
        // 请求的数据 message 必须是a=b&c=d的格式 data是post请求的数据，params是get请求的数据
        data: data,
        // 请求头 post要加上请求头 默认Content-Type:application/json;charset=UTF-8，post后台接受不到json数据，转为表单提交
        headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8', 'Accept': '*!/!*'},
        // 是否启用缓存HTTP请求 默认false
        cache: cache,
        // 超时时间 毫秒
        timeout: 10000
    }).success(function (data, status, headers, config) {
        if (typeof (callback) === 'function') {
            callback(data);
        }
    }).error(function (data, status, headers, config) {
        if (0 === status) {
            alert("error:响应超时");
            console.log("error:响应超时", data, status, headers);
        } else {
            alert("error:后台错误");
            console.log("error:后台错误", data, status, headers);
        }
    })*/
}
