package org.gobiiproject.gobiimodel.dto.entity.noaudit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiParam;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityColumn;
import org.gobiiproject.gobiimodel.dto.entity.annotations.GobiiEntityParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by VCalaminos on 6/25/2019.
 * Modified By VishnuG
 */

@JsonIgnoreProperties(ignoreUnknown = true, value={
        "id", "allowedProcessTypes", "entityNameType",
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CallSetBrapiDTO extends DTOBase {

    private Integer callSetDbId;

    private Integer studyDbId;

    private Integer sampleDbId;

    private String callSetName;

    private List<Integer> variantSetIds = new ArrayList<>();

    private Integer germplasmDbId;

    private String germplasmName;

    private String sampleName;

    private Map<String, String> additionalInfo = new HashMap<>();

    @Override
    public Integer getId() {
        return this.callSetDbId;
    }

    @Override
    public void setId(Integer id) {
        this.callSetDbId = id;
    }

    public Integer getCallSetDbId() {
        return callSetDbId;
    }

    public void setCallSetDbId(Integer callSetDbId) {
        this.callSetDbId = callSetDbId;
    }

    public Integer getStudyDbId() {
        return studyDbId;
    }

    public void setStudyDbId(Integer studyDbId) {
        this.studyDbId = studyDbId;
    }

    public Integer getSampleDbId() {
        return sampleDbId;
    }

    public void setSampleDbId(Integer sampleDbId) {
        this.sampleDbId = sampleDbId;
    }

    public String getCallSetName() {
        return callSetName;
    }

    public void setCallSetName(String callSetName) {
        this.callSetName = callSetName;
    }

    public List<Integer> getVariantSetIds() {
        return variantSetIds;
    }

    public void setVariantSetIds(List<Integer> variantSetIds) {
        this.variantSetIds = variantSetIds;
    }

    public Integer getGermplasmDbId() {
        return germplasmDbId;
    }

    public void setGermplasmDbId(Integer germplasmDbId) {
        this.germplasmDbId = germplasmDbId;
    }

    public String getGermplasmName() {
        return germplasmName;
    }

    public void setGermplasmName(String germplasmName) {
        this.germplasmName = germplasmName;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public Map<String, String> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(Map<String, String> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
