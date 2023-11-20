package com.rexel.framework.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.handlers.StrictFill;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.rexel.common.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 填充器
 *
 * @author mengkaitong 2022-05-12 14:22:23.
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        String userName = null;
        try {
            userName = SecurityUtils.getUsername();
            //如果是定时任务，则获取失败，暂定为null
        } catch (Exception e) {
        }
        this.strictInsertFill(metaObject, "createBy", String.class, userName);
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        String userName = null;
        try {
            userName = SecurityUtils.getUsername();
            //如果是定时任务，则获取失败，暂定为null
        } catch (Exception e) {
        }
        this.strictUpdateFill(metaObject, "updateBy", String.class, userName);
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
    }

    @Override
    public MetaObjectHandler strictFill(boolean insertFill, TableInfo tableInfo, MetaObject metaObject, List<StrictFill<?, ?>> strictFills) {
        if ((insertFill && tableInfo.isWithInsertFill()) || (!insertFill && tableInfo.isWithUpdateFill())) {
            strictFills.forEach(i -> {
                final String fieldName = i.getFieldName();
                final Class<?> fieldType = i.getFieldType();
                tableInfo.getFieldList().stream()
                        .filter(j -> j.getProperty().equals(fieldName) && fieldType.equals(j.getPropertyType()) &&
                                ((insertFill && j.isWithInsertFill()) || (!insertFill && j.isWithUpdateFill()))).findFirst()
                        .ifPresent(j -> strictFillStrategy(insertFill, metaObject, fieldName, i.getFieldVal()));
            });
        }
        return this;
    }

    /**
     * 如果是修改操作则覆盖
     *
     * @param insertFill
     * @param metaObject
     * @param fieldName
     * @param fieldVal
     * @return
     */
    public MetaObjectHandler strictFillStrategy(boolean insertFill, MetaObject metaObject, String fieldName, Supplier<?> fieldVal) {
        if (!insertFill) {
            Object obj = fieldVal.get();
            if (Objects.nonNull(obj)) {
                metaObject.setValue(fieldName, obj);
            }
            return this;
        }
        if (metaObject.getValue(fieldName) == null) {
            Object obj = fieldVal.get();
            if (Objects.nonNull(obj)) {
                metaObject.setValue(fieldName, obj);
            }
        }
        return this;
    }
}
