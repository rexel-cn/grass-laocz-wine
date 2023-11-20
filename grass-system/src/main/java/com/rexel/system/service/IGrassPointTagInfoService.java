package com.rexel.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.system.domain.GrassPointTagInfo;
import com.rexel.system.domain.dto.GrassPointTagInfoDTO;
import com.rexel.system.domain.vo.GrassPointTagInfoDTVO;
import com.rexel.system.domain.vo.PointTagExportVO;

import java.util.List;

/**
 * 测点标签信息Service接口
 *
 * @author grass-service
 * @date 2022-10-17
 */
public interface IGrassPointTagInfoService extends IService<GrassPointTagInfo> {


    /**
     * 查询测点标签信息列表
     *
     * @param grassPointTagInfo 测点标签信息
     * @return 测点标签信息集合
     */
     List<GrassPointTagInfo> selectGrassPointTagInfoList(GrassPointTagInfo grassPointTagInfo);


    Boolean savePointTagInfo(GrassPointTagInfoDTO grassPointTagInfoDTO);

    Boolean updatePointTagInfo(GrassPointTagInfoDTO grassPointTagInfoDTO);

    Boolean removePointTagInfo(Long pointTagInfoId);

    /**
     * 修改回显功能
     * @param id 测点标签信息id
     * @return 返回封装标签信息和测点信息的对象集合
     */
    GrassPointTagInfoDTVO getPointTgeInfoById(Long id);


    /**
     * 导入
     * fhw
     * @param pointTagExportVOS
     * @return
     */
    Boolean importPoint(List<PointTagExportVO> pointTagExportVOS);

    /**
     * 查询所有的TagKey去重后返回
     * @return
     */
    List<String> selectTagKey();

    /**
     * 导出
     * fhw
     *
     * @return
     */
    List<PointTagExportVO> selectExport();

    Boolean deleteByTenantId(String tenantId);
}
