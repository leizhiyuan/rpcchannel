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
package com.alipay.sofa.rpc.channel.demo1.bytebuddy;

import com.alipay.sofa.rpc.invoke.Invoker;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author bystander
 * @version $Id: BytebuddyInvocationHandler.java, v 0.1 2019年01月30日 12:31 bystander Exp $
 */
public class BytebuddyInvocationHandler {

    private Object delegate;

    public BytebuddyInvocationHandler(Object deleteGate) {

        this.delegate = deleteGate;
    }

    public Object getDelegate() {
        return delegate;
    }

    public void setDelegate(Invoker delegate) {
        this.delegate = delegate;
    }

    @RuntimeType
    public Object byteBuddyInvoke(@This Object proxy, @Origin Method method, @AllArguments @RuntimeType Object[] args)
            throws Throwable {
        String name = method.getName();
        if ("equals".equals(name)) {
            Object another = args[0];
            return proxy == another ||
                    (proxy.getClass().isInstance(another) && delegate.equals(parseInvoker(another)));
        } else if ("hashCode".equals(name)) {
            return delegate.hashCode();
        } else if ("toString".equals(name)) {
            return delegate.toString();
        }

        return method.invoke(delegate, args);
    }

    private Object parseInvoker(Object another) {
        try {
            Field field = another.getClass().getField("handler");
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            BytebuddyInvocationHandler interceptor = (BytebuddyInvocationHandler) field.get(another);

            return interceptor.getDelegate();
        } catch (Exception e) {
            return null;
        }
    }
}