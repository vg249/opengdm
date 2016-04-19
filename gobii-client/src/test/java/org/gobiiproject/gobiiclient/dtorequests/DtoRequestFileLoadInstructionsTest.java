// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.container.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.header.HeaderStatusMessage;
import org.gobiiproject.gobiimodel.dto.instructions.loader.Column;
import org.gobiiproject.gobiimodel.dto.instructions.loader.File;
import org.gobiiproject.gobiimodel.dto.instructions.loader.LoaderInstruction;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class DtoRequestFileLoadInstructionsTest {


    @Ignore
    public void testGetSampleInstructionFile() throws Exception {


        DtoRequestFileLoadInstructions dtoRequestFileLoadInstructions = new DtoRequestFileLoadInstructions();
        LoaderInstructionFilesDTO loaderInstructionFilesDTO = dtoRequestFileLoadInstructions.getSampleInstructionFile();


        Assert.assertNotEquals(null, dtoRequestFileLoadInstructions);
//        Assert.assertNotEquals(null, projectDTO.getProjectName());

    } // testGetMarkers()

    @Test
    public void testSendInstructionFile() throws Exception {


        LoaderInstructionFilesDTO loaderInstructionFilesDTOToSend = new LoaderInstructionFilesDTO();

        LoaderInstruction loaderInstructionOne = new LoaderInstruction();
        loaderInstructionOne.setTable("foo_table");

        // column one
        Column columnOne = new Column()
                .setCCoord(1)
                .setRCoord((1))
                .setColumnType(Column.ColumnType.VCF_MARKER)
                .setFilterFrom(".*")
                .setFilterTo(".*")
                .setName("my_foo");

        // column two
        Column columnTwo = new Column()
                .setCCoord(1)
                .setRCoord(1)
                .setColumnType(Column.ColumnType.CSV_COLUMN)
                .setFilterFrom(".*")
                .setFilterTo(".*")
                .setName("my_bar");

        loaderInstructionOne.getColumns().add(columnOne);
        loaderInstructionOne.getColumns().add(columnTwo);

        // File Config
        loaderInstructionOne.getFile().setDelimiter(",")
                .setSource("c:\\your-dir")
                .setDestination("c:\\mydir")
                .setFileType(File.FileType.VCF);

        // VCF Parameters
        loaderInstructionOne.getVcfParameters()
                .setMaf(1.1f)
                .setMinDp(1.1f)
                .setMinQ(1.1f)
                .setRemoveIndels(true)
                .setToIupac(true);

        loaderInstructionFilesDTOToSend
                .getLoaderInstructions()
                .add(loaderInstructionOne);


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

        loaderInstructionFilesDTOToSend.getLoaderInstructions().add(loaderInstructionTwo);
        loaderInstructionFilesDTOToSend.setUserName("foo_user");


        DtoRequestFileLoadInstructions dtoRequestFileLoadInstructions = new DtoRequestFileLoadInstructions();
        LoaderInstructionFilesDTO loaderInstructionFilesDTOResponse = dtoRequestFileLoadInstructions.sendInstructionFile(loaderInstructionFilesDTOToSend);


        Assert.assertNotEquals(null, loaderInstructionFilesDTOResponse);

        TestUtils.checkAndPrintHeaderMessages(loaderInstructionFilesDTOResponse);

        Assert.assertTrue(loaderInstructionFilesDTOResponse.getDtoHeaderResponse().isSucceeded());
        Assert.assertNotEquals(null, loaderInstructionFilesDTOResponse.getOutputFileId());

    } // testGetMarkers()


}
