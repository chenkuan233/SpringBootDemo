$(function () {
    /**
     * Fullscreen background 背景图片轮播
     * duration 图片切换时间间隔
     * fade 切换动画时长
     */
    $(".container").css({opacity: 0.9}); // 设置页面透明度
    $.backstretch([
        "../../framework/asset/img/backgrounds/1.jpg",
        "../../framework/asset/img/backgrounds/2.jpg",
        "../../framework/asset/img/backgrounds/3.jpg",
        "../../framework/asset/img/backgrounds/4.jpg"
    ], {duration: 4000, fade: 1500});
});
