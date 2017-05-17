package net.lovexq.seckill.common.utils.constants;

public class AppConstants {

    /**
     * 记录有效状态
     */
    public static final String YES = "Y";

    /**
     * 记录无效状态
     */
    public static final String NO = "N";

    /**
     * 记录有效状态
     */
    public static final String YES_BIT = "1";

    /**
     * 记录无效状态
     */
    public static final String NO_BIT = "0";

    /**
     * 记录有效状态:是
     */
    public static final String YES_CN = "是";

    /**
     * 记录无效状态:否
     */
    public static final String NO_CN = "否";

    /**
     * 设置会话中的登录用户属性
     */
    public static final String USER_IN_SESSION = "sessionUser";

    /**
     * 设置会话中的登录用户属性
     */
    public static final String RANDOM_NUMBER = "randomNumber";

    /**
     * 字符集编码：UTF-8
     */
    public static final String CHARSET_UTF8 = "UTF-8";

    /**
     * 广州链家网址
     */
    public static final String URL_GZ = "http://gz.lianjia.com";

    /**
     * 频道：二手房
     */
    public static final String CHANNEL_ERSHOUFANG = "/ershoufang/";

    /**
     * 未作错误
     */
    public static final Long UNKNOWN_ERROR = -1L;

    /**
     * 默认系统错误
     */
    public static final Long DEFAULT_SYS_ERROR = 10000L;

    /**
     * 默认应用程序错误
     */
    public static final Long DEFAULT_APP_ERROR = 20000L;

    /**
     * JWT的CLAIMS
     */
    public static final String CLAIMS = "CLAIMS";

    /**
     * Token的key
     */
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";

    /**
     * 用户名称
     */
    public static final String USER_NAME = "USER_NAME";

    /**
     * 缓存KEY：访问Token
     */
    public static final String CACHE_ACCESS_TOKEN = "ACCESS_TOKEN:";

    /**
     * 缓存KEY：API关键词
     */
    public static final String CACHE_API_KEY = "API_KEY:";

    /**
     * 缓存KEY：验证码
     */
    public static final String CACHE_CAPTCHA = "CAPTCHA:";

    /**
     * 缓存KEY：秒杀库存数
     */
    public static final String CACHE_SPECIAL_STOCK_COUNT = "SPECIAL_STOCK_COUNT:";

    /**
     * 缓存KEY：秒杀订单列表
     */
    public static final String CACHE_ZSET_SPECIAL_ORDER = "ZSET-SPECIAL_ORDER:";

}