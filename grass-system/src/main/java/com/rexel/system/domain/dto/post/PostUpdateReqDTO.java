package com.rexel.system.domain.dto.post;


import com.rexel.system.domain.base.PostBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class PostUpdateReqDTO extends PostBase {

    /**
     * 岗位主键id
     */
    @NotNull(message = "岗位编号不能为空")
    private Long postId;

}
