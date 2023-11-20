package com.rexel.web.controller.tag;

import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.enums.BusinessType;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.system.domain.GrassPointTagInfo;
import com.rexel.system.domain.dto.GrassPointTagInfoDTO;
import com.rexel.system.domain.vo.PointTagExportVO;
import com.rexel.system.service.IGrassPointTagInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 测点标签信息Controller
 *
 * @author grass-service
 * @date 2022-10-17
 */
@RestController
@RequestMapping("/rexel-api/info")
public class GrassPointTagInfoController extends BaseController {
    @Autowired
    private IGrassPointTagInfoService grassPointTagInfoService;

    /**
     * 导入模板下载
     * fhw
     *
     * @PreAuthorize("@ss.hasPermi('system:asset:export')")
     */
    @Log(title = "测点标签", businessType = BusinessType.EXPORT)
    @PostMapping("/template")
    public void template(HttpServletResponse response) throws Exception {
        ExcelUtil<PointTagExportVO> util = new ExcelUtil<>(PointTagExportVO.class);
        util.exportExcel(response, new ArrayList<>(), "标签信息列表模板");
    }

    /**
     * fhw
     * 导出查询的数据列表
     * 2022/10/25
     */
    //  @PreAuthorize("@ss.hasPermi('system:tag:export')")
    @Log(title = "测点标签", businessType = BusinessType.EXPORT)
    @PostMapping("/exportBypointTag")
    public void exportBypointTag(HttpServletResponse response) throws IOException {
        ExcelUtil<PointTagExportVO> util = new ExcelUtil<>(PointTagExportVO.class);
        List<PointTagExportVO> pointTagExportVOS = grassPointTagInfoService.selectExport();
        util.exportExcel(response, pointTagExportVOS, "标签信息数据查询列表");
    }

    /**
     * 导入
     * fhw
     * 2022/10/25
     *
     * @param file excel文件
     * @return
     * @throws Exception
     */
    @PostMapping("/import")
    public AjaxResult importPoint(@RequestParam("file") MultipartFile file) throws Exception {
        ExcelUtil<PointTagExportVO> util = new ExcelUtil<>(PointTagExportVO.class);
        List<PointTagExportVO> pointTagInfos = util.importExcel(file.getInputStream());
        return AjaxResult.success(grassPointTagInfoService.importPoint(pointTagInfos));
    }

    /**
     * 查询所有的TagKey去重后返回
     * fhw
     */
    //   @PreAuthorize("@ss.hasPermi('system:info:list')")
    @GetMapping("/listTagKey")
    public List<String> listTagKey(GrassPointTagInfo grassPointTagInfo) {
        return grassPointTagInfoService.selectTagKey();
    }

    /**
     * fhw
     * 修改回显
     */
    @GetMapping("/updateInfo/{id}")
    @ApiOperation(value = "修改回显")
    public AjaxResult updateInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(grassPointTagInfoService.getPointTgeInfoById(id));
    }


    /**
     * 新增标签
     * fhw
     *
     * @param grassPointTagInfoDTO 封装标签信息和测点信息的对象
     * @return
     */
    @Log(title = "标签", businessType = BusinessType.INSERT)
    @ApiOperation(value = "新增标签")
    @PostMapping("/addTag")
    public AjaxResult add(@RequestBody GrassPointTagInfoDTO grassPointTagInfoDTO) {
        return toAjax(grassPointTagInfoService.savePointTagInfo(grassPointTagInfoDTO));
    }

    /**
     * fhw
     * 修改标签
     *
     * @PreAuthorize("@ss.hasPermi('system:asset:edit')")
     */
    @Log(title = "标签", businessType = BusinessType.UPDATE)
    @PutMapping("/editTag")
    @ApiOperation(value = "修改标签")
    public AjaxResult edit(@RequestBody GrassPointTagInfoDTO grassPointTagInfoDTO) {
        return toAjax(grassPointTagInfoService.updatePointTagInfo(grassPointTagInfoDTO));
    }

    /**
     * fhw
     * 删除测点标签
     *
     * @PreAuthorize("@ss.hasPermi('system:asset:remove')")
     */
    @Log(title = "标签", businessType = BusinessType.DELETE)
    @DeleteMapping("/deleteTagById")
    @ApiOperation(value = "删除测点标签")
    public AjaxResult remove(Long pointTagInfoId) {
        return toAjax(grassPointTagInfoService.removePointTagInfo(pointTagInfoId));
    }

    /**
     * 查询测点标签信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(GrassPointTagInfo grassPointTagInfo) {
        startPage();
        List<GrassPointTagInfo> list = grassPointTagInfoService.selectGrassPointTagInfoList(grassPointTagInfo);
        return getDataTable(list);
    }


    /**
     * 测点下拉列表
     *
     * @param grassPointTagInfo
     * @return
     */
    @GetMapping("/pointDropDown")
    public AjaxResult pointDropDown(GrassPointTagInfo grassPointTagInfo) {
        List<GrassPointTagInfo> list = grassPointTagInfoService.selectGrassPointTagInfoList(grassPointTagInfo);
        return AjaxResult.success(list);
    }


    /**
     * 获取测点标签信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:info:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(grassPointTagInfoService.getById(id));
    }

    /**
     * 新增测点标签信息
     */
    @PreAuthorize("@ss.hasPermi('system:info:add')")
    @Log(title = "测点标签信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GrassPointTagInfo grassPointTagInfo) {
        return toAjax(grassPointTagInfoService.save(grassPointTagInfo));
    }

    /**
     * 修改测点标签信息
     */
    @PreAuthorize("@ss.hasPermi('system:info:edit')")
    @Log(title = "测点标签信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GrassPointTagInfo grassPointTagInfo) {
        return toAjax(grassPointTagInfoService.updateById(grassPointTagInfo));
    }

    /**
     * 删除测点标签信息
     */
    @PreAuthorize("@ss.hasPermi('system:info:remove')")
    @Log(title = "测点标签信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(grassPointTagInfoService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * 导出测点标签信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:info:export')")
    @Log(title = "测点标签信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GrassPointTagInfo grassPointTagInfo) throws IOException {
        List<GrassPointTagInfo> list = grassPointTagInfoService.selectGrassPointTagInfoList(grassPointTagInfo);
        ExcelUtil<GrassPointTagInfo> util = new ExcelUtil<>(GrassPointTagInfo.class);
        util.exportExcel(response, list, "测点标签信息数据");
    }
}
