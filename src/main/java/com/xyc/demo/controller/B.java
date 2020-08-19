package com.xyc.demo.controller;

import com.xyc.demo.A;
import org.springframework.stereotype.Component;

@Component("bbbb")
public class B {
    public A getA(){
        return new A();
    }
}
