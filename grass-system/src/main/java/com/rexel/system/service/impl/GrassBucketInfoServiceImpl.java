package com.rexel.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.config.PulseUrlConfig;
import com.rexel.common.utils.pulse.PulseHttpRequestUtil;
import com.rexel.common.utils.pulse.PulseHttpResponseUtil;
import com.rexel.system.domain.GrassBucketInfo;
import com.rexel.system.domain.dto.PulseBucketDTO;
import com.rexel.system.mapper.GrassBucketInfoMapper;
import com.rexel.system.service.IGrassBucketInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 存储信息Service业务层处理
 *
 * @author grass-service
 * @date 2022-08-15
 */
@Service
public class GrassBucketInfoServiceImpl extends ServiceImpl<GrassBucketInfoMapper, GrassBucketInfo> implements IGrassBucketInfoService {

    @Autowired
    private PulseUrlConfig pulseUrlConfig;

    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public Boolean insertGrassBucketInfo(GrassBucketInfo grassBucketInfo) {
        //创建bucket创建存储空间
        this.createBucket(grassBucketInfo.getBucketId(), grassBucketInfo.getEverySeconds());
        return this.save(grassBucketInfo);
    }

    /**
     * 查询存储信息列表
     *
     * @param grassBucketInfo 存储信息
     * @return 存储信息
     */
    @Override
    public List<GrassBucketInfo> selectGrassBucketInfoList(GrassBucketInfo grassBucketInfo) {
        return baseMapper.selectGrassBucketInfoList(grassBucketInfo);
    }

    /**
     * 根据租户Id查询
     *
     * @param tenantIds 租户id
     * @return
     */
    @Override
    public List<GrassBucketInfo> selectGrassBucketInfoByTenantIds(List<String> tenantIds) {
        return baseMapper.selectGrassBucketInfoByTenantIds(tenantIds);
    }

    @Override
    public GrassBucketInfo selectGrassBucketInfoByTenantId(String tenantId) {
        return baseMapper.selectGrassBucketInfoByTenantId(tenantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteGrassBucketInfoByTenantId(String tenantId) {
        GrassBucketInfo grassBucketInfo = this.selectGrassBucketInfoByTenantId(tenantId);
        if (grassBucketInfo == null) {
            return true;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("bucketId", grassBucketInfo.getBucketId());
        threadPoolTaskExecutor.execute(() -> {
            log.debug("删除存储空间，bucketId:" + grassBucketInfo.getBucketId());
            Boolean aBoolean = PulseHttpResponseUtil.responseToBoolean(
                    PulseHttpRequestUtil.sendPostJson(pulseUrlConfig.getBucketDelete(), jsonObject.toJSONString()));
            if (!aBoolean) {
                log.error("删除存储空间失败，bucketId:" + grassBucketInfo.getBucketId());
            }
        });
        return baseMapper.deleteGrassBucketInfoByTenantId(tenantId);
    }

    /**
     * 根据bucketId批量删除
     *
     * @param bucketIds
     * @return
     */
    @Override
    public Boolean deleteByBucketIds(List<String> bucketIds) {
        return baseMapper.deleteByBucketIds(bucketIds);
    }

    @Override
    public Boolean updateGrassBucketInfo(Long everySeconds) {
        GrassBucketInfo old = lambdaQuery().one();
        //修改存储时间
        if (ObjectUtil.notEqual(old.getEverySeconds(), everySeconds)) {
            updateBucket(old.getBucketId(), everySeconds);
            old.setEverySeconds(everySeconds);
            updateById(old);
        }
        return true;
    }

    /**
     * 创建bucket
     *
     * @param bucketId
     */
    private String createBucket(String bucketId, Long everySeconds) {
        return PulseHttpResponseUtil.responseToData(
                PulseHttpRequestUtil.sendPostJson(pulseUrlConfig.getBucketCreate(),
                        JSON.toJSONString(new PulseBucketDTO(bucketId, everySeconds))));
    }

    private String updateBucket(String bucketId, Long everySeconds) {
        return PulseHttpResponseUtil.responseToData(
                PulseHttpRequestUtil.sendPostJson(pulseUrlConfig.getBucketUpdate(),
                        JSON.toJSONString(new PulseBucketDTO(bucketId, everySeconds))));
    }

}
