package com.zh.learn01;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/8/21.
 */
public class Demo17 {
    public static void main(String[] args){
        Server server = new Server();
        for(int i=0;i<100;i++){
            Task task  = new Task("Task-"+i);
            server.executeTask(task);
        }
        server.shutDownServer();
    }
}


class Server{
    private ThreadPoolExecutor threadPoolExecutor;

    public Server(){
        this.threadPoolExecutor = (ThreadPoolExecutor)Executors.newFixedThreadPool(5);
    }

    public void executeTask(Task task){
        System.out.print("Server: A new Task has arrived \n ");
        threadPoolExecutor.execute(task);

        System.out.printf("Server: pool size : %d\n",
                threadPoolExecutor.getPoolSize());
        System.out.printf("Server: Active Count : %d\n",
                threadPoolExecutor.getActiveCount());
        System.out.printf("Server: Completed Tasks : %d\n",
                threadPoolExecutor.getCompletedTaskCount());
    }
    public void shutDownServer(){
        threadPoolExecutor.shutdown();
    }
}

class Task implements Runnable{

    private Date initdate;
    private String name;

    public Task(String name) {
        initdate = new Date();
        this.name = name;
    }

    @Override
    public void run() {
        System.out.printf("%s : TaskName %s : Created on :%s\n", Thread
                .currentThread().getName(), name, initdate);
        System.out.printf("%s : Task %s : Started on :%s\n", Thread
                .currentThread().getName(), name, new Date());
        try {
            Long duration = (long) (Math.random() * 10);
            System.out.printf("%s: Task %s: Doing a task during %d seconds\n",
                    Thread.currentThread().getName(), name, duration);
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("%s : Task %s: Finished on: %s\n", Thread
                .currentThread().getName(), name, new Date());
    }
}
