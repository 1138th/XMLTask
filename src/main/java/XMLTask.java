import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class XMLTask {
    public static void main(String[] args) {
        File XMLFile = new File("src\\XMLTask.xml");
        File directory = new File("src");
        try {
            if (XMLFile.createNewFile()) System.out.println("File has been successfully created.");
            BufferedWriter writer = new BufferedWriter(new FileWriter(XMLFile));
            FileWork.fillDirectory();
            FileWork.fillXML(directory, writer);
            writer.close();
        } catch (Exception e){
            System.out.println("Haven't access to create or edit file.");
        }
    }
}