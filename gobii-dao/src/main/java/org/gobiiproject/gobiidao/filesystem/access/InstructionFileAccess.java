package org.gobiiproject.gobiidao.filesystem.access;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

/**
 * Created by araquel on 3/16/2017.
 */
public class InstructionFileAccess<T> {

    private final String LOADER_FILE_EXT = ".json";

    public Boolean writeInstructions(String instructionFileFqpn,
                                    T instructions) throws GobiiDaoException {

         Boolean returnVal = null;

        try {

            File instructionFile = new File(instructionFileFqpn);
            if (!instructionFile.exists()) {

                String filePath = instructionFile.getAbsolutePath().
                        substring(0, instructionFile.getAbsolutePath().lastIndexOf(File.separator));

                File destinationDirectory = new File(filePath);

                if (destinationDirectory.exists()) {

                    if (destinationDirectory.isDirectory()) {

                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
                        objectMapper.enable(SerializationFeature.WRITE_NULL_MAP_VALUES);
                        String instructionsAsJson = objectMapper.writeValueAsString(instructions);
                        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(instructionFileFqpn));
                        bufferedWriter.write(instructionsAsJson);
                        bufferedWriter.flush();
                        bufferedWriter.close();

                        returnVal = true;
                    } else {
                        throw new GobiiDaoException("Path of specified instruction file name is not a directory: "
                                + destinationDirectory);
                    } // if-else directory is really a directory

                } else {
                    throw new GobiiDaoException("Path of specified instruction file does not exist: "
                            + destinationDirectory);

                } // if-else destination directory exists

            } else {

                throw new GobiiDaoException("The specified instruction file already exists: "
                        + instructionFileFqpn);
            } // if-else file does not arleady exist

        } catch (Exception e) {
            String message = e.getMessage() + "; fqpn: " + instructionFileFqpn;
            throw new GobiiDaoException(message);
        }

        return returnVal;

    } // writeInstructions


    public T getInstructions(String instructionFileFqpn, Class<T> instructionType) {

        T returnVal = null;

        try {

            File file = new File(instructionFileFqpn);

            FileInputStream fileInputStream = new FileInputStream(file);

            org.codehaus.jackson.map.ObjectMapper objectMapper = new org.codehaus.jackson.map.ObjectMapper();

            returnVal = objectMapper.readValue(fileInputStream, instructionType);

        } catch (Exception e) {
            String message = e.getMessage() + "; fqpn: " + instructionFileFqpn;
            throw new GobiiDaoException(message);
        }

        return returnVal;

    }


}
