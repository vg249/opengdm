// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.project.ProjectDTO;
import org.gobiiproject.gobiimodel.dto.container.project.ProjectProperty;
import org.junit.Assert;
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
        projectDTORequest.setProjectName("foo project");
        projectDTORequest.setProjectDescription("foo description");
        projectDTORequest.setProjectCode("foo codez");
        projectDTORequest.setProjectStatus(1);
        projectDTORequest.setModifiedBy(1);
        projectDTORequest.setPiContact(1);

        projectDTORequest.getProperties().add(new ProjectProperty(null, "division", "foo division"));
        projectDTORequest.getProperties().add(new ProjectProperty(null, "study_name", "foo study name"));
        projectDTORequest.getProperties().add(new ProjectProperty(null, "genotyping_purpose", "foo purpose"));

        ProjectDTO projectDTOResponse = dtoRequestProject.updateProject(projectDTORequest);

        Assert.assertNotEquals(null, projectDTOResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(projectDTOResponse));
        Assert.assertNotEquals(null, projectDTOResponse.getProjectId());
        Assert.assertTrue(projectDTOResponse.getProjectId() > 0);

        Assert.assertNotEquals(null, projectDTOResponse.getProperties());
        Assert.assertTrue(projectDTOResponse.getProperties().size() > 0);

        ProjectProperty divisionProperty = projectDTOResponse
                .getProperties()
                .stream()
                .filter(p -> p.getPropertyName().equals("division"))
                .collect(Collectors.toList())
                .get(0);

        Assert.assertTrue(divisionProperty.getProjectId() == projectDTOResponse.getProjectId());

    } // testGetMarkers()


}
