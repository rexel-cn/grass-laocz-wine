package com.rexel.earlywarning.service;

import java.util.List;
import com.rexel.earlywarning.domain.GrassEarlyWarningNotice;

/**
 * 预警规则通知模板Service接口
 *
 * @author admin
 * @date 2022-01-14
 */
public interface IGrassEarlyWarningNoticeService {
    /**
     * 查询预警规则通知范围列表
     *
     * @param notice 预警规则通知范围
     * @return 预警规则通知范围集合
     */
    List<GrassEarlyWarningNotice> selectGrassEarlyWarningNoticeList(GrassEarlyWarningNotice notice);

    /**
     * 新增预警规则通知范围
     *
     * @param notice 预警规则通知范围
     * @return 结果
     */
    int insertGrassEarlyWarningNotice(GrassEarlyWarningNotice notice);

    /**
     * 修改预警规则通知范围
     *
     * @param notice 预警规则通知范围
     * @return 结果
     */
    int updateGrassEarlyWarningNotice(GrassEarlyWarningNotice notice);

    /**
     * 删除预警规则通知范围信息
     *
     * @param id 预警规则通知范围ID
     * @return 结果
     */
    int deleteGrassEarlyWarningNoticeById(Long id);
}
