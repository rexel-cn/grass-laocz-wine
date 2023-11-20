package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.GrassRuleNotice;
import com.rexel.system.domain.vo.GrassRuleNoticeVO;

import java.util.List;

/**
 * 报警规则关联通知模板Service接口
 *
 * @author grass-service
 * @date 2022-08-05
 */
public interface IGrassRuleNoticeService extends IService<GrassRuleNotice> {

    /**
     * 查询报警规则关联通知模板列表
     *
     * @param grassRuleNotice 报警规则关联通知模板
     * @return 报警规则关联通知模板集合
     */
    List<GrassRuleNotice> selectGrassRuleNoticeList(GrassRuleNotice grassRuleNotice);

    /**
     * 根据规则id查询模板
     *
     * @param ids
     * @return
     */
    List<GrassRuleNoticeVO> selectGrassRuleNoticeVOByIds(List<String> ids);


    Boolean deleteByTenantId(String tenantId);
}
