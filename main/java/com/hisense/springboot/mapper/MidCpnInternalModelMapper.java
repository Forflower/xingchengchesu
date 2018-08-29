package com.hisense.springboot.mapper;

import com.hisense.springboot.model.MidCpnInternalModel;

import java.util.List;

public interface MidCpnInternalModelMapper {

    int insert(MidCpnInternalModel record);

    List<MidCpnInternalModel> selectAll();
}