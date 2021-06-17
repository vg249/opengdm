package org.gobiiproject.gobiimodel.dto.instructions.extractor;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class GobiiExtractorInstruction {

    private List<GobiiDataSetExtract> dataSetExtracts = new ArrayList<>();
    private List<Integer> mapsetIds = new ArrayList<>();

}
