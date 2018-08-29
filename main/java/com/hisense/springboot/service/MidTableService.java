package com.hisense.springboot.service;

import com.hisense.springboot.model.*;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MidTableService {

    public int addMidInternalTable(MidCpnInternalModel model);

    public int addMidRouteVelocityTable(MidCpnRouteVelocity model);

    public List<TenMinRouteAvgVelocity> queryMidRouteVelocityTable(Duration duration);

    public List<RouteList> queryMidRouteVelocityByGroup(Duration duration);

    public int addTenMinRouteAvgVelocityTable(TenMinRouteAvgVelocity model);

    int insertAvgVelocityTableBatch(List<TenMinRouteAvgVelocity> vlist);

    public int addVehicleAmountOnRoad(VehicleAmountOnRoad model);

    List<VehicleAmountOnRoad> queryVehicleAmountOnRoad(Duration duration);

    public int addBatchVehicleAmountOnRoad(List<VehicleAmountOnRoad> vlist);
}
