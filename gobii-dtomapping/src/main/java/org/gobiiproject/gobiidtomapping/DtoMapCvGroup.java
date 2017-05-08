package org.gobiiproject.gobiidtomapping;

import org.gobiiproject.gobiimodel.headerlesscontainer.CvDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.CvGroupDTO;

import java.util.List;

/**
 * Created by VCalaminos on 2/1/2017.
 */
public interface DtoMapCvGroup {

    List<CvDTO> getCvsForGroup(Integer groupId) throws GobiiDtoMappingException;

    CvGroupDTO getUserCvByGroupName(String groupName) throws GobiiDtoMappingException;

    CvGroupDTO getCvGroup(Integer cvGroupId) throws GobiiDtoMappingException;

    Integer getGroupTypeForGroupId(Integer groupId) throws GobiiDtoMappingException;

}
