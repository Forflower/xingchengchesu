package com.hisense.springboot.job;

import com.hisense.springboot.kafka.ResultVelocityThread;
import com.hisense.springboot.kafka.RouteVelocityProducer;
import com.hisense.springboot.kafka.TenMinRouteVelocityProducer;
import com.hisense.springboot.mapper.TenMinRouteAvgVelocityMapper;
import com.hisense.springboot.model.Duration;
import com.hisense.springboot.model.MidCpnRouteVelocity;
import com.hisense.springboot.model.RouteList;
import com.hisense.springboot.model.TenMinRouteAvgVelocity;
import com.hisense.springboot.service.MidTableService;
import com.hisense.springboot.thread.ConsumerThread;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.util.CollectionUtils;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestQuartz extends QuartzJobBean {

    @Autowired
    private MidTableService midTableService;

    private final static ExecutorService executorService = new ThreadPoolExecutor(2, 50, 1L, TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>(), new ThreadPoolExecutor.CallerRunsPolicy());;

    private static final Logger logger = LoggerFactory.getLogger(TestQuartz.class);
    /**
     * 每小时执行定时任务，统计号牌类型结果
     *
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

//        logger.info("定时器1");
        long startTime = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //最近十分钟
        Duration duration = new Duration();

        String string = dateFormat.format(new Date());

        try {
            duration.setEndTime(dateFormat.parse(string.toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(duration.getEndTime());

        cal.add(Calendar.MINUTE, -10);
        duration.setStartTime(cal.getTime());
        //查库,时间段，路段编号
//        List<TenMinRouteAvgVelocity> vlist = midTableService.queryMidRouteVelocityTable(duration);
        List<RouteList> areaLists= midTableService.queryMidRouteVelocityByGroup(duration);
        ArrayList<TenMinRouteAvgVelocity> vlist = new ArrayList<TenMinRouteAvgVelocity>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (RouteList item: areaLists) {

            TenMinRouteAvgVelocity avgitem = new TenMinRouteAvgVelocity();

            Collections.sort(item.getList());
            //取系数
            int size = item.getList().size();
            int lsize = (int)(0.1 * size);
            List<MidCpnRouteVelocity> subList = item.getList().subList(lsize, size-lsize);
            BigDecimal vsum = BigDecimal.ZERO;
            for (MidCpnRouteVelocity it: subList) {
                vsum = vsum.add(it.getVelocity());
            }

            BigDecimal avgv = vsum.divide(BigDecimal.valueOf((long)subList.size()), 2, BigDecimal.ROUND_HALF_UP);
            //velocity
            avgitem.setVelocity(avgv);
            //calTime
            avgitem.setCalTime(format.format(new Date()));
            //routeid
            avgitem.setRouteId(item.getAreaId());

            vlist.add(avgitem);

            if (vlist.size() > 100) {
                insertAvgVelocityTable(vlist);
                vlist.clear();
            }
        }

        if (CollectionUtils.isEmpty(vlist)) {
            return;
        }

        insertAvgVelocityTable(vlist);


//        for (TenMinRouteAvgVelocity item : vlist) {
//            if(item.getVelocity().intValue() == 0) {
//                //默认为10
//                item.setVelocity(BigDecimal.TEN);
//            }
//            System.out.println("定时入库数据:" + item);
//            midTableService.addTenMinRouteAvgVelocityTable(item);
//        }
    }

    private void insertAvgVelocityTable(ArrayList<TenMinRouteAvgVelocity> vlist) {

        //入库,批量入库
        executorService.execute(new ResultVelocityThread(vlist));
            //入库kafka
        executorService.submit(new TenMinRouteVelocityProducer(vlist));

    }
}
