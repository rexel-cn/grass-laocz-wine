package com.rexel.web.controller.webconf;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.enums.BusinessType;
import com.rexel.system.domain.GrassWebBoard;
import com.rexel.system.domain.vo.GrassWebBoardVO;
import com.rexel.system.service.IGrassWebBoardService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 看板地址Controller
 *
 * @author grass-service
 * @date 2022-08-01
 */
@RestController
@RequestMapping("/rexel-api/board")
public class GrassWebBoardController extends BaseController {
    @Autowired
    private IGrassWebBoardService grassWebBoardService;

    /**
     * 查询看板地址列表
     */
    @GetMapping("/list")
    public TableDataInfo list(GrassWebBoard grassWebBoard) {
        startPage();
        List<GrassWebBoard> list = grassWebBoardService.selectGrassWebBoardList(grassWebBoard);
        return getDataTable(list, "grassWebBoard-table");
    }

    @PostMapping("/listAll")
    public AjaxResult listAll(@RequestBody GrassWebBoard grassWebBoard) {
        List<GrassWebBoard> list = grassWebBoardService.selectGrassWebBoardList(grassWebBoard);
        Map<String, List<GrassWebBoard>> map = list.stream().collect(Collectors.groupingBy(GrassWebBoard::getWebTypeName));
        List<GrassWebBoardVO> listVO = new ArrayList<GrassWebBoardVO>();
        for(String key : map.keySet()){
            List<GrassWebBoard> grassWebBoards = map.get(key);
            listVO.add(new GrassWebBoardVO(key,grassWebBoards));
        }

        return AjaxResult.success(listVO);
    }

    /**
     * 获取看板地址详细信息
     */
    @PostMapping(value = "/getInfo")
    public AjaxResult getInfo(@RequestBody GrassWebBoard grassWebBoard) {
        return AjaxResult.success(grassWebBoardService.getById(grassWebBoard));
    }

    /**
     * 新增看板地址
     */
    @Log(title = "看板地址", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody GrassWebBoard grassWebBoard) {
        return toAjax(grassWebBoardService.save(grassWebBoard));
    }

    /**
     * 修改看板地址
     */
    @Log(title = "看板地址", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody GrassWebBoard grassWebBoard) {
        return toAjax(grassWebBoardService.updateById(grassWebBoard));
    }

    /**
     * 删除看板地址
     */
    @Log(title = "看板地址", businessType = BusinessType.DELETE)
    @DeleteMapping("/remove")
    public AjaxResult remove(@RequestBody List<GrassWebBoard> grassWebBoard) {
        List<Long> collect = grassWebBoard.stream().map(GrassWebBoard::getId).collect(Collectors.toList());
        return toAjax(grassWebBoardService.removeByIds(collect));
    }



}
