package org.gobiiproject.gobiiprocess.digester.validation;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiiclient.core.common.HttpCore;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiiprocess.digester.utils.validation.DigestFileValidator;
import org.gobiiproject.gobiiprocess.digester.utils.validation.ValidationWebServicesUtil;
import org.gobiiproject.gobiiprocess.digester.utils.validation.errorMessage.Failure;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

/**
 * Unit test for CSVFileReaderV2Test. Approach: A temporary folder is created
 * which contains all input file. Instruction file is created with required
 * fields. Using CSVFileReaderV2 input file is read and stored in output file
 * after processing. Generated o/p file is verified that it is as expected.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ValidationWebServicesUtil.class)
@PowerMockRunnerDelegate(BlockJUnit4ClassRunner.class)
@PowerMockIgnore({"javax.management.*", "javax.net.ssl.*"})
public class DigestFileValidationTest {

    private static String tempFolderLocation;

    @ClassRule
    public static TemporaryFolder tempFolder = new TemporaryFolder();


    @BeforeClass
    public static void setUp() throws IOException {
        tempFolderLocation = tempFolder.getRoot().getPath();
        File source = new File("src/test/resources/validation");
        FileUtils.copyDirectory(source, tempFolder.getRoot());
        System.out.println();
    }


    /**
     * According to JUnit no exception is thrown when temp folder is not
     * deleted. This method ensures that temp folder is always deleted.
     */
    @AfterClass
    public static void checkAndCleanTempFile() {
        try {
            FileUtils.deleteDirectory(new File(tempFolderLocation));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test case for multiple_CSV_ROW
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testGermplasm() {
        DigestFileValidator digestFileValidator = new DigestFileValidator(tempFolder.getRoot().getAbsolutePath() + "/germplasm", tempFolder.getRoot().getAbsolutePath() + "/germplasm/validationConfig.json", "http://192.168.56.101:8081/gobii-dev/", "mcs397", "q");

        PowerMockito.mockStatic(ValidationWebServicesUtil.class);
        List<Failure> failures = new ArrayList<>();
        PowerMockito
                .when(ValidationWebServicesUtil.loginIntoServer("http://192.168.56.101:8081/gobii-dev/", "mcs397", "q", null, failures))
                .thenReturn(true);
        digestFileValidator.performValidation();

    }
}




