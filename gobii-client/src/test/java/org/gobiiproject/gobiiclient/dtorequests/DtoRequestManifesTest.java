// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import java.util.Map.Entry;

import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DtoRequestManifesTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }



    @Test
    public void testGetTableDisplayNamesWithColDisplay() throws Exception {

    	 NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
         nameIdListDTORequest.setEntityName("manifest");
         DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
         NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.getNamesById(nameIdListDTORequest);

         Assert.assertNotEquals(null, nameIdListDtoResponse);
         Assert.assertEquals(true, nameIdListDtoResponse.getDtoHeaderResponse().isSucceeded());
         Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
    } // testGetMarkers()


}
