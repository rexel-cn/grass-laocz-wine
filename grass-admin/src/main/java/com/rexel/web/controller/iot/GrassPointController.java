package com.rexel.web.controller.iot;

import com.rexel.common.annotation.RepeatSubmit;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.utils.poi.ExcelUtil;
import com.rexel.system.domain.GrassPointInfo;
import com.rexel.system.domain.dto.PulsePointCurveDTO;
import com.rexel.system.domain.dto.PulsePointQueryDTO;
import com.rexel.system.domain.vo.PointQueryVO;
import com.rexel.system.service.IGrassPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName GrassPointController
 * @Description grass-point
 * @Author Hai.Dong
 * @Date 2022/7/20 9:52
 **/
@RestController
@RequestMapping("/rexel-api/grass/point")
public class GrassPointController extends BaseController {

    @Autowired
    private IGrassPointService pointService;


    /**
     * 分页查询
     *
     * @param pointQueryDTO
     * @return
     */
    @GetMapping("/getListPageRealValue")
    public TableDataInfo getListPageRealValue(PulsePointQueryDTO pointQueryDTO) {
        return getDataTable(pointService.getListPageRealValue(pointQueryDTO), "pointList_table");
    }

    /**
     * 分页查询
     *
     * @param pointQueryDTO
     * @return
     */
    @GetMapping("/getListPage")
    public TableDataInfo getListPage(PulsePointQueryDTO pointQueryDTO) {
        startPage();
        return getDataTable(pointService.getListPage(pointQueryDTO), "pointList_table");
    }

    /**
     * 测点修改
     *
     * @param grassPointInfo
     * @return
     */
    @PostMapping("/update")
    @RepeatSubmit(interval = 1000)
    public AjaxResult update(@RequestBody GrassPointInfo grassPointInfo) {
        return toAjax(pointService.updateByGrassPointInfo(grassPointInfo));
    }

    /**
     * 测点曲线
     *
     * @param pointCurveDTO
     * @return
     */
    @PostMapping("/curve")
    @RepeatSubmit(interval = 3000)
    public AjaxResult curve(@RequestBody PulsePointCurveDTO pointCurveDTO) {
        return pointService.curve(pointCurveDTO);
    }

    /**
     * 导入
     *
     * @param file excel文件
     * @return
     * @throws Exception
     */
    @PostMapping("/import")
    @RepeatSubmit(interval = 1000)
    public AjaxResult importPoint(@RequestParam("file") MultipartFile file) throws Exception {
        ExcelUtil<GrassPointInfo> util = new ExcelUtil<>(GrassPointInfo.class);
        List<GrassPointInfo> pointInfos = util.importExcel(file.getInputStream());
        return pointService.importPoint(pointInfos);
    }

    /**
     * 导出
     *
     * @return excel文件
     */
    @PostMapping("/export")
    public void export(@RequestBody PulsePointQueryDTO pointQueryDTO, HttpServletResponse response) throws IOException {
        List<PointQueryVO> list = pointService.export(pointQueryDTO);
        ExcelUtil<PointQueryVO> util = new ExcelUtil<>(PointQueryVO.class);
        util.exportExcel(response, list, "数据测点");
    }

    /**
     * 测点模糊查询
     *
     * @param pointQueryDTO
     * @return
     */
    @GetMapping("/dropDown")
    public AjaxResult dropDown(PulsePointQueryDTO pointQueryDTO) {
        return AjaxResult.success(pointService.dropDown(pointQueryDTO));
    }

}
