package com.lanwei.pmp.server.annotation;

import java.lang.annotation.*;

/**
 * 日志记录
 * @author lanwei
 * @email 335747949@qq.com
 */
@Target(ElementType.METHOD)   //修饰的对象范围
@Retention(RetentionPolicy.RUNTIME)   //元注解，该Annotation被保留的时间长短
@Documented  //表明这个注释是由 javadoc记录的
public @interface LogAnnotation {

    String value() default "";
}
