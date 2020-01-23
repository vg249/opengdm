package org.gobiiproject.gobiiprocess.digester;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.gobiiproject.gobiiprocess.ProcessGlobalConfigs;

public class LoaderGlobalConfigs extends ProcessGlobalConfigs{
    private LoaderGlobalConfigs(){}
    private static boolean singleThreadFileRead=false;
    private static boolean deleteIntermediateFiles=false;
    private static boolean enableValidation=true;//TODO- make true to enable validation by default
    private static boolean newTwoLetterNucleotideParse=true;

    /**
     * Adds options to an Options object which will be read in 'setFromFlags'.
     */
    public static void addOptions(Options o)  {
        ProcessGlobalConfigs.addOptions(o);
        o.addOption("str", "singleThreadRead", false, "Use a single thread for file reading");
        o.addOption("dif","deleteIntermediateFiles", false, "Delete intermediate files (and save some space)");
        o.addOption("ev","enableValidation",true,"Enable data validation [[true]/false]");
        o.addOption("ntlnp","newTwoLetterNucleotideParse",true,"Use the new parser for two letter parsing [[true]/false]");
    }
    public static void setFromFlags(CommandLine cli){
        ProcessGlobalConfigs.setFromFlags(cli);
        if(cli.hasOption("singleThreadRead")) singleThreadFileRead=true;
        if(cli.hasOption("deleteIntermediateFiles")) deleteIntermediateFiles=true;
        if(cli.hasOption("enableValidation")){
            enableValidation=Boolean.parseBoolean(cli.getOptionValue("enableValidation"));//True on 'true' 'TRUE' 'tRuE', false on anything else.
        }
        if(cli.hasOption("newTwoLetterNucleotideParse")){
            newTwoLetterNucleotideParse=Boolean.parseBoolean(cli.getOptionValue("newTwoLetterNucleotideParse"));//True on 'true' 'TRUE' 'tRuE', false on anything else.
        }

    }

    public static boolean getSingleThreadFileRead(){
        return singleThreadFileRead;
    }
    public static boolean getDeleteIntermediateFiles() { return deleteIntermediateFiles; }
    public static boolean getValidation() { return enableValidation; }
    public static boolean getTwoLetterNucleotideParse(){return newTwoLetterNucleotideParse;}
}
