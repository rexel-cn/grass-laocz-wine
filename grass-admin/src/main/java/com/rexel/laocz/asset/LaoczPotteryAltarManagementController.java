package com.rexel.laocz.asset;

import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.laocz.domain.LaoczPotteryAltarManagement;
import com.rexel.laocz.service.ILaoczPotteryAltarManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
