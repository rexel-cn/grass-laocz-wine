package com.rexel.laocz.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.rexel.oss.utils.AttachmentHelper;
import com.rexel.oss.utils.DownLoadUrlSplit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码处理
 *
 * @author Chunhui.Qu
 */
@Service
public class TinciQrCodeUtils {
    @Autowired
    AttachmentHelper attachmentHelper;

    /**
     * 生成二维码内容
     *
     * @param potteryAltarNumber 陶坛编号
     * @return 结果
     */
    public String makeQrCodeContent(String potteryAltarNumber) {
       //return this.ip + ":" + this.port + this.url + potteryAltarNumber;
       return potteryAltarNumber;

    }

    /**
     * 生成并上传二维码
     *
     * @param category 分类
     * @param fileName 文件名
     * @param content 二维码内容
     * @return 结果
     */
    public String generateQrcode(String category, String fileName, String content) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>(3);
            hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8);
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.MARGIN, 1);
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 350, 350, hints);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream);
            byte[] qrCodeImageData = outputStream.toByteArray();

            MultipartFile qrCodeFile = new MockMultipartFile(fileName, null,"image/png", qrCodeImageData);
            return attachmentHelper.upload(qrCodeFile, category, "laocz");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除二维码图片
     *
     * @param url 地址
     */
    public void deleteQrcode(String url) {
        try {
            if (StrUtil.isNotEmpty(url)) {
                attachmentHelper.delete(DownLoadUrlSplit.getFileName(url), DownLoadUrlSplit.getBucketName(url));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
