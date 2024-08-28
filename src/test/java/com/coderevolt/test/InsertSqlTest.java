package com.coderevolt.test;

import com.coderevolt.connect.SqlConnectFactory;
import com.coderevolt.model.StudentModel;
import com.coderevolt.model.SubjectModel;
import com.coderevolt.sql.SqlExecutor;
import com.coderevolt.util.SubUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

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
//                .insertInto(StudentModel.class) 等于 insert into student
                .insertInto(StudentModel.class, StudentModel::getName, StudentModel::getAge)
                .values(new StudentModel("老王", 20))
                .exec();

        System.out.println("执行结果: " + result);
    }

    @Test
    public void insertSelectTest() throws SQLException {

        System.out.println(SqlExecutor.builder().insertChain()
                .insertInto(StudentModel.class, StudentModel::getName)
                .select(SubUtil.subSelect()
                        .select(StudentModel::getName)
                        .from(StudentModel.class))
                .exec());
    }

    @Test
    public void blobTest() {
        System.out.println(SqlExecutor.builder().insertChain()
                .insertInto(SubjectModel.class)
                .values(new SubjectModel(null, "test", "hello".getBytes()))
                .exec());
    }

}
