package org.forsbootpractice.practicesboot.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "toss")
public class Toss {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tossIdx;

    @Column(length = 30)
    private String korean;

    @Column(length = 30)
    private String english;

    @Builder
    public Toss(String korean, String english) {
        this.korean = korean;
        this.english = english;
    }
}
