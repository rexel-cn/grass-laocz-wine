package com.rexel.web.controller.iot;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.rexel.common.annotation.RepeatSubmit;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.core.page.TableSupport;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.system.domain.dto.PulseDeviceDTO;
import com.rexel.system.domain.dto.PulseLinkInfoDTO;
import com.rexel.system.domain.vo.EquipmentVO;
import com.rexel.system.service.IEquipmentService;
import com.rexel.system.service.IGrassDeviceInfoService;
import com.rexel.system.service.IPulseLinkInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Kaitong.Meng
 * @ClassName Equipment
 * @Description
 * @Date 2022/7/19 16:11
 */
@RestController
@RequestMapping("/rexel-api/grass/device")
public class GrassDeviceController extends BaseController {

    @Autowired
    private IEquipmentService iEquipmentService;
    @Autowired
    private IGrassDeviceInfoService iGrassDeviceInfoService;
    @Autowired
    private IPulseLinkInfoService iPulseLinkInfoService;

    /**
     * 查询物联设备 有主
     *
     * @return
     */
    //@PreAuthorize("@ss.hasPermi('')")
    @GetMapping("/list")
    //@PreAuthorize("@ss.hasPermi('grass:device:list')")
    public TableDataInfo list(PulseLinkInfoDTO pulseLinkInfoDTO) {
        BeanUtil.copyProperties(TableSupport.buildPageRequest(), pulseLinkInfoDTO);
        pulseLinkInfoDTO.setMaster(true);
        pulseLinkInfoDTO.setTenantId(SecurityUtils.getTenantId());
        List<EquipmentVO> list = iEquipmentService.getList(pulseLinkInfoDTO);
        Page<EquipmentVO> page = new Page<>();
        page.addAll(list);
        page.setTotal(iEquipmentService.getListCount(pulseLinkInfoDTO));
        return getDataTable(page, "equipment-table");
    }

    /**
     * 无主物联设备
     *
     * @return
     */
    @GetMapping("/noBindList")
    //@PreAuthorize("@ss.hasPermi('grass:device:list')")
    public TableDataInfo derelictionList(PulseLinkInfoDTO pulseLinkInfoDTO) {
        BeanUtil.copyProperties(TableSupport.buildPageRequest(), pulseLinkInfoDTO);
        pulseLinkInfoDTO.setMaster(false);
        pulseLinkInfoDTO.setTenantId(SecurityUtils.getTenantId());
        List<EquipmentVO> list = iEquipmentService.getList(pulseLinkInfoDTO);
        Page<EquipmentVO> page = new Page<>();
        Long listCount = iEquipmentService.getListCount(pulseLinkInfoDTO);
        page.setTotal(listCount);
        page.addAll(list);
        return getDataTable(page, "equipment-table");
    }

    /**
     * 创建设备
     *
     * @param pulseDeviceDTO
     * @return
     */
    @PostMapping("/create")
    //@PreAuthorize("@ss.hasPermi('grass:device:create')")
    @RepeatSubmit(interval = 1000)
    public AjaxResult create(@RequestBody PulseDeviceDTO pulseDeviceDTO) {
        return AjaxResult.success(iEquipmentService.creat(pulseDeviceDTO));
    }

    /**
     * 修改设备
     *
     * @param pulseDeviceDTO
     * @return
     */
    @PostMapping("/update")
    //@PreAuthorize("@ss.hasPermi('grass:device:update')")
    public AjaxResult update(@RequestBody PulseDeviceDTO pulseDeviceDTO) {
        return AjaxResult.success(iEquipmentService.update(pulseDeviceDTO));
    }

    /**
     * 删除设备
     *
     * @param pulseDeviceDTO
     * @return
     */
    @PostMapping("/delete")
    //@PreAuthorize("@ss.hasPermi('grass:device:delete')")
    public AjaxResult delete(@RequestBody PulseDeviceDTO pulseDeviceDTO) {
        return AjaxResult.success(iEquipmentService.delete(pulseDeviceDTO));
    }

    /**
     * 设备下拉框
     *
     * @return
     */
    @GetMapping("/dropDown")
    public AjaxResult dropDown() {
        return AjaxResult.success(iGrassDeviceInfoService.dropDown());
    }


    /**
     * 物联设备统计概览
     *
     * @return
     */
    @GetMapping("/equipmentOverview")
    public AjaxResult equipmentOverview() {
        return AjaxResult.success(iEquipmentService.equipmentOverview());
    }

    /**
     * 导出
     *
     * @return excel文件
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody PulseLinkInfoDTO pulseLinkInfoDTO) throws IOException {
        pulseLinkInfoDTO.setMaster(true);
        pulseLinkInfoDTO.setTenantId(SecurityUtils.getTenantId());
        List<EquipmentVO> list = iEquipmentService.getList(pulseLinkInfoDTO);
        ExcelUtil<EquipmentVO> util = new ExcelUtil<>(EquipmentVO.class);
        util.exportExcel(response, list, "物联管理");
    }

    /**
     * 物联设备型号下拉框
     *
     * @return
     */
    @GetMapping("/linkDeviceTypeDropDown")
    public AjaxResult linkDeviceTypeDropDown() {
        return AjaxResult.success(iPulseLinkInfoService.selectLinkDeviceType());
    }
}
