package org.gobiiproject.gobiidomain.services.gdmv3;

import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.LoaderTemplateDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.templates.DnaRunTemplateDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.templates.MarkerTemplateDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.LoaderTemplate;
import org.gobiiproject.gobiisampletrackingdao.GobiiDaoException;

import java.util.List;

public interface LoaderTemplateService {
    LoaderTemplateDTO addMarkerTemplate(LoaderTemplateDTO markerTemplate) throws Exception;
    LoaderTemplateDTO addDnaRunTemplate(LoaderTemplateDTO dnaRunTemplate) throws Exception;
    PagedResult<LoaderTemplateDTO> getDnaRunTemplates(Integer pageSize,
                                                      Integer pageNum) throws GobiiException;
    PagedResult<LoaderTemplateDTO> getMarkerTemplates(Integer pageSize,
                                                      Integer pageNum) throws GobiiException;
    DnaRunTemplateDTO getEmptyDnaRunTemplate() throws GobiiException;
    MarkerTemplateDTO getEmptyMarkerTemplate() throws GobiiException;
}
