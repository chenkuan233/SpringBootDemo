var app = angular.module('uploadFileApp', ['angucomplete']);
app.controller('uploadFileController', function ($scope) {

    // 调用文件上传公共方法
    uploadFile('uploadFile-upload', function (result) {
        layer.msg(result, {icon: 1});
    });

});
