package com.zh.learn01;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2015/8/14.
 */
public class Demo09 {
    public static void main(String[] args) {

        //共享的打印队列对象
        PrintQueue printQueue = new PrintQueue();

        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(new Job(printQueue), "Thread " + i);
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
    }
}

class PrintQueue{
    //声明一个锁对象
    private Lock queueLock = new ReentrantLock();

    public void printObj(Object document){
        //调用lock方法获得对锁对象的控制
        queueLock.lock();
        try {
            Long duration = (long) (Math.random() * 10000);
            System.out.println(Thread.currentThread().getName()
                    + " : PrintQueue : Printing a Job during "
                    + (duration / 1000) + " seconds");
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //释放锁
            queueLock.unlock();
        }

    }
}

/**
 * 打印工作类
 *
 * @author Nicholas
 *
 */
class Job implements Runnable {

    private PrintQueue printQueue;

    public Job(PrintQueue printQueue) {
        this.printQueue = printQueue;
    }

    @Override
    public void run() {
        System.out.println("Going to print a document "
                + Thread.currentThread().getName());
        printQueue.printObj(new Object());
        System.out.println("The document has been printed "
                + Thread.currentThread().getName());
    }
}
