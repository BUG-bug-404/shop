package com.keith.common.annotation;
import java.lang.annotation.*;

/**
 * 自定义注解 同步锁
 * 创建者	JohnSon
 * 创建时间	2019年9月3日
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})    
@Retention(RetentionPolicy.RUNTIME)    
@Documented    
public  @interface Servicelock { 
	 String description()  default "";
}
