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
    }

});
