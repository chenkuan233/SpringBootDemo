// https://127.0.0.1/springBootDemo/
projectPath = window.location.protocol + "//" + window.location.host + window.location.pathname.substring(0, window.location.pathname.substr(1).indexOf("/") + 2);

document.write('<link href="' + projectPath + 'css/aLink.css" rel="stylesheet" type="text/css">');
