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
    template: `
        <div class="el-dialog--center"><h1>未完成的功能！</h1></div>
    `
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
