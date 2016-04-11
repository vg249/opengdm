// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiimodel.dto.container.PingDTO;
import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DtoRequestProjectTest {


    @Test
    public void testGetPingFromExtractController() throws Exception {


        DtoRequestProject dtoRequestProject = new DtoRequestProject();
        ProjectDTO projectDTO = dtoRequestProject.getProject(1);

        Assert.assertNotEquals(null, projectDTO);
        Assert.assertNotEquals(null, projectDTO.getProjectName());

    } // testGetMarkers()


}
