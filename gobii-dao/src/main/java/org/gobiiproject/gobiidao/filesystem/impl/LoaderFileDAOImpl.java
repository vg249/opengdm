package org.gobiiproject.gobiidao.filesystem.impl;

import org.gobiiproject.gobiidao.filesystem.LoaderFileDAO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.Column;
import org.gobiiproject.gobiimodel.dto.instructions.loader.File;
import org.gobiiproject.gobiimodel.dto.instructions.loader.LoaderInstruction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 4/12/2016.
 */
public class LoaderFileDAOImpl implements LoaderFileDAO {

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
        columnOne.setrCoord((1));
        columnOne.setColumnType(Column.ColumnType.VCF_MARKER);
        columnOne.setFilterFrom(".*");
        columnOne.setFilterTo(".*");
        columnOne.setName("my_foo");

        // column two
        Column columnTwo = new Column();
        columnTwo.setCCoord(1);
        columnTwo.setrCoord(1);
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
        columnOne.setrCoord(1);
        columnOne.setColumnType(Column.ColumnType.VCF_MARKER);
        columnOne.setFilterFrom(".*");
        columnOne.setFilterTo(".*");
        columnOne.setName("my_foobar");

        // column two
        columnTwo = new Column();
        columnTwo.setCCoord(1);
        columnTwo.setrCoord((1));
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

        return  returnVal;

    } // getSampleInstructions()

} // LoaderFileDAOImpl
