package com.rexel.laocz.history;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import com.rexel.common.core.controller.BaseController;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.core.page.TableDataInfo;
import com.rexel.common.exception.file.FileNameLengthLimitExceededException;
import com.rexel.common.utils.DateUtils;
import com.rexel.laocz.domain.LaoczSamplingHistority;
import com.rexel.laocz.service.ILaoczSamplingHistorityService;
import com.rexel.oss.exception.InvalidExtensionException;
import com.rexel.oss.utils.AttachmentHelper;
import org.apache.commons.fileupload.FileUploadBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 取样记录
 *
 * @author Yiyang.Hu
 * @version 1.0
 * @date 2024-03-20 4:54 PM
 */
@RestController
@RequestMapping("/rexel-api/LaoczSamplingHistority")
public class LaoczSamplingHistorityController extends BaseController {
    @Autowired
    public ILaoczSamplingHistorityService laoczSamplingHistorityService;
    @Autowired
    private AttachmentHelper attachmentHelper;

    /**
     * 查询取样
     *
     * @param potteryAltarId 陶坛ID
     * @param fromTime       开始时间
     * @param endTime        结束时间
     * @param liquorBatchId  批次ID
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo getListTableDataList(Long potteryAltarId, String fromTime, String endTime, String liquorBatchId) {
        startPage();
        return getDataTable(laoczSamplingHistorityService.selectLaoczSamplingHist(potteryAltarId, fromTime, endTime, liquorBatchId), "SamplingHistory");
    }

    /**
     * 取样管理
     *
     * @param fromTime           开始时间
     * @param endTime            结束时间
     * @param areaId             场区编号
     * @param fireZoneId         防火区编号
     * @param potteryAltarNumber 陶坛编号
     * @param liquorBatchId      酒品批次
     * @return
     */
    @GetMapping("/samplingList")
    public TableDataInfo getSamplingListTableData(String fromTime, String endTime, Long areaId, Long fireZoneId, String potteryAltarNumber, String liquorBatchId) {
        startPage();
        return getDataTable(laoczSamplingHistorityService.selectLaoczSamplingList(fromTime, endTime, areaId, fireZoneId, potteryAltarNumber, liquorBatchId), "SamplingHistory");
    }

    /**
     * 上传文件
     *
     * @param requestParams
     * @return
     */
    @PutMapping("/uploadFiles")
    public AjaxResult uploadFile(@RequestBody JSONObject requestParams) {
        Long samplingHistorityId = requestParams.getLong("samplingHistorityId");
        String url = requestParams.getString("url");
        if (laoczSamplingHistorityService.updateLaoczSampling(samplingHistorityId, url)) {
            return AjaxResult.success("上传文件成功");
        } else {
            return AjaxResult.error("上传文件失败");
        }
    }

    /**
     * 取样管理详情
     *
     * @param samplingHistorityId 取样历史数据主键
     * @return
     */
    @GetMapping("/getLaoczSamplingHistoryInfo")
    public AjaxResult getLaoczSamplingHistoryInfo(Long samplingHistorityId) {
        return AjaxResult.success(laoczSamplingHistorityService.getLaoczSamplingHistoryInfo(samplingHistorityId));
    }

    /**
     * 上传
     *
     * @param file                文件
     * @param dir                 路径
     * @param samplingHistorityId 取样记录ID
     * @return
     */
    @PostMapping(value = "/common/ossUpload")
    public AjaxResult upload(MultipartFile file, @RequestParam(required = false, defaultValue = "") String dir, Long samplingHistorityId) {
        try {
            if (ObjectUtil.isEmpty(file)) {
                return AjaxResult.error("上传文件不能为空！");
            }
            LaoczSamplingHistority laoczSamplingHistority = new LaoczSamplingHistority();
            laoczSamplingHistority.setSamplingFileName(file.getOriginalFilename());
            laoczSamplingHistority.setSamplingFileDate(DateUtils.getNowDate());
            laoczSamplingHistorityService.lambdaUpdate().eq(LaoczSamplingHistority::getSamplingHistorityId, samplingHistorityId).update(laoczSamplingHistority);
            return AjaxResult.success((Object) attachmentHelper.upload(file, dir));
        } catch (IOException | FileUploadBase.FileSizeLimitExceededException | FileNameLengthLimitExceededException |
                 com.rexel.oss.exception.FileNameLengthLimitExceededException | InvalidExtensionException e) {
            return AjaxResult.error("上传失败");
        }
    }

    /**
     * 文件下载
     *
     * @param samplingHistorityId 取样记录ID
     * @return
     */
    @GetMapping("/downloadFile")
    private ResponseEntity<ByteArrayResource> downloadFile(Long samplingHistorityId) {
        return laoczSamplingHistorityService.downloadFile(samplingHistorityId);
    }
}
