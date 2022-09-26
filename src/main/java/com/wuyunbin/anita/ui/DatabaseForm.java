package com.wuyunbin.anita.ui;

import com.wuyunbin.anita.constant.Constant;
import com.wuyunbin.anita.util.DatabaseUtil;
import com.wuyunbin.anita.xml.DatabaseXml;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @author wuyunbin
 */
@Slf4j
public class DatabaseForm extends JFrame {
    private Map<String, Map<String, String>> dbMap = null;
    private final DatabaseUtil databaseUtil = new DatabaseUtil();
    private Map<String, String> propertyMap = null;
    private String db = "";

    private final MyLabel txtDatabaseType = new MyLabel("数据库类型");
    private final MyComboBox<String> databaseTypes = new MyComboBox<>();
    private final MyLabel txtAddress = new MyLabel("数据库地址");
    private final MyTextField address = new MyTextField("127.0.0.1");
    private final MyLabel txtUsername = new MyLabel("数据库用户名");
    private final MyTextField username = new MyTextField("root");
    private final MyLabel txtPassword = new MyLabel("数据库密码");
    private final MyTextField password = new MyTextField("root");
    private final MyLabel txtDatabase = new MyLabel("数据库");
    private final MyComboBox<String> databases = new MyComboBox<>();
    private final JCheckBox hasPrefix = new JCheckBox("去掉表前缀");
    private final MyButton btnConnect = new MyButton("测试连接");
    private final MyButton btnSkip = new MyButton("跳过");


    public DatabaseForm() {
        initComponents();
    }

    private void initComponents() {

        this.setTitle("艾妮塔代码生成器");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 350);
        this.setLayout(null);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent evt) {
                formWindowClosed(evt);
            }

            @Override
            public void windowOpened(WindowEvent evt) {
                formWindowOpened(evt);
            }

            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        txtDatabaseType.setBounds(1);
        this.add(txtDatabaseType);

        databaseTypes.setModel(new DefaultComboBoxModel<>(new String[]{"--请选择--"}));
        databaseTypes.setBounds(1);
        this.add(databaseTypes);

        txtAddress.setBounds(2);
        this.add(txtAddress);

        address.setBounds(2);
        this.add(address);

        txtUsername.setBounds(3);
        this.add(txtUsername);

        username.setBounds(3);
        this.add(username);

        txtPassword.setBounds(4);
        this.add(txtPassword);

        password.setBounds(4);
        this.add(password);

        txtDatabase.setBounds(5);
        this.add(txtDatabase);

        databases.setModel(new DefaultComboBoxModel<>(new String[]{"--请选择数据库--"}));
        databases.setBounds(5);
        this.add(databases);

        hasPrefix.setBounds(Constant.elementLeft, Constant.lineHeight * 6, Constant.elementWidth, Constant.elementHeight);
        this.add(hasPrefix);

        btnConnect.setBounds(0, 7);
        btnConnect.addActionListener(this::connectDatabase);
        this.add(btnConnect);

        btnSkip.setBounds(1, 7);
        btnSkip.addActionListener(this::generator);
        this.add(btnSkip);
    }


    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        dispose();
        System.exit(0);
        System.out.println("释放");
    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {
        dispose();
        System.exit(0);
        System.out.println("释放");
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        setLocationRelativeTo(null);

        dbMap = com.wuyunbin.anita.xml.ConfigXml.readConfig();

        for (String key : dbMap.keySet()) {
            this.databaseTypes.addItem(key);
        }

    }


    private void generator(ActionEvent evt) {

        if ("下一步".equals(this.btnSkip.getText())) {
            this.btnSkip.setText("运行中...");
            this.btnSkip.setEnabled(false);

            try {
                db = Objects.requireNonNull(this.databases.getSelectedItem()).toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //设置数据库名称
            this.databaseUtil.setDbName(db);
            //获取指定数据库的属性
            this.databaseUtil.setDbType(Objects.requireNonNull(this.databaseTypes.getSelectedItem()).toString());

            propertyMap = dbMap.get(this.databaseUtil.getDbType());
            String url = propertyMap.get("url").replace("[ip]", this.address.getText());
            url = url.replace("[db]", db);

            this.databaseUtil.setDriverName(this.propertyMap.get("driverName"));
            this.databaseUtil.setUrl(url);
            this.databaseUtil.setDbName(this.db);
            this.databaseUtil.setIp(this.address.getText());
            this.databaseUtil.setUsername(this.username.getText());
            this.databaseUtil.setPassword(this.password.getText());
            this.databaseUtil.setHasPrefix(this.hasPrefix.isSelected() ? "1" : "0");

            if (!url.contains("&amp;") && url.indexOf("&") > 0) {
                url = url.replace("&", "&amp;");
            }

            this.propertyMap.put("url", url);

            ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                    2,
                    5,
                    1L,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(3),
                    Executors.defaultThreadFactory(),
                    new ThreadPoolExecutor.AbortPolicy()
            );

            threadPool.execute(() -> {
                try {
                    //获取当前文件夹下的模板目录下的所有文件夹
                    String basePath = new File("").getAbsolutePath() + File.separatorChar + "db";
                    log.info("bashPath:{}", basePath);
                    DatabaseXml.writeDatabaseXml(databaseUtil, propertyMap, basePath);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(
                            null,
                            "发生错误",
                            "错误详情请查看error.log",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }

                CodeForm codeForm = new CodeForm();
                codeForm.setVisible(true);
                codeForm.setDbInfo(db, username.getText(), password.getText());

                setVisible(false);
            });
        } else {
            CodeForm codeForm = new CodeForm();
            codeForm.setVisible(true);
            codeForm.setDbInfo(db, username.getText(), password.getText());
            setVisible(false);
        }


    }

    private void connectDatabase(ActionEvent evt) {
        //
        btnConnect.setText("连接中...");
        btnConnect.setEnabled(false);

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                2,
                5,
                1L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );

        threadPool.execute(() -> {
            try {
                DatabaseUtil databaseUtil = new DatabaseUtil();

                Map<String, String> map = dbMap.get(databaseTypes.getSelectedItem());
                databaseUtil.setDriverName(map.get("driverName"));
                String url = map.get("url").replace("[ip]", address.getText()).replace("[db]", "");

                databaseUtil.setUrl(url);

                databaseUtil.setUsername(username.getText());
                databaseUtil.setPassword(password.getText());

                List<String> catalogs = databaseUtil.getSchemas();
                databases.removeAllItems();
                for (String c : catalogs) {
                    databases.addItem(c);
                }

                JOptionPane.showMessageDialog(
                        null,
                        "连接成功",
                        "提示",
                        JOptionPane.PLAIN_MESSAGE
                );
                btnSkip.setText("下一步");

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,
                        "无法加载驱动类",
                        "提示",
                        JOptionPane.ERROR_MESSAGE
                );

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,
                        "无法连接数据库，请核对连接信息是否正确",
                        "提示",
                        JOptionPane.ERROR_MESSAGE
                );

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,
                        "发生错误",
                        "错误详情请查看error.log",
                        JOptionPane.INFORMATION_MESSAGE
                );

            }

            btnConnect.setEnabled(true);
            btnConnect.setText("测试连接");
        });
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new DatabaseForm().setVisible(true));
    }

}