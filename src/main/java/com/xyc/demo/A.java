package com.xyc.demo;

import sun.applet.AppletClassLoader;

public class A {
    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> aClass = AppletClassLoader.getSystemClassLoader().loadClass("java.lang.Object");
        System.out.println(new Object().toString());
        System.out.println();
    }
}
