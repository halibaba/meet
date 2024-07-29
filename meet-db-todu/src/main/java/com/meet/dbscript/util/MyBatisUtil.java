package com.meet.dbscript.util;

import java.io.Reader;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;


public class MyBatisUtil {
	private static Logger logger = Logger.getLogger(MyBatisUtil.class);
	private  final static SqlSessionFactory sqlSessionFactory;
	
	static {
		String resource = "mybatis-config.xml";
		Reader reader = null;
		Properties mybatisProperties = null;
		try {
			reader = Resources.getResourceAsReader(resource);
			//读取属性配置文件
			mybatisProperties = Resources.getResourceAsProperties("dataSource.properties");
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader,mybatisProperties);
	}
	
	public static SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}
}
