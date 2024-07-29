package com.meet.dbscript.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigUtil {


    private static String outBaseDir = null;
    private static String sysName = null;
    private static String schemaFileName = null;
    private static String dataFileName = null;
    private static String initDataTable = null;
    private static String shardingTables = null;

    private static List<String> shardingSuffix = new ArrayList<>(367);


    private static Logger logger = Logger.getLogger(ConfigUtil.class);

    /**
     * 静态代码块中（只加载一次）,初始化配置数据
     */
    static {
        InputStream in = null;
        try {
            Properties props = new Properties();

            in = ConfigUtil.class.getResourceAsStream("/config.properties");

            // 加载文件
            props.load(in);
            outBaseDir = props.getProperty("outBaseDir");
            sysName = props.getProperty("sysName");
            schemaFileName = props.getProperty("schemaFileName");
            dataFileName = props.getProperty("dataFileName");
            initDataTable = props.getProperty("initDataTable");
            shardingTables = props.getProperty("shardingTables");
        } catch (Exception ex) {
            logger.error("初始化配置数据异常", ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("关闭输入流异常", e);
            }
        }
    }

    static {
        Integer[] monthArray = {1, 3, 5, 7, 8, 10, 12};
        List<Integer> monthList = Arrays.asList(monthArray);
        List<String> tableNameSuffix = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            if (monthList.contains(i)) {
                tableNameSuffix = add(i, 31, tableNameSuffix);
            } else {
                tableNameSuffix = add(i, 30, tableNameSuffix);
            }
        }
        tableNameSuffix.add("1301");
        shardingSuffix.addAll(tableNameSuffix);
    }

    public static List<String> add(Integer i, Integer maxDay, List<String> tableNameSuffix) {
        for (int j = 1; j <= maxDay; j++) {
            if (i == 2 && j == 30) {
                continue;
            }
            if (i < 10) {
                if (j < 10) {
                    String tag = "0" + i + "0" + j;
                    tableNameSuffix.add(tag);
                } else {
                    String tag = "0" + i + j;
                    tableNameSuffix.add(tag);
                }
            } else if (j < 10) {
                String tag = i + "0" + j;
                tableNameSuffix.add(tag);
            } else {
                String tag = i + "" + j + "";
                tableNameSuffix.add(tag);
            }
        }
        return tableNameSuffix;
    }


    /**
     * 获取初始化脚本存放的根路径
     */
    public static String getOutBaseDir() {
        return outBaseDir;
    }

    public static String getSysName() {
        return sysName;
    }

    public static String getSchemaFileName() {
        return schemaFileName;
    }

    public static String getDataFileName() {
        return dataFileName;
    }

    public static String getInitDataTable() {
        return initDataTable;
    }

    public static String getShardingTables() {
        return shardingTables;
    }

    public static List<String> getShardingSuffix() {
        return shardingSuffix;
    }

    public static void close(Statement stmt, ResultSet rs) {
        if (rs != null)
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("释放rs异常", e);
            }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.error("释放stmt异常", e);
            }
        }
    }
}
