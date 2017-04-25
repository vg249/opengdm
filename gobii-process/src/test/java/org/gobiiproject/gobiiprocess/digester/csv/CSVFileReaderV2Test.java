package org.gobiiproject.gobiiprocess.digester.csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Unit test for CSVFileReaderV2Test. Approach: A temporary folder is created
 * which contains all input file. Instruction file is created with required
 * fields. Using CSVFileReaderV2 input file is read and stored in output file
 * after processing. Generated o/p file is verified that it is as expected.
 */
public class CSVFileReaderV2Test {

	public static String tempFolderLocation;

	@ClassRule
	public static TemporaryFolder tempFolder = new TemporaryFolder();

	@BeforeClass
	public static void setUp() throws IOException {
		File srcFolder;
		srcFolder = tempFolder.newFolder("src");
		tempFolder.newFolder("dest");
		tempFolderLocation = tempFolder.getRoot().getPath();
		File resourcesDirectory = new File("src/test/resources/input.txt");
		File source = new File(resourcesDirectory.getAbsolutePath());
		File dest= new File(srcFolder.getAbsolutePath()+"\\input.txt"); 
		Files.copy(source.toPath(), dest.toPath());
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
	public void testMultipleCSV_ROW() throws IOException, InterruptedException {

		String table = "multipleCSV_ROW";
		GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
		Util.createAndSetGobiiFile(instruction, tempFolderLocation);
		instruction.setTable(table);
		List<GobiiFileColumn> gobiiColumns = new ArrayList<GobiiFileColumn>();
		gobiiColumns.add(Util.createGobiiConstantColumn(Integer.toString(0)));
		gobiiColumns.add(Util.createGobiiAutoIncreamentColumn());
		gobiiColumns.add(Util.createGobiiCSV_ROW_Column(0, 0));
		gobiiColumns.add(Util.createGobiiCSV_ROW_Column(1, 0));
		instruction.setGobiiFileColumns(gobiiColumns);

		CSVFileReaderV2 csvReader = new CSVFileReaderV2("", "");
		csvReader.processCSV(instruction);

		String output[] = { "constantName	AutoIncrement	csvRow	csvRow", 
							"0	0	marker_name	dommarker1",
							"0	1	dnarunname_dom_1	1", 
							"0	2	dnarunname_dom_2	0", 
							"0	3	dnarunname_dom_3	1",
							"0	4	dnarunname_dom_4	1"};
		Util.validateResult(tempFolderLocation, table, output);
	}

	@Test
	public void testMultipleCSV_COL() throws IOException, InterruptedException {
		String table = "multipleCSV_COL";
		GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
		Util.createAndSetGobiiFile(instruction, tempFolderLocation);
		instruction.setTable(table);
		List<GobiiFileColumn> gobiiColumns = new ArrayList<GobiiFileColumn>();
		gobiiColumns.add(Util.createGobiiConstantColumn(Integer.toString(0)));
		gobiiColumns.add(Util.createGobiiAutoIncreamentColumn());
		gobiiColumns.add(Util.createGobiiCSV_COL_Column(0, 0));
		gobiiColumns.add(Util.createGobiiCSV_COL_Column(0, 1));
		instruction.setGobiiFileColumns(gobiiColumns);

		CSVFileReaderV2 csvReader = new CSVFileReaderV2("", "");
		csvReader.processCSV(instruction);

		String output[] = { "constantName	AutoIncrement	csvCol	csvCol", 
							"0	0	marker_name	dnarunname_dom_1",
							"0	1	dommarker1	1", 
							"0	2	dommarker2	1", 
							"0	3	dommarker3	1", 
							"0	4	dommarker4	1",
							"0	5	dommarker5	1", 
							"0	6	dommarker6	1", 
							"0	7	dommarker7	1", 
							"0	8	dommarker8	1" };
		Util.validateResult(tempFolderLocation, table, output);
	}

	@Test
	public void testCSV_BOTH() throws IOException, InterruptedException {
		String table = "multipleCSV_BOTH";
		GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
		Util.createAndSetGobiiFile(instruction, tempFolderLocation);
		instruction.setTable(table);
		List<GobiiFileColumn> gobiiColumns = new ArrayList<GobiiFileColumn>();
		gobiiColumns.add(Util.createGobiiCSV_BOTH_Column(0, 0));
		instruction.setGobiiFileColumns(gobiiColumns);

		CSVFileReaderV2 csvReader = new CSVFileReaderV2("", "");
		csvReader.processCSV(instruction);

		String output[] = { "csvBoth",
							"marker_name	dnarunname_dom_1	dnarunname_dom_2	dnarunname_dom_3	dnarunname_dom_4",
							"dommarker1	1	0	1	1", 
							"dommarker2	1	0	1	0", 
							"dommarker3	1	0	1	0",
							"dommarker4	1	0	1	1", 
							"dommarker5	1	0	1	1", 
							"dommarker6	1	0	1	0",
							"dommarker7	1	0	1	1", 
							"dommarker8	1	0	1	1"};
		Util.validateResult(tempFolderLocation, table, output);
	}

	@Test
	public void testSubColumn() throws InterruptedException, IOException {
		String table = "multipleCSV_ROW";
		GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
		Util.createAndSetGobiiFile(instruction, tempFolderLocation);
		instruction.setTable(table);
		List<GobiiFileColumn> gobiiColumns = new ArrayList<GobiiFileColumn>();
		gobiiColumns.add(Util.createGobiiConstantColumn(Integer.toString(0)));
		gobiiColumns.add(Util.createGobiiAutoIncreamentColumn());
		gobiiColumns.add(Util.createGobiiCSV_SUB_Column(0, 0));
		instruction.setGobiiFileColumns(gobiiColumns);

		CSVFileReaderV2 csvReader = new CSVFileReaderV2("", "");
		csvReader.processCSV(instruction);

		String output[] = { "constantName	AutoIncrement", 
							"0	0marker_name", 
							"0	1dnarunname_dom_1",
							"0	2dnarunname_dom_2", 
							"0	3dnarunname_dom_3", 
							"0	4dnarunname_dom_4" };
		Util.validateResult(tempFolderLocation, table, output);
	}

	@Test
	public void testMultipleFilesCSV_ROW() throws IOException, InterruptedException {
		String table = "multipleCSV_ROW";
		File file2 = new File(tempFolderLocation + "\\src" + "\\file2.txt");
		;
		String data[] = { "marker_name	dnarunname_dom_1	dnarunname_dom_2	dnarunname_dom_3	dnarunname_dom_4",
						  "dommarker1	1	0	1	1" };
		BufferedWriter srcFileWriter = new BufferedWriter(new FileWriter(file2));
		srcFileWriter.write(data[0] + "\n");
		srcFileWriter.write(data[1] + "\n");
		srcFileWriter.close();

		GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
		Util.createAndSetGobiiFile(instruction, tempFolderLocation);
		instruction.setTable(table);
		List<GobiiFileColumn> gobiiColumns = new ArrayList<GobiiFileColumn>();
		gobiiColumns.add(Util.createGobiiConstantColumn(Integer.toString(0)));
		gobiiColumns.add(Util.createGobiiAutoIncreamentColumn());
		gobiiColumns.add(Util.createGobiiCSV_ROW_Column(0, 0));
		instruction.setGobiiFileColumns(gobiiColumns);

		CSVFileReaderV2 csvReader = new CSVFileReaderV2("", "");
		csvReader.processCSV(instruction);

		String output[] = { "constantName	AutoIncrement	csvRow", 
							"0	0	marker_name", 
							"0	1	dnarunname_dom_1",
							"0	2	dnarunname_dom_2", 
							"0	3	dnarunname_dom_3", 
							"0	4	dnarunname_dom_4",
							"0	5	marker_name", 
							"0	6	dnarunname_dom_1", 
							"0	7	dnarunname_dom_2",
							"0	8	dnarunname_dom_3", 
							"0	9	dnarunname_dom_4" };
		Util.validateResult(tempFolderLocation, table, output);
		file2.delete();
	}

}