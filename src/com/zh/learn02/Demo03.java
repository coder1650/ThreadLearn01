package com.zh.learn02;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/9/7.
 */
public class Demo03 {
    public static void main(String[] args) throws InterruptedException {
        Deque<Event> deque = new ArrayDeque<Event>();
        WriterRunnable writer = new WriterRunnable(deque);
        //创建三个写的线程
        for (int i = 0; i < 3; i++) {
            Thread w = new Thread(writer);
            w.start();
        }
        TimeUnit.SECONDS.sleep(1);
        Thread clean = new CleanThread(deque);
        clean.start();

    }
}

class Event{
    private Date date;
    private String event;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}

/**
 * 每隔一秒向队列中写入一个事件对象
 */
class WriterRunnable implements Runnable{
    private Deque<Event> deque;

    public WriterRunnable(Deque<Event> deque) {
        this.deque = deque;
    }

    @Override
    public void run() {
        System.out.println("begin write");
        for (int i = 0; i < 100; i++) {
            Event event = new Event();
            event.setDate(new Date());
            event.setEvent(Thread.currentThread().getId()+":已产生一个事件");
            deque.addFirst(event);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}

class CleanThread extends Thread{
    private Deque<Event> deque;

    public CleanThread(Deque<Event> deque){
        this.deque = deque;
        //设置为守护线程
        setDaemon(true);
    }

    @Override
    public void run(){
        while (true) {
            Date date = new Date();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clean(date);
        }
    }

    //读取队列中最后一个事件，如果这个事件是10秒以前创建的，删除该事件，然后判断下一个
    public void clean(Date date){
        long difference;
        boolean delete;
        if(deque.size() < 0){return;}
        delete = false;
        System.out.println("------------------------");
        do {
            Event event = deque.getLast();
            Date d = new Date();
            difference = d.getTime() - event.getDate().getTime();
            if(difference > 1000){
                System.out.println("Clean:"+event.getEvent());
                deque.removeLast();
                delete = true;
            }
        } while (difference > 1000);

        if(delete){
            System.out.println("Clean:size of the deque:"+deque.size());
        }


    }


}
