package ASMproject.project;


import org.objectweb.asm.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyuxiao on 10/24/17.
 */
public class Variable extends MethodVisitor implements Opcodes {
    public Variable(MethodVisitor mv) {
        super(ASM5, mv);
    }

    private int variableDeclarationNum = 0;
    private int variableReferenceNum = 0;
//    private List<String> nameList = new ArrayList<>();

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        variableDeclarationNum++;

        // get rid of those primitive data types (the descriptors of them are single)
        if(!name.equals("this")){
            if(desc.length() > 1){
                variableReferenceNum ++;
                //               nameList.add(desc);
            }
        }
        super.visitLocalVariable(name, desc, signature, start, end, index);
    }

    // get rid of this
    // get the number of variables declared in the method
    public int getVariableDeclarationNum() {
        return Math.max(variableDeclarationNum - 1, 0);
    }

    // get the number of variables referenced in the method
    public int getVariableReferenceNum(){
        return Math.max(variableReferenceNum, 0);
    }

//    public List<String> getNameList(){
//        return nameList;
//    }
}