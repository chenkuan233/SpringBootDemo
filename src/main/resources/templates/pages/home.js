var app = angular.module('myApp', ['ngRoute', 'angucomplete']);
app.controller('MyHomeController', function ($scope, $http) {

    // $scope.constants = new com.springboot.constant.Constants();
    // var requestName = constants.projectName;

    $scope.find = function () {
        $http({
            url: '/springBootDemo/service/userService/findAllMyMapper', // 请求地址
            method: 'POST', // 请求方式
            data: JSON.stringify(), // 请求的数据 message 必须是a=b&c=d的格式 data是post请求的数据，params是get请求的数据,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
                'Accept': '*/*'
            }, // 请求的头部，如果默认可以不写
            timeout: 10000 // 超时时间 毫秒
        }).success(function (data, status, headers, config) {
            $scope.resultList = data;
        }).error(function (data, status, headers, config) {
            if (0 === status) {
                console.log("error:响应超时", data, status, headers);
            } else {
                console.log("error:后台错误", data, status, headers);
            }
        })
    }

});
