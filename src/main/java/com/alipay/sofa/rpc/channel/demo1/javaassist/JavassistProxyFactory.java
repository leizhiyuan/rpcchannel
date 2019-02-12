/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.sofa.rpc.channel.demo1.javaassist;

import com.alipay.sofa.rpc.channel.demo1.factory.DynamicProxyFactory;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

/**
 * @author bystander
 * @version $Id: JavassistProxyFactory.java, v 0.1 2019年02月12日 17:57 bystander Exp $
 */
public class JavassistProxyFactory implements DynamicProxyFactory {
    public <T> T createProxy(Class<T> interfaceType, Object delegate) {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(new Class[]{interfaceType});
        Class proxyClass = proxyFactory.createClass();
        T javassistProxy = null;
        try {
            javassistProxy = (T) proxyClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        ((ProxyObject) javassistProxy).setHandler(new JavaAssitInterceptor(delegate));
        return javassistProxy;
    }
}