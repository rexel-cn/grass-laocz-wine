package com.rexel.send.factory;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONObject;
import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.common.enums.SendContentTemplateEnum;
import com.rexel.common.utils.StringUtils;
import com.rexel.nsq.rule.domain.dto.SendDTO;
import com.rexel.send.constant.PushConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ChoiceFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName PushFactory
 * @Description 推送类型工厂
 * Author 孟开通
 * Date 2022/8/9 17:27
 **/
@Slf4j
@Component
public class PushFactory {
    @Autowired
    private MessageSend messageSend;
    @Autowired
    private DingDingSend dingDingSend;
    @Autowired
    private MailboxSend mailboxSend;
    @Autowired
    private SmsSend smsSend;

    /**
     * 创建发送句柄
     *
     * @param type type
     * @return 结果
     */
    public Push creatPush(String type) {
        Push push = null;
        switch (type) {
            case PushConstants.PUSH_MESSAGE_SEND:
                push = messageSend;
                break;
            case PushConstants.PUSH_MAILBOX_SEND:
                push = mailboxSend;
                break;
            case PushConstants.PUSH_SMS_SEND:
                push = smsSend;
                break;
            case PushConstants.PUSH_DING_DING_SEND:
                push = dingDingSend;
                break;
            default:
                break;
        }
        return push;
    }

    /**
     * 开始进行发送
     *
     * @param userIdList userIdList
     * @param sendType sendType
     * @param sendContent sendContent
     * @param noticeType noticeType
     */
    public void startSend(List<SysUser> userIdList, List<String> sendType, String sendContent, Long noticeType) {
        String userIdArray = Arrays.toString(userIdList.stream().map(SysUser::getUserId).toArray(Long[]::new));
        for (String s : sendType) {
            Push push = this.creatPush(s);
            try {
                if (push instanceof MessageSend) {
                    push.startSend(userIdList, sendContent, null, noticeType);
                } else {
                    push.startSend(userIdList, sendContent, noticeType);
                }
            } catch (Exception e) {
                log.error("报警通知异常，报警方式:{},报警租户:{},报警内容:{}", s, userIdArray, sendContent);
            }
        }
    }

    /**
     * 开始进行发送
     *
     * @param userIdList userIdList
     * @param sendType sendType
     * @param sendDTO sendDTO
     * @param sendContentTemplateEnum jsonObject
     * @param noticeType noticeType
     */
    public void startSend(List<SysUser> userIdList, List<String> sendType,
                          SendDTO sendDTO, SendContentTemplateEnum sendContentTemplateEnum, Long noticeType) {
        String userIdArray = Arrays.toString(userIdList.stream().map(SysUser::getUserId).toArray(Long[]::new));
        for (String s : sendType) {
            Push push = this.creatPush(s);
            try {
                String sendContent = buildSendContent(sendContentTemplateEnum, sendDTO, push);
                if (push instanceof MessageSend) {
                    push.startSend(userIdList, sendContent, sendDTO.getRulesLevel(), noticeType);
                } else {
                    push.startSend(userIdList, sendContent, noticeType);
                }
            } catch (Exception e) {
                log.error("报警通知异常，报警方式:{},报警租户:{},报警内容:{}", s, userIdArray, sendDTO);
            }
        }
    }

    /**
     * 构建发送内容
     *
     * @param sendContentTemplateEnum sendContentTemplateEnum
     * @param sendDTO                 sendDTO
     * @param push                    push
     * @return 结果
     */
    private String buildSendContent(SendContentTemplateEnum sendContentTemplateEnum, SendDTO sendDTO, Push push) {
        if (sendContentTemplateEnum.getCode().equals(SendContentTemplateEnum.DEVICE_RULES_ALARM_CONTENT_TEMPLATE.getCode())) {
            if (push instanceof SmsSend) {
                JSONObject templateContent = new JSONObject();
                templateContent.put("equipmentName", sendDTO.getEquipmentName());
                templateContent.put("alarmDetail", sendDTO.getNoticeContent());
                return templateContent.toJSONString();
            }
            return getContentByRulesAlarm(sendDTO);
        }
        return null;
    }

    private String getContentByRulesAlarm(SendDTO sendDTO) {
        double[] fileLimits = {0, 1};
        String[] filePart = {"", SendContentTemplateEnum.DEVICE_RULES_ALARM_CONTENT_TEMPLATE_CHOOSE.getInfo()};
        ChoiceFormat fileForm = new ChoiceFormat(fileLimits, filePart);
        Format[] testFormats = {null, null, null, null, fileForm, null};
        MessageFormat pattForm = new MessageFormat(SendContentTemplateEnum.DEVICE_RULES_ALARM_CONTENT_TEMPLATE.getInfo());
        pattForm.setFormats(testFormats);
        Object[] testArgs = {"{0}", sendDTO.getPointName(), DateUtil.format(sendDTO.getCurrTime(),
                DatePattern.NORM_DATETIME_FORMAT), sendDTO.getRtVal(), null, null};
        String silentCycle = sendDTO.getSilentCycle();
        if (StringUtils.isNotEmpty(silentCycle)) {
            testArgs[4] = 1;
        } else {
            testArgs[4] = 0;
        }
        testArgs[5] = silentCycle;
        return pattForm.format(testArgs);
    }
}