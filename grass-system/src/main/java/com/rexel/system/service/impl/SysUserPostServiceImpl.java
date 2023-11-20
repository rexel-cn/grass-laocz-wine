package com.rexel.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.system.domain.SysUserPost;
import com.rexel.system.mapper.SysUserPostMapper;
import com.rexel.system.service.ISysUserPostService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName SysUserPostServiceImpl
 * @Description
 * @Author 孟开通
 * @Date 2022/12/5 14:51
 **/
@Service
public class SysUserPostServiceImpl extends ServiceImpl<SysUserPostMapper, SysUserPost> implements ISysUserPostService {


    /**
     * @param userId
     * @return
     */
    @Override
    public List<SysUserPost> selectListByUserId(Long userId) {
        return lambdaQuery().eq(userId != null, SysUserPost::getUserId, userId).list();
    }

    /**
     * @param userId
     * @param deletePostIds
     */
    @Override
    public void deleteListByUserIdAndPostIds(Long userId, Collection<Long> deletePostIds) {
        lambdaUpdate()
                .eq(userId != null, SysUserPost::getUserId, userId)
                .in(deletePostIds != null && !deletePostIds.isEmpty(), SysUserPost::getPostId, deletePostIds)
                .remove();

    }

    /**
     * @param id
     * @return
     */
    @Override
    public int selectCountByPostId(Long id) {
        return lambdaQuery().eq(id != null, SysUserPost::getPostId, id).count();
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public Set<Long> selectPostIdsByUserId(Long userId) {
        return lambdaQuery().eq(userId != null, SysUserPost::getUserId, userId).list().stream().map(SysUserPost::getPostId).collect(Collectors.toSet());
    }

    @Override
    public boolean deleteByUserId(Long userId) {
        return lambdaUpdate()
                .eq(userId != null, SysUserPost::getUserId, userId)
                .remove();
    }
}
