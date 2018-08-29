package com.hisense.springboot.service;

import com.hisense.springboot.kafka.MiddleVelocityThread;
import com.hisense.springboot.mapper.MidCpnInternalModelMapper;
import com.hisense.springboot.mapper.MidCpnRouteVelocityMapper;
import com.hisense.springboot.mapper.TenMinRouteAvgVelocityMapper;
import com.hisense.springboot.mapper.VehicleAmountOnRoadMapper;
import com.hisense.springboot.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class MidTableServiceImpl implements MidTableService{

    @Autowired
    private MidCpnInternalModelMapper midCpnInternalModelMapper;

    @Autowired
    private MidCpnRouteVelocityMapper midCpnRouteVelocityMapper;

    @Autowired
    private TenMinRouteAvgVelocityMapper tenMinRouteAvgVelocityMapper;

    @Autowired
    private VehicleAmountOnRoadMapper vehicleAmountOnRoadMapper;


    private static List<MidCpnRouteVelocity> mlist = new ArrayList<MidCpnRouteVelocity>();


    @Override
    public int addMidInternalTable(MidCpnInternalModel model) {
        return midCpnInternalModelMapper.insert(model);
    }

    @Override
    public int addMidRouteVelocityTable(MidCpnRouteVelocity model) {
//        executorService.execute(new MiddleVelocityThread(model));
        return 1;
    }

    @Override
    public List<TenMinRouteAvgVelocity> queryMidRouteVelocityTable(Duration duration) {
        return midCpnRouteVelocityMapper.calAvgVelocity(duration);
    }

    @Override
    public int addTenMinRouteAvgVelocityTable(TenMinRouteAvgVelocity model) {
        return tenMinRouteAvgVelocityMapper.insert(model);
    }

    @Override
    public int insertAvgVelocityTableBatch(List<TenMinRouteAvgVelocity> modellist) {
        return tenMinRouteAvgVelocityMapper.insertBath(modellist);
    }
    @Override
    public List<RouteList> queryMidRouteVelocityByGroup(Duration duration) {
        return midCpnRouteVelocityMapper.selectVelocityByGroup(duration);
    }

    @Override
    public int addVehicleAmountOnRoad(VehicleAmountOnRoad model) {
        return vehicleAmountOnRoadMapper.insert(model);
    }

    @Override
    public List<VehicleAmountOnRoad> queryVehicleAmountOnRoad(Duration duration) {
        return vehicleAmountOnRoadMapper.selectAll();
    }

    @Override
    public int addBatchVehicleAmountOnRoad(List<VehicleAmountOnRoad> vlist) {
        return vehicleAmountOnRoadMapper.insertBatch(vlist);
    }
}
