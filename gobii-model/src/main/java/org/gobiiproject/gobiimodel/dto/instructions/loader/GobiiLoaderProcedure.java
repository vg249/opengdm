package org.gobiiproject.gobiimodel.dto.instructions.loader;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

@Data
@Accessors(chain = true)
public class GobiiLoaderProcedure {

	private GobiiLoaderMetadata metadata = new GobiiLoaderMetadata();
	private List<GobiiLoaderInstruction> instructions = new LinkedList<>();

}