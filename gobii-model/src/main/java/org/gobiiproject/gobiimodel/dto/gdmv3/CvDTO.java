package org.gobiiproject.gobiimodel.dto.gdmv3;

import java.util.List;

import org.gobiiproject.gobiimodel.dto.children.CvPropertyDTO;

import lombok.Data;

@Data
public class CvDTO {

    public static interface Create{}

    private Integer cvId;

    private String cvName;

    private String cvDescription;

    private Integer cvGroupId;

    private String cvGroupName;

    private String cvStatus;

    private String cvGroupType;

    private List<CvPropertyDTO> properties;
    
}