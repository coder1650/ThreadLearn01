package com.zh.learn01;

import org.junit.Test;

import java.util.Random;

/**
 * Created by Administrator on 2015/8/13.
 */
public class Demo04 {
    private Integer num = 1;
    public static Integer num_s = 1;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Test
    public void test01(){
        Demo04 demo04_1 = new Demo04();
        Demo04 demo04_2 = new Demo04();
        Demo04 demo04_3 = new Demo04();
        ThreadTest t1 = new ThreadTest(demo04_1);
        ThreadTest t2 = new ThreadTest(demo04_2);
        ThreadTest t3 = new ThreadTest(demo04_3);
        t1.start();
        t2.start();
        t3.start();
        System.gc();

    }
    @Test
    public void test02(){
        Random rand = new Random();
        for(int i = 0;i<10;i++){
            System.out.println(rand.nextInt(100));
            System.out.print(rand.nextGaussian());
        }

    }
}

class ThreadTest extends Thread{
    Demo04 demo04;
    public ThreadTest(Demo04 demo04){
        this.demo04 = demo04;
    }
    @Override
    public void run(){
        //demo04.setNum(demo04.getNum() + 1);
        Demo04.num_s += 1;
        System.out.println("thread["+Thread.currentThread().getName()+"]:"+Demo04.num_s);
    }
}
