package com.wuyunbin.anita.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.wuyunbin.anita.entity.Column;
import com.wuyunbin.anita.entity.Table;
import com.wuyunbin.anita.xml.XmlUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wuyunbin
 */
@Slf4j
@Data
public class DatabaseUtil {
    /**
     * 数据库类型
     */
    private String dbType;
    private String driverName;
    private String username;
    private String password;
    private String url;
    /**
     * 数据库名称
     */
    private String dbName;
    private String ip;
    private String hasPrefix;


    /**
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public List<String> getSchemas() throws ClassNotFoundException, SQLException {

        log.info("url:{},username:{},password:{}", url, username, password);
        Class.forName(driverName);
        Connection connection = java.sql.DriverManager.getConnection(url, username, password);
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet rs = metaData.getCatalogs();
        List<String> list = new ArrayList<String>();
        while (rs.next()) {
            list.add(rs.getString(1));
        }
        rs.close();
        connection.close();
        return list;
    }

    /**
     * 获取表及字段信息
     *
     * @param
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public List<Table> getDbInfo() {
        List<Table> list = new ArrayList<>();
        try{
            //加载转换器
            Map<String, String> convertMap = XmlUtil.readNu("typeConverter.xml");

            Class.forName(driverName);
            Properties props = new Properties();
            props.put("remarksReporting", "true");
            props.put("user", username);
            props.put("password", password);
            if (dbName != null) {
                url = url.replace("[db]", dbName);
            }

            if (ip != null && !"".equals(ip)) {
                url = url.replace("[ip]", ip);
            } else {
                url = url.replace("[ip]", "127.0.0.1");
            }

            Connection connection = java.sql.DriverManager.getConnection(url, props);


            DatabaseMetaData metaData = connection.getMetaData();

            String schema = null;
            String catalog = null;
            //如果是oracle数据库
            if (dbType != null && dbType.toUpperCase().contains("ORACLE")) {
                schema = username.toUpperCase();
                catalog = connection.getCatalog();
            }
            ResultSet tables = metaData.getTables(catalog, schema, null, new String[]{"TABLE"});

            while (tables.next()) {
                Table table = new Table();
                String tableName = tables.getString("TABLE_NAME");

                //如果为垃圾表
                if (tableName.contains("=") || tableName.contains("$")) {
                    continue;
                }

                //判断 表名为全大写 ，则转换为小写
                if (tableName.toUpperCase().equals(tableName)) {
                    table.setName(tableName.toLowerCase());
                } else {
                    table.setName(tableName);
                }

                table.setComment(tables.getString("REMARKS"));

                //获得主键
                ResultSet primaryKeys = metaData.getPrimaryKeys(catalog, schema, tableName);
                List<String> keys = new ArrayList<>();
                while (primaryKeys.next()) {
                    String keyname = primaryKeys.getString("COLUMN_NAME");
                    //判断 表名为全大写 ，则转换为小写
                    if (keyname.toUpperCase().equals(keyname)) {
                        //转换为小写
                        keyname = keyname.toLowerCase();
                    }
                    keys.add(keyname);
                }
                log.info("信息：" + catalog + "   " + schema + "   " + tableName);

                //获得所有列
                ResultSet columns = metaData.getColumns(catalog, schema, tableName, null);

                List<Column> columnList = new ArrayList<>();
                while (columns.next()) {
                    Column column = new Column();
                    String columnName = columns.getString("COLUMN_NAME");
                    //判断 表名为全大写 ，则转换为小写
                    if (columnName.toUpperCase().equals(columnName)) {
                        //转换为小写
                        columnName = columnName.toLowerCase();
                    }
                    column.setColumnName(columnName);

                    String columnDbType = columns.getString("TYPE_NAME");
                    //数据库原始类型
                    column.setColumnDbType(columnDbType);
                    //获取转换后的类型
                    String typeName = convertMap.get(columnDbType);
                    if (typeName == null) {
                        typeName = columns.getString("TYPE_NAME");
                    }
                    column.setColumnType(typeName);
                    //备注
                    String remarks = columns.getString("REMARKS");
                    if (remarks == null) {
                        remarks = columnName;
                    }
                    column.setColumnComment(remarks);
                    //如果该列是主键
                    if (keys.contains(columnName)) {
                        column.setColumnKey("PRI");
                        table.setKey(column.getColumnName());
                    } else {
                        column.setColumnKey("");
                    }
                    //小数位数
                    int decimalDigits = columns.getInt("DECIMAL_DIGITS");
                    if (decimalDigits > 0) {
                        //如果是小数则设置为Double
                        column.setColumnType("Double");
                    }

                    column.setDecimalDigits(decimalDigits);
                    //字段长度
                    column.setColumnSize(columns.getInt("COLUMN_SIZE"));

                    columnList.add(column);
                }
                columns.close();
                table.setColumns(columnList);

                list.add(table);

            }
            tables.close();
            connection.close();

        }catch(ClassNotFoundException|SQLException e){
            e.printStackTrace();
        }
        return list;
    }
}
