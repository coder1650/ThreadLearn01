package com.zh.learn01;

import java.io.*;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2015/8/25.
 */
public class Demo27 {

    public static void main(String[] args) {
        FileSource fileSource = new FileSource(10);
        ReaderFromFile readerFromFile = new ReaderFromFile(fileSource);
        //开启一个线程读取文件中的内容
        Thread readThread = new Thread(readerFromFile);
        readThread.start();
        //开启三个线程把读取到的内容输出到控制台
        for (int i = 0; i < 3; i++) {
            new Thread(new WriterToControl(fileSource)).start();
        }

    }
}

class FileSource{
    private LinkedList<String> context;
    private int maxSize;
    private ReentrantLock reentrantLock;
    private Condition waitSpeace;
    private Condition waitInsert;
    private boolean isHasContent = true;


    public FileSource(int maxSize) {
        this.context = new LinkedList<String>();
        this.maxSize = maxSize;
        this.reentrantLock = new ReentrantLock();
        this.waitInsert = reentrantLock.newCondition();
        this.waitSpeace = reentrantLock.newCondition();
    }

    public void readLineFromFileToContext(){
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader("D:\\charge.log");
            bufferedReader = new BufferedReader(fileReader);
            String line = null;
            reentrantLock.lock();
            while ((line = bufferedReader.readLine()) != null){
                System.out.println(Thread.currentThread().getName()+"--read line:"+line);
                try {
                    while (context.size() == maxSize){
                        waitSpeace.await();
                    }
                    //把读到的数据放入集合末尾
                    context.offer(line);
                    waitInsert.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            reentrantLock.unlock();
            isHasContent = false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void outToControlFromContext(){
        reentrantLock.lock();
        try {
            //当文件中的内容没有被读完的时候，context为空时，需要等候context被填充内容，否则，不需要阻塞线程
            while(context.isEmpty() && isHasContent()){
                waitInsert.await();
                System.out.println(Thread.currentThread().getName()+"awaiting...");
            }
            String line = context.poll();
            waitSpeace.signalAll();
            System.out.println(Thread.currentThread().getName()+"-- out "+line+"context.size:"+context.size()+",isHasContext:"+isHasContent());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean isHasContent(){
        if(isHasContent || !context.isEmpty()){
            return true;
        }else{
            return false;
        }
    }

}

class ReaderFromFile implements Runnable{
    private FileSource fileSource;

    public ReaderFromFile(FileSource fileSource) {
        this.fileSource = fileSource;
    }

    @Override
    public void run() {
        fileSource.readLineFromFileToContext();
        System.out.println("reader is out");
    }
}

class  WriterToControl implements Runnable{
    private FileSource fileSource;

    public WriterToControl(FileSource fileSource) {
        this.fileSource = fileSource;
    }

    @Override
    public void run() {
        while(fileSource.isHasContent()){
            fileSource.outToControlFromContext();

        }
        System.out.println(Thread.currentThread().getName()+"--writer is out");
    }
}
