/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.sofa.rpc.channel.demo1.benchmark;

import com.alipay.sofa.rpc.channel.demo1.asm.AsmProxyFactory;
import com.alipay.sofa.rpc.channel.demo1.bytebuddy.BytebuddyProxyFactory;
import com.alipay.sofa.rpc.channel.demo1.cglib.CglibProxyFactory;
import com.alipay.sofa.rpc.channel.demo1.factory.DynamicProxyFactory;
import com.alipay.sofa.rpc.channel.demo1.javaassist.JavassistByteProxyFactory;
import com.alipay.sofa.rpc.channel.demo1.javaassist.JavassistProxyFactory;
import com.alipay.sofa.rpc.channel.demo1.jdk.JdkProxyFactory;
import com.alipay.sofa.rpc.channel.demo1.service.RpcService;
import com.alipay.sofa.rpc.channel.demo1.service.impl.RpcServiceImpl;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author bystander
 * @version $Id: CreateProxyBenchmark.java, v 0.1 2019年02月12日 18:21 bystander Exp $
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class ProxyInvokeBenchmark {
    private DynamicProxyFactory jdkProxyFactory = new JdkProxyFactory();
    private DynamicProxyFactory cglibProxyFactory = new CglibProxyFactory();
    private DynamicProxyFactory javassistProxyFactory = new JavassistProxyFactory();
    private DynamicProxyFactory javassistByteProxyFactory = new JavassistByteProxyFactory();
    private DynamicProxyFactory asmProxyFactory = new AsmProxyFactory();
    private DynamicProxyFactory byteBuddyProxyFactory = new BytebuddyProxyFactory();

    private RpcService delegate = new RpcServiceImpl();
    private RpcService jkdProxy = jdkProxyFactory.createProxy(RpcService.class, delegate);
    private RpcService cglibProxy = cglibProxyFactory.createProxy(RpcService.class, delegate);
    private RpcService javassistProxy = javassistProxyFactory.createProxy(RpcService.class, delegate);
    private RpcService javassistByteProxy = javassistByteProxyFactory.createProxy(RpcService.class, delegate);
    private RpcService bytebuddyProxy = byteBuddyProxyFactory.createProxy(RpcService.class, delegate);
    private RpcService asmProxy = asmProxyFactory.createProxy(RpcService.class, delegate);

    @Benchmark
    public void invokeByJdk() {
        jkdProxy.sayHello();
    }

    @Benchmark
    public void invokeByCglib() {
        cglibProxy.sayHello();

    }

    @Benchmark
    public void invokeByJavassist() {
        javassistProxy.sayHello();

    }

    @Benchmark
    public void invokeByJavassistByte() {
        javassistByteProxy.sayHello();
    }

    @Benchmark
    public void invokeByBytebuddy() {
        bytebuddyProxy.sayHello();

    }

    @Benchmark
    public void invokeByAsm() {
        asmProxy.sayHello();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ProxyInvokeBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(5)
                .measurementIterations(10)
                .build();

        new Runner(opt).run();
    }
}