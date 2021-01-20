package org.gobiiproject.gobiiprocess.aspectmappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.Tika;
import org.gobii.masticator.aspects.TableAspect;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.LoaderInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.MatrixLoaderInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.v3.Table;
import org.gobiiproject.gobiiprocess.digester.utils.FileUtils;

import java.io.*;
import java.util.*;

/**
 * Maps table aspect for hapmap file
 */
@Slf4j
public class HapMapAspectsMapper {

    private MatrixLoaderInstruction matrixLoaderInstruction;

    private final String headerIdentifier = "rs#";

    private final int maxLinesToLookForHeader = 1000;

    private final String[] hapMapRequiredColumns = {
        "rs#",
        "alleles",
        "chrom",
        "pos",
        "strand",
        "assembly#",
        "center",
        "protLSID",
        "assayLSID",
        "panelLSID",
        "QCcode"
    };

    private final ObjectMapper jsonMapper = new ObjectMapper();

    /**
     *
     * @param loaderInstruction   Matrix loader instructions with file type and data format
     *                            details.
     * @return  List of Maps for table name and their respective aspects
     */
    public List<Map<String, Table>> mapHapMapAspects(
        LoaderInstruction loaderInstruction
    ) throws GobiiException {

        File inputFile = new File(loaderInstruction.getInputFile());
        List<File> filesToDigest = new ArrayList<>();

        Scanner fileScanner;

        try {
            if (inputFile.exists()) {

                if(inputFile.isDirectory()) {
                    File[] filesArray = inputFile.listFiles();
                    if(filesArray != null) {
                        filesToDigest.addAll(Arrays.asList(filesArray));
                    }
                }
                // extract files
                else if(inputFile.getName().endsWith(FileUtils.TAR_GUNZIP_EXTENSION) &&
                    inputFile.getName().endsWith(FileUtils.GUNZIP_EXTENSION)) {
                    filesToDigest = FileUtils.extractTarGunZipFile(inputFile);
                }
                else if(inputFile.getName().endsWith(FileUtils.GUNZIP_EXTENSION)) {
                    filesToDigest.add(FileUtils.extractGunZipFile(inputFile));
                }
                else {
                    filesToDigest.add(inputFile);
                }

                for(File fileToDigest : filesToDigest) {

                    // Ignore non text file
                    if(FileUtils.isFileTextFile(fileToDigest)) {
                        continue;
                    }

                    fileScanner = new Scanner(fileToDigest);

                    // Get header line for hapmap.
                    // Usually the first line but in some instances they could be not be.
                    // So check for first 1000 lines. If there is no hapmap header,
                    // assume that file is not a hapmap file.
                    String headerLine = null;
                    int headerLineNum = 1;
                    while (fileScanner.hasNextLine()) {
                        headerLine = fileScanner.nextLine();
                        if((!headerLine.startsWith(headerIdentifier)) &&
                            headerLineNum <= 1000) {
                            headerLineNum++;
                            continue;
                        }
                    }

                    Map<String, Object> aspectValues = new HashMap<>();

                    // process only hapmap files.
                    // Ignore otherwise
                    if(StringUtils.isEmpty(headerLine) &&
                        headerLine.startsWith(headerIdentifier)) {

                        String[] headers = headerLine.split(FileUtils.TAB_SEP);

                        MatrixLoaderInstruction matrixLoaderInstruction = jsonMapper.convertValue(
                                loaderInstruction.getMatrix(),
                                MatrixLoaderInstruction.class);

                        // If instruction asks to load markers before loading
                        // datset markers and hdf5 files.
                        if(matrixLoaderInstruction.isLoadMarkers()) {
                        }

                    }
                    else {
                        continue;
                    }


                }
            }
            else {
                throw new GobiiException("Input file does not exist");
            }
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new GobiiException(e);
        }

        return new ArrayList<>();
    }

    private Map<String, Object> getHapMapAspectValuesMap(String[] fileColumns) {
        return new HashMap<>();
    }



}
