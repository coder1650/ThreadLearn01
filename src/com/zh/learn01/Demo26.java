package com.zh.learn01;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2015/8/25.
 */
public class Demo26 {

    public static void main(String[] args) {
        DataStorage storage = new DataStorage(5);
        //开启五个写的线程
        Thread writer = new Thread(new Writer(storage));
        writer.start();
        /*for (int i = 0; i <5 ; i++) {
            Thread writer = new Thread(new Writer(storage));
            writer.start();
        }*/
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //开启两个读线程
        for (int i = 0; i <2 ; i++) {
            Thread reader = new Thread(new ReaderThread(storage));
            reader.start();
        }
    }

}

class DataStorage{

    private LinkedList<String> storage;
    private int maxSize;
    private ReentrantLock reentrantLock;
    private Condition isSpeace;
    private Condition isFull;

    public DataStorage(int maxSize) {
        this.maxSize = maxSize;
        storage = new LinkedList<String>();
        reentrantLock = new ReentrantLock();
        isSpeace = reentrantLock.newCondition();
        isFull = reentrantLock.newCondition();
    }

    public void insertStorage(String line){
        //获得锁
        reentrantLock.lock();
        //判断storage中是否还有空位子
        try{
            //如果storage已满的话，则等待空位子的出现
            while (storage.size() == maxSize){
                isSpeace.await();
            }
            storage.offer(line);
            System.out.println(Thread.currentThread().getName()
                    + "Inserted line,storage.size: " + storage.size());
            isFull.signalAll();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }
    }

    public String getStorage(){
        String line = null;
        //获得锁
        reentrantLock.lock();

        try {
            //如果storage是空，则等待
            while(storage.isEmpty()){
                isFull.await();
            }
            //获得并移除集合中第一个元素
            line = storage.poll();
            System.out.println(""+Thread.currentThread().getName()+"取出："+line+"storage.size:"+storage.size());
            isSpeace.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }
        return line;
    }


}

class ReaderThread implements Runnable{
    private DataStorage storage;

    public ReaderThread(DataStorage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        while (true){
            String line = storage.getStorage();
            System.out.println("reader:"+Thread.currentThread().getName()+"--"+line);
        }


    }
}

class Writer implements Runnable{

    private DataStorage storage;

    public Writer(DataStorage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            storage.insertStorage(Thread.currentThread().getName()+"_"+i);
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
