package org.gobiiproject.gobiiprocess.digester.csv;

import org.gobiiproject.gobiimodel.dto.entity.children.PropNameId;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.types.DataSetType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for CSVFileReaderV2Test. Approach: A temporary folder is created
 * which contains all input file. Instruction file is created with required
 * fields. Using CSVFileReaderV2 input file is read and stored in output file
 * after processing. Generated o/p file is verified that it is as expected.
 */
public class CSVFileReader_CSV_BOTHV2Test {

    private static String tempFolderLocation, resourceDestFolderLocation, loaderScriptPath;

    @ClassRule
    public static TemporaryFolder tempFolder = new TemporaryFolder();

    /**
     * According to JUnit no exception is thrown when temp folder is not
     * deleted. This method ensures that temp folder is always deleted.
     */
    @AfterClass
    public static void checkAndCleanTempFile() {
        Util.deleteDirectory(new File(tempFolderLocation));
    }

    /**
     * Test case for multiple_CSV_ROW
     *
     * @throws IOException
     * @throws InterruptedException
     */

    @Test
    public void testCSV_BOTH_Dominant() throws IOException {
        File srcFolder;
        srcFolder = tempFolder.newFolder("dominant");
        tempFolder.newFolder("dest");

        tempFolderLocation = tempFolder.getRoot().getPath();
        File resourceDest = new File("src/test/resources/csvBoth");
        resourceDestFolderLocation = resourceDest.getAbsolutePath();
        loaderScriptPath = new File("src/test/resources/loaderScriptPath").getAbsolutePath();
        File resourceSource = new File("src/test/resources/csvBoth/Dominant.txt");
        File dest = new File(srcFolder.getAbsolutePath() + "\\Dominant.txt");
        Files.copy(resourceSource.toPath(), dest.toPath());

        String table = "CSV_BOTH_Dominant";
        GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
        Util.createAndSetGobiiFile(instruction, tempFolderLocation);
        instruction.getGobiiFile().setSource(tempFolderLocation + "/dominant");

        instruction.setTable(table);
        List<GobiiFileColumn> gobiiColumns = new ArrayList<>();
        gobiiColumns.add(Util.createGobiiCSV_BOTH(1, 1, DataSetType.DOMINANT_NON_NUCLEOTIDE));
        instruction.setGobiiFileColumns(gobiiColumns);
        instruction.setDatasetType(new PropNameId(99, "DOMINANT_NON_NUCLEOTIDE"));

        CSVFileReaderV2 csvReader = new CSVFileReaderV2(loaderScriptPath);
        csvReader.processCSV(instruction);

        Util.validateResult(tempFolderLocation, table, resourceDestFolderLocation);
        Util.deleteDirectory(srcFolder);
        Util.deleteDirectory(new File(tempFolder.getRoot().getAbsolutePath() + "/dest"));
    }

    @Test
    public void testCSV_BOTH_Dominant_Fail() throws IOException {
        File srcFolder;
        srcFolder = tempFolder.newFolder("dominant");
        tempFolder.newFolder("dest");
        tempFolderLocation = tempFolder.getRoot().getPath();
        File resourceDest = new File("src/test/resources/csvBoth");
        resourceDestFolderLocation = resourceDest.getAbsolutePath();
        loaderScriptPath = new File("src/test/resources/loaderScriptPath").getAbsolutePath();
        File resourceSource = new File("src/test/resources/csvBoth/Dominant_Fail.txt");
        File dest = new File(srcFolder.getAbsolutePath() + "\\Dominant.txt");
        Files.copy(resourceSource.toPath(), dest.toPath());

        String table = "CSV_BOTH_Dominant";
        GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
        Util.createAndSetGobiiFile(instruction, tempFolderLocation);
        instruction.getGobiiFile().setSource(tempFolderLocation + "/dominant");

        instruction.setTable(table);
        List<GobiiFileColumn> gobiiColumns = new ArrayList<>();
        gobiiColumns.add(Util.createGobiiCSV_BOTH(1, 1, DataSetType.DOMINANT_NON_NUCLEOTIDE));
        instruction.setGobiiFileColumns(gobiiColumns);
        instruction.setDatasetType(new PropNameId(99, "DOMINANT_NON_NUCLEOTIDE"));

        CSVFileReaderV2 csvReader = new CSVFileReaderV2(loaderScriptPath);
        csvReader.processCSV(instruction);

        Util.checkFileAbsence(table, tempFolderLocation);
        Util.deleteDirectory(srcFolder);
        Util.deleteDirectory(new File(tempFolder.getRoot().getAbsolutePath() + "/dest"));
    }

    @Test
    public void testCSV_BOTH_Co_Dominant() throws IOException{
        File srcFolder;
        srcFolder = tempFolder.newFolder("Codominant");
        tempFolder.newFolder("dest");

        tempFolderLocation = tempFolder.getRoot().getPath();
        File resourceDest = new File("src/test/resources/csvBoth");
        resourceDestFolderLocation = resourceDest.getAbsolutePath();
        loaderScriptPath = new File("src/test/resources/loaderScriptPath").getAbsolutePath();
        File resourceSource = new File("src/test/resources/csvBoth/Codominant.txt");
        File dest = new File(srcFolder.getAbsolutePath() + "\\Codominant.txt");
        Files.copy(resourceSource.toPath(), dest.toPath());

        String table = "CSV_BOTH_CoDominant";
        GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
        Util.createAndSetGobiiFile(instruction, tempFolderLocation);
        instruction.getGobiiFile().setSource(tempFolderLocation + "/Codominant");

        instruction.setTable(table);
        List<GobiiFileColumn> gobiiColumns = new ArrayList<>();
        gobiiColumns.add(Util.createGobiiCSV_BOTH(1, 1, DataSetType.CO_DOMINANT_NON_NUCLEOTIDE));
        instruction.setGobiiFileColumns(gobiiColumns);
        instruction.setDatasetType(new PropNameId(100, "CO_DOMINANT_NON_NUCLEOTIDE"));

        CSVFileReaderV2 csvReader = new CSVFileReaderV2(loaderScriptPath);
        csvReader.processCSV(instruction);

        Util.validateResult(tempFolderLocation, table, resourceDestFolderLocation);
        Util.deleteDirectory(srcFolder);
        Util.deleteDirectory(new File(tempFolder.getRoot().getAbsolutePath() + "/dest"));
    }

    @Test
    public void testCSV_BOTH_Co_Dominant_Fail() throws IOException{
        File srcFolder;
        srcFolder = tempFolder.newFolder("Codominant");
        tempFolder.newFolder("dest");

        tempFolderLocation = tempFolder.getRoot().getPath();
        File resourceDest = new File("src/test/resources/csvBoth");
        resourceDestFolderLocation = resourceDest.getAbsolutePath();
        loaderScriptPath = new File("src/test/resources/loaderScriptPath").getAbsolutePath();
        File resourceSource = new File("src/test/resources/csvBoth/Codominant_Fail.txt");
        File dest = new File(srcFolder.getAbsolutePath() + "\\Codominant.txt");
        Files.copy(resourceSource.toPath(), dest.toPath());

        String table = "CSV_BOTH_CoDominant";
        GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
        Util.createAndSetGobiiFile(instruction, tempFolderLocation);
        instruction.getGobiiFile().setSource(tempFolderLocation + "/Codominant");

        instruction.setTable(table);
        List<GobiiFileColumn> gobiiColumns = new ArrayList<>();
        gobiiColumns.add(Util.createGobiiCSV_BOTH(1, 1, DataSetType.CO_DOMINANT_NON_NUCLEOTIDE));
        instruction.setGobiiFileColumns(gobiiColumns);
        instruction.setDatasetType(new PropNameId(100, "CO_DOMINANT_NON_NUCLEOTIDE"));

        CSVFileReaderV2 csvReader = new CSVFileReaderV2(loaderScriptPath);
        csvReader.processCSV(instruction);

        Util.checkFileAbsence(table, tempFolderLocation);
        Util.deleteDirectory(srcFolder);
        Util.deleteDirectory(new File(tempFolder.getRoot().getAbsolutePath() + "/dest"));
    }

    @Test
    public void testCSV_BOTH_IUPAC() throws IOException{
        File srcFolder;
        srcFolder = tempFolder.newFolder("IUPAC");
        tempFolder.newFolder("dest");

        tempFolderLocation = tempFolder.getRoot().getPath();
        File resourceDest = new File("src/test/resources/csvBoth");
        resourceDestFolderLocation = resourceDest.getAbsolutePath();
        loaderScriptPath = new File("src/test/resources/loaderScriptPath").getAbsolutePath();
        File resourceSource = new File("src/test/resources/csvBoth/IUPAC.txt");
        File dest = new File(srcFolder.getAbsolutePath() + "\\IUPAC.txt");
        Files.copy(resourceSource.toPath(), dest.toPath());

        String table = "CSV_BOTH_IUPAC";
        GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
        Util.createAndSetGobiiFile(instruction, tempFolderLocation);
        instruction.getGobiiFile().setSource(tempFolderLocation + "/IUPAC");

        instruction.setTable(table);
        List<GobiiFileColumn> gobiiColumns = new ArrayList<>();
        gobiiColumns.add(Util.createGobiiCSV_BOTH(1, 11, DataSetType.IUPAC));
        instruction.setGobiiFileColumns(gobiiColumns);
        instruction.setDatasetType(new PropNameId(98, "IUPAC"));

        CSVFileReaderV2 csvReader = new CSVFileReaderV2(loaderScriptPath);
        csvReader.processCSV(instruction);

        Util.validateResult(tempFolderLocation, table, resourceDestFolderLocation);
        Util.deleteDirectory(srcFolder);
        Util.deleteDirectory(new File(tempFolder.getRoot().getAbsolutePath() + "/dest"));
    }

    @Test
    public void testCSV_BOTH_IUPAC_Fail() throws IOException{
        File srcFolder;
        srcFolder = tempFolder.newFolder("IUPAC");
        tempFolder.newFolder("dest");

        tempFolderLocation = tempFolder.getRoot().getPath();
        File resourceDest = new File("src/test/resources/csvBoth");
        resourceDestFolderLocation = resourceDest.getAbsolutePath();
        loaderScriptPath = new File("src/test/resources/loaderScriptPath").getAbsolutePath();
        File resourceSource = new File("src/test/resources/csvBoth/IUPAC_Fail.txt");
        File dest = new File(srcFolder.getAbsolutePath() + "\\IUPAC.txt");
        Files.copy(resourceSource.toPath(), dest.toPath());

        String table = "CSV_BOTH_IUPAC";
        GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
        Util.createAndSetGobiiFile(instruction, tempFolderLocation);
        instruction.getGobiiFile().setSource(tempFolderLocation + "/IUPAC");

        instruction.setTable(table);
        List<GobiiFileColumn> gobiiColumns = new ArrayList<>();
        gobiiColumns.add(Util.createGobiiCSV_BOTH(1, 11, DataSetType.IUPAC));
        instruction.setGobiiFileColumns(gobiiColumns);
        instruction.setDatasetType(new PropNameId(98, "IUPAC"));

        CSVFileReaderV2 csvReader = new CSVFileReaderV2(loaderScriptPath);
        csvReader.processCSV(instruction);

        Util.checkFileAbsence(table, tempFolderLocation);
        Util.deleteDirectory(srcFolder);
        Util.deleteDirectory(new File(tempFolder.getRoot().getAbsolutePath() + "/dest"));
    }

    @Test
    public void testCSV_BOTH_Two_Letter() throws IOException, InterruptedException {
        File srcFolder;
        srcFolder = tempFolder.newFolder("NUCLEOTIDE_2_LETTER");
        tempFolder.newFolder("dest");

        tempFolderLocation = tempFolder.getRoot().getPath();
        File resourceDest = new File("src/test/resources/csvBoth");
        resourceDestFolderLocation = resourceDest.getAbsolutePath();
        loaderScriptPath = new File("src/test/resources/loaderScriptPath").getAbsolutePath();
        File resourceSource = new File("src/test/resources/csvBoth/NUCLEOTIDE_2_LETTER.txt");
        File dest = new File(srcFolder.getAbsolutePath() + "\\NUCLEOTIDE_2_LETTER.txt");
        Files.copy(resourceSource.toPath(), dest.toPath());

        String table = "CSV_BOTH_NUCLEOTIDE_2_LETTER";
        GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
        Util.createAndSetGobiiFile(instruction, tempFolderLocation);
        instruction.getGobiiFile().setSource(tempFolderLocation + "/NUCLEOTIDE_2_LETTER");

        instruction.setTable(table);
        List<GobiiFileColumn> gobiiColumns = new ArrayList<>();
        gobiiColumns.add(Util.createGobiiCSV_BOTH(1, 1, DataSetType.NUCLEOTIDE_2_LETTER));
        instruction.setGobiiFileColumns(gobiiColumns);
        instruction.setDatasetType(new PropNameId(97, "NUCLEOTIDE_2_LETTER"));

        CSVFileReaderV2 csvReader = new CSVFileReaderV2(loaderScriptPath);
        csvReader.processCSV(instruction);

        Util.validateResult(tempFolderLocation, table, resourceDestFolderLocation);
        Util.deleteDirectory(srcFolder);
        Util.deleteDirectory(new File(tempFolder.getRoot().getAbsolutePath() + "/dest"));
    }

    @Test
    public void testCSV_BOTH_Two_Letter_Fail() throws IOException{
        File srcFolder;
        srcFolder = tempFolder.newFolder("NUCLEOTIDE_2_LETTER");
        tempFolder.newFolder("dest");

        tempFolderLocation = tempFolder.getRoot().getPath();
        File resourceDest = new File("src/test/resources/csvBoth");
        resourceDestFolderLocation = resourceDest.getAbsolutePath();
        loaderScriptPath = new File("src/test/resources/loaderScriptPath").getAbsolutePath();
        File resourceSource = new File("src/test/resources/csvBoth/NUCLEOTIDE_2_LETTER_Fail.txt");
        File dest = new File(srcFolder.getAbsolutePath() + "\\NUCLEOTIDE_2_LETTER.txt");
        Files.copy(resourceSource.toPath(), dest.toPath());

        String table = "CSV_BOTH_NUCLEOTIDE_2_LETTER";
        GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
        Util.createAndSetGobiiFile(instruction, tempFolderLocation);
        instruction.getGobiiFile().setSource(tempFolderLocation + "/NUCLEOTIDE_2_LETTER");

        instruction.setTable(table);
        List<GobiiFileColumn> gobiiColumns = new ArrayList<>();
        gobiiColumns.add(Util.createGobiiCSV_BOTH(1, 1, DataSetType.NUCLEOTIDE_2_LETTER));
        instruction.setGobiiFileColumns(gobiiColumns);
        instruction.setDatasetType(new PropNameId(97, "NUCLEOTIDE_2_LETTER"));

        CSVFileReaderV2 csvReader = new CSVFileReaderV2(loaderScriptPath);
        csvReader.processCSV(instruction);

        Util.checkFileAbsence(table, tempFolderLocation);
        Util.deleteDirectory(srcFolder);
        Util.deleteDirectory(new File(tempFolder.getRoot().getAbsolutePath() + "/dest"));
    }
}