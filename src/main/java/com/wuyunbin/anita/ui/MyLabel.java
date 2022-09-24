package com.wuyunbin.anita.ui;

import com.wuyunbin.anita.constant.Constant;

import javax.swing.*;

/**
 * @author wuyunbin
 */
public class MyLabel extends JLabel {
    public MyLabel(String text) {
        super(text);
    }

    public MyLabel() {
    }

    public void setBounds(int line){
        super.setBounds(Constant.left, Constant.lineHeight * line, Constant.labelWidth, Constant.elementHeight);
    }
}
