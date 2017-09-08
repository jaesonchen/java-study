package com.asiainfo.mybatis;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlSessionFactoryUtil {
	private static SqlSessionFactory factory;
	
	static{
		
        String configName = "mybatis-config.xml";
        InputStream in;
        try {
            
            in = Resources.getResourceAsStream(configName);
            factory = new SqlSessionFactoryBuilder().build(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    //获取SqlSession
    public static SqlSession getSession() {
        return factory.openSession();
    }
    /**
	 * 获取SqlSession
	 * @param isAutoCommit 
	 *         true 表示创建的SqlSession对象在执行完SQL之后会自动提交事务
	 *         false 表示创建的SqlSession对象在执行完SQL之后不会自动提交事务，这时就需要我们手动调用sqlSession.commit()提交事务
	 * @return SqlSession
	 */
	public static SqlSession getSqlSession(boolean isAutoCommit) {
		return factory.openSession(isAutoCommit);
	}
}
