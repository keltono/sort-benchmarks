package mutation;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class InstructionCall implements Opcodes {
    public enum CallType {
        INSN,
        METHOD_INSN,
        LDC_INSN,
        JUMP_INSN
    }

    private final CallType type;
    private final int opcode;
    //for methodInsn:
    private String owner;
    private String name;
    private String descriptor;
    private boolean isInterface;
    //for ldcInsn:
    private Object argument;

    InstructionCall(int op) {
        type = CallType.INSN;
        opcode = op;
    }

    InstructionCall(int op, String ow, String n, String d, boolean i) {
        type = CallType.METHOD_INSN;
        opcode = op;
        owner = ow;
        name = n;
        descriptor = d;
        isInterface = i;
    }

    InstructionCall(int op, Object arg) {
        if(op == Opcodes.LDC) {
            type = CallType.LDC_INSN;
            opcode = op;
            argument = arg;
        } else {
            type = CallType.JUMP_INSN;
            opcode = op;
        }
    }

    public void call(MethodVisitor mv, Label l) {
        switch(type) {
            case INSN: mv.visitInsn(opcode); break;
            case METHOD_INSN: mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface); break;
            case LDC_INSN: mv.visitLdcInsn(argument); break;
            case JUMP_INSN: mv.visitJumpInsn(opcode, l);
        }
    }
}
