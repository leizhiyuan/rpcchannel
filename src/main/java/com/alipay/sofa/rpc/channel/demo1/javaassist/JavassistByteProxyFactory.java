/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.sofa.rpc.channel.demo1.javaassist;

import com.alipay.sofa.rpc.channel.demo1.factory.DynamicProxyFactory;
import com.alipay.sofa.rpc.channel.demo1.service.RpcService;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author bystander
 * @version $Id: JavassistProxyFactory.java, v 0.1 2019年02月12日 17:57 bystander Exp $
 */
public class JavassistByteProxyFactory implements DynamicProxyFactory {


    //only for benchmark
    private AtomicLong atomicLong = new AtomicLong(0);

    public <T> T createProxy(Class<T> type, Object delegate) {
        T bytecodeProxy = null;
        try {
            ClassPool mPool = new ClassPool(true);
            CtClass mCtc = mPool.makeClass(RpcService.class.getName() + "JavaassistProxy" + atomicLong.incrementAndGet());
            mCtc.addInterface(mPool.get(RpcService.class.getName()));
            mCtc.addConstructor(CtNewConstructor.defaultConstructor(mCtc));
            mCtc.addField(CtField.make("public " + RpcService.class.getName() + " delegate;", mCtc));
            mCtc.addMethod(CtNewMethod.make("public int sayHello() { return delegate.sayHello(); }", mCtc));
            Class pc = mCtc.toClass();
            bytecodeProxy = (T) pc.newInstance();
            Field filed = bytecodeProxy.getClass().getField("delegate");
            filed.set(bytecodeProxy, delegate);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return bytecodeProxy;
    }
}