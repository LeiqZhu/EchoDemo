package exper.mybatis.tools;

import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MybatisUtil {

    private static SqlSessionFactory sqlSessionFactory = null;

    static{
        try {
            if (sqlSessionFactory ==null){
            InputStream is = Resources.getResourceAsStream("mybatis-config/mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //创建能执行映射文件中sql的sqlSession
    public static SqlSession getSession(){
        return sqlSessionFactory.openSession();
    }

    public static void selectOne(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {

        }finally {
            sqlSession.close();

        }
    }

    /**
     * 不适用xml配置文件创建SqlSessionFactory
     * @return
     */
    public static SqlSessionFactory createSSFFromXml(){
        Properties properties = new Properties();
        properties.setProperty("driver", "com.mysql.jdbc.Driver");
        properties.setProperty("url", "jdbc:mysql://127.0.0.1:3306/demo");
        properties.setProperty("username", "root");
        properties.setProperty("password", "123456");
        PooledDataSourceFactory pooledDataSourceFactory = new PooledDataSourceFactory();
        pooledDataSourceFactory.setProperties(properties);
        DataSource dataSource = pooledDataSourceFactory.getDataSource();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        return sqlSessionFactory;
    }


    public static void main(String[] args) {
        SqlSessionFactory sqlSessionFactory = createSSFFromXml();
        System.out.println(sqlSessionFactory);
    }
}
