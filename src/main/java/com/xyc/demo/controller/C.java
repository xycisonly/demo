package com.xyc.demo.controller;

import com.xyc.demo.A;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
@Component
public class C {
    @Resource(name = "bbbb")
    public A a;
}
