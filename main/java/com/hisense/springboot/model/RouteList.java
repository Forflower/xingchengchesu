package com.hisense.springboot.model;

import java.util.ArrayList;

/**
 * mybatis返回结果
 */
public class RouteList {

    private String routeId;

    private ArrayList<MidCpnRouteVelocity> avgList;

    public String getAreaId() {
        return routeId;
    }

    public void setAreaId(String routeId) {
        this.routeId = routeId;
    }

    public ArrayList<MidCpnRouteVelocity> getList() {
        return avgList;
    }

    public void setList(ArrayList<MidCpnRouteVelocity> avgList) {
        this.avgList = avgList;
    }


}
