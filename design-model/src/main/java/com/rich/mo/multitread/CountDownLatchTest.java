package com.rich.mo.multitread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CountDownLatch 使用例子
 * 1、CountDownLatch:A synchronization aid that allows one or more threads to wait until a set of operations being performed in other threads completes.
 *
 * 　　　　大致意思：也就是说主线程在等待所有其它的子线程完成后再往下执行
 *
 *
 *
 * 　　　　2、构造函数：CountDownLatch(int count)//初始化count数目的同步计数器，只有当同步计数器为0，主线程才会向下执行
 * 　　　　　 主要方法：void	await()//当前线程等待计数器为0
 *       　　　　　　　　 boolean	await(long timeout, TimeUnit unit)//与上面的方法不同，它加了一个时间限制。
 *      　　　　　　　　 void	countDown()//计数器减1
 *       　　　　　　　　long	getCount()//获取计数器的值
 *
 *       　　3.它的内部有一个辅助的内部类：sync.
 */
public class CountDownLatchTest {
    private  ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {

        new CountDownLatchTest().test();


    }
    public  void test(){
        int tasks = 6;
        int init_num = 1;
        CountDownLatch countDownLatch = new CountDownLatch(tasks);
        for(int i=0;i<tasks;i++){
            System.out.println("外部初始值:"+init_num);
            executorService.execute(new workThead(init_num,countDownLatch));
            init_num+=100;
        }

        try {
            System.out.println("countDownLatch.getCount()="+countDownLatch.getCount());
            //防止等待超时则需要使用带有时间参数的await(xx,xx);
            //根据业务场景选择使用
            countDownLatch.await();
            System.out.println("======所有线程执行完毕，继续执行=====");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.shutdown();

    }

    /**
     * 工作线程：
     * 执行同类任务的工作
     */
    class workThead implements Runnable{

        private int start_num;
        CountDownLatch countDownLatch;

        public  workThead(int start_num, CountDownLatch countDownLatch){
            this.start_num = start_num;
            this.countDownLatch = countDownLatch;
        }

        public void run() {
            System.out.println("countDownLatch.getCount()="+countDownLatch.getCount());
            System.out.println(Thread.currentThread().getName()+":初始数据="+start_num);

            for(int i=0;i<100;i++){
                start_num++;
            }

            System.out.println(Thread.currentThread().getName()+":累计结果为="+start_num);


            System.out.println("休眠5秒。。。。");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            countDownLatch.countDown();




        }
    }
}
