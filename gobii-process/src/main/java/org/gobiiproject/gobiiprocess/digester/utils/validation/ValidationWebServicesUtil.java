package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiiapimodel.payload.HeaderStatusMessage;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.payload.Status;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.config.ServerConfigItem;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ExperimentDTO;
import org.gobiiproject.gobiimodel.dto.entity.auditable.MapsetDTO;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.Failure;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.FailureTypes;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationWebServicesUtil {

    public static boolean loginIntoServer(String url, String username, String password, String crop, List<Failure> failures) {
        try {
            GobiiClientContext.getInstance(url, true).getCurrentClientCropType();
            String contextRoot = new URL(url).getPath();
            if ('/' != contextRoot.charAt(contextRoot.length() - 1))
                contextRoot = contextRoot + "/";
            List<String> crops = GobiiClientContext.getInstance(null, false).getCropTypeTypes();
            for (String currentCrop : crops) {
                ServerConfigItem currentServerConfig = GobiiClientContext.getInstance(null, false).getServerConfig(currentCrop);
                if (contextRoot.equals(currentServerConfig.getContextRoot())) {
                    // use the crop for this server config
                    crop = currentCrop;
                    break;
                }
            }
            if (crop == null || crop.isEmpty()) {
                Failure failure = new Failure();
                failure.reason = FailureTypes.LOGIN_FAILURE;
                failure.values.add("Undefined crop for server: " + url);
                failures.add(failure);
                return false;
            }

            boolean login = GobiiClientContext.getInstance(url, true).login(crop, username, password);
            if (!login) {
                String failureMessage = GobiiClientContext.getInstance(null, false).getLoginFailure();

                Failure failure = new Failure();
                failure.reason = FailureTypes.LOGIN_FAILURE;
                failure.values.add(failureMessage);
                failures.add(failure);
                return false;
            }
            return true;
        } catch (Exception e) {
            Failure failure = new Failure();
            failure.reason = FailureTypes.LOGIN_FAILURE;
            failure.values.add(e.getMessage());
            failures.add(failure);
            return false;
        }
    }

    public static Map<String, String> getAllowedForeignKeyList(String foreignKey, List<Failure> failureList) throws MaximumErrorsValidationException {
        Map<String, String> mapsetDTOList = new HashMap<>();
        try {
            GobiiUriFactory uriFactory = GobiiClientContext.getInstance(null, false).getUriFactory();
            RestUri restUri = null;
            if (foreignKey.equalsIgnoreCase(ValidationConstants.LINKAGE_GROUP)) {
                restUri = uriFactory.resourceColl(RestResourceId.GOBII_MAPSET);
                GobiiEnvelopeRestResource<MapsetDTO, MapsetDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUri);
                PayloadEnvelope<MapsetDTO> resultEnvelope = gobiiEnvelopeRestResource.get(MapsetDTO.class);
                resultEnvelope.getPayload().getData().forEach(dto -> mapsetDTOList.put(dto.getName(), dto.getMapsetId().toString()));
                return mapsetDTOList;
            } else if (foreignKey.equalsIgnoreCase(ValidationConstants.DNARUN)) {
                restUri = uriFactory.resourceColl(RestResourceId.GOBII_EXPERIMENTS);
                GobiiEnvelopeRestResource<ExperimentDTO, ExperimentDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUri);
                PayloadEnvelope<ExperimentDTO> resultEnvelope = gobiiEnvelopeRestResource
                        .get(ExperimentDTO.class);
                resultEnvelope.getPayload().getData().forEach(dto -> mapsetDTOList.put(dto.getExperimentName(), dto.getExperimentId().toString()));
                return mapsetDTOList;
            } else {
                Failure failure = new Failure();
                failure.reason = FailureTypes.UNDEFINED_FOREIGN_KEY;
                failure.values.add(foreignKey);
                ValidationUtil.addMessageToList(failure, failureList);
                return mapsetDTOList;
            }
        } catch (Exception e) {
            Failure failure = new Failure();
            failure.reason = FailureTypes.EXCEPTION;
            failure.values.add(e.getMessage());
            ValidationUtil.addMessageToList(failure, failureList);
            return mapsetDTOList;
        }
    }

    /**
     * Web service call to validate CV and reference type
     *
     * @param nameIdDTOList       Items list
     * @param gobiiEntityNameType CV or Reference
     * @param filterValue         filter value
     * @param failureList         failure list
     * @return Items list with id
     * @throws MaximumErrorsValidationException exception
     */
    public static List<NameIdDTO> getNamesByNameList(List<NameIdDTO> nameIdDTOList, String gobiiEntityNameType, String filterValue, List<Failure> failureList) throws MaximumErrorsValidationException {
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
                        namesUri.setParamValue("filterValue", CvGroup.CVGROUP_GERMPLASM_SPECIES.getCvGroupName());
                        break;
                    case "type_name":
                        namesUri.setParamValue("filterValue", CvGroup.CVGROUP_GERMPLASM_TYPE.getCvGroupName());
                        break;
                    case "strand_name":
                        namesUri.setParamValue("filterValue", CvGroup.CVGROUP_MARKER_STRAND.getCvGroupName());
                        break;
                    default:
                        Failure failure = new Failure();
                        failure.reason = FailureTypes.UNDEFINED_CV;
                        failure.values.add(filterValue);
                        ValidationUtil.addMessageToList(failure, failureList);
                        return nameIdDTOListResponse;
                }
            } else if (gobiiEntityNameType.equalsIgnoreCase(GobiiEntityNameType.REFERENCE.toString()) || gobiiEntityNameType.equalsIgnoreCase(GobiiEntityNameType.MARKER.toString())
                    || gobiiEntityNameType.equalsIgnoreCase(GobiiEntityNameType.LINKAGE_GROUP.toString()) || gobiiEntityNameType.equalsIgnoreCase(GobiiEntityNameType.DNARUN.toString()))
                namesUri.setParamValue("filterValue", filterValue);

            PayloadEnvelope<NameIdDTO> responsePayloadEnvelope = gobiiEnvelopeRestResource.post(NameIdDTO.class, payloadEnvelope);
            Status status = responsePayloadEnvelope.getHeader().getStatus();
            if (!status.isSucceeded()) {
                ArrayList<HeaderStatusMessage> statusMessages = status.getStatusMessages();
                for (HeaderStatusMessage message : statusMessages) {
                    Failure failure = new Failure();
                    failure.reason = FailureTypes.DATABASE_ERROR;
                    failure.values.add(message.getMessage());
                    ValidationUtil.addMessageToList(failure, failureList);
                }
                return nameIdDTOListResponse;
            }
            nameIdDTOListResponse.addAll(responsePayloadEnvelope.getPayload().getData());
        } catch (MaximumErrorsValidationException e) {
            throw e;
        } catch (Exception e) {
            Failure failure = new Failure();
            failure.reason = FailureTypes.EXCEPTION;
            failure.values.add(e.getMessage());
            ValidationUtil.addMessageToList(failure, failureList);
        }
        return nameIdDTOListResponse;
    }


}
