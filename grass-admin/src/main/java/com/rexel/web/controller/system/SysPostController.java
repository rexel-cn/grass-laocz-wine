package com.rexel.web.controller.system;

import com.rexel.common.annotation.Log;
import com.rexel.common.annotation.RepeatSubmit;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.enums.BusinessType;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.system.domain.SysPost;
import com.rexel.system.domain.dto.post.PostCreateReqDTO;
import com.rexel.system.domain.dto.post.PostPageReqDTO;
import com.rexel.system.domain.dto.post.PostUpdateReqDTO;
import com.rexel.system.service.ISysPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位信息操作处理
 *
 * @author ids-admin
 */
@RestController
@RequestMapping("/system/post")
public class SysPostController extends BaseController {
    @Autowired
    private ISysPostService postService;

    /**
     * 获取岗位列表
     */
    @PreAuthorize("@ss.hasPermi('system:post:list')")
    @GetMapping("/list")
    public TableDataInfo list(PostPageReqDTO post) {
        startPage();
        List<SysPost> list = postService.selectPostList(post);
        return getDataTable(list);
    }

    @Log(title = "岗位管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:post:export')")
    @GetMapping("/export")
    public AjaxResult export(PostPageReqDTO post) {
        List<SysPost> list = postService.selectPostList(post);
        ExcelUtil<SysPost> util = new ExcelUtil<SysPost>(SysPost.class);
        return util.exportExcel(list, "岗位数据");
    }

    /**
     * 根据岗位编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:post:query')")
    @GetMapping(value = "/{postId}")
    public AjaxResult getInfo(@PathVariable Long postId) {
        return AjaxResult.success(postService.selectPostById(postId));
    }

    /**
     * 新增岗位
     */
    @PreAuthorize("@ss.hasPermi('system:post:add')")
    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit(interval = 1000)
    public AjaxResult add(@Validated @RequestBody PostCreateReqDTO post) {
        postService.createPost(post);
        return AjaxResult.success();
    }

    /**
     * 修改岗位
     */
    @PreAuthorize("@ss.hasPermi('system:post:edit')")
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @RepeatSubmit(interval = 1000)
    public AjaxResult edit(@Validated @RequestBody PostUpdateReqDTO post) {
        postService.updatePost(post);
        return AjaxResult.success();
    }

    /**
     * 删除岗位
     */
    @PreAuthorize("@ss.hasPermi('system:post:remove')")
    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{postIds}")
    @RepeatSubmit(interval = 1000)
    public AjaxResult remove(@PathVariable Long postIds) {
        postService.deletePost(postIds);
        return AjaxResult.success();
    }

    /**
     * 获取岗位选择框列表
     */
    @GetMapping("/option-select")
    public AjaxResult optionSelect() {
        List<SysPost> posts = postService.list();
        return AjaxResult.success(posts);
    }
}
