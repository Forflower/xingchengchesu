package com.hisense.springboot.mapper;

import com.hisense.springboot.model.TenMinRouteAvgVelocity;
import java.util.List;

public interface TenMinRouteAvgVelocityMapper {
    int insert(TenMinRouteAvgVelocity record);

    List<TenMinRouteAvgVelocity> selectAll();

    int insertBath(List<TenMinRouteAvgVelocity> recordList );
}