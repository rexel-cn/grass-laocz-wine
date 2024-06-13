package com.rexel.laocz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rexel.common.core.domain.AjaxResult;
import com.rexel.laocz.domain.LaoczPotteryAltarManagement;
import com.rexel.laocz.domain.dto.WineEntryPotteryAltarDTO;
import com.rexel.laocz.domain.dto.WineOutPotteryAltarDTO;
import com.rexel.laocz.domain.dto.WinePourPotteryAltarDTO;
import com.rexel.laocz.domain.dto.WineSamplePotteryAltarDTO;
import com.rexel.laocz.domain.vo.*;

import java.text.ParseException;
import java.util.List;

/**
 * 陶坛管理Service接口
 *
 * @author grass-service
 * @date 2024-03-06
 */
public interface ILaoczPotteryAltarManagementService extends IService<LaoczPotteryAltarManagement> {

    /**
     * 查询陶坛管理列表
     *
     * @param laoczPotteryAltarManagement 陶坛管理
     * @return 陶坛管理集合
     */
    List<LaoczPotteryAltarManagement> selectLaoczPotteryAltarManagementList(LaoczPotteryAltarManagement laoczPotteryAltarManagement);
    /**
     * 查询陶坛管理列表详细信息
     *
     * @param laoczPotteryAltarManagement
     * @return
     */
    /**
     * 查询陶坛下拉框
     *
     * @param fireZoneId 防火区ID
     * @return
     */
    List<PotteryPullDownFrameVO> selectPotteryPullDownFrameList(Long fireZoneId);

    /**
     * 获取陶坛信息
     *
     * @param potteryAltarId 主键ID
     * @return
     */
    PotteryAltarInformationVO selectPotteryAltarInformation(Long potteryAltarId);

    /**
     * 获取当前陶坛酒液
     *
     * @param potteryAltarId 主键ID
     * @return
     */
    CurrentWineIndustryVO selectCurrentWineIndustry(Long potteryAltarId) throws ParseException;

    List<PotteryAltarVo> selectLaoczPotteryAltarManagementListDetail(LaoczPotteryAltarManagement laoczPotteryAltarManagement);

    /**
     * 编辑回显,通过Id查询陶坛管理详情
     */
    PotteryAltarVo selectLaoczPotteryAltarManagement(Long potteryAltarId);

    /**
     * 新增陶坛
     *
     * @param laoczPotteryAltarManagement
     * @return
     */
    boolean addPotteryAltar(LaoczPotteryAltarManagement laoczPotteryAltarManagement);

    /**
     * 修改陶坛
     *
     * @param laoczPotteryAltarManagement
     * @return
     */
    boolean updateByIdWithPotteryAltar(LaoczPotteryAltarManagement laoczPotteryAltarManagement);


    /**
     * 删除陶坛管理
     *
     * @param potteryAltarId 陶坛Id
     * @return 返回标识
     */
    boolean removeWithReal(Long potteryAltarId);


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
    List<WineOperaPotteryAltarVO> wineOutPotteryAltarListPage(WineOutPotteryAltarDTO wineOutPotteryAltarDTO);

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
     * @param WineSamplePotteryAltarDTO 取样，陶坛筛选DTO
     */
    List<WineOperaPotteryAltarVO> wineSamplePotteryAltarList(WineSamplePotteryAltarDTO WineSamplePotteryAltarDTO);


    /**
     * 倒坛时，陶坛列表
     * 1：倒坛出，陶坛列表查询条件如下：
     * 1：防火区id
     * 2：陶坛编号 可选 过滤用
     * 3：陶坛状态 必须是使用状态
     * 4：陶坛有酒
     * 5：陶坛目前没有进行其他任务，目前是存储状态
     * 2：返回参数如下：
     * 1：酒批次id
     * 2：陶坛管理主键id （用来倒坛时带入，选择的那个陶坛）
     * 3：陶坛管理编号 用来显示
     * 4：区域名称
     * 5：防火区名称
     * 6：酒液重量
     * 7: 满坛重量
     * 8：存储时长
     * 9：酒品相关信息
     * 3：倒坛入，陶坛列表查询条件如下： 空陶坛或者同一批次的有酒的陶坛
     * 1：酒批次id(以倒坛出的酒返回的查询)
     * 2：防火区id
     * 3：陶坛编号
     * 4：陶坛状态 必须是使用状态
     * 5：陶坛没有酒或者同一个批次有酒的陶坛
     * 6：陶坛如果有酒，那么酒液重量必须小于等于倒坛出的酒液重量
     * 7：陶坛如果有酒目前没有进行其他任务，目前是存储状态
     * <p>
     * 4:返回参数如下：
     * 1:  陶坛管理主键id （用来倒坛时带入，选择的那个陶坛）
     * 2:  陶坛管理编号 用来显示
     * 3:  区域名称
     * 4:  防火区名称
     * 5:  满坛重量
     * 6:  酒液重量
     */

    List<WineOperaPotteryAltarVO> winePourPotteryAltarList(WinePourPotteryAltarDTO winePourPotteryAltarDTO);

    AjaxResult getPotteryAltarManagementQrCodePdf();

    boolean importPotteryAltar(List<PotteryAltarVo> potteryAltarVos);


    WaitPotteryVO getPotteryByWorkOrderId(String workOrderId);

    List<WaitPotteryVO> getOutPotteryByWorkOrderId(String workOrderId,String detailType);

    WaitPotteryVO getFinishPotteryByWorkOrderId(String workOrderId);

    List<WaitPotteryVO> getFinishOutPotteryByWorkOrderId(String workOrderId,String detailType);
}
