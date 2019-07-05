/**
 * ######系统服务方法类######
 */

/**
 * 协议 http: https:
 * @returns {string}
 */
function getProtocol() {
    return window.location.protocol;
}

/**
 * IP 127.0.0.1
 * @returns {string}
 */
function getHost() {
    return window.location.host;
}

/**
 * 项目访问根路径 /springBootDemo
 * @returns {string}
 */
function getPathName() {
    var pathName = window.location.pathname;
    return pathName.substring(0, pathName.substr(1).indexOf("/") + 1);
}

/**
 * 访问地址 https://127.0.0.1
 * @returns {string}
 */
function getProjectPath() {
    return getProtocol() + "//" + getHost();
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
    var data = {'params': JSON.stringify(params)};

    // 发送请求
    sendPost(url, data, callback);
}

/**
 * login登录服务
 * @param username
 * @param password
 * @param rememberMe
 * @param callback
 */
function loginService(username, password, rememberMe, callback) {
    var data = {'username': username, 'password': password, 'rememberMe': rememberMe};
    var url = getProjectPath() + '/login';
    // POST请求
    sendPost(url, data, callback);
}

/**
 * 发送POST请求
 * @param url
 * @param data
 * @param callback
 */
function sendPost(url, data, callback) {
    sendAjax(url, 'POST', data, callback);
}

/**
 * 发送GET请求
 * @param url
 * @param data
 * @param callback
 */
function sendGet(url, data, callback) {
    sendAjax(url, 'GET', data, callback);
}

/**
 * 发送Ajax请求
 * @param url
 * @param method
 * @param data
 * @param callback
 */
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
        timeout: 60000, // 设置请求超时时间（毫秒）
        beforeSend: function (xhr) {
        }, // 发送请求前可修改XMLHttpRequest对象的函数，如添加自定义HTTP头
        success: function (data, status, xhr) {
            handleReturnSuccess(JSON.parse(data), callback);
        },
        error: function (xhr, status, exception) {
            handleReturnError(xhr, status, exception);
        }
    })
}

/**
 * 上传文件
 * @param divId         必选 divId
 * @param serviceName   必选 服务bean名称
 * @param funcName      必选 方法名
 * @param options       可选 设置属性
 * @param callback      可选 回调函数获取上传文件存放路径
 */
function uploadFile(divId, serviceName, funcName) {
    // 参数处理
    var argsCount = arguments.length;
    if (argsCount < 3 || argsCount > 5) {
        layer.alert('uploadFile参数个数错误', {icon: 2});
        return false;
    }
    var url = getProjectPath() + '/upload/' + serviceName + '/' + funcName;
    var options = undefined;
    var callback = undefined;
    if (argsCount === 4) {
        if (typeof arguments[3] === 'function') {
            callback = arguments[3];
        } else {
            options = arguments[3];
        }
    }
    if (argsCount === 5) {
        if (typeof arguments[3] === 'function' || typeof arguments[4] !== 'function') {
            layer.alert('uploadFile参数类型错误', {icon: 2});
            return false;
        }
        options = arguments[3];
        callback = arguments[4];
    }

    // 生成 form 表单
    var formId = divId + '-upload-form';
    var formHtml = '<form id="' + formId + '" enctype="multipart/form-data"> <input type="file" name="file"> <input type="submit" value="上传"> </form>';
    // 上传进度条
    var uploadProgress =
        '<div class="upload-file-stateWrap hidden">' +
        '<div class="progress">' +
        '<div class="progress-bar" role="progressbar" aria-valuemin="0" aria-valuemax="100" style="width: 0;">' +
        '<span class="progress-bar-status">0%</span>' +
        '</div>' +
        '</div>' +
        '</div>';

    // 将html代码插入到指定div中
    document.getElementById(divId).innerHTML = formHtml + uploadProgress;

    // 上传进度条相关
    var progressWrap = $('.upload-file-stateWrap');
    var progress = $(".progress-bar");
    var status = $(".progress-bar-status");
    var percentVal = '0%';
    // 判断是否显示进度条
    var showProgress = true; // 默认显示
    if (options && options.progress === false) showProgress = false;

    // 默认配置
    var defaultOptions = {
        url: url, // form提交数据的地址
        type: 'post', // form提交的方式(method:post/get)
        resetForm: true, // 提交成功后是否重置表单中的字段值，即恢复到页面加载时的状态
        timeout: 0, // 设置请求时间，超过该时间后，自动退出请求，单位(毫秒) (0永不超时)
        beforeSubmit: function (form) {
            for (var i = 0; i < form.length; i++) {
                var file = form[i].value;
                if (!file) {
                    layer.msg('请选择文件', {icon: 2});
                    return false;
                }
                console.log('开始上传文件: name:' + file.name + ', type:' + file.type + ', size:' + Math.round(file.size / 1024) + 'KB');
            }
            layer.msg('开始上传..请勿关闭当前页面');
        }, // 提交前执行的回调函数
        beforeSend: function () {
            // 进度条样式
            if (showProgress) {
                progressWrap.removeClass("hidden");
                progress.width(percentVal);
                status.html(percentVal);
            }
        },
        uploadProgress: function (event, position, total, percentComplete) {
            if (showProgress) {
                percentVal = percentComplete + '%';
                progress.width(percentVal);
                status.html(percentVal);
            }
        }, // 上传过程中 position:已经上传完成的字节数 total:总字节数 percentComplete:已完成的比例
        success: function (data) {
            if (showProgress) progressWrap.addClass("hidden"); //隐藏进度条
            handleReturnSuccess(JSON.parse(data), callback);
        }, // 上传成功后执行的回调函数
        error: function (xhr, status, error) {
            if (showProgress) progressWrap.addClass("hidden"); //隐藏进度条
            handleReturnError(xhr, status, error)
        } // 上传失败执行的回调函数
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

    // ajaxForm会自动阻止提交
    $('#' + formId).ajaxForm(options);
}

/**
 * 后台返回成功处理
 * @param data
 * @param callback
 * @returns {boolean}
 */
function handleReturnSuccess(data, callback) {
    layer.closeAll('loading'); //关闭所有layer loading
    if (data) {
        if (data.code !== 0) {
            layer.alert('错误: ' + data.data, {icon: 2});
            return false;
        } else {
            if (typeof callback === 'function') {
                callback(data.data);
            }
        }
    } else {
        layer.alert('错误: 无响应数据', {icon: 2});
        return false;
    }
}

/**
 * 后台返回失败处理
 * @param xhr
 * @param status
 * @param error
 */
function handleReturnError(xhr, status, error) {
    layer.closeAll('loading'); //关闭所有layer loading
    if (status === 'timeout') {
        console.log("error: 响应超时", xhr, status, error);
        layer.alert("error: 响应超时 - " + status, {icon: 2});
    } else if (xhr && xhr.status === 403) {
        console.log("error: 登录失效，需要重新登录", xhr, status, error);
        layer.confirm("error: 登录失效，是否重新登录", {icon: 2}, function (index) {
            window.open(getProjectPath());
            layer.close(index);
        })
    } else {
        try {
            var responseText = JSON.parse(xhr.responseText);
            console.log("error: 错误", xhr, status, error);
            layer.alert("error: 错误(status: " + responseText.status + ") - " + responseText.message, {icon: 2});
        } catch (err) {
            console.log("error: 错误", xhr, status, error);
            layer.alert("error: 错误 - " + err, {icon: 2});
        }
    }
}

/**
 * 下载(导出)文件
 * @param filePath  必须 文件路径
 */
function downloadFile(filePath) {
    // 编码处理
    filePath = Base64.encode(encodeURIComponent(filePath));
    // 后台处理url
    var url = getProjectPath() + '/download?file=' + filePath;
    // input属性传递filePath参数
    // var input = '<input type="hidden" name="file" value="' + filePath + '"/>';
    var input = '<input type="hidden"/>'; // 使用get方式传参

    // form表单
    var form = $('<form>');
    form.attr('action', url);
    form.attr('method', 'post');
    form.attr('target', '_blank'); // 新页面中打开下载对话框，为''则默认当前页面
    form.attr('style', 'display:none');

    // input插入到form表单中
    form.html(input);
    // form表单append到指定div中
    $('body').append(form);

    // 提交表单
    form.submit().remove();
}
