package com.zh.learn01;

import java.util.concurrent.*;

/**
 * Created by Administrator on 2015/8/31.
 */
public class Demo28 {

    public static void main(String[] args) {
        //创建线程执行器
        ExecutorService executor = Executors.newCachedThreadPool();
        CompletionService<String> completionService = new ExecutorCompletionService<String>(executor);
        //创建两个请求报告任务，让后创建线程执行他们
        ReprotRequest reprotRequest1 = new ReprotRequest("req1",completionService);
        ReprotRequest reprotRequest2 = new ReprotRequest("req2",completionService);

        Thread t1 = new Thread(reprotRequest1);
        Thread t2 = new Thread(reprotRequest2);

        //创建报告处理任务，然后创建线程执行他们
        ReportProcess reportProcess = new ReportProcess(completionService);
        Thread proThread = new Thread(reportProcess);

        //启动这三个线程
        t1.start();
        t2.start();
        proThread.start();

        // 等待ReportRequest线程处理结束
        try {
            System.out.println("Main : waiting for the report generators");
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 结束线程执行器
        System.out.println("Main : shutting down the executor");
        executor.shutdown();

        // 调用awaitTermination等待所有的任务执行结束
        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 结束ReportProcessor的执行
        reportProcess.setEnd(true);
        System.out.println("Main : Ends");
    }
}


/**
 * 模拟生成一份报告
 */
class ReportGenerator implements Callable<String>{
    private String sender;
    private String title;

    public ReportGenerator(String sender, String title) {
        this.sender = sender;
        this.title = title;
    }


    @Override
    public String call() throws Exception {

        long duration = (long)(Math.random() * 10);
        System.out.printf("%s_%s : ReportGenerator : Generating a report during %d seconds \n",
                this.sender, this.title, duration);
        TimeUnit.SECONDS.sleep(duration);
        String retString = sender+":"+title;
        return retString;
    }
}

/**
 * 模拟请求报告
 */
class ReprotRequest implements Runnable{
    private String name;
    private CompletionService<String> completionService;

    public ReprotRequest(String name, CompletionService<String> completionService) {
        this.name = name;
        this.completionService = completionService;
    }

    @Override
    public void run() {
        //生成一份报告
        ReportGenerator reportGenerator = new ReportGenerator(name,"a new Report");
        completionService.submit(reportGenerator);
    }
}

/**
 * 模拟处理报告
 */
class ReportProcess implements Runnable{
    private CompletionService<String> completionService;
    private boolean end;

    public ReportProcess(CompletionService<String> completionService) {
        this.completionService = completionService;
        this.end = false;
    }

    public void setEnd(boolean end){
        this.end = end;
    }

    @Override
    public void run() {
        // end 为false，调用completionService接口的poll()方法，来获取下一个已经完成任务的Future对象
        // 这个任务是采用completionService完成的
        while(!end){
            try {
                Future<String> result = completionService.poll(20, TimeUnit.SECONDS);
                String report = result.get();
                System.out.println("ReportReceiver : Report Received : \n"  + report);
            } catch (InterruptedException e ) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }
}




