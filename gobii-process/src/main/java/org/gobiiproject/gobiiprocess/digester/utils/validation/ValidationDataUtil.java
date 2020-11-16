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
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.config.ServerConfigItem;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.dto.auditable.ExperimentDTO;
import org.gobiiproject.gobiimodel.dto.auditable.MapsetDTO;
import org.gobiiproject.gobiimodel.dto.auditable.PlatformDTO;
import org.gobiiproject.gobiimodel.dto.auditable.ProjectDTO;
import org.gobiiproject.gobiimodel.dto.children.NameIdDTO;
import org.gobiiproject.gobiimodel.entity.Mapset;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiimodel.utils.error.Logger;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.Failure;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.FailureTypes;
import org.gobiiproject.gobiisampletrackingdao.MapsetDao;
import org.gobiiproject.gobiisampletrackingdao.PlatformDao;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.net.URL;
import java.util.*;

public class ValidationDataUtil {

    @PersistenceContext
    private static EntityManager em;

    @Autowired
    private PlatformDao platformDao;

    @Autowired
    private static MapsetDao mapsetDao;

    private final static int pageSize = 1000;

    /**
     * Gets the allowed values for foreign key
     */
    public static Map<String, String> getAllowedForeignKeyList(String foreignKey, List<Failure> failureList) throws MaximumErrorsValidationException {
        Map<String, String> foreignKeyIdNameMap = new HashMap<>();
        try {
            GobiiUriFactory uriFactory = GobiiClientContext.getInstance(null, false).getUriFactory();
            RestUri restUri;
            if (foreignKey.equalsIgnoreCase(ValidationConstants.LINKAGE_GROUP)) {
                List<Mapset> mapsets = mapsetDao.getMapsets(pageSize, 0, null);
                mapsets.forEach(mapset -> {
                    foreignKeyIdNameMap.put(mapset.getMapsetId().toString(),
                        mapset.getMapsetName());
                });
                return foreignKeyIdNameMap;
            } else if (foreignKey.equalsIgnoreCase(ValidationConstants.DNARUN)) {
                restUri = uriFactory.resourceColl(RestResourceId.GOBII_EXPERIMENTS);
                return foreignKeyIdNameMap;
            } else if (foreignKey.equalsIgnoreCase(ValidationConstants.DNASAMPLE_NAME)) {
                restUri = uriFactory.resourceColl(RestResourceId.GOBII_PROJECTS);
                return foreignKeyIdNameMap;
            } else {
                ValidationUtil.createFailure(FailureTypes.UNDEFINED_FOREIGN_KEY, new ArrayList<>(), foreignKey, failureList);
                return foreignKeyIdNameMap;
            }
        } catch (Exception e) {
            ValidationUtil.createFailure(FailureTypes.EXCEPTION, new ArrayList<>(), e.getMessage(), failureList);
            return foreignKeyIdNameMap;
        }
    }

    /**
     * Verifies whether the Platform Id is valid or not
     */
    public static Map<String, String> validatePlatformId(String platformId, List<Failure> failureList) throws MaximumErrorsValidationException {
        Map<String, String> mapsetDTOList = new HashMap<>();
        try {
            RestUri restUri = GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .resourceByUriIdParam(RestResourceId.GOBII_PLATFORM);
            restUri.setParamValue("id", platformId);
            GobiiEnvelopeRestResource<PlatformDTO, PlatformDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUri);
            PayloadEnvelope<PlatformDTO> resultEnvelope = gobiiEnvelopeRestResource.get(PlatformDTO.class);
            if (resultEnvelope.getHeader().getStatus().isSucceeded())
                resultEnvelope.getPayload().getData().forEach(dto -> mapsetDTOList.put(dto.getPlatformId().toString(), dto.getPlatformName()));
            else
                ValidationUtil.createFailure(FailureTypes.UNDEFINED_PLATFORM_ID, new ArrayList<>(), resultEnvelope.getHeader().getStatus().messages(), failureList);
            return mapsetDTOList;
        } catch (Exception e) {
            ValidationUtil.createFailure(FailureTypes.EXCEPTION, new ArrayList<>(), e.getMessage(), failureList);
            return mapsetDTOList;
        }
    }

    /**
     * Verifies whether the Project Id is valid or not
     */
    public static Map<String, String> validateProjectId(String projectId, List<Failure> failureList) throws MaximumErrorsValidationException {
        Map<String, String> mapsetDTOList = new HashMap<>();
        try {
            RestUri restUri = GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .resourceByUriIdParam(RestResourceId.GOBII_PROJECTS);
            restUri.setParamValue("id", projectId);
            GobiiEnvelopeRestResource<ProjectDTO, ProjectDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUri);
            PayloadEnvelope<ProjectDTO> resultEnvelope = gobiiEnvelopeRestResource.get(ProjectDTO.class);
            if (resultEnvelope.getHeader().getStatus().isSucceeded())
                resultEnvelope.getPayload().getData().forEach(dto -> mapsetDTOList.put(dto.getProjectId().toString(), dto.getProjectName()));
            else
                ValidationUtil.createFailure(FailureTypes.UNDEFINED_PROJECT_ID, new ArrayList<>(), resultEnvelope.getHeader().getStatus().messages(), failureList);
            return mapsetDTOList;
        } catch (Exception e) {
            ValidationUtil.createFailure(FailureTypes.EXCEPTION, new ArrayList<>(), e.getMessage(), failureList);
            return mapsetDTOList;
        }
    }

    /**
     * Verifies whether the Experiment Id is valid or not
     */
    public static Map<String, String> validateExperimentId(String experimentId, List<Failure> failureList) throws MaximumErrorsValidationException {
        Map<String, String> mapsetDTOList = new HashMap<>();
        try {
            RestUri restUri = GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .resourceByUriIdParam(RestResourceId.GOBII_EXPERIMENTS);
            restUri.setParamValue("id", experimentId);
            GobiiEnvelopeRestResource<ExperimentDTO, ExperimentDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUri);
            PayloadEnvelope<ExperimentDTO> resultEnvelope = gobiiEnvelopeRestResource.get(ExperimentDTO.class);
            if (resultEnvelope.getHeader().getStatus().isSucceeded())
                resultEnvelope.getPayload().getData().forEach(dto -> mapsetDTOList.put(dto.getExperimentId().toString(), dto.getExperimentName()));
            else
                ValidationUtil.createFailure(FailureTypes.UNDEFINED_EXPERIMENT_ID, new ArrayList<>(), resultEnvelope.getHeader().getStatus().messages(), failureList);
            return mapsetDTOList;
        } catch (Exception e) {
            ValidationUtil.createFailure(FailureTypes.EXCEPTION, new ArrayList<>(), e.getMessage(), failureList);
            return mapsetDTOList;
        }
    }

    public static Map<String, String> validateMapId(String mapId, List<Failure> failureList) throws MaximumErrorsValidationException {
        Map<String, String> mapsetDTOList = new HashMap<>();
        try {
            RestUri restUri = GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .resourceByUriIdParam(RestResourceId.GOBII_MAPSET);
            restUri.setParamValue("id", mapId);
            GobiiEnvelopeRestResource<MapsetDTO, MapsetDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUri);
            PayloadEnvelope<MapsetDTO> resultEnvelope = gobiiEnvelopeRestResource.get(MapsetDTO.class);
            if (resultEnvelope.getHeader().getStatus().isSucceeded())
                resultEnvelope.getPayload().getData().forEach(dto -> mapsetDTOList.put(dto.getMapsetId().toString(), dto.getName()));
            else
                ValidationUtil.createFailure(FailureTypes.UNDEFINED_MAP_ID, new ArrayList<>(), resultEnvelope.getHeader().getStatus().messages(), failureList);
            return mapsetDTOList;
        } catch (Exception e) {
            ValidationUtil.createFailure(FailureTypes.EXCEPTION, new ArrayList<>(), e.getMessage(), failureList);
            return mapsetDTOList;
        }
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
