package org.gobiiproject.gobidomain.services.impl.sampletracking;

import org.apache.commons.io.FileUtils;
import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobidomain.services.ContactService;
import org.gobiiproject.gobidomain.services.ExperimentService;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.entity.auditable.sampletracking.ExperimentDTO;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.ExperimentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ExperimentServiceImpl implements ExperimentService<ExperimentDTO> {

    Logger LOGGER = LoggerFactory.getLogger(ExperimentServiceImpl.class);

    @Autowired
    private ContactService contactService;

    @Autowired
    private ExperimentDao experimentDao;

    @Autowired
    private CvDao cvDao;


    @Override
    public ExperimentDTO createExperiment(ExperimentDTO newExperimentDTO) throws GobiiDomainException {

        ExperimentDTO returnVal = new ExperimentDTO();

        try {

            Experiment newExperiment = new Experiment();

            ModelMapper.mapDtoToEntity(newExperimentDTO, newExperiment);

            //Setting created by contactId
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();

            Integer contactId = this.contactService.getContactByUserName(userName).getContactId();

            newExperiment.setCreatedBy(contactId);

            //Setting created date
            newExperiment.setCreatedDate(new Date(new Date().getTime()));

            //Experiment Code is a non null field in experiment entity
            newExperiment.setExperimentCode(newExperimentDTO.getExperimentName());

            // Set the Status of the experiment as newly created by getting it respective cvId
            List<Cv> statusCvList = cvDao.getCvsByCvTermAndCvGroup(
                    "new", CvGroup.CVGROUP_STATUS.getCvGroupName(),
                    GobiiCvGroupType.GROUP_TYPE_SYSTEM);


            //As CV term is unique under its CV group, there should be only
            //one cv for term "new" under group "status"
            if(statusCvList.size() > 0) {
                Cv statusCv = statusCvList.get(0);
                newExperiment.setExperimentStatus(statusCv.getCvId());
            }

            Integer newExperimentId = experimentDao.createExperiment(newExperiment);

            returnVal.setExperimentId(newExperimentId);

        }
        catch(GobiiException gE) {

            LOGGER.error(gE.getMessage(), gE.getMessage());

            throw new GobiiDomainException(
                    gE.getGobiiStatusLevel(),
                    gE.getGobiiValidationStatusType(),
                    gE.getMessage());

        }
        catch(Exception e) {

            LOGGER.error("Gobii service error", e);

            throw new GobiiDomainException(e);

        }

        return returnVal;
    }


    @Override
    public ExperimentDTO replaceExperiment(Integer experimentId, ExperimentDTO experimentDTO) throws GobiiDomainException {
        return experimentDTO;
    }

    @Override
    public List<ExperimentDTO> getExperiments() throws GobiiDomainException {
        List<ExperimentDTO> returnVal = new ArrayList<>();


        return returnVal;
    }

    @Override
    public ExperimentDTO getExperimentById(Integer experimentId) throws GobiiDomainException {

        ExperimentDTO returnVal = new ExperimentDTO();


        return returnVal;

    }

    @Override
    public List<ExperimentDTO> getExperimentsByProjectIdForLoadedDatasets(Integer projectId) throws GobiiDomainException {
        List<ExperimentDTO> returnVal = null;

        return returnVal;
    }

}
