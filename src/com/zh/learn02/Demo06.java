package com.zh.learn02;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/9/9.
 */
public class Demo06 {
    public static void main(String[] args) {
        Count count = new Count();
        count.setBalance(100);
        Company company = new Company(count);
        Bank bank = new Bank(count);
        Thread addT = new Thread(company);
        Thread subT = new Thread(bank);
        System.out.println("start...");
        addT.start();
        subT.start();


        try {
            addT.join();
            subT.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("count:"+count.getBalance());
    }
}


class Count{
    private int balance;

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    //向账户充值
    public synchronized void addAmount(int amount){
        int tmp = balance;

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tmp += amount;
        balance = tmp;
        System.out.println("after add balance:"+balance);
    }

    //向账户扣钱
    public synchronized void subAmount(int amount){
        int tmp = balance;
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tmp -= amount;
        balance = tmp;
        System.out.println("after sub amount:"+balance);
    }

}

class Bank implements Runnable{

    private Count count;

    public Bank(Count count) {
        this.count = count;
    }

    @Override
    public void run() {

        for (int i = 0; i < 10; i++) {
            count.addAmount(1000);
        }

    }
}

class Company implements Runnable{

    private Count count;

    public Company(Count count) {
        this.count = count;
    }

    @Override
    public void run() {

        for (int i = 0; i < 10; i++) {
            count.subAmount(1000);
        }

    }
}
