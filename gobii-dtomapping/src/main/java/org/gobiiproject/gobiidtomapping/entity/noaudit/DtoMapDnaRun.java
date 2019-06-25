package org.gobiiproject.gobiidtomapping.entity.noaudit;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DnaRunDTO;

/**
 * Created by VCalaminos on 6/25/2019.
 */
public interface DtoMapDnaRun {

    DnaRunDTO get(Integer dnaRunId) throws GobiiDtoMappingException;

}
