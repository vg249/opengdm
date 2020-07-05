package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.gobiiproject.gobidomain.services.gdmv3.exceptions.DeleteException;
import org.gobiiproject.gobidomain.services.gdmv3.exceptions.EntityDoesNotExistException;
import org.gobiiproject.gobidomain.services.gdmv3.exceptions.InvalidException;
import org.gobiiproject.gobidomain.services.gdmv3.exceptions.UnknownEntityException;
import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.gdmv3.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.CvTypeDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.DatasetDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.DatasetRequestDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Dataset;
import org.gobiiproject.gobiimodel.entity.DnaRun;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.entity.Marker;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiisampletrackingdao.AnalysisDao;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.DatasetDao;
import org.gobiiproject.gobiisampletrackingdao.DnaRunDao;
import org.gobiiproject.gobiisampletrackingdao.ExperimentDao;
import org.gobiiproject.gobiisampletrackingdao.MarkerDao;
import org.springframework.beans.factory.annotation.Autowired;

public class DatasetServiceImpl implements DatasetService {

	@Autowired
	private DatasetDao datasetDao;

	@Autowired
	private ExperimentDao experimentDao;

	@Autowired
	private CvDao cvDao;
	
	@Autowired
	private AnalysisDao analysisDao;

	@Autowired
	private ContactDao contactDao;

	@Autowired
	private MarkerDao markerDao;

	@Autowired
	private DnaRunDao dnaRunDao;
	
	@Transactional
	@Override
	public DatasetDTO createDataset(DatasetRequestDTO request, String user) throws Exception {
		//check if the experiment exists
		Experiment experiment = experimentDao.getExperiment(request.getExperimentId());
		if (
			experiment == null
		) {
			throw new UnknownEntityException("Experiment");
		}

		//check if the calling analysis id exists
		Analysis callingAnalysis = analysisDao.getAnalysis(request.getCallingAnalysisId());
		if (
			callingAnalysis == null
		) {
			throw new UnknownEntityException("Calling Analysis Id");
		}

		//check analysis ids
		List<Analysis> analyses = this.checkAndGetAnalysesFromIds(request.getAnalysisIds());
		
		//check datasetTypeId
		Cv datasetType = null;
		if (
			request.getDatasetTypeId() != null
		) {
			datasetType = this.getDatasetCv(request.getDatasetTypeId());
		}

		//everything is ok
		Dataset dataset = new Dataset();
		dataset.setDatasetName(request.getDatasetName());
		dataset.setExperiment(experiment);
		dataset.setAnalyses(request.getAnalysisIds());
		dataset.setCallingAnalysis(callingAnalysis);
		dataset.setType(datasetType);

		//status item
		Cv newStatus = cvDao.getNewStatus();
		dataset.setStatus(newStatus);

		// audit items
		Contact creator = contactDao.getContactByUsername(user);
		if (creator != null)
			dataset.setCreatedBy(creator.getContactId());
		dataset.setCreatedDate(new java.util.Date());

		Dataset savedDataset = datasetDao.saveDataset(dataset);
		
		DatasetDTO datasetDTO = new DatasetDTO();
		//manual load the analysis DTOs
		if (analyses != null) {
			List<AnalysisDTO> analysisDTOs = this.getAnalysisDTOs(analyses);
			datasetDTO.setAnalyses(analysisDTOs);
		}
		
		ModelMapper.mapEntityToDto(savedDataset, datasetDTO);
		//force set -- TODO:  debug why piContactId and piContactName is null 
		datasetDTO.setPiContactFirstName(savedDataset.getExperiment().getProject().getContact().getFirstName());
		datasetDTO.setPiContactLastName(savedDataset.getExperiment().getProject().getContact().getLastName());
		datasetDTO.setPiContactId(savedDataset.getExperiment().getProject().getContact().getContactId());
		
		return datasetDTO;
	}

	private Cv getDatasetCv(Integer id) throws Exception {
		Cv datasetType = cvDao.getCvByCvId(id);
		
		if (datasetType == null ) {
			throw new UnknownEntityException("Dataset Type Id");
		}

		//check if the cv is a dataset type
		if (datasetType.getCvGroup().getCvGroupName() != CvGroupTerm.CVGROUP_DATASET_TYPE.getCvGroupName()) {
			throw new InvalidException("Dataset Type Id");
		}

		return datasetType;
	}

	@Transactional
	@Override
	public PagedResult<DatasetDTO> getDatasets(Integer page, Integer pageSize, Integer experimentId,
			Integer datasetTypeId)  throws Exception {
		Integer rowOffset = page * pageSize;
		List<Dataset> datasets = datasetDao.getDatasets(pageSize, rowOffset, null, null,  datasetTypeId, experimentId, null);

		List<DatasetDTO> datasetDTOs = new java.util.ArrayList<>();

		for (int i = 0; i < datasets.size(); i++) {
		
			DatasetDTO datasetDTO = new DatasetDTO();
			ModelMapper.mapEntityToDto(datasets.get(i), datasetDTO);
			
			//convert
			List<AnalysisDTO> analysesDTOs = this.getAnalysisDTOs(
				this.checkAndGetAnalysesFromIds(
					datasets.get(i).getAnalyses()
				)
			);
		

			datasetDTO.setAnalyses(analysesDTOs);
			datasetDTOs.add(datasetDTO);
		}

		return PagedResult.createFrom(page, datasetDTOs);
	}

	private List<Analysis> checkAndGetAnalysesFromIds(Integer[] analysisIdArray) throws Exception {
		//check analysis ids
		Set<Integer> analysisIds = new HashSet<>(
			Arrays.asList(
				Optional.ofNullable(analysisIdArray).orElse(
					new Integer[]{}
				)
			)
		);

		List<Analysis> analyses = null;
		if (
			!analysisIds.isEmpty()
		) {
			analyses = analysisDao.getAnalysesByAnalysisIds(analysisIds);
			if (analyses.size() != analysisIds.size()) {
				throw new UnknownEntityException("Analysis Id");
			}
		}
		return analyses;
	}

	private List<AnalysisDTO> getAnalysisDTOs(List<Analysis> analyses) {
		List<AnalysisDTO> analysisDTOs = new ArrayList<>();
		analyses.forEach(analysis -> {
			AnalysisDTO analysisDTO = new AnalysisDTO();
			ModelMapper.mapEntityToDto(analysis, analysisDTO);
			analysisDTOs.add(analysisDTO);
		});
		return analysisDTOs;
	}

	@Transactional
	@Override
	public DatasetDTO getDataset(Integer datasetId) throws Exception {
		Dataset dataset = this.loadDataset(datasetId);
		DatasetDTO datasetDTO = new DatasetDTO();

		ModelMapper.mapEntityToDto(dataset, datasetDTO);
		//set the analyses
		Set<Integer> analysisIds = new HashSet<>(
				Arrays.asList(
					Optional.ofNullable(dataset.getAnalyses()).orElse(new Integer[]{})
				)
			);
		if (analysisIds.size() > 0) {
			//convert
			List<AnalysisDTO> analysesDTOs = this.getAnalysisDTOs(
				analysisDao.getAnalysesByAnalysisIds(analysisIds)
			);

			datasetDTO.setAnalyses(
				analysesDTOs
			);
		}

		return datasetDTO;
	}

	@Transactional
	@Override
	public DatasetDTO updateDataset(Integer datasetId, DatasetRequestDTO request, String user) throws Exception {
		//check if  datasetId exists
		Dataset targetDataset = this.loadDataset(datasetId);

		boolean modified = false;
		//check if datasetName change
		if (!LineUtils.isNullOrEmpty(request.getDatasetName())) {
			targetDataset.setDatasetName(request.getDatasetName());
			modified = true;
		}

		//check if experiment change
		if (request.getExperimentId() != null && request.getExperimentId() > 0) {
			//check if experiment exists
			Experiment experiment = experimentDao.getExperiment(request.getExperimentId());
			if (experiment == null) {
				throw new InvalidException("Experiment");
			}

			targetDataset.setExperiment(experiment);
			modified = true;
		}

		//check if callingAnalysisId
		if (request.getCallingAnalysisId() != null &&  request.getCallingAnalysisId() > 0) {
			Analysis analysis = analysisDao.getAnalysis(request.getCallingAnalysisId());
			if (analysis == null) {
				throw new InvalidException("Calling Analysis");
			}
			targetDataset.setCallingAnalysis(analysis);
			modified = true;
		}

		//check for datasetTypeId
		if (request.getDatasetTypeId() != null && request.getDatasetTypeId() > 0) {
			Cv cv = this.getDatasetCv(request.getDatasetTypeId());
			targetDataset.setType(cv);
			modified = true;
		}

		//check for analysisIds
		if (request.getAnalysisIds() != null && request.getAnalysisIds().length > 0) {
			this.checkAndGetAnalysesFromIds(request.getAnalysisIds());
			targetDataset.setAnalyses(request.getAnalysisIds());
			modified = true;
		}

		Dataset updatedDataset = targetDataset;
		
		if (modified) {
			//audit items
			//status item
			Cv modifiedStatus = cvDao.getModifiedStatus();
			targetDataset.setStatus(modifiedStatus);

			// audit items
			Contact creator = contactDao.getContactByUsername(user);
			if (creator != null)
				targetDataset.setModifiedBy(creator.getContactId());
			targetDataset.setModifiedDate(new java.util.Date());

			updatedDataset = datasetDao.updateDataset(targetDataset);
		} 

		DatasetDTO datasetDTO = new DatasetDTO();
		ModelMapper.mapEntityToDto(updatedDataset, datasetDTO);

		//set the analysis
		if (targetDataset.getAnalyses() != null && targetDataset.getAnalyses().length > 0) {
			datasetDTO.setAnalyses(
				this.getAnalysisDTOs(
					this.checkAndGetAnalysesFromIds(
						targetDataset.getAnalyses()
					)
				)
			);
		}
		return datasetDTO;

	}

	@Transactional
	@Override
	public void deleteDataset(Integer datasetId) throws Exception {
		Dataset dataset = this.loadDataset(datasetId);
		//check run counts
		//check marker 
		List<Marker> markers = markerDao.getMarkersByDatasetId(datasetId, 1, 0);
		if (markers != null && markers.size() > 0) {
			throw new DeleteException();
		}

		//check dnarun
		List<DnaRun> dnaRuns = dnaRunDao.getDnaRunsByDatasetId(datasetId, 1, 0);
		if (dnaRuns != null && dnaRuns.size() > 0) {
			throw new DeleteException();
		}
		datasetDao.deleteDataset(dataset);
	}

	//---Dataset Types
	@Transactional
	@Override
    public PagedResult<CvTypeDTO> getDatasetTypes(Integer page, Integer pageSize) {
        List<Cv> cvs = cvDao.getCvs(null, CvGroupTerm.CVGROUP_DATASET_TYPE.getCvGroupName(), null, page, pageSize);
        List<CvTypeDTO> cvTypeDTOs = new ArrayList<>();

        cvs.forEach(cv -> {
            CvTypeDTO cvTypeDTO = new CvTypeDTO();
            ModelMapper.mapEntityToDto(cv, cvTypeDTO);
            cvTypeDTOs.add(cvTypeDTO);
		});
		
        return PagedResult.createFrom(page, cvTypeDTOs);
    }

	@Transactional
	@Override
	public CvTypeDTO createDatasetType(String datasetTypeName, String datasetTypeDescription, String user) {     
        org.gobiiproject.gobiimodel.entity.CvGroup cvGroup = cvDao.getCvGroupByNameAndType(
            CvGroupTerm.CVGROUP_DATASET_TYPE.getCvGroupName(),
            2 //TODO:  this is custom type
        );
        if (cvGroup == null) throw new GobiiDaoException("Missing CvGroup for Analysis Type");

        Cv cv = new Cv();
        cv.setCvGroup(cvGroup);
		cv.setTerm(datasetTypeName);
		
		if (datasetTypeDescription != null)
        	cv.setDefinition(datasetTypeDescription);

        //get the new row status
        // Get the Cv for status, new row
        Cv status = cvDao.getNewStatus();
        cv.setStatus(status.getCvId());

        //set rank
        cv.setRank(0);
        cv = cvDao.createCv(cv);
        
        CvTypeDTO datasetDTO = new CvTypeDTO();
        ModelMapper.mapEntityToDto(cv, datasetDTO);
        return datasetDTO;

	}
	
	private Dataset loadDataset(Integer datasetId) throws EntityDoesNotExistException {
		Dataset dataset = datasetDao.getDataset(datasetId);
		if (dataset == null) {
			throw new EntityDoesNotExistException.Dataset();
		}
		return dataset;
	}

}