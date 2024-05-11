package com.rexel.laocz.bpm.service;

import com.rexel.common.utils.PageResult;
import com.rexel.laocz.domain.dto.ProcessInstancePageDTO;
import com.rexel.laocz.domain.vo.ProcessInstancePageVO;
import com.rexel.laocz.domain.vo.TaskPageVO;

/**
 * @ClassName LaoczBpmProcessInstanceService
 * @Description
 * @Author 孟开通
 * @Date 2024/5/10 13:51
 **/
public interface LaoczBpmProcessInstanceService {
    PageResult<ProcessInstancePageVO> getProcessInstancePage(ProcessInstancePageDTO processInstancePageDTO);
}
