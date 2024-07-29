package com.meet.dbscript.util;

import com.meet.dbscript.entity.Table;
import jxl.CellView;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.Boolean;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName: CreateExcelUtil
 * @Description: 生成excel
 * @author: duchuangxin
 * @date: 2020/12/8 15:43
 */
public class CreateExcelUtil {
    private static Logger logger = Logger.getLogger(CreateExcelUtil.class);

    /**
     * 将表结构数据导出成Excel表
     *
     * @return
     * @throws
     */
    public static void createSchemaExcel(Map<String[], List<Map<String, String>>> tables) throws WriteException, IOException, IOException, WriteException {
        //创建文件输出路径
        logger.info("开始生成表结构文档-----------------------------------------");
        String outBaseDir = ConfigUtil.getOutBaseDir();
        String fileName = ConfigUtil.getSchemaFileName();
        String DOWNLOAD_URL = outBaseDir + "\\" + fileName;
        String sysName = ConfigUtil.getSysName();
        //创建文件输出流，输出Excel
        FileOutputStream os = new FileOutputStream(DOWNLOAD_URL);

        //创建工作薄
        WritableWorkbook workbook = Workbook.createWorkbook(os);

        /* 表格头部样式*/
        WritableFont head = new WritableFont(WritableFont.createFont("宋体"), 14, WritableFont.BOLD, true);//设置字体种类和黑体显示,字体为宋体,字号大小为14,不采用黑体显示
        WritableCellFormat headFormate = new WritableCellFormat(head);//生成一个单元格样式控制对象
        headFormate.setAlignment(jxl.format.Alignment.CENTRE);//单元格中的内容水平方向居中
        headFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//单元格的内容垂直方向居中

        /* 表格头部下划线超链接样式*/
        WritableFont underline = new WritableFont(WritableFont.createFont("宋体"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.NO_BOLD, false, UnderlineStyle.SINGLE);
        WritableCellFormat greyBackground = new WritableCellFormat(underline);
        greyBackground.setBackground(Colour.GRAY_25);//设置背景颜色为灰色
        greyBackground.setAlignment(jxl.format.Alignment.CENTRE);//单元格中的内容水平方向居中
        greyBackground.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//单元格的内容垂直方向居中


        /* 目录超链接样式*/
        WritableFont hyperlinksSection = new WritableFont(WritableFont.createFont("宋体"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.NO_BOLD, false, UnderlineStyle.SINGLE, Colour.BLUE);
        WritableCellFormat links = new WritableCellFormat(hyperlinksSection);
        links.setAlignment(jxl.format.Alignment.LEFT);//单元格中的内容水平方向居中
        links.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//单元格的内容垂直方向居中

        /* 表格头部第二行及底部样式*/
        WritableFont secondLine = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE);//设置字体种类和黑体显示,字体为宋体,字号大小为12,不采用黑体显示
        WritableCellFormat blueBackground = new WritableCellFormat(secondLine);//生成一个单元格样式控制对象
        blueBackground.setBackground(Colour.SKY_BLUE);//设置背景颜色为蓝色
        blueBackground.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
        blueBackground.setAlignment(jxl.format.Alignment.CENTRE);//单元格中的内容水平方向居中
        blueBackground.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//单元格的内容垂直方向居中

        /* 表格中间样式*/
        WritableFont midSection = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE);//设置字体种类和黑体显示,字体为宋体,字号大小为12,不采用黑体显示
        WritableCellFormat valueFormate = new WritableCellFormat(midSection);//生成一个单元格样式控制对象
        valueFormate.setAlignment(jxl.format.Alignment.LEFT);//单元格中的内容左对齐
        valueFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//单元格的内容垂直方向居中

        /* 数字样式*/
        WritableFont numberSection = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE);//设置字体种类和黑体显示,字体为宋体,字号大小为12,不采用黑体显示
        WritableCellFormat numberFormate = new WritableCellFormat(numberSection);//生成一个单元格样式控制对象
        numberFormate.setAlignment(jxl.format.Alignment.RIGHT);//单元格中的内容左对齐
        numberFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);//单元格的内容垂直方向居中

        /* 创建目录页*/
        WritableSheet sheetCatalog = workbook.createSheet("catalog", 0);
        sheetCatalog.mergeCells(0, 0, 1, 0);//添加合并单元格，第一个参数是起始列，第二个参数是起始行，第三个参数是终止列，第四个参数是终止行
        /* 表格目录头部*/
        Label catalogA1 = new Label(0, 0, sysName.toUpperCase() + "目录表", headFormate);
        sheetCatalog.addCell(catalogA1);
        /* 表格头部第二行*/
        sheetCatalog.setColumnView(0, 50);//设置列宽
        sheetCatalog.setColumnView(1, 30);
        Label catalogA2 = new Label(0, 1, "表名", blueBackground);
        Label catalogB2 = new Label(1, 1, "说明", blueBackground);
        sheetCatalog.addCell(catalogA2);
        sheetCatalog.addCell(catalogB2);

        AtomicInteger catalogRow = new AtomicInteger(2);//目录从第三行开始放入字段数据
        //获取需要分表的表名
        String shardingTables = ConfigUtil.getShardingTables();
        List<String> shardingSuffix = ConfigUtil.getShardingSuffix();
        tables.keySet().stream().forEach(tableNameArray -> {
            if (StringUtils.isNotEmpty(shardingTables)) {
                List<String> needShardingTableNames = Stream.of(shardingTables.split(",")).collect(Collectors.toList());
                if (needShardingTableNames.contains(tableNameArray[0])) {
                    shardingSuffix.stream().forEach(suffix -> {
                        String tableNameStr = tableNameArray[0] + suffix;
                        String tableRemarkStr = tableNameArray[1] + suffix;
                        try {
                            catalogRow.set(getCatalogRow(tables.get(tableNameArray), workbook.createSheet(tableNameStr, 1), headFormate, greyBackground, new Label(0, catalogRow.get(), tableNameStr, links), blueBackground, valueFormate, numberFormate, sheetCatalog, catalogRow, tableNameStr, tableRemarkStr,true,suffix));
                        } catch (WriteException e) {
                            e.printStackTrace();
                        }
                    });
                }
            } else {
                String tableNameStr = tableNameArray[0];
                String tableRemarkStr = tableNameArray[1];
                try {
                    catalogRow.set(getCatalogRow(tables.get(tableNameArray), workbook.createSheet(tableNameStr, 1), headFormate, greyBackground, new Label(0, catalogRow.get(), tableNameStr, links), blueBackground, valueFormate, numberFormate, sheetCatalog, catalogRow, tableNameStr, tableRemarkStr,false,null));
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
        });
        workbook.write();
        workbook.close();
        os.close();
        logger.info("表结构文档生成完成-----------------------------------------");
    }

    private static int getCatalogRow(List<Map<String, String>> maps, WritableSheet sheet1, WritableCellFormat headFormate, WritableCellFormat greyBackground, Label writableCell, WritableCellFormat blueBackground, WritableCellFormat valueFormate, WritableCellFormat numberFormate, WritableSheet sheetCatalog, AtomicInteger catalogRow, String tableName, String tableRemark, Boolean sharding, String suffix) throws WriteException {
        //创建新的一页
        WritableSheet sheet = sheet1;

        /* 创建目录超链接*/
        WritableHyperlink whl = new WritableHyperlink(0, catalogRow.get(), tableName, sheet, 0, 0);
        sheetCatalog.addHyperlink(whl);
        /* 表格目录数据部分*/
        sheetCatalog.addCell(writableCell);
        sheetCatalog.addCell(new Label(1, catalogRow.get(), tableRemark, valueFormate));
        catalogRow.incrementAndGet();
        /* 添加合并单元格，第一个参数是起始列，第二个参数是起始行，第三个参数是终止列，第四个参数是终止行*/
        sheet.mergeCells(1, 0, 6, 0);

        /* 表格头部*/
        Label A1 = new Label(0, 0, "表名", headFormate);
        Label B1 = new Label(1, 0, tableName.toUpperCase(), headFormate);
        Label C1 = new Label(7, 0, "返回目录", greyBackground);
        WritableHyperlink whlToCatalog = new WritableHyperlink(7, 0, "返回目录", sheetCatalog, 0, 0);
        sheet.addHyperlink(whlToCatalog);
        sheet.addCell(A1);
        sheet.addCell(B1);
        sheet.addCell(C1);

        /* 表格头部第二行*/
        /* 设置行高*/
        sheet.setRowView(1, 300, false);

        /* 设置列宽*/
            /*CellView cellView = new CellView();
            cellView.setAutosize(true); //设置自动大小
            sheet.setColumnView(0, cellView);//根据内容自动设置列宽
             */
        sheet.setColumnView(0, 25);
        sheet.setColumnView(1, 15);
        sheet.setColumnView(2, 10);
        sheet.setColumnView(3, 10);
        sheet.setColumnView(4, 10);
        sheet.setColumnView(5, 10);
        sheet.setColumnView(6, 30);
        sheet.setColumnView(7, 30);
        sheet.setColumnView(8, 20);

        Label A2 = new Label(0, 1, "字段名", blueBackground);
        Label B2 = new Label(1, 1, "类型", blueBackground);
        Label C2 = new Label(2, 1, "长度", blueBackground);
        Label D2 = new Label(3, 1, "精度", blueBackground);
        Label E2 = new Label(4, 1, "是否为空", blueBackground);
        Label F2 = new Label(5, 1, "是否主键", blueBackground);
        Label G2 = new Label(6, 1, "默认值", blueBackground);
        Label H2 = new Label(7, 1, "说明", blueBackground);
//        Label I2 = new Label(8, 1, "索引名称", blueBackground);
        sheet.addCell(A2);
        sheet.addCell(B2);
        sheet.addCell(C2);
        sheet.addCell(D2);
        sheet.addCell(E2);
        sheet.addCell(F2);
        sheet.addCell(G2);
        sheet.addCell(H2);
//        sheet.addCell(I2);

        int row = 2;//从第三行开始放入字段数据
        for (Map<String, String> value : maps) {
            Label cell1 = new Label(0, row, value.get("columnCode"), valueFormate);
            Label cell2 = new Label(1, row, value.get("valueType"), valueFormate);
            Label cell3 = new Label(2, row, value.get("length"), numberFormate);
            Label cell4 = new Label(3, row, value.get("digits"), numberFormate);
            Label cell5 = new Label(4, row, value.get("nullable"), valueFormate);
            Label cell6 = new Label(5, row, value.get("isPk"), valueFormate);
            Label cell7 = new Label(6, row, value.get("defaultValue"), valueFormate);
            Label cell8 = new Label(7, row, value.get("remarks"), valueFormate);
//            Label cell9 ;
//            if(sharding){
//                if(value.get("isIndex") != null){
//                    cell9 = new Label(8, row, value.get("isIndex")+ suffix, valueFormate);
//                }else{
//                    cell9 = new Label(8, row, value.get("isIndex"), valueFormate);
//                }
//            }else{
//                cell9 = new Label(8, row, value.get("isIndex"), valueFormate);
//            }

            sheet.addCell(cell1);
            sheet.addCell(cell2);
            sheet.addCell(cell3);
            sheet.addCell(cell4);
            sheet.addCell(cell5);
            sheet.addCell(cell6);
            sheet.addCell(cell7);
            sheet.addCell(cell8);
//            sheet.addCell(cell9);
            row++;
        }
        return catalogRow.get();
    }


    /**
     * 将表数据导出成Excel表
     *
     * @param tables<String, Table> tables 表数据
     * @return
     * @throws
     */
    public static void createDataExcel(Map<String, Table> tables) throws WriteException, IOException {
        //创建文件输出路径
        logger.info("开始生成初始化数据文档-----------------------------------------");
        String outBaseDir = ConfigUtil.getOutBaseDir();
        String dataFileName = ConfigUtil.getDataFileName();
        String DOWNLOAD_URL = outBaseDir + "\\" + dataFileName;
        String sysName = ConfigUtil.getSysName();
        //创建文件输出流，输出Excel
        FileOutputStream os = new FileOutputStream(DOWNLOAD_URL);

        //目录第一行样式
        WritableFont catalogFirst = new WritableFont(WritableFont.createFont("黑体"),        //字体
                13,    // 字号
                WritableFont.NO_BOLD,    // 粗体
                false,    // 斜体
                UnderlineStyle.NO_UNDERLINE,    // 下划线
                Colour.BLACK        // 字体颜色
        );
        WritableCellFormat catalogFirstFormate = new WritableCellFormat(catalogFirst);    //生成一个单元格样式控制对象
        catalogFirstFormate.setAlignment(jxl.format.Alignment.CENTRE);                    //设置对齐方式:单元格中的内容水平方向居中
        catalogFirstFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);    //设置对齐方式:单元格的内容垂直方向居中
        catalogFirstFormate.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);    //添加边框:薄
        catalogFirstFormate.setBackground(Colour.WHITE);                                //设置单元格的背景颜色:白色

        //目录第二行样式
        WritableFont catalogSecond = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.WHITE);
        WritableCellFormat catalogSecondFormate = new WritableCellFormat(catalogSecond);    //生成一个单元格样式控制对象
        catalogSecondFormate.setAlignment(jxl.format.Alignment.CENTRE);                        //设置对齐方式:单元格中的内容水平方向居中
        catalogSecondFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);        //设置对齐方式:单元格的内容垂直方向居中
        catalogSecondFormate.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);        //添加边框:薄
        catalogSecondFormate.setBackground(Colour.SKY_BLUE);                                //设置单元格的背景颜色:暗蓝绿色  

        //目录超链接样式
        WritableFont catalogLink = new WritableFont(WritableFont.createFont("宋体"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.NO_BOLD, false, UnderlineStyle.SINGLE, Colour.BLUE);
        WritableCellFormat catalogLinkFormate = new WritableCellFormat(catalogLink);
        catalogLinkFormate.setAlignment(jxl.format.Alignment.LEFT);
        catalogLinkFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        catalogLinkFormate.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);

        //sheet页表名样式
        WritableFont tableNameFont = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.BOLD, false);
        WritableCellFormat tableNameFormate = new WritableCellFormat(tableNameFont);
        tableNameFormate.setAlignment(jxl.format.Alignment.CENTRE);
        tableNameFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        tableNameFormate.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);

        //sheet页返回目录下划线超链接样式
        WritableFont returnCatalogLink = new WritableFont(WritableFont.createFont("宋体"), WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD, true, UnderlineStyle.SINGLE, Colour.BLUE);
        WritableCellFormat returnCatalogLinkFormate = new WritableCellFormat(returnCatalogLink);
        returnCatalogLinkFormate.setAlignment(jxl.format.Alignment.CENTRE);
        returnCatalogLinkFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        returnCatalogLinkFormate.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
        returnCatalogLinkFormate.setBackground(Colour.GRAY_25);//设置单元格的背景颜色:灰色

        //sheet页A列样式
        WritableFont colAFont = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.WHITE);
        WritableCellFormat colAFormate = new WritableCellFormat(colAFont);
        colAFormate.setAlignment(jxl.format.Alignment.LEFT);
        colAFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        colAFormate.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
        colAFormate.setBackground(Colour.DARK_RED);//设置单元格的背景颜色:深红色 

        //sheet页字段名样式
        WritableFont secondFont = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD, false);
        WritableCellFormat secondFormate = new WritableCellFormat(secondFont);
        secondFormate.setAlignment(jxl.format.Alignment.LEFT);
        secondFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        secondFormate.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
        secondFormate.setBackground(Colour.GOLD);//设置单元格的背景颜色:金色

        //sheet页左数据样式
        WritableFont leftDataFont = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE);
        WritableCellFormat leftDataFormate = new WritableCellFormat(NumberFormats.TEXT);//生成一个单元格文本样式控制对象
        leftDataFormate.setFont(leftDataFont);
        leftDataFormate.setAlignment(jxl.format.Alignment.LEFT);
        leftDataFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        leftDataFormate.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);

        //sheet页右数据样式
        WritableFont rightDataFont = new WritableFont(WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE);
        WritableCellFormat rightDataFormate = new WritableCellFormat(NumberFormats.TEXT);
        rightDataFormate.setFont(rightDataFont);
        rightDataFormate.setAlignment(jxl.format.Alignment.RIGHT);
        rightDataFormate.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        rightDataFormate.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);

        //创建工作薄
        WritableWorkbook workbook = Workbook.createWorkbook(os);

        //创建目录页
        WritableSheet sheetCatalog = workbook.createSheet("catalog", 0);
        sheetCatalog.mergeCells(0, 0, 2, 0);//添加合并单元格，第一个参数是起始列，第二个参数是起始行，第三个参数是终止列，第四个参数是终止行
        //表格目录头部
        Label catalogA1 = new Label(0, 0, sysName.toUpperCase() + "数据表目录", catalogFirstFormate);
        sheetCatalog.addCell(catalogA1);
        //表格头部第二行
        sheetCatalog.setColumnView(0, 50);//设置列宽
        sheetCatalog.setColumnView(1, 30);
        sheetCatalog.setColumnView(2, 20);
        Label catalogA2 = new Label(0, 1, "表名", catalogSecondFormate);
        Label catalogB2 = new Label(1, 1, "说明", catalogSecondFormate);
        Label catalogC2 = new Label(2, 1, "是否保留原始数据", catalogSecondFormate);
        sheetCatalog.addCell(catalogA2);
        sheetCatalog.addCell(catalogB2);
        sheetCatalog.addCell(catalogC2);

        int catalogRow = 2;//目录从第三行开始放入字段数据
        int sheetIndex = 1;
        for (String tableCode : tables.keySet()) {
            //创建新的一页
            WritableSheet sheet = workbook.createSheet(" ", sheetIndex);
            sheetIndex++;
            Table table = tables.get(tableCode);
            String tableName = table.getTableName();
            sheet.setName(tableCode);
            //创建目录超链接
            WritableHyperlink whl = new WritableHyperlink(0, catalogRow, tableCode, sheet, 0, 0);
            sheetCatalog.addHyperlink(whl);
            //表格目录数据部分
            sheetCatalog.addCell(new Label(0, catalogRow, tableCode, catalogLinkFormate));
            sheetCatalog.addCell(new Label(1, catalogRow, tableName, leftDataFormate));
            sheetCatalog.addCell(new Label(2, catalogRow, "y", rightDataFormate));
            catalogRow++;
            System.out.println(tableCode + "_" + tableName);
            //添加合并单元格，第一个参数是起始列，第二个参数是起始行，第三个参数是终止列，第四个参数是终止行
            sheet.mergeCells(1, 0, 3, 0);
            sheet.mergeCells(4, 0, 5, 0);

            //表格头部
            if (tableName == null) {
                tableName = "";
            }
            Label B1 = new Label(1, 0, tableCode.toUpperCase(), tableNameFormate);
            Label E1 = new Label(4, 0, "返回目录", returnCatalogLinkFormate);
            Label A1 = new Label(0, 0, "表名", colAFormate);
            Label A2 = new Label(0, 1, "列名", colAFormate);
            Label A3 = new Label(0, 2, "中文名", colAFormate);
            Label A4 = new Label(0, 3, "数据类型", colAFormate);
            Label A5 = new Label(0, 4, "数据", colAFormate);

            WritableHyperlink whlToCatalog = new WritableHyperlink(4, 0, "返回目录", sheetCatalog, 0, 0);
            sheet.addHyperlink(whlToCatalog);
            sheet.addCell(A1);
            sheet.addCell(B1);
            sheet.addCell(E1);
            sheet.addCell(A2);
            sheet.addCell(A3);
            sheet.addCell(A4);
            sheet.addCell(A5);

            List<Map<String, String>> tableStructure = table.getTableStructure();

            int col = 1;//从第二列开始放入字段名、中文名、数据类型
            for (Map<String, String> value : tableStructure) {
                /* 设置列宽*/
                CellView cellView = new CellView();
                cellView.setAutosize(true); //设置自动大小
                sheet.setColumnView(col, cellView);//根据内容自动设置列宽

                Label cell1 = new Label(col, 1, value.get("columnName"), secondFormate);    //字段名
                Label cell2 = new Label(col, 2, value.get("remarks"), secondFormate);        //中文名
                Label cell3 = new Label(col, 3, value.get("typeName"), secondFormate);        //数据类型
                sheet.addCell(cell1);
                sheet.addCell(cell2);
                sheet.addCell(cell3);
                col++;
                Label cell4 = new Label(col, 1, "Action", secondFormate);    //字段名
                Label cell5 = new Label(col, 2, "操作类型", secondFormate);        //中文名
                Label cell6 = new Label(col, 3, "STRING", secondFormate);
                sheet.addCell(cell4);
                sheet.addCell(cell5);
                sheet.addCell(cell6);
            }
            List<List<String>> tableDatas = table.getTableDatas();
            //写入数据
            int row = 4;//从第五行开始放入数据
            for (List<String> tableData : tableDatas) {
                for (int i = 0; i < tableData.size(); i++) {
                    Label cell = new Label(i + 1, row, tableData.get(i), leftDataFormate);
                    sheet.addCell(cell);
                }
                row++;
            }
        }
        workbook.write();
        workbook.close();
        os.close();
        logger.info("初始化数据文档生成完成-----------------------------------------");
    }
}

