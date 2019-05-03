package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.ProjectSamplesDTO;

import java.util.List;

/**
 * Created by VCalaminos on 5/2/2019.
 */
public interface DnaSampleService<T> {

    ProjectSamplesDTO createSamples(List<T> sampleListDTO) throws GobiiDomainException;

}
