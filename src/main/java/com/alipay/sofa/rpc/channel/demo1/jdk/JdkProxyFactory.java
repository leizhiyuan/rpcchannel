/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.sofa.rpc.channel.demo1.jdk;

import com.alipay.sofa.rpc.channel.demo1.factory.DynamicProxyFactory;
import com.alipay.sofa.rpc.channel.demo1.service.CountService;

import java.lang.reflect.Proxy;

/**
 * @author bystander
 * @version $Id: JdkProxyFactory.java, v 0.1 2019年02月12日 18:02 bystander Exp $
 */
public class JdkProxyFactory implements DynamicProxyFactory {
    public <T> T createProxy(Class<T> type, Object delegate) {
        T jdkProxy = (T) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
                new Class[]{CountService.class}, new JdkHandler(delegate));

        return jdkProxy;
    }
}