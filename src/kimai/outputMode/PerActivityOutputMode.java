package kimai.outputMode;

import kimai.LogEntry;
import kimai.Main;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerActivityOutputMode implements IOutputMode {
    @Override
    public void output(Map<String, List<LogEntry>> activities, Map<String, List<LogEntry>> users) {
        //First we will compress the log entries.
        List<LogEntry> activitySums = new ArrayList<>();
        HashMap<String, LogEntry> activitySumsAfterDate = new HashMap<>();

        //Make sure that we count up all the times.
        for(String activity : activities.keySet()) {
            double totalTime = 0;
            double totalTimeAfterDate = 0;
            for(LogEntry entry : activities.get(activity)) {
                totalTime += entry.time;

                String time = entry.date + "." + entry.in;
                if(Main.dateFilter.compareTo(time) <= 0) {
                    totalTimeAfterDate += entry.time;
                }
            }

            //Add it to the list.
            activitySums.add(new LogEntry(totalTime, activity, ""));
            activitySumsAfterDate.put(activity, new LogEntry(totalTimeAfterDate, activity, ""));
        }

        //Output all the entries.
        try(PrintWriter writer = new PrintWriter("./output/activity_overview.csv", "UTF-8")) {
            writer.println("\"sep=;\"");
            writer.println("Back log item; Hours spent this week; Hours spent total");

            double totalTime = 0;
            double totalTimeAfterDate = 0;
            for(LogEntry totalEntry : activitySums) {
                writer.print(totalEntry.activity + "; ");
                LogEntry entry = activitySumsAfterDate.get(totalEntry.activity);
                writer.printf("%.2f", entry == null ? 0 : entry.time);
                totalTimeAfterDate += entry == null ? 0 : entry.time;
                writer.print("; ");
                writer.printf("%.2f", totalEntry.time);
                writer.println();
                totalTime += totalEntry.time;
            }

            writer.println("");
            writer.print("Sum:; ");
            writer.printf("%.2f", totalTimeAfterDate);
            writer.print("; ");
            writer.printf("%.2f", totalTime);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getSubFolder() {
        return "";
    }
}
