package com.zjzy.IOC.service.impl;


import com.zjzy.IOC.annotation.ExtService;
import com.zjzy.IOC.service.OrderService;

@ExtService
public class OrderServiceImpl implements OrderService {

	public void addOrder() {
		System.out.println("addOrder");
	}

}
