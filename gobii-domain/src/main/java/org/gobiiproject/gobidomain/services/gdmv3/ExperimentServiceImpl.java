/**
 * ExperimentServiceImpl.java
 * 
 * @author Rodolfo N. Duldulao, Jr.
 * @since 2020-03-28
 */

package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.dto.gdmv3.ExperimentDTO;
import org.gobiiproject.gobiimodel.dto.request.ExperimentPatchRequest;
import org.gobiiproject.gobiimodel.dto.request.ExperimentRequest;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.entity.VendorProtocol;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.ExperimentDao;
import org.gobiiproject.gobiisampletrackingdao.ProjectDao;
import org.springframework.beans.factory.annotation.Autowired;

public class ExperimentServiceImpl implements ExperimentService {

    @Autowired
    private ExperimentDao experimentDao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private ContactDao contactDao;

    @Transactional
    @Override
    public PagedResult<ExperimentDTO> getExperiments(Integer page, Integer pageSize, Integer projectId) {
        // TODO Auto-generated method stub
        List<Experiment> experiments = experimentDao.getExperiments(pageSize, page * pageSize, projectId);

        List<ExperimentDTO> dtos = new ArrayList<>();

        experiments.forEach(experiment -> {
            ExperimentDTO dto = new ExperimentDTO();
            ModelMapper.mapEntityToDto(experiment, dto);
            dtos.add(dto);
        });

        PagedResult<ExperimentDTO> pagedResult = new PagedResult<>();
        pagedResult.setResult(dtos);
        pagedResult.setCurrentPageNum(page);
        pagedResult.setCurrentPageSize(pageSize);

        return pagedResult;
    }

    @Transactional
    @Override
    public ExperimentDTO getExperiment(Integer i) throws Exception {
        Experiment experiment = experimentDao.getExperiment(i);
        if (experiment == null)
            throw new NullPointerException("Experiment does not exist");
        ExperimentDTO dto = new ExperimentDTO();
        ModelMapper.mapEntityToDto(experiment, dto);
        return dto;
    }

    @Transactional
    @Override
    public ExperimentDTO createExperiment(ExperimentRequest request, String createdBy) throws Exception {
        Project project = projectDao.getProject(request.getProjectId());
        if (project == null) {
            throw new GobiiDaoException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                    "Unknown project");
        }

        VendorProtocol vp = experimentDao.getVendorProtocol(request.getVendorProtocolId());
        if (vp == null) {
            throw new GobiiDaoException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                    "Unknown vendor protocol");
        }

        // get contact info
        Contact contact = contactDao.getContactByUsername(createdBy);

        Experiment experiment = new Experiment();
        experiment.setExperimentName(request.getExperimentName());
        experiment.setProject(project);
        experiment.setVendorProtocol(vp);

        experiment.setCreatedBy(contact.getContactId());
        experiment.setCreatedDate(new java.util.Date());

        experiment = experimentDao.createExperiment(experiment);

        ExperimentDTO dto = new ExperimentDTO();
        ModelMapper.mapEntityToDto(experiment, dto);
        return dto;
    }

    @Transactional
    @Override
    public ExperimentDTO updateExperiment(Integer experimentId, ExperimentPatchRequest request, String updatedBy) throws Exception {
        Experiment target = experimentDao.getExperiment(experimentId);
        if (target == null) {
            throw new GobiiDaoException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                "Unknown experiment"
            );
        }

        if (request.getProjectId() != null) {
            Project project = projectDao.getProject(request.getProjectId());
            if (project == null) {
                throw new GobiiDaoException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.BAD_REQUEST,
                    "Unknown project"
                );
            }
            target.setProject(project);
        }

        if (!LineUtils.isNullOrEmpty(request.getExperimentName())) {
            target.setExperimentName(request.getExperimentName());
        }

        if (request.getVendorProtocolId() != null ) {
            VendorProtocol vp = experimentDao.getVendorProtocol(request.getVendorProtocolId());
            if (vp == null) {
                throw new GobiiDaoException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.BAD_REQUEST,
                   "Unknown vendor protocol"
                );
            }
            target.setVendorProtocol(vp);
        }

        // get contact info
        Contact contact = contactDao.getContactByUsername(updatedBy);
        target.setModifiedBy(contact.getContactId());
        target.setModifiedDate(new java.util.Date());

        experimentDao.updateExperiment(target);
        return this.getExperiment(target.getExperimentId());
    }
    
}