package com.rexel.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.SysUserPost;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 用户与岗位关联Service接口
 *
 * @author grass-service
 * @date 2022-12-05
 */
public interface ISysUserPostService extends IService<SysUserPost> {


    List<SysUserPost> selectListByUserId(Long userId);

    void deleteListByUserIdAndPostIds(Long userId, Collection<Long> deletePostIds);

    int selectCountByPostId(Long id);

    Set<Long> selectPostIdsByUserId(Long userId);

    boolean deleteByUserId(Long id);
}
