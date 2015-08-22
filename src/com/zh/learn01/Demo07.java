package com.zh.learn01;

/**
 * Created by Administrator on 2015/8/13.
 * 模拟场景：电影院售票，有两个播放室，两个售票处，一张电影票只能去特定的播放室使用
 */
public class Demo07 {
    private long num1;
    private long num2;//每一个播放室的票量

    private final Object show1;
    private final Object show2;

    public Demo07() {
        this.show1 = new Object();
        this.show2 = new Object();
        this.num2 = 20;
        this.num1 = 20;
    }

    //售票处1
    public boolean sell1(int num){
        synchronized (show1){
            if(num < num1){
                num1 -= num;
                return true;
            }else{
                return false;
            }
        }
    }
    //售票处2
    public boolean sell2(int num){
        synchronized (show2){
            if(num < num2){
                num2 -= num;
                return true;
            }else{
                return false;
            }
        }
    }

    //获得当前剩余票量
    public long getNum1(){
        return num1;
    }
    public long getNum2(){
        return num2;
    }

    public static void main(String[] args) throws InterruptedException {
        Demo07 demo07 = new Demo07();
        Buy1 buy1 = new Buy1(demo07);
        Buy2 buy2 = new Buy2(demo07);
        buy1.start();
        buy2.start();
        buy1.join();
        buy2.join();

        System.out.println(demo07.getNum1());
        System.out.println(demo07.getNum2());
    }

}

//买票顾客1,去售票处1
class Buy1 extends Thread{
    Demo07 demo07;
    public Buy1(Demo07 demo07) {
        this.demo07 = demo07;
    }

    public void buy(int num){
        demo07.sell1(num);
    }

    @Override
    public void run(){
        buy(2);
    }

}
//买票顾客2,去售票处2
class Buy2 extends Thread{
    Demo07 demo07;
    public Buy2(Demo07 demo07) {
        this.demo07 = demo07;
    }

    public void buy(int num){
        demo07.sell2(num);
    }

    @Override
    public void run(){
        buy(3);
    }

}
