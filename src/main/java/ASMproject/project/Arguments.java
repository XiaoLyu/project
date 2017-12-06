package ASMproject.project;

import org.objectweb.asm.*;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by lyuxiao on 10/25/17.
 * number of arguments
 */
public class Arguments extends ClassVisitor implements Opcodes{

    private List<String> referencesList = new ArrayList<String>();
    private int argumentNum;

    public Arguments(ClassVisitor cv) {
        super(ASM5, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

        //get the number of arguments
        Type[] t= Type.getArgumentTypes(desc);
        argumentNum = t.length;

        referencesList.add(desc);

        return super.visitMethod(access, name, desc, signature, exceptions);
    }

//    public List<String> getReferencesList(){
//        return referencesList;
//    }

    public int getArgumentNum(){
        return argumentNum;
    }

}