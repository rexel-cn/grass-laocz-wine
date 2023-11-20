package com.rexel.web.controller.tag;

import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.enums.BusinessType;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.system.domain.GrassPointTagPoint;
import com.rexel.system.domain.dto.PulsePointQueryDTO;
import com.rexel.system.domain.vo.GrassPointTagDeviceVO;
import com.rexel.system.domain.vo.GrassPointTagInfoVO;
import com.rexel.system.domain.vo.reduce.ReduceInfo;
import com.rexel.system.service.IGrassPointTagToPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 测点标签Controller
 *
 * @author grass-service
 * @date 2022-10-14
 */
@RestController
@RequestMapping("/rexel-api/tag")
public class GrassPointTagController extends BaseController {
    @Autowired
    private IGrassPointTagToPointService grassPointTagService;

    /**
     * 根据标签键查询测点标签信息数据列表，空值为查全部
     */
    @GetMapping(value = "/listByTagKey")
    public TableDataInfo mlistByTagKey(String tagKey) {
        startPage();
        List<GrassPointTagInfoVO> list = grassPointTagService.selectGrassPointTagRelationListByTagKey(tagKey);
        return getDataTable(list);
    }

    /**
     * 获取关联数量中的测点具体信息
     *
     * @param pointTagInfoId 测点标签信息id
     */
    @GetMapping(value = "/listByRelationID")
    public TableDataInfo listByRelationId(Long pointTagInfoId) {
        startPage();
        List<GrassPointTagDeviceVO> list = grassPointTagService.getByPointTagInfoId(pointTagInfoId);
        return getDataTable(list);
    }

    /**
     * 查询测点标签列表
     */
    @PreAuthorize("@ss.hasPermi('system:tag:list')")
    @GetMapping("/list")
    public TableDataInfo list(GrassPointTagPoint grassPointTagPoint) {
        startPage();
        List<GrassPointTagPoint> list = grassPointTagService.selectGrassPointTagList(grassPointTagPoint);
        return getDataTable(list);
    }

    /**
     * 获取测点标签详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:tag:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(grassPointTagService.getById(id));
    }

    /**
     * 新增测点标签
     */
    @PreAuthorize("@ss.hasPermi('system:tag:add')")
    @Log(title = "测点标签", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GrassPointTagPoint grassPointTagPoint) {
        return toAjax(grassPointTagService.save(grassPointTagPoint));
    }

    /**
     * 修改测点标签
     */
    @PreAuthorize("@ss.hasPermi('system:tag:edit')")
    @Log(title = "测点标签", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GrassPointTagPoint grassPointTagPoint) {
        return toAjax(grassPointTagService.updateById(grassPointTagPoint));
    }

    /**
     * 删除测点标签
     */
    @PreAuthorize("@ss.hasPermi('system:tag:remove')")
    @Log(title = "测点标签", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(grassPointTagService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 导出测点标签列表
     */
    @PreAuthorize("@ss.hasPermi('system:tag:export')")
    @Log(title = "测点标签", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GrassPointTagPoint grassPointTagPoint) throws IOException {
        List<GrassPointTagPoint> list = grassPointTagService.selectGrassPointTagList(grassPointTagPoint);
        ExcelUtil<GrassPointTagPoint> util = new ExcelUtil<>(GrassPointTagPoint.class);
        util.exportExcel(response, list, "测点标签数据");
    }

    /**
     * 根据物联设备Id+测点Id 查询标签
     *
     * @param pointQueryDTO pointQueryDTO
     * @return 结果
     */
    @GetMapping("/getPointTagInfoByPoint")
    public TableDataInfo getPointTagInfoByPoint(PulsePointQueryDTO pointQueryDTO) {
        startPage();
        return getDataTable(grassPointTagService.getPointTagInfoByPoint(pointQueryDTO), "pointTagInfoByPoint");
    }

    /**
     * 查询数据降维测点信息
     * @return 结果
     */
    @GetMapping("/getReduceInfo")
    public List<ReduceInfo> getReduceInfo() {
        return grassPointTagService.getPointTagBucket();
    }
}
