package com.hisense.springboot.service;

import com.google.common.collect.Lists;
import com.hisense.springboot.model.VehicleInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * key由no+color
 */
@Service
public class CacheVehicleInfoServiceImpl implements CacheVehicleInfoService{

    private Logger logger = LoggerFactory.getLogger(CacheVehicleInfoServiceImpl.class);
    /**
     * 缓存的key
     */
    public static final String VEHICLE_ALL_KEY   = "\"vehicle_info\"";

    /**
     * value属性表示使用哪个缓存策略，缓存策略在ehcache.xml
     */
    public static final String DEMO_CACHE_NAME = "demo";

    private static Map<String, VehicleInfo> data = new ConcurrentHashMap<String, VehicleInfo>();

    @CacheEvict(value = DEMO_CACHE_NAME,key = VEHICLE_ALL_KEY)
    @Override
    public void save(VehicleInfo vehicleInfo){
        data.put(vehicleInfo.getVehicleNo()+'_'+vehicleInfo.getVehicleColor()
                , vehicleInfo);
    }

    @Cacheable(value = DEMO_CACHE_NAME, key = "#vehicleInfo.vehicleNo + '_' + #vehicleInfo.vehicleColor")
    @Override
    public VehicleInfo findById(VehicleInfo vehicleInfo){
        //return data.get(vehicleInfo.getVehicleNo()+'_'+vehicleInfo.getVehicleColor());
        return vehicleInfo;
    }

    @Cacheable(value = DEMO_CACHE_NAME, key = VEHICLE_ALL_KEY)
    @Override
    public List<VehicleInfo> findAll(){
        System.out.println(data.keySet());
        return Lists.newArrayList (data.values ());
    }

    @Override
    @CachePut(value = DEMO_CACHE_NAME,key = "#vehicleInfo.vehicleNo + '_' + #vehicleInfo.vehicleColor")
    //@CacheEvict(value = DEMO_CACHE_NAME, key = VEHICLE_ALL_KEY)
    public VehicleInfo update(VehicleInfo vehicleInfo){
        logger.info("更新vehicleinfo" + vehicleInfo);
                data.put (vehicleInfo.getVehicleNo()+'_'+vehicleInfo.getVehicleColor(), vehicleInfo);
                return vehicleInfo;
    }

    @CacheEvict(value = DEMO_CACHE_NAME)
    @Override
    public void delete(VehicleInfo vehicleInfo){
        logger.debug("delete cache which name is" + vehicleInfo.getVehicleNo()+'_'+vehicleInfo.getVehicleColor());
        data.remove(vehicleInfo.getVehicleNo()+'_'+vehicleInfo.getVehicleColor());
    }

}
