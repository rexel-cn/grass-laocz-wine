package com.rexel.web.controller.common;

import cn.hutool.core.util.ObjectUtil;
import com.rexel.common.config.GrassServerConfig;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.common.exception.file.FileNameLengthLimitExceededException;
import com.rexel.common.utils.StringUtils;
import com.rexel.common.utils.file.FileUploadUtils;
import com.rexel.common.utils.file.FileUtils;
import com.rexel.framework.config.ServerConfig;
import com.rexel.oss.exception.InvalidExtensionException;
import com.rexel.oss.utils.AttachmentHelper;
import org.apache.commons.fileupload.FileUploadBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 通用请求处理 路径
 *
 * @author ids-admin
 */
@RestController
public class CommonController {
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);
    private static final String PATH_PREFIX = "template/";

    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private AttachmentHelper attachmentHelper;

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    @GetMapping("common/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!FileUtils.checkAllowDownload(fileName)) {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = GrassServerConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete) {
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求
     */
    @PostMapping("/common/upload")
    public AjaxResult uploadFile(MultipartFile file) throws Exception {
        try {
            // 上传文件路径
            String filePath = GrassServerConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileName", fileName);
            ajax.put("url", url);
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/common/download/resource/{resource}")
    public void resourceDownload(@PathVariable("resource") String resource, HttpServletResponse response) {
        String filepath = PATH_PREFIX + resource;
        log.info("开始下载模板");
        FileUtils.download(response, filepath, resource);
        log.info("下载模板成功");
    }


    @PostMapping(value = "/common/ossUpload")
    public AjaxResult upload(HttpServletRequest request, MultipartFile file, @RequestParam(required = false, defaultValue = "") String dir) {
        try {
            if (ObjectUtil.isEmpty(file)) {
                return AjaxResult.error("上传文件不能为空！");
            }
            return AjaxResult.success((Object) attachmentHelper.upload(request, file, dir));
        } catch (IOException | FileUploadBase.FileSizeLimitExceededException | FileNameLengthLimitExceededException |
                 com.rexel.oss.exception.FileNameLengthLimitExceededException | InvalidExtensionException e) {
            return AjaxResult.error("上传失败");
        }
    }

}
