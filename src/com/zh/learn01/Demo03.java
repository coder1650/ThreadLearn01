package com.zh.learn01;

import org.junit.Test;

/**
 * Created by Administrator on 2015/8/13.
 */
public class Demo03 {
    public static Integer num = 0;

    //通过匿名内部类覆盖ThreadLocal的initialValue()方法，指定初始值
    private static ThreadLocal<Integer> seqNum = new ThreadLocal<Integer>(){
        public Integer initialValue(){
            return num;
        }
    };

    //获取下一个序列值
    public static Integer getNextNum(){
        seqNum.set(seqNum.get() + 1);
        return seqNum.get();
    }


    public static void main(String[] args){
        Demo03 demo03 = new Demo03();
        ThreadClent t1 = new ThreadClent(demo03);
        ThreadClent t2 = new ThreadClent(demo03);
        ThreadClent t3 = new ThreadClent(demo03);
        t1.start();
        t2.start();
        t3.start();
    }


}


class ThreadClent extends Thread{
    private  Demo03 seq;

    public ThreadClent(Demo03 seq) {
        this.seq = seq;
    }

    @Override
    public void run(){

        for(int i = 0;i<3;i++){
            System.out.println("thread["+Thread.currentThread().getName()+"],sn["+seq.getNextNum()+"]");
        }
    }
}
