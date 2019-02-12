/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.sofa.rpc.channel.demo1.bytebuddy;

import com.alipay.sofa.rpc.channel.demo1.factory.DynamicProxyFactory;
import com.alipay.sofa.rpc.core.exception.SofaRpcRuntimeException;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @author bystander
 * @version $Id: BytebuddyProxyFactory.java, v 0.1 2019年02月12日 18:09 bystander Exp $
 */
public class BytebuddyProxyFactory implements DynamicProxyFactory {
    @Override
    public <T> T createProxy(Class<T> interfaceClass, Object delegate) {
        Class<? extends T> cls = new ByteBuddy()
                .subclass(interfaceClass)
                .method(
                        ElementMatchers.isDeclaredBy(interfaceClass))
                .intercept(MethodDelegation.to(new BytebuddyInvocationHandler(delegate), "handler"))
                .make()
                .load(interfaceClass.getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                .getLoaded();
        try {
            return cls.newInstance();
        } catch (Throwable t) {
            throw new SofaRpcRuntimeException("construct proxy with bytebuddy occurs error", t);
        }
    }
}