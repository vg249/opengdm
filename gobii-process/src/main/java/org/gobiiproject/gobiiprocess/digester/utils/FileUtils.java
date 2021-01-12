package org.gobiiproject.gobiiprocess.digester.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.tika.Tika;
import org.gobiiproject.gobiimodel.config.GobiiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

@Slf4j
public class FileUtils {

    public static final String TAR_GUNZIP_EXTENSION = ".tar.gz";

    public static final String GUNZIP_EXTENSION = ".gz";

    public static final String ZIP_EXTENSION = ".zip";

    public static final String TAB_SEP = "\t";

    private static final Tika tika = new Tika();

    /**
     * https://knpcode.com/java-programs/decompress-and-untar-multiple-gzipped-files-java/
     * @param inputFile
     * @return
     * @throws GobiiException
     */
    public static List<File> extractTarGunZipFile(File inputFile) throws GobiiException {

        TarArchiveInputStream tis = null;
        List<File> extractedFiles = new ArrayList<>();
        try {
            FileInputStream inputStream = new FileInputStream(inputFile);
            GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
            tis = new TarArchiveInputStream(gzipInputStream);
            TarArchiveEntry tarEntry = null;
            while ((tarEntry = tis.getNextTarEntry()) != null) {
                if(tarEntry.isDirectory()){
                    continue;
                }else {
                    File extractedFile = new File(inputFile.getParent()
                        + File.separator + tarEntry.getName());
                    extractedFile.getParentFile().mkdirs();
                    Files.copy(tis, extractedFile.toPath());
                    extractedFiles.add(extractedFile);
                }
            }
            inputFile.deleteOnExit();
            return extractedFiles;
        }
        catch (IOException e) {
            log.error(e.getMessage());
            throw new GobiiException(e);
        }
    }

    public static File extractGunZipFile(File inputFile) throws GobiiException {
        try {
            FileInputStream inputStream = new FileInputStream(inputFile);
            GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);

            int gzExtensionIndex = inputFile.getName().lastIndexOf(GUNZIP_EXTENSION);
            String fileNameWithoutGzExtenstion = inputFile.getName().substring(0, gzExtensionIndex);

            File destFile = new File(
                inputFile.getParent() + File.separator + fileNameWithoutGzExtenstion);
            Files.copy(gzipInputStream, destFile.toPath());
            return destFile;
        }
        catch (IOException e) {
            log.error(e.getMessage());
            throw new GobiiException(e);
        }
    }

    public static boolean isFileTextFile(File file) throws GobiiException {
        try {
            String fileType = tika.detect(file);
            return fileType.startsWith("text/");
        }
        catch (IOException e) {
            throw new GobiiException(e);
        }
    }

}
