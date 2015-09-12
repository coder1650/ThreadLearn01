package com.zh.learn02;

/**
 * Created by Administrator on 2015/9/9.
 * ThreadLocal  线程局部变量
 */
public class Demo04 {
    public static void main(String[] args) {
        //SessionContent sessionContent = new SessionContent();
        Thread t1 = new Thread(new GetSession1());
        Thread t2 = new Thread(new GetSession1());
        t1.start();
        t2.start();
    }

}

class SessionContent{
    private  int sessionId = 1;

    /**
     * 每获得一次session,sessionId自动加1
     * @return
     */
    public int getSessionId() {
        return sessionId++;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }
}

class GetSession1 implements Runnable{

    //private SessionContent session;
    //ThreadLocal的实现原理是:ThreadLocal中有一个内部类ThreadLocalMap,该map的键是当前ThreadLocal对象实例，值为ThreadLocal要保存的
    //变量副本value
    private ThreadLocal<SessionContent> session = new ThreadLocal<SessionContent>(){
        @Override
        protected SessionContent initialValue() {
            return new SessionContent();
        }
    };

   /* public GetSession1(SessionContent session) {
        this.session = session;
    }*/

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            int sessionId = session.get().getSessionId();
            System.out.println(Thread.currentThread().getName()+"_sessionId:"+sessionId);
        }
    }
}


