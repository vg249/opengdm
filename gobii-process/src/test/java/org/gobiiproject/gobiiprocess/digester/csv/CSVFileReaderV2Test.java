package org.gobiiproject.gobiiprocess.digester.csv;

<<<<<<< HEAD
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.gobiiproject.gobiimodel.dto.entity.children.PropNameId;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderMetadata;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderProcedure;
import org.gobiiproject.gobiimodel.types.DataSetType;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

/**
 * Unit test for CSVFileReaderV2Test. Approach: A temporary folder is created
 * which contains all input file. Instruction file is created with required
 * fields. Using CSVFileReaderV2 input file is read and stored in output file
 * after processing. Generated o/p file is verified that it is as expected.
 */
@Ignore //TODO - fix for non-windows file endings
public class CSVFileReaderV2Test {

    private static String tempFolderLocation, resourceDestFolderLocation, loaderScriptPath;

    @ClassRule
    public static TemporaryFolder tempFolder = new TemporaryFolder();

    @BeforeClass
    public static void setUp() throws IOException {
        File srcFolder;
        srcFolder = tempFolder.newFolder("src");
        tempFolder.newFolder("dest");
        tempFolderLocation = tempFolder.getRoot().getPath();
        File resourceDest = new File("src/test/resources");
        resourceDestFolderLocation = resourceDest.getAbsolutePath();
        loaderScriptPath = new File("src/test/resources/loaderScriptPath").getAbsolutePath();
        File resourceSource = new File("src/test/resources/input.txt");
        File dest = new File(srcFolder.getAbsolutePath() + "\\input.txt");
        Files.copy(resourceSource.toPath(), dest.toPath());
    }

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
    public void testMultipleCSV_ROW() throws IOException {

		GobiiLoaderProcedure procedure = new GobiiLoaderProcedure();
		procedure.setMetadata(new GobiiLoaderMetadata());
		Util.createAndSetGobiiFile(procedure, tempFolderLocation);

        String table = "multipleCSV_ROW";
        GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
        instruction.setTable(table);
        List<GobiiFileColumn> gobiiColumns = new ArrayList<>();
        gobiiColumns.add(Util.createGobiiConstantColumn(Integer.toString(0)));
        gobiiColumns.add(Util.createGobiiAutoIncrementColumn());
        gobiiColumns.add(Util.createGobiiCSV_ROW(0, 0));
        gobiiColumns.add(Util.createGobiiCSV_ROW(1, 0));
        instruction.setGobiiFileColumns(gobiiColumns);

        CSVFileReaderV2 csvReader = new CSVFileReaderV2(loaderScriptPath);

        csvReader.processCSV(procedure, instruction);

        Util.validateResult(tempFolderLocation, table, resourceDestFolderLocation);
    }

    @Test
    public void testMultipleCSV_COL() throws IOException {

		GobiiLoaderProcedure procedure = new GobiiLoaderProcedure();
		procedure.setMetadata(new GobiiLoaderMetadata());
		Util.createAndSetGobiiFile(procedure, tempFolderLocation);

        String table = "multipleCSV_COL";
        GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
        instruction.setTable(table);
        List<GobiiFileColumn> gobiiColumns = new ArrayList<>();
        gobiiColumns.add(Util.createGobiiConstantColumn(Integer.toString(0)));
        gobiiColumns.add(Util.createGobiiAutoIncrementColumn());
        gobiiColumns.add(Util.createGobiiCSV_COL(0, 0));
        gobiiColumns.add(Util.createGobiiCSV_COL(0, 1));
        instruction.setGobiiFileColumns(gobiiColumns);

        CSVFileReaderV2 csvReader = new CSVFileReaderV2(loaderScriptPath);
        csvReader.processCSV(procedure, instruction);

        Util.validateResult(tempFolderLocation, table, resourceDestFolderLocation);
    }

    @Test
    public void testSubColumn() throws IOException {

		GobiiLoaderProcedure procedure = new GobiiLoaderProcedure();
		procedure.setMetadata(new GobiiLoaderMetadata());
		Util.createAndSetGobiiFile(procedure, tempFolderLocation);

        String table = "CSVSubColumn";
        GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
        instruction.setTable(table);
        List<GobiiFileColumn> gobiiColumns = new ArrayList<>();
        gobiiColumns.add(Util.createGobiiConstantColumn(Integer.toString(0)));
        gobiiColumns.add(Util.createGobiiAutoIncrementColumn());
        gobiiColumns.add(Util.createGobiiCSV_SUB(0, 0));
        instruction.setGobiiFileColumns(gobiiColumns);

        CSVFileReaderV2 csvReader = new CSVFileReaderV2(loaderScriptPath);
        csvReader.processCSV(procedure, instruction);

        Util.validateResult(tempFolderLocation, table, resourceDestFolderLocation);
    }

    @Test
    public void testMultipleFilesCSV_ROW() throws IOException {

		GobiiLoaderProcedure procedure = new GobiiLoaderProcedure();
		procedure.setMetadata(new GobiiLoaderMetadata());
		Util.createAndSetGobiiFile(procedure, tempFolderLocation);

        String table = "multiFileCSV_ROW";
        File file2 = new File(tempFolderLocation + "\\src" + "\\file2.txt");
        String data[] = {"marker_name	dnarunname_dom_1	dnarunname_dom_2	dnarunname_dom_3	dnarunname_dom_4",
                "dommarker1	1	0	1	1"};
        BufferedWriter srcFileWriter = new BufferedWriter(new FileWriter(file2));
        srcFileWriter.write(data[0] + "\n");
        srcFileWriter.write(data[1] + "\n");
        srcFileWriter.close();

        GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
        instruction.setTable(table);
        List<GobiiFileColumn> gobiiColumns = new ArrayList<>();
        gobiiColumns.add(Util.createGobiiConstantColumn(Integer.toString(0)));
        gobiiColumns.add(Util.createGobiiAutoIncrementColumn());
        gobiiColumns.add(Util.createGobiiCSV_ROW(0, 0));
        instruction.setGobiiFileColumns(gobiiColumns);

        CSVFileReaderV2 csvReader = new CSVFileReaderV2(loaderScriptPath);
        csvReader.processCSV(procedure, instruction);

        Util.validateResult(tempFolderLocation, table, resourceDestFolderLocation);
        file2.delete();
    }
=======
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFile;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.types.DataSetOrientationType;
import org.gobiiproject.gobiimodel.types.DataSetType;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
/**
 * Unit test for CSVFileReaderV2Test.
 */
public class CSVFileReaderV2Test{
		
	public  static String tempFolderLocation;

	 @Rule
	 public TemporaryFolder tempFolder = new TemporaryFolder();

	 @After
	 public static void checkAndCleanTempFile(){
		 System.out.println(tempFolderLocation);
	 }
	 @Test
	public void testMultipleCSV_ROW(){

		tempFolderLocation = tempFolder.getRoot().getPath();
		try {
			File srcFolder = tempFolder.newFolder("src");
			File destFolder = tempFolder.newFolder("dest"); 
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		GobiiLoaderInstruction instruction = new GobiiLoaderInstruction(); 
		createAndSetGobiiFile(instruction);
		instruction.setTable("table");
		List<GobiiFileColumn> gobiiColumns = new ArrayList<GobiiFileColumn>();
		gobiiColumns.add(createGobiiConstantColumn(Integer.toString(0)));
		gobiiColumns.add(createGobiiAutoIncreamentColumn());
		gobiiColumns.add(createGobiiCSV_ROW_Column(0, 0));
	//	createGobiiCSV_COL_Column();
	//	createGobiiCSV_BOTH_Column();
	//	createGobiiCSV_SUB_Column();
		instruction.setGobiiFileColumns(gobiiColumns);

		CSVFileReaderV2 csvReader = new CSVFileReaderV2("", "");
		try {
			csvReader.processCSV(instruction);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(tempFolder.toString());
		System.out.println(tempFolder.getRoot());
		System.out.println("Yellow");
	}
/*	
	@Test
	public void testMultipleCSV_COL(){
		System.out.println(tempFolderaaa.toString());
		System.out.println("Hello2");
	}
	
	@Test
	public void testCSV_BOTH(){
		System.out.println(tempFolderaaa.toString());
		System.out.println("Hello3");
	}
	
	@Test
	public void testMultipleFileCSV_ROW(){
		System.out.println(tempFolderaaa.toString());
		System.out.println("Hello4");
	}
	
	@Test
	public void testMultipleFileCSV_COL(){
		System.out.println(tempFolderaaa.toString());
		System.out.println("Hello5");
	}
	
	@Test
	public void testMultipleFileCSV_BOTH(){
		System.out.println(tempFolderaaa.toString());
		System.out.println("Hello6");
	}
	
	@Test
	public void testSubColumn(){
		System.out.println(tempFolderaaa.toString());
		System.out.println("Hello7");
	}

	public void testConstantField(){
		System.out.println(tempFolderaaa.toString());
		System.out.println("Hello8");
	} */
	 
	private void createGobiiCSV_SUB_Column() {
		// TODO Auto-generated method stub
		
	}
	private void createGobiiCSV_BOTH_Column() {
		// TODO Auto-generated method stub
		
	}
	private void createGobiiCSV_COL_Column() {
		// TODO Auto-generated method stub
		
	}
	private GobiiFileColumn createGobiiAutoIncreamentColumn() {
		GobiiFileColumn fileColumn = new GobiiFileColumn();
		fileColumn.setGobiiColumnType(GobiiColumnType.AUTOINCREMENT);
		fileColumn.setRCoord(null);
		fileColumn.setCCoord(null);
		fileColumn.setName("AutoIncrement");
		fileColumn.setFilterFrom(null);
		fileColumn.setFilterTo(null);
		fileColumn.setFindText(null);
		fileColumn.setReplaceText(null);
		fileColumn.setConstantValue(null);
		fileColumn.setIndex(null);
		fileColumn.setSubcolumn(false);
		fileColumn.setSubcolumnDelimiter(null);
		fileColumn.setDataSetType(DataSetType.IUPAC);
		fileColumn.setDataSetOrientationType(DataSetOrientationType.MARKER_FAST);
		fileColumn.setMetaDataId(null);
		return fileColumn;
		
	}
	private GobiiFileColumn createGobiiCSV_ROW_Column(Integer rCoord, Integer cCoord) {

		GobiiFileColumn fileColumn = new GobiiFileColumn();
		fileColumn.setGobiiColumnType(GobiiColumnType.CSV_ROW);
		fileColumn.setRCoord(rCoord);
		fileColumn.setCCoord(cCoord);
		fileColumn.setName("csvRow");
		fileColumn.setFilterFrom(null);
		fileColumn.setFilterTo(null);
		fileColumn.setFindText(null);
		fileColumn.setReplaceText(null);
		fileColumn.setConstantValue(null);
		fileColumn.setIndex(null);
		fileColumn.setSubcolumn(false);
		fileColumn.setSubcolumnDelimiter(null);
		fileColumn.setDataSetType(DataSetType.IUPAC);
		fileColumn.setDataSetOrientationType(DataSetOrientationType.MARKER_FAST);
		fileColumn.setMetaDataId(null);
		return fileColumn;
	}
	private GobiiFileColumn createGobiiConstantColumn(String constantValue) {
		
		GobiiFileColumn fileColumn = new GobiiFileColumn();
		fileColumn.setGobiiColumnType(GobiiColumnType.CONSTANT);
		fileColumn.setRCoord(null);
		fileColumn.setCCoord(null);
		fileColumn.setName("constantName");
		fileColumn.setFilterFrom(null);
		fileColumn.setFilterTo(null);
		fileColumn.setFindText(null);
		fileColumn.setReplaceText(null);
		fileColumn.setConstantValue(constantValue);
		fileColumn.setIndex(null);
		fileColumn.setSubcolumn(false);
		fileColumn.setSubcolumnDelimiter(null);
		fileColumn.setDataSetType(DataSetType.IUPAC);
		fileColumn.setDataSetOrientationType(DataSetOrientationType.MARKER_FAST);
		fileColumn.setMetaDataId(null);
		return fileColumn;
		
	}

	private void createAndSetGobiiColumns(GobiiLoaderInstruction instruction) {
		
	}
	/**
	 * Sets the gobiiFile in the instruction.
	 * This is common across all the test cases.
	 * Refer {@code org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFile.java}
	 * @param gobiiFile
	 */
	private void createAndSetGobiiFile(GobiiLoaderInstruction instruction) {
		GobiiFile gobiiFile = new GobiiFile();
		gobiiFile.setSource(tempFolderLocation + "/src");
		gobiiFile.setDestination(tempFolderLocation + "/dest");
		gobiiFile.setDelimiter("\\t");
		gobiiFile.setGobiiFileType(GobiiFileType.GENERIC);
		gobiiFile.setCreateSource(false);
		gobiiFile.setRequireDirectoriesToExist(false);
		instruction.setGobiiFile(gobiiFile);
	}
>>>>>>> e50928c18... EOD-21-4
}