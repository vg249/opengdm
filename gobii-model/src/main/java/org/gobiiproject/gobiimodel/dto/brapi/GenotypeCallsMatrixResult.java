package org.gobiiproject.gobiimodel.dto.brapi;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class GenotypeCallsMatrixResult {

    public GenotypeCallsMatrixResult(
        String sepUnphased, String sepPhased, String unknownString, String[][] data,
        List<CallSetDTO> callsets, List<VariantDTO> variants) {

        this.sepUnphased = sepUnphased;
        this.sepPhased = sepPhased;
        this.unknownString = unknownString;
        this.variants = variants;
        this.callSets = callsets;
        this.data = data;

    }

    private String sepUnphased;

    private String sepPhased;

    private String unknownString;

    private List<VariantDTO> variants;

    private List<CallSetDTO> callSets;

    private String[][] data;

}
