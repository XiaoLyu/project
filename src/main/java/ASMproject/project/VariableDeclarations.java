package ASMproject.project;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

//import java.util.ArrayList;
//import java.util.List;

/**
 * Created by lyuxiao on 10/24/17.
 */
public class VariableDeclarations extends MethodVisitor implements Opcodes {
    public VariableDeclarations(MethodVisitor mv) {
        super(ASM5, mv);
    }

    private int variableNum = 0;
 //   private List<String> nameList = new ArrayList<>();

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        variableNum++;
 //       nameList.add(name);
        super.visitLocalVariable(name, desc, signature, start, end, index);
    }

    // get rid of this
    public int getVariableNum() {
        return Math.max(variableNum-1, 0);
    }

//    public List<String> getNameList(){
//        return nameList;
//    }

}
