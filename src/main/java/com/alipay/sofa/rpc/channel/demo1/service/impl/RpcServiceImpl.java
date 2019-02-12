/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.sofa.rpc.channel.demo1.service.impl;

import com.alipay.sofa.rpc.channel.demo1.service.RpcService;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author bystander
 * @version $Id: CountServiceImpl.java, v 0.1 2019年02月12日 17:17 bystander Exp $
 */
public class RpcServiceImpl implements RpcService {
    private AtomicInteger count = new AtomicInteger(0);

    @Override
    public int sayHello() {
        return count.incrementAndGet();
    }
}