package com.rexel.web.controller.rule;

import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.enums.BusinessType;
import com.rexel.system.domain.GrassNoticeTemplate;
import com.rexel.system.domain.dto.GrassNoticeTemplateDTO;
import com.rexel.system.domain.vo.GrassNoticeTemplateVO;
import com.rexel.system.service.IGrassNoticeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通知配置模板Controller
 *
 * @author grass-service
 * @date 2022-08-01
 */
@RestController
@RequestMapping("/rexel-api/notice-template")
public class GrassNoticeTemplateController extends BaseController {
    @Autowired
    private IGrassNoticeTemplateService grassNoticeTemplateService;

    /**
     * 查询通知配置模板列表
     */
    @GetMapping("/list")
    public TableDataInfo list(GrassNoticeTemplate grassNoticeTemplate) {
        List<GrassNoticeTemplateVO> list = grassNoticeTemplateService.selectGrassNoticeTemplateList(grassNoticeTemplate);
        return getDataTable(list, "grassNoticeTemplate-table");
    }


    /**
     * 获取通知配置模板详细信息
     */
    @PostMapping(value = "/getInfo")
    public AjaxResult getInfo(@RequestBody GrassNoticeTemplate grassNoticeTemplate) {
        return AjaxResult.success(grassNoticeTemplateService.selectGrassNoticeTemplateById(grassNoticeTemplate));
    }

    /**
     * 新增通知配置模板
     */
    @Log(title = "通知配置模板", businessType = BusinessType.INSERT)
    @PostMapping(value = "/add")
    public AjaxResult add(@RequestBody GrassNoticeTemplateDTO grassNoticeTemplate) {
        return toAjax(grassNoticeTemplateService.createGrassNoticeTemplate(grassNoticeTemplate));
    }

    /**
     * 修改通知配置模板
     */
    @Log(title = "通知配置模板", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/edit")
    public AjaxResult edit(@RequestBody GrassNoticeTemplateDTO grassNoticeTemplate) {
        return toAjax(grassNoticeTemplateService.updateGrassNoticeTemplate(grassNoticeTemplate));
    }

    /**
     * 删除通知配置模板
     */
    @Log(title = "通知配置模板", businessType = BusinessType.DELETE)
    @PostMapping(value = "/remove")
    public AjaxResult remove(@RequestBody GrassNoticeTemplateDTO grassNoticeTemplate) {
        return toAjax(grassNoticeTemplateService.deleteGrassNoticeTemplate(grassNoticeTemplate));
    }

    @GetMapping("/dropDown")
    public AjaxResult dropDown() {
        List<GrassNoticeTemplate> list = grassNoticeTemplateService.list();
        return AjaxResult.success(list);
    }

}
