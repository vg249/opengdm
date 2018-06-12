package org.gobiiproject.gobiidao.gql;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class GqlWrapper {

    Logger LOGGER = LoggerFactory.getLogger(GqlWrapper.class);
    public void run(String execString, String outputFileName, String errorFileName) throws Exception {


        Integer timeOutSecs = 10;

        String executedProcName = HelperFunctions.makeExecString(execString);
        Process process = HelperFunctions.initProcecess(executedProcName, outputFileName, errorFileName, null, timeOutSecs);

        Integer exitValue = process.exitValue();
        if (exitValue != 0) {
            String errorText = "";
           BufferedReader br = new BufferedReader(new FileReader(errorFileName));
            while (br.ready()) {
                errorText += br.readLine() + "\n";
            }
            String message = "Error executing gql query " + executedProcName + ": " + errorText;
            LOGGER.error(errorText);
            throw new GobiiDaoException(message);

        }

    } // end method run()

} // end class