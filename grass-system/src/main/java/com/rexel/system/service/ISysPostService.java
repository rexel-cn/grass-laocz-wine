package com.rexel.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.SysPost;
import com.rexel.system.domain.dto.post.PostCreateReqDTO;
import com.rexel.system.domain.dto.post.PostPageReqDTO;
import com.rexel.system.domain.dto.post.PostUpdateReqDTO;

import java.util.List;
import java.util.Set;

/**
 * 岗位信息 服务层
 *
 * @author ids-admin
 */
public interface ISysPostService extends IService<SysPost> {

    /**
     * 查询岗位信息集合
     *
     * @param post 岗位信息
     * @return 岗位列表
     */
    List<SysPost> selectPostList(PostPageReqDTO post);

    /**
     * 创建岗位
     *
     * @param reqVO 岗位信息
     * @return 岗位编号
     */
    Long createPost(PostCreateReqDTO reqVO);

    /**
     * 更新岗位
     *
     * @param reqVO 岗位信息
     */
    void updatePost(PostUpdateReqDTO reqVO);

    /**
     * 删除岗位信息
     *
     * @param id 岗位编号
     */
    void deletePost(Long id);

    List<SysPost> selectPostByIds(Set<Long> postIds);

    SysPost selectPostById(Long postId);
}
