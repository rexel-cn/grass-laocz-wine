package com.rexel.common.utils.poi;/**
 * /**
 *
 * @ClassName CustomCellWriteHandler
 * @Description TODO
 * @Author Hai.Dong
 * @Date 2022/11/10 13:52
 **/
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import org.apache.poi.ss.usermodel.Cell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomCellWriteHandler extends AbstractColumnWidthStyleStrategy {
    private Map<Integer, Map<Integer, Integer>> CACHE = new HashMap<>(8);

    public CustomCellWriteHandler() {
    }

    @Override
    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        //表头自适应列宽
        if (isHead) {
            int length = cell.getStringCellValue().getBytes().length;
            writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), length * 300);
        }

    }

    private Integer dataLength(List<WriteCellData<?>> cellDataList, Cell cell, Boolean isHead) {
        if (isHead) {
            return cell.getStringCellValue().getBytes().length;
        } else {
            CellData cellData = cellDataList.get(0);
            CellDataTypeEnum type = cellData.getType();
            if (type == null) {
                return -1;
            } else {
                switch (type) {
                    case STRING:
                        return cellData.getStringValue().getBytes().length;
                    case BOOLEAN:
                        return cellData.getBooleanValue().toString().getBytes().length;
                    case NUMBER:
                        return cellData.getNumberValue().toString().getBytes().length;
                    default:
                        return -1;
                }
            }
        }
    }
}