package org.gobiiproject.gobiiprocess.gobiiadl;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by VCalaminos on 7/1/2019.
 */
public class GobiiAdlHelper {

    public static void printADLSummary(HashMap<String, List<String>> errorList) {

        if (errorList.size() == 0) {
            System.out.println("\nADL process successfully finished!");
        } else {

            System.out.println("\nADL process finished but with errors");

            for (Map.Entry<String, List<String>> entry : errorList.entrySet()) {

                String headerMessage = "\n\nError for scenario: " + entry.getKey();
                System.out.println(headerMessage);

                for (String error : entry.getValue()) {
                    System.out.println(error);
                }
            }
        }

        System.exit(0);

    }


}
