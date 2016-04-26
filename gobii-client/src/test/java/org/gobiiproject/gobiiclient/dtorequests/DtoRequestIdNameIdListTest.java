// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import java.util.Map.Entry;

import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.junit.Assert;
import org.junit.Ignore;
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
        NameIdListDTO nameIdListDTO = dtoRequestNameIdList.getProjectNamesById(2);

        Assert.assertNotEquals(null, nameIdListDTO);
        Assert.assertEquals(true, nameIdListDTO.getDtoHeaderResponse().isSucceeded());
        Assert.assertTrue(nameIdListDTO.getNamesById().size() >= 0);

    }
    
    @Test
    public void testGetExperimentNamesByProjectId() throws Exception {


        // Assumes rice data with seed script is loaded
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("experiment");
        nameIdListDTORequest.setFilter("1");
        NameIdListDTO nameIdListDTO = dtoRequestNameIdList.getNamesById(nameIdListDTORequest);

        Assert.assertNotEquals(null, nameIdListDTO);
        Assert.assertEquals(true, nameIdListDTO.getDtoHeaderResponse().isSucceeded());
        Assert.assertTrue(nameIdListDTO.getNamesById().size() >= 0);

    }
    
    @Test
    public void testGetCvTermsByGroup() throws Exception {


        // Assumes rice data with seed script is loaded
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("cvgroupterms");
        nameIdListDTORequest.setFilter("map_type");
        NameIdListDTO nameIdListDTO = dtoRequestNameIdList.getNamesById(nameIdListDTORequest);

        Assert.assertNotEquals(null, nameIdListDTO);
        Assert.assertEquals(true, nameIdListDTO.getDtoHeaderResponse().isSucceeded());
        Assert.assertTrue(nameIdListDTO.getNamesById().size() >= 0);

    }
    @Test
    public void testGetPlatformNames() throws Exception {


        // Assumes rice data with seed script is loaded
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("platform");
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.getNamesById(nameIdListDTORequest);

        
        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertEquals(true, nameIdListDtoResponse.getDtoHeaderResponse().isSucceeded());
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);

    } // testGetMarkers()
    
    @Test
    public void testGetReferenceNames() throws Exception {


        // Assumes rice data with seed script is loaded
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("reference");
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.getNamesById(nameIdListDTORequest);

        
        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertEquals(true, nameIdListDtoResponse.getDtoHeaderResponse().isSucceeded());
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);

    } // testGetMarkers()
    
    @Test
    public void testGetMapNames() throws Exception {

        // Assumes rice data with seed script is loaded
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("map");
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.getNamesById(nameIdListDTORequest);

        
        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(nameIdListDtoResponse));
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
    } // testGetMarkers()
    
    @Test
    public void testGetCvTypes() throws Exception {

        // Assumes rice data with seed script is loaded
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("cvgroups");
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.getNamesById(nameIdListDTORequest);

        
        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(nameIdListDtoResponse));
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
    } // testGetMarkers()
    
    @Test
    public void testGetMapNamesByType() throws Exception {

        // Assumes rice data with seed script is loaded
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("mapNameByTypeId");
        nameIdListDTORequest.setFilter("19");
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.getNamesById(nameIdListDTORequest);

        
        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(nameIdListDtoResponse));
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
    } // testGetMarkers()
    
    @Test
    public void testGetAnalysisNames() throws Exception {

        // Assumes rice data with seed script is loaded
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("analysis");
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.getNamesById(nameIdListDTORequest);

        
        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertEquals(true, nameIdListDtoResponse.getDtoHeaderResponse().isSucceeded());
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
    } // testGetMarkers()
    
    @Test
    public void testGetManifestNames() throws Exception {

        // Assumes rice data with seed script is loaded
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("manifest");
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.getNamesById(nameIdListDTORequest);

        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertEquals(true, nameIdListDtoResponse.getDtoHeaderResponse().isSucceeded());
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);
        
        for (Entry entry : nameIdListDtoResponse.getNamesById().entrySet()){ //add project on list
			System.out.println((String) entry.getValue());//project name
		}
    }

    @Test
    public void testGetDataSetFileNames() throws Exception {

        // Assumes rice data with seed script is loaded
        NameIdListDTO nameIdListDTORequest = new NameIdListDTO();
        nameIdListDTORequest.setEntityName("dataset");
        nameIdListDTORequest.setFilter("2");
        DtoRequestNameIdList dtoRequestNameIdList = new DtoRequestNameIdList();
        NameIdListDTO nameIdListDtoResponse = dtoRequestNameIdList.getNamesById(nameIdListDTORequest);

        Assert.assertNotEquals(null, nameIdListDtoResponse);
        Assert.assertEquals(true, nameIdListDtoResponse.getDtoHeaderResponse().isSucceeded());
        Assert.assertTrue(nameIdListDtoResponse.getNamesById().size() >= 0);

        for (Entry entry : nameIdListDtoResponse.getNamesById().entrySet()){ //add project on list
            System.out.println((String) entry.getValue());//project name
        }

    }

}
