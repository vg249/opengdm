package org.gobiiproject.gobiiprocess;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.gobiiproject.gobiiapimodel.payload.HeaderStatusMessage;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.cvnames.CvGroupTerm;
import org.gobiiproject.gobiimodel.cvnames.JobProgressStatusType;
import org.gobiiproject.gobiimodel.dto.noaudit.JobDTO;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.entity.Job;
import org.gobiiproject.gobiimodel.types.GobiiAutoLoginType;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.utils.error.Logger;
import org.gobiiproject.gobiisampletrackingdao.CvDao;
import org.gobiiproject.gobiisampletrackingdao.GobiiDaoException;
import org.gobiiproject.gobiisampletrackingdao.JobDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.transaction.Transactional;

import static org.gobiiproject.gobiimodel.utils.error.Logger.logError;

@Transactional
public class JobStatus {

    Job job;

	private static Set<String> acceptedStatuses=new HashSet<>(Arrays.asList(
			JobProgressStatusType.CV_PROGRESSSTATUS_ABORTED.getCvName(),
			JobProgressStatusType.CV_PROGRESSSTATUS_COMPLETED.getCvName(),
			JobProgressStatusType.CV_PROGRESSSTATUS_DIGEST.getCvName(),
			JobProgressStatusType.CV_PROGRESSSTATUS_INPROGRESS.getCvName(),
			JobProgressStatusType.CV_PROGRESSSTATUS_MATRIXLOAD.getCvName(),
			JobProgressStatusType.CV_PROGRESSSTATUS_METADATALOAD.getCvName(),
			JobProgressStatusType.CV_PROGRESSSTATUS_TRANSFORMATION.getCvName(),
			JobProgressStatusType.CV_PROGRESSSTATUS_VALIDATION.getCvName(),
			JobProgressStatusType.CV_PROGRESSSTATUS_FAILED.getCvName(),
			JobProgressStatusType.CV_PROGRESSSTATUS_METADATAEXTRACT.getCvName(),
			JobProgressStatusType.CV_PROGRESSSTATUS_FINALASSEMBLY.getCvName(),
			JobProgressStatusType.CV_PROGRESSSTATUS_QCPROCESSING.getCvName()
	));

	private static ApplicationContext context = new ClassPathXmlApplicationContext(
        "classpath:/spring/application-config.xml");

    public JobStatus(String jobName) throws GobiiDaoException {
        JobDao jobDao = context.getBean(JobDao.class);
        this.job = jobDao.getByName(jobName);
    }

    public void set(String status,String message) {

    	if(status==null || !acceptedStatuses.contains(status)) {
    		Logger.logError(
    		    "JobStatus","Invalid status passed to set: "+status+"\nMessage: "+message,
                new Exception());
		}
    	try{
            JobDao jobDao = context.getBean(JobDao.class);
    	    CvDao cvDao = context.getBean(CvDao.class);
    	    List<Cv> statuses = cvDao.getCvs(status,
                CvGroupTerm.CVGROUP_JOBSTATUS.getCvGroupName(),
                GobiiCvGroupType.GROUP_TYPE_SYSTEM);

    	    if(statuses.size() != 1) {
                Logger.logError(
                    "JobStatus","Invalid status passed to set: "+status+"\nMessage: "+message,
                    new Exception());
            }

    	    Cv statusCv = statuses.get(0);

    	    this.job.setStatus(statusCv);
    	    this.job.setMessage(message);
    	    this.job = jobDao.update(this.job);

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
        if(this.job!=null){
            JobDao jobDao = context.getBean(JobDao.class);
            this.job = jobDao.getByName(this.job.getJobName());
            errorMessage="Status: " + this.job.getStatus().getTerm()+" - "
                + this.job.getMessage() + " | \n";
        }
        errorMessage += message + " : " + Logger.getFirstErrorReason();
        set(JobProgressStatusType.CV_PROGRESSSTATUS_FAILED.getCvName(),errorMessage);
    }
}