package com.rexel.system.domain.dto.role;

import com.rexel.system.domain.base.RoleBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoleUpdateReqDTO extends RoleBase {
    /**
     * 菜单id
     */
    private Set<Long> menuIds;
}
