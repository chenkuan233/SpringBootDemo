/**
 * ######缓存数据操作类######
 * 使用localStorage持久化保存当前会话数据
 * localStorage在勾选rememberMe后登陆依然可获得userInfo
 * sessionStorage关闭浏览器后信息丢失
 */

var userInfoFlag = "userInfo";

/**
 * 保存缓存数据
 * @param key
 * @param value
 */
function setCache(key, value) {
    localStorage.setItem(key, JSON.stringify(value));
}

/**
 * 根据key获取缓存数据
 * @param key
 * @returns {any}
 */
function getCache(key) {
    return JSON.parse(localStorage.getItem(key));
}

/**
 * 根据key移除缓存数据
 * @param key
 */
function removeCache(key) {
    localStorage.removeItem(key);
}

/**
 * 清除所有缓存数据
 */
function clearAllCache() {
    localStorage.clear();
}

/**
 * 获取当前sessionStorage缓存长度
 * @returns {number}
 */
function getCacheLength() {
    return localStorage.length;
}

/**
 * 获取某个index索引的key
 * @param index
 * @returns {string | null}
 */
function getKeyByIndex(index) {
    return localStorage.key(index);
}

/**
 * 根据index索引获取cache数据
 * @param index
 * @returns {any}
 */
function getCacheByIndex(index) {
    var key = getKeyByIndex(index);
    return getCache(key);
}

/**
 * 获取所有key
 * @returns {Array}
 */
function getAllKeys() {
    var length = getCacheLength();
    var keys = [];
    for (var i = 0; i < length; i++) {
        keys[i] = getKeyByIndex(i);
    }
    return keys;
}

/**
 * 保存用户信息到缓存
 * @param userInfo
 */
function saveUserInfoCache(userInfo) {
    setCache(userInfoFlag, userInfo);
}

/**
 * 获取当前登陆用户信息
 * @returns {any}
 */
function getUserInfoCache() {
    return getCache(userInfoFlag);
}

/**
 * 获取当前登陆用户名称
 * @returns {*}
 */
function getUserNameCache() {
    const userInfo = getUserInfoCache();
    if (userInfo !== undefined && userInfo !== null) {
        return userInfo.userName;
    } else {
        return null;
    }
}
