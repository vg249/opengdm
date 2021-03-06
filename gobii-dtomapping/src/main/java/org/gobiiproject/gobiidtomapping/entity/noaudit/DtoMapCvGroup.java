package org.gobiiproject.gobiidtomapping.entity.noaudit;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CvDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CvGroupDTO;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;

import java.util.List;

/**
 * Created by VCalaminos on 2/1/2017.
 */
public interface DtoMapCvGroup {

    List<CvDTO> getCvsForGroup(Integer groupId) throws GobiiDtoMappingException;

    CvGroupDTO getUserCvByGroupName(String groupName) throws GobiiDtoMappingException;

    CvGroupDTO getCvGroup(Integer cvGroupId) throws GobiiDtoMappingException;

    List<CvGroupDTO> getCvGroupsForType(GobiiCvGroupType gobiiCvGroupType) throws GobiiDtoMappingException;

    Integer getGroupTypeForGroupId(Integer groupId) throws GobiiDtoMappingException;

    CvGroupDTO getCvGroupDetailsByGroupName(String groupName, Integer cvGroupTypeId) throws GobiiDtoMappingException;

}
