package com.rich.mo.multitread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class ForkJoinPollTest {

    ForkJoinPool forkJoinPool = new ForkJoinPool();

    public static void main(String[] args) throws Exception{

       new ForkJoinPollTest().test();
    }

    public void test() throws  Exception{

        int init = 1;
        Integer result=0;
        List<ForkJoinTask<Integer>> list = new ArrayList<ForkJoinTask<Integer>>();

        for(int k=0;k<10;k++){

            /**
             * 先提交任务，并行执行，保存好临时结果，
             * 最后统一获取解析
             * 可并发执行任务，最后才关心结果输出
             */
            ForkJoinTask<Integer> submit = forkJoinPool.submit(new workThread(init));
            list.add(submit);
            init+=100;
        }

        System.out.println("提交所有任务，等待结果");


        for(ForkJoinTask<Integer> forkJoinTask : list){

            Integer integer = forkJoinTask.get();
            result = result+integer;

        }
        System.out.println("总共累计为:"+result);

        forkJoinPool.shutdown();


    }

    public void test2()throws Exception{

        int init = 1;
        Integer result=0;
        List<Integer> list = new ArrayList<Integer>();

        for(int k=0;k<10;k++){

            /**
             * 直接阻塞等待结果，不利于并发
             * 但可用于天然需要顺序执行的任务场景
             */
           Integer invoke = forkJoinPool.invoke(new workThread(init));
            list.add(invoke);
            init+=100;
        }

        System.out.println("提交所有任务，等待结果....");

        for(Integer integer : list){

            result = result+integer;

        }
        System.out.println("总共累计为:"+result);

        forkJoinPool.shutdown();

    }

    /**
     * 根据业务场景选择：
     * 继承RecursiveTask<Integer>则是线程可以有返回结果；
     * 继承RecursiveAction则线程无返回结果；
     */
    class workThread extends RecursiveTask<Integer>{

        int start_num;
        public workThread(int start_num){
            this.start_num=start_num;

        }

        protected Integer compute() {
            System.out.println(Thread.currentThread().getName()+"初始化："+start_num);

            try {
                System.out.println("休眠5秒");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i=0;i<100;i++){
                start_num++;
            }
            System.out.println(Thread.currentThread().getName()+"累计结果："+start_num);

            return start_num;
        }
    }
}
