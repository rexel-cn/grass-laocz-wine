package com.rexel.earlywarning.service.impl;

import java.util.List;
import com.rexel.earlywarning.domain.GrassEarlyWarningCarrier;
import com.rexel.earlywarning.mapper.GrassEarlyWarningCarrierMapper;
import com.rexel.earlywarning.service.IGrassEarlyWarningCarrierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 预警规则运行载体Service业务层处理
 *
 * @author admin
 * @date 2022-02-23
 */
@Service
public class GrassEarlyWarningCarrierServiceImpl implements IGrassEarlyWarningCarrierService {
    @Autowired
    private GrassEarlyWarningCarrierMapper busyEarlyWarningCarrierMapper;

    /**
     * 查询预警规则运行载体列表
     *
     * @param carrier 预警规则运行载体
     * @return 预警规则运行载体
     */
    @Override
    public List<GrassEarlyWarningCarrier> selectGrassEarlyWarningCarrierList(GrassEarlyWarningCarrier carrier) {
        return busyEarlyWarningCarrierMapper.selectGrassEarlyWarningCarrierList(carrier);
    }

    /**
     * 新增预警规则运行载体
     *
     * @param carrier 预警规则运行载体
     * @return 结果
     */
    @Override
    public int insertGrassEarlyWarningCarrier(GrassEarlyWarningCarrier carrier) {
        return busyEarlyWarningCarrierMapper.insertGrassEarlyWarningCarrier(carrier);
    }

    /**
     * 修改预警规则运行载体
     *
     * @param carrier 预警规则运行载体
     * @return 结果
     */
    @Override
    public int updateGrassEarlyWarningCarrier(GrassEarlyWarningCarrier carrier) {
        return busyEarlyWarningCarrierMapper.updateGrassEarlyWarningCarrier(carrier);
    }

    /**
     * 删除预警规则运行载体信息
     *
     * @param id 预警规则运行载体ID
     * @return 结果
     */
    @Override
    public int deleteGrassEarlyWarningCarrierById(Long id) {
        return busyEarlyWarningCarrierMapper.deleteGrassEarlyWarningCarrierById(id);
    }
}
