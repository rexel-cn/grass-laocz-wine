package com.rexel.laocz.domain.vo;

import com.rexel.common.core.page.PageHeader;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象陶坛查询
 *
 * @author ids-admin
 */
@Data
public class TableDataInfoDataReportLossVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 申请总量
     */
    private Double totalApplications;
    /**
     * 库存总量
     */
    private Double inventoryQuantity;
    /**
     * 出酒总量
     */
    private Double totalLiquorOutput;
    /**
     * 取样总量
     */
    private Double totalSampling;
    /**
     * 亏损总量
     */
    private Double totalLoss;


    /**
     * 总记录数
     */
    private long total;

    /**
     * 列表数据
     */
    private List<?> rows;

    /**
     * 表头数据
     */
    private List<PageHeader> tableColumnList;

    /**
     * 消息状态码
     */
    private int code;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 表格数据对象
     */
    public TableDataInfoDataReportLossVO() {
    }

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public TableDataInfoDataReportLossVO(List<?> list, int total) {
        this.rows = list;
        this.total = total;
    }
}
