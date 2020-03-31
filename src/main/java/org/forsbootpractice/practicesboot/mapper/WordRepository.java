package org.forsbootpractice.practicesboot.mapper;

import org.forsbootpractice.practicesboot.dto.Toeic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Toeic,Long> {

}
