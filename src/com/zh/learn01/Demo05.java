package com.zh.learn01;

import org.junit.Test;

/**
 * Created by Administrator on 2015/8/13.
 */
public class Demo05 {

    private static ThreadLocal<Integer> numLocal = new ThreadLocal<Integer>(){
        @Override
        public Integer initialValue(){
            return 1;
        }
    };

    public static Integer getLocalNum(){
        return numLocal.get();
    }

    public static void setNumLocal(Integer num){
        numLocal.set(num);
    }

    @Test
    public void test01(){
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i  = 0;i < 3;i++){
                    Demo05.setNumLocal(Demo05.getLocalNum()+1);
                    System.out.println(Thread.currentThread().getName()+":"+Demo05.getLocalNum());
                }

            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i  = 0;i < 3;i++){
                    Demo05.setNumLocal(Demo05.getLocalNum()+1);
                    System.out.println(Thread.currentThread().getName()+":"+Demo05.getLocalNum());
                }

            }
        });
        t1.start();
        t2.start();
    }

}
