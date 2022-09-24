package com.wuyunbin.anita.xml;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuyunbin
 */
public class ConfigXml {

    public static Map<String, Map<String, String>> readConfig() {

        //mysql
        Map<String, String> mysqlMap = new HashMap<>(5);
        mysqlMap.put("databaseTYPE", "MYSQL");
        mysqlMap.put("driverName", "com.mysql.cj.jdbc.Driver");
        mysqlMap.put("url", "jdbc:mysql://[ip]:3306/[db]?characterEncoding=UTF8&nullCatalogMeansCurrent=true&useSSL=false");
        mysqlMap.put("dialect", "org.hibernate.dialect.MySQLInnoDBDialect");
        mysqlMap.put("generator", "<![CDATA[<generator class=\"native\"></generator>]]>");


        //oracle
        Map<String, String> oracleMap = new HashMap<>(5);
        oracleMap.put("databaseTYPE", "ORACLE");
        oracleMap.put("driverName", "oracle.jdbc.driver.OracleDriver");
        oracleMap.put("url", "jdbc:oracle:thin:@[ip]:1521:ORCL");
        oracleMap.put("dialect", "org.hibernate.dialect.Oracle10gDialect");
        oracleMap.put("generator", "<![CDATA[<generator class=\"org.hibernate.id.SequenceGenerator\"><param name=\"sequence\">[table]_seq</param></generator>]]>");


        Map<String, Map<String, String>> map = new HashMap<>();
        map.put("MYSQL", mysqlMap);
        map.put("ORACLE", oracleMap);
        return map;
    }

}
