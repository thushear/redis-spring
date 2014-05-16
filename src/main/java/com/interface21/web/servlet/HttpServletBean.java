package com.interface21.web.servlet;

import com.interface21.beans.BeanWrapper;
import com.interface21.beans.BeanWrapperImpl;
import com.interface21.beans.BeansException;
import com.interface21.beans.PropertyValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.LinkedList;
import java.util.List;

/**
 * 说明:
 * User: kongming
 * Date: 14-5-16
 * Time: 上午10:12
 */
/**
 * Simple extension of javax.servlet.http.HttpServlet that treats its config
 * parameters as bean properties. A very handy superclass for any type of servlet.
 * Type conversion is automatic. It is also
 * possible for subclasses to specify required properties. This servlet leaves
 * request handling to subclasses, inheriting the default behaviour of HttpServlet.
 * <p/>This servlet superclass has no dependency on the WebApplicationContext.
 * However, it does use log4j, which must have been configured by another component.
 * @author Rod Johnson
 * @version $Revision: 1.1.1.1 $
 */
public class HttpServletBean extends HttpServlet {
    //---------------------------------------------------------------------
    // Instance data
    //---------------------------------------------------------------------
    /** Log4j Category to use for logging for this object. We obtain it
     * once in this class's constructor to avoid the need for repeated lookups.
     * Protected to avoid the need for method calls. Final to avoid tampering
     * by subclasses.
     */
    protected final  Logger logger = LoggerFactory.getLogger(getClass().getName());


    /** May be null. List of required properties (Strings) that must
     * be supplied as config parameters to this servlet.
     */
    private List requiredProperties = new LinkedList();


    /** Holds name info: useful for logging */
    private String identifier;

    //---------------------------------------------------------------------
    // Constructors
    //---------------------------------------------------------------------
    /** Construct a new ServletBean
     */
    public HttpServletBean() {
    }

    /** Subclasses can invoke this method to specify that this property
     * (which must match a property they expose) is mandatory,
     * and must be supplied as a config parameter.
     * @param property name of the required property
     */
    protected final void addRequiredProperty(String property) {
        requiredProperties.add(property);
    }

    //---------------------------------------------------------------------
    // Overridden methods
    //---------------------------------------------------------------------
    /** Map config parameters onto bean properties of this servlet, and
     * invoke subclass initialization.
     * @throws ServletException if bean properties are invalid (or required properties
     * are missing), or if subclass initialization fails.
     */
    @Override
    public final void init() throws ServletException {

        this.identifier = "Servlet with name '" + getServletConfig().getServletName() + "' ";
        logger.info(getIdentifier() + "entering init...");

        try {
            PropertyValues pvs = new ServletConfigPropertyValues(getServletConfig(), requiredProperties);
            BeanWrapper bw = new BeanWrapperImpl(this);
            bw.setPropertyValues(pvs);
            // Let subclasses do whatever initialization they like
            initServletBean();

        }
        catch (BeansException ex) {
            String mesg = getIdentifier() + ": error setting properties from ServletConfig";
            throw new ServletException(mesg, ex);
        }
        catch (Throwable t) {
            // Let subclasses throw unchecked exceptions
            String mesg = getIdentifier() + ": initialization error";
            throw new ServletException(mesg, t);
        }


    }

    /** Subclasses may override this to perform custom initialization.
     *  All bean properties of this servlet will have been set before this
     * method is invoked. This default implementation does nothing.
     * @throws ServletException if subclass initialization fails
     */
    protected void initServletBean() throws ServletException {
        logger.info(getIdentifier() + "NOP default implementation of initServletBean");
    }

    /** Return the name of this servlet:
     * handy to include in log messages. Subclasses may override it if
     * necessary to include additional information. Use like this:
     * <code>
     * Category.getInstance(getClass()).debug(getIdentifier() + "body of message");
     * </code>
     * @return the name of this servlet
     */
    protected String getIdentifier() {
        return this.identifier;
    }



}
