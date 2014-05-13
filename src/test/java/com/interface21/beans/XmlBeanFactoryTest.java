/**
 * 
 */
package com.interface21.beans;

import com.interface21.beans.factory.BeanFactory;
import com.interface21.beans.factory.support.XmlBeanFactory;

/**
 * @author kongming
 *
 */
public class XmlBeanFactoryTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BeanFactory factory = new XmlBeanFactory("testBean.xml");
		
		
		TBeam t =  (TBeam) factory.getBean("rod");
		System.out.println(t);
	}

}
