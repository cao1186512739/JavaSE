package com.itmayiedu.sql;

import java.lang.reflect.Proxy;

import com.itmayiedu.orm.mybatis.aop.MyInvocationHandlerMbatis;

public class SqlSession {

	// 加载Mapper接口
	public static <T> T getMapper(Class classz) {
		return (T) Proxy.newProxyInstance(classz.getClassLoader(), new Class[] { classz },
				new MyInvocationHandlerMbatis(classz));
	}

}
