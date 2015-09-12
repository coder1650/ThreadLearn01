package com.zh.learn01;

import java.util.concurrent.*;

/**
 * Created by Administrator on 2015/8/24.
 */
public class Demo24 {


    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        TestCancel testCancel = new TestCancel();
        System.out.println("Main: Executing the task....");
        Future<String> future = threadPoolExecutor.submit(testCancel);

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 调用cancel()取消任务
        System.out.println("Main : Canceling the Task");
        future.cancel(true);
        System.out.println("Main : Cancelled : " + future.isCancelled());
        System.out.println("Main : Done : " + future.isDone());

        threadPoolExecutor.shutdown();
        System.out.println("Main : The threadPoolExecutor has finished");

    }
}

class TestCancel implements Callable<String>{

    @Override
    public String call() throws Exception {
        int i = 0;
        while (true){
            System.out.printf("Task:test%d\n",i);
            i++;
            TimeUnit.MILLISECONDS.sleep(100);
        }
    }
}
