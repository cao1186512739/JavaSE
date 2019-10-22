package com.itmayeide.service;

import com.itmayeide.orm.annotation.ExtInsert;
import com.itmayeide.orm.annotation.ExtParam;
import com.itmayeide.orm.annotation.ExtSelect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;



/**
 * 功能说明:手写mybatis框架注解版本 <br>
 * 1.使用动态代理技术,获取接口方法上的sql语句<br>
 * 2.根据不同的SQL语句<br>
 * 作者: 每特教育-余胜军<br>
 * 联系方式:QQ644064779|WWW.itmayiedu.com<br>
 */
public class MyInvocationHandlerMbatis implements InvocationHandler {

	/**
	 * 这个就是我们要代理的真实对象
	 */
	private Object subject;

	/**
	 * 构造方法，给我们要代理的真实对象赋初值
	 * 
	 * @param subject
	 */
	public MyInvocationHandlerMbatis(Object subject) {
		this.subject = subject;
	}

	/**
	 * 该方法负责集中处理动态代理类上的所有方法调用。 调用处理器根据这三个参数进行预处理或分派到委托类实例上反射执行
	 * 
	 * @param proxy
	 *            代理类实例
	 * @param method
	 *            被调用的方法对象
	 * @param args
	 *            调用参数
	 * @return
	 * @throws Throwable
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// 判断方法上是否有ExtInsert注解
		ExtInsert extInsert = method.getAnnotation(ExtInsert.class);
		if (extInsert != null) {
			return insertSQL(extInsert, method, args);
		}
		// 判断方法上注解类型
		ExtSelect extSelect = method.getAnnotation(ExtSelect.class);
		if (extSelect != null) {
			return selectMybatis(extSelect, method, args);
		}

		return null;
	}

	public int insertSQL(ExtInsert extInsert, Method method, Object[] args) {
		// 获取注解上的sql
		String insertSql = extInsert.value();
		System.out.println("sql:" + insertSql);
		// 获取方法上的参数
		Parameter[] parameters = method.getParameters();
		// 将方法上的参数存放在Map集合中
		ConcurrentHashMap<Object, Object> parameterMap = getExtParams(parameters, args);
		// 获取SQL语句上需要传递的参数
		String[] sqlParameter = SQLUtils.sqlInsertParameter(insertSql);
		List<Object> parameValues = new ArrayList<>();
		for (int i = 0; i < sqlParameter.length; i++) {
			String str = sqlParameter[i];
			Object object = parameterMap.get(str);
			parameValues.add(object);
		}
		// 将SQL语句替换为？号
		String newSql = SQLUtils.parameQuestion(insertSql, sqlParameter);
		System.out.println("newSql:" + newSql);
		// 调用jdbc代码执行
		int insertResult = JDBCUtils.insert(newSql, false, parameValues);
		return insertResult;
	}

	public Object selectMybatis(ExtSelect extInsert, Method method, Object[] args) throws SQLException {
		try {
			// 获取查询SQL语句
			String selectSQL = extInsert.value();
			// 将方法上的参数存放在Map集合中
			Parameter[] parameters = method.getParameters();
			// 获取方法上参数集合
			ConcurrentHashMap<Object, Object> parameterMap = getExtParams(parameters, args);
			// 获取SQL传递参数
			List<String> sqlSelectParameter = SQLUtils.sqlSelectParameter(selectSQL);
			// 排序参数
			List<Object> parameValues = new ArrayList<>();
			for (int i = 0; i < sqlSelectParameter.size(); i++) {
				String parameterName = sqlSelectParameter.get(i);
				Object object = parameterMap.get(parameterName);
				parameValues.add(object.toString());
			}
			// 变为?号
			String newSql = SQLUtils.parameQuestion(selectSQL, sqlSelectParameter);
			System.out.println("执行SQL:" + newSql + "参数信息:" + parameValues.toString());
			// 调用JDBC代码查询
			ResultSet rs = JDBCUtils.query(newSql, parameValues);
			// 获取返回类型
			Class<?> returnType = method.getReturnType();
			if (!rs.next()) {
				// 没有查找数据
				return null;
			}
			// 向上移动
			rs.previous();
			// 实例化对象
			Object newInstance = returnType.newInstance();
			while (rs.next()) {
				for (String parameterName : sqlSelectParameter) {
					// 获取集合中数据
					Object value = rs.getObject(parameterName);
					// 查找对应属性
					Field field = returnType.getDeclaredField(parameterName);
					// 设置允许私有访问
					field.setAccessible(true);
					// 赋值参数
					field.set(newInstance, value);
				}

			}
			return newInstance;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private ConcurrentHashMap<Object, Object> getExtParams(Parameter[] parameters, Object[] args) {
		// 获取方法上参数集合
		ConcurrentHashMap<Object, Object> parameterMap = new ConcurrentHashMap<>();
		for (int i = 0; i < parameters.length; i++) {
			// 参数信息
			Parameter parameter = parameters[i];
			ExtParam extParam = parameter.getDeclaredAnnotation(ExtParam.class);
			// 参数名称
			String paramValue = extParam.value();
			// 参数值
			Object oj = args[i];
			parameterMap.put(paramValue, oj);
		}
		return parameterMap;
	}
}