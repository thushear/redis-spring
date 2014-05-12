package com.interface21.beans;

/**
 * Created with IntelliJ IDEA.
 * User: kongming
 * Date: 14-5-12
 * Time: 上午9:33
 * To change this template use File | Settings | File Templates.
 */

import com.interface21.util.ResponseTimeMonitor;

/**
 * run() from Runnable interface actually runs the test
 * @author  Rod Johnson
 * @since February 9, 2001
 */
public interface Test extends Runnable {


    void setName(String name);

    //init
    void  setTestSuite(AbstractTestSuite ts);

    void setInstances(int count);

    int getInstances();

    void setFixture(Object context);

    // These methods can be queried during work

    int getTestsCompletedCount();
    String getName();
    int getErrorCount();
    boolean isComplete();
    int getPasses();


    void setLongReports(boolean flag);

    long getMaxPause();
    void setMaxPause(long p);

    void setPasses(int count);

    double getTestsPerSecondCount();

    long getElapsedTime();
    long getTotalWorkingTime();

    long getTotalPauseTime();
    //ResponseTimeMonitoringAuditedResource getTargetResponse();

    ResponseTimeMonitor getTargetResponse();

    void reset();

}
