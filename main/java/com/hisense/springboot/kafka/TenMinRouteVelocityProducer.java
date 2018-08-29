package com.hisense.springboot.kafka;

import com.hisense.springboot.model.TenMinRouteAvgVelocity;

import java.util.List;

public class TenMinRouteVelocityProducer implements Runnable {

    public final static String TOPIC = "TOPIC.TEST";

    public List<TenMinRouteAvgVelocity> avgList;

    public TenMinRouteVelocityProducer(List<TenMinRouteAvgVelocity> modelList) {
        this.avgList = modelList;

    }

    @Override
    public void run() {

        KafkaProducerSingleton kafkaProducerSingleton = KafkaProducerSingleton.getInstance();

        kafkaProducerSingleton.init(TOPIC, 3);

//        System.out.println("十分钟结果集---当前线程:" + Thread.currentThread().getName() + ",获取的kafka实例:" + kafkaProducerSingleton);

        for (TenMinRouteAvgVelocity model : avgList) {

            kafkaProducerSingleton.sendKafkaMessage(model.toTranferString());
        }
    }

}
