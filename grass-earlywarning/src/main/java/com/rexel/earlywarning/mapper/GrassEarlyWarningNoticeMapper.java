package com.rexel.earlywarning.mapper;

import java.util.List;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.rexel.earlywarning.domain.GrassEarlyWarningNotice;
import com.rexel.system.domain.vo.RuleNoticeVO;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 预警规则通知范围Mapper接口
 *
 * @author admin
 * @date 2022-01-14
 */
@Repository
public interface GrassEarlyWarningNoticeMapper {
    /**
     * 查询预警规则通知范围
     *
     * @param rulesId 预警规则通知范围ID
     * @return 预警规则通知范围
     */
    List<GrassEarlyWarningNotice> selectGrassEarlyWarningNoticeByRulesId(Long rulesId);

    /**
     * 查询预警规则通知范围列表
     *
     * @param notice 预警规则通知范围
     * @return 预警规则通知范围集合
     */
    List<GrassEarlyWarningNotice> selectGrassEarlyWarningNoticeList(GrassEarlyWarningNotice notice);

    /**
     * 查询通知用户列表
     *
     * @param rulesId rulesId
     * @return 结果
     */
    @InterceptorIgnore(tenantLine = "on")
    List<RuleNoticeVO> selectEarlyWarningNoticeUserList(Long rulesId);

    /**
     * 新增预警规则通知范围
     *
     * @param notice 预警规则通知范围
     * @return 结果
     */
    int insertGrassEarlyWarningNotice(GrassEarlyWarningNotice notice);

    /**
     * 新增预警规则通知范围
     *
     * @param list list
     */
    void batchInsertGrassEarlyWarningNotice(@Param("list") List<GrassEarlyWarningNotice> list);

    /**
     * 修改预警规则通知范围
     *
     * @param notice 预警规则通知范围
     * @return 结果
     */
    int updateGrassEarlyWarningNotice(GrassEarlyWarningNotice notice);

    /**
     * 删除预警规则通知范围
     *
     * @param id 预警规则通知范围ID
     * @return 结果
     */
    int deleteGrassEarlyWarningNoticeById(Long id);

    /**
     * 删除预警规则通知范围
     *
     * @param rulesId rulesId
     * @return 结果
     */
    int deleteGrassEarlyWarningNoticeByRulesId(Long rulesId);

    /**
     * 删除预警规则通知范围
     *
     * @param tenantId tenantId
     * @return 结果
     */
    int deleteGrassEarlyWarningNoticeByTenantId(String tenantId);

    /**
     * 批量删除预警规则通知范围
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteGrassEarlyWarningNoticeByIds(Long[] ids);
}
