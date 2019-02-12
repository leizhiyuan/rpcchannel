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
import com.alipay.sofa.rpc.channel.demo1.service.CountService;
import com.alipay.sofa.rpc.channel.demo1.service.impl.CountServiceImpl;
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

    private CountService delegate = new CountServiceImpl();
    CountService jkdProxy = jdkProxyFactory.createProxy(CountService.class, delegate);
    CountService cglibProxy = cglibProxyFactory.createProxy(CountService.class, delegate);
    CountService javassistProxy = javassistProxyFactory.createProxy(CountService.class, delegate);
    CountService javassistByteProxy = javassistByteProxyFactory.createProxy(CountService.class, delegate);
    CountService bytebuddyProxy = byteBuddyProxyFactory.createProxy(CountService.class, delegate);
    CountService asmProxy = asmProxyFactory.createProxy(CountService.class, delegate);

    @Benchmark
    public void createProxyByJdk() {
        jkdProxy.count();
    }

    @Benchmark
    public void createProxyByCglib() {
        cglibProxy.count();

    }

    @Benchmark
    public void createProxyByJavassist() {
        javassistProxy.count();

    }

    @Benchmark
    public void createProxyByJavassistByte() {
        javassistByteProxy.count();
    }

    @Benchmark
    public void createProxyByBytebuddy() {
        bytebuddyProxy.count();

    }

    @Benchmark
    public void createProxyByAsm() {
        asmProxy.count();
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