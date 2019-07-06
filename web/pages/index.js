//常量类
var constants = new com.springboot.constant.Constants();

//定义全局组件 footer-bar
Vue.component('footer-bar', {
    template: `
        <footer>
          <hr/>
          <p class="el-dialog--center"><el-link v-on:click="show('没有更多信息')">版权所有@chen</el-link><p>
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
    template: loadPage("pages/templates/home.html"),
    data: function () {
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

//路由配置
var routes = [
    {path: "/", redirect: "/home"}, //这个表示会默认渲染
    {path: "/home", component: home}
];

var vm = new Vue({
    el: "#indexApp",
    data: {},
    methods: {
        handleSelect(key, keyPath) {
            console.log(key, keyPath);
        },
        logout() {
            vm.$confirm('确认要退出登录吗？', '提示').then(() => {
                window.location.href = getProjectPath() + constants.logout;
            }).catch(() => {
            })
        }
    },
    router: new VueRouter({
        routes: routes
    })
});
