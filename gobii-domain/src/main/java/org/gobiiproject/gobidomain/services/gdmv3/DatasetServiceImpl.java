package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.gdmv3.AnalysisDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.DatasetDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.DatasetRequestDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.DatasetTypeDTO;
import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.entity.Contact;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Dataset;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.modelmapper.ModelMapper;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiisampletrackingdao.AnalysisDao;
import org.gobiiproject.gobiisampletrackingdao.ContactDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.DatasetDao;
import org.gobiiproject.gobiisampletrackingdao.ExperimentDao;
import org.gobiiproject.gobiidao.GobiiDaoException;
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
	
	@Transactional
	@Override
	public DatasetDTO createDataset(DatasetRequestDTO request, String user) throws Exception {
		//check if the experiment exists
		Experiment experiment = experimentDao.getExperiment(request.getExperimentId());
		if (
			experiment == null
		) {
			throw new GobiiDaoException(
				GobiiStatusLevel.ERROR,
				GobiiValidationStatusType.BAD_REQUEST,
				"Unknown experiment"
			);
		}

		//check if the calling analysis id exists
		Analysis callingAnalysis = analysisDao.getAnalysis(request.getCallingAnalysisId());
		if (
			callingAnalysis == null
		) {
			throw new GobiiDaoException(
				GobiiStatusLevel.ERROR,
				GobiiValidationStatusType.BAD_REQUEST,
				"Unknown calling analysis id"
			);
		}

		//check analysis ids
		Set<Integer> analysisIds = new HashSet<>(
			Arrays.asList(
				Optional.ofNullable(request.getAnalysisIds()).orElse(
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
				throw new GobiiDaoException(
					GobiiStatusLevel.ERROR,
					GobiiValidationStatusType.BAD_REQUEST,
					"Unknown analysis id"
				);
			}
		}

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

		//audit items
		// audit items
		Contact creator = contactDao.getContactByUsername(user);
		if (creator != null)
			dataset.setCreatedBy(creator.getContactId());
		dataset.setCreatedDate(new java.util.Date());

		Dataset savedDataset = datasetDao.saveDataset(dataset);
		
		DatasetDTO datasetDTO = new DatasetDTO();
		//manual load the analysis DTOs
		if (analyses != null) {
			List<AnalysisDTO> analysisDTOs = new ArrayList<>();
			analyses.forEach(analysis -> {
				AnalysisDTO analysisDTO = new AnalysisDTO();
				ModelMapper.mapEntityToDto(analysis, analysisDTO);
				analysisDTOs.add(analysisDTO);
			});
			datasetDTO.setAnalyses(analysisDTOs);
		}
		

		ModelMapper.mapEntityToDto(savedDataset, datasetDTO);
		
		return datasetDTO;
	}

	private Cv getDatasetCv(Integer id) throws Exception {
		Cv datasetType = cvDao.getCvByCvId(id);
		
		if (datasetType == null ) {
			throw new GobiiDaoException(
				GobiiStatusLevel.ERROR,
				GobiiValidationStatusType.BAD_REQUEST,
				"Unknown dataset type id"
			);
		}

		//check if the cv is a dataset type
		if (datasetType.getCvGroup().getCvGroupName() != CvGroup.CVGROUP_DATASET_TYPE.getCvGroupName()) {
			throw new GobiiDaoException(
				GobiiStatusLevel.ERROR,
				GobiiValidationStatusType.BAD_REQUEST,
				"Invalid dataset type id"
			);
		}

		return datasetType;
	}

}