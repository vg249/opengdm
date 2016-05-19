// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.types.DataSetType;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;
import org.gobiiproject.gobiimodel.types.GobiiCropType;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

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


        ClientContext.getInstance().setCurrentClientCrop(GobiiCropType.RICE);

        String instructionFileName = "testapp_" + DateUtils.makeDateIdString();

        String intermediateFilesDirectory = ClientContext
                .getInstance()
                .getCurrentClientCropConfig()
                .getIntermediateFilesDirectory();


        String rawUserFilesDirectory = ClientContext
                .getInstance()
                .getCurrentClientCropConfig()
                .getRawUserFilesDirectory();

        loaderInstructionFilesDTOToSend.setInstructionFileName(instructionFileName);
        loaderInstructionFilesDTOToSend.setRawUserFilesDirectory(rawUserFilesDirectory);
        loaderInstructionFilesDTOToSend.setIntermediateFilesDirectory(intermediateFilesDirectory);

        String instructionOneTableName = "foo_table";

        GobiiLoaderInstruction gobiiLoaderInstructionOne = new GobiiLoaderInstruction();
        gobiiLoaderInstructionOne.setTable(instructionOneTableName);

        // column one
        String instructionOneColumnOneName = "my_foo_column";
        DataSetType dataSetTypeTableOneColumnOne = DataSetType.IUPAC;
        String findTextTableOneColumnOne = "foo-replace-me";
        String replaceTextTextTableOneColumnOne = "bar-replace-me";
        GobiiFileColumn gobiiFileColumnOne = new GobiiFileColumn()
                .setCCoord(1)
                .setRCoord((1))
                .setGobiiColumnType(GobiiColumnType.VCF_MARKER)
                .setFilterFrom(".*")
                .setFilterTo(".*")
                .setName(instructionOneColumnOneName)
                .setFindText(findTextTableOneColumnOne)
                .setReplaceText(replaceTextTextTableOneColumnOne)
                .setDataSetType(dataSetTypeTableOneColumnOne);

        // column two
        GobiiFileColumn gobiiFileColumnTwo = new GobiiFileColumn()
                .setCCoord(1)
                .setRCoord(1)
                .setGobiiColumnType(GobiiColumnType.CSV_COLUMN)
                .setFilterFrom(".*")
                .setFilterTo(".*")
                .setName("my_bar");

        gobiiLoaderInstructionOne.getGobiiFileColumns().add(gobiiFileColumnOne);
        gobiiLoaderInstructionOne.getGobiiFileColumns().add(gobiiFileColumnTwo);

        // GobiiFile Config
        gobiiLoaderInstructionOne.getGobiiFile().setDelimiter(",")
                .setSource("c://your-dir")
                .setDestination("c://mydir")
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
        gobiiFileColumnOne = new GobiiFileColumn();
        gobiiFileColumnOne.setCCoord(1);
        gobiiFileColumnOne.setRCoord(1);
        gobiiFileColumnOne.setGobiiColumnType(GobiiColumnType.VCF_MARKER);
        gobiiFileColumnOne.setFilterFrom(".*");
        gobiiFileColumnOne.setFilterTo(".*");
        gobiiFileColumnOne.setName("my_foobar");

        // column two
        gobiiFileColumnTwo = new GobiiFileColumn();
        gobiiFileColumnTwo.setCCoord(1);
        gobiiFileColumnTwo.setRCoord((1));
        gobiiFileColumnTwo.setGobiiColumnType(GobiiColumnType.CSV_COLUMN);
        gobiiFileColumnTwo.setFilterFrom(".*");
        gobiiFileColumnTwo.setFilterTo(".*");
        gobiiFileColumnTwo.setName("my_barfoo");

        gobiiLoaderInstructionTwo.getGobiiFileColumns().add(gobiiFileColumnTwo);
        gobiiLoaderInstructionTwo.getGobiiFileColumns().add(gobiiFileColumnTwo);

        // GobiiFile Config
        gobiiLoaderInstructionTwo.getGobiiFile().setDelimiter(",");
        gobiiLoaderInstructionTwo.getGobiiFile().setSource("c://your-bar-dir");
        gobiiLoaderInstructionTwo.getGobiiFile().setDestination("c://mybardir");
        gobiiLoaderInstructionTwo.getGobiiFile().setGobiiFileType(GobiiFileType.VCF);

        // VCF Parameters
        gobiiLoaderInstructionTwo.getVcfParameters().setMaf(1.1f);
        gobiiLoaderInstructionTwo.getVcfParameters().setMinDp(1.1f);
        gobiiLoaderInstructionTwo.getVcfParameters().setMinQ(1.1f);
        gobiiLoaderInstructionTwo.getVcfParameters().setRemoveIndels(true);
        gobiiLoaderInstructionTwo.getVcfParameters().setToIupac(true);

        loaderInstructionFilesDTOToSend.getGobiiLoaderInstructions().add(gobiiLoaderInstructionTwo);


        DtoRequestFileLoadInstructions dtoRequestFileLoadInstructions = new DtoRequestFileLoadInstructions();
        LoaderInstructionFilesDTO loaderInstructionFilesDTOResponse = dtoRequestFileLoadInstructions.process(loaderInstructionFilesDTOToSend);


        Assert.assertNotEquals(null, loaderInstructionFilesDTOResponse);

        TestUtils.checkAndPrintHeaderMessages(loaderInstructionFilesDTOResponse);

        LoaderInstructionFilesDTO loaderInstructionFilesDTOretrieve = new LoaderInstructionFilesDTO();
        loaderInstructionFilesDTOretrieve.setProcessType(DtoMetaData.ProcessType.READ);
        loaderInstructionFilesDTOretrieve
                .setInstructionFileName(loaderInstructionFilesDTOResponse.getInstructionFileName());
        LoaderInstructionFilesDTO loaderInstructionFilesDTOretrieveResponse
                = dtoRequestFileLoadInstructions.process(loaderInstructionFilesDTOretrieve);

        TestUtils.checkAndPrintHeaderMessages(loaderInstructionFilesDTOretrieveResponse);

        Assert.assertTrue(
                2 == loaderInstructionFilesDTOretrieveResponse
                        .getGobiiLoaderInstructions()
                        .size()
        );

        Assert.assertTrue(
                loaderInstructionFilesDTOretrieveResponse
                        .getGobiiLoaderInstructions()
                        .get(0)
                        .getGobiiFileColumns()
                        .get(0)
                        .getName().equals(instructionOneColumnOneName)
        );


    } // testGetMarkers()
}
