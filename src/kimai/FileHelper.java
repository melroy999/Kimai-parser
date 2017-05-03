package kimai;

import kimai.outputMode.IOutputMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {
    /**
     * Read all lines in the given filename, and return them as a list.
     *
     * @param fileName Name and path to the file.
     * @return List of strings containing the individual lines of the file.
     * @throws IOException When the resource cannot be loaded.
     */
    @SuppressWarnings("StringConcatenationInLoop")
    public static List<String> readAllLines(String fileName) throws IOException {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String fullLine="";
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                fullLine = fullLine + currentLine;
                int length = fullLine.split(";").length;
                if (length == 20){
                    list.add(fullLine);
                    fullLine = "";
                }else if (length>20){
                    throw new Error("Line too long: " + currentLine);
                }
            }
        }
        return list;
    }

    public static void createDirectoryStructure(IOutputMode[] modes) {
        //Create main folder.
        File root = new File("./output");
        if(!root.exists()) {
            if (!root.mkdir()) throw new Error("AAAAAAAAH");
        }

        for(IOutputMode mode : modes) {
            if(mode.getSubFolder().equals("")) continue;

            File subfolder = new File("./output/" + mode.getSubFolder());
            if(!subfolder.exists()) {
                if (!subfolder.mkdir()) throw new Error("AAAAAAAAH");
            }
        }
    }
}
