package com.hisense.springboot.model;

import java.math.BigDecimal;

public class VehicleAmountOnRoad {
    private BigDecimal roadId;

    private BigDecimal amount;

    private String capTime;

    public BigDecimal getRoadId() {
        return roadId;
    }

    public void setRoadId(BigDecimal roadId) {
        this.roadId = roadId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCapTime() {
        return capTime;
    }

    public void setCapTime(String capTime) {
        this.capTime = capTime == null ? null : capTime.trim();
    }
}