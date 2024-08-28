package com.coderevolt.test;

import com.coderevolt.connect.SqlConnectFactory;
import com.coderevolt.model.StudentModel;
import com.coderevolt.sql.SqlExecutor;
import com.coderevolt.util.SubUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;

public class DeleteSqlTest {

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        config.setUsername("root");
        config.setPassword("7758258");
        SqlConnectFactory.register("mysql", new HikariDataSource(config), true);
    }

    @Test
    public void simpleTest() {
        boolean result = SqlExecutor.builder().deleteChain()
                .deleteFrom(StudentModel.class)
                .where(SubUtil.lt(StudentModel::getAge, 10))
                .exec();

        System.out.println("执行结果: " + result);
    }

}
