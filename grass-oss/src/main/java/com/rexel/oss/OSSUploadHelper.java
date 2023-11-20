package com.rexel.oss;

import cn.hutool.core.util.StrUtil;
import com.rexel.common.utils.DateUtils;
import com.rexel.common.utils.PropertiesUtil;
import com.rexel.common.utils.SecurityUtils;
import com.rexel.common.utils.StringUtils;
import com.rexel.common.utils.file.FileUtils;
import com.rexel.oss.client.IOSSClient;
import com.rexel.oss.client.OSSClientFactory;
import com.rexel.oss.config.OssConfig;
import com.rexel.oss.constant.Constants;
import com.rexel.oss.exception.FileNameLengthLimitExceededException;
import com.rexel.oss.exception.InvalidExtensionException;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Date;

/**
 * 文件上传操作助手
 */
public class OSSUploadHelper {
    public static final String DEFAULT_CONFIG_FILE = "upload.properties";

    // 默认的文件名最大长度
    public static final int DEFAULT_FILE_NAME_LENGTH = 200;

    public static final String[] IMAGE_EXTENSION = {"bmp", "gif", "jpg", "jpeg", "png"};

    public static final String[] FLASH_EXTENSION = {"swf", "flv"};

    public static final String[] MEDIA_EXTENSION = {"swf", "flv", "mp3", "wav", "wma", "wmv", "mid", "avi", "mpg",
            "asf", "rm", "rmvb"};
    //文件大小限制
    // 默认大小 50M
    public static final long DEFAULT_MAX_SIZE = 52428800;
    //允许扩展名
    public static final String[] DEFAULT_ALLOWED_EXTENSION = {
            // 图片
            "blob", "bmp", "gif", "jpg", "jpeg", "png",
            // word excel powerpoint
            "doc", "docx", "xls", "xlsx", "ppt", "pptx", "html", "htm", "txt",
            // 压缩文件
            "rar", "zip", "gz", "bz2",
            // pdf
            "pdf"};

    // 默认上传的地址
    private static final String DEFAULT_BASE_DIR = "upload";
    public String[] allowedExtension = DEFAULT_ALLOWED_EXTENSION;
    private String ossBaseDir = DEFAULT_BASE_DIR;
    private long maxSize = DEFAULT_MAX_SIZE;
    private boolean needDatePath = false;
    //OSS上传客户端
    private String clientType = Constants.CLIENT_LOCAL;
    private IOSSClient ossClient;

    public void init() {
        init(DEFAULT_CONFIG_FILE);
    }

    public void init(String configname) {
        PropertiesUtil propertiesUtil = new PropertiesUtil(configname);
        maxSize = propertiesUtil.getLong("upload.max.size");
        ossBaseDir = propertiesUtil.getString("upload.base.dir");
        String extension = propertiesUtil.getString("upload.allowed.extension");
        allowedExtension = extension.split(",");
        clientType = propertiesUtil.getString("upload.client.type");
        this.ossClient = OSSClientFactory.build(clientType);
        this.ossClient.init();
    }

    public void init(OssConfig ossConfig) {
        maxSize = ossConfig.getMaxSize();
        ossBaseDir = ossConfig.getBaseDir();
        needDatePath = ossConfig.isNeedDatePath();
        String extension = ossConfig.getAllowedExtension();
        allowedExtension = extension.split(",");
        clientType = ossConfig.getClientType();
        this.ossClient = OSSClientFactory.build(clientType);
        this.ossClient.init(ossConfig);
    }

    /**
     * 以默认配置进行文件上传
     *
     * @param request 当前请求
     * @param file    上传的文件
     *                添加出错信息
     * @return
     * @throws IOException
     * @throws FileNameLengthLimitExceededException
     * @throws InvalidExtensionException
     * @throws FileSizeLimitExceededException
     */
    public String upload(HttpServletRequest request, MultipartFile file, String baseDir)
            throws FileSizeLimitExceededException, InvalidExtensionException, FileNameLengthLimitExceededException,
            IOException {
        return upload(request, file, baseDir, allowedExtension);
    }


    public String upload(MultipartFile file, String baseDir, String tenantId) throws FileSizeLimitExceededException, FileNameLengthLimitExceededException, IOException, InvalidExtensionException {
        return upload(null, file, baseDir, allowedExtension, maxSize, needDatePath, tenantId);
    }


    /**
     * 以默认配置进行文件上传
     *
     * @param request          当前请求
     * @param file             上传的文件
     *                         添加出错信息
     * @param allowedExtension 允许上传的文件类型
     * @return
     * @throws IOException
     * @throws FileNameLengthLimitExceededException
     * @throws InvalidExtensionException
     * @throws FileSizeLimitExceededException
     */
    public String upload(HttpServletRequest request, MultipartFile file, String baseDir, String[] allowedExtension)
            throws FileSizeLimitExceededException, InvalidExtensionException, FileNameLengthLimitExceededException,
            IOException {
        return upload(request, file, baseDir, allowedExtension, maxSize, needDatePath, null);
    }

    /**
     * 文件上传
     *
     * @param request          当前请求 从请求中提取 应用上下文根
     * @param baseDir          相对应用的基目录
     * @param file             上传的文件
     * @param allowedExtension 允许的文件类型 null 表示允许所有
     * @param maxSize          最大上传的大小 -1 表示不限制
     * @param needDatePath     是否需要日期目录和随机文件名前缀
     * @return 返回上传成功的文件名
     * @throws InvalidExtensionException            如果MIME类型不允许
     * @throws FileSizeLimitExceededException       如果超出最大大小
     * @throws FileNameLengthLimitExceededException 文件名太长
     * @throws IOException                          比如读写文件出错时
     */
    public String upload(HttpServletRequest request, MultipartFile file, String baseDir,
                         String[] allowedExtension, long maxSize, boolean needDatePath, String tenantId)
            throws InvalidExtensionException, FileSizeLimitExceededException, IOException,
            FileNameLengthLimitExceededException {
        int fileNamelength = file.getOriginalFilename().length();
        if (fileNamelength > OSSUploadHelper.DEFAULT_FILE_NAME_LENGTH) {
            throw new FileNameLengthLimitExceededException(file.getOriginalFilename(), fileNamelength,
                    OSSUploadHelper.DEFAULT_FILE_NAME_LENGTH);
        }
        assertAllowed(file, allowedExtension, maxSize);
        String filename = extractFilename(file, baseDir, needDatePath);
        filename = StringUtils.trimDiagonal(filename);
        if (StrUtil.isEmpty(tenantId)) {
            tenantId = SecurityUtils.getLoginUser().getTenantId();
        }
        return ossClient.upload(file, tenantId, filename);
    }


    /**
     * 以默认配置进行文件上传
     *
     * @param request   当前请求
     * @param remoteUrl 上传的文件
     *                  添加出错信息
     * @return
     * @throws IOException
     * @throws FileNameLengthLimitExceededException
     * @throws InvalidExtensionException
     * @throws FileSizeLimitExceededException
     */
    public String remote(HttpServletRequest request, String remoteUrl, String baseDir)
            throws FileSizeLimitExceededException, InvalidExtensionException, FileNameLengthLimitExceededException,
            IOException {
        return remote(request, remoteUrl, baseDir, allowedExtension);
    }

    /**
     * 以默认配置进行文件上传
     *
     * @param request          当前请求
     * @param remoteUrl        上传的文件
     *                         添加出错信息
     * @param allowedExtension 允许上传的文件类型
     * @return
     * @throws IOException
     * @throws FileNameLengthLimitExceededException
     * @throws InvalidExtensionException
     * @throws FileSizeLimitExceededException
     */
    public String remote(HttpServletRequest request, String remoteUrl, String baseDir, String[] allowedExtension)
            throws FileSizeLimitExceededException, InvalidExtensionException, FileNameLengthLimitExceededException,
            IOException {
        return remote(request, remoteUrl, baseDir, allowedExtension, maxSize, true);
    }

    /**
     * 文件上传
     *
     * @param request                   当前请求 从请求中提取 应用上下文根
     * @param baseDir                   相对应用的基目录
     * @param remoteUrl                 上传的文件
     * @param allowedExtension          允许的文件类型 null 表示允许所有
     * @param maxSize                   最大上传的大小 -1 表示不限制
     * @param needDatePathAndRandomName 是否需要日期目录和随机文件名前缀
     * @return 返回上传成功的文件名
     * @throws InvalidExtensionException            如果MIME类型不允许
     * @throws FileSizeLimitExceededException       如果超出最大大小
     * @throws FileNameLengthLimitExceededException 文件名太长
     * @throws IOException                          比如读写文件出错时
     */
    public String remote(HttpServletRequest request, String remoteUrl, String baseDir,
                         String[] allowedExtension, long maxSize, boolean needDatePathAndRandomName)
            throws InvalidExtensionException, FileSizeLimitExceededException, IOException,
            FileNameLengthLimitExceededException {
        URL url = new URL(remoteUrl);
        assertAllowed(remoteUrl, allowedExtension, maxSize);
        String filename = extractFilename(remoteUrl, baseDir, needDatePathAndRandomName);
        filename = StringUtils.trimDiagonal(filename);
        return ossClient.upload(url.openStream(), filename);
    }

    public String extractFilename(String remoteUrl, String baseDir, boolean needDatePathAndRandomName)
            throws UnsupportedEncodingException {
        //字符串处理
        if (!StringUtils.isEmpty(this.ossBaseDir)) {
            if (!StringUtils.isEmpty(baseDir)) {
                baseDir = this.ossBaseDir + "/" + baseDir;
            } else {
                baseDir = this.ossBaseDir;
            }
        }
        String filename = remoteUrl;
        int slashIndex = filename.indexOf("/");
        if (slashIndex >= 0) {
            filename = filename.substring(slashIndex + 1);
        }
        if (needDatePathAndRandomName) {
            filename = datePath() + "/" + System.currentTimeMillis() + "."
                    + FileUtils.getExtensionName(filename);
        }
        if (!StringUtils.isEmpty(baseDir)) {
            filename = baseDir + "/" + filename;
        }
        return filename;
    }

    /**
     * 是否允许文件上传
     *
     * @param remoteUrl        上传的文件
     * @param allowedExtension 文件类型 null 表示允许所有
     * @param maxSize          最大大小 字节为单位 -1表示不限制
     * @return
     * @throws InvalidExtensionException      如果MIME类型不允许
     * @throws FileSizeLimitExceededException 如果超出最大大小
     */
    public void assertAllowed(String remoteUrl, String[] allowedExtension, long maxSize)
            throws InvalidExtensionException, FileSizeLimitExceededException {

        String extension = FilenameUtils.getExtension(remoteUrl);
        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension)) {
            if (allowedExtension == IMAGE_EXTENSION) {
                throw new InvalidExtensionException.InvalidImageExtensionException(allowedExtension, extension,
                        remoteUrl);
            } else if (allowedExtension == FLASH_EXTENSION) {
                throw new InvalidExtensionException.InvalidFlashExtensionException(allowedExtension, extension,
                        remoteUrl);
            } else if (allowedExtension == MEDIA_EXTENSION) {
                throw new InvalidExtensionException.InvalidMediaExtensionException(allowedExtension, extension,
                        remoteUrl);
            } else {
                throw new InvalidExtensionException(allowedExtension, extension, remoteUrl);
            }
        }
    }

    public static final String getExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension)) {
            extension = com.rexel.common.utils.file.MimeTypeUtils.getExtension(file.getContentType());
        }
        return extension;
    }

    /**
     * 日期路径 即年/月/日 如2013/01/03
     *
     * @return
     */
    private String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    public String extractFilename(MultipartFile file, String baseDir, boolean needDatePathAndRandomName)
            throws UnsupportedEncodingException {
        if (!StringUtils.isEmpty(this.ossBaseDir)) {
            if (!StringUtils.isEmpty(baseDir)) {
                baseDir = this.ossBaseDir + "/" + baseDir;
            } else {
                baseDir = this.ossBaseDir;
            }
        }
        String filename = file.getOriginalFilename();
        //文件名必须重新命名，以时间精确到毫秒命名
        filename = DateUtils.dateTimeNow() + "."
                + getExtension(file);
        if (needDatePathAndRandomName) {
            filename = datePath() + "/" + filename;
        }
        if (!StringUtils.isEmpty(baseDir)) {
            filename = baseDir + "/" + filename;
        }
        return filename;
    }

    /**
     * 判断MIME类型是否是允许的MIME类型
     *
     * @param extension
     * @param allowedExtension
     * @return
     */
    public boolean isAllowedExtension(String extension, String[] allowedExtension) {
        for (String str : allowedExtension) {
            if (str.trim().equalsIgnoreCase(extension.trim())) {
                return true;
            }
        }
        return false;
    }


    public void delete(String filename, String bucketName) throws IOException {
        if (StringUtils.isEmpty(filename)) {
            return;
        }
        ossClient.delete(filename, bucketName);
    }

    public void delete(String bucketName) {
        if (StringUtils.isEmpty(bucketName)) {
            return;
        }
        ossClient.delete(bucketName);
    }

    /**
     * 是否允许文件上传
     *
     * @param file             上传的文件
     * @param allowedExtension 文件类型 null 表示允许所有
     * @param maxSize          最大大小 字节为单位 -1表示不限制
     * @return
     * @throws InvalidExtensionException      如果MIME类型不允许
     * @throws FileSizeLimitExceededException 如果超出最大大小
     */
    public void assertAllowed(MultipartFile file, String[] allowedExtension, long maxSize)
            throws InvalidExtensionException, FileSizeLimitExceededException {

        String filename = file.getOriginalFilename();

        String extension = getExtension(file);

        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension)) {
            if (allowedExtension == IMAGE_EXTENSION) {
                throw new InvalidExtensionException.InvalidImageExtensionException(allowedExtension, extension,
                        filename);
            } else if (allowedExtension == FLASH_EXTENSION) {
                throw new InvalidExtensionException.InvalidFlashExtensionException(allowedExtension, extension,
                        filename);
            } else if (allowedExtension == MEDIA_EXTENSION) {
                throw new InvalidExtensionException.InvalidMediaExtensionException(allowedExtension, extension,
                        filename);
            } else {
                throw new InvalidExtensionException(allowedExtension, extension, filename);
            }
        }

        long size = file.getSize();
        if (maxSize != -1 && size > maxSize) {
            throw new FileSizeLimitExceededException("not allowed upload upload", size, maxSize);
        }
    }
}
