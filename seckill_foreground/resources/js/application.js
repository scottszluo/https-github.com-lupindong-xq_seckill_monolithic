/**
 * Created by LuPindong on 2017-4-24.
 */
// 全局
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

    var userName = $.cookie("USER_NAME");
    if (Common.StringUtil.isBlank(userName)) {
        $("#login").show();
    } else {
        $("#userName").text(userName);
        $("#logout").show();
    }

});
// 通用
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
            this.common(text, title, 'primary', 'notification-primary stack-bottomright', stack_bottomright);
        },
        notice: function (text, title) {
            this.common(text, title, 'notice', 'stack-bottomright', stack_bottomright);
        },
        success: function (text, title) {
            this.common(text, title, 'success', 'stack-bottomright', stack_bottomright);
        },
        info: function (text, title) {
            this.common(text, title, 'info', 'stack-bottomright', stack_bottomright);
        },
        error: function (text, title) {
            this.common(text, title, 'error', 'stack-bottomright', stack_bottomright);
        }
    };

    var Lazyload = {
        // 初始化图片延迟加载
        init: function () {
            $("img.lazyload").lazyload({
                threshold: 100,
                effect: "fadeIn"
            });
        }
    };

    var StringUtil = {
        isBlank: function (strVal) {
            if (typeof(strVal) == "undefined" || null == strVal || "" == strVal || strVal.length < 1) {
                return true;
            }
            return false;
        },
        isNotBlank: function (strVal) {
            return !this.isBlank(strVal);
        },
        getNewSortType: function (sortType) {
            if (this.isNotBlank(sortType)) {
                if ("ASC" == $.trim(sortType)) {
                    sortType = "DESC";
                } else {
                    sortType = "ASC";
                }
            }
            return sortType;
        },
        //获取url中"?"符后的字串
        getRequestParam: function () {
            var url = window.location.search;
            var theRequest = new Object();
            if (url.indexOf("?") != -1) {
                var keyValue = url.substr(1);
                strs = keyValue.split("&");
                for (var i = 0; i < strs.length; i++) {
                    theRequest[strs[i].split("=")[0]] = decodeURI(strs[i].split("=")[1]);
                }
            }
            return theRequest;
        },
        getRequestParamWithSearch: function (locationSearch) {
            var theRequest = new Object();
            if (locationSearch.indexOf("?") != -1) {
                var keyValue = locationSearch.substr(1);
                strs = keyValue.split("&");
                for (var i = 0; i < strs.length; i++) {
                    theRequest[strs[i].split("=")[0]] = decodeURI(strs[i].split("=")[1]);
                }
            }
            return theRequest;
        }
    };

    $.addTemplateFormatter({});

    return {
        PNotice: PNotice,
        Lazyload: Lazyload,
        StringUtil: StringUtil
    }
})();
// 房源信息
var Estate = (function () {
    var List = {
        uiUrl: function () {
            return "/estate/listUI.html";
        },

        dataUrl: function () {
            return "/estates";
        },

        init: function () {
            var requestParamObj = JSON.parse(sessionStorage.getItem("requestParam"));
            if (requestParamObj == null || requestParamObj.length < 1) {
                requestParamObj = {"page": 1};
            }
            this.loadData(requestParamObj);

            // 绑定数据过滤函数
            $("select").on("change", function () {
                List.filterData(this);
            })

            $(".sort").click(function () {
                List.sortData(this);
            })

        },

        goNextPage: function (pageNum) {
            var requestParamObj = JSON.parse(sessionStorage.getItem("requestParam"));
            if (requestParamObj != null && requestParamObj.length > 0) {
                requestParamObj.push("page", pageNum);
            } else {
                requestParamObj = {"page": pageNum};
            }
            this.loadData(requestParamObj);
        },

        loadData: function (requestParamObj) {
            $.get(List.dataUrl(), requestParamObj).done(function (result) {
                var pageData = result.data;
                $("#listing").loadTemplate("#listData", pageData.content, {
                    complete: function () {
                        Common.Lazyload.init();
                    },
                });

                // 先解绑
                $('#pagination').off('page');

                // 分页
                $('#pagination').bootpag({
                    total: (pageData.totalPages > 100) ? 100 : pageData.totalPages,
                    page: pageData.number + 1,
                    maxVisible: 5,
                    leaps: true,
                    firstLastUse: true,
                    first: '首页',
                    last: '尾页',
                }).on('page', function (event, num) {
                    $(window).scrollTop(0);
                    List.goNextPage(num);
                });
            }).fail(function (result) {
                Common.PNotice.error(result.message);
            });

            sessionStorage.setItem("requestParam", JSON.stringify(requestParamObj));
        },

        filterData: function (obj) {
            if ("LI" == obj.tagName) {
                var classNames = obj.className.split(" ");
                var sortValues = $("#sort").val().split(":");
                var sortType = Common.StringUtil.getNewSortType(sortValues[1]);
                $("#sort").val(classNames[1] + ":" + sortType);
            }

            $("#estatesForm").attr("action", Estate.List.dataUrl()).submit();
        },

        sortData: function (obj) {
            var requestParamObj = JSON.parse(sessionStorage.getItem("requestParam"));
            console.log(requestParamObj);
            var classNames = obj.className.split(" ");
            if (Common.StringUtil.isBlank(requestParamObj.sort)) {
                requestParamObj.sort = "id:DESC";
            }
            var sortValues = requestParamObj.sort.split(":");
            var sortType = Common.StringUtil.getNewSortType(sortValues[1]);
            requestParamObj.sort = classNames[1] + ":" + sortType;
            this.loadData(requestParamObj);
        }
    };

    return {
        List: List
    }
})();
// 特价秒杀
var Special = (function () {
    var List = {
        dataUrl: function () {
            return "/specials";
        },

        init: function () {
            $.get(List.dataUrl()).done(function (result) {
                $("#listing").loadTemplate("#listData", result.data, {
                    complete: function () {
                        Common.Lazyload.init();
                    }
                });
            }).fail(function (result) {
                Common.PNotice.error(result.responseJSON.message);
            });
        }
    };

    var Detail = {
        nowUrl: function () {
            return "/special/now";
        },
        exposure: function (houseCode) {
            return '/special/' + houseCode + '/exposure';
        },
        execution: function (houseCode, key) {
            return '/special/' + houseCode + '/execution/' + key;
        },
        init: function (params) {
            var houseCode = params['houseCode'];
            var nowTime = params['nowTime'];
            var startTime = params['startTime'];
            var endTime = params['endTime'];

            Detail.countDownX(houseCode, nowTime, startTime, endTime);
        },
        countDownX: function (houseCode, nowTime, startTime, endTime) {
            var countDownArea = $("#countDownArea");
            if (nowTime > endTime) {
                countDownArea.html('<h2 class="font-red">秒杀结束</h2>');
            } else if (nowTime < startTime) {
                var killTime = new Date(startTime);
                countDownArea.countdown(killTime, function (event) {
                    var format = event.strftime('距秒杀开启: %D天 %H时 %M分 %S秒 ');
                    countDownArea.html('<h2 class="font-red">' + format + '</h2>');
                }).on('finish.countdown', function () {
                    Detail.executeSecKill(houseCode, nowTime, startTime, endTime);
                });
            } else {
                Detail.executeSecKill(houseCode, nowTime, startTime, endTime);
            }
        },
        executeSecKill: function (houseCode, nowTime, startTime, endTime) {
            var countDownArea = $("#countDownArea");
            var execution = $("#execution");

            execution.hide().html('<a href="javascript:void(0)" class="btn btn-lg btn-primary" id="killBtn">开始秒杀</a>');

            $.get(Detail.exposure(houseCode)).done(function (result) {
                //在回调函数种执行交互流程
                if (200 == result.status) {
                    var endTimeX = new Date(endTime);
                    countDownArea.countdown(endTimeX, function (event) {
                        var format = event.strftime('距秒杀结束: %D天 %H时 %M分 %S秒 ');
                        countDownArea.html('<h2 class="font-red">' + format + '</h2>');
                    }).on('finish.countdown', function () {
                        countDownArea.html('<h2 class="font-red">秒杀结束</h2>');
                        execution.hide();
                    });

                    var killUrl = Detail.execution(houseCode, result.data);

                    //绑定一次点击事件
                    $('#killBtn').one('click', function () {
                        var userName = $.cookie("USER_NAME");
                        if (Common.StringUtil.isBlank(userName)) {
                            Common.PNotice.error("受限内容，请登录后再访问！");
                            return false;
                        }

                        //执行秒杀请求
                        //1.先禁用按钮
                        $(this).hide();

                        //2.发送秒杀请求执行秒杀
                        $.post(killUrl).done(function (result) {
                            if (200 == result.status) {
                                Common.PNotice.success(result.message);
                            } else {
                                Common.PNotice.error(result.message);
                            }
                        }).fail(function (result) {
                            Common.PNotice.error(result.responseJSON.message);
                        });
                    });
                    execution.show();
                } else if (403 == result.status) {
                    countDownArea.html('<h2 class="font-red">秒杀成功</h2>');
                }
            }).fail(function (result) {
                Common.PNotice.error(result.responseJSON.message);
            });
        }
    };

    return {
        List: List,
        Detail: Detail
    }
})();
// 用户管理
var SysUser = (function () {

    var saltUrl = function () {
        return "/user/salt";
    };

    var Register = {
        signupUrl: function () {
            return "/user/signup";
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

            $.get(saltUrl()).done(function (result) {
                var val1st = SparkMD5.hash(account, false) + SparkMD5.hash(password, false) + SparkMD5.hash(result.data, false);
                var val2nd = SparkMD5.hash(val1st, false);
                var data = {
                    account: window.btoa(account),
                    email: window.btoa(email),
                    cipher: window.btoa(val2nd)
                }

                // 执行登录操作
                $.post(Register.signupUrl(), data).done(function (result) {
                    if (200 == result.status) {
                        Common.PNotice.success(result.message);
                        window.location.href = "/user/login";
                    } else {
                        Common.PNotice.error(result.message);
                    }
                }).fail(function (result) {
                    Common.PNotice.error(result.message, result.data);
                });
            });
        }
    };

    var Login = {
        signinUrl: function () {
            return "/user/signin";
        },
        init: function () {
            $('#frmSignIn').validate({
                rules: {
                    account: {required: true, minlength: 4},
                    password: {required: true, minlength: 6},
                },
                messages: {
                    account: {required: "请输入账号/邮箱", minlength: "账号至少由4个字母组成"},
                    password: {required: "请输入密码", minlength: "密码长度不能小于6个字母"}
                },
                submitHandler: function (form) {
                    Login.signin();
                }
            });

            $('#frmSignIn input').keypress(function (e) {
                if (e.which == 13) {
                    if ($('#frmSignUp').validate().form()) {
                        Login.signin();
                    }
                    return false;
                }
            });
        },
        signin: function () {
            var account = $.trim($("#account").val());
            var password = $.trim($("#password").val());

            $.get(saltUrl()).done(function (result) {
                var val1st = SparkMD5.hash(account, false) + SparkMD5.hash(password, false) + SparkMD5.hash(result.data, false);
                var val2nd = SparkMD5.hash(val1st, false);
                var data = {
                    account: window.btoa(account),
                    cipher: window.btoa(val2nd)
                }

                // 执行登录操作
                $.post(Login.signinUrl(), data).done(function (result) {
                    if (200 == result.status) {
                        Common.PNotice.success(result.message);
                        window.location.href = "/special/listUI.html";
                    } else {
                        Common.PNotice.error(result.message);
                    }
                }).fail(function (result) {
                    Common.PNotice.error(result.message, result.data);
                });
            });
        }
    };

    var Logout = {
        signoutUrl: function () {
            return "/user/signout";
        },
        signout: function () {
            // 执行登出操作
            $.post(Logout.signoutUrl()).done(function (result) {
                if (200 == result.status) {
                    Common.PNotice.success(result.message);
                    window.location.href = window.location.href;
                } else {
                    Common.PNotice.error(result.message);
                }
            }).fail(function (result) {
                Common.PNotice.error(result.message, result.data);
            });
        }
    };

    return {
        Register: Register,
        Login: Login,
        Logout: Logout
    }
})();