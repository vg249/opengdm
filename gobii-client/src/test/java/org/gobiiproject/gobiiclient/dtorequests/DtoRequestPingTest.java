// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiimodel.dto.container.PingDTO;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DtoRequestPingTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }


    @Test
    public void testGetPingFromExtractController() throws Exception {

        List<String> requestStrings = new ArrayList<>();

        requestStrings.add("Test String 1");
        requestStrings.add("Test String 2");

        DtoRequestPing dtoRequestPing = new DtoRequestPing();
        PingDTO pingDTO = dtoRequestPing.getPingFromExtractController(requestStrings);

        Assert.assertNotEquals(null, pingDTO);
        Assert.assertNotEquals(null, pingDTO.getPingRequests());
        Assert.assertNotEquals(null, pingDTO.getPingResponses());
        Assert.assertTrue(pingDTO.getPingResponses().size() >= requestStrings.size());

    } // testGetMarkers()

    @Test
    public void testGetPingFromLoadController() throws Exception {

        List<String> requestStrings = new ArrayList<>();

        requestStrings.add("Test String 1");
        requestStrings.add("Test String 2");

        DtoRequestPing dtoRequestPing = new DtoRequestPing();
        PingDTO pingDTO = dtoRequestPing.getPingFromLoadController(requestStrings);

        Assert.assertNotEquals(null, pingDTO);
        Assert.assertNotEquals(null, pingDTO.getPingRequests());
        Assert.assertNotEquals(null, pingDTO.getPingResponses());
        Assert.assertTrue(pingDTO.getPingResponses().size() >= requestStrings.size());

    } // testGetMarkers()

}
