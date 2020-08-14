package com.topsci.edgexfoundry;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by zlli on 2020/5/28.
 * Desc:
 * _        _     _
 * | |      | |   | |
 * ___| |_ __ _| |__ | | ___
 * / __| __/ _` | '_ \| |/ _ \
 * \__ \ || (_| | |_) | |  __/
 * |___/\__\__,_|_.__/|_|\___|
 */
public class DeviceCache {
    private volatile static DeviceCache singleton;

    private Map<String,String> cache = new Hashtable<>();

    private DeviceCache (){}
    public static DeviceCache getSingleton() {
        if (singleton == null) {
            synchronized (DeviceCache.class) {
                if (singleton == null) {
                    singleton = new DeviceCache();
                }
            }
        }
        return singleton;
    }

    public Map<String,String> cache(){
        return cache;
    }

}
