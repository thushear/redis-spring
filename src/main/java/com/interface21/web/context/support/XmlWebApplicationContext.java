package com.interface21.web.context.support;

import com.interface21.context.ApplicationContext;
import com.interface21.context.support.AbstractXmlApplicationContext;
import com.interface21.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 说明:
 * User: kongming
 * Date: 14-5-13
 * Time: 下午8:27
 */
/**
 * Root WebApplicationContext: for an entire web application,
 * associated with a ServletContext.
 * @author  Rod Johnson
 * @version $Revision: 1.1.1.1 $
 */
public class XmlWebApplicationContext extends AbstractXmlApplicationContext implements WebApplicationContext {

    public static final String CONFIG_URL = "configUrl";


    //---------------------------------------------------------------------
    // Instance data
    //---------------------------------------------------------------------
    /** URL from which the configuration was loaded */
    private String url;

    private ServletContext servletContext;

    //---------------------------------------------------------------------
    // Constructors
    //---------------------------------------------------------------------
    public XmlWebApplicationContext() {
        setDisplayName("Root WebApplicationContext");
    }


    //---------------------------------------------------------------------
    // Implementation of superclass abstract methods
    //---------------------------------------------------------------------
    /**
     * Open and return the input stream for the bean factory for this namespace.
     * If namespace is null, return the input stream for the default bean factory.
     * @throw IOException if the required XML document isn't found
     */
    @Override
    protected InputStream getInputStreamForBeanFactory() throws IOException {
        String xmlFile = getURL();
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    /** Create a new child WebApplicationContext
     */
    public XmlWebApplicationContext(ApplicationContext parent, String namespace) {
        super(parent);
        this.url = namespace;
        setDisplayName("WebApplicationContext for namespace '" + namespace + "'");
    }

    @Override
    public void setServletContext(ServletContext servletContext) throws ServletException {
        this.servletContext = servletContext;
        if (this.getParent() == null) {
            String configURL = servletContext.getInitParameter(CONFIG_URL);
            if (configURL == null) {
                throw new ServletException("Cannot initialize context of " + getClass() + ": missing required context param with name '" + CONFIG_URL + "'");
            }
            this.url = configURL;
        } else {
            this.url = "/WEB-INF/" + url + ".xml";
        }
        refresh();
        if (this.getParent() == null) {

        }

    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;  //To change body of implemented methods use File | Settings | File Templates.
    }

    //---------------------------------------------------------------------
    // Implementation of WebApplicationConfig
    //---------------------------------------------------------------------


    protected String getURL() {
        return url;
    }


    public String toString() {
        StringBuffer sb = new StringBuffer( super.toString() + "; ");
        sb.append("config URL='" + url + "'; ");
        return sb.toString();
    }





}
