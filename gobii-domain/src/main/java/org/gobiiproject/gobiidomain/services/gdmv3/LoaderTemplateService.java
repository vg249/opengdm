package org.gobiiproject.gobiidomain.services.gdmv3;

import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.LoaderTemplateDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.templates.DnaRunTemplateDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.templates.MarkerTemplateDTO;
import org.gobiiproject.gobiimodel.entity.LoaderTemplate;

public interface LoaderTemplateService {
    LoaderTemplateDTO addMarkerTemplate(LoaderTemplateDTO markerTemplate) throws Exception;
    LoaderTemplateDTO addDnaRunTemplate(LoaderTemplateDTO dnaRunTemplate) throws Exception;
    DnaRunTemplateDTO getDnaRunTemplate() throws GobiiException;
    MarkerTemplateDTO getMarkerTemplate() throws GobiiException;
}
