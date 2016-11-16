// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests.dtorequest;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiiapimodel.hateos.LinkCollection;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.UriFactory;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.restmethods.RestResource;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderFilePreviewDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.NameIdDTO;
import org.gobiiproject.gobiimodel.types.*;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DtoRequestLoaderFilePreviewTest {

    private static UriFactory uriFactory;

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());
        String currentCropContextRoot = ClientContext.getInstance(null, false).getCurrentCropContextRoot();
        DtoRequestLoaderFilePreviewTest.uriFactory = new UriFactory(currentCropContextRoot);

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }

    @Test
    public void testCreateDirectory() throws Exception {
        LoaderFilePreviewDTO loaderFilePreviewDTO = new LoaderFilePreviewDTO();
        RestUri namesUri = uriFactory.createLoaderFilesByLoaderFileName();
        namesUri.setParamValue("directoryName", "NewFilePreviewDirectory");

        RestResource<LoaderFilePreviewDTO> restResource = new RestResource<>(namesUri);
        PayloadEnvelope<LoaderFilePreviewDTO> resultEnvelope = restResource.put(LoaderFilePreviewDTO.class,
                new PayloadEnvelope<>(loaderFilePreviewDTO, GobiiProcessType.CREATE));



        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        LoaderFilePreviewDTO resultLoaderFilePreviewDTO = resultEnvelope.getPayload().getData().get(0);
        Assert.assertNotNull(loaderFilePreviewDTO.getDirectoryName());

    }

    @Test
    public void testGetFilePreview() throws Exception {
        LoaderFilePreviewDTO loaderFilePreviewDTO = new LoaderFilePreviewDTO();
        RestUri namesUri = uriFactory.fileLoaderPreviewQuery();
        namesUri.setParamValue("directoryName", "NewFilePreviewDirectory");

        RestResource<LoaderFilePreviewDTO> restResource = new RestResource<>(namesUri);
        PayloadEnvelope<LoaderFilePreviewDTO> resultEnvelope = restResource.get(LoaderFilePreviewDTO.class);



        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        LoaderFilePreviewDTO resultLoaderFilePreviewDTO = resultEnvelope.getPayload().getData().get(0);
        Assert.assertNotNull(loaderFilePreviewDTO.getDirectoryName());

    }
}
