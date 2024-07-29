package com.meet.dbscript.util;


import com.meet.dbscript.entity.Table;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class InitDBDataUtil {

	private static Logger logger = Logger.getLogger(InitDBDataUtil.class);
	private static String catalog;
	private static String schema;
    //数据库mybatis的连接
  	private static SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory().openSession();
  	//数据库连接
  	private static Connection conn  = sqlSession.getConnection();

	public static Map<String, Table> genereateInitData() throws Exception {
		List<String[]> tables = new ArrayList<>();
		Map<String,Table> result = new LinkedHashMap<>();
		catalog = conn.getCatalog(); //目录名称，一般都为空
		//schema = "%";//数据库名，对于mysql来说用通配符
		schema =conn.getMetaData().getUserName();//数据库名称
		DatabaseMetaData dbmd = conn.getMetaData();
		List<String> initDataTable = Arrays.asList(ConfigUtil.getInitDataTable().split(","));
		ResultSet tablesResultSet = dbmd.getTables(catalog,schema,"%",new String[]{"TABLE"});
		try{
			while(tablesResultSet.next()){
				String tableName = tablesResultSet.getString("TABLE_NAME");  //表名
				String remarks = tablesResultSet.getString("REMARKS");	//表注释
				if(initDataTable.contains(tableName)){
					String[] tableInfo = {tableName,remarks};
					tables.add(tableInfo);
				}
			}
		}finally{
			if(tablesResultSet != null){
				tablesResultSet.close();
			}
		}
		for(String[] tableInfo: tables) {
			Table table = new Table();
			logger.info("初始化表-----------"+tableInfo[0]+":"+tableInfo[1]);
			List<Map<String, String>> tableStructure = getTableStructure(conn, tableInfo);
			List<List<String>> tableDatas = getTableDatas(tableInfo,tableStructure);
			String tableCode = tableInfo[0];
			String tableName = tableInfo[1];
			table.setTableDatas(tableDatas);
			table.setTableStructure(tableStructure);
			table.setTableCode(tableCode);
			table.setTableName(tableName);
			result.put(tableCode, table);
		}
		logger.info("总表数:"+tables.size());
		return result;
	}

	/**
	 * @throws Exception
	 * @throws SQLException
	 * 根据表名查询表结构
	 * @return List<Map<String, String>>	表结构
	 * @throws
	 */
	private static List<Map<String, String>> getTableStructure(Connection conn, String[] table) throws Exception {
		List<Map<String, String>> columnList = new ArrayList<>();
		DatabaseMetaData dbmd =conn.getMetaData();
		catalog = conn.getCatalog(); //目录名称，一般都为空
		//schema = "%";//数据库名，对于mysql来说用通配符
		schema =conn.getMetaData().getUserName();//数据库名称
		ResultSet rs = dbmd.getColumns(null, schema, table[0].toUpperCase(), "%"); // 获取表列集合
		try{
			while (rs.next()) {
				Map<String, String> map = new HashMap<>();
				String columnName = rs.getString("COLUMN_NAME");	// 字段名

				String remarks = rs.getString("REMARKS");			// 字段注释
				String dbType = rs.getString("TYPE_NAME");// 数据类型
				String length = rs.getString("COLUMN_SIZE");// 长度
				String digits = rs.getString("DECIMAL_DIGITS");// 精度
				int ordinalPosition = rs.getInt("ORDINAL_POSITION");
				if (remarks == null) {
					remarks = "";
				}
				map.put("columnName", columnName);
				String typeName = JdbcDbTypeMap.getJavaType(dbType, digits, length);
				if(typeName.equals("DATETIME")) {
					typeName = "TIMESTAMP";
				}
				map.put("typeName", typeName);
				map.put("remarks", remarks);
				columnList.add(map);
			}
		}finally{
			if(rs != null){
				rs.close();
			}
		}
		return columnList;
	}

	/**
	 * 根据表名查询表数据
	 * @param table table tables 表名
	 * @return List<List<String>> 表数据
	 * @throws
	 */
	private static List<List<String>> getTableDatas(String[] table,List<Map<String, String>> tableStructure) {
		List<List<String>> resultData = new ArrayList<>();
		String where="";
		try {
			for (Map<String, String> tableSchema:tableStructure) {
                if(tableSchema.get("columnName").equalsIgnoreCase("STACID")){
					 where  = "  WHERE STACID='1'";
				}
			}
			String tableName = table[0].toUpperCase();
			String	sql = "SELECT * FROM " + tableName + where;
			logger.info("sql:"+sql);
			PreparedStatement pstmt = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			try{
				SimpleDateFormat pattern = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				int col = metaData.getColumnCount();
				while (rs.next()) {
					List<String> rowData = new ArrayList<>();
					for (int i = 1; i <= col; i++) {
						String valueStr = null;
						String columnName = metaData.getColumnName(i);
						String columnType = metaData.getColumnTypeName(i);
						 if("TIMESTAMP".equals(columnType)){
						    if(columnName.contains("_DATE")){
                                Timestamp  timeStamp = rs.getTimestamp(i);
                                if(timeStamp!=null){
                                    try{
                                        valueStr = pattern.format(timeStamp);
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }else{
                                    valueStr="";
                                }
                                rowData.add(valueStr);
                            }
						}else if("BLOB".equals(columnType)){//不处理Blob类型
							 valueStr = "";
                             rowData.add(valueStr);
						}else{
							rowData.add(rs.getString(i));
						}

					}
                    rowData.add("U");
					resultData.add(rowData);
				}
			}finally{
				if(pstmt != null){
					pstmt.close();
				}
				if(rs != null){
					rs.close();
				}
			}
		} catch (Exception e) {
			logger.error(table[0]+"表数据错误！", e);
			e.printStackTrace();
		}
		return resultData;
	}

	public static void main(String[] args) {
		try {
			genereateInitData();
		}catch (Exception e) {
			logger.error("生成数据库初始化数据脚本异常", e);
		}
	}
}