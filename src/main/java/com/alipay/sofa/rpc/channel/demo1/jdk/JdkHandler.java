/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.sofa.rpc.channel.demo1.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author bystander
 * @version $Id: JdkHandler.java, v 0.1 2019年02月12日 17:50 bystander Exp $
 */
public  class JdkHandler implements InvocationHandler {

    final Object delegate;

    public JdkHandler(Object delegate) {
        this.delegate = delegate;
    }

    public Object invoke(Object object, Method method, Object[] objects)
            throws Throwable {
        return method.invoke(delegate, objects);
    }
}