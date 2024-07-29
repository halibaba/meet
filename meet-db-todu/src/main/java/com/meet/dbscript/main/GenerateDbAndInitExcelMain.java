package com.meet.dbscript.main;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.meet.dbscript.entity.Table;
import com.meet.dbscript.util.ConfigUtil;
import com.meet.dbscript.util.CreateExcelUtil;
import com.meet.dbscript.util.InitDBDataUtil;
import com.meet.dbscript.util.InitDBSchemaUtil;
import org.apache.log4j.Logger;


/**
 * @ClassName: GenerateDbAndInitExcelMain
 * @Description: 生成数据库初始化文档启动类
 * @author: duchuangxin
 * @date: 2020-12-10
 */
public class GenerateDbAndInitExcelMain {

	private static Logger logger = Logger.getLogger(GenerateDbAndInitExcelMain.class);
	
	public static void main(String[] args) {
		try {
			//初始化配置
			initConfig();
			//获取表结构信息
			Map<String[], List<Map<String, String>>> tablesSchemaMap = InitDBSchemaUtil.genereateInitSchema();
			//获取初始化表相关信息
			Map<String, Table> tableDataMap = InitDBDataUtil.genereateInitData();
			//生成表结构文档
			CreateExcelUtil.createSchemaExcel(tablesSchemaMap);
			//生成表初始化数据文档
			CreateExcelUtil.createDataExcel(tableDataMap);
		}catch (Exception e) {
			logger.error("生成数据库初始化脚本异常", e);
		}
	}
	
	private static void initConfig() {
		try {
			//初始化脚本存放的根路径
			String outBaseDir = ConfigUtil.getOutBaseDir();

			File outDir = new File(outBaseDir);
			if (!outDir.exists()) {
				outDir.mkdir();
			}

		} catch (Exception ex) {
			logger.error("加载系统配置文件失败", ex);
		}
	}
}