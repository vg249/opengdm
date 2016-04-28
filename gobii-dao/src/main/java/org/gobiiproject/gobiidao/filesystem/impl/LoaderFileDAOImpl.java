package org.gobiiproject.gobiidao.filesystem.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.filesystem.LoaderFileDAO;
import org.gobiiproject.gobiimodel.ConfigurationSettings;
import org.gobiiproject.gobiimodel.dto.instructions.loader.Column;
import org.gobiiproject.gobiimodel.dto.instructions.loader.File;
import org.gobiiproject.gobiimodel.dto.instructions.loader.LoaderInstruction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
    public String writeInstructions(String fileUniqueId, List<LoaderInstruction> instructions) throws GobiiDaoException {

        String returnVal = null;

        try {


            ConfigurationSettings configurationSettings = new ConfigurationSettings();

            String laoderFilePath = configurationSettings.getPropValue("loaderfilepath");
//            if (! laoderFilePath.substring(laoderFilePath.length() - 1).equals("\\")) {
//                laoderFilePath += "\\";
//            }

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

            String fileFQPN = laoderFilePath + fileUniqueId + "_" + dateTimeForFileName + LOADER_FILE_EXT;

            ObjectMapper objectMapper = new ObjectMapper();
            String instructionsAsJson = objectMapper.writeValueAsString(instructions);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileFQPN));
            bufferedWriter.write(instructionsAsJson);
            bufferedWriter.flush();
            bufferedWriter.close();

            returnVal = fileFQPN; // set this last in case we got an exception


        } catch (IOException e) {
            throw new GobiiDaoException(e);
        }

        return returnVal;

    } // writeInstructions


    @Override
    public List<LoaderInstruction> getSampleInstructions() {

        List<LoaderInstruction> returnVal = new ArrayList<>();

        // **********************************************************************
        // INSTRUCTION ONE BEGIN
        LoaderInstruction loaderInstructionOne = new LoaderInstruction();

        loaderInstructionOne.setTable("foo_table");

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

        loaderInstructionOne.getColumns().add(columnOne);
        loaderInstructionOne.getColumns().add(columnTwo);

        // File Config
        loaderInstructionOne.getFile().setDelimiter(",");
        loaderInstructionOne.getFile().setSource("c:\\your-dir");
        loaderInstructionOne.getFile().setDestination("c:\\mydir");
        loaderInstructionOne.getFile().setFileType(File.FileType.VCF);

        // VCF Parameters
        loaderInstructionOne.getVcfParameters().setMaf(1.1f);
        loaderInstructionOne.getVcfParameters().setMinDp(1.1f);
        loaderInstructionOne.getVcfParameters().setMinQ(1.1f);
        loaderInstructionOne.getVcfParameters().setRemoveIndels(true);
        loaderInstructionOne.getVcfParameters().setToIupac(true);

        returnVal.add(loaderInstructionOne);
        // INSTRUCTION ONE END
        // **********************************************************************


        // **********************************************************************
        // INSTRUCTION TWO BEGIN
        LoaderInstruction loaderInstructionTwo = new LoaderInstruction();

        loaderInstructionTwo.setTable("bar_table");

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

        loaderInstructionTwo.getColumns().add(columnTwo);
        loaderInstructionTwo.getColumns().add(columnTwo);

        // File Config
        loaderInstructionTwo.getFile().setDelimiter(",");
        loaderInstructionTwo.getFile().setSource("c:\\your-bar-dir");
        loaderInstructionTwo.getFile().setDestination("c:\\mybardir");
        loaderInstructionTwo.getFile().setFileType(File.FileType.VCF);

        // VCF Parameters
        loaderInstructionTwo.getVcfParameters().setMaf(1.1f);
        loaderInstructionTwo.getVcfParameters().setMinDp(1.1f);
        loaderInstructionTwo.getVcfParameters().setMinQ(1.1f);
        loaderInstructionTwo.getVcfParameters().setRemoveIndels(true);
        loaderInstructionTwo.getVcfParameters().setToIupac(true);

        returnVal.add(loaderInstructionTwo);
        // INSTRUCTION TWO END
        // **********************************************************************        

        return returnVal;

    } // getSampleInstructions()

} // LoaderFileDAOImpl
