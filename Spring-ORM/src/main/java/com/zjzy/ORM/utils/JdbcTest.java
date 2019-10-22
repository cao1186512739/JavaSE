package com.zjzy.ORM.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JdbcTest {

	public static void main(String[] args) throws SQLException {
		// #{ mybatis 替换成?
		// String inserSql = "insert into user(userName,userAge) values(?,?)";
		// ArrayList<Object> arrayList = new ArrayList<>();
		// arrayList.add("余胜军");
		// arrayList.add(20);
		// int insert = JDBCUtils.insert(inserSql, false, arrayList);
		// System.out.println("insert:" + insert);

		// 查询语句
		ArrayList<Object> arrayList = new ArrayList<>();
		arrayList.add("余胜军");
		arrayList.add(20);
		ResultSet res = JDBCUtils.query("select * from User where userName=? and userAge=? ", arrayList);
		while (res.next()) {
			Integer id = res.getInt("id");
			String userName = res.getString("userName");
			System.out.println("id:" + id + ",userName:" + userName);
		}
	}

}
