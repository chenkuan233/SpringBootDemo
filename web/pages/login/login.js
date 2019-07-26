//常量类
var constants = new com.springboot.constant.Constants();

//Vue初始化
var vm = new Vue({
    el: "#loginApp",
    data: {
        username: undefined,
        password: undefined,
        rememberMe: undefined
    },
    methods: {
        //登陆服务
        login(username, password, rememberMe) {
            if (isEmpty(username) || isEmpty(password)) {
                vm.$message({message: '请输入用户名和密码', type: 'warning'});
                return false;
            }
            // 登录服务
            loginService(username, password, rememberMe, function (result) {
                if (result.code !== "0") {
                    //登录失败
                    vm.$message.error(result.msg)
                } else {
                    //登录成功
                    saveUserInfoCache(result.data); //缓存用户信息
                    window.location.href = getProjectPath() + constants.indexUrl;
                }
            })
        },

        //跳转注册页面
        toRegister() {
            window.location.href = getProjectPath() + constants.registerUrl;
        }
    },

    //mounted钩子函数:它表示页面一加载进来就执行函数里面的内容（和window.onload类似）
    mounted() {
        //username input自动聚焦
        document.getElementById("form-username").focus();
    }
});
