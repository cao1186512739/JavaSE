package com.itmayeide.dao;


import com.itmayeide.entity.User;
import com.itmayeide.orm.annotation.ExtInsert;
import com.itmayeide.orm.annotation.ExtParam;
import com.itmayeide.orm.annotation.ExtSelect;

/**
 * 自定义注解 作者: 每特教育-余胜军<br>
 * 联系方式:QQ644064779|WWW.itmayiedu.com<br>
 */
public interface UserDao {
	// 1.接口既然不能被实例化？那么我们是怎么实现能够调用的？
	// 2.参数如何和sql参数绑定
	// 3.返回结果
	@ExtSelect("select * from User where userName=#{userName} and userAge=#{userAge} ")
	User selectUser(@ExtParam("userName") String name, @ExtParam("userAge") Integer userAge);

	@ExtInsert("insert into user(userName,userAge) values(#{userName},#{userAge})")
    int insertUser(@ExtParam("userAge") Integer userAge, @ExtParam("userName") String name);

}
