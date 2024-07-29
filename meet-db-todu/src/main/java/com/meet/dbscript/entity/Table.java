package com.meet.dbscript.entity;

import java.util.List;
import java.util.Map;

public class Table {

	private String tableCode;

	private String tableName;

	private List<Map<String, String>> tableStructure;

	private List<List<String>> tableDatas;


	public List<Map<String, String>> getTableStructure() {
		return tableStructure;
	}

	public void setTableStructure(List<Map<String, String>> tableStructure) {
		this.tableStructure = tableStructure;
	}

	public List<List<String>> getTableDatas() {
		return tableDatas;
	}

	public void setTableDatas(List<List<String>> tableDatas) {
		this.tableDatas = tableDatas;
	}

	public String getTableCode() {
		return tableCode;
	}

	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


}
