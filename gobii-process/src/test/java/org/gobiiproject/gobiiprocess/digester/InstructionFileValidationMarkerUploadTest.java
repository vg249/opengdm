package org.gobiiproject.gobiiprocess.digester;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;
import org.gobiiproject.gobiiprocess.digester.utils.GobiiTableType;
import org.gobiiproject.gobiiprocess.digester.utils.InstructionFileValidator;
import org.junit.Test;

import com.google.common.annotations.VisibleForTesting;

/**
 * Unit test for InstructionFileValidationTest.
 */
public class InstructionFileValidationMarkerUploadTest {

	private final String NAME = "name";
	private final String MARKER_NAME = "marker_name";
	private final String LINKAGE_GROUP_NAME = "linkage_group_name";
	private final String STOP = "stop";
	private final String START = "start";
	private final String EXTERNAL_CODE = "external_code";
	private final String DNA_SAMPLE_NAME = "dnasample_name";
	private final String DNA_RUN_NAME = "dnarun_name";
	private final String MATRIX = "matrix";

	int rCoord = 1, cCoord = 2;

	/**
	 * Test case for digest.marker (name) == digest.lg_marker (marker_name)
	 */
	@Test
	public void testMarker2lgMarker() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();
		
		instructionList.add(createInstruction(GobiiTableType.MARKER, NAME, rCoord, cCoord));
		instructionList.add(createlgMarkerInstruction(rCoord, cCoord));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNull(instructionFileValidator.validateMarkerUpload());
	}

	/**
	 * Test case for digest.marker (name) == digest.lg_marker (marker_name)
	 */
	@Test
	public void testMarker2lgMarker_Failure() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();
		
		instructionList.add(createInstruction(GobiiTableType.MARKER, NAME, rCoord, cCoord));
		instructionList.add(createlgMarkerInstruction(rCoord+1, cCoord+1));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNotNull(instructionFileValidator.validateMarkerUpload());
	}

	
	/**
	 * Test case for digest.lg_marker (lg_name) == digest.linkage_group (name)
	 */
	@Test
	public void testlgMarker2linkageGroup() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();

		instructionList.add(createlgMarkerInstruction(rCoord, cCoord));
		instructionList.add(createInstruction(GobiiTableType.LINKAGE_GROUP, NAME, rCoord,	cCoord));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNull(instructionFileValidator.validateMarkerUpload());
	}

	/**
	 * Test case for digest.lg_marker (lg_name) == digest.linkage_group (name)
	 */
	@Test
	public void testlgMarker2linkageGroup_Failure() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();

		instructionList.add(createlgMarkerInstruction(rCoord, cCoord));
		instructionList.add(createInstruction(GobiiTableType.LINKAGE_GROUP, NAME, rCoord+1,	cCoord+1));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNotNull(instructionFileValidator.validateMarkerUpload());
	}

	
	/**
	 * Test case for digest.marker_prop (marker_name) == digest.marker (name)
	 */
	@Test
	public void testMarkerProp2Marker() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();

		instructionList.add(createInstruction(GobiiTableType.MARKER_PROP, MARKER_NAME, rCoord, cCoord));
		instructionList.add(createInstruction(GobiiTableType.MARKER, NAME, rCoord, cCoord));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNull(instructionFileValidator.validateMarkerUpload());
	}

	/**
	 * Test case for digest.marker_prop (marker_name) == digest.marker (name)
	 */
	@Test
	public void testMarkerProp2Marker_Failure() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();

		instructionList.add(createInstruction(GobiiTableType.MARKER_PROP, MARKER_NAME, rCoord, cCoord));
		instructionList.add(createInstruction(GobiiTableType.MARKER, NAME, rCoord+1, cCoord+1));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNotNull(instructionFileValidator.validateMarkerUpload());
	}
	
	/*
	 * Tests case for digest.marker_prop upload only if digest.marker exist
	 */
	@Test
	public void testMarkerProp2MarkerExist() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();

		instructionList.add(createInstruction(GobiiTableType.MARKER_PROP, NAME, rCoord, cCoord));
		instructionList.add(createInstruction(GobiiTableType.MARKER, NAME, rCoord, cCoord));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNull(instructionFileValidator.validateMarkerUpload());
	}

	/*
	 * Tests case for digest.marker_prop upload only if digest.marker exist
	 */
	@Test
	public void testMarkerProp2MarkerExist_Failure() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();

		instructionList.add(createInstruction(GobiiTableType.MARKER_PROP, NAME, rCoord, cCoord));

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNotNull(instructionFileValidator.validateMarkerUpload());
	}
	
	/*
	 * Test case for if digest.marker_linkage_group exists, then file must contain all columns lg_name, marker_name, start and stop
	 */
	@Test
	public void testMarkerLinkageGroup() {

		List<GobiiLoaderInstruction> instructionList = new ArrayList<>();

		GobiiLoaderInstruction lgMarkerInstruction = createlgMarkerInstruction(rCoord, cCoord);
		lgMarkerInstruction.getGobiiFileColumns().remove(0);
		instructionList.add(lgMarkerInstruction);

		InstructionFileValidator instructionFileValidator = new InstructionFileValidator(instructionList);
		instructionFileValidator.processInstructionFile();
		assertNotNull(instructionFileValidator.validateMarkerUpload());
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