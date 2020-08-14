package com.topsci.task;

import com.topsci.service.DeviceInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by zlli on 2019/9/25.
 * Desc:
 * _        _     _
 * | |      | |   | |
 * ___| |_ __ _| |__ | | ___
 * / __| __/ _` | '_ \| |/ _ \
 * \__ \ || (_| | |_) | |  __/
 * |___/\__\__,_|_.__/|_|\___|
 */

/**
 * cron: 通过 Cron 表达式控制执行
 */
@Component
public class AutoLoadDdCacheJob {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private DeviceInfoService deviceService;

    @Scheduled(cron = "${AUTO_LOAD_DS_CACHE}")
    public void  cron() {
        deviceService.cacheDeviceInfo();
        deviceService.profileInfo();
    }
}
