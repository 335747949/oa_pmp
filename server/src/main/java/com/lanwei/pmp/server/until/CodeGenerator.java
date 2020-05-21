package com.lanwei.pmp.server.until;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;
import java.util.Scanner;
/**
 * @author lanwei
 * @email 335747949@qq.com
 */

/**
 * mybatis plus 代码生成器
 */
public class CodeGenerator {


    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            /*if (StringUtils.isBlank(ipt)) {
                return ipt;
            }*/
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }


    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator autoGenerator = new AutoGenerator();

        // 数据源配置
        autoGenerator.setDataSource(new DataSourceConfig()
                .setDriverName("com.mysql.cj.jdbc.Driver")
                // 设置数据库类型
                .setDbType(DbType.MYSQL)
                // 数据库连接账户和密码
                .setUsername("root")
                .setPassword("123456")
                // 数据库连接的url
                .setUrl("jdbc:mysql://localhost:3306/pmp?useUnicode=true&characterEncoding=utf8&tinyInt1isBit=false&useSSL=false&useAffectedRows=true&serverTimezone=CTT")
                // 类型转换，默认由 dbType 类型决定选择对应数据库内置实现，实现 ITypeConvert 接口自定义数据库 字段类型 转换为自己需要的 java 类型
                .setTypeConvert(new MySqlTypeConvert() {
                    @Override
                    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                        System.out.println("转换类型：" + fieldType);

                        String t = fieldType.toLowerCase();
                        if (t.contains("tinyint(1)")) {
                            return DbColumnType.INTEGER;
                        }
                        return super.processTypeConvert(globalConfig, fieldType);

                    }
                })
        );

        String projectPath = System.getProperty("user.dir");
        // 全局配置
        autoGenerator.setGlobalConfig(new GlobalConfig()
                // 输出目录
                .setOutputDir(projectPath+ "server/src/main/java")
                // 是否覆盖
                .setFileOverride(true)
                //主键策略
                .setIdType(IdType.AUTO)
                //swagger注解
                .setSwagger2(true)
                // 时间格式
                .setDateType(DateType.ONLY_DATE)
                // 开启AR模式
                .setActiveRecord(false)
                // XML二级缓存
                .setEnableCache(false)
                // 生成ResultMap
                .setBaseResultMap(true)
                // 生成 sql片段
                .setBaseColumnList(true)
                // 自动打开生成后的文件夹
                .setOpen(false)
                // 所有文件的生成者
                .setAuthor("lanwei")
        );

        // 包配置
        PackageConfig packageConfig = new PackageConfig()
                // 基本包路径
                .setParent("com.lanwei.pmp.server")
                .setModuleName(scanner("模块名"))
                // 设置Mapper包名
                .setMapper("mapper")
                .setService("service")
                .setController("controller")
                .setEntity("entity");
        autoGenerator.setPackageInfo(packageConfig);

        // 策略配置
        autoGenerator.setStrategy(new StrategyConfig()
                // 需要生成的表
                .setInclude(scanner("表名"))
                // 实体类使用Lombok
                .setEntityLombokModel(true)
                // 表名生成策略,下划线转驼峰
                .setNaming(NamingStrategy.underline_to_camel)
                // 字段名生成策略,下划线转驼峰
                .setColumnNaming(NamingStrategy.underline_to_camel)
                // 可以继承父实体类，没有可以不用配置
                //.setSuperEntityClass("com.lanwei.pmp.parent.entity.BaseEntity")
                // 自定义基础的Entity类的公共字段
                //.setSuperEntityColumns("id","deleted","create_time","update_time")
                // 生成 @RestController 控制器
                .setRestControllerStyle(true)
                // 驼峰转连字符
                .setControllerMappingHyphenStyle(true)
                // 逻辑删除属性名称
                //.setLogicDeleteFieldName("deleted")
        );


        // 注入自定义配置
        autoGenerator.setCfg(new InjectionConfig() {
            @Override
            public void initMap() {
//                Map<String, Object> map = new HashMap<>(1);
//                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
//                this.setMap(map);
            }
        }.setFileOutConfigList(Collections.singletonList(
                new FileOutConfig("/templates/mapper.xml.ftl") {
                    // 自定义Mapper.xml输出路径
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        return projectPath + "/src/main/resources/mapper/" + packageConfig.getModuleName()
                                + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                    }
                })));


        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        // 不生成xml文件
        templateConfig.setXml(null);
        autoGenerator.setTemplate(templateConfig);
        // 选择了非默认引擎，需要在 AutoGenerator 中 设置模板引擎
        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());

        autoGenerator.execute();
    }

}
