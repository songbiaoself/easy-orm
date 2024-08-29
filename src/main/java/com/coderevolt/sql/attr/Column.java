package com.coderevolt.sql.attr;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface Column {

    /**
     * 字段名称， 增删改查时自动映射
     * @return
     */
    String name() default "@";

    /**
     * 字段类型
     * @return
     */
    ColumnType type() default ColumnType.NORMAL;

    /**
     * 字段处理策略
     * @return
     */
    DmlStrategy dmlStrategy() default DmlStrategy.IGNORE_NULL;

    enum ColumnType {
        ID,
        NORMAL
    }

    enum DmlStrategy {
        /**
         * 设置null
         */
        SET_NULL,
        /**
         * 忽略null值
         */
        IGNORE_NULL
    }

}
