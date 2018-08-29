package com.hisense.springboot.controller;

import com.hisense.springboot.mapper.VehicleTypeStaticsMapper;
import com.hisense.springboot.model.VehicleTypeStatics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoController {

    @Autowired
    private VehicleTypeStaticsMapper vehicleTypeStaticsMapper;

    @RequestMapping(value = "/demo")
    public String hello() {

        List<VehicleTypeStatics> list = vehicleTypeStaticsMapper.selectAll();
        System.out.println("all list size is" + list.size());
        return "Hello , Spring Boot";
    }
}

