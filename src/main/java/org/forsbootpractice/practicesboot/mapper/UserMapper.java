package org.forsbootpractice.practicesboot.mapper;

import org.apache.ibatis.annotations.*;
import org.forsbootpractice.practicesboot.dto.User;
import org.forsbootpractice.practicesboot.model.SignUpReq;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from user")
    List<User> findAll();

    @Select("select * from user where name = #{name}")
    User findByName(@Param("name") final String name);

    @Select("select * from user where useridx = #{userIdx}")
    User findByUserIdx(@Param("userIdx") final int userIdx);

//    //Auto Increment 값을 받아오고 싶으면, 리턴 타입을 int로 하면된다
//    @Insert("insert into user(name,part) values(#{user.name},#{user.part})")
//    @Options(useGeneratedKeys = true,keyColumn = "user.userIdx")
//    int save(@Param("user") final User user);


    //회원 등록, Auto Increment는 회원 고유 번호
    //Auto Increment 값을 받아오고 싶으면 리턴 타입을 int(Auto Increment 컬럼 타입)으로 하면 된다.
    @Insert("insert into user(name, part, profileUrl) VALUES(#{signUpReq.name}, #{signUpReq.part}, #{signUpReq.profileUrl})")
    void save(@Param("signUpReq") final SignUpReq signUpReq);


    @Insert("insert into user(name,part) values(#{user.name},#{user.part}")
    void save2(@Param("user") final User user);

    @Update("update user set name = #{user.name}, part = #{user.part} where userIdx = #{userIdx}")
    void update(@Param("userIdx") final int userIdx,@Param("user") final User user);

    @Delete("delete from user where userIdx = #{userIdx}")
    void deleteByUserIdx(@Param("userIdx") final int userIdx);


    //이름과 비밀번호로 조회
    @Select("SELECT * FROM user WHERE name = #{name} AND password = #{password}")
    User findByNameAndPassword(@Param("name") final String name, @Param("password") final String password);

}
