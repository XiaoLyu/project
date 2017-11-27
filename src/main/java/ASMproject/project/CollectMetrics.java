package ASMproject.project;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

import com.opencsv.CSVWriter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "collectMetrics")
public class CollectMetrics extends AbstractMojo{

    @Parameter (defaultValue="{project.basedir}", required=true, readonly=true)
    public File inputPath;

    public void execute() throws MojoExecutionException, MojoFailureException {
        // folder path
        String arg = inputPath.getParent();

        // folder's parent
        String fParent = inputPath.getParentFile().getParentFile().getParent();

        // file name
        String fName = inputPath.getParentFile().getName();

        String folder = fParent + "/resource/" + fName;

        FindAllClassFile className = new FindAllClassFile();
        List<String> classFileNames = className.findAllClassFile(folder);

        String[] header = {"Method Name",
                "Number Of Arguments", "Variable Declarations", "Variable References", "Halstead Length",
                "Halstead Vocabulary", " Halstead Volume",
                "Halstead Difficulty", "Halstead Effort", "Halstead Bugs", "Number Of Casts", "Number Of Operators",
                "Number Of Operands", "Class References", "External Methods", "Local Methods", "Exceptions Referenced",
                "Exceptions Thrown", "Modifiers", "Lines Of Code", "Cyclomatic Complexity"};

        //     System.out.println(String.join(",", Arrays.asList(header)));

        // write to csv file
        // needed to be modified when write all the files

        String dire = fParent + "/result";

        String nameOfFile = fParent + "/result/" + fName + ".csv";

        File directory = new File(dire);

        if (! directory.exists()){
            directory.mkdir();
        }

        File file = new File(nameOfFile);

        CSVWriter csvWrite;

        try {
            csvWrite = new CSVWriter(new FileWriter(file));

            // add headers
            csvWrite.writeNext(header);

            for (int i = 0; i < classFileNames.size(); i++) {
                String classPath = classFileNames.get(i);
                //            System.out.printf("Calculating metrics for class file %s\n", classPath);
                FileInputStream is = new FileInputStream(classPath);

                ClassReader reader = new ClassReader(is);

                ClassNode classNode = new ClassNode();
                reader.accept(classNode, 0);

                for (MethodNode method : (List<MethodNode>) classNode.methods) {
                    String metrics;
                    try {
                        metrics = collectMetrics(classNode, method);

                        // add body
                        csvWrite.writeNext(metrics.split(","));
                    } catch (AnalyzerException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            csvWrite.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static String collectMetrics(ClassNode classNode, MethodNode method) throws AnalyzerException {

        String desAll = method.desc;

        // collect class names and methods' names
        String methodName = String.format("%s.%s:%s", classNode.name, method.name, desAll);

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
        String exThrown = ex.getNames();

        // collect modifiers
        Modifiers modifiers = new Modifiers(null);
        method.accept(modifiers);
        String modi = modifiers.getModifier();

        // collect lines of code
        LinesOfCode lineCount = new LinesOfCode(null);
        method.accept(lineCount);
        int lines = lineCount.getLines();

        // collect cyclomatic complexity
        CyclomaticComplexity complexity = new CyclomaticComplexity();
        int cyclomaticComplexity = complexity.getCyclomaticComplexity(classNode.name, method);

        String result = methodName + "," + numOfArguments + "," + variableDelarationNum +
                "," + variableReferenceNum + ","+ halsteadLength + "," + halsteadVocabulary
                + "," + halsteadVolume + "," + halsteadDifficulty + "," + halsteadEffort +
                "," + halsteadBugs + "," + castingNum + "," + numOfOperators + "," +
                numOfOperands + ","  + classReferenceNames + "," + localMethods + ","
                + externalMethods +  "," + exName + "," + exThrown + "," + modi + "," + lines + "," + cyclomaticComplexity;

        return result;
    }
}