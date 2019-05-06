package org.redrock.demo.Mapping;

import org.apache.ibatis.annotations.*;
import org.redrock.demo.Entity.UserEntity;

@Mapper
public interface UserMapping {
    @Insert("insert into user(username,password) value (#{username},#{password)")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    void insertUser(UserEntity entity);

    @Select("select * from user where username= #{username} and password= #{password}")
    UserEntity checkUser(@Param("username")String username, @Param("password")String password);

    @Insert("update user set code = #{code} where username = #{username}")
    void insertCode(@Param("username")String username, @Param("code")String code);

    @Select("select username from user where code= #{code}")
    String getUser(@Param("code")String code);

    //代表获取用户所有必要信息
    @Select("select username from user where username= #{username}")
    UserEntity getInfo(@Param("username")String username);
}
