package ASMproject.project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyuxiao on 11/4/17.
 * find all the .class files
 */
public class FindAllClassFile {

    private List<String> p = new ArrayList<String>();

    public List<String> findAllClassFile(String folder){
        File directory = new File(folder);
        if(directory.isFile()){
            p.add(folder);
            return p;
        }

        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                if(file.getName().endsWith(".class")) {
//                    System.out.println("File: " + file.getAbsolutePath());
                    p.add(file.getAbsolutePath());
                }

            } else {
                findAllClassFile(file.toString());
            }
        }
        return p;
    }
}
