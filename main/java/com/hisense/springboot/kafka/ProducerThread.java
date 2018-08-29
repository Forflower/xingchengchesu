package com.hisense.springboot.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ProducerThread {

    public final static String TOPIC = "TOPIC.TEST";

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "20.2.15.25:9092,20.2.15.26:9092,20.2.15.29:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 100; i++) {
            producer.send(new ProducerRecord<String, String>(TOPIC, Integer.toString(i), Integer.toString(i)));
        }
        System.out.println("-------------推送数据结束----------------");
        producer.close();

    }
}
