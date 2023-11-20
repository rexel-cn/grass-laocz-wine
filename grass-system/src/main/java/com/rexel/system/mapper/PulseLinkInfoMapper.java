package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.PulseLinkInfo;
import com.rexel.system.domain.dto.PulseLinkInfoDTO;
import com.rexel.system.domain.vo.EquipmentVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 连接信息Mapper接口
 *
 * @author grass-service
 * @date 2022-08-16
 */
@Repository
public interface PulseLinkInfoMapper extends BaseMapper<PulseLinkInfo> {
    /**
     * 查询连接信息列表
     *
     * @param pulseLinkInfo 连接信息
     * @return 连接信息集合
     */
    List<PulseLinkInfo> selectPulseLinkInfoList(PulseLinkInfo pulseLinkInfo);

    /**
     * 查询物联设备
     *
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    List<EquipmentVO> selectEquipmentVO(PulseLinkInfoDTO pulseLinkInfoDTO);

    @InterceptorIgnore(tenantLine = "on")
    Long selectEquipmentVOCount(PulseLinkInfoDTO pulseLinkInfoDTO);


    /**
     * 根据设备id查询物联设备
     *
     * @param deviceId 设备id
     * @return
     */
    PulseLinkInfo selectPulseLinkInfoByDeviceId(String deviceId);

    List<String> selectLinkDeviceType();
}
