package ASMproject.project;

/**
 * Author: Jiale He
 * Create: 10/27/17
 * Count the cyclomatic complexity of the test code
 */

import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LdcInsnNode;

public class CyclomaticComplexity extends MethodVisitor implements Opcodes {
	public int getCyclomaticComplexity(String owner, MethodNode mn) throws AnalyzerException {
			Analyzer<BasicValue> a = new Analyzer<BasicValue>(new BasicInterpreter()) {
			protected Frame<BasicValue> newFrame(int nLocals, int nStack) {
			return new Node<BasicValue>(nLocals, nStack);
		}
			protected Frame<BasicValue> newFrame(Frame<? extends BasicValue> src) {
			return new Node<BasicValue>(src);
		}
			protected void newControlFlowEdge(int src, int dst) {
			Node<BasicValue> s = (Node<BasicValue>) getFrames()[src];
			s.successors.add((Node<BasicValue>) getFrames()[dst]);
		}
	};
			a.analyze(owner, mn);
			Frame<BasicValue>[] frames = a.getFrames();
			int edges = 0;
			int nodes = 0;
			for (int i = 0; i < frames.length; ++i) {
				if (frames[i] != null) {
					edges += ((Node<BasicValue>) frames[i]).successors.size();
					nodes += 1;
			}
		}
			return edges - nodes + 2;
	}
//	public Methods(MethodVisitor mv) {
//        super(ASM5, mv);
//        
//       try{
//        ClassReader reader = new ClassReader("TestClassName");  // Test class name here
//        ClassNode cn = new ClassNode(); 
//        reader.accept(cn, 0); 
//        List<MethodNode> methodList = cn.methods; 
//        for (MethodNode md : methodList) { 
//            System.out.println(md.name); 
//            System.out.println(md.access); 
//            System.out.println(md.desc); 
//            System.out.println(md.signature); 
//            List<LocalVariableNode> lvNodeList = md.localVariables; 
//
//            Iterator<AbstractInsnNode> instraIter = md.instructions.iterator(); 
//            while (instraIter.hasNext()) { 
//                AbstractInsnNode abi = instraIter.next(); 
//                if (abi instanceof LdcInsnNode) { 
//                    LdcInsnNode ldcI = (LdcInsnNode) abi; 
//                    System.out.println("LDC node value: " + ldcI.cst); 
//                } 
//            } 
//        } 
//        MethodVisitor mv = cn.visitMethod(Opcodes.AALOAD, "<init>", Type 
//                .getType(String.class).toString(), null, null); 
//        mv.visitFieldInsn(Opcodes.GETFIELD, Type.getInternalName(String.class), "str", Type 
//                .getType(String.class).toString()); 
//       }catch (Exception e) {
//		// TODO: handle exception
//    	   e.printStackTrace();
//	}  
//}
}

