package com.zh.learn02;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/9/9.
 * 使用线程工厂类ThreadFactory创建线程
 */
public class Demo05 {
    public static void main(String[] args) {
        MyThreadFactory myThreadFactory = new MyThreadFactory("myThreadFactory");
        Task task = new Task();
        Thread thread;
        System.out.println("start create thread");
        for (int i = 0; i < 10; i++) {
            thread = myThreadFactory.newThread(task);
            thread.start();
        }
        System.out.println("factory stats:");
        System.out.println(myThreadFactory.getStats());
    }
}

class MyThreadFactory implements ThreadFactory{
    private  int counter;
    private String name;
    private List<String> stats;

    public String getStats(){
        StringBuffer stringBuffer = new StringBuffer();
        Iterator iterator = stats.iterator();
        while (iterator.hasNext()){
            stringBuffer.append(iterator.next());
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }

    public MyThreadFactory(String name) {
        this.name = name;
        stats = new ArrayList<String>();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r,name+"_Thread_"+counter);
        counter++;
        stats.add(String.format("Create thread %d with name %s on %s\n",
                thread.getId(), thread.getName(), new Date()));
        return thread
                ;
    }
}

class Task implements Runnable{

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


