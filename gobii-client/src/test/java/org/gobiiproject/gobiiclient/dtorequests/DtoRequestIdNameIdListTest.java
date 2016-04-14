// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;
import org.junit.Assert;
import org.junit.Test;

public class DtoRequestIdNameIdListTest {


    @Test
    public void testGetContactsByIdForContactType() throws Exception {

        // Assumes rice data with seed script is loaded
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDTO = dtoRequestNameIdList.getContactsById("PI");


        Assert.assertNotEquals(null, nameIdListDTO);
        Assert.assertEquals(true, nameIdListDTO.getDtoHeaderResponse().isSucceeded());
        Assert.assertTrue(nameIdListDTO.getNamesById().size() >= 0);

    } // testGetMarkers()

    @Test
    public void testGetProjectNamesByContactId() throws Exception {


        // Assumes rice data with seed script is loaded
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDTO = dtoRequestNameIdList.getProjectNamesByContact(2);

        Assert.assertNotEquals(null, nameIdListDTO);
        Assert.assertEquals(true, nameIdListDTO.getDtoHeaderResponse().isSucceeded());
        Assert.assertTrue(nameIdListDTO.getNamesById().size() >= 0);

    } // testGetMarkers()


}
