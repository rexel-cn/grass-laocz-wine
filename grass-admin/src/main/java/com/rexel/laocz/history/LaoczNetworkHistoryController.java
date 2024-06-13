package com.rexel.laocz.history;

import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.laocz.domain.dto.LaoczNetworkHistoryDTO;
import com.rexel.laocz.service.ILaoczNetworkHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName LaoczNetworkHistoryControllre
 * @Description 老村长网络设备报警历史记录
 * @Author 孟开通
 * @Date 2024/6/13 14:43
 **/
@RequestMapping("/rexel-api/network-history")
@RestController
public class LaoczNetworkHistoryController extends BaseController {
    @Autowired
    private ILaoczNetworkHistoryService iLaoczNetworkHistoryService;

    /**
     * 查询老村长网络设备报警历史记录
     *
     * @param laoczNetworkHistory 查询条件
     * @return 网络设备报警历史记录
     */
    @GetMapping
    public TableDataInfo selectLaoczNetworkHistoryList(LaoczNetworkHistoryDTO laoczNetworkHistory) {
        startPage();
        return getDataTable(iLaoczNetworkHistoryService.selectLaoczNetworkHistoryList(laoczNetworkHistory), "laocz_network_history");
    }

}
