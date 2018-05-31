// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Chaitanya Sarma
// Create Date:   2018-05-18
// ************************************************************************
package org.gobiiproject.gobiiclient.gobii.instructions;

import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiiclient.Helper;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiiclient.core.gobii.GobiiTestConfiguration;
import org.gobiiproject.gobiimodel.config.TestExecConfig;
import org.gobiiproject.gobiimodel.dto.system.ConfigSettingsDTO;
import org.gobiiproject.gobiimodel.types.ServerCapabilityType;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class GobiiAdlTest {

    private static final String SPACE = " ";

    private static boolean backendSupoorted;
    private static String servercommand;

    @BeforeClass
    public static void setUpClass() throws Exception {
        final String HOST = "-h", USERNAME = "-u", PASSWORD = "-p", TIMEOUT = "-t";
        GobiiTestConfiguration gobiiTestConfiguration = new GobiiTestConfiguration();
        TestExecConfig testExecConfig = gobiiTestConfiguration.getConfigSettings().getTestExecConfig();
        servercommand = testExecConfig.getConfigUtilCommandlineStem() + "/gobiiadl.jar" + SPACE;
        servercommand += HOST + SPACE + testExecConfig.getInitialConfigUrl() + SPACE;
        servercommand += USERNAME + SPACE + testExecConfig.getLdapUserForUnitTest() + SPACE;
        servercommand += PASSWORD + SPACE + testExecConfig.getLdapPasswordForUnitTest() + SPACE;
        servercommand += TIMEOUT + SPACE + testExecConfig.getAsynchOpTimeoutMinutes() + SPACE;

        backendSupoorted = Helper.isBackEndSupported();
    }

    private void executeGobiiADL(String baseFolder) {
        final String BASEFOLDER = "-d", OUTPUT = "output.txt", ERROR = "error.txt";
        String command = servercommand + BASEFOLDER + SPACE + baseFolder + SPACE;
        boolean status = HelperFunctions.tryExec(command, baseFolder + OUTPUT, baseFolder + ERROR);
        if (!status) {
            final String[] msg = {"Command Executed:" + SPACE + command + SPACE};
            try (Stream<String> stream = Files.lines(Paths.get(baseFolder + ERROR))) {
                stream.forEach(s -> msg[0] += s + SPACE);
            } catch (IOException e) {
                msg[0] += e.getMessage();
            }
            Assert.assertTrue(msg[0], false);
        }
    }


    @Test
    public void testADLBatchProcessing() {
        if (backendSupoorted) executeGobiiADL(new File("src/test/resources/gobiiAdl").getAbsolutePath());
    }
}
