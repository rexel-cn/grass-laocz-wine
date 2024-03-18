package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.exception.ServiceException;
import com.rexel.laocz.domain.LaoczLiquorManagement;
import com.rexel.laocz.domain.vo.LiquorVo;
import com.rexel.laocz.mapper.LaoczLiquorManagementMapper;
import com.rexel.laocz.service.ILaoczLiquorManagementService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 酒品管理Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczLiquorManagementServiceImpl extends ServiceImpl<LaoczLiquorManagementMapper, LaoczLiquorManagement> implements ILaoczLiquorManagementService {


    /**
     * 查询酒品管理列表
     *
     * @param laoczLiquorManagement 酒品管理
     * @return 酒品管理
     */
    @Override
    public List<LaoczLiquorManagement> selectLaoczLiquorManagementList(LaoczLiquorManagement laoczLiquorManagement) {
        return baseMapper.selectLaoczLiquorManagementList(laoczLiquorManagement);
    }

    @Override
    public boolean importPoint(List<LiquorVo> liquorVos) {
        ArrayList<LaoczLiquorManagement> laoczLiquorManagements = new ArrayList<>();
        if (CollectionUtil.isEmpty(liquorVos)) {
            throw new ServiceException("导入数据为空");
        }
        // 数据验证
        check(liquorVos);

        liquorVos.stream().map((item) -> {

            LaoczLiquorManagement laoczLiquorManagement = new LaoczLiquorManagement();

            BeanUtil.copyProperties(item, laoczLiquorManagement);

            laoczLiquorManagements.add(laoczLiquorManagement);

            return laoczLiquorManagements;

        }).collect(Collectors.toList());

        return saveBatch(laoczLiquorManagements);
    }

    /**
     * 校验数据
     *
     * @param liquorVos
     */
    private void check(List<LiquorVo> liquorVos) {
        List<String> errList = new ArrayList<>();
        // 校验数据
        for (int i = 0; i < liquorVos.size(); i++) {
            LiquorVo liquorVo = liquorVos.get(i);
            if (StrUtil.isEmpty(liquorVo.getLiquorName())) {
                errList.add("第" + (i + 2) + "酒品名称为空");
            }
            if (ObjectUtil.isNull(liquorVo.getLiquorLevel())) {
                errList.add("第" + (i + 2) + "酒类等级为空");
            }
            if (StrUtil.isEmpty(liquorVo.getLiquorRound())) {
                errList.add("第" + (i + 2) + "酒业轮次为空");
            }
            if (StrUtil.isEmpty(liquorVo.getLiquorFlavorName())) {
                errList.add("第" + (i + 2) + "香型名称为空");
            }
            if (StrUtil.isEmpty(liquorVo.getLiquorSource())) {
                errList.add("第" + (i + 2) + "酒液来源为空");
            }
            if (StrUtil.isEmpty(liquorVo.getLiquorYear())) {
                errList.add("第" + (i + 2) + "酒液年份为空");
            }
            if (StrUtil.isEmpty(liquorVo.getLiquorBrewingTime())) {
                errList.add("第" + (i + 2) + "酿造时间为空");
            }
            if (StrUtil.isEmpty(liquorVo.getLiquorContent())) {
                errList.add("第" + (i + 2) + "酒精度数为空");
            }
        }
        if (CollectionUtil.isNotEmpty(errList)) {
            throw new ServiceException(StrUtil.join("\n", errList));
        }
    }

}
