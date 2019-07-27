//获取当前登陆用户名
var username = getUserNameCache();
//初始化websocket
var websocket = null;

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常
window.onbeforeunload = function () {
    if (websocket && websocket.readyState === websocket.OPEN)
        websocket.close();
};

var vm = new Vue({
    el: "#websocketApp",
    data: {
        socketStatus: false
    },
    methods: {
        //初始化websocket
        startWebsocket() {
            if ('WebSocket' in window) {
                //若已连接直接返回
                if (websocket && websocket.readyState === websocket.OPEN) return;
                //初始化连接
                websocket = new WebSocket("ws://" + getHost() + "/websocket/" + username);
            } else {
                vm.$message.error('当前浏览器 Not support websocket');
                return;
            }

            //连接发生错误的回调方法
            websocket.onerror = function () {
                vm.$message.error('WebSocket连接发生错误');
                vm.socketStatus = false;
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
                vm.socketStatus = false;
            };
        },

        //改变连接状态，开、关
        changeSocketStatus(value) {
            if (value) { //打开连接
                vm.startWebsocket();
            } else { //关闭连接
                if (websocket && websocket.readyState === websocket.OPEN) {
                    websocket.close();
                }
            }
        },

        //发送消息 点击事件
        websocketSend() {
            vm.sendMessageTo('admin', '来自[' + username + ']的测试消息');
        },

        //发送指定消息
        sendMessageTo(to, message) {
            if (!(websocket && websocket.readyState === websocket.OPEN)) {
                vm.$message.error("WebSocket未连接，请重新连接");
                return;
            }
            const data = "['" + to + "', '" + message + "']";
            websocket.send(data);
            vm.$message('给[' + to + ']发送消息');
        }
    }
});