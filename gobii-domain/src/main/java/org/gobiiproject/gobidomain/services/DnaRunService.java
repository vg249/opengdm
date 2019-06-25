package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DnaRunDTO;

/**
 * Created by VCalaminos on 6/25/2019.
 */
public interface DnaRunService {

    DnaRunDTO getDnaRunById(Integer dnaRunId) throws GobiiDomainException;

}
