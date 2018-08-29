package com.hisense.springboot.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class VehicleInfo implements Serializable {

    private static final long serialVersionUID = -8366929034564774130L;

    String deviceId;

    String vehicleNo;

    String cpDate;

    String vehicleColor;
}
