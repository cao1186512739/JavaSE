package com.itmayiedu.mapper;

import java.util.List;

import com.itmayiedu.entity.User;
import com.itmayiedu.orm.annotation.ExtInsert;
import com.itmayiedu.orm.annotation.ExtParam;
import com.itmayiedu.orm.annotation.ExtSelect;

public interface UserMapper {

	@ExtInsert("insert into user(userName,userAge) values(#{userName},#{userAge})")
	public int insertUser(@ExtParam("userName") String userName, @ExtParam("userAge") Integer userAge);

	@ExtSelect("select * from User where userName=#{userName} and userAge=#{userAge} ")
	User selectUser(@ExtParam("userName") String name, @ExtParam("userAge") Integer userAge);

}
