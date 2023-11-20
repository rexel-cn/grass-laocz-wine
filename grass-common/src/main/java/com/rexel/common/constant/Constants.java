package com.rexel.common.constant;

/**
 * 通用常量信息
 *
 * @author ids-admin
 */
public class Constants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";
    /**
     * 冒号
     */
    public static final String COLON = ":";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 注册
     */
    public static final String REGISTER = "Register";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 限流 redis key
     */
    public static final String RATE_LIMIT_KEY = "rate_limit:";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * RMI 远程方法调用
     */
    public static final String LOOKUP_RMI = "rmi://";

    /**
     * LDAP 远程方法调用
     */
    public static final String LOOKUP_LDAP = "ldap://";

    public static final String CODE = "code";
    public static final String DATA = "data";
    public static final String REDIS_PREFIX_NSQ_ALARM = "device:run:nsq:alarm:";

    /**
     * 通知配置类型  站内信
     */
    public static final String NOTICE_USER_MAIL = "0";

    /**
     * 通知配置类型  邮箱
     */
    public static final String NOTICE_USER_EMAIL = "1";

    /**
     * 通知配置类型  短信
     */
    public static final String NOTICE_USER_SMS = "2";

    /**
     * 通知配置类型  钉钉
     */
    public static final String NOTICE_USER_DINGDING = "3";

    /**
     * 站内信 0:报警,1:预警
     */
    public static final Long ALARM_DEVICE = 0L;
    public static final Long ALARM_EARLY_WARN = 1L;

    /**
     * 钉钉发送失败
     */
    public static final String ERRCODE = "errcode";

    public static final String USER_AVATAR = "avatar";
}
