package org.gobiiproject.gobiiprocess.digester;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;
import org.gobiiproject.gobiiprocess.digester.utils.GobiiTableType;
import org.gobiiproject.gobiiprocess.digester.utils.Validator.InstructionFileValidator;
import org.junit.Test;

import com.google.common.annotations.VisibleForTesting;

/**
 * Unit test for InstructionFileValidationTest.
 */
public class InstructionFileValidationSampleUploadTest {

	private final String NAME = "name";
	private final String MARKER_NAME = "marker_name";
	private final String LINKAGE_GROUP_NAME = "linkage_group_name";
	private final String STOP = "stop";
	private final String START = "start";
	private final String EXTERNAL_CODE = "external_code";
	private final String DNA_SAMPLE_NAME = "dnasample_name";
	private final String DNA_RUN_NAME = "dnarun_name";

	int rCoord = 1, cCoord = 2;

	/**
	 * Test case for digest.germplasm (external_code) == digest.dnasample (external_code)
	 */
	@Test
	public void testGermplasm2DNASample() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();
		
		instructionList.add(createInstruction(GobiiTableType.GERMPLASM, EXTERNAL_CODE, rCoord, cCoord));
		instructionList.add(createInstruction(GobiiTableType.DNASAMPLE, EXTERNAL_CODE, rCoord, cCoord));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNull(instructionFileValidator.validateSampleUpload());
	}

	/**
	 * Test case for digest.germplasm (external_code) == digest.dnasample (external_code)
	 */
	@Test
	public void testGermplasm2DNASample_Failure() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();
		
		instructionList.add(createInstruction(GobiiTableType.GERMPLASM, EXTERNAL_CODE, rCoord, cCoord));
		instructionList.add(createInstruction(GobiiTableType.DNASAMPLE, EXTERNAL_CODE, rCoord+1, cCoord+1));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNotNull(instructionFileValidator.validateSampleUpload());
	}

	/**
	 * Test case for digest.dnasample (name) == digest.dnarun (dnasample_name)
	 */
	@Test
	public void testDNASample2DNARUN() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();

		instructionList.add(createInstruction(GobiiTableType.DNASAMPLE, NAME, rCoord,	cCoord));
		instructionList.add(createInstruction(GobiiTableType.DNARUN, DNA_SAMPLE_NAME, rCoord,	cCoord));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNull(instructionFileValidator.validateSampleUpload());
	}

	/**
	 * Test case for digest.dnasample (name) == digest.dnarun (dnasample_name)
	 */
	@Test
	public void testDNASample2DNARUN_Failure() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();

		instructionList.add(createInstruction(GobiiTableType.DNASAMPLE, NAME, rCoord,	cCoord));
		instructionList.add(createInstruction(GobiiTableType.DNARUN, DNA_SAMPLE_NAME, rCoord+1,	cCoord));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNotNull(instructionFileValidator.validateSampleUpload());
	}

	/*
	 * Tests case for digest.germplasm_prop upload only if digest.germplasm exists
	 */
	@Test
	public void testGermplasmProp2GermplasmExist_Failure() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();

		instructionList.add(createInstruction(GobiiTableType.GERMPLASM_PROP, NAME, rCoord, cCoord));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNotNull(instructionFileValidator.validateSampleUpload());
	}
	
	/**
	 * Test case for digest.germplasm_prop (external_code) == digest.germplasm (external_code)
	 */
	@Test
	public void testGermplasmProp2Germplasm() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();

		instructionList.add(createInstruction(GobiiTableType.GERMPLASM_PROP, EXTERNAL_CODE, rCoord, cCoord));
		instructionList.add(createInstruction(GobiiTableType.GERMPLASM, EXTERNAL_CODE, rCoord, cCoord));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNull(instructionFileValidator.validateSampleUpload());
	}

	/**
	 * Test case for digest.germplasm_prop (external_code) == digest.germplasm (external_code)
	 */
	@Test
	public void testGermplasmProp2Germplasm_Failure() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();

		instructionList.add(createInstruction(GobiiTableType.GERMPLASM_PROP, EXTERNAL_CODE, rCoord, cCoord));
		instructionList.add(createInstruction(GobiiTableType.GERMPLASM, EXTERNAL_CODE, rCoord+1, cCoord+1));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNotNull(instructionFileValidator.validateSampleUpload());
	}
	
	/*
	 * Tests case for digest.dnasample_prop upload only if digest.dnasample exists
	 */
	@Test
	public void testDNASampleProp2DNASampleExist_Failure() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();

		instructionList.add(createInstruction(GobiiTableType.DNASAMPLE_PROP, NAME, rCoord, cCoord));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNotNull(instructionFileValidator.validateSampleUpload());
	}

	/*
	 * Tests case for digest.dnasample_prop (dnasample_name) == digest.dnasample (name)
	 */
	@Test
	public void testDNASampleProp2DNASample() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();

		instructionList.add(createInstruction(GobiiTableType.DNASAMPLE_PROP, DNA_SAMPLE_NAME, rCoord, cCoord));
		instructionList.add(createInstruction(GobiiTableType.DNASAMPLE, NAME, rCoord, cCoord));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNull(instructionFileValidator.validateSampleUpload());
	}

	/**
	 * Test case for digest.dnasample_prop (dnasample_name) == digest.dnasample (name)
	 */
	@Test
	public void testDNASampleProp2DNASample_Failure() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();

		instructionList.add(createInstruction(GobiiTableType.DNASAMPLE_PROP, DNA_SAMPLE_NAME, rCoord, cCoord));
		instructionList.add(createInstruction(GobiiTableType.DNASAMPLE, NAME, rCoord+1, cCoord+1));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNotNull(instructionFileValidator.validateSampleUpload());
	}
	
	/*
	 * Tests case for digest.dnarun_prop upload only if digest.dnarun file exists
	 */
	@Test
	public void testDNARunProp2DNARunExist_Failure() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();

		instructionList.add(createInstruction(GobiiTableType.DNARUN_PROP, NAME, rCoord, cCoord));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNotNull(instructionFileValidator.validateSampleUpload());
	}
	
	/*
	 * Tests case for digest.dnarun_prop (dnarun_name) == digest.dnarun (name)
	 */
	@Test
	public void testDNARunProp2DNARun() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();

		instructionList.add(createInstruction(GobiiTableType.DNARUN_PROP, DNA_RUN_NAME, rCoord, cCoord));
		instructionList.add(createInstruction(GobiiTableType.DNARUN, NAME, rCoord, cCoord));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNull(instructionFileValidator.validateSampleUpload());
	}

	/*
	 * Tests case for digest.dnarun_prop (dnarun_name) == digest.dnarun (name)
	 */
	@Test
	public void testDNARunProp2DNARun_Failure() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();

		instructionList.add(createInstruction(GobiiTableType.DNARUN_PROP, DNA_RUN_NAME, rCoord, cCoord));
		instructionList.add(createInstruction(GobiiTableType.DNARUN, NAME, rCoord+1, cCoord+1));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNotNull(instructionFileValidator.validateSampleUpload());
	}
	
	@VisibleForTesting
	private GobiiLoaderInstruction createInstruction(String tableName, String nameValue, int rCoord, int cCoord) {
		GobiiLoaderInstruction markerInstruction = new GobiiLoaderInstruction();
		markerInstruction.setTable(tableName);
		List<GobiiFileColumn> linkageGroupGobiiColumns = new ArrayList<>();
		linkageGroupGobiiColumns.add(createGobiiColumn(nameValue, rCoord, cCoord));
		markerInstruction.setGobiiFileColumns(linkageGroupGobiiColumns);
		return markerInstruction;
	}

	@VisibleForTesting
	private GobiiLoaderInstruction createlgMarkerInstruction(int rCoord, int cCoord) {
		GobiiLoaderInstruction lgMarkerInstruction = new GobiiLoaderInstruction();
		lgMarkerInstruction.setTable(GobiiTableType.MARKER_LINKAGE_GROUP);
		List<GobiiFileColumn> lgMarkerGobiiColumns = new ArrayList<>();
		lgMarkerGobiiColumns.add(createGobiiColumn(MARKER_NAME, rCoord, cCoord));
		lgMarkerGobiiColumns.add(createGobiiColumn(LINKAGE_GROUP_NAME, rCoord, cCoord));
		lgMarkerGobiiColumns.add(createGobiiColumn(START, rCoord, cCoord));
		lgMarkerGobiiColumns.add(createGobiiColumn(STOP, rCoord, cCoord));
		lgMarkerInstruction.setGobiiFileColumns(lgMarkerGobiiColumns);
		return lgMarkerInstruction;
	}

	GobiiFileColumn createGobiiColumn(String name, Integer rCoord, Integer cCoord) {
		GobiiFileColumn fileColumn = new GobiiFileColumn();
		fileColumn.setGobiiColumnType(GobiiColumnType.CSV_ROW);
		fileColumn.setRCoord(rCoord);
		fileColumn.setCCoord(cCoord);
		fileColumn.setName(name);
		return fileColumn;

	}

}