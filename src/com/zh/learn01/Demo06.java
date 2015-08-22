package com.zh.learn01;

import org.junit.Test;

/**
 * Created by Administrator on 2015/8/13.
 */
public class Demo06 {

    @Test
    public void test01(){
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.print(Thread.currentThread().getName());
            }
        },"线程1");
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.print(Thread.currentThread().getName());
            }
        },"线程1");
        t1.start();
        t2.start();
    }
}
