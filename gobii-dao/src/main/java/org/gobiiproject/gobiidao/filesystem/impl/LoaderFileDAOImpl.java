package org.gobiiproject.gobiidao.filesystem.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.LoaderFileDAO;
import org.gobiiproject.gobiimodel.ConfigSettings;
import org.gobiiproject.gobiimodel.dto.instructions.loader.Column;
import org.gobiiproject.gobiimodel.dto.instructions.loader.File;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.types.GobiiFileType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Phil on 4/12/2016.
 */
public class LoaderFileDAOImpl implements LoaderFileDAO {

    private final String LOADER_FILE_EXT = ".json";

    @Override
    public String writeInstructions(String fileUniqueId, List<GobiiLoaderInstruction> instructions) throws GobiiDaoException {

        String returnVal = null;

        String fileFQPN = null;
        String loaderFilePath = null;
        try {


            ConfigSettings configSettings = new ConfigSettings();

            loaderFilePath = configSettings.getCurrentCropConfig().getLoaderFilesLocation();

            if( null != loaderFilePath ) {

                Path path = Paths.get(loaderFilePath);
                if (Files.exists(path)) {

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    String dateTimeForFileName =
                            String.format("%02d", calendar.get(Calendar.YEAR)) +
                                    "-" +
                                    String.format("%02d", calendar.get(Calendar.MONTH) + 1) +
                                    "-" +
                                    String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) +
                                    "_" +
                                    String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) +
                                    "-" +
                                    String.format("%02d", calendar.get(Calendar.MINUTE)) +
                                    "-" +
                                    String.format("%02d", calendar.get(Calendar.SECOND));

                    fileFQPN = loaderFilePath + fileUniqueId + "_" + dateTimeForFileName + LOADER_FILE_EXT;

                    ObjectMapper objectMapper = new ObjectMapper();
                    String instructionsAsJson = objectMapper.writeValueAsString(instructions);
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileFQPN));
                    bufferedWriter.write(instructionsAsJson);
                    bufferedWriter.flush();
                    bufferedWriter.close();

                    returnVal = fileFQPN; // set this last in case we got an exception
                } else {
                    throw new GobiiDaoException("Instruction file path does not exist: " + loaderFilePath);
                } // if-else the path was found
            } else {
                throw new GobiiDaoException("The configuration does not specify a loader file destination" );
            } // if-else there is path in the configuration


        } catch (Exception e) {
            String message = e.getMessage() + "; fqpn: " + fileFQPN + "; configured path: " + loaderFilePath;
            throw new GobiiDaoException(message);
        }

        return returnVal;

    } // writeInstructions


    @Override
    public List<GobiiLoaderInstruction> getSampleInstructions() {

        List<GobiiLoaderInstruction> returnVal = new ArrayList<>();

        // **********************************************************************
        // INSTRUCTION ONE BEGIN
        GobiiLoaderInstruction gobiiLoaderInstructionOne = new GobiiLoaderInstruction();

        gobiiLoaderInstructionOne.setTable("foo_table");

        // column one
        Column columnOne = new Column();
        columnOne.setCCoord(1);
        columnOne.setRCoord((1));
        columnOne.setColumnType(Column.ColumnType.VCF_MARKER);
        columnOne.setFilterFrom(".*");
        columnOne.setFilterTo(".*");
        columnOne.setName("my_foo");

        // column two
        Column columnTwo = new Column();
        columnTwo.setCCoord(1);
        columnTwo.setRCoord(1);
        columnTwo.setColumnType(Column.ColumnType.CSV_COLUMN);
        columnTwo.setFilterFrom(".*");
        columnTwo.setFilterTo(".*");
        columnTwo.setName("my_bar");

        gobiiLoaderInstructionOne.getColumns().add(columnOne);
        gobiiLoaderInstructionOne.getColumns().add(columnTwo);

        // File Config
        gobiiLoaderInstructionOne.getFile().setDelimiter(",");
        gobiiLoaderInstructionOne.getFile().setSource("c:\\your-dir");
        gobiiLoaderInstructionOne.getFile().setDestination("c:\\mydir");
        gobiiLoaderInstructionOne.getFile().setGobiiFileType(GobiiFileType.VCF);

        // VCF Parameters
        gobiiLoaderInstructionOne.getVcfParameters().setMaf(1.1f);
        gobiiLoaderInstructionOne.getVcfParameters().setMinDp(1.1f);
        gobiiLoaderInstructionOne.getVcfParameters().setMinQ(1.1f);
        gobiiLoaderInstructionOne.getVcfParameters().setRemoveIndels(true);
        gobiiLoaderInstructionOne.getVcfParameters().setToIupac(true);

        returnVal.add(gobiiLoaderInstructionOne);
        // INSTRUCTION ONE END
        // **********************************************************************


        // **********************************************************************
        // INSTRUCTION TWO BEGIN
        GobiiLoaderInstruction gobiiLoaderInstructionTwo = new GobiiLoaderInstruction();

        gobiiLoaderInstructionTwo.setTable("bar_table");

        // column one
        columnOne = new Column();
        columnOne.setCCoord(1);
        columnOne.setRCoord(1);
        columnOne.setColumnType(Column.ColumnType.VCF_MARKER);
        columnOne.setFilterFrom(".*");
        columnOne.setFilterTo(".*");
        columnOne.setName("my_foobar");

        // column two
        columnTwo = new Column();
        columnTwo.setCCoord(1);
        columnTwo.setRCoord((1));
        columnTwo.setColumnType(Column.ColumnType.CSV_COLUMN);
        columnTwo.setFilterFrom(".*");
        columnTwo.setFilterTo(".*");
        columnTwo.setName("my_barfoo");

        gobiiLoaderInstructionTwo.getColumns().add(columnTwo);
        gobiiLoaderInstructionTwo.getColumns().add(columnTwo);

        // File Config
        gobiiLoaderInstructionTwo.getFile().setDelimiter(",");
        gobiiLoaderInstructionTwo.getFile().setSource("c:\\your-bar-dir");
        gobiiLoaderInstructionTwo.getFile().setDestination("c:\\mybardir");
        gobiiLoaderInstructionTwo.getFile().setGobiiFileType(GobiiFileType.VCF);

        // VCF Parameters
        gobiiLoaderInstructionTwo.getVcfParameters().setMaf(1.1f);
        gobiiLoaderInstructionTwo.getVcfParameters().setMinDp(1.1f);
        gobiiLoaderInstructionTwo.getVcfParameters().setMinQ(1.1f);
        gobiiLoaderInstructionTwo.getVcfParameters().setRemoveIndels(true);
        gobiiLoaderInstructionTwo.getVcfParameters().setToIupac(true);

        returnVal.add(gobiiLoaderInstructionTwo);
        // INSTRUCTION TWO END
        // **********************************************************************        

        return returnVal;

    } // getSampleInstructions()

} // LoaderFileDAOImpl
