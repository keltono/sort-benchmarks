package mutation;

import org.objectweb.asm.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class Counter {
    private final URL[] urls;

    public Counter(URL[] u) {
        urls = u;
    }

    public Map<String, Map<Mutator, Long>> getMutationInstances() throws IOException {
        Map<String, Map<Mutator, Long>> toReturn = new HashMap<>();
        for(URL u : urls) {
            String directory = u.toString().split("[:\\]]")[1];
            File urlFile = new File(directory);
            File[] urlFiles = urlFile.listFiles();
            if(urlFile.isDirectory() && urlFiles != null) {
                for (File f : urlFiles) {
                    String fName = f.getName();
                    Map<Mutator, Long> mutatorMap = new HashMap<>();
                    for(Mutator mm : Mutator.values()) {
                        mutatorMap.put(mm, getInstanceCount(MutateMain.getBytes(directory + fName), mm, directory + fName));
                    }
                    toReturn.put(directory + fName, mutatorMap);
                }
            }
        }
        return toReturn;
    }

    public long getInstanceCount(byte[] bytes, Mutator mm, String className) {
        AtomicLong instances = new AtomicLong(0);
        ClassWriter cw = new ClassWriter(0);
        ClassReader cr = new ClassReader(bytes);
        cr.accept(new ClassVisitor(MutateMain.cvArg, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String signature,
                                             String superName, String[] interfaces) {
                return new MethodVisitor(MutateMain.cvArg, cv.visitMethod(access, name,
                        signature, superName, interfaces)) {
                    @Override
                    public void visitJumpInsn(int opcode, Label label) {
                        if (opcode == mm.toReplace()) {
                            instances.getAndIncrement();
                        }
                        super.visitJumpInsn(opcode, label);
                    }
                    @Override
                    public void visitLdcInsn(Object value) {
                        if (Opcodes.LDC == mm.toReplace()) {
                            instances.getAndIncrement();
                        }
                        super.visitLdcInsn(value);
                    }
                    @Override
                    public void visitIincInsn(int var, int increment) {
                        if (Opcodes.IINC == mm.toReplace()) {
                            instances.getAndIncrement();
                        }
                        super.visitIincInsn(var, increment);
                    }
                    @Override
                    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                        if (opcode == mm.toReplace() && (mm.returnType() == null || descriptor.contains(")" + mm.returnType()))) {
                            instances.getAndIncrement();
                        }
                        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                    }
                    @Override
                    public void visitInsn(int opcode) {
                        if (opcode == mm.toReplace() && (mm.returnType() == null || signature.contains(")" + mm.returnType()))) {
                            if(mm == Mutator.IRETURN_TO_FALSE) {
                                System.out.println(name + ", " + signature + ", " + superName);
                            }
                            instances.getAndIncrement();
                        }
                        super.visitInsn(opcode);
                    }
                };
            }
        }, 0);
        return instances.longValue();
    }
}
