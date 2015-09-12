package com.zh.learn01;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2015/8/24.
 */
public class Demo21 {


    public static void main(String[] args){
        String userName = "lisi";
        String password = "123456";
        UserValidator userValidator1 = new UserValidator("zhangsan");
        UserValidator userValidator2 = new UserValidator("wangwu");

        TestValidator testValidator1 = new TestValidator(userValidator1,userName,password);
        TestValidator testValidator2 = new TestValidator(userValidator2,userName,password);

        List<TestValidator> validatorList = new ArrayList<TestValidator>();
        validatorList.add(testValidator1);
        validatorList.add(testValidator2);
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        String exeResult;
        try {
            exeResult = threadPoolExecutor.invokeAny(validatorList);
            System.out.println("Main : Result :" + exeResult);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        threadPoolExecutor.shutdown();
        System.out.println("Main : End of the Execution...");

    }

}

class UserValidator{
    private String userName;
    private String password;

    public UserValidator(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public  boolean validator(String userName,String password){
        Random random = new Random();
        long duration = (long)(Math.random()*10);
        System.out.printf("Validator %s:Validating user during %d seconds\n", this.userName, duration);
        try {
            //sleep 2s模拟用户登录
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //随机返回boolean，模拟验证结果
        return random.nextBoolean();
    }
}

class TestValidator implements Callable<String>{

    private UserValidator userValidator;

    private String userName;
    private String password;

    public TestValidator(UserValidator userValidator, String userName, String password) {
        this.userValidator = userValidator;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public String call() throws Exception {
        if(!userValidator.validator(userName,password)){
            System.out.printf("%s is not found", userName);
            throw new Exception("user not found");
        }
        System.out.printf("%s is found",userName);
        return userValidator.getUserName();
    }
}
