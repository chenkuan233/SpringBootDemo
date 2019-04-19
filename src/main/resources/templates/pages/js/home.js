var app = angular.module('homeApp', ['ngRoute', 'angucomplete']);
app.controller('homeController', function ($scope) {

    // var constants = new com.springboot.constant.Constants();

    $scope.find = function (pageNum, pageSize) {
        requestService('userService', 'findAllMapper', pageNum, pageSize, function (result) {
            $scope.resultList = result;
            $scope.$apply();
        });
    }

});
