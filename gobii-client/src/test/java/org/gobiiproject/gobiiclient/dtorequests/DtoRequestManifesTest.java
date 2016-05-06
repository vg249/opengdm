// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import java.util.Map.Entry;

import org.gobiiproject.gobiimodel.dto.container.DisplayDTO;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.junit.Assert;
import org.junit.Test;

public class DtoRequestManifesTest {


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
