package org.gobiiproject.gobiiclient.dtorequests.infrastructure;

import org.gobiiproject.gobiiclient.core.common.Authenticator;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by VCalaminos on 3/14/2017.
 */
public class TestGobiiTestData {

    private static String FILE_PATH = "vcf_test.xml";

    @BeforeClass
    public static void setUpClass() throws Exception {

        Assert.assertTrue(Authenticator.authenticate());
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        Assert.assertTrue(Authenticator.deAuthenticate());
    }

    @Test
    public void testGobiiTestData() throws Exception {

        String commandLine = "java -DcfgFqpn=C:\\Users\\VCalaminos\\Documents\\gobii\\config\\gobii-web.xml -jar C:\\Users\\VCalaminos\\Documents\\gobii\\gobiiproject\\gobii-process\\target\\gobiitestdata.jar";

        boolean succeded = HelperFunctions.tryExec(commandLine, "output.txt", "error.txt");

    }

}
