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

/**
 * 请求service层服务
 * @param serviceName   必选 服务bean名称
 * @param funcName      必选 方法名
 * @param params        可选 参数，可以有多个 param1,param2...
 * @param callback      可选 回调函数
 */
function requestService(serviceName, funcName) {
    var argsCount = arguments.length;
    if (argsCount < 2) {
        layer.alert('requestService参数个数错误', {icon: 2});
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

// Ajax 请求
function sendAjax(url, method, data, callback) {
    // jquery ajax
    $.ajax({
        url: url, // 请求地址
        type: method, // 请求方式
        data: data, // 发送到服务器的数据 将自动转换为请求字符串格式
        processData: true, // 默认true 默认情况下，通过data选项传递进来的数据，如果是一个对象(技术上讲只要不是字符串)，都会处理转化成一个查询字符串，以配合默认内容类型 "application/x-www-form-urlencoded"。如果要发送 DOM 树信息或其它不希望转换的信息，请设置为 false
        dataType: 'json', // 预期服务器返回的数据类型 "json": 返回JSON数据, "text": 返回纯文本字符串
        async: true, // 默认值: true 默认设置下，所有请求均为异步请求。如果需要发送同步请求，请将此选项设置为 false
        cache: false, // 默认值: true 设置为false将不缓存此页面
        timeout: 60000, // 设置请求超时时间（毫秒）
        beforeSend: function (xhr) {
        }, // 发送请求前可修改XMLHttpRequest对象的函数，如添加自定义HTTP头
        success: function (data, status, xhr) {
            if (data) {
                if (data.code !== 0) {
                    layer.alert(data.data, {icon: 2});
                    return false;
                } else {
                    if (typeof callback === 'function') {
                        callback(data.data);
                    }
                }
            } else {
                layer.alert('发生错误: 系统无响应数据', {icon: 2});
                return false;
            }
        },
        error: function (xhr, status, exception) {
            if (status === 'timeout') {
                layer.alert("error:响应超时", {icon: 2});
                console.log("error:响应超时", xhr, status, exception);
            } else {
                layer.alert("error:后台错误", {icon: 2});
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
            layer.alert("error:响应超时", {icon: 2});
            console.log("error:响应超时", data, status, headers);
        } else {
            layer.alert("error:后台错误", {icon: 2});
            console.log("error:后台错误", data, status, headers);
        }
    })*/
}

/**
 * 上传文件
 * @param divId     必选 divId
 * @param options   可选 设置属性
 * @param callback  可选 回调函数获取上传文件存放路径
 */
function uploadFile(divId) {
    var options = undefined;
    var callback = undefined;

    // 参数处理
    var argsCount = arguments.length;
    if (argsCount < 1 || argsCount > 3) {
        layer.alert('uploadFile参数个数错误', {icon: 2});
        return false;
    }
    if (argsCount === 2) {
        if (typeof (arguments[1]) === 'function') {
            callback = arguments[1];
        } else {
            options = arguments[1];
        }
    }
    if (argsCount === 3) {
        options = arguments[1];
        callback = arguments[2];
    }

    // 默认配置
    var url = getProjectPath() + '/upload';
    var defaultOptions = {
        url: url, // form提交数据的地址
        type: 'post', // form提交的方式(method:post/get)
        resetForm: true, // 提交成功后是否重置表单中的字段值，即恢复到页面加载时的状态
        timeout: 60000, // 设置请求时间，超过该时间后，自动退出请求，单位(毫秒)
        beforeSubmit: function (form) {
            for (var i = 0; i < form.length; i++) {
                var file = form[i].value;
                if (!file) {
                    layer.msg('请选择文件', {icon: 2});
                    return false;
                }
                console.log('开始上传文件: name:' + file.name + ', type:' + file.type + ', size:' + Math.round(file.size / 1024) + 'KB');
            }
        }, // 提交前执行的回调函数
        success: function (data) {
            if (data) {
                data = JSON.parse(data);
                if (data.code !== 0) {
                    layer.alert(data.data, {icon: 2});
                    return false;
                } else {
                    if (typeof callback === 'function') {
                        callback(data.data);
                    }
                }
            } else {
                layer.alert('发生错误: 系统无响应数据', {icon: 2});
                return false;
            }
        } // 提交成功后执行的回调函数
    };

    // 若没有传入相关配置则使用默认配置
    if (!options) {
        options = defaultOptions;
    } else {
        for (var key in defaultOptions) {
            if (isEmpty(options[key])) {
                options[key] = defaultOptions[key];
            }
        }
    }

    // 生成 form 表单
    var formId = divId + '-upload-form';
    var formHtml = '<form id="' + formId + '" enctype="multipart/form-data"> <input type="file" name="file"> <input type="submit" value="上传"> </form>';
    document.getElementById(divId).innerHTML = formHtml;

    // ajaxForm会自动阻止提交
    $('#' + formId).ajaxForm(options);
}

// 对象深拷贝(对象属性包含function)
function copyObject(obj) {
    if (obj === null) return null;
    if (typeof obj !== 'object') return obj;
    if (obj.constructor === Date) return new Date(obj);
    if (obj.constructor === RegExp) return new RegExp(obj);
    var newObj = new obj.constructor(); // 保持继承链
    for (var key in obj) {
        if (obj.hasOwnProperty(key)) { // 不遍历其原型链上的属性
            var val = obj[key];
            newObj[key] = typeof val === 'object' ? arguments.callee(val) : val; // 使用arguments.callee解除与函数名的耦合
        }
    }
    return newObj;
}

// 判断是否为空
function isEmpty(value) {
    return value === undefined || value === null || value === '' || value.length === 0;
}
