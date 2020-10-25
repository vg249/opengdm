package org.gobiiproject.gobiidomain.services.gdmv3;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.gdmv3.ContactDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.LoaderTemplateDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.templates.MarkerTemplateDTO;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.LoaderTemplate;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.LoaderTemplateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional
public class LoaderTemplateServiceImpl implements LoaderTemplateService {


    @Autowired
    private LoaderTemplateDao loaderTemplateDao;

    @Autowired
    private CvDao cvDao;

    @Autowired
    private ContactDao contactDao;

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public LoaderTemplateDTO addMarkerTemplate(
        LoaderTemplateDTO loaderTemplateDTO
    ) throws Exception {

        LoaderTemplate loaderTemplate  = new LoaderTemplate();

        // Get current user
        String userName = ContactService.getCurrentUser();
        Contact createdBy = contactDao.getContactByUsername(userName);

        loaderTemplate.setTemplateName(loaderTemplateDTO.getTemplateName());
        loaderTemplate.setTemplate(loaderTemplateDTO.getTemplate());

        loaderTemplate.setCreatedDate(new Date());
        loaderTemplate.setModifiedDate(new Date());
        loaderTemplate.setCreatedBy(createdBy);
        loaderTemplate.setModifiedBy(createdBy);


        // Set marker payload type as template type
        Cv templateType = cvDao.getCvs(
            "markers",
            CvGroupTerm.CVGROUP_PAYLOADTYPE.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);

        loaderTemplate.setTemplateType(templateType);

        loaderTemplate = loaderTemplateDao.create(loaderTemplate);

        ModelMapper.mapEntityToDto(loaderTemplate, loaderTemplateDTO);

        return loaderTemplateDTO;

    }


}
