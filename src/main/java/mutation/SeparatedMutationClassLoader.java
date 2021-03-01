package mutation;

import org.objectweb.asm.*;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.atomic.AtomicLong;

public class SeparatedMutationClassLoader extends URLClassLoader {
    private final Mutator mutator;
    private final long instance;
    private final String mutateName;

    public SeparatedMutationClassLoader(URL u, Mutator m, long i, String n) {
        super(new URL[]{u}, new ClassLoader() {});
        mutator = m;
        instance = i;
        mutateName = n;
    }

    @Override
    public Class<?> findClass(String className) throws ClassNotFoundException {
        byte[] bytes;

        try {
            bytes = MutateMain.getBytes(className);
        } catch (IOException e) {
            throw new ClassNotFoundException("I/O exception while loading class.", e);
        }

        //TODO find a way to not hard-code
        String nname = className.split("target/classes/")[1].replace('/', '.').split(".class")[0];

        if(className.equals(mutateName)) {
            AtomicLong found = new AtomicLong(0);
            //final List<String> currentDescriptor = new ArrayList<>();
            ClassWriter cw = new ClassWriter(0);
            ClassReader cr = new ClassReader(bytes);
            cr.accept(new ClassVisitor(Mutator.cvArg, cw) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String signature,
                                                 String superName, String[] interfaces) {
                    return new MethodVisitor(Mutator.cvArg, cv.visitMethod(access, name,
                            signature, superName, interfaces)) {
                        @Override
                        public void visitJumpInsn(int opcode, Label label) {
                            if (opcode == mutator.toReplace() && found.get() == instance) {
                                for (InstructionCall ic : mutator.replaceWith(signature, false)) {
                                    ic.call(mv, label);
                                }
                                found.getAndIncrement();
                            } else if (opcode == mutator.toReplace()) {
                                super.visitJumpInsn(opcode, label);
                                found.getAndIncrement();
                            } else {
                                super.visitJumpInsn(opcode, label);
                            }
                        }

                        @Override
                        public void visitLdcInsn(Object value) {
                            if (Opcodes.LDC == mutator.toReplace() && found.get() == instance) {
                                for (InstructionCall ic : mutator.replaceWith(signature, false)) {
                                    ic.call(mv, null);
                                }
                                found.getAndIncrement();
                            } else if (Opcodes.LDC == mutator.toReplace()) {
                                super.visitLdcInsn(value);
                                found.getAndIncrement();
                            } else {
                                super.visitLdcInsn(value);
                            }
                        }
                        @Override
                        public void visitIincInsn(int var, int increment) {
                            if (Opcodes.IINC == mutator.toReplace() && found.get() == instance) {
                                super.visitIincInsn(var, -increment);
                                found.getAndIncrement();
                            } else if (Opcodes.IINC == mutator.toReplace()) {
                                super.visitIincInsn(var, increment);
                                found.getAndIncrement();
                            } else {
                                super.visitIincInsn(var, increment);
                            }
                        }
                        @Override
                        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                            if (opcode == mutator.toReplace() && found.get() == instance) {
                                for (InstructionCall ic : mutator.replaceWith(descriptor, owner.equals(nname.replace(".", "/")))) {
                                    ic.call(mv, null);
                                }
                                found.getAndIncrement();
                            } else if (opcode == mutator.toReplace()) {
                                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                                found.getAndIncrement();
                            } else {
                                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                            }
                        }
                        @Override
                        public void visitInsn(int opcode) {
                            if (opcode == mutator.toReplace() && found.get() == instance && (mutator.returnType() == null || signature.contains(")" + mutator.returnType()))) {
                                for (InstructionCall ic : mutator.replaceWith(signature, false)) {
                                    ic.call(mv, null);
                                }
                                found.getAndIncrement();
                            } else if (opcode == mutator.toReplace() && (mutator.returnType() == null || signature.contains(")" + mutator.returnType()))) {
                                super.visitInsn(opcode);
                                found.getAndIncrement();
                            } else {
                                super.visitInsn(opcode);
                            }
                        }
                    };
                }
            }, 0);
            bytes = cw.toByteArray();
        }
        //TODO for debug purposes only:
        /*try {
            FileOutputStream fos = new FileOutputStream("HelloWorld.class");
            for (byte aByte : bytes) {
                fos.write(aByte);
            }
            fos.close();
        } catch(Exception e) {
            e.printStackTrace();
        }*/

        return defineClass(nname, bytes,0, bytes.length);
    }
}
