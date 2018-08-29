package com.hisense.springboot.thread;

import com.hisense.springboot.kafka.RouteVelocityProducer;
import com.hisense.springboot.mapper.MidCpnRouteVelocityMapper;
import com.hisense.springboot.model.MidCpnRouteVelocity;
import com.hisense.springboot.model.VehicleInfo;
import com.hisense.springboot.service.CacheVehicleInfoService;
import com.hisense.springboot.util.ConstUtils;
import com.hisense.springboot.util.RedisUtil;
import com.hisense.springboot.util.RegexUtils;
import com.hisense.springboot.util.SpringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;
import sun.misc.DoubleConsts;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

@Import({CacheVehicleInfoService.class})
public class ConsumerThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerThread.class);

//    private CacheVehicleInfoService cacheVehicleInfoService;

    private ExecutorService executorService;

    private RedisTemplate redisTemplate;

    private static final String DEVICE_SPLITE = "_";

    private static final String VALUE_SPLITE = ",";

    private static final String CACHE_PREFIX = "tempdata_";


    @Override
    public void run() {
//        logger.info("test log");
        MidCpnRouteVelocityMapper midCpnRouteVelocityMapper = SpringUtil.getBean(MidCpnRouteVelocityMapper.class);
//        ehcacheManager = (EhCacheCacheManager) SpringUtil.getBean(EhCacheCacheManager.class);
//        CacheManager cacheManager = ehcacheManager.getCacheManager();
//        cacheManager.clearAll();
//        cacheVehicleInfoService = (CacheVehicleInfoService)SpringUtil.getBean(CacheVehicleInfoServiceImpl.class);
        redisTemplate = (RedisTemplate) SpringUtil.getBean("stringRedisTemplate");
        RedisUtil redisUtil = new RedisUtil();
        redisUtil.setRedisTemplate(redisTemplate);

        executorService = new ThreadPoolExecutor(1, 200, 1L, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(), new ThreadPoolExecutor.CallerRunsPolicy());

        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "20.2.15.25:9092,20.2.15.26:9092,20.2.15.29:9092");
        properties.put("group.id", "201808134");
        properties.put("enable.auto.commit", "false");  //定期提交offset
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("auto.offset.reset", "latest");
        properties.put("session.timeout.ms", "60000");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        //HIATMP.HISENSE.PASS.PASSINF.TEST, HIATMP.HISENSE.VOLUME.VOLUMEINF.TEST
        kafkaConsumer.subscribe(Arrays.asList("HIATMP.HISENSE.PASS.PASSINF"));  //HIATMP.HISENSE.PASS.PASSINF主题


        VehicleInfo vehicleInfo;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //批量提交数量
        final int minBatchSize = 1000;


        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            //System.out.println("records length is" + records.count());

            for (ConsumerRecord<String, String> record : records) {

                buffer.add(record);
                //判断是否未识别
                vehicleInfo = new VehicleInfo();

                long date1 = 0;

                long date2 = 0;

                String[] item = record.value().split(",");

                if (item.length < 23) {

                    logger.info("record lost " + item);

                    continue;

                }
                if (RegexUtils.isVehicleNotRecognized(item[5])) {

                    continue;
                }

                vehicleInfo.setVehicleNo(item[5]);

//                logger.info("raw:" + item[5]);

                vehicleInfo.setVehicleColor(item[8]);

                vehicleInfo.setDeviceId(item[1]);

                try {
                    vehicleInfo.setCpDate(simpleDateFormat.format(simpleDateFormat.parse(item[21])));

                } catch (ParseException e) {

                    e.printStackTrace();
                }

                String cacheKey = CACHE_PREFIX + item[5] + '_' + item[8];
                //设备id,时间
                String valueStr = (String) redisUtil.get(cacheKey);

                Double roadLength = DoubleConsts.NaN;

                if (!ObjectUtils.isEmpty(valueStr)) {
                    //varray[0] deviceId  varray[1] time
                    String[] varray = valueStr.split(",");

//                    if (varray[0].equals("3014-722031")) {
//                        logger.info("设备1："+ cacheKey + "时间:" + vehicleInfo.getCpDate());
//                    }
                    try {
                        date1 = simpleDateFormat.parse(vehicleInfo.getCpDate()).getTime();
                        date2 = simpleDateFormat.parse(varray[1]).getTime();

                    } catch (ParseException e) {
                        logger.error(e.getMessage());
                    }
                    String rkey;
                    String T2Time;
                    if (date1 > date2) {
                        //得到路段id 路段长度
                        rkey = varray[0] + DEVICE_SPLITE + vehicleInfo.getDeviceId();
                        T2Time = vehicleInfo.getCpDate();
                    } else {

                        rkey = vehicleInfo.getDeviceId() + DEVICE_SPLITE + varray[0];
                        T2Time = varray[1];
                    }
                    //deviceId_deviceId
                    //得到路段id 路段长度
                    String rvalue = (String) redisUtil.get(rkey);

                    if (StringUtils.isEmpty(rvalue)) {

//                        logger.info("没有此路段信息");

                        String newvalue = vehicleInfo.getDeviceId() + VALUE_SPLITE + vehicleInfo.getCpDate();

                        redisUtil.set(cacheKey, newvalue);

                        redisUtil.expire(cacheKey, 1200);

                        continue;
                    }
                    //路段长度 rvalues[0] roadId, rvalues[1]长度
//                    String[] rvalues = rvalue.split(VALUE_SPLITE);

                    String[] rvalues = rvalue.split(VALUE_SPLITE);
                    if (rvalues.length > 1) {

                        Double length = Double.valueOf(rvalues[1]);

                        roadLength = (length == null ? 0 : length);

                    } else {
                        continue;
                    }

                    long t = Math.abs(date1 - date2);
                    //入库
                    MidCpnRouteVelocity midCpnRouteVelocity = new MidCpnRouteVelocity();
                    //1.车牌号
                    midCpnRouteVelocity.setPlateNo(vehicleInfo.getVehicleNo());
                    //2.颜色id
                    midCpnRouteVelocity.setPlateColorId(Short.valueOf(vehicleInfo.getVehicleColor()));
                    //3.路段id  根据设备关联 ，Math.random()* 10)rvalues[0]
                    midCpnRouteVelocity.setRouteId(rvalues[0]);
                    //4.t2时间
                    try {
                        midCpnRouteVelocity.setT2Time(simpleDateFormat.parse(T2Time));
                    } catch (ParseException e) {
                        logger.error(e.getMessage());
                    }
                    //5.时间差
                    midCpnRouteVelocity.setInternalTime(BigDecimal.valueOf((double) t / 1000));
                    //6.计算平均速度 m/s
                    midCpnRouteVelocity.setVelocity(BigDecimal.valueOf(roadLength * 3600000 / t));

                    if (midCpnRouteVelocity.getVelocity().compareTo(ConstUtils.VELOCITY_LOWER) < 0 || midCpnRouteVelocity.getVelocity().compareTo(ConstUtils.VELOCITY_UPPER) > 0) {
                        continue;
                    }
                    List<Future<String>> resultList = new ArrayList<Future<String>>();
                    // 使用ExecutorService执行Callable类型的任务，并将结果保存在future变量中
                    Future<String> future = executorService.submit(new TaskWithResult(midCpnRouteVelocity, midCpnRouteVelocityMapper));
                    // 将任务执行结果存储到List中
                    resultList.add(future);
//                            executorService.shutdown();

                    // 遍历任务的结果
                    for (Future<String> fs : resultList) {
                        try {
                            fs.get(); // 打印各个线程（任务）执行的结果
                        } catch (Exception e) {
                            logger.error(e.getMessage());
//                                    e.printStackTrace();
                        }
                    }
                    //入库kafka
                    executorService.execute(new RouteVelocityProducer(midCpnRouteVelocity));
                    //deviceId, date
                    String uvalue = vehicleInfo.getDeviceId() + VALUE_SPLITE + vehicleInfo.getCpDate();
                    //更新信息
                    redisUtil.set(cacheKey, uvalue);
                    redisUtil.expire(cacheKey, 1200);


                } else {
                    //新增
                    String newvalue = vehicleInfo.getDeviceId() + VALUE_SPLITE + vehicleInfo.getCpDate();
                    redisUtil.set(cacheKey, newvalue);
                    redisUtil.expire(cacheKey, 1200);
                }
            }

            if (buffer.size() >= minBatchSize) {
                kafkaConsumer.commitSync();
                buffer.clear();
            }
        }
    }

}
