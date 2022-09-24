/*
 * CodeUtil.java
 * CodeUtil.java
 *
 * Created on __DATE__, __TIME__
 */

package com.wuyunbin.anita.ui;

import com.wuyunbin.anita.constant.Constant;
import com.wuyunbin.anita.template.Code;
import com.wuyunbin.anita.xml.XmlUtil;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import javax.swing.*;


/**
 * @author wuyunbin
 */
public class CodeForm extends JFrame {

    private final MyButton btnGenerate = new MyButton("生成代码");
    private final MyButton btnExit = new MyButton("关闭");
    private final MyButton btnStructFile = new MyButton("选择");
    private final MyButton btnTargetPath = new MyButton("选择");
    private final MyComboBox<String> templates = new MyComboBox<>();
    private final MyLabel txtTemplate = new MyLabel("模版");
    private final MyLabel txtUsername = new MyLabel("用户名");
    private final MyLabel txtPassword = new MyLabel("密码");
    private final MyLabel txtStructFile = new MyLabel("结构文档路径");
    private final MyLabel txtTargetPath = new MyLabel("代码生成路径");
    private final MyLabel txtProjectNameEn = new MyLabel("项目名(英文)");
    private final MyLabel txtProjectNameCn = new MyLabel("项目名(中文)");
    private final MyLabel txtPackageName = new MyLabel("包名");
    private final MyLabel txtAuthor = new MyLabel("作者");
    private final MyLabel txtDatabase = new MyLabel("数据库");

    private final MyTextField database = new MyTextField();
    private final MyTextField username = new MyTextField();
    private final MyTextField password = new MyTextField();
    private final MyTextField structFile = new MyTextField();
    private final MyTextField targetPath = new MyTextField();
    private final MyTextField projectNameEn = new MyTextField();
    private final MyTextField packageName = new MyTextField();
    private final MyTextField projectNameCn = new MyTextField();
    private final MyTextField author = new MyTextField();
    private final MyTextField prefix = new MyTextField();

    public CodeForm() {
        initComponents();
    }

    private void initComponents() {
        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("艾妮塔代码生成器");
        this.setSize(600, 800);
        this.setLayout(null);

        this.add(btnGenerate);
        this.add(btnExit);
        this.add(btnStructFile);
        this.add(btnTargetPath);
        this.add(templates);
        this.add(txtTemplate);
        this.add(txtUsername);
        this.add(txtPassword);
        this.add(txtStructFile);
        this.add(txtTargetPath);
        this.add(txtProjectNameEn);
        this.add(txtProjectNameCn);
        this.add(txtPackageName);
        this.add(txtAuthor);
        this.add(txtDatabase);
        this.add(database);
        this.add(username);
        this.add(password);
        this.add(structFile);
        this.add(targetPath);
        this.add(projectNameEn);
        this.add(packageName);
        this.add(projectNameCn);
        this.add(author);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }

            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        txtTemplate.setBounds(1);
        this.add(txtTemplate);

        templates.setModel(new DefaultComboBoxModel<>(new String[]{"--请选择模版--"}));
        templates.setBounds(1);
        this.add(templates);

        txtTargetPath.setBounds(2);
        this.add(txtTargetPath);

        targetPath.setBounds(2);
        this.add(targetPath);

        btnTargetPath.setBounds(Constant.elementLeft + Constant.elementWidth + 10, Constant.lineHeight * 2, Constant.btnWidth, Constant.elementHeight);
        this.add(btnTargetPath);

        txtStructFile.setBounds(3);
        this.add(txtStructFile);

        structFile.setBounds(3);
        this.add(structFile);

        btnStructFile.setBounds(Constant.elementLeft + Constant.elementWidth + 10, Constant.lineHeight * 3, Constant.btnWidth, Constant.elementHeight);
        this.add(btnStructFile);

        txtProjectNameEn.setBounds(4);
        this.add(txtProjectNameEn);

        projectNameEn.setBounds(4);
        this.add(projectNameEn);

        txtProjectNameCn.setBounds(5);
        this.add(txtProjectNameCn);

        projectNameCn.setBounds(5);
        this.add(projectNameCn);

        txtPackageName.setBounds(6);
        this.add(txtPackageName);

        packageName.setBounds(6);
        this.add(packageName);

        txtAuthor.setBounds(7);
        this.add(txtAuthor);

        author.setBounds(7);
        this.add(author);

        txtDatabase.setBounds(8);
        this.add(txtDatabase);

        database.setBounds(8);
        this.add(database);

        txtUsername.setBounds(9);
        this.add(txtUsername);

        username.setBounds(9);
        this.add(username);

        txtPassword.setBounds(10);
        this.add(txtPassword);

        password.setBounds(10);
        this.add(password);

        btnGenerate.setBounds(1,11);
        this.add(btnGenerate);


        btnGenerate.addActionListener(this::generate);

        btnExit.addActionListener(this::jButton2ActionPerformed);

        btnStructFile.addActionListener(this::setStructFile);

        btnTargetPath.addActionListener(this::setTargetPath);
    }

    private void formWindowClosing(WindowEvent evt) {
        dispose();
        System.exit(0);

    }

    private void formWindowOpened(WindowEvent evt) {

        setLocationRelativeTo(null);

        //获取当前文件夹下的模板目录下的所有文件夹
        //设定为当前文件夹
        File directory = new File(new File("").getAbsolutePath() + File.separatorChar + "模板");
        File[] listFiles = directory.listFiles();
        if (listFiles != null) {
            for (File f : listFiles) {
                if (f.isDirectory()) {
                    this.templates.addItem(f.getName());
                }
            }
        }
        this.structFile.setText(new File("").getAbsolutePath() + File.separatorChar + "db");

        Map<String, String> publicMap = XmlUtil.read("publicMap.xml");
        if (publicMap.size() > 0) {
            this.projectNameEn.setText(publicMap.get("project"));
            this.packageName.setText(publicMap.get("package"));
            this.projectNameCn.setText(publicMap.get("projectComment"));
            this.author.setText(publicMap.get("author"));
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    public void setDbInfo(String dbName, String dbuser, String dbpassword) {
        this.database.setText(dbName);
        this.username.setText(dbuser);
        this.password.setText(dbpassword);
    }

    private void generate(ActionEvent evt) {
        btnGenerate.setEnabled(false);
        btnGenerate.setText("代码生成中...");

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
                //路径map封装
                Map<String, String> pathMap = new HashMap<>();
                //获取当前文件夹下的模板目录下的所有文件夹
                String basePath = new File("").getAbsolutePath() + File.separatorChar + "模板" + File.separatorChar
                        + templates.getSelectedItem();//设定为当前文件夹

                pathMap.put("templetPath", basePath);
                pathMap.put("projectTempletPath", basePath + File.separatorChar + "工程模板");
                pathMap.put("tablleTempletPath", basePath + File.separatorChar + "表级模板");
                pathMap.put("columnTempletPath", basePath + File.separatorChar + "列级模板");
                pathMap.put("xmlPath", structFile.getText());
                pathMap.put("codePath", targetPath.getText());

                //全局替换符
                Map<String, String> publicMap = new HashMap<>();
                publicMap.put("project", projectNameEn.getText());
                publicMap.put("package", packageName.getText());

                publicMap.put("projectComment", projectNameCn.getText());
                publicMap.put("author", author.getText());

                publicMap.put("db", database.getText());
                publicMap.put("dbuser", username.getText());
                publicMap.put("dbpassword", password.getText());
                publicMap.put("prefix",prefix.getText());
                String s = packageName.getText().replace(".", ",");
                String[] paths = s.split(",");
                for (int i = 0; i < paths.length; i++) {
                    publicMap.put("path_" + String.valueOf(i + 1), paths[i]);
                }
                publicMap.put("path_all", s.replace(",", "/"));

                //暂存变量
                XmlUtil.write(publicMap, "publicMap.xml");
                //生成代码
                Code.create(pathMap, publicMap);
                JOptionPane.showMessageDialog(
                        null,
                        "代码生成成功",
                        "提示",
                        JOptionPane.PLAIN_MESSAGE
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
            btnGenerate.setEnabled(true);
            btnGenerate.setText("生成代码");
        });

    }

    private void setTargetPath(ActionEvent evt) {
        this.targetPath.setText(selectPath("选择代码生成路径"));
    }

    private void setStructFile(ActionEvent evt) {
        this.structFile.setText(selectPath("选择结构文档路径"));
    }


    /**
     * 选择路径
     *
     * @param title
     * @return
     */
    private String selectPath(String title) {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.showDialog(new JLabel(), title);
        File file = jfc.getSelectedFile();
        if (file == null) {
            return null;
        }
        return file.getPath();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new CodeForm().setVisible(true));
    }

}