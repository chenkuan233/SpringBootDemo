// https://127.0.0.1/
projectPath = window.location.protocol + "//" + window.location.host + '/';

//vue.js
document.write('<script src="' + projectPath + 'framework/vue/vue.min.js"></script>');
//vue-router.js
document.write('<script src="' + projectPath + 'framework/vue/vue-router.min.js"></script>');

//element-ui
document.write('<link href="' + projectPath + 'framework/element-ui/element-ui.css" rel="stylesheet">');
document.write('<script src="' + projectPath + 'framework/element-ui/element-ui.js"></script>');

// jquery
document.write('<script src="' + projectPath + 'framework/jquery/jquery-3.4.1.min.js"></script>');
// jquery.form
document.write('<script src="' + projectPath + 'framework/jquery/jquery.form.js"></script>');

// namespace
document.write('<script src="' + projectPath + 'framework/util/namespace.js"></script>');

// constants
document.write('<script src="' + projectPath + 'js/constants.js"></script>');

// service.js服务方法类
document.write('<script src="' + projectPath + 'js/service.js"></script>');

// app.js公共方法类
document.write('<script src="' + projectPath + 'js/app.js"></script>');

// layer弹出层
document.write('<script src="' + projectPath + 'framework/layer/layer.js"></script>');

// Base64编码
document.write('<script src="' + projectPath + 'framework/util/Base64.js"></script>');

// 上传文件样式css
document.write('<link href="' + projectPath + 'css/upload.css" rel="stylesheet">');
