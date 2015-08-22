package com.zh.learn01;

import org.junit.Test;

import java.util.concurrent.Phaser;

/**
 * Created by Administrator on 2015/8/20.
 */
public class Demo15 {


    @Test
    public void test01(){
        Phaser phaser = new Phaser(3){
            //共有3个工作线程，因此在构造函数中赋值为3
            //当phaser管理的线程都到达每一个阶段的终点时，都会调用该方法
            @Override
            protected boolean onAdvance(int phase,int registerParties){
                System.out.println("\n------------------------------------");
                //本例中，当只剩一个线程时，这个线程必定是主线程，返回true表示终结
                return registerParties == 1;
            }
        };
        System.out.println("程序开始执行");
        for(int i=0; i<3; i++) { //创建并启动3个线程
            new ThreadLearn(phaser,(char)(97+i)).start();
        }
        phaser.register();//将主线程动态增加到phaser中，执行此句后，phaser管理了4个线程
        while (!phaser.isTerminated()){
            phaser.arriveAndAwaitAdvance();//只要phaser不终结，主线程就一直等待
        }
        //跳出上面循环后，意味着phaser终结，即3个工作线程已经结束
        System.out.println("程序结束");
    }

    @Test
    public void test02(){
        Thread t1 = new ThreadLearn2((char)97);
        Thread t2 = new ThreadLearn2((char)98);
        Thread t3 = new ThreadLearn2((char)99);
        t1.start();
        t2.start();
        t3.start();
    }
}

class ThreadLearn extends Thread{
    private char c;
    private Phaser phaser;

    public ThreadLearn(Phaser phaser,char c){
        this.c = c;
        this.phaser = phaser;
    }

    @Override
    public void run(){
        while (!phaser.isTerminated()){
            for (int i = 0;i<3;i++){
                System.out.print(c + " ");
            }
            //当打印完成后，把当前字母向后退三个，例如，b打印完后是e
            c = (char)(c+3);
            if(c>'z'){
                //如果超出了字母z，则在phaser中动态减少一个线程，并退出循环结束本线程
                //当3个工作线程都执行此语句后，phaser中就只剩一个主线程了
                phaser.arriveAndDeregister();
                break;
            }else {
                //反之，等待其他线程到达阶段终点，再一起进入下一个阶段
                phaser.arriveAndAwaitAdvance();
            }
        }
    }

}

class ThreadLearn2 extends Thread{
    private char c;
    public ThreadLearn2(char c){
        this.c = c;
    }
    @Override
    public void run(){
        while(true){
            for (int i = 0;i<3;i++){
                System.out.print(c+" ");
            }
            c = (char)(c + 3);
            if(c > 'z'){
                break;
            }
        }
    }
}
