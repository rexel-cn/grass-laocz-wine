package com.rexel.web.controller.asset;

import com.rexel.common.annotation.Log;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.enums.BusinessType;
import com.rexel.common.utils.StringUtils;
import com.rexel.system.domain.GrassAssetType;
import com.rexel.system.service.IGrassAssetTypeService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资产类型Controller
 *
 * @author grass-service
 * @date 2022-07-21
 */
@RestController
@RequestMapping("/rexel-api/asset/type")
public class GrassAssetTypeController extends BaseController {
    @Autowired
    private IGrassAssetTypeService grassAssetTypeService;

    @GetMapping(value = "/treeSelect")
    public AjaxResult treeSelect() {
        List<GrassAssetType> assetTreeList = grassAssetTypeService.selectAssetTypeTree();
        return AjaxResult.success(grassAssetTypeService.buildAssetTree(assetTreeList));
    }

    /**
     * 查询详情
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable String id) {
        return AjaxResult.success(grassAssetTypeService.getById(id));
    }

    /**
     * 查询资产类型列表（排除节点）
     */
    @GetMapping("/exclude/{id}")
    public AjaxResult excludeChild(@PathVariable(value = "id", required = false) String id) {
        List<GrassAssetType> assetTreeList = grassAssetTypeService.selectAssetTypeTree();
        if (id != null) {
            assetTreeList.removeIf(d -> d.getId().equals(id) || ArrayUtils.contains(StringUtils.split(
                    d.getAncestors(), ","), id + ""));
        }
        return AjaxResult.success(assetTreeList);
    }

    @GetMapping("/list")
    public AjaxResult list() {
        return AjaxResult.success(grassAssetTypeService.selectAssetTypeTree());
    }

    /**
     * 新增资产类型
     *
     * @PreAuthorize("@ss.hasPermi('system:type:add')")
     */
    @Log(title = "资产类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GrassAssetType grassAssetType) {
        return toAjax(grassAssetTypeService.saveAssetType(grassAssetType));
    }


    /**
     * 修改资产类型
     *
     * @PreAuthorize("@ss.hasPermi('system:type:edit')")
     */

    @Log(title = "资产类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GrassAssetType grassAssetType) {
        return toAjax(grassAssetTypeService.updateAssetTypeById(grassAssetType));
    }

    /**
     * 删除资产类型
     *
     * @PreAuthorize("@ss.hasPermi('system:type:remove')")
     */
    @Log(title = "资产类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable String id) {
        return toAjax(grassAssetTypeService.removeAssetTypeById(id));
    }
}
