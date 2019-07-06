/**
 * ######公共方法类######
 */

/**
 * 对象深拷贝(对象属性包含function)
 * @param obj
 * @returns {*}
 */
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

/**
 * 判断是否为空
 * @param value
 * @returns {boolean}
 */
function isEmpty(value) {
    return value === undefined || value === null || value === '' || value.length === 0;
}

/**
 * 判断非空
 * @param value
 * @returns {boolean}
 */
function isNotEmpty(value) {
    return !isEmpty(value);
}

/**
 * layer加载层调用 layer.load(2, {shade: [0.3, '#fff']});
 * @param type 类型1、2等 请参考layer官方文档
 */
function layerLoading(type) {
    if (isEmpty(type)) type = 2;
    layer.load(type, {shade: [0.3, '#fff']});
}

/**
 * 同步获取页面内容
 * @param url 页面文件相对根目录的路径 例：pages/login/login.html
 * @returns {*}
 */
function loadPage(url) {
    var returnData = "";
    $.ajax({
        url: url,
        global: false, //是否触发全局AJAX事件
        type: "GET",
        async: false, //同步请求
        success: function (data) {
            returnData = data;
        }
    });
    return returnData;
}
