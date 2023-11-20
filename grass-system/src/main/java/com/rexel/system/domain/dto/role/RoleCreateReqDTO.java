package com.rexel.system.domain.dto.role;

import com.rexel.system.domain.base.RoleBase;
import lombok.Data;

import java.util.Set;

@Data
public class RoleCreateReqDTO extends RoleBase {
    /**
     * 菜单id
     */
    private Set<Long> menuIds;
}
