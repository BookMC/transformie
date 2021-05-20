package org.bookmc.transformie.mixin;

import org.bookmc.transformie.Transformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class MixinTransformer implements Transformer {
    @Override
    public byte[] transform(String name, byte[] classBytes) {
        if (name.equals("org.spongepowered.asm.service.mojang.MixinServiceLaunchWrapper")) {
            ClassNode node = new ClassNode();
            ClassReader reader = new ClassReader(classBytes);

            reader.accept(node, ClassReader.EXPAND_FRAMES);

            for (MethodNode methodNode : node.methods) {
                if (methodNode.name.equals("getClassBytes")) {
                    InsnList instructions = methodNode.instructions;

                    for (AbstractInsnNode instruction : instructions) {
                        if (instruction instanceof MethodInsnNode && instruction.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                            MethodInsnNode methodInsnNode = (MethodInsnNode) instruction;

                            System.out.println(methodInsnNode.desc);
                        }
                    }
                }
            }
        }

        return classBytes;
    }
}
