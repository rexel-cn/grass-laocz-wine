package com.rexel.quartz.service.impl;

import com.rexel.quartz.service.IEquipmentSchedulingService;
import com.rexel.system.service.IEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName IEquipmentSchedulingServiceImpl
 * @Description
 * @Author 孟开通
 * @Date 2022/12/20 15:39
 **/
@Service
public class IEquipmentSchedulingServiceImpl implements IEquipmentSchedulingService {

    @Autowired
    private IEquipmentService iEquipmentService;

    @Override
    public void deleteObsoleteDevices() {
        iEquipmentService.deleteObsoleteDevices();
    }
}
