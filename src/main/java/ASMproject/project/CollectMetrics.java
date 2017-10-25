package ASMproject.project;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class CollectMetrics {

	public static void main(String args[]) throws Exception {
		
		System.out.println(String.join(",", Arrays.asList(new String[] { "methodName", "linesOfCode"})));
//		for (int i=0;i< args.length;i++) {
//			String arg = args[i];
            String arg = "resource/aalto-xml/classes/com/fasterxml/aalto/async/AsyncByteScanner.class";
			System.out.printf("Calculating metrics for class file %s\n", arg);
			FileInputStream is = new FileInputStream(arg);

			ClassReader reader = new ClassReader(is);

			ClassNode classNode = new ClassNode();
			reader.accept(classNode, 0);

			System.out.printf("Calculating metrics for each method");

			for (MethodNode method : (List<MethodNode>) classNode.methods) {
				String metrics = collectMetrics(classNode, method);
				System.out.println(metrics);
			}

//		}

	}

	private static String collectMetrics(ClassNode classNode, MethodNode method) {

        // collect class names and methods' names
		String methodName = String.format("%s.%s", classNode.name, method.name);

        // collect arguments
        Arguments arguments = new Arguments(null);
        method.accept(arguments);
        //  String argumentsList = arguments.getArgumentsList();
        int numOfArguments = arguments.getArgumentNum();

        // collect variable declarations
        VariableDeclarations vd = new VariableDeclarations(null);
        method.accept(vd);
        int variableDelarationNum = vd.getVariableNum();

        // collect Halstead length
        Halstead halstead = new Halstead(null);
        method.accept(halstead);
        long halsteadLength = halstead.info.HalsteadLength();

        // collect Halstead vocabulary
        long halsteadVocabulary = halstead.info.HalsteadVocabulary();

        // collect Halstead volumne
        double halsteadVolume = halstead.info.HalsteadVolume();

        // collect Halstead difficulty
        double halsteadDifficulty = halstead.info.HalsteadDifficulty();

        // collect Halstead effort
        double halsteadEffort = halstead.info.HalsteadEffort();

        // collect Halstead bugs
        double halsteadBugs = halstead.info.HalsteadBugs();

        // collect number of operators
        long numOfOperators = halstead.info.operatorsNum();

        // collect number of operands
        long numOfOperands = halstead.info.operandsNum();

        // collect unique number of operators
        long uniOperators = halstead.info.uniqueOperatorsNum();

        // collect unique number of operands
        long uniOperands = halstead.info.uniqueOperandsNum();

        // collect lines of code
        LinesOfCode lineCount = new LinesOfCode(null);
        method.accept(lineCount);
        int lines = lineCount.getLines();

        String result = methodName + "," + numOfArguments + "," + variableDelarationNum + "," +
                halsteadLength + "," + halsteadVocabulary + "," + halsteadVolume + ","
                + halsteadDifficulty + "," + halsteadEffort + "," +
                halsteadBugs + "," + numOfOperators + "," + numOfOperands + ","
                + uniOperators + "," + uniOperands + "," + lines;

		return result;
	}

}
