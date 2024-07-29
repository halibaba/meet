package com.meet.excelscript.main;

import com.meet.dbscript.util.InitDBSchemaUtil;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.log4j.Logger;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Excel生成sql
 */
public class ExcelToDB {

    private static Logger logger = Logger.getLogger(InitDBSchemaUtil.class);
    private static String catalog;
    private static String schema;
    private static Connection conn = null;

    private static Map<String,String> JavaTypeMapping=new HashMap<String,String>();

    static {
        JavaTypeMapping.put("INTEGER", "INT");
        JavaTypeMapping.put("INT", "INT");
        JavaTypeMapping.put("TINYINT", "INT");
        JavaTypeMapping.put("CHAR", "CHAR");
        JavaTypeMapping.put("VARCHAR", "VARCHAR");
        JavaTypeMapping.put("VARCHAR2", "VARCHAR");
        JavaTypeMapping.put("DECIMAL", "DECIMAL");
        JavaTypeMapping.put("DOUBLE", "DECIMAL");
        JavaTypeMapping.put("NUMERIC", "DECIMAL");
        JavaTypeMapping.put("NUMBER", "DECIMAL");
        JavaTypeMapping.put("DATE", "DATE");
        JavaTypeMapping.put("TIMESTAMP", "TIMESTAMP");
        JavaTypeMapping.put("TIME", "TIMESTAMP");
        JavaTypeMapping.put("DATETIME", "DATETIME");
    }

    public static void main(String[] args) throws Exception {

        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/meet_admin?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";
        String username = "root";
        String password = "1234";
        Properties props = new Properties();
        props.put("remarksReporting", "true");
        props.put("user", username);
        props.put("password", password);
        conn = DriverManager.getConnection(url, props);
        logger.info("-----------开始导入-------------");
        Statement statement = conn.createStatement();

        Workbook book = Workbook.getWorkbook(new File("/Users/huangjiayi/2021/caogao/text01.xls"));

        String sql = "";
        for (int i = 0; i < book.getSheets().length; i++) {
            Sheet sheet = book.getSheet(i);
            sql = "CREATE TABLE " + sheet.getCell(1, 0).getContents() +" (";
            for (int j = 2; j < sheet.getRows(); j++) {
                int columns = sheet.getColumns();

                String name = sheet.getCell(0,j).getContents() + " ";
                String type = JavaTypeMapping.get(sheet.getCell(1,j).getContents());
                String length = sheet.getCell(2,j).getContents();
                String degree = sheet.getCell(3,j).getContents();
                String isBlank = "N".equals(sheet.getCell(4,j).getContents()) ? " NOT NULL" : "Y".equals(sheet.getCell(4,j).getContents()) ? "" : "";
                String isPrimary = "N".equals(sheet.getCell(5,j).getContents()) ? "" : "Y".equals(sheet.getCell(5,j).getContents()) ? " PRIMARY KEY" : "";
                String defaults = "".equals(sheet.getCell(6,j).getContents()) ? "" : " default('"+ sheet.getCell(6,j).getContents() +"')";
                String desc = " COMMENT '"+ sheet.getCell(7,j).getContents() +"'";
                if("DECIMAL".equals(type)){
                    sql += name + type + "(" + length + "," + degree + ") " + isBlank + isPrimary + defaults + desc;
                }
                else {
                    if("".equals(length) || "0".equals(length)){
                        sql += name + type + isBlank + isPrimary + defaults + desc;
                    }else {
                        sql += name + type + "(" + length + ") " + isBlank + isPrimary + defaults + desc;
                    }

                }

                if(j == sheet.getRows() - 1){
                    sql += ") collate = utf8mb4_general_ci comment '"+ sheet.getCell(0, 0).getContents() +"';";
                }else {
                    sql += ",";
                }
            }
            try{
                statement.execute(sql);
                logger.info("执行成功 : " + sql);
            }catch (Exception e){
                logger.error("执行失败 : " + sql);
                e.printStackTrace();
            }

        }
    }
}
