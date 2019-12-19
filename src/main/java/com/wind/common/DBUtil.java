package com.wind.common;

import org.apache.ibatis.session.*;
import java.io.IOException;
import java.io.Reader;
import static org.apache.ibatis.io.Resources.*;

/**
 * MyBatis 是一款优秀的持久层框架，它支持定制化 SQL、存储过程以及高级映射。
 * MyBatis 避免了几乎所有的 JDBC 代码和手动设置参数以及获取结果集。
 * MyBatis 可以使用简单的 XML 或注解来配置和映射原生信息，将接口和 Java 的 POJOs(Plain Ordinary Java Object)映射成数据库中的记录。
 *
 * 1.配置mysql数据库 √
 * 2.配置pom文件 √
 * 3.配置application.properties文件 ----(保存api地址等数据)
 * 4.配置databaseConfig.xml文件 ----(mybatis-config.xml，连接mysql的相关参数)
 * 5.配置testng.xml文件 √
 * 6.配置SQLMapper.xml文件 ----(sql执行语句)
 * 7.配置model文件 ----(loginModel，定义sql查询结果的返回集)
 * 8.配置config文件 ----(ExtentTestNGIReporterListener文件和TestConfig，定义报告、共用测试配置)
 * 9.配置utils文件 ----(ConfigFile文件和DBUtil，声明一个SqlSession)
 * 10.配置cases文件 ----(loginCase，保存在数据库中)
 * ————————————————————————————————————————————————————————————————————————————————————————————————————————
 * @Author mandy
 * @Create 2019/11/15 17:48
 */

public class DBUtil {
    public static SqlSession getSqlsession() throws IOException {

        //获取配置资源文件
        Reader reader = getResourceAsReader("mybatis-config.xml");

        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader);
        //sqlSession就是能够执行配置文件中的sql语句
        SqlSession sqlSession = factory.openSession();
        return sqlSession;
    }
}
