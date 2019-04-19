var app = angular.module('uploadFileApp', ['angucomplete']);
app.controller('uploadFileController', function ($scope) {

    // 设置上传属性 若无则使用系统默认配置
    var options = {
        resetForm: false,
        beforeSubmit: function (form) {
            for (var i = 0; i < form.length; i++) {
                var file = form[i].value;
                if (!file) {
                    layer.msg('请选择文件', {icon: 2});
                    return false;
                }
                console.log('开始上传文件: name:' + file.name + ', type:' + file.type + ', size:' + Math.round(file.size / 1024) + 'KB');
            }
        }
    };

    // 调用文件上传公共方法
    uploadFile('uploadFile-upload', options, function (result) {
        layer.msg(result, {icon: 1});
    });

});
