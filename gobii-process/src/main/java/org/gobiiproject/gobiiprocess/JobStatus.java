package org.gobiiproject.gobiiprocess;

import org.gobiiproject.gobiiapimodel.payload.HeaderStatusMessage;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.cvnames.JobProgressStatusType;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.JobDTO;
import org.gobiiproject.gobiimodel.types.GobiiAutoLoginType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.util.*;

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
     * @param jobName the name of the instruction file, minus the .json suffix
     */
    GobiiUriFactory uriFactory;
	JobDTO job;

	/**List of valid statuses. Update as appropriate. Stops 'random argument' assignment, even though Progress Status
	 * has been stringly typed.
	 */
	private static Set<JobProgressStatusType> acceptedStatuses=new HashSet<>(Arrays.asList(
			JobProgressStatusType.CV_PROGRESSSTATUS_ABORTED,
			JobProgressStatusType.CV_PROGRESSSTATUS_COMPLETED,
			JobProgressStatusType.CV_PROGRESSSTATUS_DIGEST,
			JobProgressStatusType.CV_PROGRESSSTATUS_INPROGRESS,
			JobProgressStatusType.CV_PROGRESSSTATUS_MATRIXLOAD,
			JobProgressStatusType.CV_PROGRESSSTATUS_METADATALOAD,
			JobProgressStatusType.CV_PROGRESSSTATUS_TRANSFORMATION,
			JobProgressStatusType.CV_PROGRESSSTATUS_VALIDATION,
			JobProgressStatusType.CV_PROGRESSSTATUS_FAILED,
			JobProgressStatusType.CV_PROGRESSSTATUS_METADATAEXTRACT,
			JobProgressStatusType.CV_PROGRESSSTATUS_FINALASSEMBLY,
			JobProgressStatusType.CV_PROGRESSSTATUS_QCPROCESSING
	));

    public JobStatus(ConfigSettings config, String cropName, String jobName) throws Exception {

		this.job = new JobDTO();
		job.setJobName(jobName);
		// set up authentication and so forth
		// you'll need to get the current from the instruction file
		GobiiClientContext context = GobiiClientContext.getInstance(config, cropName, GobiiAutoLoginType.USER_RUN_AS);
		uriFactory = context.getUriFactory();
    }

    public void set(JobProgressStatusType status, String message){

    	if(!acceptedStatuses.contains(status)){
    		ErrorLogger.logError(
    				"JobStatus",
					String.format("Invalid status passed to set: %s\nMessage: %s", status.getCvName(), message),
					new Exception());//passing a new exception throws a stack trace in there
		}

		try {
			RestUri restUri = uriFactory.resourceByUriIdParam(RestResourceId.GOBII_JOB);
			restUri.setParamValue("id", job.getJobName());

			GobiiEnvelopeRestResource<JobDTO,JobDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUri);
			PayloadEnvelope<JobDTO> resultEnvelope = gobiiEnvelopeRestResource.get(JobDTO.class);

			if (!resultEnvelope.getHeader().getStatus().isSucceeded()) {
				System.out.println();
				logError("Digester", "Job table response errors");

				for (HeaderStatusMessage currentStatusMesage : resultEnvelope.getHeader().getStatus().getStatusMessages()) {
					logError("HeaderError", currentStatusMesage.getMessage());
				}
				return;
			}

			List<JobDTO> responses = resultEnvelope.getPayload().getData();
			if(responses.size() == 0) {
				logError("JobStatus", String.format("No Job record returned for job %s", job.getJobName()));
				return;
			}

			JobDTO dataSetResponse = responses.get(0);

			dataSetResponse.setMessage(message);
			dataSetResponse.setStatus(status.getCvName());

			resultEnvelope = gobiiEnvelopeRestResource
					.put(JobDTO.class, new PayloadEnvelope<>(dataSetResponse, GobiiProcessType.UPDATE));

			//Set 'job' to the current status
			job = dataSetResponse;

			// if you didn't succeed, do not pass go, but do log errors to your log file
			if (!resultEnvelope.getHeader().getStatus().isSucceeded()) {
				logError("Digester", "Data set response response errors");
				for (HeaderStatusMessage currentStatusMesage : resultEnvelope.getHeader().getStatus().getStatusMessages()) {
					logError("HeaderError", currentStatusMesage.getMessage());
				}
				return;
			}
		} catch (Exception e) {
			logError("Digester", "Exception while referencing Job table in Postgresql", e);
			return;
		}
	}

	/**
     * Sets the status of the job underway. Adds a message based on the error automatically.
     *
     */
	public void setError(String message){

        String errorMessage="";
        if(job !=null){
            errorMessage = String.format("Status: %s - %s | \n", job.getStatus(), job.getMessage());
        }
        errorMessage += String.format("%s : %s", message, ErrorLogger.getFirstErrorReason());
        set(JobProgressStatusType.CV_PROGRESSSTATUS_FAILED,errorMessage);
    }
}