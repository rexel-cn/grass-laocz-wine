package com.rexel.tenant;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.rexel.tenant.context.TenantContextHolder;
import lombok.AllArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;


@AllArgsConstructor
public class CommonTenantHandler implements TenantLineHandler {

    /**
     * 获取租户ID
     *
     * @return 租户ID
     */
    @Override
    public Expression getTenantId() {
        return new StringValue(TenantContextHolder.getRequiredTenantId());
    }

    /**
     * 获取租户字段名称
     *
     * @return 租户字段名称
     */
    @Override
    public String getTenantIdColumn() {
        return com.rexel.tenant.TenantProperties.getInstance().getColumn();
    }

    /**
     * 过滤租户表
     *
     * @param tableName 表名
     * @return 是否进行过滤 返回true 表示不进行多租户处理
     */
    @Override
    public boolean ignoreTable(String tableName) {
        if (!TenantProperties.getInstance().getEnable()) {
            return true;
        }
        return TenantProperties.getInstance().getIgnoreTables().contains(tableName);
    }
}
