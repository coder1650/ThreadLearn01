package com.zh.learn01;

import java.sql.Time;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/8/24.
 */
public class Demo23 {

    public static void main(String[] args){
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(5);
        System.out.printf("main thread is start on %s \n",new Date());
        for(int i=0;i<5;i++){
            ScheduleExe scheduleExe = new ScheduleExe("Thread"+i);
            //已任务开始时间为基础
            scheduledThreadPoolExecutor.schedule(scheduleExe,i+1, TimeUnit.SECONDS);
        }
        scheduledThreadPoolExecutor.shutdown();
        try {
            scheduledThreadPoolExecutor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("main thread is end on %s \n",new Date());
    }
}

class ScheduleExe implements Callable<String>{

    private String name;

    public ScheduleExe(String name) {
        this.name = name;
    }

    @Override
    public String call() throws Exception {
        System.out.printf("%s is start at : %s \n",name,new Date());
        return "hello world";
    }
}