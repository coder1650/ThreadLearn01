package com.zh.learn01;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Created by Administrator on 2015/8/21.
 */
public class Demo18 {

    public static void main(String[] args){
        ThreadCallable threadCallable = new ThreadCallable();
        FutureTask<Integer> futureTask = new FutureTask<Integer>(threadCallable);
        new Thread(futureTask,"有返回值的线程").start();
        //获得线程的返回值
        try {
           System.out.print("线程的返回值："+ futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

class ThreadCallable implements Callable<Integer>{

    @Override
    public Integer call() throws Exception {
        System.out.print("当前线程的名字为：\n"+Thread.currentThread().getName());
        int i = 0;
        for(;i<10;i++){
            System.out.print("循环变量i的值为：\n"+i);
        }
        return i;
    }
}
