package org.gobiiproject.gobiidtomapping.entity.noaudit;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.noaudit.CallSetBrapiDTO;

import java.util.List;

/**
 * Created by VCalaminos on 6/25/2019.
 */
public interface DtoMapDnaRun {

    CallSetBrapiDTO get(Integer dnaRunId) throws GobiiDtoMappingException;
    List<CallSetBrapiDTO> getList(Integer pageToken, Integer pageSize, CallSetBrapiDTO callSetBrapiDTOFilter) throws GobiiDtoMappingException;

}
