package ASMproject.project;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyuxiao on 10/23/17.
 */
public class Halstead extends MethodVisitor implements Opcodes {

    public Halstead(MethodVisitor mv) {
        super(ASM5, mv);
    }

    public class Info{
        private List<String> operatorList = new ArrayList<>();
        private List<Object> operandList = new ArrayList<>();

        public void addOperator(String operator){
            operatorList.add(operator);
        }

        public void addOperand(Object operand){
            operandList.add(operand);
        }

        //number of operators
        public long operatorsNum(){
            long size = operatorList.size();
            if(size < 1){
                System.out.printf("0 operator! Default to 1!\n");
                return 1;
            }
            return size;
        }

        //number of operands
        public long operandsNum(){
            long size = operandList.size();
            if(size < 1){
                System.out.printf("0 operand! Default to 1!\n");
                return 1;
            }
            return size;
        }

        //number of unique operators
        public long uniqueOperatorsNum(){
            long size = operatorList.stream().distinct().count();
            if (size < 1){
                System.out.printf("0 unique operator! Default to 1!\n");
                return 1;
            }
            return size;
        }

        //number of unique operands
        public long uniqueOperandsNum(){
            long size = operandList.stream().distinct().count();
            if (size < 1){
                System.out.printf("0 unique operator! Default to 1!\n");
                return 1;
            }
            return size;
        }

        //Halstead length: number of operators plus number of operands.
        public long HalsteadLength(){
            long length = operandsNum() + operatorsNum();
            return length;
        }

        //Halstead vocabulary: number of unique operators plus number of unique operands.
        public long HalsteadVocabulary(){
            long length = uniqueOperandsNum() + uniqueOperatorsNum();
            return length;
        }

        //Halstead volume: LTH * log2(VOC) where LTH Halstead length and VOC is Halstead Vocabulary.
        public double HalsteadVolume(){
            long LTH = HalsteadLength();
            long VOC = HalsteadVocabulary();
            double log2VOC = Math.log(VOC)/Math.log(2);
            double volume = LTH * log2VOC;
            return volume;
        }

        //Halstead difficulty: (UOP/2) * (OD/UOD) where UOP is the number of unique operators,
        // OD is the number of operands and UOD is the number of unique operands.
        public long HalsteadDifficulty(){
            long UOP = uniqueOperatorsNum();
            long OD = operandsNum();
            long UOD = uniqueOperandsNum();
            long difficulty = (UOP/2) * (OD/UOD);
            return difficulty;
        }

        //Halstead effort: DIF * VOL where DIF is Halstead Difficulty and VOL is Halstead volume.
        public double HalsteadEffort(){
            long DIF = HalsteadDifficulty();
            double VOL = HalsteadVolume();
            double effort = DIF * VOL;
            return effort;
        }

        //Halstead bugs: VOL/3000 or EFF^(2/3)/3000 where VOL is Halstead volume and EFF is Halstead effort.
        public double HalsteadBugs(){
            double VOL = HalsteadVolume();
            double bugs = VOL/3000;
            return bugs;
        }
    }

    public Info info = new Info();

    //1 operator
    // visit a zero operand instruction
    @Override
    public void visitInsn(int opcode) {
        info.addOperator(Integer.toString(opcode));
        super.visitInsn(opcode);
    }

    //1 operand, 1 operator
    // Visits an instruction with a single int operand.
    @Override
    public void visitIntInsn(int opcode, int operand) {
        info.addOperator(Integer.toString(opcode));
        info.addOperand(operand);
        super.visitIntInsn(opcode, operand);
    }

    //1 operand, 1 operator
    @Override
    public void visitVarInsn(int opcode, int var) {
        info.addOperator(Integer.toString(opcode));
        info.addOperand(var);
        super.visitVarInsn(opcode, var);
    }

    //1 operand, 1 operator
    @Override
    public void visitTypeInsn(int opcode, String type) {
        info.addOperator(Integer.toString(opcode));
        info.addOperand(type);
        super.visitTypeInsn(opcode, type);
    }

    //1 operand, 1 operator
    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        info.addOperator(Integer.toString(opcode));
        String operand = owner + '.' + name;
        info.addOperand(operand);
        super.visitFieldInsn(opcode, owner, name, desc);
    }

    //1 operand, 1 operator
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        info.addOperator(Integer.toString(opcode));
        String operand = owner + '.' + name;
        info.addOperand(operand);
        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

    //1 operator
    @Override
    public void visitJumpInsn(int opcode, Label label) {
        info.addOperator(Integer.toString(opcode));
        super.visitJumpInsn(opcode, label);
    }

    //1 operand
    @Override
    public void visitLdcInsn(Object cst) {
        info.addOperand(cst);
        super.visitLdcInsn(cst);
    }

    //1 operator, 2 operands
    @Override
    public void visitIincInsn(int var, int increment) {
        info.addOperator("increment");
        info.addOperand(var);
        info.addOperand(increment);
        super.visitIincInsn(var, increment);
    }

    // tableswitch min - max , default, switch
    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        info.addOperator("switch");
        for (int i = min; i < (max + 1); i++){
            info.addOperator("case");
        }
        info.addOperator("default");
        super.visitTableSwitchInsn(min, max, dflt, labels);
    }

    // lookupswitch len(keys) operators, default, switch
    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        info.addOperator("switch");
        for (int i = 0; i < keys.length; i++){
            info.addOperator("case");
        }
        info.addOperator("default");
        super.visitLookupSwitchInsn(dflt, keys, labels);
    }

    // Create new multidimensional array
    // 1 operator, 2 operands
    @Override
    public void visitMultiANewArrayInsn(String desc, int dims) {
        info.addOperator("multidimensional");
        info.addOperand(desc);
        info.addOperand(dims);
        super.visitMultiANewArrayInsn(desc, dims);
    }

}
