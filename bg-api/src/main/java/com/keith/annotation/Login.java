

package com.keith.annotation;

import java.lang.annotation.*;

/**
 * 登录效验
 *
 * @author JohnSon
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Login {
}
