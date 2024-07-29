package com.meet.dbscript.util;

import java.io.File;

public class FileUtil {

	/**
	 * 
	 * @Title: deleteDir 
	 * @Description: 迭代清空目录
	 * @param path
	 * @throws Exception
	 * @return: void
	 */
	public static void deleteDir(String path) throws Exception {
		File file = new File(path);
		if (file.isFile()) {
			file.delete();
		} else {
			File[] subFiles = file.listFiles();
			for (int i = 0; i < subFiles.length; i++) {
				deleteDir(subFiles[i].getPath());
				subFiles[i].delete();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		// 初始化脚本存放的根路径
		String outBaseDir = ConfigUtil.getOutBaseDir();
		deleteDir(outBaseDir);
		
	}

}
