package org.forsbootpractice.practicesboot.mapper;

import org.forsbootpractice.practicesboot.dto.Toeic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordRepository extends JpaRepository<Toeic,Long> {

}
