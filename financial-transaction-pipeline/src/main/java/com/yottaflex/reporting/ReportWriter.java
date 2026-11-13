
package com.yottaflex.reporting;

import com.yottaflex.model.RunReport;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReportWriter {

    public void writeReport(RunReport report, String filePath)
            throws Exception {

        Files.createDirectories(Paths.get(filePath).getParent());

        FileWriter writer = new FileWriter(filePath);

        CSVPrinter printer = new CSVPrinter(
                writer,
                CSVFormat.DEFAULT.withHeader(
                        "run_id",
                        "input_count",
                        "accepted_count",
                        "rejected_count",
                        "error_count",
                        "status"
                )
        );

        printer.printRecord(
                report.getRunId(),
                report.getInputCount(),
                report.getAcceptedCount(),
                report.getRejectedCount(),
                report.getErrorCount(),
                report.getStatus()
        );

        printer.close();
    }
}
