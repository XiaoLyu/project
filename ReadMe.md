How to run our code:

First compile our project by using:
mvn clean install

Create a folder names 'resource' in the main directory, add those compiled testing projects into this folder.

Configure testing project pom.xml file by inserting:

```
<build>
    <plugins>
        <plugin>
            <groupId>ASMproject</groupId>
            <artifactId>project</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </plugin>
      <plugins>
</build>
```


And then go to the project folder which you want to test under /resource folder, run:
mvn ASMproject:project:0.0.1-SNAPSHOT:collectMetrics

Then you could find a corresponding csv file with a same name under /result folder in this project main directory.

Resource folder already added ten compiled real-world projects. And they are already configured the pom.xml file. You could just run following comand:

cd /project
<br>mvn clean install


cd /resource/akela
<br>mvn ASMproject:project:0.0.1-SNAPSHOT:collectMetrics

The csv files in result folder are their results after applying this maven plugin.

