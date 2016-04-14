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

public class DtoRequestDisplayTest {


    @Test
    public void testGetTableDisplayNamesWithColDisplay() throws Exception {

        DtoRequestDisplay dtoRequestDisplay = new DtoRequestDisplay();

        DisplayDTO displayDTORequest = new DisplayDTO();
        displayDTORequest.getTableNamesWithColDisplay();


        DisplayDTO displayDTOResponse = dtoRequestDisplay.getDisplayNames(displayDTORequest);

        Assert.assertNotEquals(displayDTOResponse,null);
        Assert.assertTrue(displayDTOResponse.getTableNamesWithColDisplay().size() > 0);

        for(Entry e : displayDTOResponse.getTableNamesWithColDisplay().entrySet()){
            System.out.println(e.getKey().toString());
        }
//        Assert.assertNotEquals(null, nameIdListDTO);
//        Assert.assertEquals(true, nameIdListDTO.getDtoHeaderResponse().isSucceeded());
//        Assert.assertTrue(nameIdListDTO.getNamesById().size() >= 0);

    } // testGetMarkers()


}
