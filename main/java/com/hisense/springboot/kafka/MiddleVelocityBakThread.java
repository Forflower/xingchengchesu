package com.hisense.springboot.kafka;

import com.hisense.springboot.mapper.MidCpnRouteVelocityMapper;
import com.hisense.springboot.model.MidCpnRouteVelocity;
import com.hisense.springboot.model.VehicleInfo;
import com.hisense.springboot.util.RedisUtil;
import com.hisense.springboot.util.RegexUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import sun.misc.DoubleConsts;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MiddleVelocityBakThread extends Thread {

    private ConsumerRecords<String, String> records;

    private MidCpnRouteVelocityMapper midCpnRouteVelocityMapper;
    private RedisUtil redisUtil;
    private static final String DEVICE_SPLITE = "_";

    private static final String VALUE_SPLITE = ",";

    private static final String CACHE_PREFIX = "cache_";

    public MiddleVelocityBakThread(ConsumerRecords<String, String> records, MidCpnRouteVelocityMapper midCpnRouteVelocityMapper
            , RedisUtil redisUtil) {
        this.records = records;
        this.midCpnRouteVelocityMapper = midCpnRouteVelocityMapper;
        this.redisUtil = redisUtil;
    }

    private static final Logger logger = LoggerFactory.getLogger("errorLog");
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void run() {
//        int count = 0;
        int i = 0;
        for (ConsumerRecord<String, String> record : records) {
            String keystr = "";
            //判断是否未识别
            VehicleInfo vehicleInfo = new VehicleInfo();
            String[] item = record.value().split(",");

            if (item.length < 23) {
                logger.info("record lost " + item);
                continue;
            }
            if (RegexUtils.isVehicleNotRecognized(item[5])) {
                continue;
            }
            vehicleInfo.setVehicleNo(item[5]);
            vehicleInfo.setVehicleColor(item[8]);
            vehicleInfo.setDeviceId(item[1]);
            try {
                vehicleInfo.setCpDate(simpleDateFormat.format(simpleDateFormat.parse(item[21])));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            String key = item[5] + '_' + item[8];
            //取出 设备id 时间
            String valueStr = (String) redisUtil.get(CACHE_PREFIX + key);
            Double roadLength = DoubleConsts.NaN;

            if (!ObjectUtils.isEmpty(valueStr)) {
                //varray[0] deviceId  varray[1] time
                String[] varray = valueStr.split(",");

                long date1 = 0;
                long date2 = 0;
                String rkey = "";
                String rvalue = "";
                String[] rvalues = null;
                String T2Time = "";
                try {
                    date1 = simpleDateFormat.parse(vehicleInfo.getCpDate()).getTime();
                    date2 = simpleDateFormat.parse(varray[1]).getTime();

                    if (date1 > date2) {
                        //得到路段id 路段长度
                        rkey = varray[0] + DEVICE_SPLITE + vehicleInfo.getDeviceId();
                        T2Time = vehicleInfo.getCpDate();
                    } else {

                        rkey = vehicleInfo.getDeviceId() + DEVICE_SPLITE + varray[0];
                        T2Time = varray[1];
                    }
                    rvalue = (String) redisUtil.get(rkey);
//                    count3++;
                    keystr += rkey + "@" + vehicleInfo.getVehicleNo() + "$$";
                    if (StringUtils.isEmpty(rvalue)) {
                        String newvalue = vehicleInfo.getDeviceId() + VALUE_SPLITE + vehicleInfo.getCpDate();
                        redisUtil.set(CACHE_PREFIX + key, newvalue);
                        redisUtil.expire(CACHE_PREFIX + key, 3600);
                        continue;
                    }
//                    count4++;
                    rvalues = rvalue.split(VALUE_SPLITE);
                    //找出路段长度 rvalues[0] roadId rvalues[1]长度
                    if (StringUtils.isNotEmpty(rvalues[1])) {
                        Double length = Double.valueOf(rvalues[1]);
                        roadLength = (length == null ? 0 : length);
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long t = Math.abs(date1 - date2);
//                            System.out.println("时间间隔为" + t);
                //入库
                MidCpnRouteVelocity midCpnRouteVelocity = new MidCpnRouteVelocity();
                //1.车牌号
                midCpnRouteVelocity.setPlateNo(vehicleInfo.getVehicleNo());
                //2.颜色id
                midCpnRouteVelocity.setPlateColorId(Short.valueOf(vehicleInfo.getVehicleColor()));
                //3.路段id  根据设备关联 ，Math.random()* 10)
                midCpnRouteVelocity.setRouteId(rvalues[0]);
                //4.t2时间
                try {
                    midCpnRouteVelocity.setT2Time(simpleDateFormat.parse(T2Time));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //5.时间差
                midCpnRouteVelocity.setInternalTime(BigDecimal.valueOf((double) t / 1000));
                //6.计算平均速度 m/s
                midCpnRouteVelocity.setVelocity(BigDecimal.valueOf(roadLength * 3600000 / t));

//                        logger.info((count4++)+"----- " + midCpnRouteVelocity.toString() );
                if (vehicleInfo.getDeviceId().equals("3014-722031") || vehicleInfo.getDeviceId().equals("3014-722029")) {

//                    logger.info((count4++) + "----- " + midCpnRouteVelocity.toString());
                }
                midCpnRouteVelocityMapper.insert(midCpnRouteVelocity);

                new RouteVelocityProducer(midCpnRouteVelocity).run();

                String brvalue = vehicleInfo.getDeviceId() + VALUE_SPLITE + T2Time;
                //更新信息
                redisUtil.set(CACHE_PREFIX + key, brvalue);
                redisUtil.expire(CACHE_PREFIX + key, 1200);
//                            redisUtil.hset("vehicle_hm", rkey, rvalue);

            } else {
//                count5++;
                //新增
                String newvalue = vehicleInfo.getDeviceId() + VALUE_SPLITE + vehicleInfo.getCpDate();
                redisUtil.set(CACHE_PREFIX + key, newvalue);
                redisUtil.expire(CACHE_PREFIX + key, 3600);
//                    redisUtil.hset("vehicle_hm", key, newvalue);
            }
            long edate = Calendar.getInstance().getTime().getTime();
       }
    }

}
