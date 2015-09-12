package com.zh.learn01;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType;

import javax.xml.transform.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2015/8/24.
 */
public class Demo22 {

    public static void main(String[] args){
        ReturnRandom returnRandom1 = new ReturnRandom();
        ReturnRandom returnRandom2 = new ReturnRandom();
        ReturnRandom returnRandom3 = new ReturnRandom();
        List<ReturnRandom> threadList = new ArrayList<ReturnRandom>();
        threadList.add(returnRandom1);
        threadList.add(returnRandom2);
        threadList.add(returnRandom3);
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        List<Future<Integer>> resultList;
        try {
            resultList = threadPoolExecutor.invokeAll(threadList);
            for(Future result : resultList){
               Integer in = (Integer) result.get();
                System.out.println("resutl:"+in);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

class ReturnRandom implements Callable<Integer>{

    @Override
    public Integer call() throws Exception {
        Random random = new Random();
        return random.nextInt(100);
    }
}
