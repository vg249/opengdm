// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests;

import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiimodel.dto.container.PingDTO;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
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

        PingDTO pingDTORequest = TestDtoFactory.makePingDTO();
        pingDTORequest.setControllerType(ControllerType.EXTRACTOR);

        DtoRequestPing dtoRequestPing = new DtoRequestPing();
        PingDTO pingDTOResponse = dtoRequestPing.process(pingDTORequest);

        Assert.assertNotEquals(null, pingDTOResponse);
        Assert.assertNotEquals(null, pingDTOResponse.getPingRequests());
        Assert.assertNotEquals(null, pingDTOResponse.getPingResponses());
        Assert.assertTrue(pingDTOResponse.getPingResponses().size()
                >= pingDTORequest.getPingRequests().size());

    } // testGetMarkers()


    @Test
    public void testGetPingFromLoadController() throws Exception {

        PingDTO pingDTORequest = TestDtoFactory.makePingDTO();
        pingDTORequest.setControllerType(ControllerType.LOADER);

        DtoRequestPing dtoRequestPing = new DtoRequestPing();
        PingDTO pingDTOResponse = dtoRequestPing.process(pingDTORequest);

        Assert.assertNotEquals(null, pingDTOResponse);
        Assert.assertNotEquals(null, pingDTOResponse.getPingRequests());
        Assert.assertNotEquals(null, pingDTOResponse.getPingResponses());
        Assert.assertTrue(pingDTOResponse.getPingResponses().size()
                >= pingDTORequest.getPingRequests().size());

    } // testGetMarkers()

}
