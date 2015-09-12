package com.zh.learn01;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Created by Administrator on 2015/9/6.
 */
public class Demo33 {
    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        FoldorProcess foldorProcess = new FoldorProcess("c:"+File.separator+"windows","log");
        forkJoinPool.execute(foldorProcess);
        forkJoinPool.shutdown();
        List<String> result = foldorProcess.join();
        System.out.println(result.size());
    }
}


class FoldorProcess extends RecursiveTask<List<String>>{
    private String path;
    private String extension;

    public FoldorProcess(String path, String extension) {
        this.path = path;
        this.extension = extension;
    }

    @Override
    protected List<String> compute() {
        List<String> listFile = new ArrayList<String>();
        List<FoldorProcess> foldorProcesses = new ArrayList<FoldorProcess>();
        File file = new File(path);
        File[] contentFiles = file.listFiles();
        if(contentFiles != null){
            for (int i = 0; i < contentFiles.length; i++) {
                if(contentFiles[i].isDirectory()){
                    FoldorProcess foldorProcess = new FoldorProcess(contentFiles[i].getAbsolutePath(),extension);
                    //调用fork方法异步执行
                    foldorProcess.fork();
                    foldorProcesses.add(foldorProcess);
                }else{
                    if(contentFiles[i].getName().toLowerCase().endsWith(extension)){
                        listFile.add(contentFiles[i].getAbsolutePath());
                    }
                }
            }

            for(FoldorProcess foldorPro : foldorProcesses){
                listFile.addAll(foldorPro.join());
            }

        }


        return listFile;
    }
}