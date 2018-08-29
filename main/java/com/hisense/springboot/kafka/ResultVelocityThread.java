package com.hisense.springboot.kafka;

import com.hisense.springboot.mapper.MidCpnRouteVelocityMapper;
import com.hisense.springboot.model.MidCpnRouteVelocity;
import com.hisense.springboot.model.TenMinRouteAvgVelocity;
import com.hisense.springboot.service.MidTableService;
import com.hisense.springboot.util.SpringUtil;

import java.util.ArrayList;
import java.util.List;

public class ResultVelocityThread implements Runnable {

    public ArrayList<TenMinRouteAvgVelocity> list;

    MidTableService midTableService;

    public ResultVelocityThread(ArrayList<TenMinRouteAvgVelocity> list) {
        this.list = (ArrayList<TenMinRouteAvgVelocity>)list.clone();

    }

    @Override
    public void run() {

        midTableService = (MidTableService) SpringUtil.getBean("midTableServiceImpl");
        midTableService.insertAvgVelocityTableBatch(list);
    }

}
