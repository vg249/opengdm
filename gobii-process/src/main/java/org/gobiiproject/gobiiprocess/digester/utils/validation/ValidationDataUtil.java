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
import org.gobiiproject.gobiimodel.entity.*;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiimodel.utils.error.Logger;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.Failure;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.FailureTypes;
import org.gobiiproject.gobiisampletrackingdao.*;
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

    static final int MAX_ENTITIES_PER_QUERY = 2000;

    private static ApplicationContext context = new ClassPathXmlApplicationContext(
        "classpath:/spring/application-config.xml");

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
                MapsetDao mapsetDao = context.getBean(MapsetDao.class);
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
                ExperimentDao experimentDao = context.getBean(ExperimentDao.class);
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
                ProjectDao projectDao = context.getBean(ProjectDao.class);
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
            PlatformDao platformDao = context.getBean(PlatformDao.class);
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
            ProjectDao projectDao = context.getBean(ProjectDao.class);
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
            ExperimentDao experimentDao = context.getBean(ExperimentDao.class);
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


    public static List<NameIdDTO> getNamesByNameList(List<String> names,
                                                     String gobiiEntityNameType,
                                                     String filterValue,
                                                     List<Failure> failureList
    ) throws MaximumErrorsValidationException {

        int numEntities = names.size();

        List<NameIdDTO> results = new ArrayList<>(numEntities);
        int maxEntitiesPerCall = MAX_ENTITIES_PER_QUERY;

        for(int i=0; i < numEntities; i+=maxEntitiesPerCall) {
            Set<String> namesSubSet = new HashSet<>(names.subList(i, i+maxEntitiesPerCall));
            results.addAll(mapNamesToIds(namesSubSet,
                gobiiEntityNameType,
                filterValue,
                failureList));
        }
        return results;
    }

    /**
     *
     * @param names
     * @param gobiiEntityNameType
     * @param filterValue
     * @param failureList
     * @return
     * @throws MaximumErrorsValidationException
     */
    private static List<NameIdDTO> mapNamesToIds(Set<String> names,
                                                 String gobiiEntityNameType,
                                                 String filterValue,
                                                 List<Failure> failureList
    ) throws MaximumErrorsValidationException {

        List<NameIdDTO> nameIdDTOs = new ArrayList<>();
        Integer intFilterValue;
        try {
            intFilterValue = Integer.parseInt(filterValue);
        }
        catch (NumberFormatException nE) {
            ValidationUtil.createFailure(FailureTypes.UNDEFINED_FOREIGN_KEY,
                new ArrayList<>(),
                filterValue,
                failureList);
            return nameIdDTOs;
        }

        try {
            switch (gobiiEntityNameType.toLowerCase()) {

                case "dnarun":
                    nameIdDTOs = getDnaRunNameIds(names, intFilterValue);
                    break;
                case "dnasample":
                    nameIdDTOs = getDnaSampleNameIds(names, intFilterValue);
                    break;
                default:
                    ValidationUtil.createFailure(FailureTypes.UNDEFINED_CONDITION_TYPE,
                        new ArrayList<>(),
                        filterValue,
                        failureList);
                    return nameIdDTOs;


            }

            //if (gobiiEntityNameType.equalsIgnoreCase(GobiiEntityNameType.CV.toString())) {
            //    switch (filterValue) {
            //        case "species_name":
            //            namesUri.setParamValue("filterValue", CvGroupTerm.CVGROUP_GERMPLASM_SPECIES.getCvGroupName());
            //            break;
            //        case "type_name":
            //            namesUri.setParamValue("filterValue", CvGroupTerm.CVGROUP_GERMPLASM_TYPE.getCvGroupName());
            //            break;
            //        case "strand_name":
            //            namesUri.setParamValue("filterValue", CvGroupTerm.CVGROUP_MARKER_STRAND.getCvGroupName());
            //            break;
            //        default:
            //            ValidationUtil.createFailure(FailureTypes.UNDEFINED_CV, new ArrayList<>(), filterValue, failureList);
            //            return nameIdDTOListResponse;
            //    }
            //} else namesUri.setParamValue("filterValue", filterValue);

        }
        catch (GobiiDaoException gE) {
            ValidationUtil.createFailure(FailureTypes.EXCEPTION,
                new ArrayList<>(),
                gE.getMessage(),
                failureList);
        }
        catch (MaximumErrorsValidationException e) {
            throw e;
        } catch (Exception e) {
            ValidationUtil.createFailure(FailureTypes.EXCEPTION,
                new ArrayList<>(),
                e.getMessage(),
                failureList);
        }
        return nameIdDTOs;
    }

    /**
     * @param names         List of dnaRun names for which
     * @param filterValue   for DnaRun filter value is experiment id
     * @return NameIdDto list
     */
    private static List<NameIdDTO> getDnaRunNameIds(Set<String> names,
                                                    Integer filterValue
    ) throws GobiiDaoException {
        List<NameIdDTO> nameIdDTOs = new ArrayList<>();
        DnaRunDao dnaRunDao = context.getBean(DnaRunDao.class);
        List<DnaRun> dnaRuns = dnaRunDao.getDnaRunsByDnaRunNames(names, filterValue);
        for(DnaRun dnaRun : dnaRuns) {
            nameIdDTOs.add(new NameIdDTO(dnaRun.getDnaRunId(), dnaRun.getDnaRunName()));
        }
        return nameIdDTOs;
    }

    private static List<NameIdDTO> getDnaSampleNameIds(Set<String> names,
                                                       Integer filterValue
    ) throws GobiiDaoException {
        List<NameIdDTO> nameIdDTOs = new ArrayList<>();
        DnaSampleDao dnaSampleDao = context.getBean(DnaSampleDao.class);
        List<DnaSample> dnaSamples = dnaSampleDao.getDnaSamples(names, filterValue);
        for(DnaSample dnaSample : dnaSamples) {
            nameIdDTOs.add(new NameIdDTO(dnaSample.getDnaSampleId(), dnaSample.getDnaSampleName()));
        }
        return nameIdDTOs;
    }

}
