/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.alipay.sofa.rpc.channel.demo1.asm;

import com.alipay.sofa.rpc.channel.demo1.factory.DynamicProxyFactory;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Field;

/**
 * @author bystander
 * @version $Id: AsmProxyFactory.java, v 0.1 2019年02月12日 17:52 bystander Exp $
 */
public class AsmProxyFactory implements DynamicProxyFactory {
    public <T> T createProxy(Class<T> interfaceType, Object delegate) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        String className = interfaceType.getName() + "AsmProxy";
        String classPath = className.replace('.', '/');
        String interfacePath = interfaceType.getName().replace('.', '/');
        classWriter.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC, classPath, null, "java/lang/Object", new String[]{interfacePath});

        MethodVisitor initVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        initVisitor.visitCode();
        initVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        initVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        initVisitor.visitInsn(Opcodes.RETURN);
        initVisitor.visitMaxs(0, 0);
        initVisitor.visitEnd();

        FieldVisitor fieldVisitor = classWriter.visitField(Opcodes.ACC_PUBLIC, "delegate", "L" + interfacePath + ";", null, null);
        fieldVisitor.visitEnd();

        MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "count", "()I", null, null);
        methodVisitor.visitCode();
        methodVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        methodVisitor.visitFieldInsn(Opcodes.GETFIELD, classPath, "delegate", "L" + interfacePath + ";");
        methodVisitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, interfacePath, "count", "()I");
        methodVisitor.visitInsn(Opcodes.IRETURN);
        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();

        classWriter.visitEnd();
        byte[] code = classWriter.toByteArray();
        T bytecodeProxy = null;
        try {
            bytecodeProxy = (T) new AsmByteArrayClassLoader().getClass(className, code).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Field filed = null;
        try {
            filed = bytecodeProxy.getClass().getField("delegate");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            filed.set(bytecodeProxy, delegate);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return bytecodeProxy;
    }
}