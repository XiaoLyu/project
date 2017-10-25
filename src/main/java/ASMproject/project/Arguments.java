package ASMproject.project;

import org.objectweb.asm.*;

import java.util.Arrays;
//import java.util.List;
//import java.util.ArrayList;


/**
 * Created by lyuxiao on 10/25/17.
 */
public class Arguments extends ClassVisitor implements Opcodes{

    private String argumentsList = "";
    private int argumentNum;

    public Arguments(ClassVisitor cv) {
        super(ASM5, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        argumentsList = Arrays.toString(Type.getArgumentTypes(desc));
        Type[] t= Type.getArgumentTypes(desc);
        argumentNum = t.length;
        return super.visitMethod(access, name, desc, signature, exceptions);
    }

//    public String getArgumentsList(){
//        return argumentsList;
//    }

    public int getArgumentNum(){
        return argumentNum;
    }

}
