package org.gobiiproject.gobiiprocess.digester;

import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HTSInterfaceTest {
    private static String tempFolderLocation;

    @ClassRule
    public static TemporaryFolder tempFolder = new TemporaryFolder();

    @BeforeClass
    public static void setUp() throws IOException {
        tempFolderLocation = tempFolder.getRoot().getPath();
        File source = new File("src/test/resources/validation/HTS");
        FileUtils.copyDirectory(source, tempFolder.getRoot());
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
        }
    }


    @Test
    @Ignore //TODO - need to set up 'clean' unit test file and figure out why 'set up' is not firing
    public void setupAndRead() throws Exception{
        String filePath;
        filePath = " C:\\Users\\jdl232.RS-BTOM1YJDL232\\workspace\\gobiiproject\\gobii-process\\target\\test-classes\\validation\\HTS\\VCF100Testwithperiods_inAlt_071118_S2.vcf";
        org.gobiiproject.gobiiprocess.vcfinterface.HTSInterface.setupVariantOnlyInputLine(new File(filePath));
        List<String> line = org.gobiiproject.gobiiprocess.vcfinterface.HTSInterface.getVariantOnlyInputLine("/");
        String[] expectedArr = "C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/A, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C, C/C/C/C".split(", ");
        Assert.assertEquals("First Read Line is not equal", Arrays.deepToString(expectedArr),Arrays.deepToString(line.toArray()));
    }
}
