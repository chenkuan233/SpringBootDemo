var app = angular.module('homeApp', ['ngRoute', 'angucomplete']);
app.controller('homeController', function ($scope) {

    // var constants = new com.springboot.constant.Constants();

    $scope.find = function () {
        requestService('userService', 'findAllMapper', 1, 2, function (result) {
            $scope.resultList = result;
            $scope.$apply();
        });
    }

});
