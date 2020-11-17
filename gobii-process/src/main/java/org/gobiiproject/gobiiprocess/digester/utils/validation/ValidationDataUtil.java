package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiiapimodel.payload.HeaderStatusMessage;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.payload.Status;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.config.GobiiCropConfig;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.config.ServerConfigItem;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.auditable.ExperimentDTO;
import org.gobiiproject.gobiimodel.dto.auditable.MapsetDTO;
import org.gobiiproject.gobiimodel.dto.auditable.PlatformDTO;
import org.gobiiproject.gobiimodel.dto.auditable.ProjectDTO;
import org.gobiiproject.gobiimodel.dto.children.NameIdDTO;
import org.gobiiproject.gobiimodel.entity.Experiment;
import org.gobiiproject.gobiimodel.entity.Mapset;
import org.gobiiproject.gobiimodel.entity.Platform;
import org.gobiiproject.gobiimodel.entity.Project;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiimodel.utils.error.Logger;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.Failure;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.FailureTypes;
import org.gobiiproject.gobiisampletrackingdao.ExperimentDao;
import org.gobiiproject.gobiisampletrackingdao.MapsetDao;
import org.gobiiproject.gobiisampletrackingdao.PlatformDao;
import org.gobiiproject.gobiisampletrackingdao.ProjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import java.net.URL;
import java.util.*;

public class ValidationDataUtil {

    private final static int pageSize = 1000;

    private static ApplicationContext context = new ClassPathXmlApplicationContext(
        "classpath:/spring/application-config.xml");

    private static MapsetDao mapsetDao = context.getBean(MapsetDao.class);

    private static ExperimentDao experimentDao = context.getBean(ExperimentDao.class);

    private static ProjectDao projectDao = context.getBean(ProjectDao.class);

    private static PlatformDao platformDao = context.getBean(PlatformDao.class);

    /**
     * Gets the allowed values for foreign key
     */
    public static Map<String, String> getAllowedForeignKeyList(String foreignKey,
                                                               List<Failure> failureList
    ) throws MaximumErrorsValidationException {
        Map<String, String> foreignKeyIdNameMap = new HashMap<>();
        try {
            int offset = 0;
            if (foreignKey.equalsIgnoreCase(ValidationConstants.LINKAGE_GROUP)) {
                List<Mapset> mapsets = new ArrayList<>();
                while (mapsets.size() == pageSize || offset == 0) {
                    mapsets = mapsetDao.getMapsets(pageSize, offset, null);
                    mapsets.forEach(mapset -> {
                        foreignKeyIdNameMap.put(
                            mapset.getMapsetId().toString(),
                            mapset.getMapsetName());
                    });
                    offset += pageSize;
                }
            } else if (foreignKey.equalsIgnoreCase(ValidationConstants.DNARUN)) {
                List<Experiment> experiments = new ArrayList<>();
                while (experiments.size() == pageSize || offset == 0) {
                    experiments = experimentDao.getExperiments(pageSize, 0, null);
                    experiments.forEach(experiment -> {
                        foreignKeyIdNameMap.put(
                            experiment.getExperimentId().toString(),
                            experiment.getExperimentName());
                    });
                    offset += pageSize;
                }
            } else if (foreignKey.equalsIgnoreCase(ValidationConstants.DNASAMPLE_NAME)) {
                List<Project> projects = new ArrayList<>();
                while (projects.size() == pageSize || offset == 0) {
                    projects = projectDao.getProjects(offset/pageSize, pageSize, null);
                    projects.forEach(project -> {
                        foreignKeyIdNameMap.put(
                            project.getProjectId().toString(),
                            project.getProjectName());
                    });
                    offset += pageSize;
                }
            } else {
                ValidationUtil.createFailure(FailureTypes.UNDEFINED_FOREIGN_KEY,
                    new ArrayList<>(),
                    foreignKey,
                    failureList);
            }
        } catch (Exception e) {
            ValidationUtil.createFailure(FailureTypes.EXCEPTION,
                new ArrayList<>(),
                e.getMessage(),
                failureList);
        }
        return foreignKeyIdNameMap;
    }

    /**
     * Verifies whether the Platform Id is valid or not
     */
    public static Map<String, String> validatePlatformId(String platformId,
                                                         List<Failure> failureList
    ) throws MaximumErrorsValidationException {
        Map<String, String> platformIdNameMap = new HashMap<>();
        try {
            Integer intPlatformId = Integer.parseInt(platformId);
            Platform platform = platformDao.getPlatform(intPlatformId);
            if(platform != null) {
                platformIdNameMap.put(platform.getPlatformId().toString(),
                    platform.getPlatformName());
            }
            else {
                ValidationUtil.createFailure(FailureTypes.UNDEFINED_PLATFORM_ID,
                    new ArrayList<>(),
                    "Platform not found",
                    failureList);
            }
        } catch (Exception e) {
            ValidationUtil.createFailure(FailureTypes.EXCEPTION,
                new ArrayList<>(),
                e.getMessage(),
                failureList);
        }
        return platformIdNameMap;
    }

    /**
     * Verifies whether the Project Id is valid or not
     */
    public static Map<String, String> validateProjectId(String projectId,
                                                        List<Failure> failureList
    ) throws MaximumErrorsValidationException {
        Map<String, String> projectIdNameMap = new HashMap<>();
        try {
            Integer intProjectId = Integer.parseInt(projectId);
            Project project = projectDao.getProject(intProjectId);
            if(project != null) {
                projectIdNameMap.put(project.getProjectId().toString(),
                    project.getProjectName());
            }
            else {
                ValidationUtil.createFailure(FailureTypes.UNDEFINED_PROJECT_ID,
                    new ArrayList<>(),
                    "Project not found",
                    failureList);
            }
        } catch (Exception e) {
            ValidationUtil.createFailure(FailureTypes.EXCEPTION,
                new ArrayList<>(),
                e.getMessage(),
                failureList);
        }
        return projectIdNameMap;
    }

    /**
     * Verifies whether the Experiment Id is valid or not
     */
    public static Map<String, String> validateExperimentId(String experimentId,
                                                           List<Failure> failureList
    ) throws MaximumErrorsValidationException {
        Map<String, String> experimentIdNameMap = new HashMap<>();
        try {
            Integer intExperimentId = Integer.parseInt(experimentId);
            Experiment experiment = experimentDao.getExperiment(intExperimentId);
            if(experiment != null) {
                experimentIdNameMap.put(
                    experiment.getExperimentId().toString(),
                    experiment.getExperimentName());
            }
            else {
                ValidationUtil.createFailure(FailureTypes.UNDEFINED_EXPERIMENT_ID,
                    new ArrayList<>(),
                    "Experiment not found",
                    failureList);
            }
        } catch (Exception e) {
            ValidationUtil.createFailure(FailureTypes.EXCEPTION,
                new ArrayList<>(),
                e.getMessage(),
                failureList);
        }
        return experimentIdNameMap;
    }

    public static Map<String, String> validateMapId(String mapId,
                                                    List<Failure> failureList
    ) throws MaximumErrorsValidationException {
        Map<String, String> mapsetIdNameMap = new HashMap<>();
        try {
            Integer intMapSetId = Integer.parseInt(mapId);
            MapsetDao mapsetDao = context.getBean(MapsetDao.class);
            Mapset mapset = mapsetDao.getMapset(intMapSetId);
            if(mapset != null) {
                mapsetIdNameMap.put(
                    mapset.getMapsetId().toString(),
                    mapset.getMapsetName());
            }
            else {
                ValidationUtil.createFailure(FailureTypes.UNDEFINED_MAP_ID,
                    new ArrayList<>(),
                    "Genome Map not found",
                    failureList);
            }
        } catch (Exception e) {
            ValidationUtil.createFailure(
                FailureTypes.EXCEPTION,
                new ArrayList<>(),
                e.getMessage(),
                failureList);
        }
        return mapsetIdNameMap;
    }

    //TODO - this limit can be divined by another webservice call
    static final int DEFAULT_MAX_NAMES_PER_CALL = 2000;

    /**
     * Web service call to validate CV and reference type. Will batch over the arbitrary limit.
     *
     * @param nameIdDTOList       Items list
     * @param gobiiEntityNameType CV or Reference
     * @param filterValue         filter value
     * @param failureList         failure list
     * @return Items list with id
     * @throws MaximumErrorsValidationException exception
     */
    public static List<NameIdDTO> getNamesByNameList(List<NameIdDTO> nameIdDTOList, String gobiiEntityNameType, String filterValue, List<Failure> failureList, GobiiCropConfig cropConfig) throws MaximumErrorsValidationException {
        int numEntities = nameIdDTOList.size();
        List<NameIdDTO> results = new ArrayList<>(numEntities);
        int maxEntitiesPerCall = DEFAULT_MAX_NAMES_PER_CALL;

        //Determine limit from configuration if possible
        Integer limit = cropConfig != null ? getEntityLimit(gobiiEntityNameType,cropConfig):null;
        if((limit!=null) && (limit > 0)){
            maxEntitiesPerCall = limit;
        }

        for(int i=0;i < numEntities;i+=maxEntitiesPerCall){
            List<NameIdDTO> sublist = getSubList(nameIdDTOList,i,i+maxEntitiesPerCall);
            results.addAll(getNamesByShortNameList(sublist,gobiiEntityNameType,filterValue,failureList));
        }
        return results;
    }

    private static Integer getEntityLimit(String gobiiEntityNameType, GobiiCropConfig cropConfig) {
        Integer limit = null;
        try {
            limit = cropConfig.getServer(ServerType.GOBII_WEB).getRestResourceLimit(RestResourceId.GOBII_NAMES,RestMethodType.GET,gobiiEntityNameType);
        }catch(Exception e){
            //Program flow - getRestResourceLimit follows success or exception paradigm
        }
        if(limit != null){
            return limit;
        }
        try{
            limit = cropConfig.getServer(ServerType.GOBII_WEB).getRestResourceLimit(RestResourceId.GOBII_NAMES,RestMethodType.GET,gobiiEntityNameType);
        }catch(Exception e){
            //Program flow - getRestResourceLimit follows success or exception paradigm
        }
        return limit;
    }

    /**
     * Works like ArrayList.getSubList, but if last argument is longer than the list length, truncates instead of warning.
     * Works on both ArrayLists and LinkedLists, but only ArrayList implementation is efficient, so generates a warning
     * on non-Array lists. Analysis of existing codebase suggests primarily Array Lists are being used.
     * @param inList input list - preferably an ArrayList, but will work with other list types.
     * @param fromList index 'from' to begin sublist at
     * @param toList index 'to' to end sublist at. Sublist will end early if 'to' is too large
     * @param <T>
     * @return
     */
    private static<T> List<T> getSubList(List<T> inList, int fromList, int toList){
        if(toList > inList.size()){
            toList = inList.size();
        }
        if(inList instanceof ArrayList){
            ArrayList<T> aList = (ArrayList<T>)inList;
            return aList.subList(fromList,toList);
        }

        Logger.logWarning("Validation", "Inefficient call to getSubList on non-array list type");
        List<T> outList = new LinkedList<T>();
        Iterator<T> interator = inList.listIterator(fromList);
        for(int i = 0; i < toList - fromList; i++){
            if(!interator.hasNext()){
                break;
            }
            outList.add(interator.next()); //bleugh
        }
        return outList;
    }

    /**
     * Underlying web service call to validate CV and reference type
     *
     * @param nameIdDTOList       Items list
     * @param gobiiEntityNameType CV or Reference
     * @param filterValue         filter value
     * @param failureList         failure list
     * @return Items list with id
     * @throws MaximumErrorsValidationException exception
     */
    public static List<NameIdDTO> getNamesByShortNameList(List<NameIdDTO> nameIdDTOList, String gobiiEntityNameType, String filterValue, List<Failure> failureList) throws MaximumErrorsValidationException {
        List<NameIdDTO> nameIdDTOListResponse = new ArrayList<>();
        try {
            PayloadEnvelope<NameIdDTO> payloadEnvelope = new PayloadEnvelope<>();
            payloadEnvelope.getHeader().setGobiiProcessType(GobiiProcessType.CREATE);
            payloadEnvelope.getPayload().setData(nameIdDTOList);

            RestUri namesUri = GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .nameIdListByQueryParams();
            GobiiEnvelopeRestResource<NameIdDTO, NameIdDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(namesUri);
            namesUri.setParamValue("entity", gobiiEntityNameType.toLowerCase());

            namesUri.setParamValue("filterType", StringUtils.capitalize(GobiiFilterType.NAMES_BY_NAME_LIST.toString().toUpperCase()));
            if (gobiiEntityNameType.equalsIgnoreCase(GobiiEntityNameType.CV.toString())) {
                switch (filterValue) {
                    case "species_name":
                        namesUri.setParamValue("filterValue", CvGroupTerm.CVGROUP_GERMPLASM_SPECIES.getCvGroupName());
                        break;
                    case "type_name":
                        namesUri.setParamValue("filterValue", CvGroupTerm.CVGROUP_GERMPLASM_TYPE.getCvGroupName());
                        break;
                    case "strand_name":
                        namesUri.setParamValue("filterValue", CvGroupTerm.CVGROUP_MARKER_STRAND.getCvGroupName());
                        break;
                    default:
                        ValidationUtil.createFailure(FailureTypes.UNDEFINED_CV, new ArrayList<>(), filterValue, failureList);
                        return nameIdDTOListResponse;
                }
            } else namesUri.setParamValue("filterValue", filterValue);

            PayloadEnvelope<NameIdDTO> responsePayloadEnvelope = gobiiEnvelopeRestResource.post(NameIdDTO.class, payloadEnvelope);
            Status status = responsePayloadEnvelope.getHeader().getStatus();
            if (!status.isSucceeded()) {
                Logger.logWarning("ValidtionWebServices","Bad NameIdDTO request");
                for (HeaderStatusMessage m : status.getStatusMessages()) {
                    Logger.logWarning("ValidationWebServices", m.getMessage());
                }
                ArrayList<HeaderStatusMessage> statusMessages = status.getStatusMessages();
                for (HeaderStatusMessage message : statusMessages)
                    ValidationUtil.createFailure(FailureTypes.DATABASE_ERROR, new ArrayList<>(), message.getMessage(), failureList);
                return nameIdDTOListResponse;
            }
            nameIdDTOListResponse.addAll(responsePayloadEnvelope.getPayload().getData());
        } catch (MaximumErrorsValidationException e) {
            throw e;
        } catch (Exception e) {
            ValidationUtil.createFailure(FailureTypes.EXCEPTION, new ArrayList<>(), e.getMessage(), failureList);
        }
        return nameIdDTOListResponse;
    }
}
