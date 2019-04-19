var app = angular.module('registerApp', []);
app.controller('registerController', function ($scope) {

    $scope.registerAccount = function (username, password) {
        if (!username || !password) {
            layer.msg('账号密码为必填', {icon: 2});
            return false;
        }
        requestService('registerService', 'findByUserName', username, function (result) {
            if (result) {
                layer.msg(username + ' 账号已存在', {icon: 2});
                return false;
            }
            requestService('registerService', 'registerAccount', username, password, function (result) {
                if (result.code !== "0") {
                    layer.msg(result.msg, {icon: 2});
                } else {
                    layer.confirm('注册成功！是否立即登录？', {icon: 1}, function () {
                        window.location.href = '../login.html';
                    });
                }
            })
        })
    }
});
