package com.rexel.laocz.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.j2objc.annotations.AutoreleasePool;
import com.rexel.laocz.domain.LaoczLiquorBatch;
import com.rexel.laocz.domain.LaoczLiquorManagement;
import com.rexel.laocz.mapper.LaoczLiquorBatchMapper;
import com.rexel.laocz.service.ILaoczLiquorBatchService;
import com.rexel.laocz.service.ILaoczLiquorManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 酒液批次相关信息Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczLiquorBatchServiceImpl extends ServiceImpl<LaoczLiquorBatchMapper, LaoczLiquorBatch> implements ILaoczLiquorBatchService {


    @Autowired
    private ILaoczLiquorManagementService iLaoczLiquorManagementService;

    /**
     * 查询酒液批次相关信息列表
     *
     * @param laoczLiquorBatch 酒液批次相关信息
     * @return 酒液批次相关信息
     */
    @Override
    public List<LaoczLiquorBatch> selectLaoczLiquorBatchList(LaoczLiquorBatch laoczLiquorBatch) {
        return baseMapper.selectLaoczLiquorBatchList(laoczLiquorBatch);
    }

    /**
     * 出酒时，酒液批次下拉框，只显示有酒并且是存储的批次
     *
     * @return 酒液批次相关信息
     */
    @Override
    public List<LaoczLiquorBatch> wineOutLaoczLiquorBatchList() {
        return baseMapper.wineOutLaoczLiquorBatchList();
    }

    /**
     * 根据批次id，获取对应批次id的酒品名称
     * @param liquorBatchIds
     * @return
     */
    @Override
    public Map<String, String> getLiquorManagementNameMap(List<String> liquorBatchIds) {
        if(CollectionUtil.isEmpty(liquorBatchIds)){
            return new HashMap<>();
        }
        //查询批次
        List<LaoczLiquorBatch> laoczLiquorBatches = listByIds(liquorBatchIds);
        //批次获取酒品
        List<Long> liquorManagementIds = laoczLiquorBatches.stream().map(LaoczLiquorBatch::getLiquorManagementId).collect(Collectors.toList());
        //查询酒品
        List<LaoczLiquorManagement> managements = iLaoczLiquorManagementService.listByIds(liquorManagementIds);
        //酒品转换map，key酒品id，值：酒品名称
        Map<Long, String> managementMap = managements.stream().collect(Collectors.toMap(LaoczLiquorManagement::getLiquorManagementId, LaoczLiquorManagement::getLiquorName));
        //返回以批次id为key，酒品名称为值的map
        return laoczLiquorBatches.stream().collect(Collectors.toMap(LaoczLiquorBatch::getLiquorBatchId, item -> managementMap.get(item.getLiquorManagementId())));
    }

}
