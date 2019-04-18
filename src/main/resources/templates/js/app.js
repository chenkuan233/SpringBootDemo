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
