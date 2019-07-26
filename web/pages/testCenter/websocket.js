//获取当前登陆用户名
var username = getUserNameCache();
//初始化websocket
var websocket = null;

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常
window.onbeforeunload = function () {
    websocket.close();
};

var vm = new Vue({
    el: "#websocketApp",
    data: {},
    methods: {
        startWebsocket() {
            if ('WebSocket' in window) {
                websocket = new WebSocket("ws://" + getHost() + "/websocket/" + username);
            } else {
                vm.$message.error('当前浏览器 Not support websocket');
                return false;
            }

            //连接发生错误的回调方法
            websocket.onerror = function () {
                vm.$message.error('WebSocket连接发生错误');
            };

            //连接成功建立的回调方法
            websocket.onopen = function () {
                vm.$message.success('WebSocket连接成功');
            };

            //接收到消息的回调方法
            websocket.onmessage = function (event) {
                vm.$message(event.data);
            };

            //连接关闭的回调方法
            websocket.onclose = function () {
                vm.$message.warning("WebSocket连接关闭");
            };
        },
    }
});