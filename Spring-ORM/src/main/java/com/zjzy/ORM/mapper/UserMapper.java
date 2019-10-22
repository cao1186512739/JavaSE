package com.zjzy.ORM.mapper;


import com.zjzy.ORM.ORM.annotation.ExtInsert;
import com.zjzy.ORM.ORM.annotation.ExtParam;
import com.zjzy.ORM.ORM.annotation.ExtSelect;
import com.zjzy.ORM.entity.User;

public interface UserMapper {

	@ExtInsert("insert into user(userName,userAge) values(#{userName},#{userAge})")
	public int insertUser(@ExtParam("userName") String userName, @ExtParam("userAge") Integer userAge);

	@ExtSelect("select * from User where userName=#{userName} and userAge=#{userAge} ")
	User selectUser(@ExtParam("userName") String name, @ExtParam("userAge") Integer userAge);

}
