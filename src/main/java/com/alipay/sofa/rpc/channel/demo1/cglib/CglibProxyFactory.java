/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.sofa.rpc.channel.demo1.cglib;

import com.alipay.sofa.rpc.channel.demo1.factory.DynamicProxyFactory;
import com.alipay.sofa.rpc.channel.demo1.service.CountService;
import net.sf.cglib.proxy.Enhancer;

/**
 * @author bystander
 * @version $Id: CglibProxyFactory.java, v 0.1 2019年02月12日 18:03 bystander Exp $
 */
public class CglibProxyFactory implements DynamicProxyFactory {
    public <T> T createProxy(Class<T> type, Object delegate) {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(new CglibInterceptor(delegate));
        enhancer.setInterfaces(new Class[]{CountService.class});
        T cglibProxy = (T) enhancer.create();
        return cglibProxy;
    }
}