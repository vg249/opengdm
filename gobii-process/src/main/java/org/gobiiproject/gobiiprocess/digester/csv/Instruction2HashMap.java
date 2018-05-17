package org.gobiiproject.gobiiprocess.digester.csv;

import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.gobiiproject.gobiimodel.utils.HelperFunctions.parseInstructionFile;


public class Instruction2HashMap {

    private static String tableName;
    private static String prevLine;
    private static String entityName;
    private static String value;
    private static int noColumn;

    public static void main(String[] args) throws Exception {

        String inFile = args[0];
        Map<String, List<GobiiFileColumn>> tableMap = new HashMap<>();

        /**
         * Read JSON from a file into a HashMap
         */
        try {

            FileReader fileReader = new FileReader(inFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            List<GobiiLoaderInstruction> list = parseInstructionFile(inFile);
            CSVFileReaderV2.parseInstructionFile(list);
/*
            for(GobiiLoaderInstruction inst:list) {
                tableMap = inst.getColumnsByTableName();
                tableName = inst.getTable();
                System.out.println();
            }
*/
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return tableMap;
    }
}

