package com.zjzy.IOC;


import com.zjzy.IOC.service.UserService;
import com.zjzy.IOC.spring.ClassPathXmlApplicationContext;

public class Test001 {

	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext("com.itmayiedu.service.impl");
		UserService userService = (UserService) app.getBean("userServiceImpl");
		userService.add();
	}

}
