/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.sofa.rpc.channel.demo4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
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
 * @version $Id: ByteBufApplication.java, v 0.1 2019年02月23日 17:31 bystander Exp $
 */

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class ByteBufReadBeanchmark {


    @Benchmark
    public void readInt() {

        ByteBuf buffer = PooledByteBufAllocator.DEFAULT.buffer(4);
        buffer.writeInt(5);
        int result = buffer.readInt();
        buffer.release();

    }

    @Benchmark
    public void readBytes() {
        ByteBuf buffer = PooledByteBufAllocator.DEFAULT.buffer(4);
        buffer.writeInt(5);
        byte[] bytes = new byte[4];
        buffer.readBytes(bytes, 0, 4);
        int result = fromByteArray(bytes);
        buffer.release();
    }

    private int fromByteArray(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ByteBufReadBeanchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(3)
                .measurementIterations(10)
                .build();

        new Runner(opt).run();
    }
}