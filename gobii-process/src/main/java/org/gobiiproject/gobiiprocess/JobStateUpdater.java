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
public class JobStateUpdater {
    /**
     * Creates a new job object based on information about the job already in progress.
     * @param config
     * @param cropName
     * @param jobName the name of the instruction file, minus the .json suffix
     */
    GobiiUriFactory uriFactory;
	JobDTO job;
	String jobName;

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

    public JobStateUpdater(ConfigSettings config, String cropName, String jobName) throws Exception {


		// set up authentication and so forth
		// you'll need to get the current from the instruction file
		GobiiClientContext context = GobiiClientContext.getInstance(config, cropName, GobiiAutoLoginType.USER_RUN_AS);
		uriFactory = context.getUriFactory();
    }

    public void doUpdate(JobProgressStatusType status, String message){

    	if(!acceptedStatuses.contains(status)){
    		ErrorLogger.logError(
    				"Digester",
					String.format("Invalid status passed to set: %s\nMessage: %s", status.getCvName(), message),
					new Exception());//passing a new exception throws a stack trace in there
		}

		final String param = "id";
		RestUri restUri = null;
		try {
			restUri = uriFactory.resourceByUriIdParam(RestResourceId.GOBII_JOB);
			restUri.setParamValue(param, jobName);
		} catch (Exception e) {
    		// Theres no real reason an exception should be thrown here
			logError("Digester", String.format("Unexpected error when generated log error. Perhaps the param name changed? " +
					                                  "Was 'id', now is %s", param, e));
		}

		GobiiEnvelopeRestResource<JobDTO, JobDTO> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(restUri);

    	JobDTO currentJob = Optional
				.of(executeGetOnCurrentJobStatus(gobiiEnvelopeRestResource))
				.map(this::validateGetResponse)
				.map(this::validateResponseContent)
				.map(this::extractJobContent)
				.get();

    	if (currentJob == null) {
			logError("Digester", "Job state could not be gotten from server");
			return;
		}

		PayloadEnvelope<JobDTO> jobStateUpdateResult = Optional
				.of(currentJob)
				.map(j -> changeValuesInJobStatus(currentJob, message, status))
				.map(j -> executePutUpdateJobStatus(gobiiEnvelopeRestResource, j))
				.map(this::verifyJobStateUpdateSuccess)
				.get();

    	if (jobStateUpdateResult == null) {
			logError("Digester", "Job state could not be updated");
			return;
		}

		job = currentJob;
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
        doUpdate(JobProgressStatusType.CV_PROGRESSSTATUS_FAILED,errorMessage);
    }

    private PayloadEnvelope<JobDTO> executeGetOnCurrentJobStatus(GobiiEnvelopeRestResource<JobDTO, JobDTO> gobiiEnvelopeRestResource) {

		PayloadEnvelope<JobDTO> resultEnvelope = null;

		try {
			resultEnvelope = gobiiEnvelopeRestResource.get(JobDTO.class);
		} catch (Exception e) {
			logError("Digester", String.format("GET Failure during status update", e));
		}

		return resultEnvelope;
	}

    private PayloadEnvelope<JobDTO> validateGetResponse(PayloadEnvelope<JobDTO> resultEnvelope){

		if (!resultEnvelope.getHeader().getStatus().isSucceeded()) {
			System.out.println();
			logError("Digester", "Job table response errors");

			for (HeaderStatusMessage currentStatusMesage : resultEnvelope.getHeader().getStatus().getStatusMessages()) {
				logError("HeaderError", currentStatusMesage.getMessage());
			}
			return null;
		}

		return resultEnvelope;
	}

	private List<JobDTO> validateResponseContent(PayloadEnvelope<JobDTO> resultEnvelope) {
		List<JobDTO> responses = resultEnvelope.getPayload().getData();
		if(responses.size() == 0) {
			logError("JobStateUpdater", String.format("No Job record returned for job %s", jobName));
			return null;
		}

		return responses;
	}

	private JobDTO extractJobContent(List<JobDTO> responses) {

		JobDTO dataSetResponse = responses.get(0);

		return dataSetResponse;
	}

	private JobDTO changeValuesInJobStatus(JobDTO job, String message, JobProgressStatusType status) {
		job.setMessage(message);
		job.setStatus(status.getCvName());
		return job;
	}

	private PayloadEnvelope<JobDTO> executePutUpdateJobStatus(GobiiEnvelopeRestResource<JobDTO, JobDTO> gobiiEnvelopeRestResource, JobDTO job) {

		PayloadEnvelope<JobDTO> resultEnvelope = null;

		try {
			resultEnvelope = gobiiEnvelopeRestResource
					.put(JobDTO.class, new PayloadEnvelope<>(job, GobiiProcessType.UPDATE));
		} catch (Exception e) {
			logError("Digester", "Could not execute PUT update on job status");
			return null;
		}

		return resultEnvelope;
	}

	private PayloadEnvelope<JobDTO> verifyJobStateUpdateSuccess(PayloadEnvelope<JobDTO> resultEnvelope) {

		if (!resultEnvelope.getHeader().getStatus().isSucceeded()) {
			logError("Digester", "Data set response response errors");
			for (HeaderStatusMessage currentStatusMesage : resultEnvelope.getHeader().getStatus().getStatusMessages()) {
				logError("HeaderError", currentStatusMesage.getMessage());
			}
			return null;
		}

		return resultEnvelope;
	}

}