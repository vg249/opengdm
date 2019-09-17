package org.gobiiproject.bert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ernie.core.Action;
import ernie.core.Clean;
import ernie.core.Verify;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiimodel.cvnames.JobProgressStatusType;
import org.gobiiproject.gobiimodel.cvnames.JobType;
import org.gobiiproject.gobiimodel.dto.entity.auditable.*;
import org.gobiiproject.gobiimodel.dto.entity.children.PropNameId;
import org.gobiiproject.gobiimodel.dto.entity.children.VendorProtocolDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CvDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.CvGroupDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DataSetDTO;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.JobDTO;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderProcedure;
import org.gobiiproject.gobiimodel.dto.system.AuthDTO;
import org.gobiiproject.gobiimodel.dto.system.PingDTO;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static org.gobiiproject.bert.ComponentsUtil.*;

public class Components {

	private static final String HEADER_USERNAME = "X-Username";
	private static final String HEADER_PASSWORD = "X-Password";
	private static final String HEADER_AUTH_TOKEN = "X-Auth-Token";

	private static final String URL_BASE = "%s:%s%s";

	private static final String URL_AUTH = "/auth";
	private static final String URL_PING = "/ping";
	private static final String URL_CONTACT_SEARCH = "/contact-search";
	private static final String URL_PROJECT = "/projects";
	private static final String URL_EXPERIMENTS = "/experiments";
	private static final String URL_PROTOCOLS = "/protocols";
	private static final String URL_PLATFORMS = "/platforms";
	private static final String URL_VENDOR_PROTOCOL = "/protocols/%s/vendors";
	private static final String URL_ORGANIZATIONS = "/organizations";
	private static final String URL_JOBS = "/jobs";
	private static final String URL_MAPSETS = "/mapsets";
	private static final String URL_ANALYSES = "/analyses";
	private static final String URL_DATASETS = "/datasets";
	private static final String URL_CVS = "/cvs";
	private static final String URL_CV_GROUP = "/cvgroups/%s";

	private String host;
	private Long port;
	private String path;
	private String authToken;
	private String user;
	private String password;

	private String url(String endpoint, Object ... params) {
		return "http://" + String.format(String.format(URL_BASE, host, port, path) + endpoint, params);
	}

	private HttpHeaders authHeader() {
		return headers(HEADER_AUTH_TOKEN, authToken, HEADER_USERNAME, user, HEADER_PASSWORD, password);
	}

	@Action("host")
	public ResponseEntity<AuthDTO> host(String host, Long port, String path, String user, String password) {
		this.host = host;
		this.port = port;
		this.path = path;
		ResponseEntity<AuthDTO> response = post(url(URL_AUTH),
				headers(HEADER_USERNAME, user,
						HEADER_PASSWORD, password),
				AuthDTO.class);

		return response;
	}

	@Verify("host")
	public boolean verifyHost(ResponseEntity<AuthDTO> response, String host, Long port, String path, String user, String password) {
		String authToken = response.getHeaders().get(HEADER_AUTH_TOKEN).get(0);

		ResponseEntity<PingDTO> pingResponse = post(url(URL_PING), headers(HEADER_AUTH_TOKEN, authToken),
													 PingDTO.class, new PingDTO());

		if (HttpStatus.OK.equals(response.getStatusCode())
			&& HttpStatus.OK.equals(pingResponse.getStatusCode())) {
			this.authToken = authToken;
			this.user = user;
			this.password = password;
			return true;
		}
		return true;
	}

	@Action("principalInvestigator")
	public Integer principalInvestigator(String name) throws Exception {

		String[] tokens = name.split(" ");

		ResponseEntity<JsonNode> response;

		if (tokens.length == 1) {
			response = get(url(URL_CONTACT_SEARCH), authHeader(), JsonNode.class,
					"firstName", tokens[0]);
		} else if (tokens.length == 2) {
			response = get(url(URL_CONTACT_SEARCH), authHeader(), JsonNode.class,
					"firstName", tokens[0], "lastName", tokens[1]);
		} else {
			throw new Exception("Principal Investigator should be provide a first name, or a first name and a last name");
		}

		return getIn(response.getBody(), "payload", "data", 0, "contactId").asInt();
	}

	@Action("project")
	public ProjectDTO createProject(Long principalInvestigatorId, String name) throws Exception {

		ProjectDTO project = new ProjectDTO();
		project.setCreatedBy(1);
		project.setProjectName(name);
		project.setProjectCode("_" + name);
		project.setModifiedBy(1);
		project.setCreatedBy(1);
		project.setProjectStatus(1);
		project.setPiContact(principalInvestigatorId.intValue());

		PayloadEnvelope<ProjectDTO> envelope = new PayloadEnvelope<>(project, GobiiProcessType.CREATE);

		try {
			ResponseEntity<JsonNode> response = post(url(URL_PROJECT), authHeader(), JsonNode.class, envelope);
			return result(response, ProjectDTO.class);
		} catch (HttpStatusCodeException e) {
			printHttpError(e);
			throw e;
		}
	}

	@Action("protocol")
	public ProtocolDTO createProtocol(PlatformDTO platform, String name) throws Exception {

		ProtocolDTO protocol = new ProtocolDTO();

		protocol.setName(name);
		protocol.setPlatformId(platform.getId());
		protocol.setModifiedBy(1);
		protocol.setCreatedBy(1);
		protocol.setTypeId(1);
		protocol.setStatus(1);

		PayloadEnvelope<ProtocolDTO> envelope = new PayloadEnvelope<>(protocol, GobiiProcessType.CREATE);

		try {
			ResponseEntity<JsonNode> response = post(url(URL_PROTOCOLS), authHeader(), JsonNode.class, envelope);
			return result(response, ProtocolDTO.class);
		} catch (HttpStatusCodeException e) {
			printHttpError(e);
			throw e;
		}
	}

	@Action("platform")
	public PlatformDTO createPlatform(String name) throws Exception {

		PlatformDTO platform = new PlatformDTO();
		platform.setPlatformName(name);
		platform.setPlatformCode(name);
		platform.setModifiedBy(1);
		platform.setCreatedBy(1);
		platform.setStatusId(1);
		platform.setTypeId(1);

		PayloadEnvelope<PlatformDTO> envelope = new PayloadEnvelope<>(platform, GobiiProcessType.CREATE);

		try {
			ResponseEntity<JsonNode> response = post(url(URL_PLATFORMS), authHeader(), JsonNode.class, envelope);
			return result(response, PlatformDTO.class);
		} catch (HttpStatusCodeException e) {
			printHttpError(e);
			throw e;
		}
	}

	@Action("experiment")
	public ExperimentDTO createExperiment(ProjectDTO project, ProtocolDTO protocol, String name) throws Exception {

		ExperimentDTO experiment = new ExperimentDTO();
		experiment.setExperimentName(name);
		experiment.setExperimentCode(name);
		experiment.setProjectId(project.getId());
		experiment.setModifiedBy(1);
		experiment.setCreatedBy(1);
		experiment.setStatusId(1);

		PayloadEnvelope<ExperimentDTO> envelope = new PayloadEnvelope<>(experiment, GobiiProcessType.CREATE);

		try {
			ResponseEntity<JsonNode> response = post(url(URL_EXPERIMENTS), authHeader(), JsonNode.class, envelope);
			return result(response, ExperimentDTO.class);
		} catch (HttpStatusCodeException e) {
			printHttpError(e);
			throw e;
		}
	}

	@Action("organization")
	public OrganizationDTO organization(String name) throws Exception{
		OrganizationDTO organization = new OrganizationDTO();

		organization.setName(name);
		organization.setStatusId(1);
		organization.setAddress("");
		organization.setWebsite("");
		organization.setCreatedBy(1);
		organization.setModifiedBy(1);

		PayloadEnvelope<OrganizationDTO> envelope = new PayloadEnvelope<>(organization, GobiiProcessType.CREATE);

		try {
			ResponseEntity<JsonNode> response = post(url(URL_ORGANIZATIONS), authHeader(), JsonNode.class, envelope);
			return result(response, OrganizationDTO.class);
		} catch (HttpStatusCodeException e) {
			printHttpError(e);
			throw e;
		}
	}

	@Action("vendorProtocol")
	public VendorProtocolDTO vendorProtocol(OrganizationDTO vendor, ProtocolDTO protocol) throws Exception {

		try {
			PayloadEnvelope<OrganizationDTO> envelope = new PayloadEnvelope<>(vendor, GobiiProcessType.CREATE);
			ResponseEntity<JsonNode> response = post(url(URL_VENDOR_PROTOCOL, protocol.getId()), authHeader(), JsonNode.class, envelope);
			OrganizationDTO updatedVendor = result(response, OrganizationDTO.class);
			
			return updatedVendor.getVendorProtocols().stream()
					.filter(vp -> vp.getProtocolId().equals(protocol.getId()))
					.findFirst().get();
		} catch (HttpStatusCodeException e) {
			printHttpError(e);
			throw e;
		}
	}

	@Action("mapset")
	public MapsetDTO mapset(String name) {
		MapsetDTO mapset = new MapsetDTO();

		mapset.setName(name);
		mapset.setDescription(name);
		mapset.setCode("");
		mapset.setMapType(1);
		mapset.setModifiedBy(1);
		mapset.setCreatedBy(1);
		mapset.setStatusId(1);

		PayloadEnvelope<MapsetDTO> envelope = new PayloadEnvelope<>(mapset, GobiiProcessType.CREATE);

		try {
			ResponseEntity<JsonNode> response = post(url(URL_MAPSETS), authHeader(), JsonNode.class, envelope);
			return result(response, MapsetDTO.class);
		} catch (HttpStatusCodeException e) {
			printHttpError(e);
			throw e;
		}
	}

	@Action("cv")
	public CvDTO cv(CvGroupDTO group, String term, String definition) {

		CvDTO cv = new CvDTO();

		cv.setTerm(term);
		cv.setDefinition(definition);
		cv.setGroupId(group.getCvGroupId());
		cv.setRank(1);
		cv.setEntityStatus(1);

		PayloadEnvelope<CvDTO> envelope = new PayloadEnvelope<>(cv, GobiiProcessType.CREATE);

		try {
			ResponseEntity<JsonNode> response = post(url(URL_CVS), authHeader(), JsonNode.class, envelope);
			return result(response, CvDTO.class);
		} catch (HttpStatusCodeException e) {
			printHttpError(e);
			throw e;
		}
	}

	@Action("cvGroup")
	public CvGroupDTO cvGroup(String name) {
		try {
			ResponseEntity<JsonNode> cvGroup = get(url(URL_CV_GROUP, name), authHeader(), JsonNode.class, "cvGroupTypeId", GobiiCvGroupType.GROUP_TYPE_SYSTEM.getGroupTypeId().toString());
			return result(cvGroup, CvGroupDTO.class);
		} catch (HttpStatusCodeException e) {
			printHttpError(e);
			throw e;
		}
	}

	@Action("analysis")
	public AnalysisDTO analysis(CvDTO cv, String name) {

		AnalysisDTO analysis = new AnalysisDTO();

		analysis.setAnalysisName(name);
		analysis.setAnlaysisTypeId(cv.getCvId());
		analysis.setAnalysisDescription(name);
		analysis.setProgram(name);
		analysis.setProgramVersion(name);
		analysis.setAlgorithm(name);
		analysis.setSourceName(name);
		analysis.setSourceUri(name);
		analysis.setSourceName(name);
		analysis.setSourceVersion(name);
		analysis.setStatusId(1);
		analysis.setCreatedBy(1);
		analysis.setModifiedBy(1);

		PayloadEnvelope<AnalysisDTO> envelope = new PayloadEnvelope<>(analysis, GobiiProcessType.CREATE);

		try {
			ResponseEntity<JsonNode> response = post(url(URL_ANALYSES), authHeader(), JsonNode.class, envelope);
			return result(response, AnalysisDTO.class);
		} catch (HttpStatusCodeException e) {
			printHttpError(e);
			throw e;
		}
	}

	@Action("dataset")
	public DataSetDTO dataset(ExperimentDTO experiment, AnalysisDTO analysis, String name) {
		DataSetDTO dataset = new DataSetDTO();
		dataset.setDatasetName(name);
		dataset.setExperimentId(experiment.getExperimentId());
		dataset.setCallingAnalysisId(analysis.getId());
		dataset.setStatusId(1);
		dataset.setCreatedBy(1);
		dataset.setModifiedBy(1);

		PayloadEnvelope<DataSetDTO> envelope = new PayloadEnvelope<>(dataset, GobiiProcessType.CREATE);

		try {
			ResponseEntity<JsonNode> response = post(url(URL_DATASETS), authHeader(), JsonNode.class, envelope);
			return result(response, DataSetDTO.class);
		} catch (HttpStatusCodeException e) {
			printHttpError(e);
			throw e;
		}
	}

	@Action("job")
	public JobDTO job(DataSetDTO dataset, String jobPayloadType, String name) {
		try {
			JobDTO job = new JobDTO();
			job.setSubmittedBy(1);
			job.setMessage("");
			job.setPayloadType(jobPayloadType);
			job.setType(JobType.CV_JOBTYPE_LOAD.getCvName());
			job.setStatus(JobProgressStatusType.CV_PROGRESSSTATUS_PENDING.getCvName());
			job.setSubmittedDate(new Date());
			job.setJobName(name);
			job.getDatasetIds().add(dataset.getDataSetId());

			PayloadEnvelope<JobDTO> envelope = new PayloadEnvelope<>(job, GobiiProcessType.CREATE);
			ResponseEntity<JsonNode> response = post(url(URL_JOBS), authHeader(), JsonNode.class, envelope);

			return result(response, JobDTO.class);
		} catch (HttpStatusCodeException e) {
			printHttpError(e);
			throw e;
		}
	}

	@Action("load")
	public GobiiLoaderProcedure load(ProjectDTO project, ExperimentDTO experiment, PlatformDTO platform, DataSetDTO dataset,
									 String procedureFilePath, String dataFolderPath) throws IOException {

		GobiiLoaderProcedure procedure = new ObjectMapper().readValue(slurp(procedureFilePath), GobiiLoaderProcedure.class);
		String jobPayloadType = procedure.getMetadata().getJobPayloadType().getCvName();

		String randomString = randomString();

		String dataSrc = procedure.getMetadata().getGobiiFile().getSource();

		scpDirectory(this.host, this.user, dataFolderPath, dataSrc);

		String procedureFileName = new File(procedureFilePath).getName();
		procedureFileName = procedureFileName.substring(0, procedureFileName.lastIndexOf(".json"));
		procedureFileName += randomString;

		job(dataset, jobPayloadType, procedureFileName);

		fillInProcedurePrototype(procedure, project, experiment, platform, dataset);

		String procedureFileRemoteDirectory = "/data/gobii_bundle/crops/" + procedure.getMetadata().getGobiiCropType() + "/loader/inprogress/";
		String procedureFileRemotePath = procedureFileRemoteDirectory + procedureFileName + ".json";

		ssh(this.host, this.user, "mkdir -p " + procedure.getMetadata().getGobiiFile().getDestination());

		scpContent(this.host, this.user, new ObjectMapper().writeValueAsString(procedure), procedureFileRemotePath);

		ssh(this.host, this.user,
				String.format("docker exec "
								+ "-w /data/gobii_bundle/core "
								+ "-i gobii-compute-node "
								+ "java -jar Digester.jar %s", procedureFileRemotePath));

		ssh(this.host, this.user, "ls");
		return procedure;
	}

	@Clean("load")
	public void cleanLoad(GobiiLoaderProcedure procedure, String procedureFilePath, String dataFolderPath) throws IOException {

		String procedureFileRemoteDirectory = "/data/gobii_bundle/crops/" + procedure.getMetadata().getGobiiCropType() + "/loader/inprogress/";
		String procedureFileName = new File(procedureFilePath).getName();
		String procedureFileRemotePath = procedureFileRemoteDirectory + procedureFileName + ".json";
		String dataSrc = procedure.getMetadata().getGobiiFile().getSource();

		ssh(this.host, this.user, "rm " + procedureFileRemotePath);
		ssh(this.host, this.user, "rm -r " + dataSrc);
	}

	private static void fillInProcedurePrototype(GobiiLoaderProcedure procedure, ProjectDTO project, ExperimentDTO experiment, PlatformDTO platform, DataSetDTO dataset) {

		procedure.getMetadata()
				.setGobiiJobStatus(JobProgressStatusType.CV_PROGRESSSTATUS_NOSTATUS)
				.setQcCheck(false)
				.setProject(new PropNameId(project.getId(), project.getProjectName()))
				.setExperiment(new PropNameId(experiment.getId(), experiment.getExperimentName()))
				.setPlatform(new PropNameId(platform.getId(), platform.getPlatformName()))
				.setDataset(new PropNameId(dataset.getDataSetId(), dataset.getDatasetName()));

	}
}
