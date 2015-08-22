package com.zh.learn01;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Administrator on 2015/8/14.
 */
public class Demo10 {
    public static void main(String[] args){
        //创建并发访问的账户
        MyCount myCount = new MyCount("95599200901215522", 10000);
        //创建一个锁对象
        ReadWriteLock lock = new ReentrantReadWriteLock(false);
        //创建一个线程池
        ExecutorService pool = Executors.newFixedThreadPool(2);
        //创建一些并发访问用户，一个信用卡，存的存，取的取，好热闹啊
        User u1 = new User("张三", myCount, -4000, lock, false);
        User u2 = new User("张三他爹", myCount, 6000, lock, false);
        User u3 = new User("张三他弟", myCount, -8000, lock, false);
        User u4 = new User("张三", myCount, 800, lock, false);
        User u5 = new User("张三他爹", myCount, 0, lock, true);
        //在线程池中执行各个用户的操作
        pool.execute(u1);
        pool.execute(u2);
        pool.execute(u3);
        pool.execute(u4);
        pool.execute(u5);
        //关闭线程池
        pool.shutdown();
    }
}


class User implements Runnable{
    private String name;
    private MyCount count;
    private int cash;
    private ReadWriteLock myLock;
    private boolean isCheck;

    public User(String name, MyCount count, int cash, ReadWriteLock lock, boolean isCheck) {
        this.name = name;
        this.count = count;
        this.cash = cash;
        this.myLock = lock;
        this.isCheck = isCheck;
    }

    @Override
    public void run(){
        if(isCheck){
            //查询，只执行读操作，加读锁
            myLock.readLock().lock();
            System.out.println("读操作：" + name + "正在查询" + count.getOid() + "账户，当前金额为：" + count.getCash());
            myLock.readLock().unlock();
        }else{
            //存取操作，只执行写操作，加写锁
            myLock.writeLock().lock();
            System.out.println("写前查询操作："+name+"正在操作"+count.getOid()+"账户,操作金额为："+cash+",当前账户金额为："+count.getCash());
            count.setCash(count.getCash() + cash);
            System.out.println("写后查询操作："+name+"正在操作"+count.getOid()+"账户,操作金额为："+cash+",当前账户金额为："+count.getCash());
            myLock.writeLock().unlock();

        }
    }

}

class MyCount{
    private String oid;
    private int cash;

    public MyCount(String oid, int cash) {
        this.oid = oid;
        this.cash = cash;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    @Override
    public String toString(){
        return "账户oid:"+oid+",当前余额："+cash;
    }
}
