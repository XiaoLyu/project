package ASMproject.project;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by lyuxiao on 10/26/17.
 * numbers of casting: changing an entity of one data type into another
 * CHECKCAST
 */

public class Casting extends MethodVisitor implements Opcodes {

    public Casting(MethodVisitor mv) {
        super(ASM5, mv);
    }

    int castingNum = 0;

    @Override
    public void visitTypeInsn(int opcode, String type) {
        boolean isCasting = (opcode & Opcodes.CHECKCAST) != 0;
        if(isCasting){
            castingNum ++;
        }
        super.visitTypeInsn(opcode, type);
    }

    public int getCastingNum(){
        return castingNum;
    }
}
