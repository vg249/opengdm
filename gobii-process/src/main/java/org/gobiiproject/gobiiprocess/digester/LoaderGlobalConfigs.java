package org.gobiiproject.gobiiprocess.digester;
import org.apache.commons.cli.*;
import org.gobiiproject.gobiimodel.utils.FileSystemInterface;
import org.gobiiproject.gobiiprocess.ProcessGlobalConfigs;

public class LoaderGlobalConfigs extends ProcessGlobalConfigs{
    private LoaderGlobalConfigs(){}
    private static boolean singleThreadFileRead=false;
    private static boolean deleteIntermediateFiles=false;


    /**
     * Adds options to an Options object which will be read in 'setFromFlags'.
     */
    public static void addOptions(Options o)  {
        ProcessGlobalConfigs.addOptions(o);
        o.addOption("str", "singleThreadRead", false, "Use a single thread for file reading");
        o.addOption("dif","deleteIntermediateFiles", false, "Delete intermediate files (and save some space)");
    }
    public static void setFromFlags(CommandLine cli){
        ProcessGlobalConfigs.setFromFlags(cli);
        if(cli.hasOption("singleThreadRead")) singleThreadFileRead=true;
        if(cli.hasOption("deleteIntermediateFiles")) deleteIntermediateFiles=true;
    }

    public static boolean getSingleThreadFileRead(){
        return singleThreadFileRead;
    }
    public static boolean getDeleteIntermediateFiles() { return deleteIntermediateFiles; }
}