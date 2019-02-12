/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.sofa.rpc.channel.demo1.factory;

/**
 * @author bystander
 * @version $Id: DynamicProxyFactory.java, v 0.1 2019年02月12日 17:50 bystander Exp $
 */
public interface DynamicProxyFactory {

    <T> T createProxy(Class<T> type, Object delegate);

}