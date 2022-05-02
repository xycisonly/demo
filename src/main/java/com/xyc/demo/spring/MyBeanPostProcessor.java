package com.xyc.demo.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class
MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Beantest) {
            System.out.println("BeanPostProcessor 的 postProcessBeforeInitialization 方法");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Beantest) {
            System.out.println("BeanPostProcessor 的 postProcessAfterInitialization 方法");
        }
        return bean;
    }
}
