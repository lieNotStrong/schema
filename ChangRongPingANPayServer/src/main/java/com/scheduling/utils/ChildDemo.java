package com.scheduling.utils;

 class ChildDemo extends ParentDemo {
	 private static String name;
		private static int age;

	public ChildDemo(String name, int age) {
		super(name, age);
		
	}

	public ChildDemo() {
		this(name, age);
		
	}
	
	
public static void main(String[] args) {
	ParentDemo pd = new ChildDemo();
}

@Override
public void study() {
	// TODO Auto-generated method stub
	
}
}

