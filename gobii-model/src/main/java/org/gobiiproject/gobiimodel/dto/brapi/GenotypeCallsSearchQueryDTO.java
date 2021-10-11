package org.gobiiproject.gobiimodel.dto.brapi;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.apache.commons.collections.CollectionUtils;
import org.gobiiproject.gobiimodel.validators.CheckAtLeastOneNotNullOrEmpty;

import lombok.Data;
import lombok.ToString;

@CheckAtLeastOneNotNullOrEmpty(
        fieldNames = {
            "callSetDbIds", "callSetNames", "variantDbIds", "variantNames",
            "sampleDbIds", "sampleNames", "samplePUIs", "variantSetDbIds",
            "germplasmPUIs", "germplasmDbIds", "germplasmNames"
        })
@ToString
@Data
public class GenotypeCallsSearchQueryDTO {

    @Size(max = 1000, message = "Only 1000 callSetIds allowed per query")
    private Set<Integer> callSetDbIds = new HashSet<>();

    @Size(max = 1000, message = "Only 1000 callSetNames allowed per query")
    private Set<String> callSetNames = new HashSet<>();

    @Size(max = 1000, message = "Only 1000 sampleDbIds allowed per query")
    private Set<Integer> sampleDbIds = new HashSet<>();

    @Size(max = 1000, message = "Only 1000 sampleDbIds allowed per query")
    private Set<String> sampleNames = new HashSet<>();

    @Size(max = 1000, message = "Only 1000 sampleDbIds allowed per query")
    private Set<String> samplePUIs = new HashSet<>();

    @Size(max = 1000, message = "Only 1000 variantDbIds allowed per query")
    private Set<Integer> variantDbIds = new HashSet<>();

    @Size(max = 1000, message = "Only 1000 variantNames allowed per query")
    private Set<String> variantNames = new HashSet<>();

    @Size(max = 1000, message = "Only 1000 variantSetDbIds allowed per query")
    private Set<String> variantSetDbIds = new HashSet<>();

    @Size(max = 1000, message = "Only 1000 variantSetNames allowed per query")
    private Set<String> variantSetNames = new HashSet<>();

    @Size(max = 1000, message = "Only 1000 germplasmPUIs allowed per query")
    private Set<String> germplasmPUIs = new HashSet<>();

    @Size(max = 1000, message = "Only 1000 germplasmDbIds allowed per query")
    private Set<Integer> germplasmDbIds = new HashSet<>();

    @Size(max = 1000, message = "Only 1000 germplasmNames allowed per query")
    private Set<String> germplasmNames = new HashSet<>();

    @JsonIgnore
    public boolean isCallSetsQueriesEmpty() {
        return CollectionUtils.isEmpty(callSetDbIds) && CollectionUtils.isEmpty(callSetNames) &&
            CollectionUtils.isEmpty(sampleDbIds) && CollectionUtils.isEmpty(sampleNames) &&
            CollectionUtils.isEmpty(samplePUIs) && CollectionUtils.isEmpty(germplasmPUIs) &&
            CollectionUtils.isEmpty(germplasmDbIds) && CollectionUtils.isEmpty(germplasmNames);

    }

    @JsonIgnore
    public boolean isVariantsQueriesEmpty() {
        return CollectionUtils.isEmpty(this.getVariantDbIds()) &&
            CollectionUtils.isEmpty(this.getVariantNames());
    }
    
    @JsonIgnore
    public boolean isVariantSetQueriesEmpty() {
        return CollectionUtils.isEmpty(this.getVariantSetDbIds()) &&
            CollectionUtils.isEmpty(this.getVariantSetNames());
    }
}
