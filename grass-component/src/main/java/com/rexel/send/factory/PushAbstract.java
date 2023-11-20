package com.rexel.send.factory;

import com.rexel.system.domain.GrassNoticeConfig;
import com.rexel.system.service.IGrassNoticeConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * ClassName PushAbstract
 * Description
 * Author 孟开通
 * Date 2022/8/9 17:27
 **/
@Component
public abstract class PushAbstract implements Push {
    @Autowired
    private IGrassNoticeConfigService iGrassNoticeConfigService;

    @Override
    public boolean isOpen(String tenantId, String pushType) {
        GrassNoticeConfig noticeConfig = new GrassNoticeConfig();
        noticeConfig.setTenantId(tenantId);
        noticeConfig.setPushType(pushType);
        GrassNoticeConfig config = iGrassNoticeConfigService.selectOneByPushType(noticeConfig);
        return config != null && !"0".equals(config.getIsOpen());
    }
}
