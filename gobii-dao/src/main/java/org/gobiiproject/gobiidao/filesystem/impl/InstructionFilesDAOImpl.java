package org.gobiiproject.gobiidao.filesystem.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.InstructionFilesDAO;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.headerlesscontainer.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiJobStatus;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Phil on 4/12/2016.
 * Modified by Angel 12/12/2016
 */
public class InstructionFilesDAOImpl implements InstructionFilesDAO {

    private final String LOADER_FILE_EXT = ".json";

    public void writePlainFile(String fileFqpn, byte[] byteArray) throws GobiiDaoException {

        try {

            File file = new File(fileFqpn);

            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(file));
            stream.write(byteArray);
            stream.close();

        } catch (IOException e) {
            throw new GobiiDaoException("Error wriring file " + fileFqpn + ": " + e.getMessage());
        }


    }

    @Override
    public boolean doesPathExist(String pathName) throws GobiiDaoException {
        return new File(pathName).exists();
    }

    @Override
    public void verifyDirectoryPermissions(String pathName) throws GobiiDaoException {

        File pathToCreate = new File(pathName);
        if (!pathToCreate.canRead() && !pathToCreate.setReadable(true, false)) {
            throw new GobiiDaoException("Unable to set read permissions on directory " + pathName);
        }

        if (!pathToCreate.canWrite() && !pathToCreate.setWritable(true, false)) {
            throw new GobiiDaoException("Unable to set write permissions on directory " + pathName);
        }
    }


    @Override
    public void makeDirectory(String pathName) throws GobiiDaoException {

        if (!doesPathExist(pathName)) {

            File pathToCreate = new File(pathName);

            if (!pathToCreate.mkdirs()) {
                throw new GobiiDaoException("Unable to create directory " + pathName);
            }

            if ((!pathToCreate.canRead()) && !(pathToCreate.setReadable(true, false))) {
                throw new GobiiDaoException("Unable to set read on directory " + pathName);
            }

            if ((!pathToCreate.canWrite()) && !(pathToCreate.setWritable(true, false))) {
                throw new GobiiDaoException("Unable to set write on directory " + pathName);
            }




        } else {
            throw new GobiiDaoException("The specified path already exists: " + pathName);
        }
    }

    @Override
    public List<GobiiExtractorInstruction> setGobiiJobStatus(boolean applyToAll, List<GobiiExtractorInstruction> instructions, GobiiFileProcessDir gobiiFileProcessDir) throws GobiiDaoException {
        List<GobiiExtractorInstruction> returnVal = instructions;

        GobiiJobStatus gobiiJobStatus;

        switch (gobiiFileProcessDir) {

            case EXTRACTOR_INPROGRESS:
                gobiiJobStatus = GobiiJobStatus.IN_PROGRESS;
                break;

            case EXTRACTOR_INSTRUCTIONS:
                gobiiJobStatus = GobiiJobStatus.STARTED;
                break;

            case EXTRACTOR_DONE:
                gobiiJobStatus = GobiiJobStatus.COMPLETED;
                break;

            default:
                gobiiJobStatus = GobiiJobStatus.FAILED;
        }

        if (applyToAll) {

            for (GobiiExtractorInstruction instruction : returnVal) {

                for (GobiiDataSetExtract dataSetExtract : instruction.getDataSetExtracts()) {

                    dataSetExtract.setGobiiJobStatus(gobiiJobStatus);
                }
            }
        } else { //check if the output file(s) exist in the directory specified by the *extractDestinationDirectory* field of the *DataSetExtract* item in the instruction file;
            GobiiJobStatus statusFailed = GobiiJobStatus.FAILED;

            for (GobiiExtractorInstruction instruction : returnVal) {

                for (GobiiDataSetExtract dataSetExtract : instruction.getDataSetExtracts()) {

                    String extractDestinationDirectory = dataSetExtract.getExtractDestinationDirectory();

                    List<String> datasetExtractFiles = new ArrayList<String>();

                    String fileName = "DS" + Integer.toString(dataSetExtract.getDataSet().getId());

                    switch (dataSetExtract.getGobiiFileType()) {
                        case GENERIC:
                            //fileNames.add(fileName+".txt"); to be added
                            break;

                        case HAPMAP:
                            datasetExtractFiles.add(fileName + "hmp.txt");
                            break;

                        case FLAPJACK:
                            datasetExtractFiles.add(fileName + ".map");

                            datasetExtractFiles.add(fileName + ".genotype");

                            break;

                        case VCF:
                            //fileNames.add(fileName+"hmp.txt"); to be added
                            break;

                        default:
                            throw new GobiiDaoException("Noe extension assigned for GobiiFileType: " + dataSetExtract.getGobiiFileType().toString());
                    }


                    for (String s : datasetExtractFiles) {

                        String currentExtractFile = extractDestinationDirectory + s;

                        if (doesPathExist(currentExtractFile)) dataSetExtract.setGobiiJobStatus(gobiiJobStatus);

                        else dataSetExtract.setGobiiJobStatus(statusFailed);
                    }
                }
            }
        }
        return returnVal;
    }




} // InstructionFilesDAOImpl
