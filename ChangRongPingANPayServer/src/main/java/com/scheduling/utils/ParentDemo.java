package com.scheduling.utils;

public abstract class ParentDemo {
	private String name;
	private int age;

	public ParentDemo(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}

	
	public ParentDemo() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public abstract void study();

}


