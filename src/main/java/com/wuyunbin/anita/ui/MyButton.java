package com.wuyunbin.anita.ui;

import com.wuyunbin.anita.constant.Constant;

import javax.swing.*;

/**
 * @author wuyunbin
 */
public class MyButton extends JButton {
    public MyButton() {
    }

    public MyButton(String text) {
        super(text);
    }

    public void setBounds(int index,int line){
        super.setBounds(Constant.btnElementWidth*index+30, Constant.lineHeight *line, Constant.btnWidth, Constant.elementHeight);
    }
}
