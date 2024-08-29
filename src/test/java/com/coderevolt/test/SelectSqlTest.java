package com.coderevolt.test;

import com.coderevolt.connect.SqlConnectFactory;
import com.coderevolt.model.StudentModel;
import com.coderevolt.model.StudentSubjectRelation;
import com.coderevolt.model.SubjectModel;
import com.coderevolt.sql.SqlExecutor;
import com.coderevolt.sql.core.symbol.SqlSort;
import com.coderevolt.util.SubUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;

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
    public void simpleTest() throws SQLException {
        List<StudentModel> studentModels = SqlExecutor.builder().selectChain()
                .select()
                .from(StudentModel.class)
                .list(StudentModel.class);

        System.out.println(studentModels);
    }

    @Test
    public void whereTest() throws SQLException {
        List<StudentModel> studentModels = SqlExecutor.builder().selectChain()
                .select()
                .from(StudentModel.class)
                .where(SubUtil.like(StudentModel::getName, "%张%").or().wrap(SubUtil.eq(StudentModel::getAge, 10).and().like(StudentModel::getHobby, "%球%")))
                .list(StudentModel.class);

        System.out.println(studentModels);
    }

    @Test
    public void groupTest() throws SQLException {
        List<StudentModel> studentModels = SqlExecutor.builder().selectChain()
                .sql("select studentModel.age, count(*)")
                .from(StudentModel.class)
                .groupBy(StudentModel::getAge)
                .having(SubUtil.gt(StudentModel::getAge, 18).and().lt(StudentModel::getAge, 30))
                .list(StudentModel.class);

        System.out.println(studentModels);
    }

    @Test
    public void orderTest() throws SQLException {
        List<StudentModel> studentModels = SqlExecutor.builder().selectChain()
                .select()
                .from(StudentModel.class)
                .order(SubUtil.order(StudentModel::getAge, SqlSort.DESC), SubUtil.order(StudentModel::getName, SqlSort.ASC))
                .list(StudentModel.class);

        System.out.println(studentModels);

    }

    @Test
    public void joinTest() throws SQLException {
        List<StudentModel> studentModels = SqlExecutor.builder().selectChain()
                .sql("select t1.*")
                .from(StudentModel.class, "t1")
                .innerJoin(StudentModel.class, "t2")
                .list(StudentModel.class);

        System.out.println(studentModels);

        List<Map<String, Object>> maps = SqlExecutor.builder().selectChain()
                .select()
                .from(StudentModel.class)
                .innerJoin(StudentSubjectRelation.class, SubUtil.eq(StudentModel::getId, StudentSubjectRelation::getStudentId))
                .listMap();

        System.out.println(maps);

        List<Map<String, Object>> maps1 = SqlExecutor.builder().selectChain()
                .select()
                .from(StudentModel.class)
                .leftJoin(StudentSubjectRelation.class, SubUtil.eq(StudentModel::getId, StudentSubjectRelation::getStudentId))
                .listMap();

        System.out.println(maps1);

        List<Map<String, Object>> maps2 = SqlExecutor.builder().selectChain()
                .select()
                .from(StudentModel.class)
                .rightJoin(StudentSubjectRelation.class, SubUtil.wrap(SubUtil.eq(StudentModel::getId, StudentSubjectRelation::getStudentId).and().sql("1 = 1")))
                .rightJoin(SubjectModel.class, SubUtil.eq(StudentSubjectRelation::getSubjectId, SubjectModel::getId))
                .listMap();

        System.out.println(maps2);

    }

    @Test
    public void subSelectTest() throws SQLException {
        List<StudentModel> studentModels = SqlExecutor.builder().selectChain()
                .select()
                .from(ctx -> {
                    return SubUtil.subSelect()
                            .select()
                            .from(StudentModel.class);
                }, "t1")
                .list(StudentModel.class);

        System.out.println(studentModels);

        List<StudentModel> studentModels1 = SqlExecutor.builder().selectChain()
                .select()
                .from(StudentModel.class, "t1")
                .innerJoin(ctx -> {
                    return SubUtil.subSelect()
                            .select()
                            .from(SubjectModel.class)
                            .where(SubUtil.exist(ctx2 -> {
                                return SubUtil.subSelect()
                                        .sql("select 1")
                                        .from(StudentSubjectRelation.class)
                                        .where(SubUtil.eq(StudentSubjectRelation::getSubjectId, SubjectModel::getId));
                            }));
                }, "t2")
                .list(StudentModel.class);
        System.out.println(studentModels1);

        List<StudentModel> studentModels2 = SqlExecutor.builder().selectChain()
                .select()
                .from(StudentModel.class, "t1")
                .where(SubUtil.notExist(SubUtil.subSql("select 0")).or().between(StudentModel::getAge, 18, 30).or())
                .where(SubUtil.sql("1 = 1").or().wrap(SubUtil.lt(StudentModel::getAge, 50).or().gt(StudentModel::getAge, 18)).or())
                .where(SubUtil.wrap(SubUtil.lq(StudentModel::getAge, 50).or().gq(StudentModel::getAge, 18)))
                .list(StudentModel.class);
        System.out.println(studentModels2);
    }

    @Test
    public void sqlInjectTest() throws SQLException {
        System.out.println(SqlExecutor.builder().selectChain()
                .select()
                .from(StudentModel.class)
                .where(SubUtil.like(StudentModel::getName, "%李四% or 1 = 1"))
                .listMap());
    }



}
