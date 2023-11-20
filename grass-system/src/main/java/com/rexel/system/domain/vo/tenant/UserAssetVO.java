package com.rexel.system.domain.vo.tenant;

import lombok.Data;

/**
 * @ClassName UserAssetVO
 * @Description UserAssetVO
 * @Author Hai.Dong
 * @Date 2023/3/1 17:51
 **/
@Data
public class UserAssetVO {
   // 统计
    private  int assetCount;
    private  int userCount;
    private  int deptCount;
    //岗位
    private  int postCount;
}
