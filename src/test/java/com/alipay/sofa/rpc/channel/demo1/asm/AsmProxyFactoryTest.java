package com.alipay.sofa.rpc.channel.demo1.asm;

import com.alipay.sofa.rpc.channel.demo1.service.RpcService;
import com.alipay.sofa.rpc.channel.demo1.service.impl.RpcServiceImpl;
import org.junit.Test;

public class AsmProxyFactoryTest {

    @Test
    public void createProxy() {

        AsmProxyFactory proxyFactory =new AsmProxyFactory();
        proxyFactory.createProxy(RpcService.class,new RpcServiceImpl());
    }
}