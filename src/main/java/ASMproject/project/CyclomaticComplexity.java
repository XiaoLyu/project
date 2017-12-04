package ASMproject.project;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.Interpreter;

public class CyclomaticComplexity {

    // CFGNode structure: node has set of successors
    private static class CFGNode extends Frame {
        int id = 0;
        Set<CFGNode> successors = new HashSet<CFGNode>();
        boolean seen = false;

        // Constructs a new frame with the given size.
        public CFGNode(int nLocals, int nStack) {
            super(nLocals, nStack);
        }

        // Constructs a new frame that is identical to the given frame.
        public CFGNode(Frame src) {
            super(src);
        }

        @Override
        public String toString() {
            String str = "[";
            for (CFGNode node : successors) {
                str += node.hashCode() + " ";
            }
            str += "]";
            return this.hashCode() + " " + str + " " + seen;
//            return this.hashCode()+ "";
        }

        @Override
        public int hashCode() {
            return id;
        }
    }

    // CFGAnalyzer
    private static class CFGAnalyzer extends Analyzer {

        private CFGAnalyzer(Interpreter interpreter) {
            super(interpreter);
        }

        // Constructs a new frame with the given size.
        // nLocals - the maximum number of local variables of the frame.
        // nStack - the maximum stack size of the frame.

        @Override
        protected Frame newFrame(int nLocals, int nStack) {
            return new CFGNode(nLocals, nStack);
        }

        // Constructs a new frame that is identical to the given frame.

        @Override
        protected Frame newFrame(Frame src) {
            return new CFGNode(src);
        }

        // Creates a control flow graph edge.
        // insn - an instruction index.
        // successor - index of a successor instruction.

        @Override
        protected void newControlFlowEdge(int insn, int successor) {
            CFGNode cfgNode = (CFGNode) getFrames()[insn];
            cfgNode.successors.add((CFGNode) getFrames()[successor]);
        }
    }


    // get a list of CFGNode
    private List<CFGNode> analyzeCFG(String className, MethodNode method) {
        Analyzer cfgAnalyzer = new CFGAnalyzer(new BasicInterpreter());
        try {
            cfgAnalyzer.analyze(className, method);
        } catch (AnalyzerException e) {
            e.printStackTrace();
        }

        List<CFGNode> cfg = new ArrayList<CFGNode>();
        for (Frame frame : cfgAnalyzer.getFrames()) {
            if (frame != null)
                cfg.add((CFGNode) frame);
        }
        return cfg;
    }

    // get Cyclomatic Complexity
    public int getCyclomaticComplexity(String className, MethodNode method) {
        int numberOfEdges = 0;
        int numberOfNodes = 0;

        List<CFGNode> listOfNode = analyzeCFG(className, method);
        for (CFGNode node : listOfNode) {
            numberOfEdges = numberOfEdges + node.successors.size();
            numberOfNodes = numberOfNodes + 1;
        }

        int CC = numberOfEdges - numberOfNodes + 2;

        return Math.max(CC, 0);
    }

    // get Number of Loops: The number of loops (for, while) in the method
    public int getNumberOfLoops(String className, MethodNode method) {
        int numberOfLoops = 0;

        List<CFGNode> listOfNode = analyzeCFG(className, method);

        Set<String> loopRecord = new HashSet<String>();
        List<Integer> nodeId = new ArrayList<Integer>();

        if (listOfNode != null && listOfNode.size() > 0) {
            int i = 1;
            for (CFGNode node : listOfNode) {
                node.id = i++;
                node.seen = false;
            }
            DFS(listOfNode.get(0), loopRecord, nodeId);
        }

        if (loopRecord != null) {
            numberOfLoops = loopRecord.size();
        }

        return numberOfLoops;
    }

    // get Number of Loops: The number of loops (for, while) in the method
    public List<Integer> getNesting(String className, MethodNode method) {

        List<Integer> start = new ArrayList<Integer>();
        List<Integer> end = new ArrayList<Integer>();

        Set<String> loopRecord = new HashSet<String>();
        List<Integer> nodeId = new ArrayList<Integer>();

        List<CFGNode> listOfNode = analyzeCFG(className, method);

        if (listOfNode != null && listOfNode.size() > 0) {
            int i = 1;
            for (CFGNode node : listOfNode) {
                node.id = i++;
                node.seen = false;
            }
            DFS(listOfNode.get(0), loopRecord, nodeId);
        }

        if (nodeId.size() > 0) {
            if (nodeId.size() == 1) {
//                start.add(nodeId.get(0));
//                end.add(nodeId.get(0));
                start.add(0);
                end.add(0);

            } else {
                // deal with start and end
                // when size of nodeId > 1
                for (int i = 0; i < (nodeId.size() - 1); i++) {
                    if ((nodeId.get(i + 1)) > (nodeId.get(i) + 1)) {
                        start.add(nodeId.get(i));
                        end.add(nodeId.get(i + 1));
                    }
                }

                // those has back edge (index)
                Set<Integer> idList = new HashSet<Integer>();

                for(int i = 0; i < (nodeId.size() - 1); i++){
                    if ((nodeId.get(i + 1)) < nodeId.get(i)){
                        int temp = nodeId.get(i + 1) - 1;
                        if(start.contains(temp)){
                            int id = start.indexOf(temp);
                            idList.add(id);
                        }
                    }
                }

                // remove those don't have back edge
                for(int i = 0; i < start.size(); i++){
                    if(!idList.contains(i)){
                        start.remove(i);
                        end.remove(i);
                    }
                }
            }
        }

        // pairList: a list of [startPointID, endPointID]
        List<int[]> pairList = new ArrayList<int[]>();

        TreeNode root = new TreeNode();

        List<Integer> result = new ArrayList<Integer>();

        if (start.size() == end.size() && start.size() > 0) {
            for (int i = 0; i < start.size(); i++) {
                int[] pairItem = new int[2];
                pairItem[0] = start.get(i);
                pairItem[1] = end.get(i);
                pairList.add(pairItem);
            }

            // form nesting tree
            int lenOfPair = pairList.size();

            for (int i = 0; i < lenOfPair; i++){
                addNode(root, pairList.get(i));
            }

            int rootKidSize = root.kids.size();
            if(rootKidSize > 0){
                for(int j = 0; j < rootKidSize; j++){
                    result.add(root.kids.get(j).depth);
                }
            }
            else {
                result.add(0);
            }
        }
        // if null
        else{
            result.add(0);
        }
        return result;
    }

    public int addNode(TreeNode node, int[] value) {
        int endOfValue = value[1];
        int lenOfKid = node.kids.size();
        if (lenOfKid > 0) {
            TreeNode kidLast = node.kids.get(lenOfKid - 1);
            int endOfKid = kidLast.value.end;
            if (endOfKid > endOfValue) {
                int kidDepth = addNode(kidLast, value);
                node.depth = Math.max(kidDepth + 1, node.depth);
                return node.depth;
            }
        }

        TreeNode newNode = new TreeNode();
        newNode.value.start = value[0];
        newNode.value.end = value[1];
        newNode.level = node.level + 1;
        newNode.depth = 0;
        node.kids.add(newNode);
        node.depth = Math.max(1, node.depth);
        return node.depth;
    }

    public class Interval {
        public int start;
        public int end;

        public Interval() {

        }
        public Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }


    }

    public class TreeNode {
        public Interval value = new Interval();
        public int depth = 0;
        public int level = 0;
        public java.util.List<TreeNode> kids = new java.util.ArrayList<TreeNode>();
    }

    public void DFS(CFGNode node, Set<String> loopRecord, List<Integer> nodeId) {
        node.seen = true;
        //      System.out.println(node.id);
        nodeId.add(node.id);
        for (CFGNode s : node.successors) {
            if (!s.seen)
                DFS(s, loopRecord, nodeId);
            else {
                // loop: node->s
                loopRecord.add(node.hashCode() + " " + s.hashCode());
            }
        }
        node.seen = false;
    }
}