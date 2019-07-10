//常量类
var constants = new com.springboot.constant.Constants();

//Vue初始化
var vm = new Vue({
    el: "#registerApp",
    data: {
        username: undefined,
        password: undefined,
        rePassword: undefined
    },
    methods: {
        //注册服务
        register(username, password, rePassword) {
            if (isEmpty(username) || isEmpty(password) || isEmpty(rePassword)) {
                vm.$message.warning('请输入用户名和密码');
                return false;
            }
            const reg_username = /^[a-zA-Z][a-zA-Z0-9_]{3,15}$/;
            if (!reg_username.test(username)) {
                vm.$message.warning('账号必须以字母开头，最少4位最多16位，允许字母数字下划线');
                return false;
            }
            const reg_password = /^[a-zA-Z]\w{5,17}$/;
            if (!reg_password.test(password)) {
                vm.$message.warning('密码必须以字母开头，最少6位最多18位，允许字母数字下划线');
                this.password = this.rePassword = undefined;
                return false;
            }
            if (password !== rePassword) {
                vm.$message.warning('两次密码不一致');
                this.rePassword = undefined;
                return false;
            }
            requestService('registerService', 'findByUserName', username, function (result) {
                if (result) {
                    vm.$message.warning(username + ' 已存在，换个用户名试试吧');
                    return false;
                }
                layerLoading();
                requestService('registerService', 'registerAccount', username, password, function (result) {
                    if (result.code !== "0") {
                        vm.$message.error(result.msg);
                    } else {
                        vm.$confirm('注册成功！是否去登录？', '提示', {
                            confirmButtonText: '去登陆',
                            cancelButtonText: '再看看',
                            type: 'success'
                        }).then(() => {
                            window.location.href = getProjectPath() + constants.loginUrl;
                        })
                    }
                })
            })
        },

        //跳转登陆
        toLogin() {
            window.location.href = getProjectPath() + constants.loginUrl;
        }
    },

    //mounted钩子函数:它表示页面一加载进来就执行函数里面的内容（和window.onload类似）
    mounted() {
        document.getElementById("form-username").focus();
    }
});
