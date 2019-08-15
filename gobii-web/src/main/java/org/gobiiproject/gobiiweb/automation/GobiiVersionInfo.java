package org.gobiiproject.gobiiweb.automation;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.slf4j.Logger;

import java.io.*;
import java.util.Properties;

/**
 * Created by VCalaminos on 2/10/2017.
 */
public class GobiiVersionInfo {

    private String version;

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(GobiiVersionInfo.class);

    public GobiiVersionInfo() throws Exception {


        ConfigSettings configSettings = new ConfigSettings();

        if(configSettings.getFileSystemRoot() != null) {

            //TODO: The full config path should be part of gobii-web.xml
            String versionFilePath = (
                    LineUtils.terminateDirectoryPath(configSettings.getFileSystemRoot()) +
                    "config/gobii.version");

            //The Version file should be a property file with gobii_version as key
            //This is done believing we can avoid junk values if appended to file

            File versionFile = new File(versionFilePath);

            try(InputStream inputStream = new FileInputStream(versionFile)) {

                Properties versionProps = new Properties();

                versionProps.load(inputStream);

                this.version = versionProps.getProperty("gobii_version");
            }
            catch(IOException ioex) {
                this.version =  "";
                LOGGER.error(ioex.getMessage());
            }

        }
        else {
            this.version = "";
        }

    }

    public static String getVersion() throws Exception {

        GobiiVersionInfo gobiiVersionInfo = new GobiiVersionInfo();

        return gobiiVersionInfo.version;
    }

}
