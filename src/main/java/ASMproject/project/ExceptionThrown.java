package ASMproject.project;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.Method;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyuxiao on 10/26/17.
 * Exceptions thrown in the method
 */
public class ExceptionThrown extends MethodVisitor implements Opcodes {


    public ExceptionThrown(MethodVisitor mv) {
        super(ASM5, mv);
    }

    private List<String> names = new ArrayList<>();
    private String exceptionThrown = "";
    private String temp = "";

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        if(owner.contains("Exception")){
            names.add(owner);
            String[] part = owner.split("/");
            temp = part[part.length-1];
            exceptionThrown = exceptionThrown + temp;
        }
        for(int i = 0; i < (getNames().size() - 1); i++){
            exceptionThrown = exceptionThrown + " ";
        }
        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

    public List<String> getNames(){
        return names;
    }

    public String getExceptionThrown(){
        return exceptionThrown;
    }
}
