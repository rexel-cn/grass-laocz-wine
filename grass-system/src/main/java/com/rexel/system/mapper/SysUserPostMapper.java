package com.rexel.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.SysUserPost;
import org.springframework.stereotype.Repository;


/**
 * 用户与岗位关联表 数据层
 *
 * @author ids-admin
 */
@Repository
public interface SysUserPostMapper extends BaseMapper<SysUserPost> {
}
