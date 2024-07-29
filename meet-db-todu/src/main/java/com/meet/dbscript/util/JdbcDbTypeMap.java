package com.meet.dbscript.util;


import java.util.HashMap;
import java.util.Map;



/**
 * @author DongJu Chen
 *
 */
public class JdbcDbTypeMap {
	/**
	 * 类型转换映射Mapping
	 */
	private static Map<String,String[]> JavaTypeMapping=new HashMap<>();
	private static Map<String,String[]> JDBCMapping=new HashMap<String,String[]>();
	private static Map<String,String> importJavaPath=new HashMap<String,String>();
	
	static {
		JavaTypeMapping.put("DECIMAL", new String[]{"DECIMAL","BIGINT","DOUBLE","NUMERIC","NUMBER","DECIMAL_D","NUMERIC_D","NUMBER_D","UNSIGNED_D"});
		JavaTypeMapping.put("STRING", new String[]{"VARCHAR","VARCHAR2","NVARCHAR2","RAW"});
		JavaTypeMapping.put("CHAR", new String[]{"CHAR","NCHAR"});
		JavaTypeMapping.put("INTEGER", new String[]{"INTEGER","TINYINT","INT"});
		JavaTypeMapping.put("DATE", new String[]{"DATE","YEAR"});
		JavaTypeMapping.put("BLOB", new String[]{"BLOB","LONGBLOB","TEXT"});
		JavaTypeMapping.put("CLOB", new String[]{"CLOB","NCLOB"});
		JavaTypeMapping.put("TIMESTAMP", new String[]{"TIME","TIMESTAMP"});
		JavaTypeMapping.put("DATETIME", new String[]{"DATETIME"});
		JavaTypeMapping.put("MEDIUMTEXT", new String[]{"MEDIUMTEXT"});
		/*//无精度
		JavaTypeMapping.put("LONG", new String[]{"DECIMAL","NUMERIC","NUMBER"});
		//有精度时候
		JavaTypeMapping.put("BigDecimal", new String[]{"DECIMAL_D","NUMERIC_D","NUMBER_D"});*/
		
		
		JDBCMapping.put("DATE", new String[]{"DATE"});
		JDBCMapping.put("TIMESTAMP", new String[]{"TIMESTAMP","DATETIME"});
		JDBCMapping.put("VARCHAR", new String[]{"CHAR","VARCHAR","VARCHAR2"});
		JDBCMapping.put("INTEGER", new String[]{"INTEGER","INT"});
		JDBCMapping.put("NUMERIC", new String[]{"DECIMAL","NUMERIC","NUMBER"});
	
		//導入包
		importJavaPath.put("Date","java.util.Date");
		importJavaPath.put("BigDecimal","java.math.BigDecimal");
	}
	/**
	 * @param dbTypeCurrent
	 * @return
	 * @throws Exception
	 */
	public static String getJavaType(String dbTypeCurrent) throws Exception {
		for (String key : JavaTypeMapping.keySet()) {
			String[] dbTypeUnion=JavaTypeMapping.get(key);
			for (String dbType : dbTypeUnion) {
				if(dbType.equalsIgnoreCase(dbTypeCurrent)) {
					return key;
				}
			}
		}
		throw new Exception(dbTypeCurrent+"找不到正确的类型，请修改，JdbcDbTypeMap的JavaTypeMapping初始化");
	}
	public static String getJavaType(String dbType, String digits) throws Exception {
		if(digits!=null && !"".equals(digits)) {
			Integer digitInt=Integer.valueOf(digits);
			if(digitInt>0) {
				dbType+="_D";
			}
		}
		return getJavaType(dbType);
	}
	
	/**
	* 数据库表设计字段导出时的类型转换
	* @param dbType 数据类型
	* @param digits 精度
	* @param length 长度
	* @return 
	* @throws 
	*/
	public static String getJavaType(String dbType, String digits, String length) throws Exception {
		if(dbType.contains("TIMESTAMP")) {
			return getJavaType("TIMESTAMP");
		}
		if(digits!=null && !"".equals(digits)) {
			Integer digitInt=Integer.valueOf(digits);
			if(digitInt>0) {
				dbType+="_D";
			}
		}
		if(length!=null && !"".equals(length) && Integer.parseInt(length)>21845) {
			if(dbType.equals("VARCHAR")) {
				dbType = "BLOB";
			}
		}
		return getJavaType(dbType);
	}
	
	/**
	* 数据库表设计字段导出时日期类型的长度转换
	* @param 
	* @return 
	* @throws 
	*/
	public static String getDateLength(String dbType, String length) throws Exception {
		if(length!=null && !"".equals(length)) {
			if(dbType.equals("DATE")) {
				length = "";
			}
			else if(dbType.equals("DATETIME")) {
				length = "6";
			}
			else if(dbType.equals("TIMESTAMP")) {
				length = "6";
			}
		}
		return length;
	}
	
	/**
	* 数据库表设计字段导出时日期类型的默认值转换
	* @param 
	* @return 
	* @throws 
	*/
	public static String getDateDefaultValue(String dbType, String defaultValue) throws Exception {
		if(defaultValue!=null && !"".equals(defaultValue)) {
			defaultValue = defaultValue.trim();
			/*if(dbType.equals("DATE") || dbType.equals("DATETIME") || dbType.equals("TIMESTAMP")) {
				if("CURRENT_TIMESTAMP".equalsIgnoreCase(defaultValue)){//mysql
					return "${sysdate}";
				}
				if("sysdate".equalsIgnoreCase(defaultValue) || "Systimestamp".equalsIgnoreCase(defaultValue)){//oracle
					return "${sysdate}";
				}
			}*/
			//默认值去掉单引号
			return defaultValue.replaceAll("\'", "");
		}
		return defaultValue;
	}
	
	/**
	 * @param dbTypeCurrent
	 * @return
	 * @throws Exception mybatis 对应的
	 */
	public static String getJdbcType(String dbTypeCurrent) throws Exception {
		for (String key : JDBCMapping.keySet()) {
			String[] jdbcTypes=JDBCMapping.get(key);
			for (String jdbcType : jdbcTypes) {
				if(jdbcType.equalsIgnoreCase(dbTypeCurrent)) {
					return key;
				}
			}
		}
		throw new Exception(dbTypeCurrent+"找不到正确的类型，请修改，jdbcType的JDBCMapping初始化");
	}
	/**
	 * @param valueType
	 * @return
	 * @throws Exception
	 */
	public static String getImportStr(String valueType) throws Exception {
		if(importJavaPath.containsKey(valueType)) {
			return importJavaPath.get(valueType);
		}
		return null;
	}
}
