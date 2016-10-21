// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.ResourceParam;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.UriFactory;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.restmethods.RestResource;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.NameIdDTO;
import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
;
import org.gobiiproject.gobiimodel.headerlesscontainer.PlatformDTO;
import org.gobiiproject.gobiimodel.tobemovedtoapimodel.HeaderStatusMessage;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DtoRequestProjectTest {

    private static UriFactory uriFactory;

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
        String currentCropContextRoot = ClientContext.getInstance(null, false).getCurrentCropContextRoot();
        uriFactory = new UriFactory(currentCropContextRoot);
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void testCreateProject() throws Exception {


        ProjectDTO newProjectDTO = new ProjectDTO(GobiiProcessType.CREATE);
        newProjectDTO.setCreatedBy(1);
        newProjectDTO.setProjectName(UUID.randomUUID().toString());
        newProjectDTO.setProjectDescription("foo description");
        newProjectDTO.setProjectCode("foo codez");
        newProjectDTO.setProjectStatus(1);
        newProjectDTO.setModifiedBy(1);
        newProjectDTO.setPiContact(1);

        newProjectDTO.getProperties().add(new EntityPropertyDTO(null, null, "division", "foo division"));
        newProjectDTO.getProperties().add(new EntityPropertyDTO(null, null, "study_name", "foo study name"));
        newProjectDTO.getProperties().add(new EntityPropertyDTO(null, null, "genotyping_purpose", "foo purpose"));


//        DtoRequestProject dtoRequestProject = new DtoRequestProject();
//        ProjectDTO projectDTOResponse = dtoRequestProject.process(projectDTORequest);

        RestUri projectsUri = uriFactory.resourceColl(ServiceRequestId.URL_PROJECTS);
        RestResource<ProjectDTO> restResourceForProjects = new RestResource<>(projectsUri);
        PayloadEnvelope<ProjectDTO> payloadEnvelope = new PayloadEnvelope<>(newProjectDTO, GobiiProcessType.CREATE);
        PayloadEnvelope<ProjectDTO> resultEnvelope = restResourceForProjects
                .post(ProjectDTO.class, payloadEnvelope);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        ProjectDTO projectDTOResponse = resultEnvelope.getPayload().getData().get(0);


        Assert.assertNotEquals(null, projectDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(projectDTOResponse));
        Assert.assertNotEquals(null, projectDTOResponse.getProjectId());
        Assert.assertTrue(projectDTOResponse.getProjectId() > 0);

        Assert.assertNotEquals(null, projectDTOResponse.getProperties());
        Assert.assertTrue(projectDTOResponse.getProperties().size() > 0);

        EntityPropertyDTO divisionProperty = projectDTOResponse
                .getProperties()
                .stream()
                .filter(p -> p.getPropertyName().equals("division"))
                .collect(Collectors.toList())
                .get(0);

        Assert.assertTrue(divisionProperty.getEntityIdId().equals(projectDTOResponse.getProjectId()));
        Assert.assertTrue(divisionProperty.getPropertyId() > 0);

    }

    @Test
    public void testGetProjectDetails() throws Exception {

        RestUri projectsUri = uriFactory
                .resourceByUriIdParam(ServiceRequestId.URL_PROJECTS);
        projectsUri.setParamValue("id", "1");
        RestResource<ProjectDTO> restResourceForProjects = new RestResource<>(projectsUri);
        PayloadEnvelope<ProjectDTO> resultEnvelope = restResourceForProjects
                .get(ProjectDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        ProjectDTO projectDTO = resultEnvelope.getPayload().getData().get(0);

//        DtoRequestProject dtoRequestProject = new DtoRequestProject();
//        ProjectDTO projectDTORequest = new ProjectDTO();
//        projectDTORequest.setProjectId(1);
//        ProjectDTO projectDTO = dtoRequestProject.process(projectDTORequest);

        Assert.assertNotEquals(null, projectDTO);
        Assert.assertNotEquals(null, projectDTO.getProjectName());
        Assert.assertTrue(projectDTO.getProperties().size() > 0);

    } // testGetMarkers()


    @Test
    public void testCreateExistingProject() throws Exception {

        RestUri projectsUri = uriFactory
                .resourceByUriIdParam(ServiceRequestId.URL_PROJECTS);
        projectsUri.setParamValue("id", "1");
        RestResource<ProjectDTO> restResourceForProjectGet = new RestResource<>(projectsUri);
        PayloadEnvelope<ProjectDTO> resultEnvelope = restResourceForProjectGet
                .get(ProjectDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        ProjectDTO projectDTOExisting = resultEnvelope.getPayload().getData().get(0);

        projectDTOExisting.setGobiiProcessType(GobiiProcessType.CREATE);
        PayloadEnvelope<ProjectDTO> payloadEnvelope = new PayloadEnvelope<>(projectDTOExisting,
                GobiiProcessType.CREATE);

        RestResource<ProjectDTO> restResourceForProjectPost =
                new RestResource<>(uriFactory.resourceColl(ServiceRequestId.URL_PROJECTS));

         resultEnvelope = restResourceForProjectPost
                .post(ProjectDTO.class, payloadEnvelope);


        //ProjectDTO projectDTOResponse = dtoRequestProject.process(projectDTOExisting);


        List<HeaderStatusMessage> headerStatusMessages = resultEnvelope.getHeader()
                .getStatus()
                .getStatusMessages()
                .stream()
                .filter(m -> m.getGobiiValidationStatusType().equals(GobiiValidationStatusType.ENTITY_ALREADY_EXISTS))
                .collect(Collectors.toList());


        Assert.assertNotNull(headerStatusMessages);
        Assert.assertTrue(headerStatusMessages.size() > 0);

    } //


    @Test
    public void testViolateUniqueConstraintProject() throws Exception {

        RestUri projectsUri = uriFactory
                .resourceByUriIdParam(ServiceRequestId.URL_PROJECTS);
        projectsUri.setParamValue("id", "1");
        RestResource<ProjectDTO> restResourceForProjectGet = new RestResource<>(projectsUri);
        PayloadEnvelope<ProjectDTO> resultEnvelope = restResourceForProjectGet
                .get(ProjectDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        ProjectDTO projectDTOExisting = resultEnvelope.getPayload().getData().get(0);

        projectDTOExisting.setGobiiProcessType(GobiiProcessType.CREATE);
        projectDTOExisting.setProjectId(0);
        PayloadEnvelope<ProjectDTO> payloadEnvelope = new PayloadEnvelope<>(projectDTOExisting,
                GobiiProcessType.CREATE);

        RestResource<ProjectDTO> restResourceForProjectPost =
                new RestResource<>(uriFactory.resourceColl(ServiceRequestId.URL_PROJECTS));

        resultEnvelope = restResourceForProjectPost
                .post(ProjectDTO.class, payloadEnvelope);


        //ProjectDTO projectDTOResponse = dtoRequestProject.process(projectDTOExisting);


        List<HeaderStatusMessage> headerStatusMessages = resultEnvelope.getHeader()
                .getStatus()
                .getStatusMessages()
                .stream()
                .filter(m -> m.getGobiiValidationStatusType().equals(GobiiValidationStatusType.VALIDATION_COMPOUND_UNIQUE))
                .collect(Collectors.toList());


        Assert.assertNotNull(headerStatusMessages);
        Assert.assertTrue(headerStatusMessages.size() > 0);
        Assert.assertTrue(headerStatusMessages.get(0).getMessage().toLowerCase().contains("name"));
        Assert.assertTrue(headerStatusMessages.get(0).getMessage().toLowerCase().contains("contact id"));

    } // testCreateProject()

    @Test
    public void testUpdateProject() throws Exception {

//        DtoRequestProject dtoRequestProject = new DtoRequestProject();
//        ProjectDTO projectDTORequest = new ProjectDTO();
//        projectDTORequest.setProjectId(1);

        RestUri projectsUri = uriFactory
                .resourceByUriIdParam(ServiceRequestId.URL_PROJECTS);
        projectsUri.setParamValue("id", "1");
        RestResource<ProjectDTO> restResourceForProjectGet = new RestResource<>(projectsUri);
        PayloadEnvelope<ProjectDTO> resultEnvelope = restResourceForProjectGet
                .get(ProjectDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

        ProjectDTO projectDTORequestReceived = resultEnvelope.getPayload().getData().get(0);

        //ProjectDTO projectDTORequestReceived = dtoRequestProject.process(projectDTORequest);

        projectDTORequestReceived.setGobiiProcessType(GobiiProcessType.UPDATE);

        String newDescription = UUID.randomUUID().toString();

        projectDTORequestReceived.setProjectDescription(newDescription);

        String divisionPropertyNewValue = UUID.randomUUID().toString();
        EntityPropertyDTO divisionProperty = projectDTORequestReceived
                .getProperties()
                .stream()
                .filter(p -> p.getPropertyName().equals("division"))
                .collect(Collectors.toList())
                .get(0);

        divisionProperty.setPropertyValue(divisionPropertyNewValue);

        PayloadEnvelope<ProjectDTO> requestEnvelope = new PayloadEnvelope<>(projectDTORequestReceived, GobiiProcessType.UPDATE);
        resultEnvelope = restResourceForProjectGet
                .put(ProjectDTO.class,requestEnvelope);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));

       // ProjectDTO projectDTOResponse = resultEnvelope.getPayload().getData().get(0);
        /// ProjectDTO projectDTOResponse = dtoRequestProject.process(projectDTORequestReceived);



//        ProjectDTO projectDTOReRequest = new ProjectDTO();
//        projectDTOReRequest.setProjectId(1);
//        ProjectDTO dtoRequestProjectProjectReRetrieved = dtoRequestProject.process(projectDTOReRequest);


        resultEnvelope = restResourceForProjectGet
                .get(ProjectDTO.class);

       ProjectDTO dtoRequestProjectProjectReRetrieved = resultEnvelope.getPayload().getData().get(0);

        Assert.assertTrue(dtoRequestProjectProjectReRetrieved.getProjectDescription().equals(newDescription));

        EntityPropertyDTO divisionPropertyReceived = dtoRequestProjectProjectReRetrieved
                .getProperties()
                .stream()
                .filter(p -> p.getPropertyName().equals("division"))
                .collect(Collectors.toList())
                .get(0);

        Assert.assertTrue(divisionPropertyReceived.getPropertyValue().equals(divisionPropertyNewValue));


    }


}
