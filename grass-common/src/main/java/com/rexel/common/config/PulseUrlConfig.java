package com.rexel.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @ClassName PulseUrlConfig
 * @Description pulse utl配置类
 * @Author Hai.Dong
 * @Date 2022/5/11 14:42
 **/
@Configuration
@PropertySource("classpath:/pulse-url.properties")
public class PulseUrlConfig {
    private PulseIpConfig pulseIpConfig;

    @Autowired
    public void setPulseIpConfig(PulseIpConfig pulseIpConfig) {
        this.pulseIpConfig = pulseIpConfig;
    }

    @Value("${pulse.token}")
    private String pulseToken;
    @Value("${pulse.refresh.token}")
    private String refreshToken;

    @Value("${pulse.timeSeries.data}")
    private String timeSeriesData;
    @Value("${pulse.timeSeries.last}")
    private String timeSeriesLast;
    @Value("${pulse.timeSeries.batch}")
    private String timeSeriesBatch;
    @Value("${pulse.timeSeries.window}")
    private String timeSeriesWindow;

    @Value("${pulse.point.data}")
    private String pointData;
    @Value("${pulse.point.listRt}")
    private String pointListRt;

    @Value("${pulse.bucket.list}")
    private String bucketList;
    @Value("${pulse.bucket.create}")
    private String bucketCreate;
    @Value("${pulse.bucket.update}")
    private String bucketUpdate;
    @Value("${pulse.bucket.delete}")
    private String bucketDelete;

    @Value("${pulse.down.setDevice}")
    private String downSetDevice;

    @Value("${pulse.reduce.data}")
    private String reduceData;
    @Value("${pulse.reduce.group}")
    private String reduceGroup;
    @Value("${pulse.reduce.window}")
    private String reduceWindow;
    @Value("${pulse.reduce.execute}")
    private String reduceExecute;
    @Value("${pulse.reduce.executeOne}")
    private String reduceExecuteOne;
    @Value("${pulse.reduce.delete}")
    private String reduceDelete;
    @Value("${pulse.reduce.deleteOne}")
    private String reduceDeleteOne;
    @Value("${pulse.reduce.deleteAll}")
    private String reduceDeleteAll;
    @Value("${pulse.reduce.planStatus}")
    private String reducePlanStatus;

    public String getBucketCreate() {
        return getIpPort() + bucketCreate;
    }

    public String getBucketDelete() {
        return getIpPort() + bucketDelete;
    }

    public String getBucketList() {
        return getIpPort() + bucketList;
    }

    public String getTokenUrl() {
        return getIpPort() + pulseToken;
    }

    public String getRefreshToken() {
        return getIpPort() + refreshToken;
    }

    public String getTimeSeriesLast() {
        return getIpPort() + timeSeriesLast;
    }

    public String getGetPointData() {
        return getIpPort() + pointData;
    }

    public String getPointListRt() {
        return getIpPort() + pointListRt;
    }

    public String getBucketUpdate() {
        return getIpPort() + bucketUpdate;
    }

    public String getTimeSeriesBatch() {
        return getIpPort() + timeSeriesBatch;
    }

    public String getTimeSeriesWindow() {
        return getIpPort() + timeSeriesWindow;
    }

    public String getDownSetDevice() {
        return getIpPort() + downSetDevice;
    }

    public String getTimeSeriesData() {
        return getIpPort() + timeSeriesData;
    }

    public String getReduceData() {
        return getIpPort() + reduceData;
    }

    public String getReduceGroup() {
        return getIpPort() + reduceGroup;
    }

    public String getReduceWindow() {
        return getIpPort() + reduceWindow;
    }

    public String getReduceExecute() {
        return getIpPort() + reduceExecute;
    }

    public String getReduceExecuteOne() {
        return getIpPort() + reduceExecuteOne;
    }

    public String getReduceDelete() {
        return getIpPort() + reduceDelete;
    }

    public String getReduceDeleteOne() {
        return getIpPort() + reduceDeleteOne;
    }

    public String getReduceDeleteAll() {
        return getIpPort() + reduceDeleteAll;
    }

    public String getReducePlanStatus() {
        return getIpPort() + reducePlanStatus;
    }

    private String getIpPort() {
        String ip = pulseIpConfig.getIp();
        Integer port = pulseIpConfig.getPort();
        return ip + ":" + port;
    }

}
