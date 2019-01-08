package com.luopan.annualmeeting.dao;

import com.luopan.annualmeeting.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDao {

  User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

}
