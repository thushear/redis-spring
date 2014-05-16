package com.interface21.web.servlet;

import com.interface21.beans.MutablePropertyValues;
import com.interface21.beans.PropertyValue;
import com.interface21.beans.PropertyValues;
import com.interface21.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 说明:
 * User: kongming
 * Date: 14-5-16
 * Time: 上午10:20
 */
/**
 * PropertyValues implementation created from ServetConfig parameters.
 * <br/>This class is immutable once initialized.
 * @author Rod Johnson
 * @version $RevisionId$
 */
class ServletConfigPropertyValues implements PropertyValues {

    private static final Logger logger = LoggerFactory.getLogger(ServletConfigPropertyValues.class.getName());


    /** PropertyValues delegate. We use delegation rather than simply subclass
     * MutablePropertyValues as we don't want to expose MutablePropertyValues's
     * update methods. This class is immutable once initialized.
     */
    private MutablePropertyValues mutablePropertyValues;


    /** Creates new PropertyValues object
     * @param config ServletConfig we'll use to take PropertyValues from
     * @throws ServletException should never be thrown from this method
     */
    public ServletConfigPropertyValues(ServletConfig config) throws ServletException {
        this(config, null);
    }



    /** Creates new PropertyValues object
     * @param config ServletConfig we'll use to take PropertyValues from
     * @param requiredProperties array of property names we need, where
     * we can't accept default values
     * @throws ServletException if any required properties are missing
     */
    public ServletConfigPropertyValues(ServletConfig config, List requiredProperties) throws ServletException {
        // Ensure we have a deep copy
        List missingProps = (requiredProperties == null) ? new ArrayList(0) : new ArrayList(requiredProperties);
        mutablePropertyValues = new MutablePropertyValues();
        Enumeration enum1 = config.getInitParameterNames();
        while (enum1.hasMoreElements()){
            String property = (String) enum1.nextElement();
            Object value = config.getInitParameter(property);
            mutablePropertyValues.addPropertyValue(new PropertyValue(property, value));
            // Check it off
            missingProps.remove(property);

        }
        // Fail if we are still missing properties

        if (missingProps.size() > 0) {
            throw new ServletException("Initialization from ServletConfig for servlet '" + config.getServletName() + "' failed: the following required properties were missing -- (" +
                    StringUtils.collectionToDelimitedString(missingProps, ", ") + ")");
        }
        logger.info("Found PropertyValues in ServletConfig: " + mutablePropertyValues);
    }





    /** Return an array of the PropertyValue objects
     * held in this object.
     * @return an array of the PropertyValue objects
     * held in this object.
     */
    @Override
    public PropertyValue[] getPropertyValues() {
        return mutablePropertyValues.getPropertyValues();
    }

    @Override
    public boolean contains(String propertyName) {
        return mutablePropertyValues.contains(propertyName);
    }

    @Override
    public PropertyValue getPropertyValue(String propertyName) {
        return mutablePropertyValues.getPropertyValue(propertyName);
    }
}
