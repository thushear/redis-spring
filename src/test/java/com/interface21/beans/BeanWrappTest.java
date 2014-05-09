/**
 * 
 */
package com.interface21.beans;

import java.beans.PropertyEditorSupport;
import java.beans.PropertyVetoException;
import java.util.StringTokenizer;

/**
 * @author kongming
 *
 */
public class BeanWrappTest {

	/**
	 * @param args
	 * @throws PropertyVetoException 
	 * @throws BeansException 
	 */
	public static void main(String[] args) throws BeansException, PropertyVetoException {
		
		TestBean rod = new TestBean();
		BeanWrapper bw = new BeanWrapperImpl(rod);
		bw.setPropertyValue("age", new Integer(23));
		System.out.println(bw.getPropertyValue("age"));
		bw.setPropertyValue(new PropertyValue("name", "rod johonson"));
		System.out.println("name = " +  bw.getPropertyValue("name"));
		bw.setPropertyValue("spouse",new TestBean());
		bw.setPropertyValue("spouse.name","kongming");
		bw.setPropertyValue("spouse.spouse",rod);
		System.out.println(bw.getPropertyValue("spouse.name"));
		
		
	}

}
interface ITestBean{
	int getAge();
	void setAge(int age);
	String getName();
	void setName(String name);
	ITestBean getSpouse();
	void setSpouse(ITestBean spouse);
}

class TestBean implements ITestBean{

	int age;
	
	String name;
	
	ITestBean spouse;
	
	@Override
	public int getAge() {
		return age;
	}

	@Override
	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public ITestBean getSpouse() {
		return spouse;
	}

	@Override
	public void setSpouse(ITestBean spouse) {
		this.spouse= spouse;
	}
	
}

class TestBeanEditor extends PropertyEditorSupport{

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		TestBean tb = new TestBean();
		StringTokenizer st = new StringTokenizer(text,"_");
		tb.setName(st.nextToken());
		tb.setAge(Integer.parseInt(st.nextToken()));
		setValue(tb);
	}
	
}






