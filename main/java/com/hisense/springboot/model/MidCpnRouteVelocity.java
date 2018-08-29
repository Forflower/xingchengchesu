package com.hisense.springboot.model;

import java.math.BigDecimal;
import java.util.Date;

public class MidCpnRouteVelocity  implements  Comparable<MidCpnRouteVelocity>{
    private String id;

    private String plateNo;

    private Short plateColorId;

    private String routeId;

    private Date t2Time;

    private BigDecimal internalTime;

    private BigDecimal velocity;

    @Override
    public String toString() {
        return "MidCpnRouteVelocity[plateNo:" + plateNo +","+"plateColorId:" + plateColorId +"," + "routeId:" + routeId.toString()
                +","+"t2Time:" + t2Time +","+"internalTime:" + internalTime.toString() +","+"velocity:" + velocity.toString() + "]" ;
    }

    public String toTranferString() {
        return plateNo +","+ plateColorId +"," + routeId
                +"," + t2Time + "," + internalTime.toString() +","+ velocity.toString() ;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo == null ? null : plateNo.trim();
    }

    public Short getPlateColorId() {
        return plateColorId;
    }

    public void setPlateColorId(Short plateColorId) {
        this.plateColorId = plateColorId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public Date getT2Time() {
        return t2Time;
    }

    public void setT2Time(Date t2Time) {
        this.t2Time = t2Time == null ? null : t2Time;
    }

    public BigDecimal getInternalTime() {
        return internalTime;
    }

    public void setInternalTime(BigDecimal internalTime) {
        this.internalTime = internalTime;
    }

    public BigDecimal getVelocity() {
        return velocity;
    }

    public void setVelocity(BigDecimal velocity) {
        this.velocity = velocity;
    }

    @Override
    public int compareTo(MidCpnRouteVelocity o) {

        int i = this.getVelocity().compareTo(o.getVelocity());
        if (i == 0) {
            return (int)(this.t2Time.getTime()-o.getT2Time().getTime());
        }
        return i;
    }
}