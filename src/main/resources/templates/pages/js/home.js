var app = angular.module('homeApp', ['ngRoute', 'angucomplete']);
app.controller('homeController', function ($scope) {

    // var constants = new com.springboot.constant.Constants();

    // 分页查询
    $scope.find = function (pageNum, pageSize) {
        var pageable = {'pageNum': pageNum, 'pageSize': pageSize};
        requestService('userService', 'findAllMapper', pageable, function (result) {
            $scope.resultList = result.list;
            $scope.$apply();
        });
    };

    // 进入页面自动查询
    $scope.find(1, 10);

    // 根据id删除
    $scope.delete = function (id, index) {
        layer.confirm('确认删除？', {icon: 3}, function () {
            requestService('userService', 'deleteMapper', id, function (result) {
                layer.msg('删除成功', {icon: 1});
                $scope.resultList.splice(index, 1);
                $scope.$apply();
            })
        })
    }

});
