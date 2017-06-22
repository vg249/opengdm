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
public class InstructionFileValidationTest {

	private final String NAME = "name";
	private final String MARKER_NAME = "marker_name";
	private final String LINKAGE_GROUP_NAME = "linkage_group_name";
	private final String STOP = "stop";
	private final String START = "start";
	private final String MATRIX = "matrix";
    private final String DNA_RUN_NAME = "dnarun_name";

	int rCoord = 1, cCoord = 2;

	/**
	 * Test case for (IF digest.matrix EXISTS) 
	 * 					digest.dataset_marker exists
	 * 					digest.dataset_dnarun exists
	 */
	@Test
	public void testMatrix2DatasetMarkerANDDatasetDNARunExist() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();
		
		instructionList.add(createInstruction(GobiiTableType.MATRIX, MATRIX, rCoord, cCoord));
		instructionList.add(createInstruction(GobiiTableType.DATASET_MARKER, MARKER_NAME,rCoord, cCoord));
		instructionList.add(createInstruction(GobiiTableType.DATASET_DNARUN, DNA_RUN_NAME,rCoord, cCoord));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNull(instructionFileValidator.validate());
	}

	/**
	 * Test case for (IF digest.matrix EXISTS) digest.dataset_marker exists
	 */
	@Test
	public void testMatrix2DatasetMarkerANDDatasetDNARunExist_Failure() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();
		
		instructionList.add(createInstruction(GobiiTableType.MATRIX, MATRIX, rCoord, cCoord));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNotNull(instructionFileValidator.validate());
	}

	/**
	 * Test case for 
                    if digest.marker file exists
                    digest.marker (name) == digest.dataset_marker (marker_name)
      */
	@Test
	public void testMarker2DatasetMarkerExist() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();
		
		instructionList.add(createInstruction(GobiiTableType.MARKER, NAME, rCoord, cCoord));
		instructionList.add(createInstruction(GobiiTableType.DATASET_MARKER, MARKER_NAME,rCoord, cCoord));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNull(instructionFileValidator.validate());
	}

	/**
	 * Test case for (IF digest.matrix EXISTS) digest.dataset_marker exists
	 */
	@Test
	public void testMarker2DatasetMarkerExist_Failure() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();
		
		instructionList.add(createInstruction(GobiiTableType.MARKER, NAME, rCoord, cCoord));
		instructionList.add(createInstruction(GobiiTableType.DATASET_MARKER, MARKER_NAME,rCoord+1, cCoord));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNotNull(instructionFileValidator.validate());
	}

	/**
	 * Test case for 
                   if digest.dnarun exists
   						digest.dnarun (name) == digest.ds_dnarun (dnarun_name)
     */
	@Test
	public void testDNARun2DatasetDNARunExist() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();
		
		instructionList.add(createInstruction(GobiiTableType.DNARUN, NAME, rCoord, cCoord));
		instructionList.add(createInstruction(GobiiTableType.DATASET_DNARUN, DNA_RUN_NAME, rCoord, cCoord));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNull(instructionFileValidator.validate());
	}
	
	/**
	 * Test case for 
                   if digest.dnarun exists
   						digest.dnarun (name) == digest.ds_dnarun (dnarun_name)
     */
	@Test
	public void testDNARun2DatasetDNARunExist_Failure() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();
		
		instructionList.add(createInstruction(GobiiTableType.DNARUN, NAME, rCoord, cCoord));
		instructionList.add(createInstruction(GobiiTableType.DATASET_DNARUN, DNA_RUN_NAME, rCoord+1, cCoord));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNotNull(instructionFileValidator.validate());
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