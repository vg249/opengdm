package org.gobiiproject.gobiimodel.dto.gdmv3;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.gobiiproject.gobiimodel.dto.annotations.GobiiEntityMap;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.gobiiproject.gobiimodel.entity.Dataset;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = false, value={
    "id", "allowedProcessTypes", "entityNameType", "status"
})
@Data
public class DatasetDTO extends DTOBaseAuditable {
 
    public DatasetDTO() {
        super(GobiiEntityNameType.DATASET);
    }

	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return datasetId;
	}

	@Override
	public void setId(Integer id) {
		setDatasetId(id);
    }
    
    @GobiiEntityMap(paramName = "datasetId", entity = Dataset.class )
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer datasetId;


    @GobiiEntityMap(paramName = "datasetName", entity = Dataset.class)
    private String datasetName;

    @GobiiEntityMap(paramName = "experiment.experimentId", entity = Dataset.class, deep = true )
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer experimentId;

    private Integer projectId;
    private String projectName;

    private Integer piContactId;
    private String  piContactName;


    @GobiiEntityMap(paramName = "datasetTypeId", entity = Dataset.class )
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer datasetTypeId;

    private String datasetTypeName;


    @GobiiEntityMap(paramName = "analyses", entity = Dataset.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer[] analysisIds;


    @GobiiEntityMap(paramName = "callingAnalysis.analysisId", entity = Dataset.class, deep = true)
    @JsonSerialize(using = ToStringSerializer.class)
    @Digits(integer = 10, fraction = 0, groups = DatasetRequestDTO.Create.class)
    private Integer callingAnalysisId;

    @GobiiEntityMap(paramName = "callingAnalysis.analysisName", entity = Dataset.class, deep = true)
    private String callingAnalysisName;

    //manually set this before transporting
    private List<AnalysisDTO> analyses;


}