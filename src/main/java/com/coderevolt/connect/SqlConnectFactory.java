package com.coderevolt.connect;

import com.coderevolt.doc.NotNull;
import com.coderevolt.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SqlConnectFactory {

    private static final Map<String, DataSource> dataSources = new ConcurrentHashMap<>();

    private static String defaultSourceName;

    public static Map<String, DataSource> getRegisteredDBMap() {
        return Collections.unmodifiableMap(dataSources);
    }

    public static String getDefaultSourceName() {
        Assert.isNotEmpty(defaultSourceName, "defaultSourceName is empty, must register before call");
        return defaultSourceName;
    }

    /**
     * @param sourceName 根据不同的sourceName获取不同的数据源
     * @param dataSource 数据源
     * @param defaultDB  com.coderevolt.sql.dataSource.SqlOption#sourceName为null时使用defaultDB
     */
    public static void register(@NotNull String sourceName, @NotNull DataSource dataSource, boolean defaultDB) {
        Assert.isTrue(sourceName != null && dataSource != null, "sourceName and dataSource must not be null");
        if (defaultDB) {
            defaultSourceName = sourceName;
        }
        dataSources.put(sourceName, dataSource);
    }

    public static Connection getConnection(String sourceName) throws SQLException {
        DataSource hikariDataSource = dataSources.get(sourceName);
        Assert.isTrue(hikariDataSource != null, "No DataSource found for " + sourceName);
        return hikariDataSource.getConnection();
    }

}
