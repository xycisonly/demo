package com.xyc.demo;

import com.xyc.demo.controller.B;

import java.beans.PropertyEditorSupport;

public class AEditor extends PropertyEditorSupport {

    @Override
    public void setValue(Object value){
        super.setValue(((B)value).getA());
    }
}
