package org.gobiiproject.gobiidomain.services.gdmv3;

import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utils {

    static ConfigSettings configSettings = new ConfigSettings();

    static String getProcessDir(String cropType, GobiiFileProcessDir gobiiFileProcessDir
    ) throws GobiiDomainException {
        try {

            String inputFileDir = Utils.configSettings.getProcessingPath(
                cropType,
                gobiiFileProcessDir);
            makeDir(inputFileDir);
            return inputFileDir;
        }
        catch (Exception e) {
            throw new GobiiDomainException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.NONE,
                "Unable to create input files directory");
        }
    }

    static void writeByteArrayToFile(String filePath, byte[] fileContent
    ) throws GobiiDomainException {
        try {
            File file = new File(filePath);
            makeDir(file.getParent());
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
            stream.write(fileContent);
            stream.close();
        }
        catch (IOException e) {
            throw new GobiiDomainException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.NONE,
                "Unable to create input files directory");
        }
    }

    static void makeDir(String dirPath) {
        File file = new File(dirPath);
        if(!file.isDirectory()) {
            file.mkdirs();
        }
    }
}
