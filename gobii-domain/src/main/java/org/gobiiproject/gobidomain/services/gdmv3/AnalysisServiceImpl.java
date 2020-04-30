package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.gdmv3.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.AnalysisTypeDTO;
import org.gobiiproject.gobiimodel.dto.request.AnalysisTypeRequest;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Reference;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiisampletrackingdao.AnalysisDao;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.DatasetDao;
import org.gobiiproject.gobiisampletrackingdao.ReferenceDao;
import org.springframework.beans.factory.annotation.Autowired;

public class AnalysisServiceImpl implements AnalysisService {

    @Autowired
    private AnalysisDao analysisDao;

    @Autowired
    private CvDao cvDao;

    @Autowired
    private ReferenceDao referenceDao;

    @Autowired
    private ContactDao contactDao;

    @Autowired
    private DatasetDao datasetDao;

    @Transactional
    @Override
    public PagedResult<AnalysisDTO> getAnalyses(Integer page, Integer pageSize) throws Exception {
        List<Analysis> analyses = analysisDao.getAnalyses(page * pageSize, pageSize);
        List<AnalysisDTO> analysisDTOs = new ArrayList<>();

        analyses.forEach((analysis) -> {
            AnalysisDTO analysisDTO = new AnalysisDTO();
            ModelMapper.mapEntityToDto(analysis, analysisDTO);
            analysisDTOs.add(analysisDTO);
        });

        PagedResult<AnalysisDTO> result = new PagedResult<>();
        result.setCurrentPageNum(page);
        result.setCurrentPageSize(analyses.size());
        result.setResult(analysisDTOs);
        return result;
    }

    @Transactional
    @Override
    public AnalysisDTO createAnalysis(AnalysisDTO analysisRequest, String user) throws Exception {
        Analysis analysis = new Analysis();
        // Get analysis type
        Cv analysisType = cvDao.getCvByCvId(analysisRequest.getAnalysisTypeId());
        if (analysisType == null) {
            throw new GobiiDaoException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                    "Unknown analysis type");
        }

        if (!analysisType.getCvGroup().getCvGroupName().equals(CvGroup.CVGROUP_ANALYSIS_TYPE.getCvGroupName())) {
            throw new GobiiDaoException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                    "Invalid analysis type id");
        }
        analysis.setType(analysisType);

        // TODO check if the reference Id

        if (analysisRequest.getReferenceId() != null) {
            Reference reference = referenceDao.getReference(analysisRequest.getReferenceId());
            if (reference == null)
                throw new GobiiDaoException(GobiiStatusLevel.ERROR, GobiiValidationStatusType.BAD_REQUEST,
                        "Unknown reference");
            analysis.setReference(reference);
        }

        //get new status
        Cv cv = cvDao.getNewStatus();       
        analysis.setStatus(cv);

        ModelMapper.mapDtoToEntity(analysisRequest, analysis, true);

        // audit items
        Contact creator = contactDao.getContactByUsername(user);
        if (creator != null)
            analysis.setCreatedBy(creator.getContactId());
        analysis.setCreatedDate(new java.util.Date());

        try {
            analysis = analysisDao.createAnalysis(analysis);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        AnalysisDTO analysisDTO = new AnalysisDTO();

        ModelMapper.mapEntityToDto(analysis, analysisDTO);
        return analysisDTO;
    }

    @Transactional
    @Override
    public AnalysisTypeDTO createAnalysisType(AnalysisTypeRequest analysisTypeRequest, String creatorId) throws Exception {
        
        org.gobiiproject.gobiimodel.entity.CvGroup cvGroup = cvDao.getCvGroupByNameAndType(
            CvGroup.CVGROUP_ANALYSIS_TYPE.getCvGroupName(),
            2 //TODO:  this is custom type
        );
        if (cvGroup == null) throw new GobiiDaoException("Missing CvGroup for Analysis Type");

        Cv cv = new Cv();
        cv.setCvGroup(cvGroup);
        cv.setTerm(analysisTypeRequest.getAnalysisTypeName());
        cv.setDefinition(analysisTypeRequest.getAnalysisTypeDescription());

        //get the new row status
        // Get the Cv for status, new row
        Cv status = cvDao.getNewStatus();
        cv.setStatus(status.getCvId());

        //set rank
        cv.setRank(0);
        cv = cvDao.createCv(cv);
        
        AnalysisTypeDTO analysisDTO = new AnalysisTypeDTO();
        ModelMapper.mapEntityToDto(cv, analysisDTO);
        return analysisDTO;

    }

    @Override
    public PagedResult<AnalysisTypeDTO> getAnalysisTypes(Integer page, Integer pageSize) {
        List<Cv> cvs = cvDao.getCvs(null, CvGroup.CVGROUP_ANALYSIS_TYPE.getCvGroupName(), null, page, pageSize);
        List<AnalysisTypeDTO> analysisTypeDTOs = new ArrayList<>();

        cvs.forEach(cv -> {
            AnalysisTypeDTO analysisTypeDTO = new AnalysisTypeDTO();
            ModelMapper.mapEntityToDto(cv, analysisTypeDTO);
            analysisTypeDTOs.add(analysisTypeDTO);
        });
        PagedResult<AnalysisTypeDTO> result = new PagedResult<>();
        result.setCurrentPageNum(page);
        result.setCurrentPageSize(analysisTypeDTOs.size());
        result.setResult(analysisTypeDTOs);

        return result;
    }

    @Transactional
    @Override
    public AnalysisDTO updateAnalysis(Integer id, AnalysisDTO analysisDTO, String updatedBy) throws Exception {
        Analysis analysis = analysisDao.getAnalysis(id);
        if (analysis == null) {
            throw new GobiiDaoException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.BAD_REQUEST,
                "Analysis not found"
            );
        }

        if (analysisDTO.getReferenceId() != null) {
            //check if the reference exists
            Reference reference = referenceDao.getReference(analysisDTO.getReferenceId());
            if (reference == null) {
                throw new GobiiDaoException(
                    GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.BAD_REQUEST,
                    "Unknown reference"
                );
            }
            analysis.setReference(reference);
        }

        ModelMapper.mapDtoToEntity(analysisDTO, analysis, true); //ignore the nulls

        // audit items
        Contact updater = contactDao.getContactByUsername(updatedBy);
        if (updater != null)
            analysis.setModifiedBy(updater.getContactId());
        analysis.setModifiedDate(new java.util.Date());

        Cv cv = cvDao.getModifiedStatus();
        analysis.setStatus(cv);

        Analysis updatedAnalysis = analysisDao.updateAnalysis(analysis);
    
        AnalysisDTO updatedAnalysisDTO = new AnalysisDTO();
        ModelMapper.mapEntityToDto(updatedAnalysis, updatedAnalysisDTO);
        return updatedAnalysisDTO;
    }

    @Override
    public AnalysisDTO getAnalysis(Integer analysisId) throws Exception {  
        Analysis analysis = analysisDao.getAnalysis(analysisId);
        if (analysis == null) {
            throw new GobiiDaoException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                "Analysis not found"
            );
        }
        AnalysisDTO analysisDTO = new AnalysisDTO();
        ModelMapper.mapEntityToDto(analysis, analysisDTO);
        return analysisDTO;
    }

    @Transactional
    @Override
    public void deleteAnalysis(Integer analysisId) throws Exception {
        //Check if analysis exists
        Analysis analysis = analysisDao.getAnalysis(analysisId);
        if (analysis == null) {
            throw new GobiiDaoException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                "Analysis not found"
            );
        }

        //check for data sets calling analysis or analyses column
        if (
            datasetDao.getDatasetCountByAnalysisId(analysisId) > 0 ||
            datasetDao.getDatasetCountWithAnalysesContaining(analysisId) > 0
        ) {
            throw new GobiiDaoException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.FOREIGN_KEY_VIOLATION,
                "Associated resources found. Cannot complete the action unless they are deleted."
            );
        }

       

        analysisDao.deleteAnalysis(analysis);
    }
}