
package com.yottaflex.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class RunIdGenerator {

    public static String generateRunId() {

        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        );

        String uniquePart = UUID.randomUUID()
                .toString()
                .substring(0, 8);

        return "RUN_" + timestamp + "_" + uniquePart;
    }
}
