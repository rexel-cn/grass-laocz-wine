package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.laocz.domain.LaoczLiquorRuleInfo;
import com.rexel.laocz.vo.LiquorRuleInfoVo;
import com.rexel.laocz.vo.UserInfoVo;

import java.util.List;

/**
 * 酒液批次存储报警规则信息Service接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
public interface ILaoczLiquorRuleInfoService extends IService<LaoczLiquorRuleInfo> {

    /**
     * 查询酒液批次存储报警规则信息列表
     *
     * @param laoczLiquorRuleInfo 酒液批次存储报警规则信息
     * @return 酒液批次存储报警规则信息集合
     */
    List<LiquorRuleInfoVo> selectLaoczLiquorRuleInfoList(LaoczLiquorRuleInfo laoczLiquorRuleInfo);

    /**
     * 根据id查询通知人员信息
     * @param id 酒液批次报警ID
     * @return 用户信息
     */
    List<UserInfoVo> getByIdWithUserInfo(Long id);
}
