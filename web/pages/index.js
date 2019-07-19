//常量类
var constants = new com.springboot.constant.Constants();

//用户信息
var userInfo = getCache("userInfo");

//定义全局组件 footer-bar
Vue.component('footer-bar', {
    template: `
        <footer>
          <hr/>
          <p class="el-dialog--center"><el-link v-on:click="show('未完成的功能！')">版权所有@chen</el-link><p>
        </footer>
    `,
    methods: {
        show(value) {
            this.$message(value);
        }
    }
});

//home路由组件
var home = {
    template: loadPage("/pages/templates/home.html"),
    data() {
        return {
            imgList: [
                {url: "/images/carousel-index/1.jpg"},
                {url: "/images/carousel-index/2.jpg"},
                {url: "/images/carousel-index/3.jpg"},
                {url: "/images/carousel-index/4.jpg"}
            ]
        }
    }
};

//messageCenter路由组件
var messageCenter = {
    template: `
        <div class="el-dialog--center"><h1>未完成的功能！</h1></div>
    `
};

//personalCenter路由组件
var personalCenter = {
    template: loadPage("/pages/templates/personal.html"),
    data() {
        //自定义密码校验
        const reg_password = /^[a-zA-Z]\w{5,17}$/;
        var validatePass = (rule, value, callback) => {
            if (!reg_password.test(value)) {
                callback(new Error('密码必须以字母开头，最少6位最多18位，允许字母数字下划线'));
                return false;
            }
            callback();
        };
        var validateConPass = (rule, value, callback) => {
            if (this.formEntity.conPassword !== this.formEntity.password) {
                callback(new Error('两次密码不一致'));
                return false;
            }
            if (!reg_password.test(value)) {
                callback(new Error('密码必须以字母开头，最少6位最多18位，允许字母数字下划线'));
                return false;
            }
            callback();
        };
        return {
            tabPosition: 'left',
            fileList: [],
            dialogVisible: false,
            imgUrl: '',
            formEntity: {},
            rules: {
                oldPassword: [
                    {required: true, message: '请输入原密码', trigger: 'blur'}
                ],
                password: [
                    {required: true, message: '请输入新密码', trigger: 'blur'},
                    {validator: validatePass, trigger: 'blur'}
                ],
                conPassword: [
                    {required: true, message: '请确认密码', trigger: 'blur'},
                    {validator: validateConPass, trigger: 'blur'}
                ]
            }
        }
    },
    methods: {
        //##########个人相册##########
        //删除前确认
        beforeRemove(file, fileList) {
            return vm.$confirm(`确定移除 ${ file.name }？`);
        },
        //执行删除
        handleRemove(file, fileList) {
            this.deletePersonalImage(file);
        },
        //已上传图片点击 显示大图
        handlePictureCardPreview(file) {
            this.dialogVisible = true;
            this.imgUrl = file.url;
        },
        //上传成功
        onSuccess(response, file, fileList) {
            response = handleResponse(response);
            if (!response) return false;
            var code = response.code;
            var result = response.data;
            if (code !== 0 || result.code !== '0') {
                this.findPersonalImage();
            } else {
                //上传成功，设置url
                file.url = result.msg;
            }
        },
        //上传失败
        onError(err, file, fileList) {
            this.findPersonalImage();
        },
        //上传中
        onProgress(event, file, fileList) {
            console.info(event);
        },
        //文件状态改变时的钩子，添加文件、上传成功和上传失败时都会被调用
        onChange(file, fileList) {
            console.info(file);
        },
        //查询个人的相册
        findPersonalImage() {
            var that = this;
            requestService("fileService", "findPersonalImage", function (result) {
                that.fileList = result;
            })
        },
        //根据文件name、url删除文件
        deletePersonalImage(file) {
            var that = this;
            requestService("fileService", "deletePersonalImage", file, function (result) {
                if (result.code !== '0')
                    that.findPersonalImage();
            })
        },

        //############密码修改##############
        //提交
        submitForm(formName, formEntity) {
            this.$refs[formName].validate((valid) => {
                if (!valid) {
                    return false;
                }
                var that = this;
                requestService('personalService', 'changePassword', formEntity, function (result) {
                    if (result.code !== '0') {
                        that.$message.error(result.msg);
                        return false;
                    } else {
                        that.$alert('密码修改成功，请重新登陆', '提示', {
                            confirmButtonText: '确定',
                            callback: () => {
                                window.location.href = getProjectPath() + constants.logout;
                            }
                        });
                    }
                })
            })
        },
        //重置
        resetForm(formName) {
            this.$refs[formName].resetFields();
        }
    },
    mounted() {
        this.findPersonalImage();
    }
};

//路由配置
var routes = [
    {path: "/", redirect: "/home"}, //这个表示会默认渲染
    {path: "/home", component: home},
    {path: "/messageCenter", component: messageCenter},
    {path: "/personalCenter", component: personalCenter}
];

var vm = new Vue({
    el: "#indexApp",
    data: {
        userInfo: userInfo
    },
    methods: {
        handleSelect(key, keyPath) {
            console.log(key, keyPath);
        },
        logout() {
            vm.$confirm('确认退出登录吗？', '提示', {type: 'warning', roundButton: true}).then(() => {
                window.location.href = getProjectPath() + constants.logout;
            }).catch(() => {
            })
        }
    },
    router: new VueRouter({
        routes: routes
    })
});
