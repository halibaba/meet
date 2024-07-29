package com.meet.dbscript.util;


import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import jxl.write.WriteException;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

public class InitDBSchemaUtil {

    private static Logger logger = Logger.getLogger(InitDBSchemaUtil.class);
    private static String catalog;
    private static String schema;
    //数据库mybatis的连接
//    private static SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory().openSession();
    //数据库连接
//    private static Connection conn = sqlSession.getConnection();
    private static Connection conn = null;

    //获取数据库信息
    public static Map<String[], List<Map<String, String>>> genereateInitSchema() throws SQLException, IOException, WriteException, ClassNotFoundException {
//        Class.forName("oracle.jdbc.driver.OracleDriver");
//        String url = "jdbc:oracle:thin:@10.22.21.10:1521:orcl";
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/cs?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";
        String username = "root";
        String password = "1234";
        Properties props = new Properties();
        props.put("remarksReporting", "true");
        props.put("user", username);
        props.put("password", password);
        conn = DriverManager.getConnection(url, props);
        logger.info("获取数据库表信息开始-----------");
        List<String[]> tables = new ArrayList<>();
        catalog = conn.getCatalog(); //目录名称，一般都为空
        //schema = "%";//数据库名，对于mysql来说用通配符
        schema = conn.getMetaData().getUserName();//数据库名称

        DatabaseMetaData dbmd = conn.getMetaData();
        ResultSet tablesResultSet = dbmd.getTables(catalog, schema, "%", new String[]{"TABLE"});
        try {
            while (tablesResultSet.next()) {
                String tableName = tablesResultSet.getString("TABLE_NAME");  //表名
                String remarks = tablesResultSet.getString("REMARKS");    //表注释
                String[] tableInfo = {tableName, remarks};

                String initDataTable = ConfigUtil.getInitDataTable();
                if (StringUtils.isNotEmpty(initDataTable)) {
                    List<String> initTablesName = Stream.of(initDataTable.split(",")).collect(Collectors.toList());
                    if (initTablesName.contains(tableName)) {
                        tables.add(tableInfo);
                    }
                }else{
                    tables.add(tableInfo);
                }
//                String shardingTables = ConfigUtil.getShardingTables();
//                if (StringUtils.isNotEmpty(shardingTables)) {
//                    List<String> needShardingTableNames = Stream.of(shardingTables.split(",")).collect(Collectors.toList());
//                    if (needShardingTableNames.contains(tableName)) {
//                        tables.add(tableInfo);
//                    }
//                } else {
//                    tables.add(tableInfo);
//                }
            }
        } finally {
            if (tablesResultSet != null) {
                tablesResultSet.close();
            }
        }
        //获取表信息
        Map<String[], List<Map<String, String>>> result = getTableSchema(conn, tables);
        logger.info("获取数据库表信息结束-----------");
        return result;
    }

    /**
     * 获取表信息
     *
     * @param conn
     * @param tables
     * @return
     */
    public static Map<String[], List<Map<String, String>>> getTableSchema(Connection conn, List<String[]> tables) {
        Map<String[], List<Map<String, String>>> result = new LinkedHashMap<>();
        try {
            for (String[] table : tables) {
                DatabaseMetaData dbmd = conn.getMetaData();
                //获取主键
                ResultSet primaryKeysRs = dbmd.getPrimaryKeys(null, schema, table[0].toUpperCase());
                List<String> primaryKeyList = new ArrayList<String>();
                while (primaryKeysRs.next()) {
                    String primaryKey = primaryKeysRs.getString("COLUMN_NAME");
                    primaryKeyList.add(primaryKey);
                }
                //获取索引
                ResultSet indexInfos = dbmd.getIndexInfo(null, schema, table[0].toUpperCase(), false, true);
                List<HashMap<String, String>> indexList = new ArrayList<>();

                while (indexInfos.next()) {
                    HashMap<String, String> indexMap = new HashMap<>();
                    String index = indexInfos.getString("INDEX_NAME");
                    String indexKey = indexInfos.getString("COLUMN_NAME");
                    if (!"PRIMARY".equalsIgnoreCase(index)) {
                        indexMap.put(indexKey, index);
                        indexList.add(indexMap);
                    }
                }
                ResultSet rs = dbmd.getColumns(null, schema, table[0].toUpperCase(), "%"); // 获取表列集合
                List<Map<String, String>> columnList = new ArrayList<>();
                logger.info("表名-----------" + table[0].toUpperCase());
                while (rs.next()) {
                    Map<String, String> map = new HashMap<>();
                    String colName = rs.getString("COLUMN_NAME");
                    if (primaryKeyList.contains(colName)) { // 是主键
                        map.put("isPk", "Y");
                    }
                    if (indexList.size() > 0) {
                        String indexName = "";
                        for (int i = 0; i < indexList.size(); i++) {
                            Map<String, String> index = indexList.get(i);
                            if (StringUtils.isNotEmpty(index.get(colName))) {
                                indexName = indexName.concat("," + index.get(colName));
                            }
                        }
                        if (StringUtils.isNotEmpty(indexName)) {
                            map.put("isIndex", indexName.substring(1));
                        }
                    }
                    map.put("columnCode", colName);
                    String dbType = rs.getString("TYPE_NAME");// 数据类型
                    String length = rs.getString("COLUMN_SIZE");// 长度
                    String digits = rs.getString("DECIMAL_DIGITS");// 精度
                    String defaultValue = rs.getString("COLUMN_DEF");// 默认值
                    String nullable = rs.getString("IS_NULLABLE");
                    if ("YES".equals(nullable)) { // 不允许为空
                        nullable = "";
                    } else {
                        nullable = "N";// 允许为空
                    }
                    String remarks = rs.getString("REMARKS");// 注释
                    if (remarks == null) {
                        remarks = "";
                    }
                    if (dbType.indexOf("(") > 0) {
                        dbType = dbType.substring(0, dbType.indexOf("("));
                    }
                    String valueType = JdbcDbTypeMap.getJavaType(dbType, digits, length);
                    if (valueType.equals("DATE") || valueType.equals("BLOB") || valueType.equals("CLOB") || valueType.equals("DATETIME") || valueType.equals("TIMESTAMP") || valueType.equals("MEDIUMTEXT")) {
                        map.put("length", "");
                        if (valueType.equals("DATE") || valueType.equals("TIMESTAMP") || valueType.equals("DATETIME")) {
                            digits = "";
                        }
                    } else {
                        map.put("length", length);
                    }

                    map.put("digits", digits);
                    map.put("defaultValue", JdbcDbTypeMap.getDateDefaultValue(valueType, defaultValue));
                    map.put("dbType", dbType);
                    map.put("valueType", valueType);
                    map.put("nullable", nullable);
                    map.put("remarks", remarks);
                    logger.info("列属性-----------" + map);
                    columnList.add(map);
                }
                result.put(table, columnList);
                primaryKeysRs.close();
                rs.close();
            }
            logger.info("数据库表数量-----------" + result.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            genereateInitSchema();
        } catch (Exception e) {
            logger.error("生成数据库初始化脚本异常", e);
        }
    }
}