package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.resultset.access.*;
import org.gobiiproject.gobiidtomapping.DtoMapNameIdList;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.metamodel.EntityType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phil on 4/6/2016.
 */
public class DtoMapNameIdListImpl implements DtoMapNameIdList {

	Logger LOGGER = LoggerFactory.getLogger(DtoMapNameIdListImpl.class);


	@Autowired
	private RsAnalysisDao rsAnalysisDao = null;

	@Autowired
	private RsContactDao rsContactDao = null;

	@Autowired
	private RsProjectDao rsProjectDao = null;

	@Autowired
	private RsPlatformDao rsPlatformDao = null;

	@Autowired
	private RsReferenceDao rsReferenceDao = null;
	
	@Autowired
	private RsMapDao rsMapDao = null;

	@Autowired
	private RsExperimentDao rsExperimentDao = null;

	@Autowired
	private RsManifestDao rsManifestDao = null;

	@Autowired
	private RsDataSetDao rsDataSetDao = null;
	
	private NameIdListDTO getNameIdListForAnalysis(NameIdListDTO nameIdListDTO) {

		NameIdListDTO returnVal = new NameIdListDTO();

		try {

			ResultSet resultSet =rsAnalysisDao.getAnalysisNames();
			Map<String, String> analysisNamesById = new HashMap<>();
			while (resultSet.next()) {

				Integer analysisId = resultSet.getInt("analysis_id");
				String name = resultSet.getString("name");
				analysisNamesById.put(analysisId.toString(), name);
			}


			returnVal.setNamesById(analysisNamesById);


		} catch (SQLException e) {
			returnVal.getDtoHeaderResponse().addException(e);
			LOGGER.error(e.getMessage());
		}

		return returnVal;
	}


	private NameIdListDTO getNameIdListForContacts(NameIdListDTO nameIdListDTO) {
		NameIdListDTO returnVal = new NameIdListDTO();

		try {

			ResultSet contactList = rsContactDao.getContactNamesForRoleName(nameIdListDTO.getFilter());

			Map<String, String> contactNamesById = new HashMap<>();
			while (contactList.next()) {

				Integer contactId = contactList.getInt("contact_id");
				String lastName = contactList.getString("lastname");
				String firstName = contactList.getString("firstname");
				String name = lastName + ", " + firstName;
				contactNamesById.put(contactId.toString(), name);
			}

			returnVal.setNamesById(contactNamesById);

		} catch (SQLException e) {
			returnVal.getDtoHeaderResponse().addException(e);
			LOGGER.error(e.getMessage());
		} catch (GobiiDaoException e) {
			returnVal.getDtoHeaderResponse().addException(e);
			LOGGER.error(e.getMessage());
		}


		return (returnVal);

	} // getNameIdListForContacts()

	private NameIdListDTO getNameIdListForPlatforms(NameIdListDTO nameIdListDTO) {

		NameIdListDTO returnVal = new NameIdListDTO();

		try {

			ResultSet resultSet = rsPlatformDao.getPlatformNames();
			Map<String, String> platformNamesById = new HashMap<>();
			while (resultSet.next()) {

				Integer platformId = resultSet.getInt("platform_id");
				String platformName = resultSet.getString("name");
				platformNamesById.put(platformId.toString(), platformName);
			}


			returnVal.setNamesById(platformNamesById);


		} catch (SQLException e) {
			returnVal.getDtoHeaderResponse().addException(e);
			LOGGER.error(e.getMessage());
		} catch (GobiiDaoException e) {
			returnVal.getDtoHeaderResponse().addException(e);
			LOGGER.error(e.getMessage());
		}

		return returnVal;
	}
	
	private NameIdListDTO getNameIdListForReference(NameIdListDTO nameIdListDTO) {

		NameIdListDTO returnVal = new NameIdListDTO();

		try {

			ResultSet resultSet = rsReferenceDao.getReferenceNames();
			Map<String, String> referenceNamesById = new HashMap<>();
			while (resultSet.next()) {

				Integer referenceId = resultSet.getInt("reference_id");
				String referenceName = resultSet.getString("name");
				referenceNamesById.put(referenceId.toString(), referenceName);
			}


			returnVal.setNamesById(referenceNamesById);


		} catch (SQLException e) {
			returnVal.getDtoHeaderResponse().addException(e);
			LOGGER.error(e.getMessage());
		} catch (GobiiDaoException e) {
			returnVal.getDtoHeaderResponse().addException(e);
			LOGGER.error(e.getMessage());
		}

		return returnVal;
	}
	
	private NameIdListDTO getNameIdListForMap(NameIdListDTO nameIdListDTO) {

		NameIdListDTO returnVal = new NameIdListDTO();

		try {

			ResultSet resultSet = rsMapDao.getMapNames();
			Map<String, String> mapNamesById = new HashMap<>();
			while (resultSet.next()) {

				Integer mapId = resultSet.getInt("map_id");
				String mapName = resultSet.getString("name");
				mapNamesById.put(mapId.toString(), mapName);
			}

			returnVal.setNamesById(mapNamesById);


		} catch (SQLException e) {
			returnVal.getDtoHeaderResponse().addException(e);
			LOGGER.error(e.getMessage());
		} catch (GobiiDaoException e) {
			returnVal.getDtoHeaderResponse().addException(e);
			LOGGER.error(e.getMessage());
		}

		return returnVal;
	}
	private NameIdListDTO getNameIdListForManifest(NameIdListDTO nameIdListDTO) {

		NameIdListDTO returnVal = new NameIdListDTO();

		try {

			ResultSet resultSet = rsManifestDao.getManifestNames();
			Map<String, String> manifestNamesById = new HashMap<>();
			while (resultSet.next()) {

				Integer manifestId = resultSet.getInt("manifest_id");
				String manifestName = resultSet.getString("name");
				manifestNamesById.put(manifestId.toString(), manifestName);
			}


			returnVal.setNamesById(manifestNamesById);


		} catch (SQLException e) {
			returnVal.getDtoHeaderResponse().addException(e);
			LOGGER.error(e.getMessage());
		} catch (GobiiDaoException e) {
			returnVal.getDtoHeaderResponse().addException(e);
			LOGGER.error(e.getMessage());
		}

		return returnVal;
	}//getNameIdListForManifest

	private NameIdListDTO getNameIdListForProjects(NameIdListDTO nameIdListDTO) {

		NameIdListDTO returnVal = new NameIdListDTO();

		try {

			ResultSet resultSet = rsProjectDao.getProjectNamesForContactId(Integer.parseInt(nameIdListDTO.getFilter()));

			Map<String, String> projectNameIdList = new HashMap<>();

			while (resultSet.next()) {
				Integer projectId = resultSet.getInt("project_id");
				String name = resultSet.getString("name").toString();
				projectNameIdList.put(projectId.toString(), name);
			}

			returnVal.setNamesById(projectNameIdList);
		} catch (SQLException e) {
			returnVal.getDtoHeaderResponse().addException(e);
			LOGGER.error(e.getMessage());
		} catch (GobiiDaoException e) {
			returnVal.getDtoHeaderResponse().addException(e);
			LOGGER.error(e.getMessage());
		}

		return returnVal;

	} // getNameIdListForContacts()

	private NameIdListDTO getNameIdListForExperiment(NameIdListDTO nameIdListDTO) {

		NameIdListDTO returnVal = new NameIdListDTO();

		try {

			ResultSet resultSet = rsExperimentDao.getExperimentNamesByProjectId(Integer.parseInt(nameIdListDTO.getFilter()));

			Map<String, String> experimentNameIdList = new HashMap<>();

			while (resultSet.next()) {
				Integer experimentId = resultSet.getInt("experiment_id");
				String name = resultSet.getString("name").toString();
				experimentNameIdList.put(experimentId.toString(), name);
			}

			returnVal.setNamesById(experimentNameIdList);
		} catch (SQLException e) {
			returnVal.getDtoHeaderResponse().addException(e);
			LOGGER.error(e.getMessage());
		} catch (GobiiDaoException e) {
			returnVal.getDtoHeaderResponse().addException(e);
			LOGGER.error(e.getMessage());
		}

		return returnVal;

	} // getNameIdListForContacts()

	private NameIdListDTO getNameIdListForDataSet(NameIdListDTO nameIdListDTO) {

		NameIdListDTO returnVal = new NameIdListDTO();

		try {

			ResultSet resultSet = rsDataSetDao.getDatasetFileNamesByExperimentId(Integer.parseInt(nameIdListDTO.getFilter()));

			Map<String, String> experimentNameIdList = new HashMap<>();

			while (resultSet.next()) {
				Integer dataSetId = resultSet.getInt("dataset_id");
				String dataFileName = resultSet.getString("data_file").toString();
				experimentNameIdList.put(dataSetId.toString(), dataFileName);
			}

			returnVal.setNamesById(experimentNameIdList);

		} catch (SQLException e) {
			returnVal.getDtoHeaderResponse().addException(e);
			LOGGER.error(e.getMessage());
		} catch (GobiiDaoException e) {
			returnVal.getDtoHeaderResponse().addException(e);
			LOGGER.error(e.getMessage());
		}

		return returnVal;

	} // getNameIdListForDataSet()

	@Override
	public NameIdListDTO getNameIdList(NameIdListDTO nameIdListDTO) {

		NameIdListDTO returnVal = new NameIdListDTO();

		if (nameIdListDTO.getEntityType() == NameIdListDTO.EntityType.DBTABLE) {

			switch (nameIdListDTO.getEntityName()) {
			case "analysis":
				returnVal = getNameIdListForAnalysis(nameIdListDTO);
				break;

			case "contact":
				returnVal = getNameIdListForContacts(nameIdListDTO);
				break;

			case "project":
				returnVal = getNameIdListForProjects(nameIdListDTO);
				break;

			case "platform":
				returnVal = getNameIdListForPlatforms(nameIdListDTO);
				break;

			case "manifest":
				returnVal = getNameIdListForManifest(nameIdListDTO);
				break;

			case "map":
				returnVal = getNameIdListForMap(nameIdListDTO);
				break;

			case "experiment":
				returnVal = getNameIdListForExperiment(nameIdListDTO);
				break;

			case "dataset":
				returnVal = getNameIdListForDataSet(nameIdListDTO);
				break;

			case "reference":
				returnVal = getNameIdListForReference(nameIdListDTO);
				break;
				
			default:
				returnVal.getDtoHeaderResponse().addStatusMessage(DtoHeaderResponse.StatusLevel.Error,
						"Unsupported entity for list request: " + nameIdListDTO.getEntityName());
			}

		}

		return returnVal;

	} // getNameIdList()

} // DtoMapNameIdListImpl
