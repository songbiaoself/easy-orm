package com.coderevolt.test;

import com.coderevolt.connect.SqlConnectFactory;
import com.coderevolt.model.StudentModel;
import com.coderevolt.sql.SqlExecutor;
import com.coderevolt.util.AtomicUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class TransactionTest {


    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        config.setUsername("root");
        config.setPassword("7758258");
        SqlConnectFactory.register("mysql", new HikariDataSource(config), true);
    }

    @Test
    public void test() throws SQLException {
        AtomicUtil.txExecute(connection -> {

            SqlExecutor.builder().insertChain()
                    .insertInto(StudentModel.class)
                    .values(new StudentModel(null, "r123"))
                    .exec(connection);


            SqlExecutor.builder().insertChain()
                    .insertInto(StudentModel.class)
                    .values(new StudentModel(null, "r234"))
                    .exec(connection);

//            if (true) {
//                throw new RuntimeException("rollback test");
//            }
        });
    }

}
