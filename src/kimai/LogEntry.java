package kimai;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LogEntry {
    public static final int dateIndex = 0;
    public static final int inIndex = 1;
    public static final int timeIndex = 4;
    public static final int activityIndex = 13;
    public static final int userIndex = 18;

    public double time;
    public String activity;
    public String user;
    public String date;
    public String in;

    public LogEntry(double time, String activity, String user) {
        this.time = time;
        this.activity = activity;
        this.user = user;
    }

    private LogEntry(double time, String activity, String user, String date, String in) {
        this.time = time;
        this.activity = activity;
        this.user = user;
        this.date = date;
        this.in = in;
    }

    public static LogEntry getEntry(String line, Map<String, List<LogEntry>> activities, Map<String, List<LogEntry>> users) {
        String[] components = line.split(";");

        System.out.println(line);

        //The instance.
        LogEntry entry = new LogEntry(Double.parseDouble(components[timeIndex]), components[activityIndex],
                components[userIndex], components[dateIndex], components[inIndex]);

        //Add it to the maps.
        List<LogEntry> activityLogs = activities.getOrDefault(components[activityIndex], new ArrayList<>());
        activityLogs.add(entry);
        activities.put(components[activityIndex], activityLogs);

        List<LogEntry> userLogs = users.getOrDefault(components[userIndex], new ArrayList<>());
        userLogs.add(entry);
        users.put(components[userIndex], userLogs);

        return entry;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "time=" + time +
                ", activity='" + activity + '\'' +
                ", user='" + user + '\'' +
                ", date='" + date + '\'' +
                ", in='" + in + '\'' +
                '}';
    }
}
