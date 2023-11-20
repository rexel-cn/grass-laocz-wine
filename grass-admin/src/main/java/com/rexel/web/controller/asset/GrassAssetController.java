package com.rexel.web.controller.asset;

import com.rexel.common.annotation.Log;
import com.rexel.common.annotation.RepeatSubmit;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.domain.DynamicHeadExcel;
import com.rexel.common.core.page.PageDomain;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.core.page.TableSupport;
import com.rexel.common.enums.BusinessType;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.system.domain.GrassAsset;
import com.rexel.system.domain.dto.GrassAssetDTO;
import com.rexel.system.domain.dto.GrassAssetPointDTO;
import com.rexel.system.domain.dto.GrassAssetQueryDTO;
import com.rexel.system.domain.dto.GrassPointHisDTO;
import com.rexel.system.domain.vo.GrassAssetExportVO;
import com.rexel.system.domain.vo.PointHisVO;
import com.rexel.system.service.IGrassAssetPointService;
import com.rexel.system.service.IGrassAssetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 资产Controller
 *
 * @author grass-service
 * @date 2022-07-19
 */
@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
@RestController
@RequestMapping("/rexel-api/asset")
@Api(tags = "设备管理")
public class GrassAssetController extends BaseController {
    @Autowired
    private IGrassAssetService grassAssetService;
    @Autowired
    private IGrassAssetPointService grassAssetPointService;

    /**
     * 查询设备列表
     *
     * @PreAuthorize("@ss.hasPermi('system:asset:list')")
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询设备列表")
    public TableDataInfo list(GrassAssetQueryDTO grassAsset) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        // 当前页码
        grassAsset.setPageNum(pageDomain.getPageNum());
        // 每页显示记录数
        grassAsset.setPageSize(pageDomain.getPageSize());
        List<GrassAsset> list = grassAssetService.selectGrassAssetList(grassAsset);
        return getDataTable(list, grassAssetService.selectGrassAssetListCount(grassAsset));
    }

    /**
     * 下拉框查询设备列表
     *
     * @param grassAsset
     * @return
     */
    @GetMapping("/drop")
    @ApiOperation(value = "查询设备列表")
    public AjaxResult drop(GrassAssetQueryDTO grassAsset) {
        return AjaxResult.success(grassAssetService.selectGrassAssetList(grassAsset));
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "根据资产设备Id获取详情")
    public AjaxResult list(@PathVariable Long id) {
        return AjaxResult.success(grassAssetService.getAssetById(id));
    }


    /**
     * 新增资产
     *
     * @PreAuthorize("@ss.hasPermi('system:asset:add')")
     */
    @Log(title = "设备", businessType = BusinessType.INSERT)
    @ApiOperation(value = "新增设备")
    @PostMapping
    public AjaxResult add(@RequestBody GrassAssetDTO grassAssetDTO) {
        return toAjax(grassAssetService.saveAsset(grassAssetDTO));
    }

    /**
     * 修改回显
     */
    @GetMapping("/updateInfo/{id}")
    @ApiOperation(value = "修改回显")
    public AjaxResult updateInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(grassAssetService.getAssetInfoById(id));
    }

    /**
     * 修改设备
     *
     * @PreAuthorize("@ss.hasPermi('system:asset:edit')")
     */
    @Log(title = "设备", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "修改设备")
    public AjaxResult edit(@RequestBody GrassAssetDTO grassAssetDTO) {
        return toAjax(grassAssetService.updateAsset(grassAssetDTO));
    }


    /**
     * 获取设备关联测点的实时数据
     *
     * @PreAuthorize("@ss.hasPermi('system:asset:query')")
     */
    @GetMapping(value = "/point/timedata")
    @ApiOperation(value = "获取设备关联测点的实时数据")
    public TableDataInfo pointTimeData(GrassAssetPointDTO grassAssetPointDTO) {
        startPage();
        return getDataTable(grassAssetService.pointTimeData(grassAssetPointDTO));
    }


    /**
     * 删除设备
     *
     * @PreAuthorize("@ss.hasPermi('system:asset:remove')")
     */
    @Log(title = "设备", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除设备")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(grassAssetService.removeAsset(id));
    }


    /**
     * @PreAuthorize("@ss.hasPermi('system:asset:export')")
     */
    @Log(title = "设备", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody GrassAssetQueryDTO grassAsset) throws IOException {
        ExcelUtil<GrassAssetExportVO> util = new ExcelUtil<>(GrassAssetExportVO.class);
        List<GrassAssetExportVO> grassAssetExportVOS = grassAssetService.exportAssetList(grassAsset);
        util.exportExcel(response, grassAssetExportVOS, "资产设备列表");
    }


    /**
     * 导入模板下载
     *
     * @PreAuthorize("@ss.hasPermi('system:asset:export')")
     */
    @Log(title = "设备", businessType = BusinessType.EXPORT)
    @PostMapping("/template")
    public void template(HttpServletResponse response) throws Exception {
        ExcelUtil<GrassAssetExportVO> util = new ExcelUtil<>(GrassAssetExportVO.class);
        util.exportExcel(response, new ArrayList<>(), "资产设备导入模板");
    }

    /**
     * 导入设备列表
     *
     * @PreAuthorize("@ss.hasPermi('system:asset:export')")
     */

    @Log(title = "设备", businessType = BusinessType.EXPORT)
    @PostMapping("/import")
    public AjaxResult importAsset(MultipartFile file) throws Exception {
        if (file.getSize() == 0) {
            return AjaxResult.success("导入文件为空，请进行检查！");
        }
        ExcelUtil<GrassAssetExportVO> util = new ExcelUtil<>(GrassAssetExportVO.class);
        List<GrassAssetExportVO> grassImportList = util.importExcel(file.getInputStream());
        return AjaxResult.success(grassAssetService.importAssetList(grassImportList));
    }

    /**
     * 历史检测数据查询
     *
     * @param grassPointHisDTO
     * @return
     * @throws IOException
     */
    @PostMapping("/pointHis")
    //@Log(title = "历史检测数据")
    @RepeatSubmit(message = "请勿重复查询", interval = 8000)
    public TableDataInfo pointHis(@RequestBody GrassPointHisDTO grassPointHisDTO) {
        PointHisVO pointHisVO = grassAssetService.pointHis(grassPointHisDTO);
        List<Map<String, String>> page = page(pointHisVO.getPointHisData());
        return getDataTable(page, pointHisVO.getHeaderMetadata(),
                pointHisVO.getPointHisData() == null ? 0 : pointHisVO.getPointHisData().size());
    }

    @Log(title = "历史检测数据导出", businessType = BusinessType.EXPORT)
    @PostMapping("/pointHisExport")
    @RepeatSubmit(message = "请勿重复导出", interval = 10000)
    public void pointHisExport(HttpServletResponse response, @RequestBody GrassPointHisDTO grassPointHisDTO) throws IOException {
        PointHisVO pointHisVO = grassAssetService.pointHis(grassPointHisDTO);
        DynamicHeadExcel dynamicHeadExcel = grassAssetService.buildExcel(pointHisVO);
        ExcelUtil.dynamicHeadWrite(response, dynamicHeadExcel.getHead(), dynamicHeadExcel.getData(), "历史检测数据");
    }

    /**
     * 根据测点主键查询关联的资产设备
     *
     * @param id 测点主键
     * @return 资产设备
     */
    @GetMapping("getAssetByPointPrKey/{id}")
    public TableDataInfo getAssetByPointPrKey(@PathVariable("id") Long id) {
        startPage();
        return getDataTable(grassAssetService.getAssetByPointPrKey(id), "assetByPointPrKey");
    }

    /**
     * 获取租户下的所有设备
     *
     * @return
     */
    @GetMapping("getAssetIdAndNameByTenantId")
    public AjaxResult getAssetIdAndNameByTenantId() {
        return AjaxResult.success(grassAssetService.getAssetIdAndNameByTenantId());
    }

    /**
     * 根据设备id查询测点
     *
     * @param id 设备ID
     * @return
     */
    @GetMapping("selectGrassAssetPointsByAssetId")
    public AjaxResult selectGrassAssetPointsByAssetId(String id) {
        return AjaxResult.success(grassAssetPointService.selectGrassAssetPointsByAssetId(id));
    }
}
