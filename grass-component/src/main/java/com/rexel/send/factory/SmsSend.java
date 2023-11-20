package com.rexel.send.factory;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.rexel.common.constant.Constants;
import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.common.utils.RexelPropertiesUtil;
import com.rexel.nsq.rule.bean.AliConfigPropertiesBean;
import com.rexel.nsq.rule.util.AliiotPropertiesUtil;
import com.rexel.system.domain.GrassNoticeSms;
import com.rexel.system.domain.GrassUserSmsMailboxDingding;
import com.rexel.system.service.IGrassNoticeSmsService;
import com.rexel.system.service.IGrassUserSmsMailboxDingdingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @ClassName SmsSend
 * @Description 短信发送
 * @Author 董海
 * @Date 2020/6/23 14:41
 **/
@Slf4j
@Component
public class SmsSend extends PushAbstract {
    public static final String ALIYUN_REQUEST_OK = "OK";
    public static final String ALIYUN_REQUEST_CODE = "Code";

    @Autowired
    private AliiotPropertiesUtil aliiotPropertiesUtil;
    @Autowired
    private AliConfigPropertiesBean aliConfigPropertiesBean;
    @Autowired
    private IGrassNoticeSmsService smsService;
    @Autowired
    private IGrassUserSmsMailboxDingdingService smsMailboxDingdingService;

    @Override
    public void startSend(List<SysUser> userIdList, String format, Long noticyType) {
        //获取阿里云accessKeyId 信息
        AliiotPropertiesUtil instance = aliiotPropertiesUtil.getInstance();
        // 获取客户端
        IAcsClient client = instance.getIotClient(aliConfigPropertiesBean);
        //获取请求头
        CommonRequest request = getCommonRequest();
        /* 阿里云配置短信规则
         * 尊敬的${name}, 设备触发报警，请及时处理。设备：${deviceName}, 告警内容：${alarmDetail}
         * */
        JSONObject templateContent = JSON.parseObject(format);
        userIdList.forEach(a -> {
            if (isOpen(a.getTenantId(), Constants.NOTICE_USER_SMS)) {
                templateContent.put("name", a.getUserName());
                request.putQueryParameter("PhoneNumbers", a.getPhoneNumber());
                GrassNoticeSms iotNoticeSms = smsService.selectOneByTenantId(a.getTenantId());
                if (iotNoticeSms == null) {
                    return;
                }
                request.putQueryParameter("SignName", iotNoticeSms.getSignName());
                request.putQueryParameter("TemplateCode", iotNoticeSms.getTemplateCode());
                request.putQueryParameter("TemplateParam", templateContent.toJSONString());
                GrassUserSmsMailboxDingding userSmsMailboxDingding = new GrassUserSmsMailboxDingding();
                try {
                    userSmsMailboxDingding.setPushType(Constants.NOTICE_USER_SMS);
                    userSmsMailboxDingding.setTenantId(a.getTenantId());
                    userSmsMailboxDingding.setUserId(a.getUserId().toString());
                    userSmsMailboxDingding.setSendContent(templateContent.toJSONString());
                    //开始进行发送
                    CommonResponse response = client.getCommonResponse(request);
                    if (!ALIYUN_REQUEST_OK.equals(
                            JSON.parseObject(response.getData()).getString(
                                    ALIYUN_REQUEST_CODE))) {
                        userSmsMailboxDingding.setSendRes(Constants.FAIL);
                        userSmsMailboxDingding.setFailReason(response.toString());
                    }
                } catch (ClientException e) {
                    userSmsMailboxDingding.setSendRes(Constants.FAIL);
                    userSmsMailboxDingding.setFailReason(e.getMessage().substring(0, 50));
                    log.error("[告警通知短信发送异常:]", e);
                } finally {
                    //添加记录表
                    smsMailboxDingdingService.save(userSmsMailboxDingding);
                }
            }
        });
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
     * 构建请求体
     *
     * @return CommonRequest
     */
    private CommonRequest getCommonRequest() {
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(aliConfigPropertiesBean.getSmsDomain());
        request.setSysVersion(aliConfigPropertiesBean.getSmsVersion());
        request.setSysAction(RexelPropertiesUtil.getKey("aliaction.properties", "SendSms"));
        request.putQueryParameter("RegionId", aliConfigPropertiesBean.getRegionId());
        return request;
    }

}