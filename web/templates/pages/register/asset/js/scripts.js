$(function () {
    /**
     * Fullscreen background 背景图片轮播
     * duration 图片切换时间间隔
     */
    $(".container").css({opacity: 0.9}); // 设置页面透明度
    $.backstretch([
        "asset/img/backgrounds/1.jpg",
        "asset/img/backgrounds/2.jpg",
        "asset/img/backgrounds/3.jpg",
        "asset/img/backgrounds/4.jpg"
    ], {duration: 5000, fade: 750});
});
