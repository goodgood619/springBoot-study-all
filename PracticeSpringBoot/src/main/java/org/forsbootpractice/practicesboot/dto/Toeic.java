package org.forsbootpractice.practicesboot.dto;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "toeic")
public class Toeic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long toeicIdx;

    @Column(length = 30)
    private String korean;

    @Column(length = 30)
    private String english;

    @Builder
    public Toeic(String korean, String english) {
        this.korean = korean;
        this.english = english;
    }
}
