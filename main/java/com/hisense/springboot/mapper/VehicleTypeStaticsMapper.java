package com.hisense.springboot.mapper;

import com.hisense.springboot.model.VehicleTypeStatics;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface VehicleTypeStaticsMapper {

    int deleteByPrimaryKey(Long cpnId);

    int insert(VehicleTypeStatics record);

    VehicleTypeStatics selectByPrimaryKey(Long cpnId);

    List<VehicleTypeStatics> selectAll();

    int updateByPrimaryKey(VehicleTypeStatics record);
}