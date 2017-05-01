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
        common: function (text, title, type, addClass, stack) {
            if (StringUtil.isBlank(title)) {
                title = '消息';
            }
            new PNotify({
                title: title,
                text: text,
                type: type,
                addclass: addClass,
                stack: stack
            });
        },
        primary: function (text, title) {
            PNotice.common(text, title, 'primary', 'notification-primary stack-bottomright', stack_bottomright);
        },
        notice: function (text, title) {
            PNotice.common(text, title, 'notice', 'stack-bottomright', stack_bottomright);
        },
        success: function (text, title) {
            PNotice.common(text, title, 'success', 'stack-bottomright', stack_bottomright);
        },
        info: function (text, title) {
            PNotice.common(text, title, 'info', 'stack-bottomright', stack_bottomright);
        },
        error: function (text, title) {
            PNotice.common(text, title, 'error', 'stack-bottomright', stack_bottomright);
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
                var sortType = Common.StringUtil.getNewSortType(sortValues[1]);
                $("#sort").val(classNames[1] + ":" + sortType);
            }

            $("#estatesForm").attr("action", Estate.List.url()).submit();
        }
    }

    return {
        List: List
    }
})();

var Special = (function () {
    var List = {
        url: function () {
            return "/special/list";
        },
        filterData: function (obj) {
            if ("LI" == obj.tagName) {
                var classNames = obj.className.split(" ");
                var sortValues = $("#sort").val().split(":");
                var sortType = Common.StringUtil.getNewSortType(sortValues[1]);
                $("#sort").val(classNames[1] + ":" + sortType);
            }

            $("#stocksForm").attr("action", Special.List.url()).submit();
        }
    }
    var Detail = {
        nowUrl: function () {
            return "/special/now";
        },
        exposure: function (houseId) {
            return '/special/' + houseId + '/exposure';
        },
        execution: function (houseId, key) {
            return '/special/' + houseId + '/execution/' + key;
        },
        init: function (params) {
            var houseId = params['houseId'];
            var nowTime = params['nowTime'];
            var startTime = params['startTime'];
            var endTime = params['endTime'];

            Detail.countDownX(houseId, nowTime, startTime, endTime);
        },
        countDownX: function (houseId, nowTime, startTime, endTime) {
            var timeArea = $("#timeArea");
            if (nowTime > endTime) {
                timeArea.html('<h2 class="font-red">秒杀结束!</h2>');
            } else if (nowTime < startTime) {
                var killTime = new Date(startTime);
                timeArea.countdown(killTime, function (event) {
                    var format = event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒 ');
                    timeArea.html('<h2 class="font-red">' + format + '</h2>');
                }).on('finish.countdown', function () {
                    Detail.executeSecKill(houseId, endTime);
                });
            } else {
                Detail.executeSecKill(houseId, endTime);
            }
        },
        executeSecKill: function (houseId, endTime) {
            var timeArea = $("#timeArea");
            var execution = $("#execution");

            execution.hide().html('<a href="javascript:void(0)" class="btn btn-lg btn-primary" id="killBtn">开始秒杀!</a>');

            $.get(Detail.exposure(houseId))
                .done(function (result) {
                    //在回调函数种执行交互流程
                    if (200 == result.status) {
                        var endTimeX = new Date(endTime);
                        timeArea.countdown(endTimeX, function (event) {
                            var format = event.strftime('距秒杀结束仅剩: %D天 %H时 %M分 %S秒 ');
                            timeArea.html('<h2 class="font-red">' + format + '</h2>');
                        }).on('finish.countdown', function () {
                            //
                        });

                        var killUrl = Detail.execution(houseId, result.data);
                        //绑定一次点击事件
                        $('#killBtn').one('click', function () {
                            //执行秒杀请求
                            //1.先禁用按钮
                            $(this).addClass('disabled');
                            //2.发送秒杀请求执行秒杀
                            $.post(killUrl)
                                .done(function (result) {
                                    if (200 == result.status) {
                                        // var killResult = result['data'];
                                        // var state = killResult['state'];
                                        // var stateInfo = killResult['stateInfo'];
                                        //显示秒杀结果
                                        //execution.html('<span class="label label-success">' + stateInfo + '</span>');
                                    }
                                }).fail(function (result) {

                            });
                        });
                        execution.show();
                    } else {
                        //未开启秒杀(浏览器计时偏差)
                        var now = exposer['now'];
                        var start = exposer['start'];
                        var end = exposer['end'];
                        seckill.countDown(seckillId, now, start, end);
                    }
                });
        }
    }
    return {
        List: List,
        Detail: Detail
    }
})();

var SysUser = (function () {
    var Register = {
        signupUrl: function () {
            return "/user/signup";
        },
        saltUrl: function () {
            return "/user/salt";
        },
        init: function () {
            $('#frmSignUp').validate({
                rules: {
                    account: {required: true, minlength: 4},
                    email: {required: true, email: true},
                    password: {required: true, minlength: 6},
                    rePassword: {required: true, minlength: 6, equalTo: "#password"}
                },
                messages: {
                    account: {required: "请输入用户账号", minlength: "用户账号至少由4个字母组成"},
                    email: {required: "请输入邮箱地址", email: "请输入一个正确的邮箱地址"},
                    password: {required: "请输入设置密码", minlength: "密码长度不能小于6个字母"},
                    rePassword: {required: "请输入确认密码", minlength: "密码长度不能小于6个字母", equalTo: "两次密码输入不一致"}
                },
                submitHandler: function (form) {
                    Register.signup();
                }
            });

            $('#frmSignUp input').keypress(function (e) {
                if (e.which == 13) {
                    if ($('#frmSignUp').validate().form()) {
                        Register.signup();
                    }
                    return false;
                }
            });
        },
        signup: function () {
            var account = $.trim($("#account").val());
            var email = $.trim($("#email").val());
            var password = $.trim($("#password").val());

            $.get(Register.saltUrl())
                .done(function (result) {
                    var val1st = SparkMD5.hash(account, false) + SparkMD5.hash(email, false) + SparkMD5.hash(password, false) + SparkMD5.hash(result.data, false);
                    var val2nd = SparkMD5.hash(val1st, false);
                    var data = {
                        account: account,
                        email: email,
                        cipher: val2nd
                    }

                    // 执行登录操作
                    $.post(Register.signupUrl(), data)
                        .done(function (result) {
                            if (200 == result.status) {
                                Common.PNotice.success(result.message);
                                window.location.href = "/";
                            } else {
                                Common.PNotice.error(result.message);
                            }
                        })
                        .fail(function (result) {
                            Common.PNotice.error(result.message, result.data);
                        });
                })
        }
    };

    return {
        Register: Register
    }
})();