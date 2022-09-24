package com.wuyunbin.anita.ui;

import com.wuyunbin.anita.constant.Constant;

import javax.swing.*;

/**
 * @author wuyunbin
 */
public class MyTextField extends JTextField {
    public MyTextField() {
    }

    public MyTextField(String text) {
        super(text);
    }

    public void setBounds(int line){
        super.setBounds(Constant.elementLeft, Constant.lineHeight * line, Constant.elementWidth, Constant.elementHeight);
    }
}
