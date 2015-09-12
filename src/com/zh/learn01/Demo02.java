package com.zh.learn01;

import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/8/12.
 */
public class Demo02 {

    public static void main(String[] args){
        // 创建队列对象，用来保存事件
        Deque<Event> dEvents = new ArrayDeque<Event>();

        WriteThread writeTask = new WriteThread(dEvents);
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(writeTask);
            thread.start();
        }
        CleanTask cleanTask = new CleanTask(dEvents);
        cleanTask.start();
    }


}

class Event{
    private Date date;
    private String event;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

class WriteThread implements Runnable{
    private Deque<Event> dequeEvent;

    public WriteThread(Deque<Event> dequeEvent) {
        this.dequeEvent = dequeEvent;
    }

    @Override
    public void run(){
        for (int i=0;i<100;i++ ){
            Event event = new Event();
            event.setDate(new Date());
            event.setEvent("thread id:"+Thread.currentThread().getId());
            dequeEvent.addFirst(event);
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class CleanTask extends Thread{
    private Deque<Event> dequeEvent;

    public CleanTask(Deque<Event> dequeEvent) {
        this.dequeEvent = dequeEvent;
        setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            Date date = new Date();
            Clean(date);
        }
    }
    // 读取队列的最后一个事件对象，如果这个事件是10秒钟前创建的，则删除，然后检查下一个
    private void Clean(Date date) {
        long difference;
        boolean delete;
        if (dequeEvent.size() <= 0)
            return;
        delete = false;

        System.out.println("--------------------------");
        do {
            Event event = dequeEvent.getLast();
            difference = date.getTime() - event.getDate().getTime();
            System.out.print("difference:"+difference);
            if (difference > 1000) {
                System.out.printf("Cleaner : %s\n", event.getEvent());
                dequeEvent.removeLast();
                delete = true;
            }
        } while (difference > 1000);

        if (delete) {
            System.out.println("Cleaner : Size of the queue :"
                    + dequeEvent.size());
        }
    }
}