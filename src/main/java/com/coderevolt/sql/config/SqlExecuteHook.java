package com.coderevolt.sql.config;

import com.coderevolt.sql.core.SqlChainContext;

import java.sql.Connection;

public interface SqlExecuteHook {

    /**
     * sql执行前调用
     * @param connection sql连接
     * @param sqlChainContext sql执行链上下文
     */
    void beforeRun(Connection connection, SqlChainContext sqlChainContext);

    /**
     * sql执行后调用
     * @param connection
     * @param sqlChainContext
     * @param result 请求结果
     */
    void afterRun(Connection connection, SqlChainContext sqlChainContext, Object result);

}
