package com.interface21.beans;
/**
* @author  rod
* @version
*/
public abstract class BeanUtils {

	
	public static Object instantiateClass(Class clazz)throws BeansException{
		try {
			return clazz.newInstance();
		} catch (InstantiationException ex) {
			throw new FatalBeanException("Cannot instantiate [" + clazz + "]; is it an interface or an abstract class?", ex);
		} catch (IllegalAccessException ex) {
			 throw new FatalBeanException("Cannot instantiate [" + clazz + "]; has class definition changed? Is there a public constructor?", ex);
		}
		
	}
	
}
