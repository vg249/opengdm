package org.gobiiproject.gobiiprocess;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.gobiiproject.gobiimodel.utils.FileSystemInterface;
import org.gobiiproject.gobiimodel.utils.email.MailInterface;

/**
 * Contains Loader and Extractor combined Options statements.
 */
public class ProcessGlobalConfigs {
     public static void addOptions(Options o){
         o.addOption("kaf","keepAllFiles", false, "keep all temporary files");
         o.addOption("ktf","keepTempFiles", false, "move temporary files into a temp/ directory");
         o.addOption("nom","noMail",false,"No emails sent");
     }
     public static void setFromFlags(CommandLine cli){
         if(cli.hasOption("keepAllFiles")) FileSystemInterface.keepAllFiles(true);
         if(cli.hasOption("keepTempFiles")) FileSystemInterface.keepTempFiles(true);
         if(cli.hasOption("NoMail")) MailInterface.noMail=true;
    }
}
