/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.sofa.rpc.channel.demo2.bolt.benchmark;

import com.alipay.remoting.exception.RemotingException;
import com.alipay.remoting.rpc.RpcClient;
import com.alipay.remoting.rpc.RpcServer;
import com.alipay.sofa.rpc.channel.demo2.bolt.model.RequestBody;
import com.alipay.sofa.rpc.channel.demo2.bolt.model.SimpleServerUserProcessor;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * need to test in load test env
 * @author bystander
 * @version $Id: CreateProxyBenchmark.java, v 0.1 2019年02月12日 18:21 bystander Exp $
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class DecodeBenchmark {
    RpcClient client;
    RpcServer server;

    @Benchmark
    public void invokeBatch() {

        String addr = "127.0.0.1:8999";

        RequestBody req = new RequestBody(2, "hello world sync");
        try {
            String res = (String) client.invokeSync(addr, req, 3000);
            // System.out.println("invoke sync result = [" + res + "]");
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Setup
    public void prepare() {

        //change this to switch between batch and not batch
        System.setProperty("batch", "false");

        if (server == null) {
            // 1. create a Rpc server with port assigned
            server = new RpcServer(8999);
            // 2. add processor for connect and close event if you need
            // 3. register user processor for client request
            server.registerUserProcessor(new SimpleServerUserProcessor(5));
            // 4. server start
            if (server.start()) {
                System.out.println("server start ok!");
            } else {
                System.out.println("server start failed!");
            }
        }

        if (client == null) {
            // 1. create a rpc client
            client = new RpcClient();
            // 2. add processor for connect and close event if you need
            // 3. do init
            client.init();
        }
    }

    @TearDown
    public void after() {
        if (server != null) {
            server.stop();
        }
        if (client != null) {
            client.shutdown();
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(DecodeBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(1)
                .measurementIterations(10)
                .build();

        new Runner(opt).run();
    }
}