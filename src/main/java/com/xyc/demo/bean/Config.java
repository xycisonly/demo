package com.xyc.demo.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public MyBeanPostProcessor getMyBeanPostProcessor(){
        return new MyBeanPostProcessor();
    }
    @Bean(initMethod = "initMethod",destroyMethod = "destroyMethod")
    public Beantest getBeantest(){
        return new Beantest();
    }
    @Bean("xycString1")
    public String getString1(){
        return new String("123");
    }
    @Bean(value = "xycString2")
    public String getString2(){
        return new String("321");
    }
}
