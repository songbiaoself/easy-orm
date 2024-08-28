package com.coderevolt.util;

import com.coderevolt.sql.attr.Column;

import java.beans.Introspector;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FieldUtil {

    private static final char underLine = '_';

    public static String camelCase(String fieldName) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < fieldName.length(); i++) {
            char c = fieldName.charAt(i);
            if (underLine == c && i < fieldName.length() - 1) {
                res.append(Character.toUpperCase(fieldName.charAt(++i)));
            } else {
                res.append(c);
            }
        }
        return Introspector.decapitalize(res.toString());
    }

    public static String underline(String fieldName) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < fieldName.length(); i++) {
            char c = fieldName.charAt(i);
            res.append(Character.toLowerCase(c));
            if ((c >= 'a' && c <= 'z') && i + 1 < fieldName.length()) {
                char nextChar = fieldName.charAt(i + 1);
                if (nextChar >= 'A' && nextChar <= 'Z') {
                    res.append("_");
                }
            }
        }
        return res.toString();
    }

    public static List<String> listField(Class<?> clz) {
        return Arrays.stream(clz.getDeclaredFields()).map(f -> {
            Column column = f.getAnnotation(Column.class);
            if (column != null) {
                return "@".equals(column.name()) ? f.getName() : column.name();
            }
            return f.getName();
        }).collect(Collectors.toList());
    }

}
