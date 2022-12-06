package org.forsbootpractice.practicesboot.lombok;

public class Main {
    public static void main(String... args) {
        TestData testData = new TestData();

        TestBuilder.builder().userIdx(1)
                .name("둥석")
                .email("gktgnjftm@naver.com")
                .build();

        
    }
}
