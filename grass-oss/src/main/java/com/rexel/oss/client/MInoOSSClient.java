package com.rexel.oss.client;/**
 * @Author 董海
 * @Date 2022/7/4 14:24
 * @version 1.0
 */

import com.rexel.oss.config.MinioConfig;
import com.rexel.oss.config.OssConfig;
import com.rexel.oss.exception.OSSException;
import io.minio.*;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName MInoOSSClienT
 * @Description minio  client
 * @Author Hai.Dong
 * @Date 2022/7/4 14:24
 **/
@Slf4j
public class MInoOSSClient extends AbstractOSSClient {

    private MinioClient minioClient;

    /**
     * 服务地址（服务接口地址）
     */
    private String url;

    /**
     * 用户名
     */
    private String accessKey;

    /**
     * 密码
     */
    private String secretKey;

    /**
     * 存储桶名称
     */
    private String bucketName;

    @Override
    public void init() {

    }

    @Override
    public void init(String propertiesName) {

    }

    @Override
    public void init(OssConfig config) {
        MinioConfig minIoConfig = config.getMinio();
        url = minIoConfig.getUrl();
        accessKey = minIoConfig.getAccessKey();
        secretKey = minIoConfig.getSecretKey();
        minioClient = MinioClient.builder().endpoint(url).credentials(accessKey, secretKey).build();

    }

    @Override
    public String upload(InputStream inputStream, String path) {
        return null;
    }

    @Override
    public String upload(MultipartFile file, String bucketName, String path) {
        makeBucket(bucketName);
        try {
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName)
                            .object(path)
                            .stream(file.getInputStream(), file.getInputStream().available(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return url + "/" + bucketName + "/" + path;
    }


    @Override
    public void delete(String filename, String bucketName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(bucketName).object(filename).build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new OSSException("删除文件失败", e);
        }
    }

    /**
     * @param bucketName
     */
    @Override
    public void delete(String bucketName) {
        try {
            minioClient.listObjects(
                            ListObjectsArgs.builder().bucket(bucketName).recursive(true).build())
                    .forEach(object -> {
                        try {
                            minioClient.removeObject(
                                    RemoveObjectArgs.builder().bucket(bucketName).object(object.get().objectName()).build());
                        } catch (Exception e) {
                            throw new OSSException("删除文件失败", e);
                        }
                    });
            minioClient.removeBucket(
                    RemoveBucketArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
            throw new OSSException("删除桶失败", e);
        }
    }

    // 检测桶是否存在
    public boolean found(String bucketName) {
        boolean found = false;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return found;
    }

    //  创建桶，检测桶是否存在，若不存在则创建存储桶
    public void makeBucket(String bucketName) {

        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (found) {
                log.info("Bucket already exists");
            } else {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                String policyJson = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\",\"s3:ListBucketMultipartUploads\"],\"Resource\":[\"arn:aws:s3:::" + bucketName + "\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:AbortMultipartUpload\",\"s3:DeleteObject\",\"s3:GetObject\",\"s3:ListMultipartUploadParts\",\"s3:PutObject\"],\"Resource\":[\"arn:aws:s3:::" + bucketName + "/*\"]}]}";
                minioClient.setBucketPolicy(
                        SetBucketPolicyArgs.builder().bucket(bucketName).config(policyJson).build());
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
