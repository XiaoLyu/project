package ASMproject.project;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyuxiao on 10/25/17.
 */
public class Modifiers extends ClassVisitor implements Opcodes {
    private List<String> modifier = new ArrayList<String>();

    public Modifiers(ClassVisitor cv) {
        super(ASM5, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        boolean isPublic = (access & Opcodes.ACC_PUBLIC) != 0;
        boolean isProtected = (access & Opcodes.ACC_PROTECTED) != 0;
        boolean isPrivate = (access & Opcodes.ACC_PRIVATE) != 0;

        boolean isStatic = (access & Opcodes.ACC_STATIC) != 0;
        boolean isFinal = (access & Opcodes.ACC_FINAL) != 0;
        boolean isAbstract = (access & Opcodes.ACC_ABSTRACT) != 0;
        boolean isSynchronized = (access & Opcodes.ACC_SYNCHRONIZED) != 0;
        boolean isVolatile = (access & Opcodes.ACC_VOLATILE) != 0;

        if(isProtected)
            modifier.add("protected");
        if(isPublic)
            modifier.add("public");
        if(isPrivate)
            modifier.add("private");
        if(isStatic)
            modifier.add("static");
        if(isFinal)
            modifier.add("final");
        if(isAbstract)
            modifier.add("abstract");
        if(isSynchronized)
            modifier.add("synchronized");
        if(isVolatile)
            modifier.add("volatile");

        return super.visitMethod(access, name, desc, signature, exceptions);
    }

    public String getModifier(){
        String s = "";
        int len = modifier.size();
        for(int i = 0; i < len; i++){
            s = s + modifier.get(i);
            if(i < len - 1) {
                s = s + ' ';
            }
        }
        return s;
    }
}