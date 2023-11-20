package com.rexel.system.domain.vo;

import com.rexel.system.domain.GrassWebBoard;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class GrassWebBoardVO {

    private String webTypeName;

    private List<GrassWebBoard> webBoardList;

}
