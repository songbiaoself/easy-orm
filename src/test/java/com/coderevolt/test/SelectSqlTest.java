package com.coderevolt.test;

import com.coderevolt.connect.SqlConnectFactory;
import com.coderevolt.model.StudentModel;
import com.coderevolt.model.StudentSubjectRelation;
import com.coderevolt.model.SubjectModel;
import com.coderevolt.sql.SqlExecutor;
import com.coderevolt.sql.config.SqlExecuteHook;
import com.coderevolt.sql.config.SqlOption;
import com.coderevolt.sql.core.SqlChainContext;
import com.coderevolt.sql.core.dql.SelectSqlGenerator;
import com.coderevolt.util.SubUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SelectSqlTest {

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        config.setUsername("root");
        config.setPassword("7758258");
        SqlConnectFactory.register("mysql", new HikariDataSource(config), true);

        HikariConfig config2 = new HikariConfig();
        config2.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        config2.setUsername("postgres");
        config2.setPassword("7758258");
        SqlConnectFactory.register("postgres", new HikariDataSource(config), false);
    }

    @Test
    public void listAll() throws SQLException {
        SelectSqlGenerator selectSqlGenerator = SqlExecutor.builder().selectChain()
                .select()
                .from(StudentModel.class)
                .where(SubUtil.eq(StudentModel::getId, 1).and().ne(StudentModel::getName, 1));

        System.out.println(selectSqlGenerator.list(StudentModel.class));
        System.out.println(selectSqlGenerator.listMap());

    }

    @Test
    public void getOne() throws SQLException {
        SelectSqlGenerator sqlGenerator = SqlExecutor.builder().selectChain()
                .select()
                .from(StudentModel.class)
                .limit(1);

        System.out.println(sqlGenerator.one(StudentModel.class));
        System.out.println(sqlGenerator.map());
    }

    @Test
    public void joinTest() throws SQLException {
        List<Map<String, Object>> listMap = SqlExecutor.builder().selectChain()
                .select(StudentModel::getName, StudentModel::getAge)
                .select(SubjectModel::getName, "subjectName")
                .from(StudentModel.class)
                .innerJoin(StudentSubjectRelation.class, SubUtil.eq(StudentModel::getId, StudentSubjectRelation::getStudentId))
                .innerJoin(SubjectModel.class, SubUtil.eq(StudentSubjectRelation::getSubjectId, SubjectModel::getId))
                .listMap();

        System.out.println(listMap);
    }

    @Test
    public void subSelect() throws SQLException {
        SelectSqlGenerator sqlGenerator = SqlExecutor.builder(SqlOption.builder().build()).selectChain()
                .select(StudentModel::getName)
                .from(StudentModel.class)
                .where(SubUtil.in(StudentModel::getId, ctx -> {
                    return SubUtil.subSelect()
                            .select(StudentSubjectRelation::getStudentId)
                            .from(StudentSubjectRelation.class)
                            .innerJoin(SubjectModel.class, SubUtil.eq(StudentSubjectRelation::getSubjectId, SubjectModel::getId));
                }));
//        System.out.println(sqlGenerator.list(StudentModel.class));
        System.out.println(sqlGenerator.listMap());
    }

    @Test
    public void blobTest() throws SQLException {
        System.out.println(SqlExecutor.builder(SqlOption.builder()
                        .sqlExecuteHook(new SqlExecuteHook() {
                            @Override
                            public void beforeRun(Connection connection, SqlChainContext sqlChainContext) {
                                System.out.println("执行前调用...");
                            }

                            @Override
                            public void afterRun(Connection connection, SqlChainContext sqlChainContext, Object result) {
                                System.out.println("执行后调用..." + result);
                            }
                        })
                        .build())
                .selectChain()
                .select()
                .from(SubjectModel.class)
                .where(SubUtil.like(SubjectModel::getBook, "%hello%"))
//                .listMap());
                .list(SubjectModel.class));
    }

    @Test
    public void funcTest() throws SQLException {
        List<StudentModel> studentModels = SqlExecutor.builder().selectChain()
                .select()
                .from(StudentModel.class).leftJoin(StudentSubjectRelation.class, SubUtil.eq(StudentModel::getId, StudentSubjectRelation::getStudentId))
                .leftJoin(SubjectModel.class, SubUtil.eq(StudentSubjectRelation::getSubjectId, SubjectModel::getId))
                .where(SubUtil.rt(StudentModel::getAge, 18).and().lt(StudentModel::getAge, 30).and().eq(SubjectModel::getName, "语文"))
                .list(StudentModel.class);

        System.out.println(studentModels);
    }

    @Test
    public void simpleTest() throws SQLException {
        List<StudentModel> studentModels = SqlExecutor.builder().selectChain()
                .select(StudentModel::getId, StudentModel::getName, StudentModel::getAge)
                .from(StudentModel.class)
                .where(SubUtil.like(StudentModel::getName, "张%").and().between(StudentModel::getAge, 18, 30))
                .list(StudentModel.class);

        System.out.println(studentModels);
    }

}
