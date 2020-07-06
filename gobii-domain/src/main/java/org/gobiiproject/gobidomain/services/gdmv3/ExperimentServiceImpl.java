/**
 * ExperimentServiceImpl.java
 * 
 * @author Rodolfo N. Duldulao, Jr.
 * @since 2020-03-28
 */

package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.gobiiproject.gobidomain.services.gdmv3.exceptions.EntityDoesNotExistException;
import org.gobiiproject.gobidomain.services.gdmv3.exceptions.UnknownEntityException;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.gdmv3.ExperimentDTO;
import org.gobiiproject.gobiimodel.dto.request.ExperimentPatchRequest;
import org.gobiiproject.gobiimodel.dto.request.ExperimentRequest;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.entity.Platform;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.entity.VendorProtocol;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
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

    @Autowired
    private CvDao cvDao;

    @Transactional
    @Override
    public PagedResult<ExperimentDTO> getExperiments(Integer page, Integer pageSize, Integer projectId) {
        List<Experiment> experiments = experimentDao.getExperiments(pageSize, page * pageSize, projectId);
        List<ExperimentDTO> experimentDTOs = new ArrayList<>();

        experiments.forEach(experiment -> {
            ExperimentDTO experimentDTO = new ExperimentDTO();
            ModelMapper.mapEntityToDto(experiment, experimentDTO);
            experimentDTOs.add(experimentDTO);
        });

        return PagedResult.createFrom(page, experimentDTOs);
    }

    @Transactional
    @Override
    public ExperimentDTO getExperiment(Integer experimentId) throws Exception {
        Experiment experiment = this.loadExperiment(experimentId);
        ExperimentDTO experimentDTO = new ExperimentDTO();
        ModelMapper.mapEntityToDto(experiment, experimentDTO);
        return experimentDTO;
    }

    @Transactional
    @Override
    public ExperimentDTO createExperiment(ExperimentRequest request, String createdBy) throws Exception {
        Project project =this.loadProject(request.getProjectId());
        VendorProtocol vp = this.loadVendorProtocol(request.getVendorProtocolId());
    
        // get contact info
        Contact contact = contactDao.getContactByUsername(createdBy);

        Experiment experiment = new Experiment();
        experiment.setExperimentName(request.getExperimentName());
        experiment.setProject(project);
        experiment.setVendorProtocol(vp);

        //set code 
        String code = String.format(
            "%s_%d",
            request.getExperimentName().replaceAll("\\s+", "_"),
            project.getProjectId()
        );

        experiment.setExperimentCode(code);

        //set status
        // Get the Cv for status, new row
        Cv cv = cvDao.getNewStatus();
        experiment.setStatus(cv);

        //audit elements
        experiment.setCreatedBy(contact.getContactId());
        experiment.setCreatedDate(new java.util.Date());
        
        experiment = experimentDao.createExperiment(experiment);
        
        ExperimentDTO experimentDTO = new ExperimentDTO();
        ModelMapper.mapEntityToDto(experiment, experimentDTO);
        
        // //TODO: debug this, why is the mapper failing at mapping subobject
        // if ((experimentDTO.getPlatformId() == null || experimentDTO.getPlatformName() == null) && 
        //      experiment.getVendorProtocol() != null && 
        //      experiment.getVendorProtocol().getProtocol() != null && 
        //      experiment.getVendorProtocol().getProtocol().getPlatform() != null
        //    ) {
        //     Platform platform = experiment.getVendorProtocol().getProtocol().getPlatform();

        //     experimentDTO.setPlatformId(platform.getPlatformId());
        //     experimentDTO.setPlatformName(platform.getPlatformName());
        // }
        Platform platform  = Optional.ofNullable(experiment.getVendorProtocol())
                                     .map(v -> v.getProtocol())
                                     .map(p -> p.getPlatform())
                                     .orElse(null);
        experimentDTO.setPlatformId(Optional.ofNullable(platform).map(p -> p.getPlatformId()).orElse(null));
        experimentDTO.setPlatformName(Optional.ofNullable(platform).map(p -> p.getPlatformName()).orElse(null));
        return experimentDTO;
    }

    @Transactional
    @Override
    public ExperimentDTO updateExperiment(Integer experimentId, ExperimentPatchRequest request, String updatedBy) throws Exception {
        Experiment target = this.loadExperiment(experimentId);

        if (request.getProjectId() != null) {
            Project project = this.loadProject(request.getProjectId());
            target.setProject(project);
        }

        if (!LineUtils.isNullOrEmpty(request.getExperimentName())) {
            target.setExperimentName(request.getExperimentName());
        }

        if (request.getVendorProtocolId() != null ) {
            VendorProtocol vp = this.loadVendorProtocol(request.getVendorProtocolId());
            target.setVendorProtocol(vp);
        }

        Cv cv = cvDao.getModifiedStatus();
        target.setStatus(cv);
        // get contact info
        Contact contact = contactDao.getContactByUsername(updatedBy);
        target.setModifiedBy(contact.getContactId());
        target.setModifiedDate(new java.util.Date());

        experimentDao.updateExperiment(target);
        return this.getExperiment(target.getExperimentId());
    }

    @Transactional
    @Override
    public void deleteExperiment(Integer experimentId) throws Exception {
        Experiment experiment =  this.loadExperiment(experimentId);
        experimentDao.deleteExperiment(experiment);

    }

    private Experiment loadExperiment(Integer experimentId) throws Exception {
        return Optional.ofNullable( experimentDao.getExperiment(experimentId))
                       .orElseThrow(() -> new EntityDoesNotExistException.Experiment());

    }

    private Project loadProject(Integer projectId) throws Exception {
        return Optional.ofNullable(projectDao.getProject(projectId))
                       .orElseThrow(() -> new UnknownEntityException.Project());
    }

    private VendorProtocol loadVendorProtocol(Integer vendorProtocolId) throws Exception {
        return Optional.ofNullable(experimentDao.getVendorProtocol(vendorProtocolId))
                       .orElseThrow(() -> new UnknownEntityException.VendorProtocol());
        
    }
    
}