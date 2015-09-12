package com.zh.learn01;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/8/31.
 */
public class Demo29 {

    public static void main(String[] args) {
        //创建1000个产品
        ProductListGenerator productListGenerator = new ProductListGenerator();
        List<Product> products = productListGenerator.generatorProduct(30);
        TaskOfFork task = new TaskOfFork(products,0,products.size(),0.2);
        //创建ForkJoin对象
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        //调用execute执行任务
        forkJoinPool.execute(task);
        // 打印线程池的演变过程
        do {
            System.out.printf("Main : Thread Count : %d\n",
                    forkJoinPool.getActiveThreadCount());
            System.out.printf("Main : Thread Steal : %d\n",
                    forkJoinPool.getStealCount());
            System.out.printf("Main : Thread Parallelism : %d\n",
                    forkJoinPool.getParallelism());
            // 每隔5ms，打印参数值
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!task.isDone());

        forkJoinPool.shutdown();
        if (task.isCompletedNormally()) {
            System.out.println("Task is Completed Normally ...");
        }

        // 更新之后，所有的产品的期望价格是12元

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            if (product.getPrice() != 12) {
                System.out.printf("Product %s : %f\n", product.getName(),
                        product.getPrice());
            }
        }

        System.out.println("Main : End of the Program ...");

    }
}


class Product{
    private String name;
    private double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

/**
 * 用来生成随机产品列表
 */
class ProductListGenerator{

    public List<Product> generatorProduct(int size){
        List<Product> list = new ArrayList<Product>();
        for (int i = 0; i < size; i++) {
            Product product = new Product();
            product.setName("product_"+i);
            product.setPrice(10);
            list.add(product);
        }
        return list;
    }
}

/**
 * 实现对产品价格的更改
 */
class TaskOfFork extends RecursiveAction{
    private List<Product> products;
    private int first;
    private int last;
    private double increment;

    public TaskOfFork(List<Product> products, int first, int last, double increment) {
        this.products = products;
        this.first = first;
        this.last = last;
        this.increment = increment;
    }

    @Override
    protected void compute() {
        //一次只能更新少于10个产品的价格
        if(last - first < 10){
            updatePrice();
        }else{
            int middle = (first + last)/2;
            System.out.println("Pending task :" + getQueuedTaskCount());
            TaskOfFork task1 = new TaskOfFork(products,first,middle + 1,increment);
            TaskOfFork task2 = new TaskOfFork(products,middle + 1,last,increment);
            invokeAll(task1,task2);
        }

    }

    public void updatePrice(){
        for (int i = first; i < last; i++) {
            Product product = products.get(i);
            product.setPrice(product.getPrice() * (1 + increment));
        }
    }

}
