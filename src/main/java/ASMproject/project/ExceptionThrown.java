package ASMproject.project;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.Method;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lyuxiao on 10/26/17.
 * Exceptions thrown in the method
 */
public class ExceptionThrown extends MethodVisitor implements Opcodes {


    public ExceptionThrown(MethodVisitor mv) {
        super(ASM5, mv);
    }

    private List<String> names = new ArrayList<String>();
    private String exceptionThrown = "";
    private String temp = "";
    Set<String> exceptionThrownSet = new HashSet<String>();

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        if(owner.contains("Exception")){
        //    names.add(owner);
            exceptionThrownSet.add(owner);

//            String[] part = owner.split("/");
//            temp = part[part.length-1];
//
//            for(int i = 0; i < (getNames().size() - 1); i++){
//                exceptionThrown = exceptionThrown + " ";
//            }
//            exceptionThrown = exceptionThrown + temp;
        }

        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

    public String getNames(){
        names.addAll(exceptionThrownSet);

        for(int j = 0; j < names.size(); j++){
            String[] part = names.get(j).split("/");
            temp = part[part.length-1];

            exceptionThrown = exceptionThrown + temp;

            for(int i = 0; i < (names.size() - 1); i++){
                exceptionThrown = exceptionThrown + " ";
            }

        }

        return exceptionThrown;
    }
}
