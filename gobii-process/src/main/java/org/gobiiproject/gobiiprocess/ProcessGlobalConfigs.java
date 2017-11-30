package org.gobiiproject.gobiiprocess;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.gobiiproject.gobiimodel.utils.FileSystemInterface;

/**
 * Contains Loader and Extractor combined Options statements.
 */
public class ProcessGlobalConfigs {
     public static void addOptions(Options o){
         o.addOption("kaf","keepAllFiles", false, "keep all temporary files");
         o.addOption("ktf","keepTempFiles", false, "move temporary files into a temp/ directory");
    }
     public static void setFromFlags(CommandLine cli){
         if(cli.hasOption("keepAllFiles")) FileSystemInterface.keepAllFiles(true);
         if(cli.hasOption("keepTempFiles")) FileSystemInterface.keepTempFiles(true);
    }
}
