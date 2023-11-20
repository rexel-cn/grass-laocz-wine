package com.rexel.web.controller.asset;

import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.system.domain.dto.GrassPointUserStarDTO;
import com.rexel.system.service.IGrassPointUserStarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 【请填写功能名称】Controller
 *
 * @author grass-service
 * @date 2022-10-21
 */
@RestController
@RequestMapping("/rexel-api/asset/star")
public class GrassPointUserStarController extends BaseController {
    @Autowired
    private IGrassPointUserStarService grassPointUserStarService;

    @PostMapping
    public AjaxResult top(@RequestBody GrassPointUserStarDTO grassPointUserStarDTO) {
        return AjaxResult.success(grassPointUserStarService.top(grassPointUserStarDTO));
    }
}
