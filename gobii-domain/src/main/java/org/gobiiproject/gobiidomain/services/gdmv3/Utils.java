package org.gobiiproject.gobiidomain.services.gdmv3;

import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    static void setField(Object instance, String fieldName, Object value
    ) throws NoSuchFieldException, IllegalAccessException {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);

    }

    /**
     * @param cvs List of cvs
     * @return map of Cvs by their names
     */
    static Map<String, Cv> mapCyNames(List<Cv> cvs) {
        Map<String, Cv> cvMap = new HashMap<>();
        for(Cv cv : cvs) {
            cvMap.put(cv.getTerm(), cv);
        }
        return cvMap;
    }
}
