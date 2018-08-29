package com.hisense.springboot.model;

import java.util.Date;

public class MidCpnInternalModel {
    private String id;

    private String startDevice;

    private String endDevice;

    private Long internalTime;

    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getStartDevice() {
        return startDevice;
    }

    public void setStartDevice(String startDevice) {
        this.startDevice = startDevice == null ? null : startDevice.trim();
    }

    public String getEndDevice() {
        return endDevice;
    }

    public void setEndDevice(String endDevice) {
        this.endDevice = endDevice == null ? null : endDevice.trim();
    }

    public Long getInternalTime() {
        return internalTime;
    }

    public void setInternalTime(Long internalTime) {
        this.internalTime = internalTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}