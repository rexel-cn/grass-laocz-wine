package com.rexel.system.domain.vo;

import com.rexel.system.domain.vo.common.NumberCountVO;
import lombok.Builder;
import lombok.Data;

/**
 * @ClassName FrontNumberCountVO
 * @Description 首页物联设备与测点情况统计
 * @Author 孟开通
 * @Date 2023/4/4 18:09
 **/
@Data
@Builder
public class FrontNumberCountVO {
    public NumberCountVO device;
    public NumberCountVO point;
}
