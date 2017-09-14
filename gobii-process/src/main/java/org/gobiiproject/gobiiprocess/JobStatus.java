package org.gobiiproject.gobiiprocess;

import org.gobiiproject.gobiiapimodel.payload.HeaderStatusMessage;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.headerlesscontainer.StatusDTO;
import org.gobiiproject.gobiimodel.types.GobiiAutoLoginType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import static org.gobiiproject.gobiimodel.utils.error.ErrorLogger.logError;

/**
 * Encapsulate calling and updating status.
 *
 * Note: Status is created when the instruction file is, and the base elements are populated there.
 * This is important * If you're making your own jobs, you need to create a Status and deal with it there.*
 */
public class JobStatus {
    /**
     * Creates a new job object based on information about the job already in progress.
     * @param config
     * @param cropName
     * @param jobId the name of the instruction file, minus the .json suffix
     */
    GobiiUriFactory uriFactory;
    String jobId;
    StatusDTO lastStatus;
    public JobStatus(ConfigSettings config, String cropName, String jobId) throws Exception {
		this.jobId=jobId;
		// set up authentication and so forth
		// you'll need to get the current from the instruction file
		GobiiClientContext context = GobiiClientContext.getInstance(config, cropName, GobiiAutoLoginType.USER_RUN_AS);
		uriFactory = context.getUriFactory();
    }

    public void set(Integer status,String message){
            try{
                RestUri restUri=uriFactory
                        .resourceByUriIdParam(GobiiServiceRequestId.URL_STATUS);
                restUri.setParamValue("id", jobId);
            GobiiEnvelopeRestResource<StatusDTO> gobiiEnvelopeRestResourceForDatasets = new GobiiEnvelopeRestResource<>(restUri);
            PayloadEnvelope<StatusDTO> resultEnvelope = gobiiEnvelopeRestResourceForDatasets
                    .get(StatusDTO.class);

            StatusDTO dataSetResponse;
            if (!resultEnvelope.getHeader().getStatus().isSucceeded()) {
                System.out.println();
                logError("Digester", "Data set response response errors");
                for (HeaderStatusMessage currentStatusMesage : resultEnvelope.getHeader().getStatus().getStatusMessages()) {
                    logError("HeaderError", currentStatusMesage.getMessage());
                }
                return;
            } else {
                dataSetResponse = resultEnvelope.getPayload().getData().get(0);
            }

                dataSetResponse.setMessages(message);
                dataSetResponse.setProcessStatus(status);

                resultEnvelope = gobiiEnvelopeRestResourceForDatasets
                        .put(StatusDTO.class, new PayloadEnvelope<>(dataSetResponse, GobiiProcessType.UPDATE));

            // if you didn't succeed, do not pass go, but do log errors to your log file
            if (!resultEnvelope.getHeader().getStatus().isSucceeded()) {
                logError("Digester", "Data set response response errors");
                for (HeaderStatusMessage currentStatusMesage : resultEnvelope.getHeader().getStatus().getStatusMessages()) {
                    logError("HeaderError", currentStatusMesage.getMessage());
                }
                return;
            }
        } catch (Exception e) {
            logError("Digester", "Exception while referencing data sets in Postgresql", e);
            return;
        }
    }

    /**
     * Sets the status of the job underway. Adds a message based on the error automatically.
     *
     */
    public void setError(String message){
        String errorMessage="";
        if(lastStatus!=null){
            errorMessage="Status: " + lastStatus.getProcessStatus()+" - " + lastStatus.getMessages() + " | \n";
        }
        errorMessage += message + " : " + ErrorLogger.getFirstErrorReason();
        Integer errorStatus=-1;
        set(StatusDTO.CV_PROGRESSSTATUS_FAILED,errorMessage);
    }
}