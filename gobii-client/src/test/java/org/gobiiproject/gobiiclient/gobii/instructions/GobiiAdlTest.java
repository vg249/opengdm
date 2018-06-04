// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Chaitanya Sarma
// Create Date:   2018-05-18
// ************************************************************************
package org.gobiiproject.gobiiclient.gobii.instructions;

import org.gobiiproject.gobiiclient.core.gobii.GobiiTestConfiguration;
import org.gobiiproject.gobiimodel.config.TestExecConfig;
import org.junit.*;

import java.io.File;

public class GobiiAdlTest {


    private static boolean backendSupoorted;
    private static TestExecConfig testExecConfig;

    @BeforeClass
    public static void setUpClass() throws Exception {
        backendSupoorted = org.gobiiproject.gobiiclient.HelperFunctions.isBackEndSupported();
        GobiiTestConfiguration gobiiTestConfiguration = new GobiiTestConfiguration();
        testExecConfig = gobiiTestConfiguration.getConfigSettings().getTestExecConfig();
        backendSupoorted = gobiiTestConfiguration.getConfigSettings().isProvidesBackend();
    }


    @Test
    public void testADLBatchProcessing() {
        System.out.println(backendSupoorted);
        if (backendSupoorted) {
            ADLEncapsulator adlEncapsulator = new ADLEncapsulator();
            String configUtilCommandlineStem = testExecConfig.getConfigUtilCommandlineStem();
            // EXAMPLE java -jar E:\gobii\1623-03\gobiiproject\gobii-process\target
            String[] split = configUtilCommandlineStem.split(" ");
            adlEncapsulator.setAdlJarPath(split[2] + "/gobiiadl.jar");
            adlEncapsulator.setInputHost(testExecConfig.getInitialConfigUrl());
            adlEncapsulator.setInputUser(testExecConfig.getLdapUserForUnitTest());
            adlEncapsulator.setInputPassword(testExecConfig.getLdapPasswordForUnitTest());
            adlEncapsulator.setInputTimeout(testExecConfig.getAsynchOpTimeoutMinutes());
            adlEncapsulator.setInputDirectory(new File("src/test/resources/gobiiAdl").getAbsolutePath());
            Assert.assertTrue(adlEncapsulator.getErrorMsg(), adlEncapsulator.executeBatchGobiiADL());
        }
    }
}
