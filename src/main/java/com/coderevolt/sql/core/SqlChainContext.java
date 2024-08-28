package com.coderevolt.sql.core;

import com.coderevolt.sql.config.SqlOption;

import java.io.ObjectOutputStream;
import java.util.*;

/**
 * sql执行链上下文
 */
public class SqlChainContext {

    private final SqlOption sqlOption;
    private final List<String> insertColumns = new ArrayList<>();
    private final List<Object> paramValueList = new ArrayList<>();
    private final Map<String, String> PLACEHOLDER_MAP = new HashMap<>();
    private boolean existSelect = false;
    private boolean existWhere = false;
    private boolean existHaving = false;
    private boolean selectAll = false;
    private boolean existSet = false;
    private boolean existGroupBy = false;
    private boolean existOrderBy = false;
    private boolean existValues = false;

    public SqlChainContext(SqlOption sqlOption) {
        this.sqlOption = sqlOption;
    }

    public void putPlaceHolder(String key, String value) {
        PLACEHOLDER_MAP.put(key, value);
    }

    public String getPlaceHolder(String key) {
        return PLACEHOLDER_MAP.get(key);
    }

    public Map<String, String> getPlaceHolderMap() {
        return Collections.unmodifiableMap(PLACEHOLDER_MAP);
    }

    public boolean isExistWhere() {
        return existWhere;
    }

    public void setExistWhere(boolean existWhere) {
        this.existWhere = existWhere;
    }

    public boolean isExistHaving() {
        return existHaving;
    }

    public void setExistHaving(boolean existHaving) {
        this.existHaving = existHaving;
    }

    public boolean isExistSelect() {
        return existSelect;
    }

    public void setExistSelect(boolean existSelect) {
        this.existSelect = existSelect;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    public SqlOption getSqlOption() {
        return sqlOption;
    }

    public boolean isExistSet() {
        return existSet;
    }

    public void setExistSet(boolean existSet) {
        this.existSet = existSet;
    }

    public boolean isExistGroupBy() {
        return existGroupBy;
    }

    public void setExistGroupBy(boolean existGroupBy) {
        this.existGroupBy = existGroupBy;
    }

    public boolean isExistOrderBy() {
        return existOrderBy;
    }

    public void setExistOrderBy(boolean existOrderBy) {
        this.existOrderBy = existOrderBy;
    }

    public List<Object> getParamValueList() {
        return Collections.unmodifiableList(this.paramValueList);
    }

    public void addParamValue(Object paramValue) {
        this.paramValueList.add(paramValue);
    }

    public boolean isExistValues() {
        return existValues;
    }

    public void setExistValues(boolean existValues) {
        this.existValues = existValues;
    }

    public void addInsertColumn(String column) {
        this.insertColumns.add(column);
    }

    public List<String> getInsertColumns() {
        return Collections.unmodifiableList(this.insertColumns);
    }
}
