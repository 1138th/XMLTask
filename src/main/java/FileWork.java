import java.io.BufferedWriter;
import java.io.File;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;

class FileWork {

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

    static void xmlFiller(File directory, BufferedWriter writer) throws Exception{
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
}