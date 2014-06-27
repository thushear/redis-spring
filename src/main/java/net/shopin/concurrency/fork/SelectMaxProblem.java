package net.shopin.concurrency.fork;

/**
 * 说明: 利用fork-join算法
 *       在大数组中找最大的数
 * User: kongming
 * Date: 14-6-27
 * Time: 上午11:18
 */
public class SelectMaxProblem {

    private final int[] numbers;
    private final int start;
    private final int end;
    public final int size;


    public SelectMaxProblem(int[] numbers, int start, int end) {
        this.end = end;
        this.start = start;
        this.numbers = numbers;
        this.size =  end - start;
    }

    /**
     *  顺序化遍历解决
      * @return
     */
    public int solveSequentially(){
        int max = Integer.MIN_VALUE;
        for (int i = start;i < end;i++){
            int n = numbers[i];
            if(n>max)
                max = n;
        }
        System.out.println("[" + Thread.currentThread().getName() + "]Max=" + max + ",start ~ end :" + start + " ~ " + end);
        return max;
    }


    /**
     * 拆解大任务为小任务
     * @param subStart
     * @param subEnd
     * @return
     */
    public SelectMaxProblem subProblem(int subStart,int subEnd){
        System.out.println("[" + Thread.currentThread().getName() + "]" + subStart + " : " + subEnd);
        return new SelectMaxProblem(numbers,start+subStart,start+subEnd);
    }

}
