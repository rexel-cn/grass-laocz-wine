package com.rexel.common.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.core.domain.TreeEntity;
import com.rexel.common.core.mapper.BaseTreeMapper;
import com.rexel.common.core.service.ITreeCommonService;
import com.rexel.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Transactional
public class TreeCommonServiceImpl<M extends BaseTreeMapper<T>, T extends TreeEntity>
        extends ServiceImpl<M, T> implements ITreeCommonService<T> {

    @Override
    public T getById(Serializable id) {
        return baseMapper.selectByTreeId(id);
    }

    @Override
    public List<T> selectTreeList(Wrapper<T> wrapper) {
        List<T> content = baseMapper.selectTreeList(wrapper);
        return content;
    }


    @Override
    public boolean save(T entity) {
        if (!StringUtils.isEmpty(entity.getParentId()) && !"0".equals(entity.getParentId())) {
            T parent = getById(entity.getParentId());
            entity.setParentId(parent.getId());
            entity.setAncestors(parent.makeSelfAsNewParentIds());
        } else {
            entity.setParentId(null);
        }
        return super.save(entity);
    }

    @Override
    public boolean updateById(T entity) {
        if (!StringUtils.isEmpty(entity.getParentId()) && !"0".equals(entity.getParentId())) {
            T parent = getById(entity.getParentId());
            updateSelftAndChild(entity, parent.getId(), parent.makeSelfAsNewParentIds());
        } else {
            entity.setParentId(null);
            updateSelftAndChild(entity, "0", "0,");
        }
        return true;
    }


    private void updateSelftAndChild(T entity, String newParentId, String newParentIds) {
        T oldEntity = getById(entity.getId());
        String oldChildrenParentIds = oldEntity.makeSelfAsNewParentIds();
        entity.setParentId(newParentId);
        entity.setAncestors(newParentIds);
        super.updateById(entity);
        String newChildrenParentIds = entity.makeSelfAsNewParentIds();
        baseMapper.updateSunTreeParentIds(newChildrenParentIds, oldChildrenParentIds);
    }


    @Override
    public boolean removeById(Serializable id) {
        T entity = getById(id);
        baseMapper.deleteSunTree(entity.makeSelfAsNewParentIds());
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        for (Serializable treeId : idList) {
            removeById(treeId);
        }
        return true;
    }


}
