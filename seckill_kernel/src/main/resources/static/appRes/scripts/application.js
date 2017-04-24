/**
 * Created by LuPindong on 2017-4-24.
 */
$(function () {
    // 激活对应频道
    var pathname = window.location.pathname;
    var channel = "#" + pathname.substr(1);
    if ("#" === channel) {
        channel = "#index";
    }
    $("#mainNav > li").removeClass("active");
    $(channel).addClass("active");
});

var Common = (function () {
    var stack_topleft = {"dir1": "down", "dir2": "right", "push": "top"};
    var stack_bottomleft = {"dir1": "right", "dir2": "up", "push": "top"};
    var stack_bottomright = {"dir1": "up", "dir2": "left", "firstpos1": 15, "firstpos2": 15};
    var stack_bar_top = {"dir1": "down", "dir2": "right", "push": "top", "spacing1": 0, "spacing2": 0};
    var stack_bar_bottom = {"dir1": "up", "dir2": "right", "spacing1": 0, "spacing2": 0};

    PNotice = {
        common: function (title, text, type, addClass, stack) {
            new PNotify({
                title: title,
                text: text,
                type: type,
                addclass: addClass,
                stack: stack,
                hide: false //是否自动关闭
            });
        },
        info: function (title, text) {
            PNotice.common(title, text, 'info', 'stack-bottomright', stack_bottomright);
        },
        info: function (title, text) {
            PNotice.common(title, text, 'info', 'stack-bottomright', stack_bottomright);
        },
        info: function (title, text) {
            PNotice.common(title, text, 'info', 'stack-bottomright', stack_bottomright);
        },
        info: function (title, text) {
            PNotice.common(title, text, 'info', 'stack-bottomright', stack_bottomright);
        },
        error: function (title, text) {
            PNotice.common(title, text, 'error', 'stack-bottomright', stack_bottomright);
        }
    }

    return {
        PNotice: PNotice
    }
})();