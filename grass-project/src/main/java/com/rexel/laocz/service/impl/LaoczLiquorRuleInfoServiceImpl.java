package com.rexel.laocz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rexel.common.core.domain.entity.SysUser;
import com.rexel.common.utils.DateUtils;
import com.rexel.common.utils.JudgeUtils;
import com.rexel.common.utils.StringUtils;
import com.rexel.laocz.domain.LaoczBatchPotteryMapping;
import com.rexel.laocz.domain.LaoczLiquorAlarmHistory;
import com.rexel.laocz.domain.LaoczLiquorRuleInfo;
import com.rexel.laocz.domain.LaoczPotteryAltarManagement;
import com.rexel.laocz.domain.vo.LiquorRuleInfoVo;
import com.rexel.laocz.domain.vo.UserInfoVo;
import com.rexel.laocz.mapper.LaoczLiquorRuleInfoMapper;
import com.rexel.laocz.service.ILaoczLiquorRuleInfoService;
import com.rexel.send.factory.MessageSend;
import com.rexel.system.domain.dto.tenant.TenantPageReqDTO;
import com.rexel.system.domain.vo.tenant.TenantRespVO;
import com.rexel.system.mapper.SysTenantMapper;
import com.rexel.system.service.ISysUserService;
import com.rexel.tenant.util.TenantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 酒液批次存储报警规则信息Service业务层处理
 *
 * @author grass-service
 * @date 2024-03-06
 */
@Service
public class LaoczLiquorRuleInfoServiceImpl extends ServiceImpl<LaoczLiquorRuleInfoMapper, LaoczLiquorRuleInfo> implements ILaoczLiquorRuleInfoService {


    @Autowired
    private ISysUserService userService;

    @Autowired
    private LaoczBatchPotteryMappingServiceImpl laoczBatchPotteryMappingService;

    @Autowired
    private LaoczPotteryAltarManagementServiceImpl laoczPotteryAltarManagementService;

    @Autowired
    private LaoczLiquorAlarmHistoryServiceImpl laoczLiquorAlarmHistoryService;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private MessageSend messageSend;

    @Autowired
    private SysTenantMapper sysTenantMapper;

    /**
     * 查询酒液批次存储报警规则信息列表
     *
     * @param laoczLiquorRuleInfo 酒液批次存储报警规则信息
     * @return 酒液批次存储报警规则信息
     */
    @Override
    public List<LiquorRuleInfoVo> selectLaoczLiquorRuleInfoList(LaoczLiquorRuleInfo laoczLiquorRuleInfo) {

        List<LiquorRuleInfoVo> laoczLiquorRuleInfos = baseMapper.selectLaoczLiquorRuleInfoListVo(laoczLiquorRuleInfo);

        for (LiquorRuleInfoVo liquorRuleInfo : laoczLiquorRuleInfos) {
            String liquorRuleNotifyUser = liquorRuleInfo.getLiquorRuleNotifyUser();

            String[] split = liquorRuleNotifyUser.split(",");

            int length = split.length;

            liquorRuleInfo.setCount(length);

            Long[] result = new Long[split.length];
            for (int i = 0; i < split.length; i++) {
                result[i] = Long.parseLong(split[i]);
            }

            liquorRuleInfo.setNoticePeopleArray(result);
        }
        return laoczLiquorRuleInfos;
    }

    /**
     * 根据id查询通知人员信息
     *
     * @param id 酒液批次报警ID
     * @return 用户信息
     */

    @Override
    public List<UserInfoVo> getByIdWithUserInfo(Long id) {
        List<UserInfoVo> userInfoVos = new ArrayList<>();

        LaoczLiquorRuleInfo laoczLiquorRuleInfo = this.getById(id);

        if (laoczLiquorRuleInfo == null) {
            return userInfoVos;
        }

        String ruleNotifyTemplate = laoczLiquorRuleInfo.getLiquorRuleNotifyUser();

        String[] split = ruleNotifyTemplate.split(",");

        if (split.length == 0) {
            return userInfoVos;
        }

        for (String s : split) {
            SysUser sysUser = userService.getById(s);
            if (sysUser != null) {
                UserInfoVo userInfoVo = new UserInfoVo();
                userInfoVo.setUserId(sysUser.getUserId());
                userInfoVo.setUserName(sysUser.getUserName());
                userInfoVo.setPhoneNumber(sysUser.getPhoneNumber());

                userInfoVos.add(userInfoVo);
            }
        }
        return userInfoVos;
    }

    /**
     * 新增报警规则
     *
     * @param laoczLiquorRuleInfo 报警规则
     * @return
     */
    @Override
    public void saveWithRule(LaoczLiquorRuleInfo laoczLiquorRuleInfo) {
        laoczLiquorRuleInfo.setLiquorRuleNotifyTemplate("于{报警时间},{酒液批次}触发存储时长{判断条件}{报警阈值}的{规则名称},达到{报警值},对应陶坛为{陶坛名称},请相关人员注意此批次的酒液！");
        Long[] noticePeopleArray = laoczLiquorRuleInfo.getNoticePeopleArray();

        if (noticePeopleArray != null && noticePeopleArray.length > 0) {

            laoczLiquorRuleInfo.setLiquorRuleNotifyUser(StringUtils.join(noticePeopleArray, ","));

        }

        save(laoczLiquorRuleInfo);
    }

    /**
     * 修改报警规则
     *
     * @param laoczLiquorRuleInfo 报警规则
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWithRule(LaoczLiquorRuleInfo laoczLiquorRuleInfo) {

        if (laoczLiquorRuleInfo.getNoticePeopleArray() != null && laoczLiquorRuleInfo.getNoticePeopleArray().length > 0) {
            laoczLiquorRuleInfo.setLiquorRuleNotifyUser(StringUtils.join(laoczLiquorRuleInfo.getNoticePeopleArray(), ","));
        }

        updateById(laoczLiquorRuleInfo);
    }

    /**
     * 酒液批次下拉
     *
     * @return
     */
    @Override
    public List<String> dropDown() {

        return baseMapper.dropDown();
    }

    /**
     * 定时酒批次报警
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pushAlarm() {
        List<TenantRespVO> tenantRespVOS = sysTenantMapper.selectSysTenantVOList(new TenantPageReqDTO());
        for (TenantRespVO tenantRespVO : tenantRespVOS) {
            TenantUtils.execute(tenantRespVO.getTenantId(), () -> {
                List<LaoczLiquorAlarmHistory> alarmHistories = new ArrayList<>();
                //筛选启用的报警规则
                LaoczLiquorRuleInfo laoczLiquorRuleInfo1 = new LaoczLiquorRuleInfo();
                laoczLiquorRuleInfo1.setLiquorRuleEnable("1");
                List<LiquorRuleInfoVo> LaoczLiquorRuleInfos = this.selectLaoczLiquorRuleInfoList(laoczLiquorRuleInfo1);

                //拿到所有的酒液批次Id
                List<Long> collectId = LaoczLiquorRuleInfos.stream().map(LaoczLiquorRuleInfo::getLiquorBatchId).collect(Collectors.toList());
                if (CollectionUtil.isEmpty(collectId)){
                   return;
                }
                //拿到所有陶坛信息
                List<LaoczBatchPotteryMapping> list1 = laoczBatchPotteryMappingService.lambdaQuery()
                        .in(LaoczBatchPotteryMapping::getLiquorBatchId, collectId)
                        .eq(LaoczBatchPotteryMapping::getRealStatus, 1).list();

                //存到Map
                Map<String, List<LaoczBatchPotteryMapping>> collect1 = list1.stream().collect(Collectors.groupingBy(LaoczBatchPotteryMapping::getLiquorBatchId));
                for (LaoczLiquorRuleInfo laoczLiquorRuleInfo : LaoczLiquorRuleInfos) {
                    //获取报警阈值
                    String liquorRuleThreshold = laoczLiquorRuleInfo.getLiquorRuleThreshold();
                    List<LaoczBatchPotteryMapping> batchPotteryMappings = collect1.get(laoczLiquorRuleInfo.getLiquorBatchId().toString());
                    if (CollectionUtil.isNotEmpty(batchPotteryMappings)) {
                        //根据入酒时间进行判断是否达到阈值进行报警推送
                        for (LaoczBatchPotteryMapping batchPotteryMapping : batchPotteryMappings) {
                            //获取入酒时间
                            Date storingTime = batchPotteryMapping.getStoringTime();
                            //获取当前时间
                            Date nowDate = DateUtils.getNowDate();
                            // 计算两个Date对象之间的毫秒数差异
                            long diffInMillis = nowDate.getTime() - storingTime.getTime();

                            // 将毫秒数差异转换为秒数
                            long diffInSeconds = diffInMillis / 1000;
                            long day = 24 * 60 * 60;
                            //判断是否报警
                            if(JudgeUtils.judge(laoczLiquorRuleInfo.getLiquorRuleJudge(),new BigDecimal(diffInSeconds),new BigDecimal(Long.parseLong(liquorRuleThreshold) * day))){
                                //超过报警阈值,保存报警记录，并通知
                                alarmHistories.add(getAlarm(laoczLiquorRuleInfo, batchPotteryMapping, diffInSeconds / day));
                                //格式化报警消息体
                                String phSendFormat = phSendFormat(batchPotteryMapping, laoczLiquorRuleInfo, diffInSeconds / day);
                                List<SysUser> sysUserList = iSysUserService.lambdaQuery()
                                        .in(SysUser::getUserId, Arrays.asList(laoczLiquorRuleInfo.getNoticePeopleArray()))
                                        .list();
                                //报警
                                messageSend.startSend(sysUserList, phSendFormat, null, 0L);
                            }
                        }
                    }
                }
                //保存报警信息
                if (CollectionUtil.isNotEmpty(alarmHistories)) {
                    laoczLiquorAlarmHistoryService.saveBatch(alarmHistories);
                }
            });
        }

    }

    /**
     * 报警内容格式化
     *
     * @param batchPotteryMapping 陶坛与批次实时对象
     * @param laoczLiquorRuleInfo 报警规则对象
     * @return
     */
    private String phSendFormat(LaoczBatchPotteryMapping batchPotteryMapping, LaoczLiquorRuleInfo laoczLiquorRuleInfo, long day) {
        LaoczPotteryAltarManagement laoczPotteryAltarManagement = laoczPotteryAltarManagementService.getById(batchPotteryMapping.getPotteryAltarId());
        //报警时间
        String date = DateUtil.format(new Date(), DatePattern.NORM_DATETIME_FORMAT);
        String noticeTemplate = StringUtils.canBeFormatted(laoczLiquorRuleInfo.getLiquorRuleNotifyTemplate());
        //报警值

        return MessageFormat.format(noticeTemplate,
                date, batchPotteryMapping.getLiquorBatchId(), laoczLiquorRuleInfo.getLiquorRuleJudge()
                , laoczLiquorRuleInfo.getLiquorRuleThreshold(), laoczLiquorRuleInfo.getLiquorRuleName(), day, laoczPotteryAltarManagement.getPotteryAltarNumber());
    }

    private LaoczLiquorAlarmHistory getAlarm(LaoczLiquorRuleInfo laoczLiquorRuleInfo, LaoczBatchPotteryMapping batchPotteryMapping, long day) {
        LaoczLiquorAlarmHistory laoczLiquorAlarmHistory = new LaoczLiquorAlarmHistory();
        BeanUtil.copyProperties(laoczLiquorRuleInfo, laoczLiquorAlarmHistory);
        laoczLiquorAlarmHistory.setPotteryAltarId(batchPotteryMapping.getPotteryAltarId());
        laoczLiquorAlarmHistory.setAlarmValue(laoczLiquorRuleInfo.getLiquorRuleThreshold());
        laoczLiquorAlarmHistory.setLiquorRuleNotifyTemplate(phSendFormat(batchPotteryMapping, laoczLiquorRuleInfo, day));
        laoczLiquorAlarmHistory.setCreateTime(null);
        laoczLiquorAlarmHistory.setUpdateTime(null);
        return laoczLiquorAlarmHistory;
    }

}
