package com.zh.learn01;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2015/8/14.
 */
public class Demo11 {



    public static void main(String[] args){
        final ReentrantLock reentrantLock = new ReentrantLock();
        final Condition condition = reentrantLock.newCondition();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    reentrantLock.lock();
                    System.out.println("我在等待一个信号"+this);
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("拿到信号了..." + this);
                reentrantLock.unlock();

            }
        },"thread_await");

        t1.start();


        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                reentrantLock.lock();
                System.out.println("我也在等一个信号"+this);
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("我也拿到信号了..."+this);
                reentrantLock.unlock();
            }
        },"thread_await2");
        t2.start();

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    reentrantLock.lock();
                    System.out.println("我拿到锁了");
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                condition.signal();
                System.out.println("我发送了一个信号");
                reentrantLock.unlock();
            }
        },"thread_signal");
        t3.start();



    }
}

