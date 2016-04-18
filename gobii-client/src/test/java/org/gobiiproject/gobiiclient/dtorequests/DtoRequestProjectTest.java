// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiimodel.dto.DtoMetaData;
import org.gobiiproject.gobiimodel.dto.container.project.ProjectDTO;
import org.junit.Assert;
import org.junit.Test;

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

        ProjectDTO projectDTOResponse = dtoRequestProject.updateProject(projectDTORequest);

//        Assert.assertNotEquals(null, projectDTOResponse);
//        Assert.assertNotEquals(null, projectDTOResponse.getProjectName());
//        Assert.assertTrue(projectDTOResponse.getProperties().size() > 0);

    } // testGetMarkers()


}
