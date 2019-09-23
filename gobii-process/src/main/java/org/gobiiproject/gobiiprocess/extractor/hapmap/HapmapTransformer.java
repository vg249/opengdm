package org.gobiiproject.gobiiprocess.extractor.hapmap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiimodel.utils.error.Logger;
import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rmIfExist;
import static org.gobiiproject.gobiiprocess.extractor.hapmap.HapmapConstants.ALLELES;
import static org.gobiiproject.gobiiprocess.extractor.hapmap.HapmapConstants.ASSAY_LSID;
import static org.gobiiproject.gobiiprocess.extractor.hapmap.HapmapConstants.ASSEMBLY;
import static org.gobiiproject.gobiiprocess.extractor.hapmap.HapmapConstants.CENTER;
import static org.gobiiproject.gobiiprocess.extractor.hapmap.HapmapConstants.CHROM;
import static org.gobiiproject.gobiiprocess.extractor.hapmap.HapmapConstants.PANEL_LSID;
import static org.gobiiproject.gobiiprocess.extractor.hapmap.HapmapConstants.POS;
import static org.gobiiproject.gobiiprocess.extractor.hapmap.HapmapConstants.PROT_LSID;
import static org.gobiiproject.gobiiprocess.extractor.hapmap.HapmapConstants.QC_CODE;
import static org.gobiiproject.gobiiprocess.extractor.hapmap.HapmapConstants.RS;
import static org.gobiiproject.gobiiprocess.extractor.hapmap.HapmapConstants.STRAND;
import static org.gobiiproject.gobiiprocess.extractor.hapmap.HapmapTransformerHelper.processSampleFile;
import static org.gobiiproject.gobiiprocess.extractor.hapmap.HapmapTransformerHelper.readHeaders;
import static org.gobiiproject.gobiiprocess.extractor.hapmap.HapmapTransformerHelper.writeMarkerAndGenoTypeInfo;
import static org.gobiiproject.gobiiprocess.extractor.hapmap.HapmapTransformerHelper.writeSampleInfoOutputFile;

public class HapmapTransformer {


    public boolean generateFile(String markerFileIn, String sampleFileIn, String extendedMarkerFileIn, String genotypeFileIn, String outFile, String errorFile) throws IOException {

        HapmapScanners hapmapScanners = new HapmapScanners();
        if(!hapmapScanners.createScanners(markerFileIn, sampleFileIn, extendedMarkerFileIn, genotypeFileIn, errorFile)) return false;

        if (hapmapScanners.sampleScanner.hasNextLine()) {
            List<String> sampleHeaders = new ArrayList<>();
            TreeMap<Integer, ArrayList<String>> sampleData = new TreeMap<>();

            // Process sample File
            int sampleRowsNumber = processSampleFile(hapmapScanners.sampleScanner, sampleHeaders, sampleData);
            Logger.logInfo("Extractor", sampleRowsNumber + " sample data rows read");
            hapmapScanners.sampleScanner.close();
            if (sampleRowsNumber == 0) {
                Logger.logError("Extractor", "No sample data rows", errorFile);
                return false;
            }
            try {

                File out = new File(outFile);
                rmIfExist(outFile);
                FileWriter fileWriter = new FileWriter(out);

                // Write sampleFile information into output file.
                int dnaIndex = writeSampleInfoOutputFile(sampleHeaders, sampleData, fileWriter);

                List<String> markerHeaders = new ArrayList<>();
                if (!readHeaders(hapmapScanners.markerScanner, markerHeaders, "marker")) {
                    Logger.logError("Extractor", "Marker data file empty", errorFile);
                    return false;
                }

                // Writing the new marker headers into the current line
                String[] newMarkerHeadersLine = new String[]{
                        RS, ALLELES, CHROM, POS, STRAND, ASSEMBLY, CENTER, PROT_LSID, ASSAY_LSID, PANEL_LSID, QC_CODE};
                StringBuilder stringBuilderNewLine = new StringBuilder(StringUtils.join(newMarkerHeadersLine, "\t"));
                stringBuilderNewLine.append("\t");

                List<String> extendedMarkerHeaders = new ArrayList<>();
                if (hapmapScanners.extendedMarkerScanner != null) {
                    if (!readHeaders(hapmapScanners.extendedMarkerScanner, extendedMarkerHeaders, "extended marker")) {
                        Logger.logInfo("Extractor", "Extended marker data file empty");
                    }
                }

                // Transposing and writing the dnarun_name column into the current line,
                // and then writing the current line into the output file
                stringBuilderNewLine.append(StringUtils.join(sampleData.get(dnaIndex), "\t"));
                stringBuilderNewLine.append(System.lineSeparator());
                fileWriter.write(stringBuilderNewLine.toString());

                if (!(hapmapScanners.markerScanner.hasNextLine())) {
                    Logger.logError("Extractor", "No marker data rows", errorFile);
                    return false;
                }

                if (!(hapmapScanners.genotypeScanner.hasNextLine())) {
                    Logger.logError("Extractor", "No genotype data rows", errorFile);
                    return false;
                }

                int processedBothRowsNumber = writeMarkerAndGenoTypeInfo(hapmapScanners, fileWriter, markerHeaders, extendedMarkerHeaders, newMarkerHeadersLine);

                Logger.logInfo("Extractor", processedBothRowsNumber + " processed rows read");

                fileWriter.close();
                hapmapScanners.markerScanner.close();
                if (hapmapScanners.extendedMarkerScanner != null) {
                    hapmapScanners.extendedMarkerScanner.close();
                }
                hapmapScanners.genotypeScanner.close();
            } catch (IOException e) {
                Logger.logError("Hapmap Transformer", "Error writing " + outFile, e);
                return false;
            } catch (Exception e) {
                Logger.logError("Hapmap Transformer", "Unexpected exception in Hapmap Transformer", e);
            }
        } else {
            Logger.logError("Hapmap Transformer", "Sample data file empty", errorFile);
            return false;
        }
        return true;
    }
}
