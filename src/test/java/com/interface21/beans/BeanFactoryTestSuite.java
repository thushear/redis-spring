package com.interface21.beans;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.InitializingBean;
import com.interface21.beans.factory.ListableBeanFactory;

/**
 * A test suite defined as a bean in a ListableBeanFactory
 * Created with IntelliJ IDEA.
 * User: kongming
 * Date: 14-5-12
 * Time: 下午7:01
 * To change this template use File | Settings | File Templates.
 */

public class BeanFactoryTestSuite extends  AbstractTestSuite implements InitializingBean{
    private String[] testNames;

    private BeanFactory beanFactory;


    @Override
    protected Test[] createTests() {
        System.out.println("Number of worker threads = " + getThreads());
        Test[] tests = new Test[getThreads()];

        int index = 0;
        for (int i = 0; i < getThreads(); ) {
            Test test = (Test) this.beanFactory.getBean(testNames[index]);
            for (int j = 0; j < test.getInstances() && i < getThreads(); j++) {
                tests[i] = (Test) this.beanFactory.getBean(testNames[index]);
                //System.out.println("New test " + tests[j].getClass());
                ++i;
            }
            ++index;
            if (index == testNames.length)
                index = 0;

        }
        return tests;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        if (getPasses() == 0)
        throw new Exception("Must set passes property");
        if (getThreads() == 0)
            throw new Exception("Must set threads property");
        if (getReportInterval() == 0)
            throw new Exception("Must set reportIntervalSeconds or reportInterval (ms) property");
    }


    public void init(ListableBeanFactory lbf){
        System.out.println("init");
        this.beanFactory = lbf;
        this.testNames = lbf.getBeanDefinitionNames(Test.class);
        if (testNames.length == 0)
            throw new RuntimeException("No test beans defined");


    }

}
