package com.zh.learn02;

import com.zh.learn01.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2015/9/12.
 */
public class Demo08 {
    private List<String> list = new ArrayList<String>();
    Lock lock = new ReentrantLock();//注意该对象声明时的作用于

    public static void main(String[] args) {
        final Demo08 demo = new Demo08();
         new Thread(){
            @Override
             public void run(){
                //demo.insert(Thread.currentThread().getName());
                demo.tryLockTest(Thread.currentThread().getName());
            }
         }.start();

        new Thread(){
            @Override
            public void run(){
                //demo.insert(Thread.currentThread().getName());
                demo.tryLockTest(Thread.currentThread().getName());
            }
        }.start();


    }

    public void insert(String str){

        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName()+":获得了锁");
            for (int i = 0; i < 5; i++) {
                list.add(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            System.out.println(Thread.currentThread().getName()+":释放了锁");
        }
    }

    public void tryLockTest(String str){
        if(lock.tryLock()){
            System.out.println(Thread.currentThread().getName()+"获取锁成功");
            try {
                for (int i = 0; i < 10; i++) {
                    list.add(str);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println(Thread.currentThread().getName()+":释放锁");
            }
        }else{
            System.out.println(Thread.currentThread().getName()+":获取锁失败");
        }
    }

}


