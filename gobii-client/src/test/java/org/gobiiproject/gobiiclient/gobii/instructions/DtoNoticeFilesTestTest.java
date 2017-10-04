// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.gobii.instructions;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpStatus;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.HttpMethodResult;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.core.gobii.GobiiTestConfiguration;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestDtoFactory;
import org.gobiiproject.gobiiclient.gobii.Helpers.TestUtils;
import org.gobiiproject.gobiimodel.headerlesscontainer.LoaderFilePreviewDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class DtoNoticeFilesTestTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.authenticate());

    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }


    @Test
    public void testGetFileFromServer() throws Exception {


        //*************************************************************
        // ****************** Prove that file is not there
        ClassLoader classLoader = getClass().getClassLoader();
        String confidentialityFileName = "confidentiality.txt";
        RestUri restUriDelete = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .file(GobiiFileProcessDir.NOTICES, confidentialityFileName);

        HttpMethodResult httpMethodResultDeleteNonExistent = GobiiClientContext.getInstance(null, false)
                .getHttp()
                .delete(restUriDelete);
        Assert.assertTrue("Expected "
                        + HttpStatus.SC_NOT_ACCEPTABLE
                        + " got: "
                        + httpMethodResultDeleteNonExistent.getResponseCode()
                        + ": "
                        + httpMethodResultDeleteNonExistent.getReasonPhrase() + ": " + httpMethodResultDeleteNonExistent.getPlainPayload(),
                httpMethodResultDeleteNonExistent.getResponseCode() == HttpStatus.SC_NOT_ACCEPTABLE);


        //*************************************************************
        // NOW UPLOAD THE FILE
        File defaultConfidentialityNotice = new File(classLoader.getResource(confidentialityFileName).getFile());

        RestUri restUriUpload = GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .file(GobiiFileProcessDir.NOTICES, confidentialityFileName);

        HttpMethodResult httpMethodResultUpload = GobiiClientContext.getInstance(null, false)
                .getHttp()
                .upload(restUriUpload, defaultConfidentialityNotice);
        Assert.assertTrue("Expected "
                        + HttpStatus.SC_OK
                        + " got: "
                        + httpMethodResultUpload.getResponseCode()
                        + ": "
                        + httpMethodResultUpload.getReasonPhrase() + ": " + httpMethodResultUpload.getPlainPayload(),
                httpMethodResultUpload.getResponseCode() == HttpStatus.SC_OK);


        //*************************************************************
        // ****************** Prove that file is there implicitly (i.e., delete succeeds)
        HttpMethodResult httpMethodResultDeleteExistent = GobiiClientContext.getInstance(null, false)
                .getHttp()
                .delete(restUriDelete);
        Assert.assertTrue("Expected "
                        + HttpStatus.SC_OK
                        + " got: "
                        + httpMethodResultDeleteExistent.getResponseCode()
                        + ": "
                        + httpMethodResultDeleteExistent.getReasonPhrase() + ": " + httpMethodResultDeleteExistent.getPlainPayload(),
                httpMethodResultDeleteExistent.getResponseCode() == HttpStatus.SC_OK);


        //*************************************************************
        // ****************** Prove that file was deleted (i.e., delete fails)
        HttpMethodResult httpMethodResultDeleteExistentThatWasDeleted = GobiiClientContext.getInstance(null, false)
                .getHttp()
                .delete(restUriDelete);
        Assert.assertTrue("Expected "
                        + HttpStatus.SC_NOT_ACCEPTABLE
                        + " got: "
                        + httpMethodResultDeleteExistentThatWasDeleted.getResponseCode()
                        + ": "
                        + httpMethodResultDeleteExistentThatWasDeleted.getReasonPhrase() + ": " + httpMethodResultDeleteExistentThatWasDeleted.getPlainPayload(),
                httpMethodResultDeleteExistentThatWasDeleted.getResponseCode() == HttpStatus.SC_NOT_ACCEPTABLE);


    }


}
