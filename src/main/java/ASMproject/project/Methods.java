package ASMproject.project;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyuxiao on 10/27/17.
 * get methods information
 */
public class Methods extends MethodVisitor implements Opcodes {

    public Methods(MethodVisitor mv) {
        super(ASM5, mv);
    }

    private List<String> classNameList = new ArrayList<>();
    private List<String> methodList = new ArrayList<>();

    // store invoke method [methodOwner, methodName]
    private List<String[]> methodInfo = new ArrayList<>();
    private String[] temp = new String[2];

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        classNameList.add(owner);
        methodList.add(name);

        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

    public List<String> getClassNameList(){
        return classNameList;
    }

    public List<String> getMethodList(){
        return methodList;
    }
}
