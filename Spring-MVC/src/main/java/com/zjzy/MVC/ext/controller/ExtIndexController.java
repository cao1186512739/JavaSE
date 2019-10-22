package com.zjzy.MVC.ext.controller;


import com.zjzy.MVC.extspringmvc.extannotation.ExtController;
import com.zjzy.MVC.extspringmvc.extannotation.ExtRequestMapping;

@ExtController
@ExtRequestMapping(value = "/ext")
public class ExtIndexController {

	// url地址如何 和方法进行关联
	@ExtRequestMapping(value = "/index")
	public String index() {
		System.out.println("手写springmvc框架");
		return "index";
	}

}
