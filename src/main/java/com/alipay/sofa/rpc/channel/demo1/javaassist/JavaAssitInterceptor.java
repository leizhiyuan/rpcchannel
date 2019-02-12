/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.sofa.rpc.channel.demo1.javaassist;

import javassist.util.proxy.MethodHandler;

import java.lang.reflect.Method;

/**
 * @author bystander
 * @version $Id: JavaAssitInterceptor.java, v 0.1 2019年02月12日 17:36 bystander Exp $
 */
public class JavaAssitInterceptor implements MethodHandler {
    final Object delegate;

    public JavaAssitInterceptor(Object delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        return thisMethod.invoke(delegate, args);
    }
}