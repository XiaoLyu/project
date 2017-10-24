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

        // collect lines of code
		LinesOfCode lineCount = new LinesOfCode(null);
		method.accept(lineCount);
		int lines = lineCount.getLines();
		return methodName + "," + lines;
	}

}
