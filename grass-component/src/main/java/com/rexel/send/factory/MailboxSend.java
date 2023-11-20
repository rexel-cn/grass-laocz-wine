package com.rexel.send.factory;

import com.rexel.common.constant.Constants;
import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.common.utils.StringUtils;
import com.rexel.system.domain.GrassNoticeMailbox;
import com.rexel.system.domain.GrassUserSmsMailboxDingding;
import com.rexel.system.service.IGrassNoticeMailboxService;
import com.rexel.system.service.IGrassUserSmsMailboxDingdingService;
import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.security.GeneralSecurityException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @ClassName MailboxSend
 * @Description 邮箱发送
 * Author 孟开通
 * Date 2022/8/9 17:27
 **/
@Slf4j
@Component
public class MailboxSend extends PushAbstract {
    @Autowired
    private IGrassNoticeMailboxService mailboxService;
    @Autowired
    private IGrassUserSmsMailboxDingdingService smsMailboxDingdingService;

    @Override
    public void startSend(List<SysUser> userIdList, String format, Long noticeType) {
        for (SysUser a : userIdList) {
            //根据发送开关状态进行发送
            if (isOpen(a.getTenantId(), Constants.NOTICE_USER_EMAIL)) {
                GrassNoticeMailbox grassNoticeMailbox = mailboxService.selectOneByTenantId(a.getTenantId());
                if (grassNoticeMailbox == null || StringUtils.isEmpty(a.getEmail())) {
                    continue;
                }
                MimeMessage mimeMessage = getMimeMessage(grassNoticeMailbox, a, format);
                GrassUserSmsMailboxDingding userSmsMailboxDingding = null;
                try {
                    userSmsMailboxDingding = new GrassUserSmsMailboxDingding();
                    userSmsMailboxDingding.setPushType(Constants.NOTICE_USER_EMAIL);
                    userSmsMailboxDingding.setTenantId(a.getTenantId());
                    userSmsMailboxDingding.setUserId(a.getUserId().toString());
                    userSmsMailboxDingding.setSendContent(MessageFormat.format(format, a.getUserName()));
                    //进行发送
                    Transport.send(mimeMessage);
                } catch (MessagingException e) {
                    e.printStackTrace();
                    userSmsMailboxDingding.setSendRes(Constants.FAIL);
                    userSmsMailboxDingding.setFailReason(e.getMessage().substring(0, 50));
                    log.error("[邮件发送异常]", e);
                } finally {
                    //添加记录表
                    smsMailboxDingdingService.save(userSmsMailboxDingding);
                }
            }
        }
    }

    @Override
    public void startSend(List<SysUser> userIdList, String format, String policeLevel, Long noticyType) {
    }

    /**
     * @param selectIotNoticeMailbox xx
     * @param a                      用户
     * @param format                 发送模板
     * @return MimeMessage
     */
    private MimeMessage getMimeMessage(GrassNoticeMailbox selectIotNoticeMailbox, SysUser a, String format) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        // smtp.exmail.qq.com： 112.60.20.192  （注意linux - docker 容器环境里，需要注意。公对公 与 公对私的 有区别）
        properties.put("mail.smtp.host", selectIotNoticeMailbox.getSmtpServerUrl());
        properties.put("mail.smtp.port", selectIotNoticeMailbox.getSmtpServerPort());
        //   properties.put("mail.transport.protocol", "ssl");
        //企业邮箱必须要SSL
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (GeneralSecurityException e1) {
            e1.printStackTrace();
        }
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);

        //
        Authenticator authenticator = new EmailAuthenticator(
                selectIotNoticeMailbox.getMailboxAddress()
                , selectIotNoticeMailbox.getServerPassword());
        Session sendMailSession = Session.getInstance(properties, authenticator);
        MimeMessage mimeMessage = new MimeMessage(sendMailSession);
        try {
            mimeMessage.setFrom(new InternetAddress(selectIotNoticeMailbox.getMailboxAddress()));
            mimeMessage.setSubject("告警通知", "UTF-8");
            mimeMessage.setSentDate(new Date());
            // Message.RecipientType.TO属性表示接收者的类型为TO
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(a.getEmail()));
            // 创建一个包含HTML内容的MimeBodyPart
            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart mainPart = new MimeMultipart();
            BodyPart html = new MimeBodyPart();
            //设置发送内容
            html.setContent(MessageFormat.format(format, a.getUserName()), "text/html; charset=utf-8");
            mainPart.addBodyPart(html);
            mimeMessage.setContent(mainPart);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return mimeMessage;
    }


    private static class EmailAuthenticator extends Authenticator {
        String userName;
        String password;

        EmailAuthenticator(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            //返回发件人账号与密码信息
            return new PasswordAuthentication(userName, password);
        }
    }

}