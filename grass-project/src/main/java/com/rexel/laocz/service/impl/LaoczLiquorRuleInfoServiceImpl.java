package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.laocz.domain.LaoczLiquorRuleInfo;
import com.rexel.laocz.mapper.LaoczLiquorRuleInfoMapper;
import com.rexel.laocz.service.ILaoczLiquorRuleInfoService;
import com.rexel.laocz.vo.LiquorRuleInfoVo;
import com.rexel.laocz.vo.UserInfoVo;
import com.rexel.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 酒液批次存储报警规则信息Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczLiquorRuleInfoServiceImpl extends ServiceImpl<LaoczLiquorRuleInfoMapper, LaoczLiquorRuleInfo> implements ILaoczLiquorRuleInfoService {


    @Autowired
    private ISysUserService userService;

    /**
     * 查询酒液批次存储报警规则信息列表
     *
     * @param laoczLiquorRuleInfo 酒液批次存储报警规则信息
     * @return 酒液批次存储报警规则信息
     */
    @Override
    public List<LiquorRuleInfoVo> selectLaoczLiquorRuleInfoList(LaoczLiquorRuleInfo laoczLiquorRuleInfo) {

        List<LaoczLiquorRuleInfo> laoczLiquorRuleInfos = baseMapper.selectLaoczLiquorRuleInfoList(laoczLiquorRuleInfo);

        List<LiquorRuleInfoVo> list = laoczLiquorRuleInfos.stream().map((item) -> {
            LiquorRuleInfoVo liquorRuleInfoVo = new LiquorRuleInfoVo();

            BeanUtil.copyProperties(item, liquorRuleInfoVo);

            String liquorRuleNotifyUser = item.getLiquorRuleNotifyUser();

            String[] split = liquorRuleNotifyUser.split(",");

            int length = split.length;

            liquorRuleInfoVo.setCount(length);

            return liquorRuleInfoVo;
        }).collect(Collectors.toList());


        return list;
    }

    /**
     * 根据id查询通知人员信息
     *
     * @param id 酒液批次报警ID
     * @return 用户信息
     */

    @Override
    public List<UserInfoVo> getByIdWithUserInfo(Long id) {
        List<UserInfoVo> userInfoVos = new ArrayList<>();

        LaoczLiquorRuleInfo laoczLiquorRuleInfo = this.getById(id);

        if (laoczLiquorRuleInfo == null){
            return userInfoVos;
        }

        String ruleNotifyTemplate = laoczLiquorRuleInfo.getLiquorRuleNotifyUser();

        String[] split = ruleNotifyTemplate.split(",");

        if (split.length == 0) {
            return userInfoVos;
        }

        for (String s : split) {
            SysUser sysUser = userService.getById(s);
            if (sysUser != null){
                UserInfoVo userInfoVo = new UserInfoVo();
                userInfoVo.setUserId(sysUser.getUserId());
                userInfoVo.setUserName(sysUser.getUserName());
                userInfoVo.setPhoneNumber(sysUser.getPhoneNumber());

                userInfoVos.add(userInfoVo);
            }
        }
        return userInfoVos;
    }

}
