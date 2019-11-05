package org.gobiiproject.gobiiprocess.extractor;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.gobiiproject.gobiiprocess.ProcessGlobalConfigs;

public class ExtractorGlobalConfigs extends ProcessGlobalConfigs{
    private ExtractorGlobalConfigs(){}

    public static boolean newSampleFilter=false;

    /**
     * Adds options to an Options object which will be read in 'setFromFlags'.
     */
    public static void addOptions(Options o)  {
        ProcessGlobalConfigs.addOptions(o);
        o.addOption("nsf", "newSampleFilter", false, "Use new sample filtering method");

    }
    public static void setFromFlags(CommandLine cli){
        if(cli.hasOption("newSampleFilter")){
            newSampleFilter=true;
        }
        ProcessGlobalConfigs.setFromFlags(cli);
     }

}