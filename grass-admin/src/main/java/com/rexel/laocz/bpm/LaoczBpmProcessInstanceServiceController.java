package com.rexel.laocz.bpm;

import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.utils.PageResult;
import com.rexel.laocz.bpm.service.LaoczBpmProcessInstanceService;
import com.rexel.laocz.domain.dto.ProcessInstancePageDTO;
import com.rexel.laocz.domain.vo.ProcessInstancePageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName LaoczBpmProcessInstanceServiceController
 * @Description 流程实例
 * @Author 孟开通
 * @Date 2024/5/10 13:52
 **/
@RestController
@RequestMapping("/rexel-api/laocz-bpm/processInstance")
public class LaoczBpmProcessInstanceServiceController extends BaseController {
    @Autowired
    private LaoczBpmProcessInstanceService laoczBpmProcessInstanceService;

    @GetMapping("/page")
    public TableDataInfo getProcessInstancePage(ProcessInstancePageDTO processInstancePageDTO) {
        PageResult<ProcessInstancePageVO> processInstancePage = laoczBpmProcessInstanceService.getProcessInstancePage(processInstancePageDTO);
        return getDataTable(processInstancePage.getList(),"processInstancePage", processInstancePage.getTotal());
    }
}
