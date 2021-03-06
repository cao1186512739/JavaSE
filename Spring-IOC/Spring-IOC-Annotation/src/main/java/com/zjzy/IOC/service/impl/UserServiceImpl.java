package com.zjzy.IOC.service.impl;


import com.zjzy.IOC.annotation.ExtResource;
import com.zjzy.IOC.annotation.ExtService;
import com.zjzy.IOC.service.OrderService;
import com.zjzy.IOC.service.UserService;

//将该类注入到spring容器里面
@ExtService
public class UserServiceImpl implements UserService {
	// 从Spring容器中读取bean
	@ExtResource
	private OrderService orderServiceImpl;

	public void add() {
		orderServiceImpl.addOrder();
		System.out.println("我是使用反射机制运行的方法");
	}

}
