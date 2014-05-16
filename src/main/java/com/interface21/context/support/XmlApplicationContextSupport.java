package com.interface21.context.support;

/**
 * 说明:
 * User: kongming
 * Date: 14-5-13
 * Time: 下午5:23
 */
import com.interface21.context.ApplicationContextException;
import com.interface21.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Standalone XML application context useful for test harnesses.
 * Does CSV paths:
 * parent first
 * @author Rod Johnson
 */
public abstract class XmlApplicationContextSupport extends AbstractXmlApplicationContext{

    private InputStream is;

    public XmlApplicationContextSupport(String paths) throws ApplicationContextException, IOException {
        this(StringUtils.commaDelimitedListToStringArray(paths));
    }


    public XmlApplicationContextSupport(String[] paths) throws ApplicationContextException, IOException{
        if (paths.length == 0)
            throw new ApplicationContextException("must have paths");
        String thisPath = paths[paths.length - 1];
        logger.info("Trying to open XML application context file '" + thisPath + "'");
        is = getInputStream(thisPath);
        logger.info("Opened XML application context file '" + thisPath + "' OK");
        // Recurse
        if (paths.length > 1) {
            // There were parent(s)
            String[] parentPaths = new String[paths.length - 1];
            System.arraycopy(paths, 0, parentPaths, 0, paths.length - 1);
            logger.info("Setting parent context for paths: [" + StringUtils.arrayToDelimitedString(parentPaths, ","));
            AbstractApplicationContext parent = new FileSystemXmlApplicationContext(parentPaths);
            setParent(parent);
        }
        refresh();
    }



    protected abstract InputStream getInputStream(String path) throws IOException;


    /**
     */
    protected final InputStream getInputStreamForBeanFactory() throws IOException {
        return is;
    }


}
