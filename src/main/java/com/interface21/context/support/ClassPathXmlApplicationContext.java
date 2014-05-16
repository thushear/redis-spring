package com.interface21.context.support;

/**
 * 说明:
 * User: kongming
 * Date: 14-5-13
 * Time: 下午5:55
 */

import com.interface21.context.ApplicationContextException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Standalone XML application context useful for test harnesses.
 * @author Rod Johnson
 */
public class ClassPathXmlApplicationContext extends XmlApplicationContextSupport   {

    public ClassPathXmlApplicationContext(String paths) throws ApplicationContextException, IOException {
        super(paths);
    }

    public ClassPathXmlApplicationContext(String[] paths) throws ApplicationContextException, IOException {
        super(paths);
    }


    @Override
    protected InputStream getInputStream(String path) throws IOException {
        return getClass().getResourceAsStream(path);  //To change body of implemented methods use File | Settings | File Templates.
    }


}
