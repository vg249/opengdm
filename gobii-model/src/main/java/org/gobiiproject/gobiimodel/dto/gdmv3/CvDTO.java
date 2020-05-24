package org.gobiiproject.gobiimodel.dto.gdmv3;

import java.util.List;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;

import lombok.Data;

@Data
public class CvDTO {
    
    public static interface Create{}

    @JsonSerialize(using = ToStringSerializer.class)
    private Integer cvId;

    @NotBlank(groups = {CvDTO.Create.class})
    private String cvName;

    @NotBlank(groups = {CvDTO.Create.class})
    private String cvDescription;

    @NotNull(groups = {CvDTO.Create.class})
    @Positive(groups = {CvDTO.Create.class})
    @JsonSerialize(using = ToStringSerializer.class )
    private Integer cvGroupId;

    private String cvGroupName;

    private String cvStatus;

    private String cvGroupType;

    private List<CvPropertyDTO> properties;
}