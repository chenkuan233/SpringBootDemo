// https://127.0.0.1/
projectPath = window.location.protocol + "//" + window.location.host + '/';

// angularjs
document.write('<script src="' + projectPath + 'framework/angular/1.4.3/angular.min.js"></script>');
document.write('<script src="' + projectPath + 'framework/angular/1.4.3/angular-route.min.js"></script>');
document.write('<script src="' + projectPath + 'framework/angular/directives/angucomplete.js"></script>');
document.write('<script src="' + projectPath + 'framework/angular/directives/angucomplete-alt.js"></script>');

// jquery1.9
document.write('<script src="' + projectPath + 'framework/jquery/1.9/jquery.js"></script>');
// jquery.form
document.write('<script src="' + projectPath + 'framework/jquery/jquery.form.js"></script>');

// namespace
document.write('<script src="' + projectPath + 'framework/util/namespace.js"></script>');

// constants
document.write('<script src="' + projectPath + 'js/constants.js"></script>');

// app.js公共方法类
document.write('<script src="' + projectPath + 'js/app.js"></script>');

// layer弹出层
document.write('<script src="' + projectPath + 'framework/layer/layer.js"></script>');
