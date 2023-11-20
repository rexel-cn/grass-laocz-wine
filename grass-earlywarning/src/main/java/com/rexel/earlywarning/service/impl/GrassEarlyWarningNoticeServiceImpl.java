package com.rexel.earlywarning.service.impl;

import java.util.List;

import com.rexel.common.utils.DateUtils;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.earlywarning.domain.GrassEarlyWarningNotice;
import com.rexel.earlywarning.mapper.GrassEarlyWarningNoticeMapper;
import com.rexel.earlywarning.service.IGrassEarlyWarningNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 预警规则通知模板Service业务层处理
 *
 * @author admin
 * @date 2022-01-14
 */
@Service
public class GrassEarlyWarningNoticeServiceImpl implements IGrassEarlyWarningNoticeService {
    @Autowired
    private GrassEarlyWarningNoticeMapper grassEarlyWarningNoticeMapper;

    /**
     * 查询预警规则通知范围列表
     *
     * @param notice 预警规则通知范围
     * @return 预警规则通知范围
     */
    @Override
    public List<GrassEarlyWarningNotice> selectGrassEarlyWarningNoticeList(GrassEarlyWarningNotice notice) {
        return grassEarlyWarningNoticeMapper.selectGrassEarlyWarningNoticeList(notice);
    }

    /**
     * 新增预警规则通知范围
     *
     * @param notice 预警规则通知范围
     * @return 结果
     */
    @Override
    public int insertGrassEarlyWarningNotice(GrassEarlyWarningNotice notice) {
        notice.setTenantId(SecurityUtils.getTenantId());
        notice.setCreateTime(DateUtils.getNowDate());
        notice.setCreateBy(SecurityUtils.getUsername());
        notice.setUpdateTime(DateUtils.getNowDate());
        notice.setUpdateBy(SecurityUtils.getUsername());
        return grassEarlyWarningNoticeMapper.insertGrassEarlyWarningNotice(notice);
    }

    /**
     * 修改预警规则通知范围
     *
     * @param notice 预警规则通知范围
     * @return 结果
     */
    @Override
    public int updateGrassEarlyWarningNotice(GrassEarlyWarningNotice notice) {
        notice.setUpdateTime(DateUtils.getNowDate());
        notice.setUpdateBy(SecurityUtils.getUsername());
        return grassEarlyWarningNoticeMapper.updateGrassEarlyWarningNotice(notice);
    }

    /**
     * 删除预警规则通知范围信息
     *
     * @param id 预警规则通知范围ID
     * @return 结果
     */
    @Override
    public int deleteGrassEarlyWarningNoticeById(Long id) {
        return grassEarlyWarningNoticeMapper.deleteGrassEarlyWarningNoticeById(id);
    }
}
