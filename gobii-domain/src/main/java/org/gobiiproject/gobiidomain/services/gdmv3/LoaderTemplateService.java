package org.gobiiproject.gobiidomain.services.gdmv3;

import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.templates.MarkerTemplateDTO;
import org.gobiiproject.gobiimodel.entity.LoaderTemplate;

public interface LoaderTemplateService {

    LoaderTemplate addMarkerTemplate(
        MarkerTemplateDTO markerTemplate
    ) throws GobiiException;
}
