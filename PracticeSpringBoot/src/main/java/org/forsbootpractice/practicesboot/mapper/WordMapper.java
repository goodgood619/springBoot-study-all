package org.forsbootpractice.practicesboot.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.forsbootpractice.practicesboot.dto.Word;

import java.util.List;

@Mapper
public interface WordMapper {

    @Select("select * from toeic")
    List<Word> toeicfindAll();

    @Select("select * from toeic where korean = #{korean}")
    Word findByName(@Param("korean") final String korean);

    @Insert("insert into toeic(korean,english) values(#{toeic.korean},#{toeic.english})")
    void save2(@Param("toeic") final Word toeic);

    @Select("select * from toss")
    List<Word> tossfindAll();
}
