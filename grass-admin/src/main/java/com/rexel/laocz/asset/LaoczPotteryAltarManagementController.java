package com.rexel.laocz.asset;

import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.laocz.domain.LaoczPotteryAltarManagement;
import com.rexel.laocz.domain.dto.WineEntryPotteryAltarDTO;
import com.rexel.laocz.domain.dto.WineOutPotteryAltarDTO;
import com.rexel.laocz.domain.dto.WinePourPotteryAltarDTO;
import com.rexel.laocz.domain.dto.WineSamplePotteryAltarDTO;
import com.rexel.laocz.domain.vo.PotteryAltarVo;
import com.rexel.laocz.domain.vo.WaitPotteryVO;
import com.rexel.laocz.service.ILaoczPotteryAltarManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @ClassName LaoczPotteryAltarManagementController
 * @Description 陶坛管理
 * @Author 孟开通
 * @Date 2024/3/12 17:50
 **/
@RestController
@RequestMapping("/rexel-api/potteryAltar")
public class LaoczPotteryAltarManagementController extends BaseController {

    @Autowired
    private ILaoczPotteryAltarManagementService iLaoczPotteryAltarManagementService;


    /**
     * 查询陶坛管理列表
     */
    @GetMapping("/list")
    public TableDataInfo list(LaoczPotteryAltarManagement laoczPotteryAltarManagement) {
        startPage();
        List<LaoczPotteryAltarManagement> list = iLaoczPotteryAltarManagementService.selectLaoczPotteryAltarManagementList(laoczPotteryAltarManagement);
        return getDataTable(list);
    }

    /**
     * 查询陶坛下拉框
     *
     * @param fireZoneId 防火区ID
     * @return 返回下拉框数据
     */
    @GetMapping("/getPotteryPullDownFrame")
    private AjaxResult getPotteryPullDownFrame(Long fireZoneId) {
        return AjaxResult.success(iLaoczPotteryAltarManagementService.selectPotteryPullDownFrameList(fireZoneId));
    }

    /**
     * 获取陶坛信息
     *
     * @param potteryAltarId 主键ID
     * @return 返回陶坛信息
     */
    @GetMapping("/getPotteryAltarInformation")
    private AjaxResult getPotteryAltarInformation(Long potteryAltarId) {
        return AjaxResult.success(iLaoczPotteryAltarManagementService.selectPotteryAltarInformation(potteryAltarId));
    }

    /**
     * 获取当前陶坛酒液
     *
     * @param potteryAltarId 主键ID
     * @return 返回数据
     */
    @GetMapping("/getCurrentWineIndustry")
    private AjaxResult getCurrentWineIndustry(Long potteryAltarId) throws ParseException {
        return AjaxResult.success(iLaoczPotteryAltarManagementService.selectCurrentWineIndustry(potteryAltarId));
    }

    /**
     * 查询陶坛管理列表详细信息
     *
     * @param laoczPotteryAltarManagement 陶坛对象
     * @return 分页数据
     */
    @GetMapping("listDetail")
    public TableDataInfo listDetail(LaoczPotteryAltarManagement laoczPotteryAltarManagement) {
        startPage();
        List<PotteryAltarVo> list = iLaoczPotteryAltarManagementService.selectLaoczPotteryAltarManagementListDetail(laoczPotteryAltarManagement);
        return getDataTable(list, "potteryAltar");
    }

    /**
     * 通过Id删除陶坛管理
     */
    @DeleteMapping("/{potteryAltarId}")
    public AjaxResult remove(@PathVariable Long potteryAltarId) {

        return toAjax(iLaoczPotteryAltarManagementService.removeWithReal(potteryAltarId));
    }

    /**
     * 编辑回显,通过Id查询陶坛管理详情
     */
    @GetMapping("/{potteryAltarId}")
    public AjaxResult byIdWithPottery(@PathVariable Long potteryAltarId) {
        PotteryAltarVo potteryAltarVo = iLaoczPotteryAltarManagementService.selectLaoczPotteryAltarManagement(potteryAltarId);
        return AjaxResult.success(potteryAltarVo);
    }

    /**
     * 修改陶坛
     *
     * @param laoczPotteryAltarManagement 陶坛信息
     * @return 返回标识
     */
    @PutMapping
    public AjaxResult edit(@RequestBody LaoczPotteryAltarManagement laoczPotteryAltarManagement) {
        return toAjax(iLaoczPotteryAltarManagementService.updateByIdWithPotteryAltar(laoczPotteryAltarManagement));
    }

    /**
     * 新增陶坛
     *
     * @param laoczPotteryAltarManagement 陶坛信息
     * @return 返回标识
     */
    @PostMapping
    public AjaxResult add(@RequestBody LaoczPotteryAltarManagement laoczPotteryAltarManagement) {

        return toAjax(iLaoczPotteryAltarManagementService.addPotteryAltar(laoczPotteryAltarManagement));
    }

    /**
     * 入酒申请，查询筛选陶坛罐
     *
     * @param wineEntryPotteryAltarDTO 入酒申请参数： 入酒，陶坛筛选DTO
     * @return 筛选陶坛罐列表
     */
    @PostMapping("/wineEntryPotteryAltarList")
    public TableDataInfo wineEntryPotteryAltarList(@RequestBody WineEntryPotteryAltarDTO wineEntryPotteryAltarDTO) {
        startPage();
        return getDataTable(iLaoczPotteryAltarManagementService.wineEntryPotteryAltarList(wineEntryPotteryAltarDTO));
    }

    /**
     * 出酒申请，查询筛选陶坛罐
     *
     * @param wineOutPotteryAltarDTO 出酒，陶坛筛选DTO
     * @return 筛选陶坛罐列表
     */
    @PostMapping("/wineOutPotteryAltarList")
    public TableDataInfo wineOutPotteryAltarList(@RequestBody WineOutPotteryAltarDTO wineOutPotteryAltarDTO) {
        return getDataTable(iLaoczPotteryAltarManagementService.wineOutPotteryAltarList(wineOutPotteryAltarDTO));
    }

    /**
     * 取样申请，查询筛选陶坛罐
     *
     * @param WineSamplePotteryAltarDTO 取样，陶坛筛选DTO
     * @return 筛选陶坛罐列表
     */
    @PostMapping("/wineSamplePotteryAltarList")
    public TableDataInfo wineSamplePotteryAltarList(@RequestBody WineSamplePotteryAltarDTO WineSamplePotteryAltarDTO) {
        startPage();
        return getDataTable(iLaoczPotteryAltarManagementService.wineSamplePotteryAltarList(WineSamplePotteryAltarDTO));
    }

    /**
     * 倒坛入酒，查询筛选陶坛罐
     *
     * @param winePourPotteryAltarDTO 倒坛入酒，陶坛筛选DTO
     * @return 筛选陶坛罐列表
     */
    @PostMapping("/winePourPotteryAltarList")
    public TableDataInfo winePourPotteryAltarList(@RequestBody WinePourPotteryAltarDTO winePourPotteryAltarDTO) {
        return getDataTable(iLaoczPotteryAltarManagementService.winePourPotteryAltarList(winePourPotteryAltarDTO));
    }

    /**
     * 获取陶坛PDF文件
     */
    @PostMapping("/qrCodePdf")
    public AjaxResult getQrCodePdf() {
        return iLaoczPotteryAltarManagementService.getPotteryAltarManagementQrCodePdf();
    }

    /**
     * 导出陶坛管理列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        ExcelUtil<PotteryAltarVo> util = new ExcelUtil<>(PotteryAltarVo.class);
        List<PotteryAltarVo> potteryAltarVos = iLaoczPotteryAltarManagementService.selectLaoczPotteryAltarManagementListDetail(null);
        util.exportExcel(response, potteryAltarVos, "陶坛管理");
    }

    /**
     * 导入陶坛管理列表
     */
    @PostMapping("/import")
    public AjaxResult importPotteryAltar(@RequestParam("file") MultipartFile file) throws Exception {
        ExcelUtil<PotteryAltarVo> util = new ExcelUtil<>(PotteryAltarVo.class);
        List<PotteryAltarVo> potteryAltarVos = util.importExcel(file.getInputStream());
        return AjaxResult.success(iLaoczPotteryAltarManagementService.importPotteryAltar(potteryAltarVos));
    }

    /**
     * 待办事宜入酒申请获取陶坛信息,倒坛入酒查询陶坛信息
     *
     * @param workOrderId 工单Id
     * @return 陶坛信息集合
     */
    @GetMapping("/getPotteryByWorkOrderId")
    public AjaxResult getPotteryByWorkOrderId(String workOrderId) {
        WaitPotteryVO waitPotteryVO = iLaoczPotteryAltarManagementService.getPotteryByWorkOrderId(workOrderId);
        return AjaxResult.success(waitPotteryVO);
    }

    /**
     * 待办事宜倒坛出酒坛,出酒，取样
     *
     * @param workOrderId 工单Id
     * @return 陶坛信息
     */
    @GetMapping("/getOutPotteryByWorkOrderId")
    public AjaxResult getOutPotteryByWorkOrderId(String workOrderId,String detailType) {
        List<WaitPotteryVO> waitPotteryVO = iLaoczPotteryAltarManagementService.getOutPotteryByWorkOrderId(workOrderId,detailType);
        return AjaxResult.success(waitPotteryVO);
    }

    /**
     * 已办任务入酒申请获取陶坛信息,倒坛入酒查询陶坛信息
     * @param workOrderId 工单Id
     * @return 信息
     */
    @GetMapping("/getFinishPotteryByWorkOrderId")
    public AjaxResult getFinishPotteryByWorkOrderId(String workOrderId) {
        WaitPotteryVO waitPotteryVO = iLaoczPotteryAltarManagementService.getFinishPotteryByWorkOrderId(workOrderId);
        return AjaxResult.success(waitPotteryVO);
    }

    /**
     * 已办任务出酒申请获取陶坛信息,倒坛入酒查询陶坛信息
     * @param workOrderId 工单Id
     * @param detailType '操作详细类型：1:入酒，2出酒，3倒坛入，4倒坛出，5取样'
     * @return 信息
     */
    @GetMapping("/getFinishOutPotteryByWorkOrderId")
    public AjaxResult getFinishOutPotteryByWorkOrderId(String workOrderId,String detailType) {
        List<WaitPotteryVO> waitPotteryVO = iLaoczPotteryAltarManagementService.getFinishOutPotteryByWorkOrderId(workOrderId,detailType);
        return AjaxResult.success(waitPotteryVO);
    }

}
