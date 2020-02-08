package org.forsbootpractice.practicesboot.lombok;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestRequireArgsConstructor {
    private int userIdx;
    private final String name;
    @NonNull
    private String email;
}
