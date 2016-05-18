// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.container.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.Column;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DtoRequestGobiiFileLoadInstructionsTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }



    @Test
    public void testSendInstructionFile() throws Exception {


        LoaderInstructionFilesDTO loaderInstructionFilesDTOToSend = new LoaderInstructionFilesDTO();

        GobiiLoaderInstruction gobiiLoaderInstructionOne = new GobiiLoaderInstruction();
        gobiiLoaderInstructionOne.setTable("foo_table");

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

        gobiiLoaderInstructionOne.getColumns().add(columnOne);
        gobiiLoaderInstructionOne.getColumns().add(columnTwo);

        // GobiiFile Config
        gobiiLoaderInstructionOne.getGobiiFile().setDelimiter(",")
                .setSource("c:\\your-dir")
                .setDestination("c:\\mydir")
                .setGobiiFileType(GobiiFileType.VCF);

        // VCF Parameters
        gobiiLoaderInstructionOne.getVcfParameters()
                .setMaf(1.1f)
                .setMinDp(1.1f)
                .setMinQ(1.1f)
                .setRemoveIndels(true)
                .setToIupac(true);

        loaderInstructionFilesDTOToSend
                .getGobiiLoaderInstructions()
                .add(gobiiLoaderInstructionOne);


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

        // GobiiFile Config
        gobiiLoaderInstructionTwo.getGobiiFile().setDelimiter(",");
        gobiiLoaderInstructionTwo.getGobiiFile().setSource("c:\\your-bar-dir");
        gobiiLoaderInstructionTwo.getGobiiFile().setDestination("c:\\mybardir");
        gobiiLoaderInstructionTwo.getGobiiFile().setGobiiFileType(GobiiFileType.VCF);

        // VCF Parameters
        gobiiLoaderInstructionTwo.getVcfParameters().setMaf(1.1f);
        gobiiLoaderInstructionTwo.getVcfParameters().setMinDp(1.1f);
        gobiiLoaderInstructionTwo.getVcfParameters().setMinQ(1.1f);
        gobiiLoaderInstructionTwo.getVcfParameters().setRemoveIndels(true);
        gobiiLoaderInstructionTwo.getVcfParameters().setToIupac(true);

        loaderInstructionFilesDTOToSend.getGobiiLoaderInstructions().add(gobiiLoaderInstructionTwo);
        loaderInstructionFilesDTOToSend.setUserName("foo_user");


        DtoRequestFileLoadInstructions dtoRequestFileLoadInstructions = new DtoRequestFileLoadInstructions();
        LoaderInstructionFilesDTO loaderInstructionFilesDTOResponse = dtoRequestFileLoadInstructions.process(loaderInstructionFilesDTOToSend);


        Assert.assertNotEquals(null, loaderInstructionFilesDTOResponse);

        TestUtils.checkAndPrintHeaderMessages(loaderInstructionFilesDTOResponse);

        Assert.assertTrue(loaderInstructionFilesDTOResponse.getDtoHeaderResponse().isSucceeded());
        Assert.assertNotEquals(null, loaderInstructionFilesDTOResponse.getOutputFileId());

    } // testGetMarkers()


}
