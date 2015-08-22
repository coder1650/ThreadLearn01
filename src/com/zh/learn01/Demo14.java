package com.zh.learn01;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/8/15.
 */
public class Demo14 {

    public static void main(String[] args){


        VideoSystem videoSystem = new VideoSystem(10);
        Thread t1 = new Thread(videoSystem);
        t1.start();

        for(int i = 0;i<10;i++){
            Part part = new Part(videoSystem,"p__"+i);
            Thread t = new Thread(part);
            t.start();
        }
    }

}

class VideoSystem implements Runnable{
    private final CountDownLatch countDownLatch;

    public VideoSystem(int num) {
        this.countDownLatch = new CountDownLatch(num);
    }

    public synchronized void isArrive(String name){
        System.out.println(name + "is arrive");
        countDownLatch.countDown();
        //打印未到人数
        System.out.println("还有"+countDownLatch.getCount()+"人未到");
    }
    @Override
    public void run(){
        System.out.println("共有"+countDownLatch.getCount()+"人要来");
        try {
            countDownLatch.await();
            System.out.println("人到齐了，可以开始了");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class Part implements Runnable{
    private VideoSystem videoSystem;
    private String name;

    public Part(VideoSystem videoSystem, String name) {
        this.videoSystem = videoSystem;
        this.name = name;
    }

    @Override
    public void run(){
        long sleepTime = (long)Math.random() * 10;
        try {
            TimeUnit.SECONDS.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        videoSystem.isArrive(name);
    }


}
