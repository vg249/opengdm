package org.gobiiproject.gobiiclient.dtorequests.standalone;

import org.codehaus.jackson.map.DeserializerFactory;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.Authenticator;
import org.gobiiproject.gobiiclient.dtorequests.Helpers.TestConfiguration;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.CropConfig;
import org.gobiiproject.gobiimodel.config.CropDbConfig;
import org.gobiiproject.gobiimodel.config.TestExecConfig;
import org.gobiiproject.gobiimodel.types.GobiiDbType;
import org.gobiiproject.gobiimodel.types.GobiiFileProcessDir;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

/**
 * Created by Phil on 11/3/2016.
 */


public class TestGobiiConfig {


    private static String FILE_PATH_DELIMETER = "/";
    private static TestExecConfig testExecConfig = null;


    @BeforeClass
    public static void setUpClass() throws Exception {
        testExecConfig = new TestConfiguration().getConfigSettings().getTestExecConfig();
    }

    private String makeCommandline(String arguments) {

        String returnVal;

        returnVal = testExecConfig.getConfigUtilCommandlineStem() + " " + arguments;

        return returnVal;
    }

    private String getTestFileDirectoryOfRecord() {
        return testExecConfig.getConfigFileTestDirectory();
    }

    private String makeTestFileFqpn(String testPurpose) {

        String randomSuffix = UUID.randomUUID().toString();
        String returnVal = testExecConfig.getConfigFileTestDirectory()
                + FILE_PATH_DELIMETER
                + "test_"
                + testPurpose
                + "_"
                + randomSuffix
                + ".xml";
        return returnVal;

    }

    @Test
    public void testCreateNewConfigFile() throws Exception {

        String testFileFqpn = makeTestFileFqpn("createnewconfig");

        String commandLine = makeCommandline("-a -wfqpn " + testFileFqpn + " -gR \"/mnt/lustre\"");
        boolean succeeded = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, succeeded);

        File createdFile = new File(testFileFqpn);
        Assert.assertTrue("File should have been created but does not exist"
                        + testFileFqpn,
                createdFile.exists());

    } //

    @Test
    public void testSetFileSystemRoot() {

        String testFileFqpn = makeTestFileFqpn("filesystemroot");

        String fsSystemRoot = "/nowhere/subnowhere";
        String commandLine = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -gR "
                + fsSystemRoot);

        boolean succeeded = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, succeeded);

        File createdFile = new File(testFileFqpn);
        Assert.assertTrue("File should have been created but does not exist"
                        + testFileFqpn,
                createdFile.exists());

        ConfigSettings configSettings = new ConfigSettings(testFileFqpn);
        Assert.assertTrue("The file system root value read from the file does not match the input",
                configSettings.getFileSystemRoot().equals(fsSystemRoot));
    }

    @Test
    public void testSetEmailServer() {

        String testFileFqpn = makeTestFileFqpn("setemailoptions");

        String user = "user_" + UUID.randomUUID().toString();
        String password = "password_" + UUID.randomUUID().toString();
        String host = "host_" + UUID.randomUUID().toString();
        String type = "type_" + UUID.randomUUID().toString();
        String hash = "hash_" + UUID.randomUUID().toString();
        Integer port = 25;

        String commandLine = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -stE "
                + " -soH "
                + host
                + " -soN "
                + port.toString()
                + " -soU "
                + user
                + " -soP "
                + password
                + " -stT "
                + type
                + " -stH "
                + hash);

        boolean succeeded = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, succeeded);


        ConfigSettings configSettings = new ConfigSettings(testFileFqpn);

        Assert.assertTrue("The host name does not match",
                configSettings.getEmailSvrDomain().equals(host));

        Assert.assertTrue("The port does not match",
                configSettings.getEmailServerPort().equals(port));

        Assert.assertTrue("The user name does not match",
                configSettings.getEmailSvrUser().equals(user));

        Assert.assertTrue("The password does not match",
                configSettings.getEmailSvrPassword().equals(password));

        Assert.assertTrue("The email type does not match",
                configSettings.getEmailSvrType().equals(type));

        Assert.assertTrue("The hash type does not match",
                configSettings.getEmailSvrHashType().equals(hash));

    }

    @Test
    public void testSetCropWebServerOptions() {

        String testFileFqpn = makeTestFileFqpn("cropwebserver");

        String cropId = "FOOCROP";
        String host = "host_" + UUID.randomUUID().toString();
        String contextPath = "context-" + UUID.randomUUID().toString();
        Integer port = 8080;

//     * Set crop web options:  -a -wfqpn "c:\gobii-config-test\testconfig.xml" -c "barcrop" -stW -soH "foohost" -soN 8080 -soU "foo userr" -soP "foo password" -soR "foo-web"

        String commandLine = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -c "
                + cropId
                + " -stW "
                + " -soH "
                + host
                + " -soN "
                + port.toString()
                + " -soR "
                + contextPath);

        boolean succeeded = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, succeeded);


        ConfigSettings configSettings = new ConfigSettings(testFileFqpn);

        CropConfig cropConfig = configSettings.getCropConfig(cropId);
        Assert.assertNotNull("The crop was not created: " + cropId,
                cropConfig);

        Assert.assertTrue("The host name does not match",
                cropConfig.getServiceDomain().equals(host));

        Assert.assertTrue("The port does not match: should be "
                        + port.toString()
                        + "; got: "
                        + cropConfig.getServicePort(),
                cropConfig.getServicePort().equals(port));

        Assert.assertTrue("Crop is not set to active by default",
                cropConfig.isActive());

        Assert.assertTrue("The context path not match",
                cropConfig.getServiceAppRoot().equals(contextPath));

    }

    @Test
    public void testSetPostGresForCrop() {
        //-a -wfqpn "c:\gobii-config-test\testconfig.xml" -c "barcrop" -stP -soH "foohost" -soN 5433 -soU "foo userr" -soP "foo password" -soR "foodb"

        String testFileFqpn = makeTestFileFqpn("croppgsql");

        String cropId = "FOOCROP";
        String user = "user_" + UUID.randomUUID().toString();
        String password = "password_" + UUID.randomUUID().toString();
        String host = "host_" + UUID.randomUUID().toString();
        String contextPath = "foodbname-" + UUID.randomUUID().toString();
        Integer port = 5063;

        String commandLine = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -c "
                + cropId
                + " -stP "
                + " -soH "
                + host
                + " -soN "
                + port.toString()
                + " -soU "
                + user
                + " -soP "
                + password
                + " -soR "
                + contextPath);

        boolean succeeded = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, succeeded);


        ConfigSettings configSettings = new ConfigSettings(testFileFqpn);

        CropDbConfig cropDbConfig = configSettings.getCropConfig(cropId).getCropDbConfig(GobiiDbType.POSTGRESQL);
        Assert.assertNotNull("The crop db config was not created: " + cropId,
                cropDbConfig);

        Assert.assertTrue("The host name does not match",
                cropDbConfig.getHost().equals(host));

        Assert.assertTrue("The port does not match: should be "
                        + port.toString()
                        + "; got: "
                        + cropDbConfig.getPort(),
                cropDbConfig.getPort().equals(port));

        Assert.assertTrue("The context path not match",
                cropDbConfig.getDbName().equals(contextPath));

        Assert.assertTrue("The user name does not match",
                cropDbConfig.getUserName().equals(user));

        Assert.assertTrue("The password does not match",
                cropDbConfig.getPassword().equals(password));
    }

    @Test
    public void testSetMonetGresForCrop() {
        String testFileFqpn = makeTestFileFqpn("cropmonet");

        String cropId = "BARCROP";
        String user = "user_" + UUID.randomUUID().toString();
        String password = "password_" + UUID.randomUUID().toString();
        String host = "host_" + UUID.randomUUID().toString();
        String contextPath = "foodbname-" + UUID.randomUUID().toString();
        Integer port = 5063;

        String commandLine = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -c "
                + cropId
                + " -stM "
                + " -soH "
                + host
                + " -soN "
                + port.toString()
                + " -soU "
                + user
                + " -soP "
                + password
                + " -soR "
                + contextPath);

        boolean succeeded = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, succeeded);

        ConfigSettings configSettings = new ConfigSettings(testFileFqpn);

        CropDbConfig cropDbConfig = configSettings.getCropConfig(cropId).getCropDbConfig(GobiiDbType.MONETDB);
        Assert.assertNotNull("The crop db config was not created: " + cropId,
                cropDbConfig);

        Assert.assertTrue("The host name does not match",
                cropDbConfig.getHost().equals(host));

        Assert.assertTrue("The port does not match: should be "
                        + port.toString()
                        + "; got: "
                        + cropDbConfig.getPort(),
                cropDbConfig.getPort().equals(port));

        Assert.assertTrue("The context path not match",
                cropDbConfig.getDbName().equals(contextPath));

        Assert.assertTrue("The user name does not match",
                cropDbConfig.getUserName().equals(user));

        Assert.assertTrue("The password does not match",
                cropDbConfig.getPassword().equals(password));
    }

    @Test
    public void testSetTestOptions() {

        String testFileFqpn = makeTestFileFqpn("testvals");

        String configFileFqpn = "fqpn_" + UUID.randomUUID().toString();
        String configFileTestDirectory = "test_dir_" + UUID.randomUUID().toString();
        String configUtilCommandlineStem = "comandstem_" + UUID.randomUUID().toString();
        String initialConfigUrl = "configurl_" + UUID.randomUUID().toString();
        String initialConfigUrlForSshOverride = "urlssh" + UUID.randomUUID().toString();
        String sshOverrideHost = "hostssh_" + UUID.randomUUID().toString();
        Integer sshOverridePort = 5;
        String testCrop = "testcrop_" + UUID.randomUUID().toString();
        boolean isTestSsh = false;

        String commandLine = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -gt "
                + " -gtcd "
                + configFileTestDirectory
                + " -gtcq "
                + configFileFqpn
                + " -gtcr "
                + testCrop
                + " -gtcs "
                + configUtilCommandlineStem
                + " -gtiu "
                + initialConfigUrl
                + " -gtsf "
                + (isTestSsh ? "true" : "false")
                + " -gtsh "
                + sshOverrideHost
                + " -gtsp "
                + sshOverridePort
                + " -gtsu "
                + initialConfigUrlForSshOverride);


        boolean succeeded = HelperFunctions.tryExec(commandLine, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + commandLine, succeeded);


        ConfigSettings configSettings = new ConfigSettings(testFileFqpn);


        Assert.assertTrue("Config test value does not match: configFileFqpn", configSettings.getTestExecConfig().getConfigFileFqpn().equals(configFileFqpn));
        Assert.assertTrue("Config test value does not match: test directory", configSettings.getTestExecConfig().getConfigFileTestDirectory().equals(configFileTestDirectory));
        Assert.assertTrue("Config test value does not match: commandline stem", configSettings.getTestExecConfig().getConfigUtilCommandlineStem().equals(configUtilCommandlineStem));
        Assert.assertTrue("Config test value does not match: config URL", configSettings.getTestExecConfig().getInitialConfigUrl().equals(initialConfigUrl));
        Assert.assertTrue("Config test value does not match: ssh override URL", configSettings.getTestExecConfig().getInitialConfigUrlForSshOverride().equals(initialConfigUrlForSshOverride));
        Assert.assertTrue("Config test value does not match: ssh override host", configSettings.getTestExecConfig().getSshOverrideHost().equals(sshOverrideHost));
        Assert.assertTrue("Config test value does not match: ssh override port", configSettings.getTestExecConfig().getSshOverridePort().equals(sshOverridePort));
        Assert.assertTrue("Config test value does not match: test crop", configSettings.getTestExecConfig().getTestCrop().equals(testCrop));
        Assert.assertTrue("Config test value does not match: test flag", configSettings.getTestExecConfig().isTestSsh() == isTestSsh);
    }

    @Test
    public void testCreateDirectories() throws Exception {

        String testFileFqpn = makeTestFileFqpn("createdirs");

        String testRootDirectory = getTestFileDirectoryOfRecord() + FILE_PATH_DELIMETER + UUID.randomUUID().toString();

        String createConfigCommand = makeCommandline("-a -wfqpn "
                + testFileFqpn
                + " -gR "
                + testRootDirectory);

        boolean succeeded = HelperFunctions.tryExec(createConfigCommand, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + createConfigCommand, succeeded);

        List<String> testCrops = Arrays.asList("DEV","TEST","EXTRA");
        for (String currentCropId : testCrops) {


            String host = "host_" + UUID.randomUUID().toString();
            String contextPath = "context-" + UUID.randomUUID().toString();
            Integer port = 8080;

            String commandLineForCurrentCrop = makeCommandline("-a -wfqpn "
                    + testFileFqpn
                    + " -c "
                    + currentCropId
                    + " -stW "
                    + " -soH "
                    + host
                    + " -soN "
                    + port.toString()
                    + " -soR "
                    + contextPath);

            succeeded = HelperFunctions.tryExec(commandLineForCurrentCrop, testFileFqpn + ".out", testFileFqpn + ".err");
            Assert.assertTrue("Command failed: " + commandLineForCurrentCrop, succeeded);


        } // iterate crops to create


       String createDirectoriesCommand = makeCommandline(" -wfqpn "
                + testFileFqpn
                + " -wdirs ");


        succeeded = HelperFunctions.tryExec(createDirectoriesCommand, testFileFqpn + ".out", testFileFqpn + ".err");
        Assert.assertTrue("Command failed: " + createDirectoriesCommand, succeeded);

        ConfigSettings configSettings = new ConfigSettings(testFileFqpn);
        for( CropConfig currentCropConfig : configSettings.getActiveCropConfigs()) {

            for(GobiiFileProcessDir currentRelativeDirectory : EnumSet.allOf(GobiiFileProcessDir.class)){

                String currentCropDir = configSettings.getProcessingPath(currentCropConfig.getGobiiCropType(), currentRelativeDirectory);
                File file = new File(currentCropDir);
                Assert.assertTrue("Crop directory was not created: " + currentCropDir,file.exists());
                Assert.assertTrue("Crop fqpn was not created as a directory: " + currentCropDir,file.isDirectory());
            }
        }

    }

}
