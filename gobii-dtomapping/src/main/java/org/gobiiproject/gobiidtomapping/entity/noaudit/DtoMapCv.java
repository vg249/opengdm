package org.gobiiproject.gobiidtomapping.entity.noaudit;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CvDTO;

import java.util.List;

/**
 * Created by Angel on 4/29/2016.
 */
public interface DtoMapCv {
    CvDTO getCvDetails(Integer cvId) throws GobiiDtoMappingException;
    List<CvDTO> getCvs() throws GobiiDtoMappingException;
    List<CvDTO> getCvsByGroupName(String groupName) throws GobiiDtoMappingException;
    CvDTO createCv(CvDTO cvDTO) throws GobiiDtoMappingException;
    CvDTO replaceCv(Integer cvId, CvDTO cvDTO) throws GobiiDtoMappingException;
    CvDTO deleteCv(CvDTO cvDTO) throws GobiiDtoMappingException;
}
