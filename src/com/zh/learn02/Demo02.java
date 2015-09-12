package com.zh.learn02;

import com.sun.org.apache.bcel.internal.generic.NEW;

import javax.xml.stream.XMLOutputFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/9/7.
 */
public class Demo02 {
    public static void main(String[] args) {
        Thread dataSource = new Thread(new DataSourceLoder());
        Thread newwork  = new Thread(new NetworkConntectionsLoder());

        dataSource.start();
        newwork.start();
        try {
            dataSource.join();
            newwork.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main:main config is all load");
    }
}

class DataSourceLoder implements Runnable{

    @Override
    public void run() {
        System.out.println("datasource begin work");
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("datasource is finished work");
    }
}

class NetworkConntectionsLoder implements Runnable{

    @Override
    public void run() {
        System.out.println("network begin work");
        try {
            TimeUnit.SECONDS.sleep(6);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("network is finished work");
    }
}
