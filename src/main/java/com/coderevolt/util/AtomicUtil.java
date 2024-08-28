package com.coderevolt.util;

import com.coderevolt.connect.SqlConnectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Consumer;

public class AtomicUtil {

    private static final Logger log = LoggerFactory.getLogger(AtomicUtil.class);

    public static void txExecute(Consumer<Connection> consumer) throws SQLException {
        Connection connection = null;
        try {
            connection = SqlConnectFactory.getConnection(SqlConnectFactory.getDefaultSourceName());
            txExecute(connection, consumer);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * 事务执行
     *
     * @param connection
     * @param consumer
     * @throws SQLException
     */
    public static void txExecute(Connection connection, Consumer<Connection> consumer) throws SQLException {
        Assert.isTrue(connection != null, "jdbc connection must not be null");
        synchronized (connection) {
            try {
                connection.setAutoCommit(false);
                consumer.accept(connection);
                connection.commit();
            } catch (Throwable e) {
                log.error("transaction commit failed, will rollback", e);
                connection.rollback();
                throw e;
            }
        }
    }

    public String test() {
        return null;
    }

}
