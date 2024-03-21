package com.rexel.laocz.domain.vo;

import com.rexel.common.core.page.PageHeader;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象陶坛实际查询
 *
 * @author ids-admin
 */
@Data
public class TableDataInfoDataReportActualVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 存储总量
     */
    private Double totalStorage;
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
    public TableDataInfoDataReportActualVO() {
    }

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public TableDataInfoDataReportActualVO(List<?> list, int total) {
        this.rows = list;
        this.total = total;
    }
}
