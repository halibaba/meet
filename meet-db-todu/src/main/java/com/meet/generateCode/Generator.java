package com.meet.generateCode;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author D
 * @title Generator
 * @description mybatisPlus 代码生成工具
 * @date 2020/5/29 14:11
 */
public class Generator {

    /**
     * url: jdbc:oracle:thin:@10.22.20.84:1521:ORCL
     * username: sit_tvm_tvmc
     * password: efd4de1c
     */
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/meet_admin?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "1234";
    private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    /**
     * FILE-SETTING
     */
    private static final String SUPER_CONTROLLER = "";
    private static final String SUPER_ENTITY_CLASS = "com.meet.api.entity.common.BaseModel";
    private static final String PARENT_MODULES_PACKAGE = "com.meet";
    private static final String AUTHOR = "huangjiayi";
    private static final String TABLE_PREFIX = "ap_";
    private static final String PROJECT_PATH = System.getProperty("user.dir") + "/meet-admin/meet-admin-biz";


    /**
     * <p>
     * 读取控制台内容1111
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(("请输入" + tip + "："));
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }


    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        // 此处目录按照实际情况改变
        String projectPath = PROJECT_PATH;
        System.out.println(projectPath);
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor(AUTHOR);
        gc.setOpen(false);
        gc.setSwagger2(true);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        // 重新生成是否覆盖
        gc.setFileOverride(true);
        gc.setIdType(IdType.AUTO);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(DB_URL);
        dsc.setDriverName(DRIVER_CLASS_NAME);
        dsc.setUsername(DB_USERNAME);
        dsc.setPassword(DB_PASSWORD);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        String moduleName = scanner("模块名");
        pc.setModuleName(moduleName);
        pc.setParent(PARENT_MODULES_PACKAGE);
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setEntity("entity");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothings
            }
        };
        String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        templateConfig.setController("templates/controller.java.vm");
        templateConfig.setEntity("templates/entity.java.vm");
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig sc = new StrategyConfig();
        sc.setNaming(NamingStrategy.underline_to_camel);
        sc.setColumnNaming(NamingStrategy.underline_to_camel);
        sc.setSuperEntityClass(SUPER_ENTITY_CLASS);
        // 实体类过滤父类字段
        sc.setSuperEntityColumns(
                "description",
                "is_deleted",
                "start_date_active",
                "end_date_active",
                "create_date",
                "create_by",
                "last_update_date",
                "last_update_by"
        );
        sc.setSuperControllerClass(SUPER_CONTROLLER);
        sc.setEntityLombokModel(true);
        sc.setRestControllerStyle(true);
        // 加了就是生成部分 不加生成所有表的
        sc.setInclude(scanner("表名").toUpperCase());
//        sc.setTablePrefix(TABLE_PREFIX);
        sc.setEntitySerialVersionUID(true);
        sc.setControllerMappingHyphenStyle(true);
        //字段常量
        sc.setEntityColumnConstant(false);
        // build 模型
        sc.setEntityBuilderModel(true);
        sc.setEntityTableFieldAnnotationEnable(true);
        /**
         * 驼峰转连字符
         * <pre>
         *      <code>@RequestMapping("/managerUserActionHistory")</code> -> <code>@RequestMapping("/manager-user-action-history")</code>
         * </pre>
         */
//        sc.setControllerMappingHyphenStyle()
        mpg.setStrategy(sc);

        // 模版引擎设置
        mpg.setTemplateEngine(new VelocityTemplateEngine());
        mpg.execute();


    }
}
