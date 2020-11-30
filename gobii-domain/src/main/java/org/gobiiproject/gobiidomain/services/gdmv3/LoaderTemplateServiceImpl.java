package org.gobiiproject.gobiidomain.services.gdmv3;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobiidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.gdmv3.ContactDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.LoaderTemplateDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.templates.DnaRunTemplateDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.templates.MarkerTemplateDTO;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.LoaderTemplate;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiimodel.types.GobiiLoaderPayloadTypes;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.LoaderTemplateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional
public class LoaderTemplateServiceImpl implements LoaderTemplateService {


    @Autowired
    private LoaderTemplateDao loaderTemplateDao;

    @Autowired
    private CvDao cvDao;

    @Autowired
    private ContactDao contactDao;

    @Override
    public LoaderTemplateDTO addMarkerTemplate(LoaderTemplateDTO loaderTemplateDTO
    ) throws Exception {
        return addLoaderTemplate(
            loaderTemplateDTO,
            GobiiLoaderPayloadTypes.MARKERS);
    }

    @Override
    public LoaderTemplateDTO addDnaRunTemplate(LoaderTemplateDTO loaderTemplateDTO
    ) throws Exception {
        return addLoaderTemplate(
            loaderTemplateDTO,
            GobiiLoaderPayloadTypes.SAMPLES);
    }

    private LoaderTemplateDTO addLoaderTemplate(LoaderTemplateDTO loaderTemplateDTO,
                                             GobiiLoaderPayloadTypes payloadType
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
        Cv templateType = cvDao.getCvs(payloadType.getTerm(),
            CvGroupTerm.CVGROUP_PAYLOADTYPE.getCvGroupName(),
            GobiiCvGroupType.GROUP_TYPE_SYSTEM).get(0);

        loaderTemplate.setTemplateType(templateType);

        loaderTemplate = loaderTemplateDao.create(loaderTemplate);

        ModelMapper.mapEntityToDto(loaderTemplate, loaderTemplateDTO);

        return loaderTemplateDTO;

    }

    @Override
    public MarkerTemplateDTO getMarkerTemplate() throws GobiiException {

        MarkerTemplateDTO markerTemplateDTO = new MarkerTemplateDTO();

        // Get marker properties
        List<Cv> markerProperties = cvDao.getCvListByCvGroup(
            CvGroupTerm.CVGROUP_MARKER_PROP.getCvGroupName(),
            null);

        for(Cv markerProperty : markerProperties) {
            markerTemplateDTO.getMarkerProperties()
                .put(markerProperty.getTerm(), new ArrayList<>());
        }

        return markerTemplateDTO;
    }

    @Override
    public DnaRunTemplateDTO getDnaRunTemplate() throws GobiiException {

        DnaRunTemplateDTO dnaRunTemplateDTO = new DnaRunTemplateDTO();

        // Add dnarun properties
        List<Cv> dnaRunProperties = cvDao.getCvListByCvGroup(
            CvGroupTerm.CVGROUP_DNARUN_PROP.getCvGroupName(),
            null);
        dnaRunProperties.forEach(dnaRunProperty -> dnaRunTemplateDTO
            .getDnaRunProperties()
            .put(dnaRunProperty.getTerm(), new ArrayList<>()));

        // Add dna sample properties
        List<Cv> dnaSampleProperties = cvDao.getCvListByCvGroup(
            CvGroupTerm.CVGROUP_DNASAMPLE_PROP.getCvGroupName(), null);
        dnaSampleProperties.forEach(dnaSampleProperty -> dnaRunTemplateDTO
            .getDnaSampleProperties()
            .put(dnaSampleProperty.getTerm(), new ArrayList<>()));

        // Add germplasm properties
        List<Cv> germplasmProperties = cvDao.getCvListByCvGroup(
            CvGroupTerm.CVGROUP_GERMPLASM_PROP.getCvGroupName(),
            null);
        germplasmProperties.forEach(germplasmProperty -> dnaRunTemplateDTO
            .getGermplasmProperties()
            .put(germplasmProperty.getTerm(), new ArrayList<>()));

        return dnaRunTemplateDTO;
    }


}
