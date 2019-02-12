/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.sofa.rpc.channel.demo1.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author bystander
 * @version $Id: CglibInterceptor.java, v 0.1 2019年02月12日 17:42 bystander Exp $
 */
public class CglibInterceptor implements MethodInterceptor {
    final Object delegate;

    public CglibInterceptor(Object delegate) {
        this.delegate = delegate;
    }

    public Object intercept(Object object, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return methodProxy.invoke(delegate, objects);
    }
}