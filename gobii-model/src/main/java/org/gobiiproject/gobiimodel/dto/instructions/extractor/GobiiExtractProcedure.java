package org.gobiiproject.gobiimodel.dto.instructions.extractor;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class GobiiExtractProcedure {

	private List<GobiiExtractorInstruction> instructions = new ArrayList<>();
	private GobiiExtractMetadata metadata = new GobiiExtractMetadata();

}
