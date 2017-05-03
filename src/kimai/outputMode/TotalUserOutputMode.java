package kimai.outputMode;

import kimai.LogEntry;
import kimai.Main;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class TotalUserOutputMode implements IOutputMode {
    @Override
    public void output(Map<String, List<LogEntry>> activities, Map<String, List<LogEntry>> users) {
        double grandTotal = 0;
        double grandTotalAfterDate = 0;

        try(PrintWriter writer = new PrintWriter("./output/user_totals.csv", "UTF-8")) {
            writer.println("\"sep=;\"");
            writer.println("User; Hours spent this week; Hours spent total");

            for(String user : users.keySet()) {
                //Output all the entries.

                double totalTime = 0;
                double totalTimeAfterDate = 0;
                for(LogEntry entry : users.get(user)) {
                    totalTime += entry.time;

                    String time = entry.date + "." + entry.in;
                    if(Main.dateFilter.compareTo(time) <= 0) {
                        totalTimeAfterDate += entry.time;
                    }
                }

                writer.print(users.get(user).get(0).user + "; ");
                writer.printf("%.2f", totalTimeAfterDate);
                writer.print("; ");
                writer.printf("%.2f", totalTime);
                writer.println("");

                grandTotal += totalTime;
                grandTotalAfterDate += totalTimeAfterDate;
            }

            writer.println("");
            writer.print("Total: ; ");
            writer.printf("%.2f", grandTotalAfterDate);
            writer.print("; ");
            writer.printf("%.2f", grandTotal);

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
