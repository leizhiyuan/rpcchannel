/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.sofa.rpc.channel.demo1;

import com.alipay.sofa.rpc.channel.demo1.asm.AsmProxyFactory;
import com.alipay.sofa.rpc.channel.demo1.cglib.CglibProxyFactory;
import com.alipay.sofa.rpc.channel.demo1.factory.DynamicProxyFactory;
import com.alipay.sofa.rpc.channel.demo1.javaassist.JavassistByteProxyFactory;
import com.alipay.sofa.rpc.channel.demo1.javaassist.JavassistProxyFactory;
import com.alipay.sofa.rpc.channel.demo1.jdk.JdkProxyFactory;
import com.alipay.sofa.rpc.channel.demo1.service.CountService;
import com.alipay.sofa.rpc.channel.demo1.service.impl.CountServiceImpl;

import java.text.DecimalFormat;

/**
 * @author bystander
 * @version $Id: DynamicProxyPerformanceTest.java, v 0.1 2019年02月12日 17:19 bystander Exp $
 */
public class DynamicProxyPerformanceStarter {
    public static void main(String[] args) throws Exception {
        CountService delegate = new CountServiceImpl();

        long time = System.currentTimeMillis();

        DynamicProxyFactory jdkProxyFactory = new JdkProxyFactory();

        CountService jdkProxy = jdkProxyFactory.createProxy(CountService.class, delegate);
        time = System.currentTimeMillis() - time;
        System.out.println("Create JDK Proxy: " + time + " ms");

        time = System.currentTimeMillis();
        DynamicProxyFactory cglibProxyFactory = new CglibProxyFactory();

        CountService cglibProxy = cglibProxyFactory.createProxy(CountService.class, delegate);
        time = System.currentTimeMillis() - time;
        System.out.println("Create CGLIB Proxy: " + time + " ms");

        time = System.currentTimeMillis();
        DynamicProxyFactory javassistProxyFactory = new JavassistProxyFactory();

        CountService javassistProxy = javassistProxyFactory.createProxy(CountService.class, delegate);
        time = System.currentTimeMillis() - time;
        System.out.println("Create JAVAASSIST Proxy: " + time + " ms");

        time = System.currentTimeMillis();
        DynamicProxyFactory javassistByteProxyFactory = new JavassistByteProxyFactory();

        CountService javassistBytecodeProxy = javassistByteProxyFactory.createProxy(CountService.class, delegate);
        time = System.currentTimeMillis() - time;
        System.out.println("Create JAVAASSIST Bytecode Proxy: " + time + " ms");

        time = System.currentTimeMillis();
        DynamicProxyFactory asmProxyFactory = new AsmProxyFactory();
        CountService asmBytecodeProxy = asmProxyFactory.createProxy(CountService.class, delegate);
        time = System.currentTimeMillis() - time;
        System.out.println("Create ASM Proxy: " + time + " ms");
        System.out.println("================================================");

        for (int i = 0; i < 3; i++) {
            test(jdkProxy, "Run JDK Proxy: ");
            test(cglibProxy, "Run CGLIB Proxy: ");
            test(javassistProxy, "Run JAVAASSIST Proxy: ");
            test(javassistBytecodeProxy, "Run JAVAASSIST Bytecode Proxy: ");
            test(asmBytecodeProxy, "Run ASM Bytecode Proxy: ");
            System.out.println("------------------------------------------------");
        }
    }

    private static void test(CountService service, String label)
            throws Exception {
        service.count(); // warm up
        int count = 10000000;
        long time = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            service.count();
        }
        time = System.currentTimeMillis() - time;
        System.out.println(label + time + " ms, " + new DecimalFormat().format(count * 1000 / time) + " t/s");
    }


}