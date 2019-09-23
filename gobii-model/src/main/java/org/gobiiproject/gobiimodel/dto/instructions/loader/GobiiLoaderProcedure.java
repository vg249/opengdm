package org.gobiiproject.gobiimodel.dto.instructions.loader;

import java.util.LinkedList;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GobiiLoaderProcedure {

	private GobiiLoaderMetadata metadata = new GobiiLoaderMetadata();
	private List<GobiiLoaderInstruction> instructions = new LinkedList<>();

}