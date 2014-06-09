package net.shopin.perf4j;

import org.apache.log4j.Logger;
import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

/**
 * 说明:
 * User: kongming
 * Date: 14-5-22
 * Time: 下午10:58
 */
public class Perf4JUtil {





    /**
     * @Methods Name main
     * @Create In 2013-10-12 By kongm
     * @param args
     *            void
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 5; i++) {
            // 创建LoggingStopWatch开始计时,构造器中包含tag,标记了被计时的代码块
            // 通常需要实例化LoggingStopWatch的一个子类例如Log4JStopWatch 或者
            // CommonsLogStopWatch
            StopWatch stopWatch = new LoggingStopWatch("codeblock1");
            // 执行代码块 延迟0-1秒
            Thread.sleep((long) (Math.random() * 1000L));
            // 停表并记录,调用stop()或lap()方法LoggingStopWatch自动的记录执行时间
            stopWatch.stop();
            // 代码执行结果 进入system.err流
                        /*
                         * start[1381564569686] time[709] tag[codeblock1]
                         * start[1381564570396] time[769] tag[codeblock1]
                         * start[1381564571166] time[13] tag[codeblock1]
                         * start[1381564571180] time[775] tag[codeblock1]
                         * start[1381564571956] time[455] tag[codeblock1]
                         */
        }

        // 此外,在stopWatch的tag中可以指定一些可选信息,这些tag或者message
        //可以在StopWatch的构造器或者在stop方法中写明,在程序执行时tag可变.
        StopWatch stopWatch = new LoggingStopWatch();
        for(int i = 0;i < 10;i++){
            try {
                //用来做计时测试的伪码
                long sleepTime = (long) (Math.random() * 1000L);
                Thread.sleep(sleepTime);
                if(sleepTime > 500L){
                    throw new Exception("Throwing exception");
                }
                stopWatch.stop("codeBlock2.success","sleep time was < 500ms");
            } catch (Exception e) {
                stopWatch.stop("codeblock2.failure","Exception is : " + e);
            }
        }


        Logger rootLogger = Logger.getRootLogger();

        for(int i=0;i < 10;i++){
            //默认的Log4JStopWatch会使用名为org.perf4j.TimeLogger的logger
            stopWatch = new Log4JStopWatch();
            //demo dummy code
            Thread.sleep((long) (Math.random() * 1000L));
            rootLogger.info("Normal logging message only go to the console");
            //调用lap方法停止先前代码的计时,发送message到log4j的Logger并开始
            //下个代码块的计时
            stopWatch.lap("firstblock");
            Thread.sleep((long) (Math.random() * 2000L));
            stopWatch.stop("secondblock");
        }

        //上述代码日志输出
                /*INFO: start[1230493236109] time[447] tag[codeBlock2.success] message[Sleep time was < 500 ms]
                                INFO: start[1230493236719] time[567] tag[codeBlock2.failure] message[Exception was: java.lang.Exception: Throwing exception]
                                INFO: start[1230493237286] time[986] tag[codeBlock2.failure] message[Exception was: java.lang.Exception: Throwing exception]
                                INFO: start[1230493238273] time[194] tag[codeBlock2.success] message[Sleep time was < 500 ms]
                                INFO: start[1230493238467] time[463] tag[codeBlock2.success] message[Sleep time was < 500 ms]
                                INFO: start[1230493238930] time[310] tag[codeBlock2.success] message[Sleep time was < 500 ms]
                                INFO: start[1230493239241] time[610] tag[codeBlock2.failure] message[Exception was: java.lang.Exception: Throwing exception]
                                INFO: start[1230493239852] time[84] tag[codeBlock2.success] message[Sleep time was < 500 ms]
                                INFO: start[1230493239937] time[30] tag[codeBlock2.success] message[Sleep time was < 500 ms]
                                INFO: start[1230493239968] time[852] tag[codeBlock2.failure] message[Exception was: java.lang.Exception: Throwing
*/
    }



}
