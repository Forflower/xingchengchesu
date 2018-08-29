package com.hisense.springboot;

import redis.clients.jedis.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Test {
    private static JedisCluster jedis;
    static {
        // 添加集群的服务节点Set集合
        Set<HostAndPort> hostAndPortsSet = new HashSet<HostAndPort>();
        // 添加节点
        hostAndPortsSet.add(new HostAndPort("192.168.56.180", 7777));
        hostAndPortsSet.add(new HostAndPort("192.168.56.180", 8888));
        hostAndPortsSet.add(new HostAndPort("192.168.56.181", 7777));
        hostAndPortsSet.add(new HostAndPort("192.168.56.181", 8888));
        hostAndPortsSet.add(new HostAndPort("192.168.56.182", 7777));
        hostAndPortsSet.add(new HostAndPort("192.168.56.182", 8888));

        // Jedis连接池配置
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲连接数, 默认8个
        jedisPoolConfig.setMaxIdle(100);
        // 最大连接数, 默认8个
        jedisPoolConfig.setMaxTotal(500);
        //最小空闲连接数, 默认0
        jedisPoolConfig.setMinIdle(0);
        // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        jedisPoolConfig.setMaxWaitMillis(2000); // 设置2秒
        //对拿到的connection进行validateObject校验
        jedisPoolConfig.setTestOnBorrow(true);
        jedis = new JedisCluster(hostAndPortsSet, jedisPoolConfig);
    }

    public static void main(String[] args) throws Exception {

        Map<String, String> data = new HashMap<String, String>();
//        redis.flushDB();
        //hmset
        long start = System.currentTimeMillis();
        //直接hmset
        for (int i = 0; i < 10000; i++) {
            data.clear();
            data.put("k_" + i, "v_" + i);
            jedis.hmset("key_" + i, data);
        }
        long end = System.currentTimeMillis();
        System.out.println("dbsize:[" + jedis.dbSize() + "] .. ");
        System.out.println("hmset without pipeline used [" + (end - start) / 1000 + "] seconds ..");
//        redis.select(8);
//        redis.flushDB();
        //使用pipeline hmset
        Pipeline p ;//= jedis.pipelined();
        start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            data.clear();
            data.put("k_" + i, "v_" + i);
//            p.hmset("key_" + i, data);
        }
//        p.sync();
        end = System.currentTimeMillis();
//        System.out.println("dbsize:[" + redis.dbSize() + "] .. ");
        System.out.println("hmset with pipeline used [" + (end - start) / 1000 + "] seconds ..");

//        //hmget
//        Set keys = redis.keys("*");
//        //直接使用Jedis hgetall
//        start = System.currentTimeMillis();
//        Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
//        for (String key : keys) {
//            result.put(key, redis.hgetAll(key));
//        }
//        end = System.currentTimeMillis();
//        System.out.println("result size:[" + result.size() + "] ..");
//        System.out.println("hgetAll without pipeline used [" + (end - start) / 1000 + "] seconds ..");
//
//        //使用pipeline hgetall
//        Map<String, Response<Map<String, String>>> responses = new HashMap<String, Response<Map<String, String>>>(keys.size());
//        result.clear();
//        start = System.currentTimeMillis();
//        for (String key : keys) {
//            responses.put(key, p.hgetAll(key));
//        }
//        p.sync();
//        for (String k : responses.keySet()) {
//            result.put(k, responses.get(k).get());
//        }
//        end = System.currentTimeMillis();
//        System.out.println("result size:[" + result.size() + "] ..");
//        System.out.println("hgetAll with pipeline used [" + (end - start) / 1000 + "] seconds ..");
//
//        redis.disconnect();

    }


}

