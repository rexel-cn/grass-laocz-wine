package com.rexel.common.constant;

/**
 * 用户常量信息
 *
 * @author ids-admin
 */
public class UserConstants {
    /**
     * 部门正常状态
     */
    public static final String DEPT_NORMAL = "0";

    /**
     * 是否为系统默认（是）
     */
    public static final String YES = "Y";

    /**
     * 是否菜单外链（是）
     */
    public static final String YES_FRAME = "0";

    /**
     * 是否菜单外链（否）
     */
    public static final String NO_FRAME = "1";

    /**
     * 菜单类型（目录）
     */
    public static final String TYPE_DIR = "M";

    /**
     * 菜单类型（菜单）
     */
    public static final String TYPE_MENU = "C";

    /**
     * Layout组件标识
     */
    public final static String LAYOUT = "Layout";

    /**
     * ParentView组件标识
     */
    public final static String PARENT_VIEW = "ParentView";

    /**
     * InnerLink组件标识
     */
    public final static String INNER_LINK = "InnerLink";

    /**
     * 校验返回结果码
     */
    public final static String UNIQUE = "0";
    public final static String NOT_UNIQUE = "1";

    /**
     * pulse token
     */
    public static final String PULSE_TOKEN = "pulse:token";

    public static final Integer PULSE_TOKEN_TIME_OUT = 30;
    /**
     * 推送方式 次数
     */
    public static final int SEND_TYPE_COUNT = 4;
    /**
     * 通知范围：个人
     */
    public static final String NOTICE_SCOPE_PERSON = "0";
    /**
     * 通知范围：角色
     */
    public static final String NOTICE_SCOPE_ROLE = "1";

    /**
     * 租户类别：超级管理员
     */
    public static final String TENANT_TYPE_SUPER = "0";
    /**
     * 租户类别：平台管理管理员
     */
    public static final String TENANT_TYPE_PLATFORM_MANAGE = "1";
    /**
     * 租户类别：租户管理员
     */
    public static final String TENANT_TYPE_NORMAL_MANAGER = "2";

    /**
     * 用户类别：超级管理员
     */
    public static final String USER_TYPE_SUPER = "0";
    /**
     * 用户类别： 平台管理员
     */
    public static final String USER_TYPE_PLATFORM_MANAGER = "1";
    /**
     * 用户类别：租户管理员
     */
    public static final String USER_TYPE_TENANT_MANAGER = "2";
    /**
     * 用户类别：租户用户
     */
    public static final String USER_TYPE_TENANT_USER = "3";
}
