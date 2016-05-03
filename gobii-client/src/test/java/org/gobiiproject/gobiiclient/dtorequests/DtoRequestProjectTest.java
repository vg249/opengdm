// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;
import org.gobiiproject.gobiimodel.dto.container.EntityPropertyDTO;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderResponse;
import org.gobiiproject.gobiimodel.dto.header.HeaderStatusMessage;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.stream.Collectors;

public class DtoRequestProjectTest {


    @Test
    public void testGetProject() throws Exception {


        DtoRequestProject dtoRequestProject = new DtoRequestProject();
        ProjectDTO projectDTO = dtoRequestProject.getProject(1);

        Assert.assertNotEquals(null, projectDTO);
        Assert.assertNotEquals(null, projectDTO.getProjectName());
        Assert.assertTrue(projectDTO.getProperties().size() > 0);

    } // testGetMarkers()

    @Test
    public void testCreateProject() throws Exception {


        DtoRequestProject dtoRequestProject = new DtoRequestProject();
        ProjectDTO projectDTORequest = new ProjectDTO(DtoMetaData.ProcessType.CREATE);
        projectDTORequest.setCreatedBy(1);
        projectDTORequest.setProjectName("foo projects");
        projectDTORequest.setProjectDescription("foo description");
        projectDTORequest.setProjectCode("foo codez");
        projectDTORequest.setProjectStatus(1);
        projectDTORequest.setModifiedBy(1);
        projectDTORequest.setPiContact(1);

        projectDTORequest.getProperties().add(new EntityPropertyDTO(null, null, "division", "foo division"));
        projectDTORequest.getProperties().add(new EntityPropertyDTO(null, null, "study_name", "foo study name"));
        projectDTORequest.getProperties().add(new EntityPropertyDTO(null, null, "genotyping_purpose", "foo purpose"));

        ProjectDTO projectDTOResponse = dtoRequestProject.process(projectDTORequest);

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

        Assert.assertTrue(divisionProperty.getEntityIdId() == projectDTOResponse.getProjectId());
        Assert.assertTrue(divisionProperty.getPropertyId() > 0);

    } // testGetMarkers()

    @Ignore
    public void testCreateExistingProject() throws Exception {

        DtoRequestProject dtoRequestProject = new DtoRequestProject();
        ProjectDTO projectDTOExisting = dtoRequestProject.getProject(1);
        projectDTOExisting.setProcessType(DtoMetaData.ProcessType.UPDATE);


        ProjectDTO projectDTOResponse = dtoRequestProject.process(projectDTOExisting);



        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(projectDTOResponse));


        HeaderStatusMessage headerStatusMessage = projectDTOResponse
                .getDtoHeaderResponse()
                .getStatusMessages()
                .stream()
                .filter(m -> m.getValidationStatusType() == DtoHeaderResponse.ValidationStatusType.VALIDATION_COMPOUND_UNIQUE)
                .collect(Collectors.toList())
                .get(0);

        Assert.assertNotNull(headerStatusMessage);
        Assert.assertTrue(headerStatusMessage.getMessage().toLowerCase().contains("contact"));
        Assert.assertTrue(headerStatusMessage.getMessage().toLowerCase().contains("name"));



    } // testCreateProject()


}
