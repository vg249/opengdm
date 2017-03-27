package org.gobiiproject.gobiiprocess.digester;
import org.apache.commons.cli.*;
public class LoaderGlobalConfigurations{
    private LoaderGlobalConfigurations(){}
    private static boolean singleThreadFileRead=false;


    /**
     * Adds options to an Options object which will be read in 'setFromFlags'.
     */
    static void addOptions(Options o){
        o.addOption("str", "singleThreadRead", false, "Use a single thread for file reading");
    }
    static void setFromFlags(CommandLine cli){
        if(cli.hasOption("singleThreadRead")) singleThreadFileRead=true;
    }

    public static boolean getSingleThreadFileRead(){
        return singleThreadFileRead;
    }
}