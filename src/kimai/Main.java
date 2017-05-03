package kimai;

import kimai.outputMode.IOutputMode;
import kimai.outputMode.PerActivityOutputMode;
import kimai.outputMode.PerUserActivityOutputMode;
import kimai.outputMode.TotalUserOutputMode;

import java.io.IOException;
import java.util.*;

public class Main {
    public static final String dateFilter = "04.27.17:00";

    public static void main(String[] args) {
        //Export settings:
        //Date Format: %m.%d , Time Format: H:M

        String sourceFileName = "resources/export.csv";

        //List of output modes we will use.
        IOutputMode[] outputModes = new IOutputMode[]{
                new PerUserActivityOutputMode(),
                new PerActivityOutputMode(),
                new TotalUserOutputMode()
        };

        //Create the output folders.
        FileHelper.createDirectoryStructure(outputModes);

        try {
            List<String> sourceFileLines = FileHelper.readAllLines(sourceFileName);

            Map<String, List<LogEntry>> activities = new HashMap<>();
            Map<String, List<LogEntry>> users = new HashMap<>();

            List<LogEntry> entries = new ArrayList<>();
            for(int i = 1; i < sourceFileLines.size(); i++) {
                String line = sourceFileLines.get(i);

                //Skip lines without data
                if(!line.contains(";")) continue;

                //Create a list of log entries.
                entries.add(LogEntry.getEntry(line, activities, users));
            }

            //Output it to the correct files and folders.
            for(IOutputMode outputMode : outputModes) {
                outputMode.output(activities, users);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


