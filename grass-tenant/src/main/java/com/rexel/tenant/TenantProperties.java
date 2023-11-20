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
                    "pulse_link_info",
                    "gs_finished_product",
                    "gs_finished_product_log",
                    "gs_report_record",
                    "gs_raw_log",
                    "gs_raw",
                    "gs_financial_data",
                    "gs_shift",
                    "gs_team",
                    "gs_team_alarm",
                    "gs_rostering",
                    "gs_rostering_detail",
                    "gs_rostering_history",
                    "gs_rostering_history_detail",
                    "gs_raw",
                    "gs_financial_data",
                    "gs_sale_data",
                    "gs_qc",
                    "gs_raw_auxiliary_materials",
                    "gs_rule_busy",
                    "gs_rule_auxiliary_materials",
                    "gs_rule_auxiliary_materials_detail",
                    "gs_rule_raw",
                    "gs_rule_fp_exp",
                    "gs_rule_fp_stock",
                    "gs_rule_fp_stock_detail",
                    "gs_alarm_busy",
                    "gs_alarm_auxiliary_materials",
                    "gs_alarm_raw",
                    "gs_shift_record",
                    "gs_alarm_finished_product",
                    "gs_shift_point",
                    "gs_team_alarm",
                    "gs_report_conf",
                    "gs_rostering_history_point",
                    "gs_plc_task",
                    "gs_plc_point_data",
                    "gs_plc_command_system_log",
                    "gs_production_efficiency",
                    "gs_production_efficiency_edit_log",
                    "gs_plc_manual_command_fail_log",
                    "gs_plc_manual_command",
                    "gs_busy_financial_data",
                    "gs_busy_qc",
                    "gs_busy_raw_auxiliary_materials",
                    "gs_partition_url",
                    "gs_current_plan",
                    "gs_dict",
                    "gs_province"
            ));
        }
        return instance;
    }
}
