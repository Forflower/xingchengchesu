package com.hisense.springboot.mapper;

import com.hisense.springboot.model.*;

import java.util.List;

public interface MidCpnRouteVelocityMapper {

    int insert(MidCpnRouteVelocity record);

    List<TenMinRouteAvgVelocity> calAvgVelocity(Duration duration);

    List<MidCpnRouteVelocity> selectAll();

    List<RouteList> selectVelocityByGroup(Duration duration);
}
