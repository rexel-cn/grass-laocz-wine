package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.exception.CustomException;
import com.rexel.common.exception.ServiceException;
import com.rexel.laocz.domain.LaoczLiquorBatch;
import com.rexel.laocz.domain.LaoczLiquorManagement;
import com.rexel.laocz.domain.vo.LiquorVo;
import com.rexel.laocz.mapper.LaoczLiquorManagementMapper;
import com.rexel.laocz.service.ILaoczLiquorBatchService;
import com.rexel.laocz.service.ILaoczLiquorManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 酒品管理Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczLiquorManagementServiceImpl extends ServiceImpl<LaoczLiquorManagementMapper, LaoczLiquorManagement> implements ILaoczLiquorManagementService {

    @Autowired
    private ILaoczLiquorBatchService iLaoczLiquorBatchService;

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
    @Transactional(rollbackFor = Exception.class)
    public boolean importPoint(List<LiquorVo> liquorVos) {
        if (CollectionUtil.isEmpty(liquorVos)) {
            throw new ServiceException("导入数据为空");
        }
        // 数据验证
        check(liquorVos);

        List<String> liquorNames = liquorVos.stream().map(LiquorVo::getLiquorName).collect(Collectors.toList());

        QueryWrapper<LaoczLiquorManagement> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("liquor_name", liquorNames);
        int size = this.list(queryWrapper).size();

        if (size > 0) {
            throw new ServiceException("酒品名称已存在");
        }


        List<LaoczLiquorManagement> laoczLiquorManagements = liquorVos.stream().map((item) -> {
            // 数据拷贝
            return BeanUtil.copyProperties(item, LaoczLiquorManagement.class);
        }).collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(laoczLiquorManagements)) {
            return saveBatch(laoczLiquorManagements);
        }
        return false;
    }

    /**
     * @param liquorManagementId
     * @return
     */
    @Override
    public boolean deleteLaoczLiquorManagementById(Long liquorManagementId) {
        Integer count = iLaoczLiquorBatchService.lambdaQuery().eq(LaoczLiquorBatch::getLiquorManagementId, liquorManagementId).count();
        if (count > 0) {
            throw new CustomException("酒品已被使用禁止删除");
        }
        return removeById(liquorManagementId);
    }

    /**
     * 新增酒品
     *
     * @param laoczLiquorManagement 酒品
     * @return 结果
     */
    @Override
    public Boolean saveLaoczLiquorManagement(LaoczLiquorManagement laoczLiquorManagement) {
        checkLaoczLiquorManagement(laoczLiquorManagement);
        return save(laoczLiquorManagement);
    }

    /**
     * 修改酒品
     *
     * @param laoczLiquorManagement 酒品
     * @return 结果
     */
    @Override
    public Boolean updateLaoczLiquorManagement(LaoczLiquorManagement laoczLiquorManagement) {
        checkLaoczLiquorManagement(laoczLiquorManagement);
        return updateById(laoczLiquorManagement);
    }

    public void checkLiquorName(String liquorName, Long liquorManagementId) {
        QueryWrapper<LaoczLiquorManagement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("liquor_name", liquorName);
        if (liquorManagementId != null) {
            queryWrapper.ne("liquor_management_id", liquorManagementId);
        }
        int size = this.list(queryWrapper).size();

        if (size > 0) {
            throw new ServiceException("酒品名称已存在");
        }
    }

    /**
     * 校验数据
     *
     * @param liquorVos
     */
    private void check(List<LiquorVo> liquorVos) {
        List<String> errList = new ArrayList<>();
        Set<String> liquorNameSet = new HashSet<>();

        // 校验数据
        for (int i = 0; i < liquorVos.size(); i++) {
            LiquorVo liquorVo = liquorVos.get(i);
            if (StrUtil.isEmpty(liquorVo.getLiquorName())) {
                errList.add("第" + (i + 2) + "行酒品名称为空");
            }
            if (!liquorNameSet.add(liquorVo.getLiquorName())) {
                errList.add("第" + (i + 2) + "行酒品名称重复");
            }
            if (ObjectUtil.isNull(liquorVo.getLiquorLevel())) {
                errList.add("第" + (i + 2) + "行酒类等级为空");
            }
            if (StrUtil.isEmpty(liquorVo.getLiquorRound())) {
                errList.add("第" + (i + 2) + "行酒业轮次为空");
            }
            if (StrUtil.isEmpty(liquorVo.getLiquorFlavorName())) {
                errList.add("第" + (i + 2) + "行香型名称为空");
            }
            if (StrUtil.isEmpty(liquorVo.getLiquorSource())) {
                errList.add("第" + (i + 2) + "行酒液来源为空");
            }
            if (StrUtil.isEmpty(liquorVo.getLiquorYear())) {
                errList.add("第" + (i + 2) + "行酒液年份为空");
            }
            if (StrUtil.isEmpty(liquorVo.getLiquorBrewingTime())) {
                errList.add("第" + (i + 2) + "行酿造时间为空");
            }
            if (StrUtil.isEmpty(liquorVo.getLiquorContent())) {
                errList.add("第" + (i + 2) + "行酒精度数为空");
            }
            if (Long.parseLong(liquorVo.getLiquorContent()) < 1 || Long.parseLong(liquorVo.getLiquorContent()) > 100) {
                errList.add("第" + (i + 2) + "行酒精度数不正确，请合理设置酒精度数");
            }
        }
        if (CollectionUtil.isNotEmpty(errList)) {
            throw new ServiceException(StrUtil.join("\n", errList));
        }
    }

    public void checkLiquorContent(Double LiquorContent) {
        if (LiquorContent < 1.0 || LiquorContent > 100.0) {
            throw new CustomException("酒精度数不正确，请合理设置酒精度数");
        }
    }

    public void checkLaoczLiquorManagement(LaoczLiquorManagement liquorManagement) {
        //验证酒品名称是否重复
        checkLiquorName(liquorManagement.getLiquorName(), liquorManagement.getLiquorManagementId());
        //验证酒精度数是否正确
        checkLiquorContent(Double.parseDouble(liquorManagement.getLiquorContent()));
    }
}
