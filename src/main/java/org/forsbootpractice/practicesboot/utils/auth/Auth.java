package org.forsbootpractice.practicesboot.utils.auth;

import java.lang.annotation.*;

// 메소드에 적용
@Target(ElementType.METHOD)
// Runtime시에만 적용
@Retention(RetentionPolicy.RUNTIME)
@Documented
//상속가능
@Inherited
public @interface Auth {

}
