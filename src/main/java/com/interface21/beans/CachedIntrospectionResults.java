package com.interface21.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to cache PropertyDescriptor information for a Java class
 * <br/>Necessary as Introspector.getBeanInfo() will return a new deep copy 
 * of the BeanInfo every time we ask for it. We take the opportunity to
 * hash property descriptors by method name for fast lookup.
 * <br/>Package-visible.
 * @author  Rod Johnson
 * @since 05 May 2001
 * @version $Revision: 1.1.1.1 $
 */
final class CachedIntrospectionResults {

	//---------------------------------------------------------------------
	// Instance data
	//---------------------------------------------------------------------
	private BeanInfo beanInfo;
	
	/** Property desciptors keyed by property name */
	private HashMap propertyDescriptorMap;

	/** Property desciptors keyed by property name */
	private HashMap methodDescriptorMap;

	//---------------------------------------------------------------------
	// Constructors
	//---------------------------------------------------------------------

	/** Creates new CachedIntrospectionResults */
	private CachedIntrospectionResults(Class clazz) throws BeansException {
		try {
			logger.info("Getting BeanInfo for class " + clazz);
			beanInfo = Introspector.getBeanInfo(clazz);
			logger.info("caching BeanInfo for class " + clazz);

			propertyDescriptorMap = new HashMap();

			PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
			for (int i = 0; i < pds.length; i++) {
				logger.info("Found property [" + pds[i].getName() + "] of type [" + pds[i].getPropertyType()
						+ "]; editor=[" + pds[i].getPropertyEditorClass() + "]");
				propertyDescriptorMap.put(pds[i].getName(), pds[i]);
			}
			logger.info("Caching MethodDescriptors for class  " + clazz);

			methodDescriptorMap = new HashMap();
			// This call is slow so we do it once
			MethodDescriptor[] mds = beanInfo.getMethodDescriptors();
			for (int i = 0; i < mds.length; i++) {
				logger.info("Found method [" + mds[i].getName() + "] of type [" + mds[i].getMethod().getReturnType()
						+ "]");
				methodDescriptorMap.put(mds[i].getName(), mds[i]);
			}

		} catch (IntrospectionException e) {
			throw new FatalBeanException("cannot get beaninfo for object of class [" + clazz.getName() + "]", e);
		}

	}

	//---------------------------------------------------------------------
	// Public methods
	//---------------------------------------------------------------------
	public BeanInfo getBeanInfo() {
		return beanInfo;
	}

	public Class getBeanClass() {
		return beanInfo.getBeanDescriptor().getBeanClass();
	}

	//---------------------------------------------------------------------
	// Factory implementation
	//---------------------------------------------------------------------
	/** HashMap keyed by class containing CachedIntrospectionResults
	 * or ReflectionException
	 */
	private static HashMap $cache = new HashMap();
	/** Log4j Category to use for logging for this object. We obtain it
	 * once in this class's constructor to avoid the need for repeated lookups.
	 * Protected to avoid the need for method calls. Final to avoid tampering
	 * by subclasses.
	 */
	private static Logger logger = LoggerFactory.getLogger(CachedIntrospectionResults.class.getName());

	/** 
	 * We might use this from the EJB tier, so we don't want to use
	 * synchronization. Object references are atomic, so we
	 * can live with doing the occasional unnecessary lookup at startup only.
	 */
	public static CachedIntrospectionResults forClass(Class clazz) throws BeansException {
		Object o = $cache.get(clazz);
		if (o == null) {
			try {
				o = new CachedIntrospectionResults(clazz);
			} catch (BeansException e) {
				o = e;
			}
			$cache.put(clazz, o);
		} else {
			logger.info("using cached introspection results for class" + clazz);
		}

		// o is now an exception or CachedIntrospectionResults

		// We already have data for this class in the cache
		if (o instanceof BeansException)
			throw (BeansException) o;
		return (CachedIntrospectionResults) o;
	}

	public PropertyDescriptor getPropertyDescriptor(String propertyName) throws BeansException {
		PropertyDescriptor pd = (PropertyDescriptor) propertyDescriptorMap.get(propertyName);
		if (pd == null)
			throw new FatalBeanException("No property [" + propertyName + "] in class [" + getBeanClass() + "]", null);
		return pd;
	}

	public MethodDescriptor getMethodDescriptor(String methodName) throws BeansException {
		MethodDescriptor md = (MethodDescriptor) methodDescriptorMap.get(methodName);
		if (md == null)
			throw new FatalBeanException("No method [" + methodName + "] in class [" + getBeanClass() + "]", null);
		return md;
	}

}
