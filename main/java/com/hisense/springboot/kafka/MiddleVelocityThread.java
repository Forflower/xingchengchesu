package com.hisense.springboot.kafka;

import com.hisense.springboot.mapper.MidCpnRouteVelocityMapper;
import com.hisense.springboot.model.MidCpnRouteVelocity;
import com.hisense.springboot.model.TenMinRouteAvgVelocity;
import com.hisense.springboot.service.MidTableService;
import com.hisense.springboot.util.SpringUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class MiddleVelocityThread implements Runnable {

    public MidCpnRouteVelocity model;

    MidCpnRouteVelocityMapper midCpnRouteVelocityMapper;

    public MiddleVelocityThread(MidCpnRouteVelocity model,MidCpnRouteVelocityMapper midCpnRouteVelocityMapper) {
        this.model = model;
        this.midCpnRouteVelocityMapper = midCpnRouteVelocityMapper;
    }

    @Override
    public void run() {

       // midCpnRouteVelocityMapper = (MidCpnRouteVelocityMapper)SpringUtil.getBean("midCpnRouteVelocityMapper");
        midCpnRouteVelocityMapper.insert(model);
    }

}
