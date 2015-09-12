package com.zh.learn01;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/9/1.
 */
public class Demo32 {
    public static void main(String[] args) {
        Path path = Paths.get("D:/");
        //List<Path> roots = (List<Path>) FileSystems.getDefault().getRootDirectories();
        List<Path> result = new ArrayList<Path>();
        List<FindFileTask> findFileTaskList = new ArrayList<FindFileTask>();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        FindFileTask fft = new FindFileTask(path,"log");
        forkJoinPool.execute(fft);
        findFileTaskList.add(fft);
        System.out.println("正在处理");
        while(!isDone(findFileTaskList)){
            System.out.println(".");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for(FindFileTask f:findFileTaskList){
            try {
                result.addAll((Collection<? extends Path>) f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }
        for(Path pp:result) {
            System.out.println(pp);
        }

    }
    public static boolean isDone(List<FindFileTask> list){
        boolean isDone = true;
        for(FindFileTask f : list){
            if(!f.isDone()){
                isDone = false;
                break;
            }
        }
        return isDone;
    }
}


class FindFileTask extends RecursiveTask<Path>{
    private Path path;
    private String fileExtention;

    FindFileTask(Path path, String fileExtention) {
        this.path = path;
        this.fileExtention = fileExtention;
    }

    @Override
    protected Path compute() {
        List<Path> pathList = new ArrayList<Path>();
        try {
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path);
            List<FindFileTask> taskList = new ArrayList<FindFileTask>();
            for(Path path : directoryStream){
                if(Files.isDirectory(path)){
                    FindFileTask f = new FindFileTask(path,fileExtention);
                    f.fork();
                    taskList.add(f);//f这个子任务执行完后会有返回结果
                }else if(Files.isRegularFile(path)){
                    if(path.toString().toLowerCase().endsWith("."+fileExtention)){
                        pathList.add(path);
                    }
                }
            }

            //把子任务执行完成后得到的结果放入pathList集合中
            for(FindFileTask findFileTask : taskList){
                pathList.add(findFileTask.join());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
