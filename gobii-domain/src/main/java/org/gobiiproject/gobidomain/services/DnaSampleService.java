package org.gobiiproject.gobidomain.services;

import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.ProjectSamplesDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.SampleMetadataDTO;

import java.io.InputStream;
import java.util.List;

public interface DnaSampleService {

    ProjectSamplesDTO createSamples(ProjectSamplesDTO sampleListDTO) throws GobiiDomainException;
    void uploadSamples(
            InputStream inputFileStream,
            SampleMetadataDTO sampleMetadataDTO,
            String cropType) throws GobiiDomainException;

}
