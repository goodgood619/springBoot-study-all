package org.forsbootpractice.practicesboot.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.forsbootpractice.practicesboot.dto.Toeic;
import org.forsbootpractice.practicesboot.dto.User;

import java.util.List;

@Mapper
public interface ToeicMapper {

    @Select("select * from toeic")
    List<Toeic> findAll();

    @Select("select * from toeic where korean = #{korean}")
    Toeic findByName(@Param("korean") final String korean);

    @Insert("insert into toeic(korean,english) values(#{toeic.korean},#{toeic.english})")
    void save2(@Param("toeic") final Toeic toeic);

}
