package com.rexel.tenant;

import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class TenantProperties {

    private static TenantProperties instance;

    private Boolean enable = true;

    private String defaultTenantId = "00000000";

    /**
     * 多租户字段名称
     */
    private String column = "tenant_id";

    /**
     * 多租户系统数据表
     */
    private List<String> ignoreTables = new CopyOnWriteArrayList<>();

    private TenantProperties() {
    }

    public synchronized static TenantProperties getInstance() {
        if (instance == null) {
            instance = new TenantProperties();
            instance.getIgnoreTables().addAll(Arrays.asList(
                    "columns",
                    "tables",
                    "gen_table",
                    "gen_table_column",
                    "sys_menu",
                    "sys_config",
                    "sys_dict_data",
                    "sys_dict_type",
                    "sys_role_data_rule",
                    "sys_logininfor",
                    "sys_tenant",
                    "sys_role_dept",
                    "sys_job",
                    "sys_job_log",
                    "sys_oper_log",
                    "sys_header_metadata",
                    "grass_asset_point",
                    "grass_notice_scope",
                    "grass_notice_mode",
                    "grass_early_warning_carrier",
                    "grass_early_warning_alarm_his",
                    "pulse_link_status",
                    "pulse_link_info"
            ));
        }
        return instance;
    }
}
