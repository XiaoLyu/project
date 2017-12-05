How to run our code:

First compile our project by using:
mvn clean install

Create a folder names 'resource' in the main directory, add those compiled testing projects into this folder.

Configure testing project pom.xml file by inserting:




\*<build>
    \*<plugins>
        \*<plugin>
            \*<groupId>ASMproject</groupId>
            \*<artifactId>project</artifactId>
            \*<version>0.0.1-SNAPSHOT</version>
        \*</plugin>
      \*<plugins>
\*</build>


And then go to the project folder which you want to test under /resouce folder, run:
mvn ASMproject:project:0.0.1-SNAPSHOT:collectMetrics

Then you could find a corresponding csv file with a same name under /result folder in this project main directory.