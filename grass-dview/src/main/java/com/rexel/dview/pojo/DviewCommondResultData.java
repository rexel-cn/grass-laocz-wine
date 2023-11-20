package com.rexel.dview.pojo;/**
 * @Author 董海
 * @Date 2022/12/2 13:41
 * @version 1.0
 */

import lombok.Data;

import java.util.List;

/**
 * @ClassName DviewCommondResultData
 * @Description dview 命令下发返回类
 * @Author Hai.Dong
 * @Date 2022/12/2 13:41
 **/
@Data
public class DviewCommondResultData {

    public String requestId;
    public String method;
    public byte variateType;
    // 0 成功 ， 其他：失败
    public Integer statusCode;
    public int okCount;
    public int ngCount;
    public String remark;
    //只读，程序set 进去。 网关不返回此结果
    public List<String> pointIdList;
}
