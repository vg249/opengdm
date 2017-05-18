package org.gobiiproject.gobiiprocess.digester.utils;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiimodel.types.NucIupacCodes;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import static org.gobiiproject.gobiimodel.types.NucIupacCodes.*;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.checkFileExistence;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by siva on 18-05-2017.
 * Converts IUPAC single code matrix to bi-allelic matrix. (WARNING: does not work with multi(2+)-allelic codes)
 */
public class IUPACmatrixToBi {

    private static final String NEWLINE = "\n";
    private static long startTime, endTime, duration;
    private static String fSep;

    public static boolean convertIUPACtoBi(String sep, String iFile, String oFile) throws FileNotFoundException {

        if (!checkFileExistence(iFile)) {
            ErrorLogger.logError("IUPAC to Bi","Input file provided does not exists.\n");
            System.exit(0);
        }

        Map<String, NucIupacCodes> hash = new HashMap<>();

        initNuclHash(hash);
        switch (sep) {
            case "tab":
                fSep = "\t";
                break;
            case "csv":
                fSep = ",";
                break;
            default:
                ErrorLogger.logError("IUPAC to Bi","Given file format can not be processed.");
                System.exit(0);
                break;
        }
        startTime = System.currentTimeMillis();
        BufferedReader buffIn = new BufferedReader(new FileReader(iFile));
        try (BufferedWriter buffOut=new BufferedWriter(new FileWriter(oFile)))
        {
            String iLine;
            while ((iLine = buffIn.readLine()) != null) {
                String[] iNucl = iLine.split(fSep);
                String[] oNucl;
                oNucl = new String[(iNucl.length)];
                for (int i = 0; i < iNucl.length; i++) {
                    oNucl[i] = hash.get(iNucl[i].toUpperCase()).toString();
                }
                buffOut.write(StringUtils.join(oNucl, fSep));
                buffOut.write(NEWLINE);
            }
            buffOut.close();
            buffIn.close();
            endTime = System.currentTimeMillis();
            duration = endTime-startTime;
            ErrorLogger.logTrace("IUPAC to Bi","Time taken:"+ duration/1000 + " Secs");
        } catch (FileNotFoundException e){
            ErrorLogger.logError("IUPAC to Bi","Missing output File:", e);
        } catch (IOException e){
            ErrorLogger.logError("IUPAC to Bi","Unexpected error", e);
        }
        return true;
    }

    /***
     * Initializing IUPAC nucleotide Dictionary
     * @param hash
     */
    private static void initNuclHash(Map<String,NucIupacCodes> hash){
        hash.put("A",AA);
        hash.put("T",TT);
        hash.put("G",GG);
        hash.put("C",CC);
        hash.put("N",NN);
        hash.put("W",AT);
        hash.put("R",AG);
        hash.put("M",AC);
        hash.put("K",TG);
        hash.put("Y",TC);
        hash.put("S",GC);
    }
}