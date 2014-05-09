package com.interface21.beans.factory.support;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.interface21.beans.BeansException;
import com.interface21.beans.factory.ListableBeanFactory;
import com.interface21.beans.factory.NoSuchBeanDefinitionException;
import com.interface21.util.StringUtils;

/**
 * Concrete implementation of ListableBeanFactory.
 * Includes convenient methods to populate the factory from a Map
 * and a ResourceBundle.
 * @author Rod Johnson
 * @since 16 April 2001
 * @version $RevisionId$
 */
public class ListableBeanFactoryImpl extends AbstractBeanFactory implements ListableBeanFactory {

	/**
	* Prefix for bean definition keys in Maps.
	*/
	public static final String DEFAULT_PREFIX = "beans.";

	public static final String CLASS_KEY = "class";

	/** owner.(singleton)=true 
	 * default is true
	 * */
	public static final String SINGLETON_KEY = "(singleton)";

	public static final String PARENT_KEY = "parent";

	// Separator between bean name and property name */
	public static final String SEPARATOR = ".";

	/** Property suffix for references: e.g. owner.dog(ref)=fido */
	public static final String REF_SUFFIX = "(ref)";

	//---------------------------------------------------------------------
	// Instance data
	//---------------------------------------------------------------------
	/** Map of BeanDefinition objects, keyed by prototype name */
	private Map beanDefinitionHash = new HashMap();

	/** ClassLoader to use. May be null, in which case
	 * we rely on the default behavior of Class.forName()
	 */
	private ClassLoader classLoader;

	//---------------------------------------------------------------------
	// Constructors
	//---------------------------------------------------------------------

	/** Creates new ListableBeanFactoryImpl */
	public ListableBeanFactoryImpl() {
	}

	/** Create a new ListableBeanFactoryImpl that takes
	 * uses the ClassLoader of the caller to load classes.
	 * Why would we need to do this? Imagine we're using this class
	 * from a WAR, but that this class is also used within an EJB Jar
	 * in the same EAR. This class will have been loaded by the EJB
	 * class loader, and will be unable to load classes within the WAR.
	 * The solution is to provide the ability to pass in a ClassLoader.
	 * <p/><b>Do not use this constructor within the EJB tier</b>.
	 * Obtaining the class loader is illegal on behalf of an EJB.
	 * @param caller object from which we should take the class loader
	 * used to load classes. Normally this is the object that
	 * is using this class.
	 */
	public ListableBeanFactoryImpl(Object caller) {
		if (caller != null)
			this.classLoader = caller.getClass().getClassLoader();
	}

	//---------------------------------------------------------------------
	// Implementation of ListableBeanFactory
	//---------------------------------------------------------------------

	@Override
	public final String[] getBeanDefinitionNames() {
		Set keys = beanDefinitionHash.keySet();
		String[] names = new String[keys.size()];
		Iterator itr = keys.iterator();
		int i = 0;
		while (itr.hasNext()) {
			names[i++] = (String) itr.next();
		}
		return names;
	}

	/**
	 * Return the number of beans defined in the factory
	 * @return
	 */
	@Override
	public int getBeanDefinitionCount() {
		return beanDefinitionHash.size();
	}

	/** Slow: don't call too often
	 */
	@Override
	public String[] getBeanDefinitionNames(Class type) {
		Set keys = beanDefinitionHash.keySet();
		List matches = new LinkedList();
		Iterator itr = keys.iterator();
		while (itr.hasNext()) {
			String name = (String) itr.next();
			Class clazz = getBeanClass((AbstractBeanDefinition) beanDefinitionHash.get(name));
			if (type.isAssignableFrom(clazz)) {
				matches.add(name);
			}
		}
		return (String[]) matches.toArray(new String[matches.size()]);
	}

	//---------------------------------------------------------------------
	// Implementation of superclass protected abstract methods
	//---------------------------------------------------------------------
	@Override
	protected BeanDefinition getBeanDefinition(String prototypeName) throws NoSuchBeanDefinitionException {
		BeanDefinition bd = (BeanDefinition) beanDefinitionHash.get(prototypeName);
		if (bd == null)
			throw new NoSuchBeanDefinitionException(prototypeName, toString());
		return bd;
	}

	public String toString() {
		String s = getClass() + ": ";
		s += " defined prototypes [" + StringUtils.arrayToDelimitedString(getBeanDefinitionNames(), ",") + "]";
		return s;
	}
	
	
	//---------------------------------------------------------------------
    // Public methods
    //---------------------------------------------------------------------
	
	/**
     * Subclasses or users should call this method to register new bean definitions
     * with this class.
     * <br/>This method isn't guaranteed to be threadsafe. It should be called
     * before any bean instances are accessed.
     * @param prototypeName name of the bean instance to register
     * @param beanDefinition definition of the bean instance to register
     */
	 public final void registerBeanDefinition(String prototypeName, BeanDefinition beanDefinition) throws BeansException {
		 beanDefinitionHash.put(prototypeName, beanDefinition);
	 }
	
	 /**
      * Ensure that even potentially unreferenced singletons are instantiated
      * Subclasses or callers should invoke this if they want this behavior.
      */
	 public void preInstantiateSingletons() {
		// Ensure that unreferenced singletons are instantiated
		 String[] beanNames = getBeanDefinitionNames();
		  for (int i = 0; i < beanNames.length; i++) {
			  BeanDefinition bd = getBeanDefinition(beanNames[i]);
			  if (bd.isSingleton()) {
                  Object singleton = getBean(beanNames[i]);
                  logger.info("Instantiated singleton: " + singleton);
          }
		  }
	 }
	
	
	

}
