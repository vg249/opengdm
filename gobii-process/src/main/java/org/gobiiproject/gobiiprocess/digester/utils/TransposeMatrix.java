package org.gobiiproject.gobiiprocess.digester.utils;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import java.io.*;

import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.lineCount;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.checkFileExistence;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.tryExec;

public class TransposeMatrix {
    private static long startTime, endTime, duration;
    public static boolean transposeMatrix (String sep, String iFile, String oFile, String dest) throws FileNotFoundException{
        if (!checkFileExistence(iFile)) {
            ErrorLogger.logError("Transpose Matrix", "Input file provided does not exists.\n");
            throw new FileNotFoundException("IUPAC to Bi:\t" + iFile +"does not exists");
        }
        boolean success;
        startTime = System.currentTimeMillis();

        switch (sep){
            case "tab":
                sep = "\t";
                break;
            case "csv":
                sep = ",";
                break;
            default:
                ErrorLogger.logError("Transpose Matrix", "Unknown file separator" + sep);
                break;
        }
        tryExec("split "+iFile+" "+dest+"/_transpose_");
        File directory = new File(dest);
        File files[] = directory.listFiles();
        String outFile;
        for (File inFile: files){
            if(inFile.getName().matches("_transpose_.*")){
                outFile = inFile.toString().replace("_transpose_","_transposed_");
                //TODO: Multi threading for the below line.
                transposeMatrix(sep, inFile.toString(), outFile);
            }
        }
        success=tryExec("paste "+dest+"/_transposed_*", oFile, null);
        success&=tryExec("rm -f "+dest+"/_trans*"); // dangerous
        return success;
    }

    /***
     * Transposes short matrix
     * @param sep = "\t" or ","
     * @param iFile = Input File
     * @param oFile = Output File
     * @return success
     */
    private static boolean transposeMatrix(String sep, String iFile, String oFile) {
        int lineNumber = lineCount(iFile);
        int lineNo=0, colNo=0;
        String inLine;
        int columnNumber = getColumnNumber(iFile, sep);
        if(columnNumber < 0){
            ErrorLogger.logError("Transpose Matrix", "Input file does not contain any content.\n");
            System.exit(1);
        }
        String[][] Matrix = new String[columnNumber][lineNumber];
        try(BufferedWriter buffOut=new BufferedWriter(new FileWriter(oFile));
            BufferedReader buffIn=new BufferedReader(new FileReader(iFile))){
            while ((inLine = buffIn.readLine()) != null){
                String [] iBase = inLine.split(sep);
                colNo = 0;
                for (String base:iBase){
                    Matrix[colNo][lineNo] = base;
                    colNo++;
                }
                lineNo++;
            }
            for (int i = 0; i < colNo; i++) {
                buffOut.write(StringUtils.join(Matrix[i], sep));
                buffOut.newLine();
            }
            endTime = System.currentTimeMillis();
            duration = endTime-startTime;
            ErrorLogger.logTrace("Transpose Matrix","Time taken" + "(" + iFile + "):" + duration/1000 + " Secs");
            return true;
        } catch (FileNotFoundException e){
            ErrorLogger.logError("Transpose Matrix","Missing output File:", e);
        } catch (IOException e){
            ErrorLogger.logError("Transpose Matrix","Unexpected error", e);
        }
        return false;
    }

    /***
     * To get the number of column of a file. Checking only with first line.
     * @param file = Input File
     * @param sep = "\t" or ","
     * @return
     */
    private static int getColumnNumber(String file, String sep) {
        try(BufferedReader buffIn = new BufferedReader(new FileReader(file))){
            String[] iLine = buffIn.readLine().split(sep);
            return iLine.length;
        }catch(Exception e){
            ErrorLogger.logError("Transpose Matrix","Unable to open the input file",e);
            return -1;
        }
    }

}


