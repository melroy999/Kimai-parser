package kimai.outputMode;

import kimai.LogEntry;

import java.util.List;
import java.util.Map;

public interface IOutputMode {
    void output(Map<String, List<LogEntry>> activities, Map<String, List<LogEntry>> users);

    String getSubFolder();
}
