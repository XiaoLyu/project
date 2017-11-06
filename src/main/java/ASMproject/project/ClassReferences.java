package ASMproject.project;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lyuxiao on 10/26/17.
 * The classes referenced in the method
 */
public class ClassReferences extends MethodVisitor implements Opcodes {

    public ClassReferences(MethodVisitor mv) {
        super(ASM5, mv);
    }

    Set<String> classReferencesSet = new HashSet<String>();
    List<String> classReferencesList = new ArrayList<String>();

    String classReferencesNames = "";
    String s = "";

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        if(!name.equals("this")){
            if(desc.length() > 1){
                classReferencesSet.add(desc);
            }
        }
        super.visitLocalVariable(name, desc, signature, start, end, index);
    }

    public List<String> getClassReferencesList(){
        classReferencesList.addAll(classReferencesSet);
        return classReferencesList;
    }

    public String getClassReferencesNames(){
        List<String> temp = getClassReferencesList();

        String tempart = "";
        String finalpart = "";

        for(int i = 0; i< temp.size(); i++){
            String[] part1 = temp.get(i).split("/");
            int indexNum = part1.length - 1;
            tempart = part1[indexNum];

            //get rid the ';' in the end
            String[] part2 = tempart.split(";");

            s = part2[0].replace("[", "[]");

            finalpart = finalpart + s;
            if (i < temp.size() - 1){
                finalpart = finalpart + " ";
            }
        }

        classReferencesNames = classReferencesNames + finalpart;

        return classReferencesNames;
    }
}
