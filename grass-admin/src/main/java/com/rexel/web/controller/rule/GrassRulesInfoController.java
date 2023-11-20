package com.rexel.web.controller.rule;

import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.system.domain.GrassRulesInfo;
import com.rexel.system.domain.dto.GrassRulesInfoDTO;
import com.rexel.system.domain.dto.PulseRulesDTO;
import com.rexel.system.domain.dto.RulesExcelDTO;
import com.rexel.system.domain.vo.RulesVO;
import com.rexel.system.service.IGrassRulesInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 报警规则Controller
 *
 * @author grass-service
 * @date 2022-08-16
 */
@RestController
@RequestMapping("/rexel-api/grass/rules")
public class GrassRulesInfoController extends BaseController {
    @Autowired
    private IGrassRulesInfoService grassRulesInfoService;

    /**
     * 查询报警规则列表
     */
    @GetMapping("/list")
    public TableDataInfo list(GrassRulesInfoDTO grassRulesInfo) {
        startPage();
        List<RulesVO> list = grassRulesInfoService.selectGrassRulesInfoList(grassRulesInfo);
        return getDataTable(list, "rulesList-table");
    }

    /**
     * 回显
     *
     * @return 结果
     */
    @GetMapping("/{id}")
    public AjaxResult getById(@PathVariable("id") Long id) {
        return AjaxResult.success(grassRulesInfoService.getRulesInfoById(id));
    }

    /**
     * 报警规则创建
     *
     * @param pulseRulesAddUpdateDTO pulseRulesAddUpdateDTO
     * @return 结果
     */
    @PostMapping("/create")
    public AjaxResult create(@RequestBody PulseRulesDTO pulseRulesAddUpdateDTO) {
        return AjaxResult.success(grassRulesInfoService.create(pulseRulesAddUpdateDTO));
    }

    /**
     * 报警规则修改
     *
     * @param pulseRulesAddUpdateDTO pulseRulesAddUpdateDTO
     * @return 结果
     */
    @PostMapping("/update")
    public AjaxResult update(@RequestBody PulseRulesDTO pulseRulesAddUpdateDTO) {
        return AjaxResult.success(grassRulesInfoService.update(pulseRulesAddUpdateDTO));
    }

    /**
     * 报警规则删除
     *
     * @param pulseRulesAddUpdateDTO pulseRulesAddUpdateDTO
     * @return 结果
     */
    @PostMapping("/delete")
    public AjaxResult delete(@RequestBody PulseRulesDTO pulseRulesAddUpdateDTO) {
        return AjaxResult.success(grassRulesInfoService.delete(pulseRulesAddUpdateDTO));
    }

    /**
     * 查询规则详情
     *
     * @param pulseRulesAddUpdateDTO pulseRulesAddUpdateDTO
     * @return 结果
     */
    @PostMapping("/getAlarmRulesDetailVoByRulesId")
    public AjaxResult getAlarmRulesDetailVoByRulesId(@RequestBody PulseRulesDTO pulseRulesAddUpdateDTO) {
        return AjaxResult.success(grassRulesInfoService.getAlarmRulesDetailVoByRulesId(pulseRulesAddUpdateDTO.getId().toString()));
    }

    /**
     * 修改报警状态
     *
     * @param pulseRulesAddUpdateDTO pulseRulesAddUpdateDTO
     * @return 结果
     */
    @PostMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody PulseRulesDTO pulseRulesAddUpdateDTO) {
        return AjaxResult.success(grassRulesInfoService.changeStatus(pulseRulesAddUpdateDTO));
    }

    /**
     * 规则导出
     *
     * @param response response
     * @throws IOException e
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        List<RulesExcelDTO> list = grassRulesInfoService.export();
        ExcelUtil<RulesExcelDTO> util = new ExcelUtil<>(RulesExcelDTO.class);
        util.exportExcel(response, list, "报警规则");
    }

    /**
     * 导入
     *
     * @param file excel文件
     * @return 结果
     * @throws Exception e
     */
    @PostMapping("/import")
    public AjaxResult importPoint(@RequestParam("file") MultipartFile file) throws Exception {
        ExcelUtil<RulesExcelDTO> util = new ExcelUtil<>(RulesExcelDTO.class);
        List<RulesExcelDTO> list = util.importExcel(file.getInputStream());
        return AjaxResult.success(grassRulesInfoService.importRuleVo(list));
    }

    /**
     * 修改报警状态
     *
     * @param pulseRulesAddUpdateDTO pulseRulesAddUpdateDTO
     * @return 结果
     */
    @PostMapping("/template/update")
    public AjaxResult updateTemplate(@RequestBody PulseRulesDTO pulseRulesAddUpdateDTO) {
        grassRulesInfoService.updateTemplateStatus(pulseRulesAddUpdateDTO);
        return AjaxResult.success();
    }

    /**
     * 查询报警规则列表
     */
    @GetMapping("/template/list")
    public TableDataInfo getTemplateList() {
        startPage();
        GrassRulesInfo select = new GrassRulesInfo();
        select.setIsTemplate(true);
        List<RulesVO> list = grassRulesInfoService.selectGrassRulesInfoList(select);
        return getDataTable(list, "rulesList-table");
    }
}
