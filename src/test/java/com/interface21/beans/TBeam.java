package com.interface21.beans;

public class TBeam implements ITestBean {

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

	@Override
	public String toString() {
		return "TBeam [age=" + age + ", name=" + name + ", spouse=" + spouse + "]";
	}
	
	
}
