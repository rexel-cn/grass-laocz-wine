package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.common.utils.StringUtils;
import com.rexel.laocz.domain.LaoczLiquorRuleInfo;
import com.rexel.laocz.domain.vo.LiquorRuleInfoVo;
import com.rexel.laocz.domain.vo.UserInfoVo;
import com.rexel.laocz.mapper.LaoczLiquorRuleInfoMapper;
import com.rexel.laocz.service.ILaoczLiquorRuleInfoService;
import com.rexel.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        List<LiquorRuleInfoVo> laoczLiquorRuleInfos = baseMapper.selectLaoczLiquorRuleInfoListVo(laoczLiquorRuleInfo);

        for (LiquorRuleInfoVo liquorRuleInfo : laoczLiquorRuleInfos) {
            String liquorRuleNotifyUser = liquorRuleInfo.getLiquorRuleNotifyUser();

            String[] split = liquorRuleNotifyUser.split(",");

            int length = split.length;

            liquorRuleInfo.setCount(length);

            Long[] result = new Long[split.length];
            for (int i = 0; i < split.length; i++) {
                result[i] = Long.parseLong(split[i]);
            }

            liquorRuleInfo.setNoticePeopleArray(result);
        }
        return laoczLiquorRuleInfos;
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

        if (laoczLiquorRuleInfo == null) {
            return userInfoVos;
        }

        String ruleNotifyTemplate = laoczLiquorRuleInfo.getLiquorRuleNotifyUser();

        String[] split = ruleNotifyTemplate.split(",");

        if (split.length == 0) {
            return userInfoVos;
        }

        for (String s : split) {
            SysUser sysUser = userService.getById(s);
            if (sysUser != null) {
                UserInfoVo userInfoVo = new UserInfoVo();
                userInfoVo.setUserId(sysUser.getUserId());
                userInfoVo.setUserName(sysUser.getUserName());
                userInfoVo.setPhoneNumber(sysUser.getPhoneNumber());

                userInfoVos.add(userInfoVo);
            }
        }
        return userInfoVos;
    }

    /**
     * 新增报警规则
     *
     * @param laoczLiquorRuleInfo 报警规则
     * @return
     */
    @Override
    public void saveWithRule(LaoczLiquorRuleInfo laoczLiquorRuleInfo) {
        Long[] noticePeopleArray = laoczLiquorRuleInfo.getNoticePeopleArray();

        if (noticePeopleArray != null && noticePeopleArray.length > 0) {

            laoczLiquorRuleInfo.setLiquorRuleNotifyUser(StringUtils.join(noticePeopleArray, ","));

        }

        save(laoczLiquorRuleInfo);
    }

    /**
     * 修改报警规则
     *
     * @param laoczLiquorRuleInfo 报警规则
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWithRule(LaoczLiquorRuleInfo laoczLiquorRuleInfo) {

        if (laoczLiquorRuleInfo.getNoticePeopleArray() != null && laoczLiquorRuleInfo.getNoticePeopleArray().length > 0) {
            laoczLiquorRuleInfo.setLiquorRuleNotifyUser(StringUtils.join(laoczLiquorRuleInfo.getNoticePeopleArray(), ","));
        }

        updateById(laoczLiquorRuleInfo);
    }

    /**
     * 酒液批次下拉
     *
     * @return
     */
    @Override
    public List<String> dropDown() {

        return baseMapper.dropDown();
    }

}
