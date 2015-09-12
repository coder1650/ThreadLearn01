package com.zh.learn01;

import java.util.concurrent.*;

/**
 * Created by Administrator on 2015/8/24.
 */
public class Demo25 {
    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        TestFutureTask[] testFutureTasks = new TestFutureTask[5];
        for (int i = 0; i < testFutureTasks.length; i++) {
            MyCallable myCallable = new MyCallable("myCallAble"+i);
            testFutureTasks[i] = new TestFutureTask(myCallable);
            threadPoolExecutor.submit(testFutureTasks[i]);
        }
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < testFutureTasks.length; i++) {
            testFutureTasks[i].cancel(true);
        }

        for (int i = 0; i < testFutureTasks.length; i++) {
            if(!testFutureTasks[i].isCancelled()){
                try {
                    System.out.println("没有被取消的线程的执行结果："+testFutureTasks[i].get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

        }

        threadPoolExecutor.shutdown();
    }
}

class TestFutureTask extends FutureTask<String>{
    private String name;

    public TestFutureTask(Callable<String> callable) {
        super(callable);
        this.name = ((MyCallable)callable).getName();
    }

    @Override
    protected void done() {
        if(isCancelled()){
            System.out.printf("%s is cancelled\n", name);
        }
        super.done();
    }
}

class MyCallable implements Callable<String>{
    private String name;

    public MyCallable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String call() throws Exception {
        try {
            long duration = (long) (Math.random() * 10);
            System.out.printf("%s : Waiting %d seconds for results.\n",
                    this.name, duration);
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "Hello World, I'm " + name;
    }
}
