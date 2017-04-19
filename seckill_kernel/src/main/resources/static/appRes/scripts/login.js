var Login = function () {
    var handleLogin = function () {
        $('.login-form').validate({
            errorElement: 'span', //default input error message container
            errorClass: 'help-block', // default input error message class
            focusInvalid: false, // do not focus the last invalid input
            rules: {
                account: {
                    required: true
                },
                password: {
                    required: true
                },
                remember: {
                    required: false
                }
            },
            messages: {
                account: {
                    required: "账号不能为空！"
                },
                password: {
                    required: "密码不能为空！"
                }
            },

            invalidHandler: function (event, validator) { //display error alert on form submit
                $('.alert-danger', $('.login-form')).show();
                $("#msg").text("请输入账号和密码！");
            },

            highlight: function (element) { // hightlight error inputs
                $(element).closest('.form-group').addClass('has-error'); // set error class to the control group
            },

            success: function (label) {
                label.closest('.form-group').removeClass('has-error');
                label.remove();
            },

            errorPlacement: function (error, element) {
                error.insertAfter(element.closest('.input-icon'));
            },

            submitHandler: function (form) {
                //form.submit(); // form validation success, call ajax form submit
                ajaxLogin(false)
            }
        });

        $('.login-form input').keypress(function (e) {
            if (e.which == 13) {
                if ($('.login-form').validate().form()) {
                    //$('.login-form').submit(); //form validation success, call ajax form submit
                    ajaxLogin(false);
                }
                return false;
            }
        });
    }

    return {
        //main function to initiate the module
        init: function () {
            handleLogin();
        }
    };

}();

jQuery(document).ready(function () {
    Login.init();
    // 初始化cookie数据
    var loginAccount = Cookies.get('loginAccount');
    if (!!loginAccount) {
        $("input[name='account']").val(loginAccount);
        var loginPass = Cookies.get('loginPass');
        if (!!loginPass) {
            var loginLegth = Cookies('loginLegth');
            if (!!loginLegth) {
                //$("input[name='password']").val(loginPass);
                ajaxLogin(true, loginAccount, loginPass)
            }
        }
        $("input[name='remember']").attr("checked", true);
        $("input[name='password']").focus();
    } else {
        $("input[name='account']").focus();
    }
    alertAutoLogin();
});


/**
 * 登录
 */
// 获取随机码
function ajaxLogin(isAuto, account, password) {
    var randomNum, data;

    $.ajaxSetup({
        async: false
    });

    $.get('/getRandomNum?t=' + new Date().getTime(), function (response) {
        console.log(response.data);
        randomNum = response.data;
    });

    if (!isAuto) {
        // 禁用按钮
        disableButton();

        var loginAccount = $.trim($("input[name='account']").val());
        var loginPass = $.trim($("input[name='password']").val());
        var md5Value = SparkMD5.hash(SparkMD5.hash(loginAccount, false) + SparkMD5.hash(loginPass, false), false);

        // 检查是否打勾记住密码
        var checked = $("input[name='remember']").is(":checked");

        if (checked) {
            Cookies.set('loginAccount', loginAccount, {expires: 30});
            Cookies.set('loginPass', md5Value, {expires: 30});
            Cookies.set('loginLegth', loginPass.length, {expires: 30});
        } else {
            Cookies.remove('loginAccount');
            Cookies.remove('loginPass');
            Cookies.remove('loginLegth');
        }

        data = {
            account: loginAccount,
            password: SparkMD5.hash(md5Value + randomNum, false)
        }
    } else {
        data = {
            account: account,
            password: SparkMD5.hash(password + randomNum, false)
        }
    }

    // 执行登录操作
    $.post('login', data, function (response) {
        if (!!response) {
            if (response.success) {
                window.location.href = '/';
            } else {
                // 登录失败,去掉cookie的密码
                Cookies.remove('loginPass');
                Cookies.remove('loginLegth');
                showLoginInfo(response.message);
            }
        }
        enableButton();
    })
}

/**
 * 在登录按钮下面显示登录异常信息
 * @param msg
 */
function showLoginInfo(msg) {
    $('.alert-danger', $('.login-form')).show();
    $("#msg").text(msg);
}

/**
 * 禁用按钮
 */
function disableButton() {
    $('.alert-danger', $('.login-form')).hide();
    $("#msg").text("");
    $('#btnSubmit').attr("disabled", "disabled");
    $('#btnSubmit').text("登录中...");
}

/**
 * 启用按钮
 */
function enableButton() {
    $('#btnSubmit').removeAttr("disabled");
    $('#btnSubmit').text("登录");
}

/**
 * 警告自动登录
 */
function alertAutoLogin() {
    var isChecked = $("input[name='remember']").is(":checked");
    if (isChecked) {
        $('.alert-danger', $('.login-form')).show();
        $("#msg").text("公共场所请勿自动登录，以防账号丢失！");
    } else {
        $('.alert-danger', $('.login-form')).hide();
        $("#msg").text("");
    }
}