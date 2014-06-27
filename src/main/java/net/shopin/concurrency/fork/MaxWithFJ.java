package net.shopin.concurrency.fork;

import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * 说明: Fork-Join框架使用
 * User: kongming
 * Date: 14-6-27
 * Time: 上午11:25
 */
public class MaxWithFJ extends RecursiveAction
{
    //判断是否开启fork-join的阀值
    private final int threshold;
    //数组求大数问题
    private final SelectMaxProblem problem;
    //
    public int result;

    public MaxWithFJ(int threshold, SelectMaxProblem problem) {
        this.threshold = threshold;
        this.problem = problem;
    }

    @Override
    protected void compute() {
       if(problem.size < threshold)
           result = problem.solveSequentially();
       else {
           int midpoint = problem.size / 2;
           MaxWithFJ left = new MaxWithFJ(threshold,problem.subProblem(0,midpoint));
           MaxWithFJ right = new MaxWithFJ(threshold,problem.subProblem(midpoint+1,problem.size));
           invokeAll(left,right); // coInvoke(leftForkJoin, rightForkJoin);
           result = Math.max(left.result, right.result);
       }

    }

    /**
     * 500k数组取大数 算法
     * 运行结果:
     * 1.顺序执行 阀值 500k  500k  50ms
     *
     *
     * @param args
     */
    public static void main(String[] args) {
        final int COUNT = 500000;

        Random random = new Random();
        int[] numbers = new int[COUNT];
        for (int i = 0;i < COUNT;i++){
            //numbers[i] =  random.nextInt(Integer.MAX_VALUE);
            numbers[i] = i;
            //System.out.print(numbers[i] + "  ");
        }
        // int numbers[] = {4,2,66,154,55};
        SelectMaxProblem problem = new SelectMaxProblem(numbers,0,numbers.length);
        int threshold = COUNT/100;
        int nthreads = 4;
        MaxWithFJ max = new MaxWithFJ(threshold,problem);
        ForkJoinPool forkJoinPool = new ForkJoinPool(nthreads);
        StopWatch watch = new LoggingStopWatch("max num problem");
        forkJoinPool.invoke(max);
        System.out.println(max.result);
        watch.stop();
    }




}
