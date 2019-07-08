/**
 * ######缓存数据操作类######
 * 使用sessionStorage保存当前会话数据
 */

/**
 * 保存缓存数据
 * @param key
 * @param value
 */
function setCache(key, value) {
    sessionStorage.setItem(key, JSON.stringify(value));
}

/**
 * 根据key获取缓存数据
 * @param key
 * @returns {any}
 */
function getCache(key) {
    return JSON.parse(sessionStorage.getItem(key));
}

/**
 * 根据key移除缓存数据
 * @param key
 */
function removeCache(key) {
    sessionStorage.removeItem(key);
}

/**
 * 清除所有缓存数据
 */
function clearAllCache() {
    sessionStorage.clear();
}

/**
 * 获取当前sessionStorage缓存长度
 * @returns {number}
 */
function getCacheLength() {
    return sessionStorage.length;
}

/**
 * 获取某个index索引的key
 * @param index
 * @returns {string | null}
 */
function getKeyByIndex(index) {
    return sessionStorage.key(index);
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
    var length = sessionStorage.length;
    var keys = [];
    for (var i = 0; i < length; i++) {
        keys[i] = getKeyByIndex(i);
    }
    return keys;
}
