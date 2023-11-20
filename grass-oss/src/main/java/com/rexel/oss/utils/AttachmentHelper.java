package com.rexel.oss.utils;

import com.rexel.common.utils.StringUtils;
import com.rexel.oss.OSSUploadHelper;
import com.rexel.oss.config.OssConfig;
import com.rexel.oss.exception.FileNameLengthLimitExceededException;
import com.rexel.oss.exception.InvalidExtensionException;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 *
 *
 * @version V1.0
 * @package com.sunseagear.oss.controller
 * @title: 附件上传助手
 * @description: 附件上传助手 * @date: 2018-04-25 14:25:55
 * @copyright: 2018 Inc. All rights reserved.
 */
@Component("attachmentHelper")
@EnableConfigurationProperties({OssConfig.class})
public class AttachmentHelper {

    @Autowired
    private OssConfig ossConfig;

    private OSSUploadHelper uploadHelper;

    @PostConstruct
    public void initHelper() {
        uploadHelper = new OSSUploadHelper();
        uploadHelper.init(ossConfig);
    }


    public String upload(HttpServletRequest request, MultipartFile file) throws FileSizeLimitExceededException,
            IOException, FileNameLengthLimitExceededException, InvalidExtensionException {
        String basePath = request.getParameter("directory");
        return upload(file, basePath);
    }

    public String upload(MultipartFile file, String directory) throws FileSizeLimitExceededException,
            IOException, FileNameLengthLimitExceededException, InvalidExtensionException {
        return uploadHelper.upload(file, directory);
    }

    public String remote(HttpServletRequest request, String remoteUrl) throws FileSizeLimitExceededException,
            IOException, FileNameLengthLimitExceededException, InvalidExtensionException {
        String basePath = request.getParameter("base_path");
        return uploadHelper.remote(remoteUrl, basePath);
    }

    public String upload(MultipartFile file, String directory, String tenantId) throws FileSizeLimitExceededException,
            IOException, FileNameLengthLimitExceededException, InvalidExtensionException {
        return uploadHelper.upload(file, directory, tenantId);
    }

    public void delete(String filename, String bucketName) throws IOException {
        if (StringUtils.isEmpty(filename)) {
            return;
        }
        uploadHelper.delete(filename, bucketName);
    }

    public void delete(String bucketName) throws IOException {
        uploadHelper.delete(bucketName);
    }
}
