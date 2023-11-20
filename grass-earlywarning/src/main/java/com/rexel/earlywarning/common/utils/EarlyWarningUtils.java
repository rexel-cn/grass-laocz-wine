package com.rexel.earlywarning.common.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rexel.common.utils.StringUtils;
import com.rexel.earlywarning.common.constants.EarlyConstants;
import com.rexel.earlywarning.domain.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 预警规则通用处理类
 *
 * @author admin
 * @date 2022-02-8
 */
public class EarlyWarningUtils {
    /**
     * rulesId转换为JobId
     *
     * @param rulesId rulesId
     * @param tenantId tenantId
     * @return 结果
     */
    public static String rulesIdToJobId(Long rulesId, String tenantId) {
        return rulesId + EarlyConstants.UNDERLINE + tenantId;
    }

    /**
     * JobId转换为rulesId
     *
     * @param jobId jobId
     * @return 结果
     */
    public static Long jobIdToRulesId(String jobId) {
        String[] splits = jobId.split(EarlyConstants.UNDERLINE);
        return Long.parseLong(splits[0]);
    }

    /**
     * 从关系表达式中获取索引列表.
     *
     * @param relation relation
     * @return 结果
     */
    public static List<String> getIndexList(String relation) {
        for (String limit : EarlyConstants.LIMITS) {
            relation = relation.replace(limit, EarlyConstants.SPACE);
        }
        String[] splits = relation.split(EarlyConstants.SPACE);
        List<String> result = new ArrayList<>();
        for (String split : splits) {
            if (StringUtils.isNotEmpty(split) && !result.contains(split)) {
                result.add(split);
            }
        }
        return result;
    }

    /**
     * 转换状态名称
     *
     * @param status status
     * @return 结果
     */
    public static String toStatusName(String status) {
        if (EarlyConstants.J_STATUS_ID_START.equals(status)) {
            return EarlyConstants.J_STATUS_NAME_START;
        } else {
            return EarlyConstants.J_STATUS_NAME_STOP;
        }
    }

    /**
     * 字符串是否以数字开头
     *
     * @param str str
     * @return 结果
     */
    public static boolean checkIndexPrefix(String str) {
        Pattern pattern = Pattern.compile(EarlyConstants.PREFIX);
        Matcher matcher = pattern.matcher(str.charAt(0)+"");
        return !matcher.matches();
    }

    /**
     * 生成下发JSON（累加）
     *
     * @param earlyWarning earlyWarning
     * @param triggerList triggerList
     * @param judgeList judgeList
     * @return 结果
     */
    public static JSONObject makeSendDownJsonAcc(
            GrassEarlyWarning earlyWarning,
            List<GrassEarlyWarningTrigger> triggerList, List<GrassEarlyWarningJudge> judgeList) {
        JSONArray triggerArray = new JSONArray();
        List<String> triggerIndexList = new ArrayList<>();
        triggerList.forEach(trigger -> {
            String index = trigger.getTriggerIndex();
            JSONObject triggerJson = new JSONObject();
            triggerJson.put(EarlyConstants.SUBMIT_TID, index);
            triggerJson.put(EarlyConstants.SUBMIT_POINT, trigger.getPointId());
            triggerJson.put(EarlyConstants.SUBMIT_VALUE, trigger.getPointValue());
            triggerJson.put(EarlyConstants.SUBMIT_JUDGE_FLAG, trigger.getJudge());
            triggerArray.add(triggerJson);
            triggerIndexList.add(index);
        });

        JSONArray judgeArray = new JSONArray();
        List<String> judgeIndexList = new ArrayList<>();
        judgeList.forEach(judge -> {
            String index = judge.getJudgeIndex();
            JSONObject judgeJson = new JSONObject();
            judgeJson.put(EarlyConstants.SUBMIT_TID, index);
            judgeJson.put(EarlyConstants.SUBMIT_POINT, judge.getPointId());
            judgeJson.put(EarlyConstants.SUBMIT_VALUE, judge.getPointValue());
            judgeJson.put(EarlyConstants.SUBMIT_JUDGE_FLAG, judge.getJudge());
            judgeArray.add(judgeJson);
            judgeIndexList.add(index);
        });

        JSONObject jsonObject = new JSONObject();
        // 任务ID
        jsonObject.put(EarlyConstants.SUBMIT_JOB_ID,
                rulesIdToJobId(earlyWarning.getRulesId(), earlyWarning.getTenantId()));
        // 触发条件
        jsonObject.put(EarlyConstants.SUBMIT_TRIGGERS, triggerArray);
        // 触发条件关系
        jsonObject.put(EarlyConstants.SUBMIT_TRIGGER_CMD,
                makeRelation(earlyWarning.getTriggerRelation(), triggerIndexList));
        // 触发条件采集频率
        jsonObject.put(EarlyConstants.SUBMIT_TRIGGER_A_F, earlyWarning.getTriggerFrequency());
        // 单次限制时间
        jsonObject.put(EarlyConstants.SUBMIT_SINGLE_C_TIME, earlyWarning.getAddUpOnceMax());
        // 采集频率
        jsonObject.put(EarlyConstants.SUBMIT_A_F, earlyWarning.getJudgeFrequency());
        // 达到N次后进行预警
        jsonObject.put(EarlyConstants.SUBMIT_EXECUTIONS, earlyWarning.getAddUpCount());
        // 任务的死亡时间
        jsonObject.put(EarlyConstants.SUBMIT_TASK_EE_TIME, earlyWarning.getAddUpDuration());
        // 判断条件
        jsonObject.put(EarlyConstants.SUBMIT_JUDGES, judgeArray);
        // 判断条件关系
        jsonObject.put(EarlyConstants.SUBMIT_TASK_COMMAND,
                makeRelation(earlyWarning.getJudgeRelation(), judgeIndexList));
        return jsonObject;
    }

    /**
     * 生成下发JSON（连续）
     *
     * @param earlyWarning earlyWarning
     * @param triggerList triggerList
     * @param judgeList judgeList
     * @return 结果
     */
    public static JSONObject makeSendDownJsonScc(
            GrassEarlyWarning earlyWarning,
            List<GrassEarlyWarningTrigger> triggerList, List<GrassEarlyWarningJudge> judgeList) {
        JSONArray triggerArray = new JSONArray();
        List<String> triggerIndexList = new ArrayList<>();
        triggerList.forEach(trigger -> {
            String index = trigger.getTriggerIndex();
            JSONObject triggerJson = new JSONObject();
            triggerJson.put(EarlyConstants.SUBMIT_TID, index);
            triggerJson.put(EarlyConstants.SUBMIT_POINT, trigger.getPointId());
            triggerJson.put(EarlyConstants.SUBMIT_VALUE, trigger.getPointValue());
            triggerJson.put(EarlyConstants.SUBMIT_JUDGE_FLAG, trigger.getJudge());
            triggerArray.add(triggerJson);
            triggerIndexList.add(index);
        });

        JSONArray judgeArray = new JSONArray();
        List<String> judgeIndexList = new ArrayList<>();
        judgeList.forEach(judge -> {
            String index = judge.getJudgeIndex();
            JSONObject judgeJson = new JSONObject();
            judgeJson.put(EarlyConstants.SUBMIT_TID, index);
            judgeJson.put(EarlyConstants.SUBMIT_POINT, judge.getPointId());
            judgeJson.put(EarlyConstants.SUBMIT_VALUE, judge.getPointValue());
            judgeJson.put(EarlyConstants.SUBMIT_JUDGE_FLAG, judge.getJudge());
            judgeArray.add(judgeJson);
            judgeIndexList.add(index);
        });


        JSONObject jsonObject = new JSONObject();
        // 任务ID
        jsonObject.put(EarlyConstants.SUBMIT_JOB_ID,
                rulesIdToJobId(earlyWarning.getRulesId(), earlyWarning.getTenantId()));
        // 触发条件
        jsonObject.put(EarlyConstants.SUBMIT_TRIGGERS, triggerArray);
        // 触发条件关系
        jsonObject.put(EarlyConstants.SUBMIT_TRIGGER_CMD,
                makeRelation(earlyWarning.getTriggerRelation(), triggerIndexList));
        // 触发条件采集频率
        jsonObject.put(EarlyConstants.SUBMIT_TRIGGER_A_F, earlyWarning.getTriggerFrequency());
        // 执行次数
        jsonObject.put(EarlyConstants.SUBMIT_EXECUTIONS, earlyWarning.getContinuousCount());
        // 单次频率
        jsonObject.put(EarlyConstants.SUBMIT_A_F, earlyWarning.getJudgeFrequency());
        // 劣化百分比
        jsonObject.put(EarlyConstants.SUBMIT_INCREMENT, earlyWarning.getContinuousIncrease());
        // 判断条件
        jsonObject.put(EarlyConstants.SUBMIT_JUDGES, judgeArray);
        // 判断条件关系
        jsonObject.put(EarlyConstants.SUBMIT_TASK_COMMAND,
                makeRelation(earlyWarning.getJudgeRelation(), judgeIndexList));
        return jsonObject;
    }

    /**
     * 生成触发条件关系表达式
     *
     * @param relation relation
     * @param indexList indexList
     * @return 结果
     */
    private static String makeRelation(String relation, List<String> indexList) {
        // 如果用户指定了关系
        if (StringUtils.isNotEmpty(relation)) {
            return relation;
        }
        // 用户未指定关系，默认所有条件为and关系
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indexList.size(); i++) {
            String index = indexList.get(i);
            if (i > 0) {
                sb.append(EarlyConstants.AND);
            }
            sb.append(index);
        }
        return sb.toString();
    }
}