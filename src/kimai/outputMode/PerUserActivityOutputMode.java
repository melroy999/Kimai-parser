package kimai.outputMode;

import kimai.LogEntry;
import kimai.Main;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerUserActivityOutputMode implements IOutputMode {

    @Override
    public void output(Map<String, List<LogEntry>> activities, Map<String, List<LogEntry>> users) {
        for(String user : users.keySet()) {
            printUser(user, users.get(user));
        }
    }

    @Override
    public String getSubFolder() {
        return "per_user_output";
    }

    private void printUser(String user, List<LogEntry> userEntries) {
        //First we will compress the log entries.
        Map<String, LogEntry> activityToLog = new HashMap<>();
        Map<String, LogEntry> activityToLogAfterDate = new HashMap<>();

        //Make sure that we count up all the times.
        for(LogEntry entry : userEntries) {
            LogEntry _entry = activityToLog.getOrDefault(entry.activity, new LogEntry(0, entry.activity, entry.user));
            _entry.time += entry.time;
            activityToLog.put(entry.activity, _entry);

            String time = entry.date + "." + entry.in;
            if(Main.dateFilter.compareTo(time) <= 0) {
                LogEntry _entry_after_date = activityToLogAfterDate.getOrDefault(entry.activity, new LogEntry(0, entry.activity, entry.user));
                _entry_after_date.time += entry.time;
                activityToLogAfterDate.put(entry.activity, _entry_after_date);
            }
        }

        //Output all the entries.
        try(PrintWriter writer = new PrintWriter("./output/" + getSubFolder() + "/" + user + ".csv", "UTF-8")) {
            writer.println("\"sep=;\"");
            writer.println("Back log item; Hours spent this week; Hours spent total");

            double totalTime = 0;
            double totalTimeAfterDate = 0;
            for(String activity : activityToLog.keySet()) {
                LogEntry entry  = activityToLog.get(activity);
                writer.print(entry.activity + "; ");

                LogEntry entryAfterDate = activityToLogAfterDate.get(activity);
                writer.printf("%.2f", entryAfterDate == null ? 0 : entryAfterDate.time);
                totalTimeAfterDate += entryAfterDate == null ? 0 : entryAfterDate.time;

                writer.print("; ");
                writer.printf("%.2f", entry.time);
                writer.println();
                totalTime += entry.time;
            }

            writer.print("");
            writer.print("Total time:; ");
            writer.printf("%.2f", totalTimeAfterDate);
            writer.print("; ");
            writer.printf("%.2f", totalTime);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
