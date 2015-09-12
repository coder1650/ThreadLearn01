package com.zh.learn01;

import java.util.concurrent.*;

/**
 * Created by Administrator on 2015/8/31.
 */
public class Demo30 {
    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Future<Integer> result = forkJoinPool.submit(new Calculator(0, 10000));
        try {
            System.out.println("1.。。10000累加的结果为："+result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

class Calculator extends RecursiveTask<Integer>{

    private final static int THERSHOLD = 100;
    private int start;
    private int end;

    public Calculator(int start, int end) {
        this.start = start;
        this.end = end;
    }


    @Override
    protected Integer compute() {
        int sum = 0;
        if(end - start < THERSHOLD){
            for (int i = start; i <= end; i++) {
                sum += i;
            }
        }else{
            int middle = (start + end)/2;
            Calculator c1 = new Calculator(start,middle);
            Calculator c2 = new Calculator(middle+1,end);
            c1.fork();
            c2.fork();
            //合并结果
            sum = c1.join() + c2.join();

        }

        return sum;
    }
}
