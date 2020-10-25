package org.gobiiproject.gobiidomain.services.gdmv3;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.gdmv3.ContactDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.LoaderTemplateDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.templates.MarkerTemplateDTO;
import org.gobiiproject.gobiimodel.entity.LoaderTemplate;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.LoaderTemplateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoaderTemplateServiceImpl implements LoaderTemplateService {


    @Autowired
    private LoaderTemplateDao loaderTemplateDao;

    @Autowired
    private CvDao cvDao;

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public LoaderTemplateDTO addMarkerTemplate(
        MarkerTemplateDTO markerTemplateDTO
    ) throws GobiiException {

        LoaderTemplateDTO loaderTemplateDTO = new LoaderTemplateDTO();

        LoaderTemplate loaderTemplate  = new LoaderTemplate();

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        ContactDTO userContact = this.contactService.getContactByUserName(userName);


        loaderTemplate.setTemplate(mapper.valueToTree(markerTemplateDTO));

    }


}
