    package com.rexel.bpm.convert.task;

import com.rexel.common.utils.DateUtils;
import com.rexel.bpm.domain.task.activity.BpmActivityRespVO;
import org.flowable.engine.history.HistoricActivityInstance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * BPM 活动 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface BpmActivityConvert {

    BpmActivityConvert INSTANCE = Mappers.getMapper(BpmActivityConvert.class);

    List<BpmActivityRespVO> convertList(List<HistoricActivityInstance> list);

    @Mappings({
            @Mapping(source = "activityId", target = "key"),
            @Mapping(source = "activityType", target = "type"),
            @Mapping(target = "startTime", qualifiedByName = "mapDate"),
            @Mapping(target = "endTime", qualifiedByName = "mapDate")
    })
    BpmActivityRespVO convert(HistoricActivityInstance bean);

    @Named("mapDate")
    default Long mapDate(Date date) {
        return date != null ? date.getTime() : null;
    }
}
