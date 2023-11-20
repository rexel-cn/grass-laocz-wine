package com.rexel.send.factory;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rexel.common.constant.Constants;
import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.common.utils.http.HttpUtils;
import com.rexel.system.domain.GrassNoticeDingding;
import com.rexel.system.domain.GrassUserSmsMailboxDingding;
import com.rexel.system.service.IGrassNoticeDingdingService;
import com.rexel.system.service.IGrassUserSmsMailboxDingdingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName DingDingSend
 * @Description 钉钉发送信息
 * 参考钉钉文档 https://ding-doc.dingtalk.com/doc#/serverapi2/qf2nxq
 * @Author 董海
 * @Date 2020/6/23 14:41
 **/
@Slf4j
@Component
public class DingDingSend extends PushAbstract {
    @Autowired
    private IGrassNoticeDingdingService dingdingService;
    @Autowired
    private IGrassUserSmsMailboxDingdingService smsMailboxDingdingService;

    /**
     * 获取签名
     *
     * @param timestamp 时间戳
     * @param secret    秘钥
     * @return String
     */
    private static String getSign(Long timestamp, String secret) {
        String stringToSign = timestamp + "\n" + secret;
        String sign = "";
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            log.error("[获取阿里云钉钉签名失败]");
        }
        return sign;
    }

    /**
     * 开始发送
     *
     * @param sysUserList 用户
     * @param format      点位运行数据
     * @param noticeType  通知类型
     */
    @Override
    public void startSend(List<SysUser> sysUserList, String format, Long noticeType) {
        // 获取租户ID
        String tenantId = getTenantId(sysUserList);

        // 检查通知是否开启
        if (!isOpen(tenantId, Constants.NOTICE_USER_DINGDING)) {
            return;
        }

        // 查询钉钉配置
        List<GrassNoticeDingding> dingTalkList = selectDingTalkList(tenantId);

        // 检查钉钉配置
        if (dingTalkList.size() == 0) {
            return;
        }

        // 获取通知内容
        String sendContent = getSendContent(sysUserList, format);

        // 遍历所有钉钉群
        for (GrassNoticeDingding dintTalk : dingTalkList) {
            // 发送告警通知
            String sendResult = doSendMsg(dintTalk, sendContent);

            // 存储通知历史
            insertMsgData(tenantId, sysUserList, sendContent, sendResult);
        }
    }

    /**
     * 开始发送   短信和站内性相对  邮箱和钉钉有所区别，因此重载
     *
     * @param sysUserList sysUserList
     * @param format      format
     * @param policeLevel policeLevel
     * @param noticeType  提供类型  --站内信  需要
     */
    @Override
    public void startSend(List<SysUser> sysUserList, String format, String policeLevel, Long noticeType) {

    }

    /**
     * 构建请求的JSON数据
     *
     * @param content xx
     * @return JSONObject
     */
    private JSONObject bodyJson(String content) {
        JSONObject textJson = new JSONObject();
        textJson.put("content", content);
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("msgtype", "text");
        bodyJson.put("text", textJson);
        return bodyJson;
    }

    /**
     * 查询钉钉配置
     *
     * @param tenantId tenantId
     * @return 结果
     */
    private List<GrassNoticeDingding> selectDingTalkList(String tenantId) {
        return dingdingService.selectByTenantId(tenantId);
    }

    /**
     * 获取通知的租户ID
     *
     * @param sysUserList sysUserList
     * @return 结果
     */
    private String getTenantId(List<SysUser> sysUserList) {
        return sysUserList.get(0).getTenantId();
    }

    /**
     * 获取发送内容
     *
     * @param sysUserList sysUserList
     * @param format      format
     * @return 结果
     */
    private String getSendContent(List<SysUser> sysUserList, String format) {
        List<String> userNameList = new ArrayList<>();
        for (SysUser sysUser : sysUserList) {
            if (!userNameList.contains(sysUser.getUserName())) {
                userNameList.add(sysUser.getUserName());
            }
        }
        String userNameStr = StringUtils.join(userNameList.toArray(), ",");
        AtomicReference<String> sendContent = new AtomicReference<>("");
        sendContent.set(MessageFormat.format(format, userNameStr));
        return sendContent.get();
    }

    /**
     * 执行通知消息发送
     *
     * @param dintTalk    dintTalk
     * @param sendContent sendContent
     * @return 结果
     */
    private String doSendMsg(GrassNoticeDingding dintTalk, String sendContent) {
        JSONObject jsonObject = bodyJson(sendContent);
        Long timestamp = System.currentTimeMillis();
        String sign = getSign(timestamp, dintTalk.getSecret());
        String url = dintTalk.getWebhook() + "&timestamp=" + timestamp + "&sign=" + sign;
        return HttpUtils.sendPostJson(url, jsonObject.toJSONString());
    }

    /**
     * 插入发送结果
     *
     * @param tenantId    tenantId
     * @param sysUserList sysUserList
     * @param sendContent sendContent
     * @param sendResult  sendResult
     */
    private void insertMsgData(String tenantId, List<SysUser> sysUserList, String sendContent, String sendResult) {
        // 获取通知用户列表
        List<String> userIdList = new ArrayList<>();
        for (SysUser sysUser : sysUserList) {
            String userId = sysUser.getUserId().toString();
            if (!userIdList.contains(userId)) {
                userIdList.add(userId);
            }
        }
        String userIdStr = StringUtils.join(userIdList.toArray(), ",");

        // 获取发送结果异常
        JSONObject sendResultJson = JSON.parseObject(sendResult);

        // 添加记录表
        GrassUserSmsMailboxDingding userSmsMailboxDingding = new GrassUserSmsMailboxDingding();
        userSmsMailboxDingding.setPushType(Constants.NOTICE_USER_DINGDING);
        userSmsMailboxDingding.setTenantId(tenantId);
        userSmsMailboxDingding.setUserId(userIdStr);
        userSmsMailboxDingding.setSendContent(sendContent);
        if (sendResultJson.getInteger(Constants.ERRCODE) == 0) {
            userSmsMailboxDingding.setSendRes(Constants.SUCCESS);
        } else {
            userSmsMailboxDingding.setSendRes(Constants.FAIL);
            userSmsMailboxDingding.setFailReason(sendResult);
        }
        smsMailboxDingdingService.save(userSmsMailboxDingding);
    }
}
