package com.rexel.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.system.domain.GrassPointTagInfo;
import com.rexel.system.domain.vo.PointTagExportVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 测点标签信息Mapper接口
 *
 * @author grass-service
 * @date 2022-10-17
 */
@Repository
public interface GrassPointTagInfoMapper extends BaseMapper<GrassPointTagInfo> {
    /**
     * 查询测点标签信息列表
     *
     * @param grassPointTagInfo 测点标签信息
     * @return 测点标签信息集合
     */
    List<GrassPointTagInfo> selectGrassPointTagInfoList(GrassPointTagInfo grassPointTagInfo);

    GrassPointTagInfo selectTagKV(GrassPointTagInfo grassPointTagInfo);

    /**
     * 查询所有的TagKey去重后返回
     * @return
     */
    List<String> selectTagKey();

    /**
     * 导出
     * fhw
     */
    List<PointTagExportVO> selectExport();

    @InterceptorIgnore(tenantLine = "on")
    Boolean deleteByTenantId(String tenantId);
}
