package com.rexel.laocz.dview;

import com.rexel.common.exception.CustomException;
import com.rexel.laocz.dview.domain.DviewPointDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * @ClassName DviewUtilsPro
 * @Description 添加重试机制
 * @Author 孟开通
 * @Date 2024/3/15 13:31
 **/
@Slf4j
public class DviewUtilsPro {
    public static void writePointValue(DviewPointDTO dviewPointDTO) throws InterruptedException {
        int maxRetries = 10;
        for (int i = 1; i <= maxRetries; i++) {
            try {
                DviewUtils.writePointValue(dviewPointDTO.getPointId(), dviewPointDTO.getPointType(), dviewPointDTO.getPointValue());
                break;
            } catch (Exception e) {

                log.error("【单测点下发，下发异常，正在重试】，测点Id:{},测点名:{},下发值：{},重试次数：{},最大重试次数:{},错误信息：{}"
                        , dviewPointDTO.getPointId()
                        , dviewPointDTO.getPointName()
                        , dviewPointDTO.getPointValue()
                        , i, maxRetries, e.getMessage());
                if (i == 10) {
                    throw new CustomException("写入测点值失败");
                }
                Thread.sleep(500);
            }
        }

    }

    public static void writePointValue(DviewPointDTO... dviewPointDTOS) throws InterruptedException {
        int maxRetries = 10;
        for (int i = 1; i <= maxRetries; i++) {
            try {
                DviewUtils.writePointValue(new ArrayList<>(Arrays.asList(dviewPointDTOS)));
                break;
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                for (DviewPointDTO dviewPointDTO : dviewPointDTOS) {
                    String message = "【测点Id:{},测点名:{},下发值：{}】";
                    String[] args = {dviewPointDTO.getPointId(), dviewPointDTO.getPointName(), dviewPointDTO.getPointValue()};
                    sb.append(MessageFormatter.arrayFormat(message, args).getMessage());
                }
                log.error("【多测点下发，下发异常，正在重试】测点相关信息:{}，重试次数：{},最大重试次数:{},错误信息：{}", sb, i, maxRetries, e.getMessage());
                if (i == 10) {
                    throw new CustomException("写入测点值失败");
                }
                Thread.sleep(500);
            }
        }

    }
}
