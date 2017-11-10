package ASMproject.project;

import java.util.Iterator;
import java.util.ArrayList;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.analysis.Interpreter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.Interpreter;

public class CyclomaticComplexity {

	public int getCyclomaticComplexity(String owner, MethodNode mn) {
		List<CFGNode> nodes = analyzeControlFlowGraph(owner, mn);
		int numberOfEdges = 0;
		int numberOfNodes = 0;
		for (CFGNode node: nodes) {
			numberOfEdges += node.successors.size();
			numberOfNodes += 1;
		}
		int complexity = numberOfEdges - numberOfNodes + 2;
		return Math.max(complexity, 1);
	}

	private List<CFGNode> analyzeControlFlowGraph(String owner, MethodNode method) {
		Analyzer a = new ControlFlowAnalyzer(new BasicInterpreter());
		try {
			a.analyze(owner, method);
		} catch (AnalyzerException e) {
			throw new RuntimeException(e);
		}
		List<CFGNode> cfg = new ArrayList<>();
		for (Frame f: a.getFrames()) {
			if (f != null)
				cfg.add((CFGNode)f);
		}
		return cfg;
	}

	@ToString
	private static class CFGNode extends Frame {
		Set<CFGNode> successors = new HashSet<>();

		public CFGNode(int nLocals, int nStack) {
			super(nLocals, nStack);
		}

		public CFGNode(Frame src) {
			super(src);
		}

	}

	private static class ControlFlowAnalyzer extends Analyzer {
		private ControlFlowAnalyzer(Interpreter interpreter) {
			super(interpreter);
		}

		@Override
		protected Frame newFrame(int nLocals, int nStack) {
			return new CFGNode(nLocals, nStack);
		}

		@Override
		protected Frame newFrame(Frame src) {
			return new CFGNode(src);
		}

		@Override
		protected void newControlFlowEdge(int src, int dst) {
			CFGNode s = (CFGNode) getFrames()[src];
			s.successors.add((CFGNode) getFrames()[dst]);
		}
	}

}

//public class CyclomaticComplexity {
//
//	public int getCyclomaticComplexity(String owner, MethodNode mn)
//			throws AnalyzerException {
//		Analyzer a = new Analyzer(new BasicInterpreter()) {
//			protected Frame newFrame(int nLocals, int nStack) {
//				return new Node(nLocals, nStack);
//			}
//			protected Frame newFrame(Frame src) {
//				return new Node(src);
//			}
//			protected void newControlFlowEdge(int src, int dst) {
//				Node s = (Node) getFrames()[src];
//				s.successors.add((Node) getFrames()[dst]);
//			}
//		};
//		a.analyze(owner, mn);
//		Frame[] frames = a.getFrames();
//		int edges = 0;
//		int nodes = 0;
//
//		for (int i = 0; i < frames.length; ++i) {
//			if (frames[i] != null) {
//				edges += ((Node) frames[i]).successors.size();
//				nodes += 1;
//			}
//		}
//		return edges - nodes + 2;
//	}
//}

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


//class Node<V extends Value> extends Frame<V> {
//	Set< Node<V> > successors = new HashSet< Node<V> >();
//	public Node(int nLocals, int nStack) {
//		super(nLocals, nStack);
//	}
//	public Node(Frame<? extends V> src) {
//		super(src);
//	}
//}

