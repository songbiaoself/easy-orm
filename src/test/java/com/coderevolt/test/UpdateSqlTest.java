package com.coderevolt.test;

import com.coderevolt.connect.SqlConnectFactory;
import com.coderevolt.model.StudentModel;
import com.coderevolt.sql.SqlExecutor;
import com.coderevolt.sql.config.SqlExecuteHook;
import com.coderevolt.sql.config.SqlOption;
import com.coderevolt.sql.core.SqlChainContext;
import com.coderevolt.util.SubUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class UpdateSqlTest {

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        config.setUsername("root");
        config.setPassword("7758258");
        SqlConnectFactory.register("mysql", new HikariDataSource(config), true);
    }

    @Test
    public void test() {
        System.out.println(SqlExecutor.builder(SqlOption.builder()
                        .sourceName("mysql")
                                .sqlExecuteHook(new SqlExecuteHook() {
                                    @Override
                                    public void beforeRun(Connection connection, SqlChainContext sqlChainContext) {
                                        System.out.println("beforeRun");
                                    }

                                    @Override
                                    public void afterRun(Connection connection, SqlChainContext sqlChainContext, Object result) {
                                        System.out.println("afterRun");
                                    }
                                })
                        .build())
                .updateChain()
                .update(StudentModel.class)
                .set(SubUtil.setNull(StudentModel::getName))
                .where(SubUtil.eq(StudentModel::getId, 3))
                .exec());
    }

    @Test
    public void updateTest() {
        List<StudentModel> list = new ArrayList<>();
        list.add(new StudentModel(4L, "test1"));
        list.add(new StudentModel(5L, "test2"));
        list.add(new StudentModel(6L, "test3"));
        System.out.println(SqlExecutor.builder().updateChain()
                .updateByIdBatch(list));
    }

    @Test
    public void simpleTest() {
        boolean result = SqlExecutor.builder().updateChain()
                .update(StudentModel.class)
                .set(SubUtil.set(StudentModel::getAge, 18))
                .where(SubUtil.eq(StudentModel::getName, "张三"))
                .exec();

        System.out.println("更新结果：" + result);
    }

}
