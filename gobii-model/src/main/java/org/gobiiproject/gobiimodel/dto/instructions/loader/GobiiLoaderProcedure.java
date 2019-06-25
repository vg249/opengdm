package org.gobiiproject.gobiimodel.dto.instructions.loader;

import lombok.Data;
import lombok.experimental.Accessors;
import java.util.List;

@Data
@Accessors(chain = true)
public class GobiiLoaderProcedure {

    private GobiiLoaderMetadata metadata;
    private List<GobiiLoaderInstruction> instructions;

}
