package com.rexel.message.service;

import com.rexel.message.domain.GrassUserMessageFramework;

/**
 * @ClassName IGrassUserMessageFrameworkApi
 * @Description
 * @Author 孟开通
 * @Date 2023/1/31 11:16
 **/
public interface IGrassUserMessageFrameworkApi {
    int selectCountByGrassUserMessage(GrassUserMessageFramework userMessageQueryPo);
}
