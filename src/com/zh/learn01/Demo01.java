package com.zh.learn01;

import org.junit.Test;

import java.util.concurrent.TimeUnit;


/**
 * Created by Administrator on 2015/8/12.
 */
public class Demo01 {

    @Test
    public void test01(){
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("begin load thread1...");
                try {
                    TimeUnit.SECONDS.sleep(4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread1 load finished...");
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("begin load thread2");
                try {
                    TimeUnit.SECONDS.sleep(6);//单位是秒
                    //Thread.sleep(6000);//单位是毫秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread2 load finished");
            }
        });
        thread1.start();
        thread2.start();

        try {
            thread1.join();//join方法会把当前线程挂起，直到调用join方法的线程执行完成
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main method finished");
    }
}
