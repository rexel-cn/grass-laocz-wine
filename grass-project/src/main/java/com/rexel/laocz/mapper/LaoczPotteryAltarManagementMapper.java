package com.rexel.laocz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rexel.laocz.domain.LaoczPotteryAltarManagement;
import com.rexel.laocz.domain.dto.WineEntryPotteryAltarDTO;
import com.rexel.laocz.domain.dto.WineOutPotteryAltarDTO;
import com.rexel.laocz.domain.dto.WineSamplePotteryAltarDTO;
import com.rexel.laocz.domain.vo.CurrentWineIndustryVO;
import com.rexel.laocz.domain.vo.PotteryAltarInformationVO;
import com.rexel.laocz.domain.vo.PotteryAltarVo;
import com.rexel.laocz.domain.vo.WineOperaPotteryAltarVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 陶坛管理Mapper接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Repository
public interface LaoczPotteryAltarManagementMapper extends BaseMapper<LaoczPotteryAltarManagement> {
    /**
     * 查询陶坛管理列表
     *
     * @param laoczPotteryAltarManagement 陶坛管理
     * @return 陶坛管理集合
     */
    List<LaoczPotteryAltarManagement> selectLaoczPotteryAltarManagementList(LaoczPotteryAltarManagement laoczPotteryAltarManagement);

    /**
     * 批量新增陶坛管理
     *
     * @param laoczPotteryAltarManagementList 陶坛管理列表
     * @return 结果
     */
    int batchLaoczPotteryAltarManagement(List<LaoczPotteryAltarManagement> laoczPotteryAltarManagementList);

    /**
     * 获取陶坛信息
     *
     * @param potteryAltarId 主键ID
     * @return
     */
    PotteryAltarInformationVO setPotteryAltarInformation(Long potteryAltarId);

    CurrentWineIndustryVO setCurrentWineIndustry(Long potteryAltarId);


    List<PotteryAltarVo> selectLaoczPotteryAltarManagementListDetail(LaoczPotteryAltarManagement laoczPotteryAltarManagement);

    /**
     * 入酒时，陶坛列表：
     * 1：查询条件如下：
     * 1：防火区id 可选
     * 2：陶坛编号 可选
     * 3：已选陶坛 可选  过滤用
     * 4: 陶坛状态 必须是使用状态
     * 5：陶坛没有酒
     * 2:返回参数如下：
     * 1：陶坛管理主键id （用来入酒时带入，选择的那个陶坛）
     * 2：陶坛管理编号 用来显示
     * 3：区域名称
     * 4：防火区名称
     * 5: 满坛重量
     *
     * @param wineEntryPotteryAltarDTO 入酒，陶坛筛选DTO
     */
    List<WineOperaPotteryAltarVO> wineEntryPotteryAltarList(WineEntryPotteryAltarDTO wineEntryPotteryAltarDTO);

    /**
     * 出酒时，陶坛列表
     * 1：查询条件如下：
     * 1：酒液批次id（可以根据酒液批次查询在酒的信息）  必须是有酒并且存储   可选
     * 2：防火区id 可选
     * 3：陶坛编号 可选 过滤用
     * 4：已选陶坛 可选
     * 5：陶坛状态 必须是使用状态
     * 6：陶坛有酒
     * 7：陶坛目前没有进行其他任务，目前是存储状态
     * 2:返回参数如下：
     * 1：陶坛管理主键id （用来出酒时带入，选择的那个陶坛）
     * 2：陶坛管理编号 用来显示
     * 3：区域名称
     * 4：防火区名称
     * 5: 酒液重量
     * 6: 存储时长
     * 7: 酒品相关信息
     */
    List<WineOperaPotteryAltarVO> wineOutPotteryAltarList(WineOutPotteryAltarDTO wineOutPotteryAltarDTO);

    /**
     * 取样时，陶坛列表
     * 1：查询条件如下：
     * 1：防火区id
     * 2：陶坛编号
     * 3：陶坛状态 必须是使用状态
     * 4：陶坛有酒
     * 5：陶坛目前没有进行其他任务，目前是存储状态
     * <p>
     * 2:返回参数如下：
     * 1:  陶坛管理主键id （用来取样时带入，选择的那个陶坛）
     * 2:  陶坛管理编号 用来显示
     * 3:  区域名称
     * 4:  防火区名称
     * 5:  酒液重量
     * 6: 满坛重量
     * 7:  酒品相关信息 id就行，点击才会查询
     *
     * @param wineSamplePotteryAltarDTO 取样，陶坛筛选DTO
     */
    List<WineOperaPotteryAltarVO> wineSamplePotteryAltarList(WineSamplePotteryAltarDTO wineSamplePotteryAltarDTO);


    /**
     * 倒坛入
     *
     * @param wineOutPotteryAltarDTO
     * @return
     */
    List<WineOperaPotteryAltarVO> pourTankInPotteryAltarList(WineOutPotteryAltarDTO wineOutPotteryAltarDTO);

}
