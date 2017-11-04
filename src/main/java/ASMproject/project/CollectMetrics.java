package ASMproject.project;

import java.io.*;
import java.util.*;

import com.opencsv.CSVWriter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class CollectMetrics {

    public static void main(String args[]) throws Exception {

//		for (int i=0;i< args.length;i++) {
//			String arg = args[i];
        String arg = "resource/aalto-xml/classes/com/fasterxml/aalto/async/AsyncByteScanner.class";

        System.out.printf("Calculating metrics for class file %s\n", arg);
        FileInputStream is = new FileInputStream(arg);

        ClassReader reader = new ClassReader(is);

        ClassNode classNode = new ClassNode();
        reader.accept(classNode, 0);

        String[] header = {"Method Name",
                "Number Of Arguments", "Variable Declarations", "Variable References", "Halstead Length",
                "Halstead Vocabulary"," Halstead Volume",
                "Halstead Difficulty","Halstead Effort", "Halstead Bugs", "Number Of Casts", "Number Of Operators",
                "Number Of Operands", "Class References", "External Methods", "Local Methods", "Exceptions Referenced",
                "Exceptions Thrown", "Modifiers", "Lines Of Code"};

   //     System.out.println(String.join(",", Arrays.asList(header)));

        // write to csv file
        // needed to be modified when write all the files
        File file = new File("result/test1.csv");
        CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

        // add headers
        csvWrite.writeNext(header);

  //      System.out.printf("Calculating metrics for each method\n");

        for (MethodNode method : (List<MethodNode>) classNode.methods) {
            String metrics = collectMetrics(classNode, method);
  //          System.out.println(metrics);

            // add body
            csvWrite.writeNext(metrics.split(","));
        }

        csvWrite.close();

//		}

    }

    private static String collectMetrics(ClassNode classNode, MethodNode method) {

        // collect class names and methods' names
        String methodName = String.format("%s.%s", classNode.name, method.name);

        // collect arguments
        Arguments arguments = new Arguments(null);
        method.accept(arguments);
        int numOfArguments = arguments.getArgumentNum();
 //       List<String> referenceList = arguments.getReferencesList();

        // collect variable declarations and variable references
        Variable vd = new Variable(null);
        method.accept(vd);
        int variableDelarationNum = vd.getVariableDeclarationNum();
        int variableReferenceNum = vd.getVariableReferenceNum();

        // collect Halstead length
        Halstead halstead = new Halstead(null);
        method.accept(halstead);
        long halsteadLength = halstead.info.HalsteadLength();

        // collect Halstead vocabulary
        long halsteadVocabulary = halstead.info.HalsteadVocabulary();

        // collect Halstead volume
        double halsteadVolume = halstead.info.HalsteadVolume();

        // collect Halstead difficulty
        double halsteadDifficulty = halstead.info.HalsteadDifficulty();

        // collect Halstead effort
        double halsteadEffort = halstead.info.HalsteadEffort();

        // collect Halstead bugs
        double halsteadBugs = halstead.info.HalsteadBugs();

        // collect number of casting
        Casting casting = new Casting(null);
        method.accept(casting);
        int castingNum = casting.getCastingNum();

        // collect number of operators
        long numOfOperators = halstead.info.operatorsNum();

        // collect number of operands
        long numOfOperands = halstead.info.operandsNum();

        // collect unique number of operators
//        long uniOperators = halstead.info.uniqueOperatorsNum();

        // collect unique number of operands
//        long uniOperands = halstead.info.uniqueOperandsNum();

        // class references
        ClassReferences cl = new ClassReferences(null);
        method.accept(cl);
        String classReferenceNames = cl.getClassReferencesNames();

        // collect method info
        Methods mt = new Methods(null);
        method.accept(mt);

        Set<String> localMethodsList = new HashSet<String>();
        Set<String> externalMethodsList = new HashSet<String>();

        String localMethods = "";
        String externalMethods = "";

        List<String> methodList = mt.getMethodList();
        List<String> classList = mt.getClassNameList();

        for (int i = 0; i < classList.size(); i++){
            if(classList.get(i).equals(classNode.name)){
                localMethodsList.add(methodList.get(i));
            }
            if(! classList.get(i).equals(classNode.name)){
                externalMethodsList.add(methodList.get(i));
            }
        }

        Object[] array1 = localMethodsList.toArray();
        Object[] array2 = externalMethodsList.toArray();

        int lenOfLocal = array1.length;
        int lenOfExter = array2.length;

        // convert set to string for local methods
        for(int i = 0; i < lenOfLocal; i++){
            localMethods = localMethods + array1[i];
            if (i < (lenOfLocal - 1)){
                localMethods = localMethods + " ";
            }
        }

        // convert set to string for external methods
        for(int i = 0; i < lenOfExter; i++){
            externalMethods = externalMethods + array2[i];
            if(i < (lenOfExter - 1)){
                externalMethods = externalMethods + " ";
            }
        }

        // collect exception information
        // exception referenced
        ExceptionReference exceptionInfo = new ExceptionReference(null);
        method.accept(exceptionInfo);
        //      List<String> exinfo = exceptionInfo.getExceptionsNameList();
        String exName = exceptionInfo.getExceptionsName();

        // exception thrown
        ExceptionThrown ex = new ExceptionThrown(null);
        method.accept(ex);
//        List<String> exList = ex.getNames();
        String exThrown = ex.getExceptionThrown();

        // collect modifiers
        Modifiers modifiers = new Modifiers(null);
        method.accept(modifiers);
        String modi = modifiers.getModifier();

        // collect lines of code
        LinesOfCode lineCount = new LinesOfCode(null);
        method.accept(lineCount);
        int lines = lineCount.getLines();

        String result = methodName + "," + numOfArguments + "," + variableDelarationNum +
                "," + variableReferenceNum + ","+ halsteadLength + "," + halsteadVocabulary
                + "," + halsteadVolume + "," + halsteadDifficulty + "," + halsteadEffort +
                "," + halsteadBugs + "," + castingNum + "," + numOfOperators + "," +
                numOfOperands + ","  + classReferenceNames + "," + localMethods + ","
                + externalMethods +  "," + exName + "," + exThrown + "," + modi + "," + lines;

        return result;
    }

}