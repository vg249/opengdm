package org.gobiiproject.gobiiprocess.digester.csv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;


public class Instruction2HashMap {

    private static String tableName;
    private static String prevLine;
    private static String entityName;
    private static String value;
    private static int noColumn;

    public static HashMap<String, InstructionHashmap<Integer, String, String>> getMap(String inFile) throws Exception {
        InstructionHashmap instructionMap = new InstructionHashmap();
        HashMap<String, InstructionHashmap<Integer, String, String>> map = new HashMap<String, InstructionHashmap<Integer, String, String>>();

        /**
         * Read JSON from a file into a HashMap
         */
        try {

            FileReader fileReader = new FileReader(inFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if(line.endsWith("[ {")){
                    tableName = line.split(":")[0].replace("\"","");
                    noColumn=1;
                }
                if(line.endsWith("} ],") || line.endsWith("}, {")){
                    entityName = prevLine.split(":")[0].replace("\"", "").trim();
                    value = prevLine.split(":")[1].replace("\"", "").trim();
                }
                if(line.endsWith(",") && !line.contains("}")){
                    entityName = line.split(":")[0].replace("\"", "").trim();
                    value = line.split(":")[1].replace("\"", "").trim();
                    map.put(tableName, (InstructionHashmap<Integer, String, String>) instructionMap.put(Integer.hashCode(noColumn), entityName, value));
                }
                if(line.endsWith("}, {")){
                    noColumn++;
                }
                if(line.contains("jobPayloadType")){
                    break;
                }
                prevLine = line;
            }
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}

