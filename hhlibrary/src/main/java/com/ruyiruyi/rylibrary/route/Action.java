package com.ruyiruyi.rylibrary.route;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by geyang on 2020/7/7.
 */

@Target(ElementType.TYPE) //注解作用于类型（类，接口，注解，枚举）
@Retention(RetentionPolicy.RUNTIME) //运行时保留，运行中可以处理
@Documented // 生成javadoc文件
public @interface Action {
    String DEFAULT = "js";

    String value() default DEFAULT;
}
