package com.zh.learn01;

/**
 * Created by Administrator on 2015/8/22.
 */
public class Demo20 {

    public static ThreadLocal<Integer> threadNum = new ThreadLocal<Integer>(){
        public Integer initialValue(){
            return 0;
        }
    };

    public Integer getNum(){
        threadNum.set(threadNum.get() + 1);
        return threadNum.get();
    }



    public static void main(String[] args){
        Demo20 demo20 = new Demo20();
        Thread t1 = new Thread(new TestThread(demo20));
        Thread t2 = new Thread(new TestThread(demo20));
        Thread t3 = new Thread(new TestThread(demo20));
        t1.start();
        t2.start();
        t3.start();
    }


}

class TestThread implements Runnable{
    private Demo20 demo20;

    public TestThread(Demo20 demo20) {
        this.demo20 = demo20;
    }

    @Override
    public void run(){
        for (int i = 0;i<3;i++){
            System.out.println(Thread.currentThread().getName()+"第"+i+"次:"+demo20.getNum());
        }
    }
}


