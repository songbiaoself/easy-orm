package com.coderevolt.util;

import com.coderevolt.sql.attr.Column;
import com.coderevolt.sql.core.symbol.VariablePlaceholder;

import java.beans.Introspector;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SFuncUtil {

    public static final Pattern FIELD_PATTERN = Pattern.compile("^(get|is)([A-Z].*)");

    public static final Pattern CLASS_PATTERN = Pattern.compile("/?([_a-zA-Z0-9]+)$");

    /**
     * 注意: 非标准变量（非小驼峰）调用这个方法可能会有问题
     *
     * @param <T>
     * @param fn
     * @return
     */
    public static <T> String getFieldName(SFunction<T, ?> fn) {
        try {
            SerializedLambda serializedLambda = getSerializedLambda(fn);
            String getter = serializedLambda.getImplMethodName();
            // 对于非标准变量生成的Get方法这里可以直接抛出异常，或者打印异常日志
            Matcher matcher = FIELD_PATTERN.matcher(getter);
            if (matcher.matches()) {
                getter = matcher.group(2);
            }
            return Introspector.decapitalize(getter);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> String getClassName(SFunction<T, ?> fn) {
        try {
            SerializedLambda serializedLambda = getSerializedLambda(fn);
            String capturingClass = serializedLambda.getCapturingClass();
            // 对于非标准变量生成的Get方法这里可以直接抛出异常，或者打印异常日志
            Matcher matcher = CLASS_PATTERN.matcher(capturingClass);
            if (matcher.matches()) {
                capturingClass = matcher.group(1);
            }
            return Introspector.decapitalize(capturingClass);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Class<?> getClass(SFunction<T, ?> fn) {
        try {
            SerializedLambda serializedLambda = getSerializedLambda(fn);
            String implClass = serializedLambda.getImplClass();
            return Class.forName(implClass.replace("/", "."));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Field getField(SFunction<T, ?> fn) {
        try {
            Method method = fn.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(fn);
            String implClass = serializedLambda.getImplClass();
            Class<?> clz = Class.forName(implClass.replace("/", "."));
            String methodName = serializedLambda.getImplMethodName();
            // 对于非标准变量生成的Get方法这里可以直接抛出异常，或者打印异常日志
            Matcher matcher = FIELD_PATTERN.matcher(methodName);
            if (matcher.matches()) {
                String field = matcher.group(2);
                try {
                    return clz.getDeclaredField(Introspector.decapitalize(field));
                } catch (NoSuchFieldException e) {
                    return clz.getDeclaredField(field);
                }
            }
            throw new IllegalArgumentException("field " + methodName + " not found");
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> SerializedLambda getSerializedLambda(SFunction<T, ?> fn) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = fn.getClass().getDeclaredMethod("writeReplace");
        method.setAccessible(Boolean.TRUE);
        return (SerializedLambda) method.invoke(fn);
    }

    public static <T> String getColumn(SFunction<T, ?> fn) {
        try {
            SerializedLambda serializedLambda = getSerializedLambda(fn);
            String methodName = serializedLambda.getImplMethodName();
            // 对于非标准变量生成的Get方法这里可以直接抛出异常，或者打印异常日志
            Matcher matcher = FIELD_PATTERN.matcher(methodName);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("field " + methodName + " not found");
            }
            Field field = null;
            methodName = matcher.group(2);
            String implClass = serializedLambda.getImplClass();
            Class<?> clz = Class.forName(implClass.replace("/", "."));
            try {
                field = clz.getDeclaredField(Introspector.decapitalize(methodName));
            } catch (NoSuchFieldException e) {
                field = clz.getDeclaredField(methodName);
            }
            Column columnAnno = field.getAnnotation(Column.class);
            String fieldName = String.format(VariablePlaceholder.TABLE_NAME_DOT, Introspector.decapitalize(implClass.substring(implClass.lastIndexOf("/") + 1)));
            if (columnAnno != null) {
                fieldName += "@".equals(columnAnno.name()) ? FieldUtil.underline(field.getName()) : columnAnno.name();
            } else {
                fieldName += FieldUtil.underline(field.getName());
            }
            return fieldName;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

    }

}
