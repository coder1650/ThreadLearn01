package com.zh.learn02;

import java.awt.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/11.
 */
public class Demo07 {

    public static void main(String[] args) {
        Storage07 storage = new Storage07();
        Thread producter = new Thread(new ProducterForStorage(storage));
        Thread customer = new Thread(new CustomerForStorage(storage));
        producter.start();
        customer.start();
    }

}

class Storage07{
    private int maxSize = 10;
    private List<Date> storage;

    public Storage07() {
        this.storage = new LinkedList<Date>();
    }

    public synchronized void set(){
        while (storage.size() == maxSize){
            System.out.println("容器满了，等一会再存吧...");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        storage.add(new Date());
        notifyAll();
    }

    public synchronized void get(){
        while (storage.size() == 0) {
            System.out.println("容器中没有东西了，等一会再来取吧...");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("容器的大小为：" + storage.size() + ",本次取出的值为：" + ((LinkedList<Date>) storage).poll());
        notifyAll();
    }


}

class ProducterForStorage implements Runnable{
    private Storage07 storage;

    public ProducterForStorage(Storage07 storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            storage.set();
        }
    }
}

class CustomerForStorage implements Runnable{
    private Storage07 storage;

    public CustomerForStorage(Storage07 storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            storage.get();
        }
    }
}