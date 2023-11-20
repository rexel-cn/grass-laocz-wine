package com.rexel.common.exception.report;

/**
 * @ClassName ExcelReportErrorException
 * @Description 导入报错异常 用于抛出导入报错信息
 * @Author 孟开通
 * @Date 2022/10/31 09:19
 **/
public class ExcelReportErrorException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ExcelReportErrorException(String message) {
        super(message);
    }
}
