package com.zh.learn01;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Exchanger;

/**
 * Created by Administrator on 2015/8/20.
 */
public class Demo16 {

    public static void main(String[] args){
        Exchanger<List<Integer>> exchanger = new Exchanger<List<Integer>>();
        Thread producter = new ProducterOfDemo16(exchanger);
        Thread customer = new CustomerOfDemo16(exchanger);
        producter.start();
        customer.start();
    }
}

class ProducterOfDemo16 extends Thread{

    private List<Integer> buffer = new ArrayList<Integer>();//用于存放交换数据的容器
    private final Exchanger<List<Integer>> exchanger;//定义交换数据的点

    public ProducterOfDemo16(Exchanger<List<Integer>> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run(){
        for(int i =0;i<10;i++ ){
                Random rand = new Random();
                buffer.clear();
                buffer.add(rand.nextInt(1000));
                buffer.add(rand.nextInt(1000));
                buffer.add(rand.nextInt(1000));
                buffer.add(rand.nextInt(1000));
                buffer.add(rand.nextInt(1000));

                try {
                    buffer = exchanger.exchange(buffer);//同步点，此方法会阻塞线程，直到其他线程再次调用该方法，此线程才会继续执行
                } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class CustomerOfDemo16 extends Thread{
    private List<Integer> buffer = new ArrayList<Integer>();//用于存放交换数据的容器
    private final Exchanger<List<Integer>> exchanger;//定义交换数据的点

    public CustomerOfDemo16(Exchanger<List<Integer>> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run(){
        for(int i =0;i<10;i++ ){

            try {
                buffer = exchanger.exchange(buffer);//同步点，此方法会阻塞线程，直到其他线程再次调用该方法，此线程才会继续执行
                System.out.print(buffer.get(0));
                System.out.print(buffer.get(1));
                System.out.print(buffer.get(2));
                System.out.print(buffer.get(3));
                System.out.print(buffer.get(4));
                System.out.println();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
