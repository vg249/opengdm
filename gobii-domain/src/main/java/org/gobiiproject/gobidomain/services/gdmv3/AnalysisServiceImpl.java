package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.gdmv3.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.AnalysisTypeDTO;
import org.gobiiproject.gobiimodel.dto.request.AnalysisRequest;
import org.gobiiproject.gobiimodel.dto.request.AnalysisTypeRequest;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Reference;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiisampletrackingdao.AnalysisDao;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
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

    @Transactional
    @Override
    public PagedResult<AnalysisDTO> getAnalyses(Integer page, Integer pageSize) throws Exception {
        List<Analysis> analyses = analysisDao.getAnalyses(page * pageSize, pageSize);
        List<AnalysisDTO> dtos = new ArrayList<>();

        analyses.forEach((analysis) -> {
            AnalysisDTO dto = new AnalysisDTO();
            ModelMapper.mapEntityToDto(analysis, dto);
            dtos.add(dto);
        });

        PagedResult<AnalysisDTO> result = new PagedResult<>();
        result.setCurrentPageNum(page);
        result.setCurrentPageSize(analyses.size());
        result.setResult(dtos);
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

        // set status
        List<Cv> cvList = cvDao.getCvs("new", CvGroup.CVGROUP_STATUS.getCvGroupName(),
                GobiiCvGroupType.GROUP_TYPE_SYSTEM);

        Cv cv = null;
        if (!cvList.isEmpty()) {
            cv = cvList.get(0);
        }

        analysis.setStatus(cv);

        ModelMapper.mapDtoToEntity(analysisRequest, analysis);

        // audit items
        Contact creator = contactDao.getContactByUsername(user);
        if (creator != null)
            analysis.setCreatedBy(creator.getContactId());
        analysis.setCreatedDate(new java.util.Date());

        analysis = analysisDao.createAnalysis(analysis);

        AnalysisDTO dto = new AnalysisDTO();

        ModelMapper.mapEntityToDto(analysis, dto);
        return dto;
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
        List<Cv> cvList = cvDao.getCvs("new", CvGroup.CVGROUP_STATUS.getCvGroupName(),
                GobiiCvGroupType.GROUP_TYPE_SYSTEM);

        Cv status = null;
        if (!cvList.isEmpty()) {
            status = cvList.get(0);
        }
        cv.setStatus(status.getCvId());

        //set rank
        cv.setRank(0);
        cv = cvDao.createCv(cv);
        
        AnalysisTypeDTO dto = new AnalysisTypeDTO();
        ModelMapper.mapEntityToDto(cv, dto);
        return dto;

    }

    @Override
    public PagedResult<AnalysisTypeDTO> getAnalysisTypes(Integer page, Integer pageSize) {
        List<Cv> cvs = cvDao.getCvs(null, CvGroup.CVGROUP_ANALYSIS_TYPE.getCvGroupName(), null, page, pageSize);
        List<AnalysisTypeDTO> dtos = new ArrayList<>();

        cvs.forEach(cv -> {
            AnalysisTypeDTO dto = new AnalysisTypeDTO();
            ModelMapper.mapEntityToDto(cv, dto);
            dtos.add(dto);
        });
        PagedResult<AnalysisTypeDTO> result = new PagedResult<>();
        result.setCurrentPageNum(page);
        result.setCurrentPageSize(dtos.size());
        result.setResult(dtos);

        return result;
    }
}