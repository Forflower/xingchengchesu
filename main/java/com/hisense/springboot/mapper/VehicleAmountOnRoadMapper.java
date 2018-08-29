package com.hisense.springboot.mapper;

import com.hisense.springboot.model.VehicleAmountOnRoad;
import java.util.List;

public interface VehicleAmountOnRoadMapper {

    int insert(VehicleAmountOnRoad record);

    List<VehicleAmountOnRoad> selectAll();

    int insertBatch(List<VehicleAmountOnRoad> vlist);
}