package com.hisense.springboot.model;

import java.util.Date;

/**
 * Created by admin on 2018/6/13.
 */
public class Duration {

    public Date startTime;
    public Date endTime;
    public DurationTypeEnum typeEnum;

    public DurationTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(DurationTypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
