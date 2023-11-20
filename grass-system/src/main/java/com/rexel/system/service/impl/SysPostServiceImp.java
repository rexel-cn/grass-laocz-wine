package com.rexel.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.exception.ServiceException;
import com.rexel.common.utils.StringUtils;
import com.rexel.system.domain.SysPost;
import com.rexel.system.domain.dto.post.PostCreateReqDTO;
import com.rexel.system.domain.dto.post.PostPageReqDTO;
import com.rexel.system.domain.dto.post.PostUpdateReqDTO;
import com.rexel.system.mapper.SysPostMapper;
import com.rexel.system.service.ISysPostService;
import com.rexel.system.service.ISysUserPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 岗位信息 服务层处理
 *
 * @author ids-admin
 */
@Service
public class SysPostServiceImp extends ServiceImpl<SysPostMapper, SysPost> implements ISysPostService {

    @Autowired
    private ISysUserPostService sysUserPostService;


    /**
     * 查询岗位信息集合
     *
     * @param post 岗位信息
     * @return 岗位列表
     */
    @Override
    public List<SysPost> selectPostList(PostPageReqDTO post) {
        return lambdaQuery().like(StringUtils.isNotEmpty(post.getPostName()), SysPost::getPostName, post.getPostName()).orderBy(true, true, SysPost::getPostSort)
                .list();
    }

    /**
     * 创建岗位
     *
     * @param reqVO 岗位信息
     * @return 岗位编号
     */
    @Override
    public Long createPost(PostCreateReqDTO reqVO) {
        // 校验正确性
        this.checkCreateOrUpdate(null, reqVO.getPostName(), null);
        // 插入岗位
        SysPost post = BeanUtil.copyProperties(reqVO, SysPost.class);
        post.setPostCode(StringUtils.randomNumber(8));
        post.setStatus("0");
        save(post);
        return post.getPostId();
    }

    /**
     * 更新岗位
     *
     * @param reqVO 岗位信息
     */
    @Override
    public void updatePost(PostUpdateReqDTO reqVO) {
        // 校验正确性
        this.checkCreateOrUpdate(reqVO.getPostId(), reqVO.getPostName(), null);
        // 插入岗位
        SysPost post = BeanUtil.copyProperties(reqVO, SysPost.class);
        updateById(post);
    }

    /**
     * 删除岗位信息
     *
     * @param id 岗位编号
     */
    @Override
    public void deletePost(Long id) {
        // 校验是否存在
        this.checkPostExists(id);
        //检测是否被使用
        this.checkPostUsed(id);
        // 删除部门
        removeById(id);
    }

    /**
     * @param postIds
     * @return
     */
    @Override
    public List<SysPost> selectPostByIds(Set<Long> postIds) {
        return lambdaQuery().in(CollectionUtil.isNotEmpty(postIds), SysPost::getPostId, postIds).list();
    }

    /**
     * @param postId
     * @return
     */
    @Override
    public SysPost selectPostById(Long postId) {
        return getById(postId);
    }

    private void checkPostUsed(Long id) {
        //检测是否被使用
        int count = sysUserPostService.selectCountByPostId(id);
        if (count > 0) {
            throw new ServiceException("岗位已被使用，不能删除");
        }
    }

    private void checkCreateOrUpdate(Long id, String name, String code) {
        // 校验自己存在
        checkPostExists(id);
        // 校验岗位名的唯一性
        checkPostNameUnique(id, name);
        // 校验岗位编码的唯一性
        //checkPostCodeUnique(id, code);
    }

    private void checkPostExists(Long id) {
        if (id == null) {
            return;
        }
        SysPost post = getById(id);
        if (post == null) {
            throw new ServiceException("当前岗位不存在");
        }
    }

    private void checkPostNameUnique(Long id, String name) {
        lambdaQuery().eq(SysPost::getPostName, name).ne(id != null, SysPost::getPostId, id).oneOpt().ifPresent(post -> {
            throw new ServiceException("当前岗位名已存在");
        });
    }

    private void checkPostCodeUnique(Long id, String code) {
        lambdaQuery().eq(SysPost::getPostCode, code).ne(id != null, SysPost::getPostId, id).oneOpt().ifPresent(post -> {
            throw new ServiceException("当前岗位编码已存在");
        });
    }


}
