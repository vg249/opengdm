// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Chaitanya Sarma
// Create Date:   2018-05-18
// ************************************************************************
package org.gobiiproject.gobiiclient.gobii.instructions;

import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class GobiiAdlTest {

    private static String gobiiAdlRoot;
    private final String JAVA = "java -jar";
    private final String BASEFOLDER = "-b";
    private final String WXML = "-wxml";
    private static final String HOST = "-h";
    private static final String USERNAME = "-u";
    private static final String PASSWORD = "-p";
    private static final String SPACE = " ";
    private final String OUTPUT = "output.txt";
    private final String ERROR = "error.txt";

    private static String servercommand;
    @ClassRule
    public static TemporaryFolder tempFolder = new TemporaryFolder();

    @BeforeClass
    public static void setUpClass() throws Exception {
        gobiiAdlRoot = tempFolder.newFolder("GobiiAdl").getAbsolutePath();
        servercommand = HOST + SPACE + "http://192.168.121.3:8081/gobii-dev/" + SPACE;
        servercommand += USERNAME + SPACE + "mcs397" + SPACE;
        servercommand += PASSWORD + SPACE + "q" + SPACE;
        copyFolder(new File("src/test/resources/gobiiAdl"), new File(gobiiAdlRoot));
        System.out.println("YELLOW");
    }

    private static void copyFolder(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            //if directory not exists, create it
            if (!dest.exists()) {
                dest.mkdir();
            }
            //list all the directory contents
            String files[] = src.list();
            for (String file : files) {
                //construct the src and dest file structure
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                //recursive copy
                copyFolder(srcFile, destFile);
            }
        } else {
            //if file, then copy it
            Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied from " + src + " to " + dest);
        }
    }

    @AfterClass
    public static void tearDownUpClass() throws Exception {
        // Assert.assertTrue(GobiiClientContextAuth.deAuthenticate());
    }

    @Test
    public void test2letternuc() throws Exception {

        String baseFolder = gobiiAdlRoot + "/2letternuc/";
        String xmlFile = "2letternuc_test.xml";

        String command = JAVA + SPACE;
        command += "E:\\yetAnotherTask\\gobiiadl\\gobiiadl.jar" + SPACE;
        command += BASEFOLDER + SPACE + baseFolder + SPACE;
        command += WXML + SPACE + xmlFile + SPACE;
        command += servercommand;
        System.out.println(command);
        HelperFunctions.tryExec(command, baseFolder + OUTPUT, baseFolder + ERROR);
        System.out.println("HE HAW");
    }
}
