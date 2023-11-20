package com.rexel.send.factory;

import com.rexel.common.core.domain.entity.SysUser;

import java.util.List;


/**
 * 工厂方法抽象类
 * Author 孟开通
 * Date 2022/8/9 17:27
 */
public interface Push {
    /**
     * 开始发送
     *
     * @param sysUserList 用户
     * @param format      点位运行数据
     * @param noticeType  通知类型
     */
    void startSend(List<SysUser> sysUserList, String format, Long noticeType);

    /**
     * 开始发送   短信和站内性相对  邮箱和钉钉有所区别，因此重载
     *
     * @param sysUserList sysUserList
     * @param format      format
     * @param policeLevel policeLevel
     * @param noticeType  提供类型  --站内信  需要
     */
    void startSend(List<SysUser> sysUserList, String format, String policeLevel, Long noticeType);

    /**
     * 发送开关是否开启
     *
     * @param tenantId tenantId
     * @return 结果
     */
    boolean isOpen(String tenantId, String pushType);
}
