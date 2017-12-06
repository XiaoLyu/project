package ASMproject.project;

import org.objectweb.asm.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyuxiao on 10/26/17.
 * Exceptions referenced
 */
public class ExceptionReference extends ClassVisitor implements Opcodes {


    public ExceptionReference(ClassVisitor cv) {
        super(ASM5, cv);
    }

    private List<String> exceptionsNameList = new ArrayList<String>();
    private String exceptionsName = "";
    private String temp = "";

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        for (int i = 0; i< exceptions.length; i++){
            exceptionsNameList.add(exceptions[i]);
            temp = exceptions[i];
            String[] part = temp.split("/");
            temp = part[part.length-1];
            exceptionsName = exceptionsName + temp;
            if( i < (exceptions.length - 1)){
                exceptionsName = exceptionsName + " ";
            }
        }
        return super.visitMethod(access, name, desc, signature, exceptions);
    }

    public List<String> getExceptionsNameList(){
        return exceptionsNameList;
    }

    public String getExceptionsName(){
        return exceptionsName;
    }
}
