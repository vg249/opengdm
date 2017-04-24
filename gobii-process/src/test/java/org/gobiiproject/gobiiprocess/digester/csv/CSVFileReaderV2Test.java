package org.gobiiproject.gobiiprocess.digester.csv;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.annotations.VisibleForTesting;

/**
 * Unit test for CSVFileReaderV2Test.
 * Approach: A temporary folder is created which contains all input file. 
 * Instruction file is created with required fields.
 * Using CSVFileReaderV2 input file is read and stored in output file after processing.
 * Generated o/p file is verified that it is as expected.
 */
public class CSVFileReaderV2Test {

	public static String tempFolderLocation;

	@ClassRule
	public static TemporaryFolder tempFolder = new TemporaryFolder();

	@BeforeClass
	public static void setUp() {
		File srcFolder;
		try {
			String data[] = {
					"marker_name	dnarunname_dom_1	dnarunname_dom_2	dnarunname_dom_3	dnarunname_dom_4",
					"dommarker1	1	0	1	1",
					"dommarker2	1	0	1	0",
					"dommarker3	1	0	1	0",
					"dommarker4	1	0	1	1",
					"dommarker5	1	0	1	1",
					"dommarker6	1	0	1	0",
					"dommarker7	1	0	1	1",
					"dommarker8	1	0	1	1" };
			srcFolder = tempFolder.newFolder("src");
			tempFolder.newFolder("dest");
			BufferedWriter srcFileWriter = new BufferedWriter(
					new FileWriter(new File(srcFolder.getPath() + "\\file1.txt")));
			srcFileWriter.write(data[0] +"\n");
			srcFileWriter.write(data[1] +"\n");
			srcFileWriter.write(data[2] +"\n");
			srcFileWriter.write(data[3] +"\n");
			srcFileWriter.write(data[4] +"\n");
			srcFileWriter.write(data[5] +"\n");
			srcFileWriter.write(data[6] +"\n");
			srcFileWriter.write(data[7] +"\n");
			srcFileWriter.write(data[8] +"\n");
			srcFileWriter.close();
		} catch (Exception e) {
		}
		tempFolderLocation = tempFolder.getRoot().getPath();
	}

	/**
	 * According to JUnit no exception is thrown when temp folder is not deleted.
	 * This method ensures that temp folder is always deleted.
	 */
	@AfterClass
	public static void checkAndCleanTempFile() {
		deleteDirectory(new File(tempFolderLocation));
	}

	/**
	 * Test case for multiple_CSV_ROW
	 */
	@Test
	public void testMultipleCSV_ROW() {

		GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
		createAndSetGobiiFile(instruction);
		instruction.setTable("multipleCSV_ROW");
		List<GobiiFileColumn> gobiiColumns = new ArrayList<GobiiFileColumn>();
		gobiiColumns.add(createGobiiConstantColumn(Integer.toString(0)));
		gobiiColumns.add(createGobiiAutoIncreamentColumn());
		gobiiColumns.add(createGobiiCSV_ROW_Column(0, 0));
		gobiiColumns.add(createGobiiCSV_ROW_Column(1, 0));
		instruction.setGobiiFileColumns(gobiiColumns);

		CSVFileReaderV2 csvReader = new CSVFileReaderV2("", "");
		try {
			csvReader.processCSV(instruction);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		String output[] = {"constantName	AutoIncrement	csvRow	csvRow",
						   "0	0	marker_name	dommarker1",
						   "0	1	dnarunname_dom_1	1",
						   "0	2	dnarunname_dom_2	0",
						   "0	3	dnarunname_dom_3	1",
						   "0	4	dnarunname_dom_4	1"};
		
		try (BufferedReader br = new BufferedReader(
				new FileReader(tempFolderLocation + "\\dest" + "\\digest.multipleCSV_ROW"))) {
			String fileRow; int i = 0;
			while ((fileRow = br.readLine()) != null) {
				assertEquals(output[i], fileRow);
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	 @Test 
	 public void testMultipleCSV_COL(){

			GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
			createAndSetGobiiFile(instruction);
			instruction.setTable("multipleCSV_COL");
			List<GobiiFileColumn> gobiiColumns = new ArrayList<GobiiFileColumn>();
			gobiiColumns.add(createGobiiConstantColumn(Integer.toString(0)));
			gobiiColumns.add(createGobiiAutoIncreamentColumn());
			gobiiColumns.add(createGobiiCSV_COL_Column(0, 0));
			gobiiColumns.add(createGobiiCSV_COL_Column(0, 1));
			instruction.setGobiiFileColumns(gobiiColumns);

			CSVFileReaderV2 csvReader = new CSVFileReaderV2("", "");
			try {
				csvReader.processCSV(instruction);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			String output[] = {"constantName	AutoIncrement	csvCol	csvCol",
							   "0	0	marker_name	dnarunname_dom_1",
							   "0	1	dommarker1	1",
							   "0	2	dommarker2	1",
							   "0	3	dommarker3	1",
							   "0	4	dommarker4	1",
							   "0	5	dommarker5	1",
							   "0	6	dommarker6	1",
							   "0	7	dommarker7	1",
							   "0	8	dommarker8	1"};
			
			try (BufferedReader br = new BufferedReader(
					new FileReader(tempFolderLocation + "\\dest" + "\\digest.multipleCSV_COL"))) {
				String fileRow; int i = 0;
				while ((fileRow = br.readLine()) != null) {
					assertEquals(output[i], fileRow);
					i++;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	 }
	 
	 @Test 
	 public void testCSV_BOTH(){
		 GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
			createAndSetGobiiFile(instruction);
			instruction.setTable("multipleCSV_BOTH");
			List<GobiiFileColumn> gobiiColumns = new ArrayList<GobiiFileColumn>();
			gobiiColumns.add(createGobiiCSV_BOTH_Column(0, 0));
			instruction.setGobiiFileColumns(gobiiColumns);

			CSVFileReaderV2 csvReader = new CSVFileReaderV2("", "");
			try {
				csvReader.processCSV(instruction);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			String output[] = {"csvBoth",
							   "marker_name	dnarunname_dom_1	dnarunname_dom_2	dnarunname_dom_3	dnarunname_dom_4",
							   "dommarker1	1	0	1	1",
							   "dommarker2	1	0	1	0",
							   "dommarker3	1	0	1	0",
							   "dommarker4	1	0	1	1",
							   "dommarker5	1	0	1	1",
							   "dommarker6	1	0	1	0",
							   "dommarker7	1	0	1	1",
							   "dommarker8	1	0	1	1"};
			
			try (BufferedReader br = new BufferedReader(
					new FileReader(tempFolderLocation + "\\dest" + "\\digest.multipleCSV_BOTH"))) {
				String fileRow; int i = 0;
				while ((fileRow = br.readLine()) != null) {
					assertEquals(output[i], fileRow);
					i++;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	 }
	 
	 @Test 
	 public void testSubColumn(){
		 GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
			createAndSetGobiiFile(instruction);
			instruction.setTable("multipleCSV_ROW");
			List<GobiiFileColumn> gobiiColumns = new ArrayList<GobiiFileColumn>();
			gobiiColumns.add(createGobiiConstantColumn(Integer.toString(0)));
			gobiiColumns.add(createGobiiAutoIncreamentColumn());
			gobiiColumns.add(createGobiiCSV_SUB_Column(0, 0));
			instruction.setGobiiFileColumns(gobiiColumns);

			CSVFileReaderV2 csvReader = new CSVFileReaderV2("", "");
			try {
				csvReader.processCSV(instruction);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			String output[] = {"constantName	AutoIncrement",
							   "0	0marker_name",
							   "0	1dnarunname_dom_1",
							   "0	2dnarunname_dom_2",
							   "0	3dnarunname_dom_3",
							   "0	4dnarunname_dom_4"};
			
			try (BufferedReader br = new BufferedReader(
					new FileReader(tempFolderLocation + "\\dest" + "\\digest.multipleCSV_ROW"))) {
				String fileRow; int i = 0;
				while ((fileRow = br.readLine()) != null) {
					assertEquals(output[i], fileRow);
					i++;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	 @Test
	 public void testMultipleFilesCSV_ROW() {

		 File file2 = new File(tempFolderLocation+ "\\src" + "\\file2.txt");;
		 try {
				String data[] = {
						"marker_name	dnarunname_dom_1	dnarunname_dom_2	dnarunname_dom_3	dnarunname_dom_4",
						"dommarker1	1	0	1	1"};
				BufferedWriter srcFileWriter = new BufferedWriter(
						new FileWriter(file2));
				srcFileWriter.write(data[0] +"\n");
				srcFileWriter.write(data[1] +"\n");
				srcFileWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		 
		 GobiiLoaderInstruction instruction = new GobiiLoaderInstruction();
			createAndSetGobiiFile(instruction);
			instruction.setTable("multipleCSV_ROW");
			List<GobiiFileColumn> gobiiColumns = new ArrayList<GobiiFileColumn>();
			gobiiColumns.add(createGobiiConstantColumn(Integer.toString(0)));
			gobiiColumns.add(createGobiiAutoIncreamentColumn());
			gobiiColumns.add(createGobiiCSV_ROW_Column(0, 0));
			instruction.setGobiiFileColumns(gobiiColumns);

			CSVFileReaderV2 csvReader = new CSVFileReaderV2("", "");
			try {
				csvReader.processCSV(instruction);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			String output[] = {"constantName	AutoIncrement	csvRow",
							   "0	0	marker_name",
							   "0	1	dnarunname_dom_1",
							   "0	2	dnarunname_dom_2",
							   "0	3	dnarunname_dom_3",
							   "0	4	dnarunname_dom_4",
							   "0	5	marker_name",
							   "0	6	dnarunname_dom_1",
							   "0	7	dnarunname_dom_2",
							   "0	8	dnarunname_dom_3",
							   "0	9	dnarunname_dom_4"};
			
			try (BufferedReader br = new BufferedReader(
					new FileReader(tempFolderLocation + "\\dest" + "\\digest.multipleCSV_ROW"))) {
				String fileRow; int i = 0;
				while ((fileRow = br.readLine()) != null) {
					assertEquals(output[i], fileRow);
					System.out.println(fileRow);
					i++;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		 file2.delete();
	}
	/**
	 * Creates gobiiFIleColumn for CSV_ROW with subcolumn
	 * @param rCoord
	 * @param cCoord
	 * @return
	 */
	 
	private GobiiFileColumn createGobiiCSV_SUB_Column(Integer rCoord, Integer cCoord) {
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
		fileColumn.setSubcolumn(true);
		fileColumn.setSubcolumnDelimiter("_");
		fileColumn.setDataSetType(DataSetType.IUPAC);
		fileColumn.setDataSetOrientationType(DataSetOrientationType.MARKER_FAST);
		fileColumn.setMetaDataId(null);
		return fileColumn;

	}

	/**
	 * Creates gobiiFIleColumn for CSV_BOTH
	 * @param rCoord
	 * @param cCoord
	 * @return
	 */
	@VisibleForTesting
	private GobiiFileColumn createGobiiCSV_BOTH_Column(Integer rCoord, Integer cCoord) {
		GobiiFileColumn fileColumn = new GobiiFileColumn();
		fileColumn.setGobiiColumnType(GobiiColumnType.CSV_BOTH);
		fileColumn.setRCoord(rCoord);
		fileColumn.setCCoord(cCoord);
		fileColumn.setName("csvBoth");
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


	/**
	 * Creates gobiiFileColumn for autoIncrement
	 * @return
	 */
	@VisibleForTesting
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

	/**
	 * Creates gobiiFileColumn for csv_row type.
	 * @param rCoord
	 * @param cCoord
	 * @return
	 */
	@VisibleForTesting
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
	
	/**
	 * Creates gobiiFileColumn for csv_col type.
	 * @param rCoord
	 * @param cCoord
	 * @return
	 */
	@VisibleForTesting
	private GobiiFileColumn createGobiiCSV_COL_Column(Integer rCoord, Integer cCoord) {

		GobiiFileColumn fileColumn = new GobiiFileColumn();
		fileColumn.setGobiiColumnType(GobiiColumnType.CSV_COLUMN);
		fileColumn.setRCoord(rCoord);
		fileColumn.setCCoord(cCoord);
		fileColumn.setName("csvCol");
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

	/**
	 * Creates a gobiiFileColumn for constant type.
	 * @param constantValue
	 * @return
	 */
	@VisibleForTesting
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

	/**
	 * Sets the gobiiFile in the instruction. This is common across all the test
	 * cases. Refer
	 * {@code org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFile.java}
	 * 
	 * @param gobiiFile
	 */
	@VisibleForTesting
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
	
	/**
	 * Deletes the directory pointed by path.
	 * @param path
	 * @return
	 */
	@VisibleForTesting
	static public boolean deleteDirectory(File path) {
	    if( path.exists() ) {
	      File[] files = path.listFiles();
	      for(int i=0; i<files.length; i++) {
	         if(files[i].isDirectory()) {
	           deleteDirectory(files[i]);
	         }
	         else {
	           files[i].delete();
	         }
	      }
	    }
	    return( path.delete() );
	  }
}