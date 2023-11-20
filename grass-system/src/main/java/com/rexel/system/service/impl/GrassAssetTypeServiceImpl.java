package com.rexel.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.utils.SequenceUtils;
import com.rexel.common.utils.StringUtils;
import com.rexel.system.domain.GrassAsset;
import com.rexel.system.domain.GrassAssetType;
import com.rexel.system.domain.dto.GrassAssetQueryDTO;
import com.rexel.system.domain.vo.GrassAssetTypeTreeVO;
import com.rexel.system.mapper.GrassAssetTypeMapper;
import com.rexel.system.service.IGrassAssetService;
import com.rexel.system.service.IGrassAssetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 资产类型Service业务层处理
 *
 * @author grass-service
 * @date 2022-07-21
 */
@Service
public class GrassAssetTypeServiceImpl extends ServiceImpl<GrassAssetTypeMapper, GrassAssetType> implements IGrassAssetTypeService {
    @Autowired
    private IGrassAssetService assetService;

    @Override
    public List<GrassAssetType> selectAssetTypeTree() {
        return baseMapper.selectAssetTypeTree();
    }

    @Override
    public List<GrassAssetTypeTreeVO> buildAssetTree(List<GrassAssetType> assetTreeList) {
        List<GrassAssetType> returnList = new ArrayList<>();
        List<String> tempList = new ArrayList<>();
        for (GrassAssetType grassAssetType : assetTreeList) {
            tempList.add(grassAssetType.getId());
        }
        for (Iterator<GrassAssetType> iterator = assetTreeList.iterator(); iterator.hasNext(); ) {
            GrassAssetType grassAssetType = iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(grassAssetType.getParentId())) {
                recursionFn(assetTreeList, grassAssetType);
                returnList.add(grassAssetType);
            }
        }
        if (returnList.isEmpty()) {
            returnList = assetTreeList;
        }


        return returnList.stream().map(GrassAssetTypeTreeVO::new).collect(Collectors.toList());
    }


    private void recursionFn(List<GrassAssetType> assetTreeList, GrassAssetType grassAssetType) {
        // 得到子节点列表
        List<GrassAssetType> childList = getChildList(assetTreeList, grassAssetType);
        Integer count = 0;
        for (GrassAssetType tChild : childList) {
            if (hasChild(assetTreeList, tChild)) {
                recursionFn(assetTreeList, tChild);
            }
            count += tChild.getAssetCount();
        }
        grassAssetType.setAssetCount(grassAssetType.getAssetCount() + count);
        grassAssetType.setChildren(childList);
    }

    private List<GrassAssetType> getChildList(List<GrassAssetType> list, GrassAssetType t) {
        List<GrassAssetType> tlist = new ArrayList<>();
        Iterator<GrassAssetType> it = list.iterator();
        while (it.hasNext()) {
            GrassAssetType n = it.next();
            if (n.getParentId().equals(t.getId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<GrassAssetType> list, GrassAssetType t) {
        return getChildList(list, t).size() > 0;
    }

    @Override
    public Boolean saveAssetType(GrassAssetType grassAssetType) {
        lambdaQuery().eq(GrassAssetType::getAssetTypeName, grassAssetType.getAssetTypeName()).oneOpt().ifPresent(assetType -> {
            throw new RuntimeException("资产设备类型名称已存在");
        });
        GrassAssetType byId = getById(grassAssetType.getParentId());
        if (byId != null) {
            grassAssetType.setAncestors(byId.getAncestors() + "," + grassAssetType.getParentId());
        }
        grassAssetType.setId(SequenceUtils.nextId().toString());
        return super.save(grassAssetType);
    }

    @Override
    public Boolean updateAssetTypeById(GrassAssetType grassAssetType) {
        if (Objects.equals(grassAssetType.getParentId(), grassAssetType.getId())) {
            throw new RuntimeException("分类选择错误");
        }
        lambdaQuery().eq(GrassAssetType::getAssetTypeName, grassAssetType.getAssetTypeName())
                .ne(GrassAssetType::getId, grassAssetType.getId()).oneOpt().ifPresent(assetType -> {
                    throw new RuntimeException("资产设备类型名称已存在");
                });

        GrassAssetType newParent = getById(grassAssetType.getParentId());
        GrassAssetType old = getById(grassAssetType.getId());
        if (StringUtils.isNotNull(newParent) && StringUtils.isNotNull(old)) {
            String newAncestors = newParent.getAncestors() + "," + newParent.getId();
            String oldAncestors = old.getAncestors();
            grassAssetType.setAncestors(newAncestors);
            updateChildren(grassAssetType.getId(), newAncestors, oldAncestors);
        } else if (StringUtils.isNull(newParent) && StringUtils.isNotNull(old)) {
            String parentId = "0";
            grassAssetType.setAncestors(parentId);
            updateChildren(grassAssetType.getId(), parentId, old.getAncestors());
        }
        return super.lambdaUpdate().eq(GrassAssetType::getId, grassAssetType.getId())
                .set(GrassAssetType::getAssetTypeName, grassAssetType.getAssetTypeName())
                .set(GrassAssetType::getParentId, grassAssetType.getParentId())
                .set(GrassAssetType::getAncestors, grassAssetType.getAncestors()).update();
    }

    @Override
    public Boolean removeAssetTypeById(String id) {
        GrassAssetQueryDTO grassAsset = new GrassAssetQueryDTO();
        grassAsset.setAssetTypeId(id);
        List<GrassAsset> grassAssets = assetService.selectGrassAssetList(grassAsset);
        if (CollectionUtil.isNotEmpty(grassAssets)) {
            throw new RuntimeException("该分类下存在资产设备，无法删除");
        }
        List<String> collect = baseMapper.selectChildrenById(id).stream().map(GrassAssetType::getId).collect(Collectors.toList());
        collect.add(id);        //删除自己
        return removeByIds(collect);
    }

    @Override
    public Boolean deleteByTenantId(String tenantId) {
        return baseMapper.deleteByTenantId(tenantId);
    }

    public void updateChildren(String id, String newAncestors, String oldAncestors) {
        List<GrassAssetType> children = baseMapper.selectChildrenById(id);
        for (GrassAssetType child : children) {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if (children.size() > 0) {
            baseMapper.updateChildren(children);
        }
    }
}
