
package com.yottaflex;

import com.yottaflex.processing.PipelineService;
import com.yottaflex.model.PipelineResult;

public class Main {

    public static void main(String[] args) {

        String transactionFile = "data/input/transactions.csv";
        String accountFile = "data/input/accounts.csv";

        if (args.length >= 2) {
            transactionFile = args[0];
            accountFile = args[1];
        }

        try {
            PipelineService pipelineService = new PipelineService();

            System.out.println("Starting Transaction Pipeline...");

            PipelineResult result = pipelineService.execute(
                    transactionFile,
                    accountFile
            );

            System.out.println("Pipeline Status: " + result.getStatus());
            System.out.println("Accepted Records: " + result.getAcceptedTransactions().size());
            System.out.println("Rejected Records: " + result.getRejectedTransactions().size());

            System.out.println("Transaction Pipeline Completed.");

        } catch (Exception e) {
            System.err.println("Pipeline failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
