package com.itmayeide.service;

import java.lang.reflect.Proxy;

/**
 * 获取SqlSession对象<br>
 * 作者: 每特教育-余胜军<br>
 * 联系方式:QQ644064779|WWW.itmayiedu.com<br>
 */
public class SqlSession {

	// 获取getMapper
	public static <T> T getMapper(Class<T> clas)
			throws IllegalArgumentException, InstantiationException, IllegalAccessException {
		return (T) Proxy.newProxyInstance(clas.getClassLoader(), new Class[] { clas },
				new MyInvocationHandlerMbatis(clas));
	}

}
//