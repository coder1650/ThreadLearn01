package com.zh.learn01;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Administrator on 2015/9/7.
 */
public class Demo34 {
    public static void main(String[] args) {
        String str = "asdbbdadf";
        char[] chars = str.toCharArray();
        Arrays.sort(chars);
        System.out.println(chars);

        String[] strs = new String[]{"cgb","sgd","etd","sdcg"};
        Arrays.sort(strs, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return  -1;
            }
        });
        for (String ss : strs){
            System.out.println(ss);
        }

    }
}
