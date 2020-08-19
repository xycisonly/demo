package com.xyc.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Value("${my.environment}")
    private String env;
    @Autowired
    @Qualifier("xycString1")
    private String str1;
    @Autowired
    @Qualifier("xycString2")
    private String str2;

    @GetMapping("/aa")
    public String aa(){
        System.out.println(str1);
        System.out.println(str2);
        return env;
    }


}
