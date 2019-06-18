import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

class FileWork {

    static void fillXML(File directory, BufferedWriter writer) throws Exception{
        writer.write("<data>\n");
        BasicFileAttributes attributes;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        for (File directoryItem : directory.listFiles()) {
            attributes = Files.readAttributes(directoryItem.toPath(), BasicFileAttributes.class);
            if (directoryItem.isFile()){
                writer.write("\t<file name=\"" + directoryItem.getName() +
                        "\" created=\"" + dateFormat.format(new Date(attributes.creationTime().toMillis())) +
                        "\" size=\"" + attributes.size() + "\" />\n");
            }
            if (directoryItem.isDirectory()){
                writer.write("\t<dir name=\"" + directoryItem.getName() +
                        "\" created=\"" + dateFormat.format(new Date(attributes.creationTime().toMillis())) +
                        "\" size=\"" + folderSize(directoryItem) + "\" />\n");
                folderContent(directoryItem, writer, attributes, dateFormat);
            }
        }
        writer.write("</data>");
    }

    static void fillDirectory(){
        File taskFolder;
        File taskFile;
        BufferedWriter writer;

        for (int i = 0; i < 3; i++){
            taskFolder = new File("src\\TaskFolder" + (i + 1));
            if (taskFolder.mkdir()) System.out.println("Directory " + taskFolder.getName() + " has been created");

            for (int j = 0; j < 3; j++){
                taskFile = new File(taskFolder + "\\TaskFile" + (i + 1) + "" + (j + 1) + ".txt");
                try {
                    if (taskFile.createNewFile())
                        System.out.println("File " + taskFile.getName() + " in folder " +
                                taskFolder.getName() + " has been created");
                    writer = new BufferedWriter(new FileWriter(taskFile));
                    writer.write("Text in "  + taskFile + " creating automatically.\n");
                    generateRandText(writer);
                    writer.close();
                } catch (Exception e){
                    System.out.println("Haven't access to create or edit file.");
                    return;
                }
            }
        }
    }

    private static void generateRandText(Writer writer) throws Exception{
        Random rand = new Random();
        int numberOfLines = rand.nextInt(100) + 50;
        int symbolsInLine = rand.nextInt(100) + 50;
        //char: 48-122 - numbers, letters and some special symbols
        for (int lines = 0; lines < numberOfLines; lines++){
            for (int symbol = 0; symbol < symbolsInLine; symbol++){
                writer.write(((char) (rand.nextInt(74) + 48)) + " ");
            }
            writer.write("\n");
        }
    }

    private static long folderSize(File directory){
        long length = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile()){
                length += file.length();
            } else{
                length += folderSize(file);
            }
        }
        return length;
    }

    private static void folderContent(File directory, Writer writer, BasicFileAttributes attributes, SimpleDateFormat dateFormat) throws Exception{
        for (File file : directory.listFiles()) {
            if (file.isFile()){
                attributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                writer.write("\t\t<file name=\"" + file.getName() +
                        "\" created=\"" + dateFormat.format(new Date(attributes.creationTime().toMillis())) +
                        "\" size=\"" + attributes.size() + "\" />\n");
            } else{
                folderContent(file, writer, attributes, dateFormat);
            }
        }
    }
}