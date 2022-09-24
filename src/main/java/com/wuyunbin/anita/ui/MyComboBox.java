package com.wuyunbin.anita.ui;

import com.wuyunbin.anita.constant.Constant;

import javax.swing.*;
import java.util.Vector;

/**
 * @author wuyunbin
 */
public class MyComboBox<T> extends JComboBox<T> {
    public MyComboBox(ComboBoxModel<T> aModel) {
        super(aModel);
    }

    public MyComboBox(T[] items) {
        super(items);
    }

    public MyComboBox(Vector<T> items) {
        super(items);
    }

    public MyComboBox() {
    }

    public void setBounds(int line){
        super.setBounds(Constant.elementLeft, Constant.lineHeight * line, Constant.elementWidth, Constant.elementHeight);
    }
}
