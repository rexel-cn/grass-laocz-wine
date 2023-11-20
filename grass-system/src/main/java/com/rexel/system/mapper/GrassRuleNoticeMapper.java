package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.GrassRuleNotice;
import com.rexel.system.domain.vo.GrassRuleNoticeVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 报警规则关联通知模板Mapper接口
 *
 * @author grass-service
 * @date 2022-08-05
 */
@Repository
public interface GrassRuleNoticeMapper extends BaseMapper<GrassRuleNotice> {
    /**
     * 查询报警规则关联通知模板列表
     *
     * @param grassRuleNotice 报警规则关联通知模板
     * @return 报警规则关联通知模板集合
     */
    List<GrassRuleNotice> selectGrassRuleNoticeList(GrassRuleNotice grassRuleNotice);

    List<GrassRuleNoticeVO> selectGrassRuleNoticeVOByIds(@Param("list") List<String> ids);


    @InterceptorIgnore(tenantLine = "on")
    Boolean deleteByTenantId(String tenantId);
}
