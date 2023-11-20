package com.rexel.system.service.impl.framework.message;

import cn.hutool.core.bean.BeanUtil;
import com.rexel.message.domain.GrassUserMessageFramework;
import com.rexel.message.service.IGrassUserMessageFrameworkApi;
import com.rexel.system.service.IGrassUserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName IGrassUserMessageFrameworkApiImpl
 * @Description
 * @Author 孟开通
 * @Date 2023/1/31 11:29
 **/
@Service
public class IGrassUserMessageFrameworkApiImpl implements IGrassUserMessageFrameworkApi {
    @Autowired
    private IGrassUserMessageService iGrassUserMessageService;

    /**
     * @param userMessageQueryPo
     * @return
     */
    @Override
    public int selectCountByGrassUserMessage(GrassUserMessageFramework userMessageQueryPo) {
        return iGrassUserMessageService.selectCountByGrassUserMessage(BeanUtil.copyProperties(userMessageQueryPo, com.rexel.system.domain.GrassUserMessage.class));
    }
}
