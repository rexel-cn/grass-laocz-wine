package com.rexel.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.constant.UserConstants;
import com.rexel.common.core.domain.entity.SysDictData;
import com.rexel.common.core.domain.entity.SysRole;
import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.common.core.domain.vo.BaseNameValueVO;
import com.rexel.system.domain.GrassNoticeScope;
import com.rexel.system.domain.vo.NoticeScopeDropDown;
import com.rexel.system.mapper.GrassNoticeScopeMapper;
import com.rexel.system.mapper.SysDictDataMapper;
import com.rexel.system.service.IGrassNoticeScopeService;
import com.rexel.system.service.ISysRoleService;
import com.rexel.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知模板对应范围Service业务层处理
 *
 * @author grass-service
 * @date 2022-08-01
 */
@Service
public class GrassNoticeScopeServiceImpl extends ServiceImpl<GrassNoticeScopeMapper, GrassNoticeScope> implements IGrassNoticeScopeService {
    private final static String NOTICE_SCOPE = "notice_scope";
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private SysDictDataMapper dictDataMapper;

    /**
     * 查询通知模板对应范围列表
     *
     * @param grassNoticeScope 通知模板对应范围
     * @return 通知模板对应范围
     */
    @Override
    public List<GrassNoticeScope> selectGrassNoticeScopeList(GrassNoticeScope grassNoticeScope) {
        return baseMapper.selectGrassNoticeScopeList(grassNoticeScope);
    }

    @Override
    public List<NoticeScopeDropDown> noticeScopeList() {
        List<SysDictData> dictDataList = dictDataMapper.selectDictDataByType(NOTICE_SCOPE);
        List<NoticeScopeDropDown> scopeVoArrayList = new ArrayList<>();
        dictDataList.forEach(a -> {
            NoticeScopeDropDown iotNoticeScopeVo = new NoticeScopeDropDown();
            iotNoticeScopeVo.setNoticeScopeName(a.getDictLabel());
            iotNoticeScopeVo.setNoticeScope(a.getDictValue());
            if (UserConstants.NOTICE_SCOPE_PERSON.equals(a.getDictValue())) {
                iotNoticeScopeVo.setNameValueVOList(scopeUserListVo());
            } else if (UserConstants.NOTICE_SCOPE_ROLE.equals(a.getDictValue())) {
                iotNoticeScopeVo.setNameValueVOList(scopeRoleListVo());
            }
            scopeVoArrayList.add(iotNoticeScopeVo);
        });
        return scopeVoArrayList;
    }

    @Override
    public Boolean deleteByTenantId(String tenantId) {
        return baseMapper.deleteByTenantId(tenantId);
    }

    private List<BaseNameValueVO> scopeUserListVo() {
        List<BaseNameValueVO> nameValueVOList = new ArrayList<>();
        List<SysUser> userList = userService.list();
        userList.forEach(b -> {
            BaseNameValueVO nameValueVO = new BaseNameValueVO();
            nameValueVO.setName(b.getUserName());
            nameValueVO.setValue(b.getUserId());
            nameValueVOList.add(nameValueVO);
        });
        return nameValueVOList;
    }

    private List<BaseNameValueVO> scopeRoleListVo() {
        List<BaseNameValueVO> nameValueVOList = new ArrayList<>();
        List<SysRole> roleList = roleService.list();
        roleList.forEach(b -> {
            BaseNameValueVO nameValueVO = new BaseNameValueVO();
            nameValueVO.setName(b.getRoleName());
            nameValueVO.setValue(b.getRoleId());
            nameValueVOList.add(nameValueVO);
        });
        return nameValueVOList;
    }
}
