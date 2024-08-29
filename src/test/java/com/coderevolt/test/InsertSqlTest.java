package com.coderevolt.test;

import com.coderevolt.connect.SqlConnectFactory;
import com.coderevolt.model.StudentModel;
import com.coderevolt.sql.SqlExecutor;
import com.coderevolt.util.SubUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;

public class InsertSqlTest {

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        config.setUsername("root");
        config.setPassword("7758258");
        SqlConnectFactory.register("mysql", new HikariDataSource(config), true);
    }

    @Test
    public void insertTest() throws SQLException {
        boolean result = SqlExecutor.builder().insertChain()
                .insertInto(StudentModel.class, StudentModel::getName, StudentModel::getAge)
                .values(new StudentModel("张三", 20))
                .exec();

        System.out.println("执行结果: " + result);
    }

    @Test
    public void insertSelectTest() throws SQLException {
        System.out.println(SqlExecutor.builder().insertChain()
                .insertInto(StudentModel.class, StudentModel::getName)
                .select(SubUtil.subSelect().select(StudentModel::getName).from(StudentModel.class))
                .exec());

        System.out.println(SqlExecutor.builder().insertChain()
                .insertInto(StudentModel.class)
                .select(SubUtil.subSelect().select().from(StudentModel.class))
                .exec());
    }

    @Test
    public void insertBatchTest() throws SQLException {
        System.out.println(SqlExecutor.builder().insertChain()
                .insertInto(StudentModel.class)
                .values(Arrays.asList(new StudentModel("张三", 20), new StudentModel("李四", 25)))
                .exec());

        System.out.println(SqlExecutor.builder().insertChain()
                .insertInto(StudentModel.class, StudentModel::getName, StudentModel::getAge)
                .values(Arrays.asList(new StudentModel("张三", 20), new StudentModel("李四", 25)))
                .exec());
    }

}
