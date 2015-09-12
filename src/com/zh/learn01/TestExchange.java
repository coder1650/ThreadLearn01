package com.zh.learn01;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * Created by Administrator on 2015/9/6.
 */
public class TestExchange {
    public static void main(String[] args) {
        List<String> buffer1 = new ArrayList<String>();
        List<String> buffer2 = new ArrayList<String>();

        Exchanger<List<String>> exchanger = new Exchanger<List<String>>();
        ProducerOfEx producer = new ProducerOfEx(buffer1, exchanger);
        ConsumerOfEx consumer = new ConsumerOfEx(buffer2, exchanger);

        Thread threadProducer = new Thread(producer);
        Thread threadConsumer = new Thread(consumer);

        threadProducer.start();
        threadConsumer.start();
    }
}

class ProducerOfEx implements Runnable {

    private List<String> buffer;// 生产者-消费者进行交换的数据结构
    private final Exchanger<List<String>> exchanger;// 用于同步生产者和消费者交换的数据结构

    public ProducerOfEx(List<String> buffer, Exchanger<List<String>> exchanger) {
        this.buffer = buffer;
        this.exchanger = exchanger;
    }

    // 循环执行10次数据交换
    @Override
    public void run() {
        int cycle = 1;
        for (int i = 0; i < 10; i++) {
            System.out.println("Producer : Cycle " + cycle);
            for (int j = 0; j < 10; j++) {
                String message = "Event " + ((i * 10) + j);
                System.out.println(message);
                buffer.add(message);
            }

            try {
                buffer=exchanger.exchange(buffer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Producer: "+buffer.size());
            cycle++;
        }
    }

}

class ConsumerOfEx implements Runnable {

    private List<String> buffer;// 生产者-消费者进行交换的数据结构
    private final Exchanger<List<String>> exchanger;// 用于同步生产者和消费者交换的数据结构

    public ConsumerOfEx(List<String> buffer, Exchanger<List<String>> exchanger) {
        this.buffer = buffer;
        this.exchanger = exchanger;
    }

    public void run() {
        int cycle = 1;
        for (int i = 0; i < 10; i++) {
            System.out.println("Consumer : Cycle " + cycle);

            try {
                buffer = exchanger.exchange(buffer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Consumer : " + buffer.size());

            for (int j = 0; j < 10; j++) {
                String message = buffer.get(0);
                System.out.println("Consumer : " + message);
                buffer.remove(0);
            }
            cycle++;
        }
    };
}