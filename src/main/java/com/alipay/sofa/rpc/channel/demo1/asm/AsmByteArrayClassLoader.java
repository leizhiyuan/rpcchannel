/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.sofa.rpc.channel.demo1.asm;

/**
 * @author bystander
 * @version $Id: ByteArrayClassLoader.java, v 0.1 2019年02月12日 17:54 bystander Exp $
 */
public class AsmByteArrayClassLoader extends ClassLoader {

    public AsmByteArrayClassLoader() {
        super(AsmByteArrayClassLoader.class.getClassLoader());
    }

    public synchronized Class getClass(String name, byte[] code) {
        if (name == null) {
            throw new IllegalArgumentException("");
        }
        return defineClass(name, code, 0, code.length);
    }

}