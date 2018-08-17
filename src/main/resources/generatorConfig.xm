<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
    <context id="Mysql" targetRuntime="MyBatis3Simple">
        <!--https://mapperhelper.github.io/docs/3.usembg/，自动生成代码的通用mapper插件-->
        <plugin type="tk.mybatis.mapper.generator.MapperPlugin">
            <property name="mappers" value="tk.mybatis.mapper.common.Mapper"/>
            <!-- caseSensitive默认false，当数据库表名区分大小写时，可以将该属性设置为true -->
            <!-- <property name="caseSensitive" value="true"/>-->
        </plugin>
        <!-- 注释 -->
        <commentGenerator >
            <!-- 是否取消自动生成的注释 -->
            <!--<property name="suppressAllComments" value="false"/>-->
            <!-- 是否生成注释代时间戳-->
            <!-- <property name="suppressDate" value="true" />-->
        </commentGenerator>

        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                        connectionURL="jdbc:mysql://192.168.33.10:3306/message?useUnicode=true&characterEncoding=utf-8&useSSL=false"
                        userId="homestead"
                        password="secret">
            <!-- 针对mysql数据库 -->
            <property name="useInformationSchema" value="true"/>
        </jdbcConnection>

        <!-- 类型转换 -->
        <javaTypeResolver >
            <!-- 是否使用bigDecimal， false可自动转化以下类型（Long, Integer, Short, etc.） -->
            <property name="forceBigDecimals" value="false" />
        </javaTypeResolver>

        <!-- 生成实体类地址 -->
        <javaModelGenerator targetPackage="top.zhchenxin.mc.model" targetProject="\src">
            <!-- 是否在当前路径下新加一层schema,eg：fase路径com.oop.eksp.user.model， true:com.oop.eksp.user.model.[schemaName] -->
            <!--<property name="enableSubPackages" value="true" />-->
            <!-- 是否针对string类型的字段在set的时候进行trim调用 -->
            <!--<property name="trimStrings" value="true" />-->
        </javaModelGenerator>

        <!-- 生成mapxml文件 -->
        <sqlMapGenerator targetPackage="ttst.xml"  targetProject="\src">
            <!-- 是否在当前路径下新加一层schema,eg：fase路径com.oop.eksp.user.model， true:com.oop.eksp.user.model.[schemaName] -->
            <!-- <property name="enableSubPackages" value="true" />-->
        </sqlMapGenerator>

        <!-- 生成mapxml对应client，也就是接口dao -->
        <javaClientGenerator type="XMLMAPPER" targetPackage="top.zhchenxin.mc.model"  targetProject="\src">
            <!-- 是否在当前路径下新加一层schema,eg：fase路径com.oop.eksp.user.model， true:com.oop.eksp.user.model.[schemaName] -->
            <!--<property name="enableSubPackages" value="true" />-->
        </javaClientGenerator>

        <!-- 配置表信息 -->
        <!-- schema即为数据库名 tableName为对应的数据库表 domainObjectName是要生成的实体类 enable*ByExample
                是否生成 example类   -->
        <!--<table schema="jack" tableName="ALLTYPES" domainObjectName="Customer" >-->
        <!-- <property name="useActualColumnNames" value="true"/>
         <generatedKey column="ID" sqlStatement="DB2" identity="true" />
         <columnOverride column="DATE_FIELD" property="startDate" />-->
        <!-- 忽略列，不生成bean 字段 -->
        <!--<ignoreColumn column="FRED" />-->
        <!-- 指定列的java数据类型 -->
        <!-- <columnOverride column="LONG_VARCHAR_FIELD" jdbcType="VARCHAR" />-->
        <!-- </table>-->
        <!--<table tableName="student"  domainObjectName="Student" />-->

    </context>
</generatorConfiguration>