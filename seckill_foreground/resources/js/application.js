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
            return !StringUtil.isBlank(strVal);
        },

        getNewSortType: function (sortType) {
            if (StringUtil.isNotBlank(sortType)) {
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
        },

        dateFormat: function (timestamp) {
            var time = new Date(timestamp);
            var y = time.getFullYear();//年
            var m = time.getMonth() + 1;//月
            var d = time.getDate();//日
            var h = time.getHours();//时
            var mm = time.getMinutes();//分
            var s = time.getSeconds();//秒
            return y + "-" + m + "-" + d + " " + h + ":" + mm + ":" + s;
        }
    };

    $.addTemplateFormatter({
        newEstateFormatter: function (value, template) {
            if (!value) {
                return "display:none";
            }
        }
    });

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

        checkRequestParamObj: function (requestParamObj) {
            if (requestParamObj == null || requestParamObj.length < 1) {
                requestParamObj = {"page": 1, "sort": "id:DESC"};
            }
            return requestParamObj;
        },

        init: function () {
            var requestParamObj = JSON.parse(sessionStorage.getItem("requestParam"));
            requestParamObj = List.checkRequestParamObj(requestParamObj);

            // 选中过滤条件
            List.checkFilterFiled(requestParamObj);

            // 触发排序函数
            var sortValues = requestParamObj.sort.split(":");
            var selector = "li.sort." + sortValues[0];
            List.sortData($(selector)[0], 0);

            // 绑定过滤函数
            $("select").on("change", function () {
                List.filterData(this, 1);
            })

            // 绑定排序函数
            $(".sort").click(function () {
                List.sortData(this, 1);
            })
        },

        goNextPage: function (pageNum) {
            var requestParamObj = JSON.parse(sessionStorage.getItem("requestParam"));
            requestParamObj = List.checkRequestParamObj(requestParamObj);
            requestParamObj.page = pageNum;

            List.loadData(requestParamObj);
        },

        loadData: function (requestParamObj) {
            $.get(List.dataUrl(), requestParamObj).done(function (result) {
                var pageData = result.data;
                if (pageData != null) {

                    $("#countContent").html("<span class='font-size-lg'>[共找到 <strong>" + pageData.totalElements + "</strong> 套广州二手房]</span>")

                    $("#listing").loadTemplate("#listData", pageData.content, {
                        success: function () {
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
                } else {
                    $('#countContent').empty();
                    $("#listing").html("<h2>暂无符合条件的数据！</h2>");
                    $('#pagination').empty();
                }
            }).fail(function (result) {
                Common.PNotice.error(result.message);
            });

            sessionStorage.setItem("requestParam", JSON.stringify(requestParamObj));
        },

        checkFilterFiled: function (requestParamObj) {
            $.each($("select.form-control"), function (index, obj) {
                var selectValue = requestParamObj[obj.name];
                if (Common.StringUtil.isNotBlank(selectValue)) {
                    obj.value = selectValue;
                } else {
                    delete requestParamObj[obj.name];
                }
            })
        },

        filterData: function (obj) {
            var requestParamObj = JSON.parse(sessionStorage.getItem("requestParam"));
            requestParamObj = List.checkRequestParamObj(requestParamObj);

            var selectName = obj.name;
            var selectValue = obj.options[obj.selectedIndex].value;
            if (Common.StringUtil.isNotBlank(selectValue)) {
                requestParamObj[selectName] = selectValue;
            } else {
                delete requestParamObj[selectName];
            }
            requestParamObj.page = 1;
            List.loadData(requestParamObj);
        },

        sortData: function (obj, type) {
            // 清空之前的排序按钮
            $("li.sort").removeClass("active");
            $("li.sort >a > i").remove();

            // 选中对应排序按钮
            var classNames = obj.className.split(" ");
            var selector = "li.sort." + classNames[1];
            $(selector).addClass("active");

            // 添加升降序箭头
            var html = '<a href="#">' + $(selector).attr("data-loading-text")
            var arrowIcon = " <i class='fa fa-long-arrow-up'></i>";

            var requestParamObj = JSON.parse(sessionStorage.getItem("requestParam"));
            requestParamObj = List.checkRequestParamObj(requestParamObj);
            var sortValues = requestParamObj.sort.split(":");
            var sortType = sortValues[1];
            if (1 == type) {
                sortType = Common.StringUtil.getNewSortType(sortType);
            }
            if ("DESC" == sortType) {
                arrowIcon = " <i class='fa fa-long-arrow-down'></i>";
            }
            html = html + arrowIcon + '</a>';
            $(selector).html(html);

            requestParamObj.sort = classNames[1] + ":" + sortType;
            List.loadData(requestParamObj);
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
                    success: function () {
                        Common.Lazyload.init();
                    }
                });
            }).fail(function (result) {
                Common.PNotice.error(result.responseJSON.message);
            });
        }
    };

    var Detail = {
        nowTimeUrl: function () {
            return "/special/nowTime";
        },

        exposureUrl: function (id) {
            return '/special/' + id + '/exposure';
        },

        executionUrl: function (id, key) {
            return '/special/' + id + '/execution/' + key;
        },

        changeCaptcha: function () {
            $('#captcha-image').attr('src', '/special/captcha?t=' + new Date().getTime());
        },

        init: function (params) {
            var id = params['id'];
            var startTimeX = params['startTimeX'];
            var endTimeX = params['endTimeX'];

            // 获取系统当前时间
            $.get(Detail.nowTimeUrl()).done(function (result) {
                if (200 == result.status) {
                    Detail.countDownFun(id, result.data, startTimeX, endTimeX);
                } else {
                    Common.PNotice.error("受限内容，请登录后再操作！");
                }
            }).fail(function (result) {
                Common.PNotice.error(result.responseJSON.message);
            });
        },

        countDownFun: function (id, nowTimeX, startTimeX, endTimeX) {
            var countDownArea = $("#countDownArea");

            // 秒杀结束
            if (nowTimeX > endTimeX) {
                countDownArea.html('<a href="javascript:void(0)" class="btn btn-lg btn-primary ml-lg" disabled>秒杀已结束</a>');

                // 秒杀未开始
            } else if (nowTimeX < startTimeX) {
                countDownArea.countdown(startTimeX, function (event) {
                    var htmlContent = '<h3>开始时间：<strong class="font-red">' + Common.StringUtil.dateFormat(startTimeX) + '</strong>';
                    htmlContent += '<a href="javascript:void(0)" class="btn btn-lg btn-primary ml-lg" disabled>即将开始</a></h3>';
                    htmlContent += '<h4>' + event.strftime('距离秒杀开启: %H时 %M分 %S秒') + '</h4>';
                    countDownArea.html(htmlContent);
                }).on('finish.countdown', function () {
                    Detail.captchaFun(id, nowTimeX, startTimeX, endTimeX);
                });

                // 秒杀进行中
            } else {
                Detail.captchaFun(id, nowTimeX, startTimeX, endTimeX);
            }
        },

        captchaFun: function (id, nowTimeX, startTimeX, endTimeX) {
            var countDownArea = $("#countDownArea");

            $.get(Detail.exposureUrl(id)).done(function (result) {
                if (200 == result.status) {
                    // 显示倒计时区域
                    countDownArea.countdown(endTimeX, function (event) {
                        var htmlContent = '<h3>结束时间：<strong class="font-red">' + Common.StringUtil.dateFormat(endTimeX) + '</strong>';
                        htmlContent += '<a href="javascript:void(0)" class="btn btn-lg btn-primary ml-lg" onclick="Special.Detail.refreshFun()">刷新抢房</a></h3>';
                        htmlContent += '<h4>' + event.strftime('距离秒杀结束: %H时 %M分 %S秒') + '</h4>';
                        countDownArea.html(htmlContent);
                    }).on('finish.countdown', function () {
                        var htmlContent = '<a href="javascript:void(0)" class="btn btn-lg btn-primary ml-lg" disabled>秒杀已结束</a>';
                        countDownArea.html(htmlContent);
                    });

                    // 显示秒杀区域
                    $("section").show();

                    $("#secKillUrl").val(Detail.executionUrl(id, result.data));
                    $("#killBtn").click(Detail.execSecKillFun);
                } else if (403 == result.status) {
                    var htmlContent = '<a href="javascript:void(0)" class="btn btn-lg btn-primary ml-lg" disabled>' + result.message + '</a>';
                    countDownArea.html(htmlContent);
                    Common.PNotice.error(result.message);
                } else {
                    var htmlContent = '<a href="javascript:void(0)" class="btn btn-lg btn-primary ml-lg" disabled>' + result.message + '</a>';
                    countDownArea.html(htmlContent);
                    Common.PNotice.error(result.message);
                }
            }).fail(function (result) {
                var htmlContent = '<a href="javascript:void(0)" class="btn btn-lg btn-primary ml-lg" disabled>' + result.responseJSON.message + '</a>';
                countDownArea.html(htmlContent);
                Common.PNotice.error(result.responseJSON.message);
            });

        },

        refreshFun: function () {
            Detail.changeCaptcha();
            $("#captcha").val("");
        },

        execSecKillFun: function () {
            var userName = $.cookie("USER_NAME");
            if (Common.StringUtil.isBlank(userName)) {
                Common.PNotice.error("受限内容，请登录后再操作！");
                return false;
            }

            var captcha = $("#captcha").val();
            if (Common.StringUtil.isBlank(captcha)) {
                Common.PNotice.error("请输入正常的答案！");
                return false;
            }

            // 发出秒杀请求
            $.post($("#secKillUrl").val(), {"captcha": captcha}).done(function (result) {
                if (200 == result.status) {
                    $("#section2").hide();
                    $("#countDownArea").countdown('stop');
                    var htmlContent = '<a href="javascript:void(0)" class="btn btn-lg btn-primary ml-lg" disabled>' + result.message + '</a>';
                    $("#countDownArea").html(htmlContent);
                    Common.PNotice.success(result.message);
                } else {
                    Detail.changeCaptcha();
                    Common.PNotice.error(result.message);
                }
            }).fail(function (result) {
                Detail.changeCaptcha();
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