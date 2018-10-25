package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiiapimodel.payload.HeaderStatusMessage;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.payload.Status;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.config.ServerConfig;
import org.gobiiproject.gobiimodel.config.ServerConfigItem;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.Failure;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.FailureTypes;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.ValidationError;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class ValidationWebServicesUtil {

    static boolean loginIntoServer(String url, String username, String password, String crop, ValidationError validationError) {
        try {
            GobiiClientContext.getInstance(url, true).getCurrentClientCropType();
            String contextRoot = new URL(url).getPath();
            if ('/' != contextRoot.charAt(contextRoot.length() - 1))
                contextRoot = contextRoot + "/";
            List<String> crops = GobiiClientContext.getInstance(null, false).getCropTypeTypes();
            for (String currentCrop : crops) {
                ServerConfigItem currentServerConfig = GobiiClientContext.getInstance(null,false).getServerConfig(currentCrop);
                if (contextRoot.equals(currentServerConfig.getContextRoot())) {
                    // use the crop for this server config
                    crop = currentCrop;
                    break;
                }
            }
            if (crop == null || crop.isEmpty()) {
                validationError.status = ValidationConstants.FAILURE;
                Failure failure = new Failure();
                failure.reason = FailureTypes.LOGIN_FAILURE;
                failure.values.add("Undefined crop for server: " + url);
                validationError.failures.add(failure);
                return false;
            }

            boolean login = GobiiClientContext.getInstance(url, true).login(crop, username, password);
            if (!login) {
                String failureMessage = GobiiClientContext.getInstance(null, false).getLoginFailure();
                validationError.status = ValidationConstants.FAILURE;
                Failure failure = new Failure();
                failure.reason = FailureTypes.LOGIN_FAILURE;
                failure.values.add(failureMessage);
                validationError.failures.add(failure);
                return false;
            }
            return true;
        } catch (Exception e) {
            validationError.status = ValidationConstants.FAILURE;
            Failure failure = new Failure();
            failure.reason = FailureTypes.LOGIN_FAILURE;
            failure.values.add( e.getMessage());
            validationError.failures.add(failure);
            return false;
        }
    }

    static void validateCVName(List<NameIdDTO> nameIdDTOList, String cvGroupName, List<Failure> failureList) throws MaximumErrorsValidationException {
        try {
            PayloadEnvelope<NameIdDTO> payloadEnvelope = new PayloadEnvelope<>();
            payloadEnvelope.getHeader().setGobiiProcessType(GobiiProcessType.CREATE);
            payloadEnvelope.getPayload().setData(nameIdDTOList);

            RestUri namesUri = GobiiClientContext.getInstance(null, false)
                    .getUriFactory()
                    .nameIdListByQueryParams();
            GobiiEnvelopeRestResource<NameIdDTO,NameIdDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(namesUri);
            namesUri.setParamValue("entity", GobiiEntityNameType.CV.toString().toLowerCase());
            namesUri.setParamValue("filterType", StringUtils.capitalize(GobiiFilterType.NAMES_BY_NAME_LIST.toString().toUpperCase()));
            switch (cvGroupName) {
                case "species_name":
                    namesUri.setParamValue("filterValue", CvGroup.CVGROUP_GERMPLASM_SPECIES.getCvGroupName());
                    break;
                case "type_name":
                    namesUri.setParamValue("filterValue", CvGroup.CVGROUP_GERMPLASM_TYPE.getCvGroupName());
                    break;
                case "marker_strand":
                    namesUri.setParamValue("filterValue", CvGroup.CVGROUP_MARKER_STRAND.getCvGroupName());
                    break;
                default:
                    Failure failure = new Failure();
                    failure.reason = FailureTypes.UNDEFINED_CV;
                    failure.values.add(cvGroupName);
                    ValidationUtil.addMessageToList(failure, failureList);
                    return;
            }
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
                return;
            }
            List<NameIdDTO> nameIdDTOListResponse = responsePayloadEnvelope.getPayload().getData();
            for (NameIdDTO nameIdDTO : nameIdDTOListResponse) {
                if (nameIdDTO.getId() == 0) {
                    Failure failure = new Failure();
                    failure.reason = FailureTypes.UNDEFINED_CV_VALUE;
                    failure.columnName.add(cvGroupName);
                    failure.values.add(nameIdDTO.getName());
                    ValidationUtil.addMessageToList(failure, failureList);
                }
            }
        } catch (MaximumErrorsValidationException e) {
            throw e;
        } catch (Exception e) {
            Failure failure = new Failure();
            failure.reason = FailureTypes.EXCEPTION;
            failure.values.add(e.getMessage());
            ValidationUtil.addMessageToList(failure, failureList);
        }
    }
}
