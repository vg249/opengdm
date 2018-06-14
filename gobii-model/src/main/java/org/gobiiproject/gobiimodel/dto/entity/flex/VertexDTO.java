package org.gobiiproject.gobiimodel.dto.entity.flex;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.cvnames.VertexNameType;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.dto.entity.flex.vertexcolumns.VertexColumns;
import org.gobiiproject.gobiimodel.dto.entity.flex.vertexcolumns.VertexColumnsNameIdGeneric;
import org.gobiiproject.gobiimodel.dto.entity.flex.vertexcolumns.VertexColumnsPrincipleInvestigator;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiEntitySubType;
import org.gobiiproject.gobiimodel.types.GobiiVertexType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 4/8/2016.
 */
public class VertexDTO extends DTOBase {


    public VertexDTO() {
    }


    public VertexDTO(Integer vertexId,
                     VertexNameType vertexNameType,
                     GobiiVertexType gobiiVertexType,
                     String vertexName,
                     GobiiEntityNameType entityType,
                     GobiiEntitySubType entitySubType,
                     CvGroup cvGroup,
                     String cvTerm) {
        this.vertexId = vertexId;
        this.vertexNameType = vertexNameType;
        this.gobiiVertexType = gobiiVertexType;
        this.vertexName = vertexName;
        this.entityType = entityType;
        this.entitySubType = entitySubType;
        this.cvGroup = cvGroup;
        this.cvTerm = cvTerm;
    }

    @Override
    public Integer getId() {
        return this.getVertexId();
    }

    @Override
    public void setId(Integer id) {
        this.vertexId = id;
    }


    private Integer vertexId = 0;
    private VertexNameType vertexNameType;
    private GobiiVertexType gobiiVertexType = GobiiVertexType.UNKNOWN;
    private String vertexName;
    private List<Integer> filterVals = new ArrayList<>();
    private GobiiEntityNameType entityType;
    private GobiiEntitySubType entitySubType;
    private CvGroup cvGroup;
    private String cvTerm;


    public Integer getVertexId() {
        return vertexId;
    }

    public void setVertexId(Integer vertexId) {
        this.vertexId = vertexId;
    }

    public VertexNameType getVertexNameType() {
        return vertexNameType;
    }

    public void setVertexNameType(VertexNameType vertexNameType) {
        this.vertexNameType = vertexNameType;
    }

    public GobiiVertexType getGobiiVertexType() {
        return gobiiVertexType;
    }

    public void setGobiiVertexType(GobiiVertexType gobiiVertexType) {
        this.gobiiVertexType = gobiiVertexType;
    }

    public String getVertexName() {
        return vertexName;
    }

    public void setVertexName(String vertexName) {
        this.vertexName = vertexName;
    }

    public List<Integer> getFilterVals() {
        return filterVals;
    }

    public void setFilterVals(List<Integer> filterVals) {
        this.filterVals = filterVals;
    }

    public GobiiEntityNameType getEntityType() {
        return entityType;
    }

    public void setEntityType(GobiiEntityNameType entityType) {
        this.entityType = entityType;
    }

    public GobiiEntitySubType getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(GobiiEntitySubType entitySubType) {
        this.entitySubType = entitySubType;
    }

    public CvGroup getCvGroup() {
        return cvGroup;
    }

    public void setCvGroup(CvGroup cvGroup) {
        this.cvGroup = cvGroup;
    }

    public String getCvTerm() {
        return cvTerm;
    }

    public void setCvTerm(String cvTerm) {
        this.cvTerm = cvTerm;
    }

    @JsonIgnore
    public VertexColumns getVertexColumns() throws Exception{

        VertexColumns returnVal;

        switch (this.vertexNameType) {
            case VERTEX_TYPE_PROJECT:
                returnVal = new VertexColumnsNameIdGeneric();
                break;

            case VERTEX_TYPE_SAMPLING_DATE:
                returnVal = new VertexColumnsNameIdGeneric();
                break;

            case VERTEX_TYPE_GENOTYPING_PURPOSE:
                returnVal = new VertexColumnsNameIdGeneric();
                break;

            case VERTEX_TYPE_DIVISION:
                returnVal = new VertexColumnsNameIdGeneric();
                break;

            case VERTEX_TYPE_TRIAL_NAME:
                returnVal = new VertexColumnsNameIdGeneric();
                break;

            case VERTEX_TYPE_EXPERIMENT:
                returnVal = new VertexColumnsNameIdGeneric();
                break;

            case VERTEX_TYPE_DATASET:
                returnVal = new VertexColumnsNameIdGeneric();
                break;

            case VERTEX_TYPE_DATASET_TYPE:
                returnVal = new VertexColumnsNameIdGeneric();
                break;

            case VERTEX_TYPE_ANALYSIS:
                returnVal = new VertexColumnsNameIdGeneric();
                break;

            case VERTEX_TYPE_ANALYSIS_TYPE:
                returnVal = new VertexColumnsNameIdGeneric();
                break;

            case VERTEX_TYPE_REFERENCE_SAMPLE:
                returnVal = new VertexColumnsNameIdGeneric();
                break;

            case VERTEX_TYPE_PLATFORM:
                returnVal = new VertexColumnsNameIdGeneric();
                break;

            case VERTEX_TYPE_VENDOR:
                returnVal = new VertexColumnsNameIdGeneric();
                break;

            case VERTEX_TYPE_PROTOCOL:
                returnVal = new VertexColumnsNameIdGeneric();
                break;

            case VERTEX_TYPE_VENDOR_PROTOCOL:
                returnVal = new VertexColumnsNameIdGeneric();
                break;

            case VERTEX_TYPE_MAPSET:
                returnVal = new VertexColumnsNameIdGeneric();
                break;

            case VERTEX_TYPE_MAPSET_TYPE:
                returnVal = new VertexColumnsNameIdGeneric();
                break;

            case VERTEX_TYPE_LINKAGE_GROUP:
                returnVal = new VertexColumnsNameIdGeneric();
                break;

            case VERTEX_TYPE_GERMPLAM_SUBSPECIES:
                returnVal = new VertexColumnsNameIdGeneric();
                break;

            case VERTEX_TYPE_GERMPLASM_SPECIES:
                returnVal = new VertexColumnsNameIdGeneric();
                break;

            case VERTEX_TYPE_GERMPLASM_TYPE:
                returnVal = new VertexColumnsNameIdGeneric();
                break;

            case VERTEX_TYPE_PRINCIPLE_INVESTIGATOR:
                returnVal = new VertexColumnsPrincipleInvestigator();
                break;

            default:
                throw new Exception("Unknown vertex name type: " + this.vertexNameType.getVertexName() );
        }

        return returnVal;
    }
}
