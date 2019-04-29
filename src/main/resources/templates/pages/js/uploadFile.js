var app = angular.module('uploadFileApp', ['angucomplete']);
app.controller('uploadFileController', function ($scope) {

    // 上传参数
    var options = {
        progress: true // 显示进度条 默认true
    };

    // 调用文件上传公共方法
    uploadFile('uploadFile-upload', 'fileService', 'uploadFile', options, function (result) {
        if (result.code !== '0') {
            layer.msg(result.msg, {icon: 2});
            return false;
        }
        var filePath = result.msg;
        // 保存文件路径到数据库
        requestService('fileService', 'saveFile', filePath, function (result) {
            if (result.code !== '0') {
                layer.msg(result.msg, {icon: 2});
            } else {
                layer.msg(result.msg, {icon: 1});
                $scope.find();
            }
        })
    });

    // 查询所有文件
    $scope.find = function () {
        requestService('fileService', 'findAllFile', function (result) {
            $scope.fileList = result;
            $scope.$apply();
        })
    };
    $scope.find();

    // 下载文件
    $scope.downloadFile = function (filePath) {
        downloadFile(filePath);
    };

    // 删除文件
    $scope.deleteFile = function (entity, index) {
        layer.confirm('确认删除？', {icon: 3}, function () {
            requestService('fileService', 'deleteFile', entity, function (result) {
                layer.msg('删除成功', {icon: 1});
                $scope.fileList.splice(index, 1);
                $scope.$apply();
            })
        });
    }

});
