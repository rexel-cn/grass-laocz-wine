package com.rexel.laocz.asset;

import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.laocz.domain.LaoczNetwork;
import com.rexel.laocz.service.ILaoczNetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName ILaoczNetworkController
 * @Description 网络设备Controller
 * @Author 孟开通
 * @Date 2024/6/13 14:47
 **/
@RestController
@RequestMapping("/rexel-api/network")
public class ILaoczNetworkController extends BaseController {
    @Autowired
    private ILaoczNetworkService iLaoczNetworkService;

    /**
     * 查询网络设备列表
     *
     * @param networkName 网络设备名
     * @param ipAddr      ip地址
     * @return 网络设备
     */
    @GetMapping
    public TableDataInfo selectNetWorkList(String networkName, String ipAddr) {
        startPage();
        return getDataTable(iLaoczNetworkService.selectNetWorkList(networkName, ipAddr), "laocz_network");
    }

    /**
     * 新增网络设备
     *
     * @param network 网络设备
     * @return 结果
     */
    @PostMapping
    public AjaxResult addNetWork(@Validated LaoczNetwork network) {
        return AjaxResult.success(iLaoczNetworkService.addNetWork(network));
    }

    /**
     * 修改网络设备
     *
     * @param network 网络设备
     * @return 结果
     */
    @PutMapping
    public AjaxResult updateNetWork(@Validated LaoczNetwork network) {
        return AjaxResult.success(iLaoczNetworkService.updateNetWork(network));
    }

    /**
     * 删除网络设备
     *
     * @param id id
     * @return 结果
     */
    @DeleteMapping("/{id}")
    public AjaxResult deleteNetWork(@PathVariable("id") Long id) {
        return AjaxResult.success(iLaoczNetworkService.deleteNetWork(id));
    }

    /**
     * 判断网络是否可达
     *
     * @param ipAddr ip地址
     * @return 结果
     */
    @GetMapping("/reachable")
    public AjaxResult netWorkIsReachable(String ipAddr) {
        return AjaxResult.success(iLaoczNetworkService.netWorkIsReachable(ipAddr));
    }

}
