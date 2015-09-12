package com.zh.learn02;

import org.junit.internal.runners.statements.RunAfters;

/**
 * Created by Administrator on 2015/9/7.
 * 线程的创建和线程信息的获取
 */
public class Demo01 {
    public static void main(String[] args) {
        Thread[] testCreateThreads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            Thread t= new Thread(new TestCreateThread(i)) ;
            if(i % 2 == 0){
                t.setName("高优先级 --Thread_"+i);
                t.setPriority(Thread.MAX_PRIORITY);
            }else{
                t.setName("低优先级 --Thread_"+i);
                t.setPriority(Thread.MIN_PRIORITY);
            }
            testCreateThreads[i] = t;
        }
        //开启线程
        for (int i = 0; i < 10; i++) {
            testCreateThreads[i].start();
            //输出线程的状态
            System.out.println(testCreateThreads[i].getName()+",state"+testCreateThreads[i].getState());
        }

    }
}

/**
 * 对于实现了runnable接口的类来说，创建一个Thread对象并不会创建一个新的执行线程 同样的，调用他的run方法，也不会创建一个新的执行线程
 * 只有调用他的start方法事，才会创建一个新的执行线程
 *
 */
class TestCreateThread implements Runnable{

    private  int number;

    public TestCreateThread(int number) {
        this.number = number;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());

    }
}