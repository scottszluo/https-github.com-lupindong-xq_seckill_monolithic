/**
 * Created by LuPindong on 2017-4-24.
 */
$(function () {
    // 激活对应频道
    var pathname = window.location.pathname;
    var channels = pathname.split("/");
    var channel = "#index";
    if (channels.length >= 2 && Common.StringUtil.isNotBlank(channels[1])) {
        channel = "#" + channels[1];
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

    var PNotice = {
        // 消息提示
        common: function (title, text, type, addClass, stack) {
            new PNotify({
                title: title,
                text: text,
                type: type,
                addclass: addClass,
                stack: stack
                // hide: false //是否自动关闭
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

    var Bootpag = {
        // 初始化分页信息（Get提交）
        initWithGet: function (url, total, page) {
            $('#pagination').bootpag({
                total: total,
                page: page,
                maxVisible: 5,
                leaps: true,
                firstLastUse: true,
                first: '首页',
                last: '尾页',
            }).on('page', function (event, num) {
                if (url.indexOf('page=') != -1) {
                    window.location.href = url + num;
                } else {
                    window.location.href = url + "?page=" + num;
                }
            });
        },
        // 初始化分页信息（Post提交）
        initWithPost: function (formId, url, total, page) {
            $('#pagination').bootpag({
                total: total,
                page: page,
                maxVisible: 5,
                leaps: true,
                firstLastUse: true,
                first: '首页',
                last: '尾页',
            }).on('page', function (event, num) {
                $("#page").val(num);
                $("#" + formId).attr("action", url).submit();
            });
        }
    }

    var Lazyload = {
        // 初始化图片延迟加载
        init: function () {
            $("img.lazyload").lazyload({
                effect: "fadeIn"
            });
        }
    }

    var StringUtil = {
        isBlank: function (strVal) {
            if (typeof(strVal) == "undefined" || "" == strVal || strVal.length < 1) {
                return true;
            }
            return false;
        },
        isNotBlank: function (strVal) {
            return !StringUtil.isBlank(strVal);
        }
    }

    return {
        PNotice: PNotice,
        Bootpag: Bootpag,
        Lazyload: Lazyload,
        StringUtil: StringUtil
    }
})();

var Estate = (function () {
    var List = {
        url: function () {
            return "/estate/list";
        },
        filterData: function (obj) {
            if ("LI" == obj.tagName) {
                var classNames = obj.className.split(" ");
                var sortValues = $("#sort").val().split(":");
                var sortType = List.getNewSortType(sortValues[1]);
                $("#sort").val(classNames[1] + ":" + sortType);
            }

            $("#estatesForm").attr("action", Estate.List.url()).submit();
        },
        getNewSortType: function (sortType) {
            if (Common.StringUtil.isNotBlank(sortType)) {
                if ("ASC" == $.trim(sortType)) {
                    sortType = "DESC";
                } else {
                    sortType = "ASC";
                }
            }
            return sortType;
        }
    }

    return {
        List: List
    }
})();