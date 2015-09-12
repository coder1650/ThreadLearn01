package com.zh.learn01;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/9/1.
 */
public class Demo31 {

    public static void main(String[] args) {
        GeneratorPerson generatorPerson = new GeneratorPerson(100);
        List<PersonOfFJ> list = generatorPerson.generatorPersonList();
        IncrementAge incrementAge = new IncrementAge(0,list.size(),list);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.execute(incrementAge);
        //打印线程池的演变过程
        do{
            System.out.println("main thread count:"+forkJoinPool.getActiveThreadCount());
            System.out.println("main thread steal:"+forkJoinPool.getStealCount());
            System.out.println("mai thread parallelism:"+forkJoinPool.getParallelism());
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while(!incrementAge.isDone());
        //打印所有人员的年龄
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i+"_p.age:"+list.get(i).getAge());
        }

    }

}


class PersonOfFJ{
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

class GeneratorPerson{
    private int size;

    public GeneratorPerson(int size) {
        this.size = size;
    }

    public List<PersonOfFJ> generatorPersonList(){
        List<PersonOfFJ> list = new ArrayList<PersonOfFJ>();
        for (int i = 0; i < size; i++) {
            PersonOfFJ p = new PersonOfFJ();
            p.setName("p_"+i);
            p.setName("12");
            list.add(p);
        }
        return list;
    }
}

class IncrementAge extends RecursiveAction {
    private final static int MAX_PROCESS = 10;
    private int first;
    private int last;
    private List<PersonOfFJ> personOfFJs;

    public IncrementAge(int first, int last, List<PersonOfFJ> personOfFJs) {
        this.first = first;
        this.last = last;
        this.personOfFJs = personOfFJs;
    }

    @Override
    protected void compute() {

        if((last - first) < MAX_PROCESS){
            imcremAge();
        }else{
            int middle = (first + last)/2;
            System.out.println(Thread.currentThread().getName()+"first:"+first+",last"+last+":middle="+middle);
            IncrementAge left = new IncrementAge(first,middle+1,personOfFJs);
            IncrementAge right = new IncrementAge(middle+1,last,personOfFJs);
            invokeAll(left,right);
        }

    }
    public void imcremAge(){
        for (int i = first; i < last; i++) {
            personOfFJs.get(i).setAge(15);
        }
    }
}