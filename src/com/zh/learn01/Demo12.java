package com.zh.learn01;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Administrator on 2015/8/15.
 */
public class Demo12 {

    public static void main(String[] args){
        TestRun testRun = new TestRun();
        Thread t1 = new Thread(testRun);
        Thread t2 = new Thread(testRun);
        Thread t3 = new Thread(testRun);

        t1.start();
        t2.start();
        t3.start();
    }


}

class TestRun implements Runnable{
    private Object obj = new Object();

    @Override
    public void run(){
        synchronized (obj){
            while (true){
                System.out.println(Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                    obj.notify();
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
