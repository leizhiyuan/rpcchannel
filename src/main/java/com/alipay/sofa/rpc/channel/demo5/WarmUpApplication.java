/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.sofa.rpc.channel.demo5;

import com.alipay.sofa.rpc.channel.demo5.service.WarmUpService;
import com.alipay.sofa.rpc.channel.demo5.service.impl.WarmUpServiceImpl;
import com.alipay.sofa.rpc.client.ProviderInfoAttrs;
import com.alipay.sofa.rpc.common.RpcConstants;
import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.alipay.sofa.rpc.config.ProviderConfig;
import com.alipay.sofa.rpc.config.RegistryConfig;
import com.alipay.sofa.rpc.config.ServerConfig;
import org.apache.curator.test.TestingServer;

import java.io.IOException;

/**
 * @author <a href="mailto:zhiyuan.lzy@antfin.com">zhiyuan.lzy</a>
 */
public class WarmUpApplication {
    private static TestingServer server;

    private static RegistryConfig registryConfig;

    private static ConsumerConfig<WarmUpService> consumerConfig;

    private static ServerConfig serverConfigOne;

    private static ServerConfig serverConfigTwo;

    private static ProviderConfig<WarmUpService> providerConfig;

    private static ProviderConfig<WarmUpService> providerConfig2;

    public static void main(String[] args) {

        try {
            startUpZk();
            constructRegistry();
            startProvider();
            startConsumer();
            stopZk();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private static void stopZk() {
        if (server != null) {
            try {
                server.stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static void startUpZk() {
        try {
            server = new TestingServer(2181, true);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void constructRegistry() {
        registryConfig = new RegistryConfig()
                .setProtocol(RpcConstants.REGISTRY_PROTOCOL_ZK)
                .setAddress("127.0.0.1:2181");
    }

    public static void startConsumer() throws InterruptedException {
        consumerConfig = new ConsumerConfig<WarmUpService>()
                .setInterfaceId(WarmUpService.class.getName())
                .setRegistry(registryConfig)
                .setProtocol(RpcConstants.PROTOCOL_TYPE_BOLT);
        WarmUpService warmUpService = consumerConfig.refer();


        long startTime = System.currentTimeMillis();


        int cnt22111 = 0;
        int cnt22222 = 0;

        // Before the 2000 ms, all the traffic goes to 22222.
        for (int i = 0; i < 10; i++) {

            if (warmUpService.getPort() == 22111) {
                cnt22111++;
            }
            if (warmUpService.getPort() == 22222) {
                cnt22222++;
            }
        }

        System.out.println("during warmup, traffic to cnt22111=" + cnt22111 / 100 + ",cnt22222=" + cnt22222 / 100);


        long elapsed = System.currentTimeMillis() - startTime;
        long sleepTime = 2100 - elapsed;
        if (sleepTime >= 0) {
            Thread.sleep(sleepTime);
        }


        // After 2000 ms, all the traffic goes to 22222 && 22111.
        cnt22111 = 0;
        cnt22222 = 0;
        for (int i = 0; i < 100; i++) {
            if (warmUpService.getPort() == 22111) {
                cnt22111++;
            }
            if (warmUpService.getPort() == 22222) {
                cnt22222++;
            }
        }
        System.out.println("after warmup, traffic to cnt22111=" + cnt22111 + ",cnt22222=" + cnt22222);

    }

    public static void startProvider() {

        serverConfigOne = new ServerConfig()
                .setPort(22222)
                .setProtocol(RpcConstants.PROTOCOL_TYPE_BOLT);
        providerConfig = new ProviderConfig<WarmUpService>()
                .setInterfaceId(WarmUpService.class.getName())
                .setRef(new WarmUpServiceImpl(22222))
                .setServer(serverConfigOne)
                .setRegistry(registryConfig)
                .setParameter(ProviderInfoAttrs.ATTR_WARMUP_TIME, "2000")
                .setParameter(ProviderInfoAttrs.ATTR_WARMUP_WEIGHT, "100")
                .setWeight(0);

        serverConfigTwo = new ServerConfig()
                .setPort(22111)
                .setProtocol(RpcConstants.PROTOCOL_TYPE_BOLT);
        providerConfig2 = new ProviderConfig<WarmUpService>()
                .setInterfaceId(WarmUpService.class.getName())
                .setRef(new WarmUpServiceImpl(22111))
                .setServer(serverConfigTwo)
                .setRegistry(registryConfig)
                .setRepeatedExportLimit(-1)
                .setWeight(0);

        providerConfig.export();
        providerConfig2.export();
    }
}