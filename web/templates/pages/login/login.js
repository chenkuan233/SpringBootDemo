var app = angular.module('loginApp', []);
app.controller('loginController', function ($scope, $timeout) {

    var constants = new com.springboot.constant.Constants();

    //登陆服务
    $scope.login = function (username, password, rememberMe) {
        if (!username || !password) {
            layer.msg('请输入用户名和密码', {icon: 2});
            return false;
        }
        // 登录服务
        loginService(username, password, rememberMe, function (result) {
            if (result) {
                // 登录成功 跳转至index.html
                window.location.href = getProjectPath() + constants.indexUrl;
            }
        })
    };

    //跳转注册页面
    $scope.toRegister = function () {
        window.location.href = getProjectPath() + constants.registerUrl;
    }
});
