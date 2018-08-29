package com.hisense.springboot.model;

import java.math.BigDecimal;
import java.util.Date;

public class TenMinRouteAvgVelocity {
    private String id;

    private String routeId;

    private String calTime;

    private BigDecimal velocity;

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "TenMinRouteAvgVelocity[id = " + id +
                "，routeId = " + routeId +
                "，calTime = " + calTime +
                "，velocity = " + velocity +"]"
                ;
    }

    public String toTranferString() {
        return id +
                "," + routeId +
                "," + calTime +
                "," + velocity.toString()
                ;
    }
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getCalTime() {
        return calTime;
    }

    public void setCalTime(String calTime) {
        this.calTime = calTime;
    }

    public BigDecimal getVelocity() {
        return velocity;
    }

    public void setVelocity(BigDecimal velocity) {
        this.velocity = velocity;
    }
}