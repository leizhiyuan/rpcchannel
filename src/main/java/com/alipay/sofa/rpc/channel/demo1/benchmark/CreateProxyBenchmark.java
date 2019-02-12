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
public class CreateProxyBenchmark {
    private DynamicProxyFactory jdkProxyFactory = new JdkProxyFactory();
    private DynamicProxyFactory cglibProxyFactory = new CglibProxyFactory();
    private DynamicProxyFactory javassistProxyFactory = new JavassistProxyFactory();
    private DynamicProxyFactory javassistByteProxyFactory = new JavassistByteProxyFactory();
    private DynamicProxyFactory asmProxyFactory = new AsmProxyFactory();
    private DynamicProxyFactory byteBuddyProxyFactory = new BytebuddyProxyFactory();

    private CountService delegate = new CountServiceImpl();


    @Benchmark
    public void createProxyByJdk() {
        CountService result = jdkProxyFactory.createProxy(CountService.class, delegate);
    }

    @Benchmark
    public void createProxyByCglib() {
        CountService result = cglibProxyFactory.createProxy(CountService.class, delegate);

    }

    @Benchmark
    public void createProxyByJavassist() {
        CountService result = javassistProxyFactory.createProxy(CountService.class, delegate);

    }

    @Benchmark
    public void createProxyByJavassistByte() {
        CountService result = javassistByteProxyFactory.createProxy(CountService.class, delegate);

    }

    @Benchmark
    public void createProxyByBytebuddy() {
        CountService result = byteBuddyProxyFactory.createProxy(CountService.class, delegate);

    }

    @Benchmark
    public void createProxyByAsm() {
        CountService result = asmProxyFactory.createProxy(CountService.class, delegate);

    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(CreateProxyBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(5)
                .measurementIterations(10)
                .build();

        new Runner(opt).run();
    }
}