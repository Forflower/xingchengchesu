package com.hisense.springboot.thread;

import com.hisense.springboot.mapper.MidCpnRouteVelocityMapper;
import com.hisense.springboot.model.MidCpnRouteVelocity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskWithResult implements Callable<String> {

    public MidCpnRouteVelocity model;

    MidCpnRouteVelocityMapper midCpnRouteVelocityMapper;

    public TaskWithResult(MidCpnRouteVelocity model, MidCpnRouteVelocityMapper midCpnRouteVelocityMapper) {
        this.model = model;
        this.midCpnRouteVelocityMapper = midCpnRouteVelocityMapper;

        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 任务的具体过程，一旦任务传给ExecutorService的submit方法，则该方法自动在一个线程上执行。
     *
     * @return
     * @throws Exception
     */
    @Override
    public String call() throws Exception {


        int n = midCpnRouteVelocityMapper.insert(model);

//        System.out.println("call()方法被自动调用,干活！！！             " + Thread.currentThread().getName());
//        if (new Random().nextBoolean())
//            throw new TaskException("Meet error in task." + Thread.currentThread().getName());
//        // 一个模拟耗时的操作
//        for (int i = 999999999; i > 0; i--)
//            ;
        return "call()方法被自动调用，任务的结果是：" +  n + "    " + Thread.currentThread().getName();
    }
}

class TaskException extends Exception {
    public TaskException(String message) {
        super(message);
    }
}
