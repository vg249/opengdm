package org.gobiiproject.gobiimodel.utils.links;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.toilelibre.libe.curl.Curl;
import java.net.URL;

/**
 * Class to handle OwnCloud download URLs with Curl
 */

public class OwnCloudURL {

    private String host;
    private String port;
    private String user;
    private String password;
    private String url;
    private String path;

    /**
     * get configuration tags for OwnCloud connections
     * @param config
     */
    public OwnCloudURL(ConfigSettings config) {
        host = config.getOwncloudDomain();
        port = config.getOwncloudPort().toString();
        user = config.getOwncloudUser();
        password = config.getOwncloudPassword();
        url = config.getOwncloudURL().toLowerCase();
        path = config.getOwncloudPath().toLowerCase();
    }

    /**
     * get OwnCloud Host Domain name: maybe an IP Eg: 192.168.56.102
     * @return
     */
    public String getHost() {
        return host;
    }


    /**
     * get OwnCloud Port. Eg: 8085
     * @return
     */
    public String getPort() {
        return port;
    }

    /**
     * Owncloud API URL. Eg: //ocs/v1.php/apps/files_sharing/api/v1/shares
     * @return
     */
    public String getURL() {
        return url;
    }

    /**
     * Properties for the Path shareType=3&permissions=1'
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     * Owncloud preferrable admin account
     * @return
     */
    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    /**
     *
     * @param filePath
     */
    public static void getLinks(String filePath) {

        try {
            String url = "http://" + this.getHost() + ":" + this.getPort() + this.getURL() ;
            String data = "path=" + filePath + "&shareType=3&permissions=1";
            URL obj = new URL(url);
            String userpass = this.getUser() + ":" + this.getPassword();
            String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
            String outLines = Curl.$("-u " + userpass + " " + url + " --data " + data);
            System.out.println(outLines);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}