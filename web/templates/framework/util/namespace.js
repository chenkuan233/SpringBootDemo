(function (root) {
    root.Namespace = {};
    root.Namespace.register = function (path) {
        var arr = path.split(".");
        var ns = "";
        for (var i = 0; i < arr.length; i++) {
            if (i > 0) {
                ns += ".";
            }
            ns += arr[i];
            eval("if(typeof(" + ns + ") == 'undefined') " + ns + " = {};");
        }
    }
})(window);