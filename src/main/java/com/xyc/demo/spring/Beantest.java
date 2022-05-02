package com.xyc.demo.spring;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


public class Beantest implements InitializingBean, DisposableBean {


    @Override
    public void destroy() throws Exception {
        System.out.println("DisposableBean的销毁方法");
    }

    public Beantest(){
        System.out.println("chushihua");
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean 的初始化方法");
    }

    public void initMethod(){
        System.out.println("创建bean时指定的 initMethod 方法");
    }

    public void destroyMethod(){
        System.out.println("创建bean时指定的 destroyMethod 方法");
    }
    @PostConstruct
    public void postConstruct(){
        System.out.println("PostConstruct 的指定方法");
    }
    @PreDestroy
    public void postPreDestroy(){
        System.out.println("PreDestroy 的指定方法");
    }
}
