package com.hisense.springboot.kafka;

import com.hisense.springboot.model.MidCpnInternalModel;
import com.hisense.springboot.model.MidCpnRouteVelocity;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RouteVelocityProducer implements Runnable {

    public final static String TOPIC = "TOPIC.TEST";


    public MidCpnRouteVelocity model;

    public RouteVelocityProducer(MidCpnRouteVelocity model) {
        this.model = model;

    }

    public void produce() {

    }

    @Override
    public void run() {
        KafkaProducerSingleton kafkaProducerSingleton = KafkaProducerSingleton.getInstance();

        kafkaProducerSingleton.init(TOPIC, 3);

        kafkaProducerSingleton.sendKafkaMessage(model.toTranferString());
    }
}
