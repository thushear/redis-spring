/**
 * 
 */
package com.interface21.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.beans.propertyeditors.PropertiesEditor;

/**
 * Default implementation of the BeanWrapper interface
 * that should be sufficient for all normal uses. Caches
 * introspection results for efficiency.
 * <p>Note: this class never tries to load a class by name.
 * This won't work if this class is used in a WAR but was loaded
 * by the EJB class loader and the class to be loaded is in the WAR.
 * (This class would use the EJB classloader, which couldn't see
 * the required class.) We don't attempt to solve such problems by
 * obtaining the classloader at runtime, because this violates
 * the EJB programming restrictions.
 * @author  Rod Johnson
 * @since 15 April 2001
 */
public class BeanWrapperImpl implements BeanWrapper {

	public final static boolean DEFAULT_EVENT_PROPAGATION_ENABLED = false;

	// Install default property editors
	// IS THERE A WAY OF GETTING ALL IN THIS PACKAGE AUTOMATICALLY?
	// Would need what it did, however
	static {
		PropertyEditorManager.registerEditor(String[].class, PropertiesEditor.class);
		PropertyEditorManager.setEditorSearchPath(new String[] { "sun.beans.editors",
				"com.interface21.beans.propertyeditors" });
	}

	//---------------------------------------------------------------------
	// Instance data
	//---------------------------------------------------------------------

	private static Logger logger = LoggerFactory.getLogger(BeanWrapperImpl.class.getName());

	/**the wrapped object*/
	private Object object;

	private CachedIntrospectionResults cachedIntrospectionResults;

	/** Standard java.beans helper object used to propagate
	* events
	*/
	private VetoableChangeSupport vetoableChangeSupport;

	/** Standard java.beans helper object used to propagate
	 * events
	 */
	private PropertyChangeSupport propertyChangeSupport;

	/** Should we propagate events!? */
	private boolean eventPropagationEnabled = DEFAULT_EVENT_PROPAGATION_ENABLED;

	//---------------------------------------------------------------------
	// Constructors
	//---------------------------------------------------------------------
	/**
	 * Creates new BeanWrapperImpl, allowing specification of whether event
	 * propagation is enabled.
	 * @param eventPropagationEnabled whether event propagation should be enabled
	 */
	public BeanWrapperImpl(Object object, boolean eventPropagationEnabled) throws BeansException {
		this.eventPropagationEnabled = eventPropagationEnabled;
		setObject(object);
	}

	/**
	 * Creates new BeanWrapperImpl with default event propagation (disabled)
	 */
	public BeanWrapperImpl(Object object) throws BeansException {
		this(object, DEFAULT_EVENT_PROPAGATION_ENABLED);
	}

	/** Creates new BeanWrapperImpl */
	public BeanWrapperImpl(Class clazz) throws BeansException {
		cachedIntrospectionResults = CachedIntrospectionResults.forClass(clazz);
		setObject(BeanUtils.instantiateClass(clazz));
	}

	/** Creates new BeanWrapperImpl */
	private BeanWrapperImpl(CachedIntrospectionResults cachedIntrospectionResults) throws BeansException {
		this.cachedIntrospectionResults = cachedIntrospectionResults;
		setObject(BeanUtils.instantiateClass(cachedIntrospectionResults.getBeanClass()));
	}

	/** Creates new BeanWrapperImpl */
	private BeanWrapperImpl(CachedIntrospectionResults cachedIntrospectionResults, Object obj) throws BeansException {
		this.cachedIntrospectionResults = cachedIntrospectionResults;
		setObject(obj);
	}

	private void setObject(Object object) throws BeansException {
		if (object == null)
			throw new FatalBeanException("Cannot set BeanWrapperImpl target to a null object", null);
		this.object = object;
		if (cachedIntrospectionResults == null || !cachedIntrospectionResults.getBeanClass().equals(object.getClass())) {
			cachedIntrospectionResults = CachedIntrospectionResults.forClass(object.getClass());
		}
		setEventPropagationEnabled(this.eventPropagationEnabled);
	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#setPropertyValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setPropertyValue(String propertyName, Object value) throws PropertyVetoException, BeansException {
		setPropertyValue(new PropertyValue(propertyName, value));
	}

	/**
	 * Set an individual field
	 * All other setters go through this
	 */
	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#setPropertyValue(com.interface21.beans.PropertyValue)
	 */
	@Override
	public void setPropertyValue(PropertyValue pv) throws PropertyVetoException, BeansException {
		if (isNestedProperty(pv.getName())) {
			try {
				BeanWrapper nestedBw = getBeanWrapperForNestedProperty(this, pv.getName());
				nestedBw.setPropertyValue(new PropertyValue(getFinalPath(pv.getName()), pv.getValue()));
				return;
			} catch (NullValueInNestedPathException ex) {
				throw ex;
			} catch (FatalBeanException ex) {
				// Error in the nested path
				throw new NotWritablePropertyException(pv.getName(), getWrappedClass());
			}

		}
		// WHAT ABOUT INDEXED PROEPRTIES!?
		int pos = pv.getName().indexOf(NESTED_PROPERTY_SEPARATOR);
		// Handle nested properties recursively
		if (pos > -1) {
			String nestedProperty = pv.getName().substring(0, pos);
			String nestedPath = pv.getName().substring(pos + 1);
			logger.info("Navigating to property path '" + nestedPath + "' of nested property '" + nestedProperty + "'");
			BeanWrapper nestedBw = new BeanWrapperImpl(getPropertyValue(nestedProperty), false);
			nestedBw.setPropertyValue(new PropertyValue(nestedPath, pv.getValue()));
			return;
		}
		if (!isWritableProperty(pv.getName()))
			throw new NotWritablePropertyException(pv.getName(), getWrappedClass());
		PropertyDescriptor pd = getPropertyDescriptor(pv.getName());
		Method writeMethod = pd.getWriteMethod();
		Method readMethod = pd.getReadMethod();

		Object oldValue = null; // May stay null if it's not a readable property
		PropertyChangeEvent propertyChangeEvent = null;
		try {
			if (readMethod != null && eventPropagationEnabled) {
				// Can only find existing value if it's a readable property
				try {
					oldValue = readMethod.invoke(object, new Object[] {});
				} catch (Exception ex) {
					// The getter threw an exception, so we couldn't retrieve the old value.
					// We're not really interested in any exceptions at this point,
					// so we merely log the problem and leave oldValue null
					logger.error(
							"BeanWrapperImpl",
							"setPropertyValue",
							"Failed to invoke getter '"
									+ readMethod.getName()
									+ "' to get old property value before property change: getter probably threw an exception",
							ex);
				}
			}

			// Old value may still be null
			propertyChangeEvent = createPropertyChangeEventWithTypeConversionIfNecessary(object, pv.getName(), oldValue,pv.getValue(), pd.getPropertyType());
			
			// May throw PropertyVetoException: if this happens the PropertyChangeSupport
			// class fires a reversion event, and we jump out of this method, meaning
			// the change was never actually made
			if (eventPropagationEnabled) {
				vetoableChangeSupport.fireVetoableChange(propertyChangeEvent);
			}

			// Make the change
			writeMethod.invoke(object, new Object[] { propertyChangeEvent.getNewValue() });

			// If we get here we've changed the property OK and can broadcast it
			if (eventPropagationEnabled)
				propertyChangeSupport.firePropertyChange(propertyChangeEvent);
		} catch (InvocationTargetException ex) {
			if (ex.getTargetException() instanceof PropertyVetoException)
				throw (PropertyVetoException) ex.getTargetException();
			if (ex.getTargetException() instanceof ClassCastException)
				throw new TypeMismatchException(propertyChangeEvent, pd.getPropertyType(), ex);
			throw new MethodInvocationException(ex.getTargetException(), propertyChangeEvent);
		} catch (IllegalAccessException ex) {
			throw new FatalBeanException("illegal attempt to set property [" + pv + "] threw exception", ex);
		} catch (IllegalArgumentException ex) {
			throw new TypeMismatchException(propertyChangeEvent, pd.getPropertyType(), ex);
		}

	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#getPropertyValue(java.lang.String)
	 */
	@Override
	public Object getPropertyValue(String propertyName) throws BeansException {
		if(isNestedProperty(propertyName)){
			BeanWrapper nestedBw = getBeanWrapperForNestedProperty(this, propertyName);
			logger.info("final path in nested propertyvalue is " + propertyName + "is" + getFinalPath(propertyName));
			return nestedBw.getPropertyValue(getFinalPath(propertyName));
		}
		PropertyDescriptor pd = getPropertyDescriptor(propertyName);
		Method m = pd.getReadMethod();
		if(m == null)
			throw new FatalBeanException("cannot get scalar property [" + propertyName + "]: not readable", null);
		try {
			return m.invoke(object, null);
		} catch (IllegalArgumentException e) {
			throw new FatalBeanException("illegal argument",e);
		} catch (IllegalAccessException e) {
			throw new FatalBeanException("illegal attempt to getProperty [" + propertyName + "] throw exception ", e);
		} catch (InvocationTargetException e) {
			throw new FatalBeanException("getter for  [" + propertyName + "] throw exception ", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#getIndexedPropertyValue(java.lang.String, int)
	 */
	@Override
	public Object getIndexedPropertyValue(String propertyName, int index) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#setPropertyValues(java.util.Map)
	 */
	@Override
	public void setPropertyValues(Map m) throws BeansException {
		// TODO Auto-generated method stub

	}

	
	 /**
     * <b>The preferred way to perform a bulk update.</b>
     * Note that performing a bulk update differs from performing a single update,
     * in that an implementation of this class will continue to update properties
     * if a <b>recoverable</b> error (such as a vetoed property change or a type mismatch,
     * but <b>not</b> an invalid fieldname or the like) is encountered, throwing a
     * PropertyVetoExceptionsException containing all the individual errors.
     * This exception can be examined later to see all binding errors.
     * Properties that were successfully updated stay changed.
     * @param pvs PropertyValues to set on the target object
     *
     */
	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#setPropertyValues(com.interface21.beans.PropertyValues)
	 */
	@Override
	public void setPropertyValues(PropertyValues pvs) throws BeansException {
		setPropertyValues(pvs, false, null);
	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#setPropertyValues(com.interface21.beans.PropertyValues, boolean, com.interface21.beans.PropertyValuesValidator)
	 */
	@Override
	public void setPropertyValues(PropertyValues propertyValues, boolean ignoreUnknown, PropertyValuesValidator pvsValidator)
			throws BeansException {
		// Create only if needed
		PropertyVetoExceptionsException propertyVetoExceptionsException = new PropertyVetoExceptionsException(this);
		if (pvsValidator != null) {
			 try {
                 pvsValidator.validatePropertyValues(propertyValues);
         }
         catch (InvalidPropertyValuesException ipvex) {
                 propertyVetoExceptionsException.addMissingFields(ipvex);
         }
		}
			 PropertyValue[] pvs = propertyValues.getPropertyValues();
			 for (int i = 0; i < pvs.length; i++) {
                 try {
                         // This method may throw ReflectionException, which won't be caught
                         // here, if there is a critical failure such as no matching field.
                         // We can attempt to deal only with less serious exceptions
                         setPropertyValue(pvs[i]);
                 }
                 // Fatal ReflectionExceptions will just be rethrown
                 catch (NotWritablePropertyException ex) {
                         if (!ignoreUnknown)
                                 throw ex;
                         // Otherwise, just ignore it and continue...
                 }
                 catch (PropertyVetoException ex) {
                         //throw new ReflectionException("Property veto on prototype [" + name + "]: property [" + key + "]", ex);
                         propertyVetoExceptionsException.addPropertyVetoException(ex);
                 }
                 catch (TypeMismatchException ex) {
                         propertyVetoExceptionsException.addTypeMismatchException(ex);
                 }
                 catch (MethodInvocationException ex) {
                         propertyVetoExceptionsException.addMethodInvocationException(ex);
                 }
         }   // for each property
		  // If we encountered individual exceptions, throw the composite exception
        if (propertyVetoExceptionsException.getExceptionCount() > 0) {
                throw propertyVetoExceptionsException;
        }
	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#getPropertyDescriptors()
	 */
	@Override
	public PropertyDescriptor[] getPropertyDescriptors() throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#getPropertyDescriptor(java.lang.String)
	 */
	@Override
	public PropertyDescriptor getPropertyDescriptor(String propertyName) throws BeansException {
		return cachedIntrospectionResults.getPropertyDescriptor(propertyName);
	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#isReadableProperty(java.lang.String)
	 */
	@Override
	public boolean isReadableProperty(String propertyName) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#isWritableProperty(java.lang.String)
	 */
	@Override
	public boolean isWritableProperty(String propertyName) {
		try {
			return getPropertyDescriptor(propertyName).getWriteMethod()!=null;
		} catch (BeansException e) {
			return false;
		}	
	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#getWrappedInstance()
	 */
	@Override
	public Object getWrappedInstance() {
		return object;
	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#setWrappedInstance(java.lang.Object)
	 */
	@Override
	public void setWrappedInstance(Object obj) throws BeansException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#newWrappedInstance()
	 */
	@Override
	public void newWrappedInstance() throws BeansException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#newWrapper(java.lang.Object)
	 */
	@Override
	public BeanWrapper newWrapper(Object obj) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#getWrappedClass()
	 */
	@Override
	public Class getWrappedClass() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#addVetoableChangeListener(java.beans.VetoableChangeListener)
	 */
	@Override
	public void addVetoableChangeListener(VetoableChangeListener l) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#removeVetoableChangeListener(java.beans.VetoableChangeListener)
	 */
	@Override
	public void removeVetoableChangeListener(VetoableChangeListener l) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#addVetoableChangeListener(java.lang.String, java.beans.VetoableChangeListener)
	 */
	@Override
	public void addVetoableChangeListener(String propertyName, VetoableChangeListener l) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#removeVetoableChangeListener(java.lang.String, java.beans.VetoableChangeListener)
	 */
	@Override
	public void removeVetoableChangeListener(String propertyName, VetoableChangeListener l) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	@Override
	public void addPropertyChangeListener(PropertyChangeListener l) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	@Override
	public void removePropertyChangeListener(PropertyChangeListener l) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
	 */
	@Override
	public void addPropertyChangeListener(String propertyName, PropertyChangeListener l) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
	 */
	@Override
	public void removePropertyChangeListener(String propertyName, PropertyChangeListener l) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#isEventPropagationEnable()
	 */
	@Override
	public boolean isEventPropagationEnable() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#setEventPropagationEnabled(boolean)
	 */
	@Override
	public void setEventPropagationEnabled(boolean flag) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.interface21.beans.BeanWrapper#invoke(java.lang.String, java.lang.Object[])
	 */
	@Override
	public Object invoke(String methodName, Object[] args) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	//----------------------------
	//---util method
	//----------------------------
	private boolean isNestedProperty(String path) {
		return path.indexOf(NESTED_PROPERTY_SEPARATOR) != -1;
	}

	/**
	* Recursive
	*/
	private BeanWrapper getBeanWrapperForNestedProperty(BeanWrapper bw, String path) {
		// WHAT ABOUT INDEXED PROEPRTIES!?

		int pos = path.indexOf(NESTED_PROPERTY_SEPARATOR);
		// Handle nested properties recursively
		if (pos > -1) {
			String nestedProperty = path.substring(0, pos);
			String nestedPath = path.substring(pos + 1);
			logger.info("Navigating to property path '" + nestedPath + "' of nested property '" + nestedProperty + "'");
			// Could consider caching these, but they're not that expensive to instantiate
			Object propertyValue = bw.getPropertyValue(nestedProperty);
			if (propertyValue == null)
				throw new NullValueInNestedPathException(bw.getWrappedClass(), nestedProperty);
			BeanWrapper nestedBw = new BeanWrapperImpl(propertyValue, false);
			return getBeanWrapperForNestedProperty(nestedBw, nestedPath);
		} else {
			return bw;
		}
	}

	/**
	 * Works if not nested
	 */
	private String getFinalPath(String nestedPath) {
		return nestedPath.substring(nestedPath.lastIndexOf(NESTED_PROPERTY_SEPARATOR) + 1);
	}

	/**
	 * Convert the value to the required type.
	 * Conversions from String to any type use the setAsTest() method of
	 * the PropertyEditor class. Note that a PropertyEditor must be registered
	 * for this class for this to work. This is a standard Java Beans API.
	 */
	private PropertyChangeEvent createPropertyChangeEventWithTypeConversionIfNecessary(Object target,
			String propertyName, Object oldValue, Object newValue, Class requiredType) throws BeansException {
		// Only need to cast if value isn't null
		if (newValue != null) {
			// We may need to change the value of newValue
			if (!requiredType.isAssignableFrom(newValue.getClass()) && (newValue instanceof String)) {
				PropertyEditor pe = PropertyEditorManager.findEditor(requiredType);
				if (pe != null) {
					try {
						pe.setAsText((String) newValue);
						newValue = pe.getValue();
					} catch (IllegalArgumentException ex) {
						throw new TypeMismatchException(new PropertyChangeEvent(target, propertyName, oldValue,
								newValue), requiredType);
					}
				}
			}
		}
		return new PropertyChangeEvent(target, propertyName, oldValue, newValue);
	}

}
