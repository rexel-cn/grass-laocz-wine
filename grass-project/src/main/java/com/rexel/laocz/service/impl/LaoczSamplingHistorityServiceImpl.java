package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.laocz.domain.LaoczSamplingHistority;
import com.rexel.laocz.domain.LaoczSamplingHistorityVO;
import com.rexel.laocz.domain.vo.*;
import com.rexel.laocz.mapper.LaoczSamplingHistorityMapper;
import com.rexel.laocz.service.ILaoczSamplingHistorityService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 取样记录
 * Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczSamplingHistorityServiceImpl extends ServiceImpl<LaoczSamplingHistorityMapper, LaoczSamplingHistority> implements ILaoczSamplingHistorityService {


    /**
     * 查询取样记录
     * 列表
     *
     * @param laoczSamplingHistority 取样记录
     * @return 取样记录
     */
    @Override
    public List<LaoczSamplingHistority> selectLaoczSamplingHistorityList(LaoczSamplingHistority laoczSamplingHistority) {
        return baseMapper.selectLaoczSamplingHistorityList(laoczSamplingHistority);
    }

    /**
     * 查询取样
     *
     * @param potteryAltarId 陶坛ID
     * @param fromTime       开始时间
     * @param endTime        结束时间
     * @param liquorBatchId  批次ID
     * @return
     */
    @Override
    public List<LaoczSamplingHistorityVO> selectLaoczSamplingHist(Long potteryAltarId, String fromTime, String endTime, String liquorBatchId) {
        return baseMapper.selectLaoczSamplingHist(potteryAltarId, fromTime, endTime, liquorBatchId);
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
    @Override
    public List<LaoczSamplingVO> selectLaoczSamplingList(String fromTime, String endTime, Long areaId, Long fireZoneId, String potteryAltarNumber, String liquorBatchId) {
        return baseMapper.selectLaoczSamplingList(fromTime, endTime, areaId, fireZoneId, potteryAltarNumber, liquorBatchId).stream()
                .peek(aaa -> aaa.setState(aaa.getSamplingFile().isEmpty() ? 0 : 1))
                .collect(Collectors.toList());
    }

    /**
     * 上传文件
     *
     * @param samplingHistorityId 取样历史数据
     * @param url                 链接
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateLaoczSampling(Long samplingHistorityId, String url) {
        LaoczSamplingHistority laoczSamplingHistority = new LaoczSamplingHistority();
        laoczSamplingHistority.setSamplingFile(url);
        return this.lambdaUpdate().eq(LaoczSamplingHistority::getSamplingHistorityId, samplingHistorityId).update(laoczSamplingHistority);
    }

    /**
     * 取样管理详情
     *
     * @param samplingHistorityId 取样历史数据主键
     * @return
     */
    @Override
    public LaoczWineHistoryInfoVO getLaoczSamplingHistoryInfo(Long samplingHistorityId) {
        try {
            PotteryAltarInfomationDInfoVO potteryAltarInfomationDInfoVO = baseMapper.getLaoczSamplingHistoryInfo(samplingHistorityId);
            if (ObjectUtil.isEmpty(potteryAltarInfomationDInfoVO)) {
                return null;
            }
            LaoczWineHistoryInfoVO laoczWineHistoryInfoVO = new LaoczWineHistoryInfoVO();
            laoczWineHistoryInfoVO.setWorkOrderId(potteryAltarInfomationDInfoVO.getWorkOrderId());
            laoczWineHistoryInfoVO.setCurrentWineIndustryVO(BeanUtil.copyProperties(potteryAltarInfomationDInfoVO, CurrentWineIndustryInfoVO.class));
            laoczWineHistoryInfoVO.setPotteryAltarInformationInfoVO(BeanUtil.copyProperties(potteryAltarInfomationDInfoVO, PotteryAltarInformationInfoVO.class));
            return laoczWineHistoryInfoVO;
        } catch (Exception e) {
            log.error("查询失败", e);
            throw new SecurityException("查询失败");
        }
    }

    /**
     * 下载文件
     *
     * @param samplingHistorityId 取样历史数据主键
     * @return
     */
    @Override
    public ResponseEntity<ByteArrayResource> downloadFile(Long samplingHistorityId) {
        LaoczSamplingHistority laoczSamplingHistority = this.lambdaQuery().eq(LaoczSamplingHistority::getSamplingHistorityId, samplingHistorityId).one();
        String samplingFile = laoczSamplingHistority.getSamplingFile();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(samplingFile);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                if (response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300) {
                    byte[] content = EntityUtils.toByteArray(entity);
                    String fileName = laoczSamplingHistority.getSamplingFileName();
                    // 对文件名进行URL编码，确保非ASCII字符正确编码
                    String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.CONTENT_DISPOSITION, encodedFileName.replaceAll("\\+", "%20"));
                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    return ResponseEntity.ok()
                            .headers(headers)
                            .contentType(MediaType.APPLICATION_PDF)
                            .body(new ByteArrayResource(content));
                } else {
                    throw new RuntimeException("从URL获取文件失败，服务器响应状态码:" + response.getStatusLine().getStatusCode());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("获取文件时发生错误", e);
        }
    }
}
