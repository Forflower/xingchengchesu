package com.hisense.springboot.service;

import com.hisense.springboot.model.VehicleInfo;

import java.util.List;

/**
 * key由no+color
 */
public interface CacheVehicleInfoService {

    public void save(VehicleInfo vehicleInfo);

    public VehicleInfo findById(VehicleInfo vehicleInfo);

    public List<VehicleInfo> findAll();

    public VehicleInfo update(VehicleInfo vehicleInfo);

    public void delete(VehicleInfo vehicleInfo);

}
