package org.gobiiproject.gobiiclient.gobii.infrastructure;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.core.common.HttpMethodResult;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContextAuth;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.headerlesscontainer.AnalysisDTO;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InterruptedIOException;

/**
 * Created by Phil on 7/7/2017.
 */
public class TestUploadDownload {

    @BeforeClass
    public static void setUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }

    @Test
    public void uploadFile() throws Exception {

        String jobId = DateUtils.makeDateIdString();
        String fileName = "test-" + jobId;
        FileWriter fileWriter = new FileWriter(fileName);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        for (Integer idx = 0; idx < 100; idx++) {
            bufferedWriter.write("Random line number " + idx);
        }
        bufferedWriter.close();
        fileWriter.close();
        File file = new File(fileName);

        GobiiEnvelopeRestResource<Void> gobiiEnvelopeRestResource = new GobiiEnvelopeRestResource<>(GobiiClientContext.getInstance(null, false)
                .getUriFactory()
                .file(jobId, GobiiFileProcessDir.EXTRACTOR_INSTRUCTIONS, fileName));

        HttpMethodResult httpMethodResult = gobiiEnvelopeRestResource.upload(file);

        Assert.assertTrue("Expected "
                + HttpStatus.SC_OK
                + " got: "
                + httpMethodResult.getResponseCode()
                + ": "
                + httpMethodResult.getReasonPhrase(),
                httpMethodResult.getResponseCode() == HttpStatus.SC_OK);

        file.delete();

    }
}
