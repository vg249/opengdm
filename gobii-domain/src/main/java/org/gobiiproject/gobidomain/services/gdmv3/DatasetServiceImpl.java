package org.gobiiproject.gobidomain.services.gdmv3;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.gdmv3.DatasetDTO;
import org.gobiiproject.gobiimodel.dto.gdmv3.DatasetRequestDTO;
import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Dataset;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiisampletrackingdao.AnalysisDao;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.DatasetDao;
import org.gobiiproject.gobiisampletrackingdao.ExperimentDao;
import org.gobiiproject.gobiisampletrackingdao.GobiiDaoException;
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
		
		if (
			!analysisIds.isEmpty()
		) {
			List<Analysis> analyses = analysisDao.getAnalysesByAnalysisIds(analysisIds);
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
			datasetType = cvDao.getCvByCvId(request.getDatasetTypeId()) ;
			if (datasetType == null) {
				throw new GobiiDaoException(
					GobiiStatusLevel.ERROR,
					GobiiValidationStatusType.BAD_REQUEST,
					"Unknown dataset type"
				);
			}

			//check if cv is the right type
			if (
				datasetType.getCvGroup().getCvGroupName() != CvGroup.CVGROUP_DATASET_TYPE.getCvGroupName()
			) {
				throw new GobiiDaoException(
					GobiiStatusLevel.ERROR,
					GobiiValidationStatusType.BAD_REQUEST,
					"Incorrect type group name"
				);
			}
		}

		//everything is ok
		Dataset dataset = new Dataset();
		dataset.setDatasetName(request.getDatasetName());
		dataset.setExperiment(experiment);
		dataset.setAnalyses(request.getAnalysisIds());
		dataset.setCallingAnalysis(callingAnalysis);
		dataset.setType(datasetType);

		//status item
		
		return null;
	}

}