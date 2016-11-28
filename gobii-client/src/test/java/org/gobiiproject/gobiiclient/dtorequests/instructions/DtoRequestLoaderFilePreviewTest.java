// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.dtorequests.instructions;

import org.apache.commons.io.FileUtils;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.UriFactory;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.restmethods.RestResource;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderFilePreviewDTO;
import org.gobiiproject.gobiimodel.types.*;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DtoRequestLoaderFilePreviewTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(Authenticator.authenticate());

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }

    @Test
    public void testCreateDirectory() throws Exception {
        LoaderFilePreviewDTO loaderFilePreviewDTO = new LoaderFilePreviewDTO();

        RestUri namesUri = ClientContext
                .getInstance(null,false)
                .getUriFactory()
                .createLoaderFilesByLoaderFileName();

        namesUri.setParamValue("directoryName", TestDtoFactory.getRandomName("newFolder"));
        RestResource<LoaderFilePreviewDTO> restResource = new RestResource<>(namesUri);
        PayloadEnvelope<LoaderFilePreviewDTO> resultEnvelope = restResource.put(LoaderFilePreviewDTO.class,
                new PayloadEnvelope<>(loaderFilePreviewDTO, GobiiProcessType.CREATE));



        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        LoaderFilePreviewDTO  resultLoaderFilePreviewDTO = resultEnvelope.getPayload().getData().get(0);
        Assert.assertNotNull(resultLoaderFilePreviewDTO.getDirectoryName());

    }

    @Test
    public void testGetFilePreview() throws Exception {
        //Create newFolder
        LoaderFilePreviewDTO loaderFileCreateDTO = new LoaderFilePreviewDTO();
        RestUri namesUriCreate = ClientContext
                .getInstance(null,false)
                .getUriFactory()
                .createLoaderFilesByLoaderFileName();
        namesUriCreate.setParamValue("directoryName", TestDtoFactory.getRandomName("newFolder"));
        RestResource<LoaderFilePreviewDTO> restResourceCreate = new RestResource<>(namesUriCreate);
        PayloadEnvelope<LoaderFilePreviewDTO> resultEnvelopeCreate = restResourceCreate.put(LoaderFilePreviewDTO.class,
                new PayloadEnvelope<>(loaderFileCreateDTO, GobiiProcessType.CREATE));

        LoaderFilePreviewDTO  resultLoaderFilePreviewDTOCreated = resultEnvelopeCreate.getPayload().getData().get(0);


        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelopeCreate.getHeader()));
        Assert.assertNotNull(resultLoaderFilePreviewDTOCreated.getDirectoryName());

        //copyContentsFromCreatedFolder
        File resourcesDirectory = new File("src/test/resources/datasets");
        File dst = new File(resultLoaderFilePreviewDTOCreated.getDirectoryName());

        try {
            FileUtils.copyDirectory(resourcesDirectory, dst);   
        } catch (IOException e) {
            e.printStackTrace();
        }


        //retrieve contents from created name
        RestUri namesUri = ClientContext
                .getInstance(null,false)
                .getUriFactory()
                .fileLoaderPreviewQuery();
        namesUri.setParamValue("directoryName", dst.getName());
        namesUri.setParamValue("fileFormat", "txt");

        RestResource<LoaderFilePreviewDTO> restResource = new RestResource<>(namesUri);
        PayloadEnvelope<LoaderFilePreviewDTO> resultEnvelope = restResource.get(LoaderFilePreviewDTO.class);

        Assert.assertFalse(TestUtils.checkAndPrintHeaderMessages(resultEnvelope.getHeader()));
        LoaderFilePreviewDTO resultLoaderFilePreviewDTO = resultEnvelope.getPayload().getData().get(0);
        Assert.assertNotNull(resultLoaderFilePreviewDTO.getDirectoryName());
        Assert.assertNotNull(resultLoaderFilePreviewDTO.getFileList().get(0));

        //compare results read to file
        Assert.assertTrue(TestDtoFactory.checkPreviewFileMatch(resultLoaderFilePreviewDTO.getFilePreview(), resourcesDirectory,resultLoaderFilePreviewDTO.getFileList().get(0)));


    }
}
