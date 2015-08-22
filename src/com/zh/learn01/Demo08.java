package com.zh.learn01;

import org.junit.Test;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/14.
 */
public class Demo08 {

    public static void main(String[] args){
        EventStage stage = new EventStage();

        Producter producter = new Producter(stage);
        Customer customer = new Customer(stage);

        Thread t1 = new Thread(producter);
        Thread t2 = new Thread(customer);
        Thread t3 = new Thread(customer);

        t1.start();
        t2.start();
        t3.start();
    }


}

/**
 *存储类
 */
class EventStage{
    private Integer maxSize;
    private List<Date> stage;

    public EventStage() {
        this.maxSize = 20;
        this.stage = new LinkedList<Date>();
    }

    /**
     *  同步set方法，保存数据到stage中
     * 1、当stage满的时候，调用wait()方法等待空闲空间出现
     * 2、当stage有空闲空间时，调用notifyAll()方法唤醒休眠的方法
     */
    public synchronized void set(){
        while (stage.size() == maxSize){
            System.out.println("stage is full,please wait.....");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stage.add(new Date());
        System.out.println("stage size:"+stage.size());
        notifyAll();

    }

    /**
     * 同步get方法，从stage中取数据
     * 1、当stage为空的时候，调用waite()方法等待stage中被放入数据
     * 2、当stage中有数据时，调用notifyAll()方法唤醒所有休眠线程
     */
    public synchronized void get(){
        while (stage.size() == 0){
            System.out.println("stage is empty please waite.....");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(Thread.currentThread().getName()+"Get : size = " + stage.size() + " Value = "
                + ((LinkedList<?>) stage).poll());
        notifyAll();

    }
}

/**
 * 生产者类
 */
class Producter implements  Runnable{
    private EventStage stage;

    public Producter(EventStage stage) {
        this.stage = stage;
    }

    @Override
    public void run(){
        for(int i = 0;i<100;i++){
            stage.set();
        }
    }
}

/**
 * 消费者类
 */
class  Customer implements Runnable{
    private EventStage stage;

    public Customer(EventStage stage) {
        this.stage = stage;
    }

    @Override
    public void run(){
        for(int i = 0;i < 100;i++){
            stage.get();
        }
    }

}
