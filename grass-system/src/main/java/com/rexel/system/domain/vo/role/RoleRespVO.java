package com.rexel.system.domain.vo.role;

import com.rexel.system.domain.base.RoleBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RoleRespVO extends RoleBase {
    private List<Integer> checkedKeys;
}
