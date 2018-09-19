package org.gobiiproject.gobiimodel.utils.links;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.types.GobiiServerType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

/**
 * Class to livelinks from Owncloud
 */
public class GetLinks {

    String username;
    String password;
    String url;
    String urlPath;


    /**
     * Just for the develop
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        ConfigSettings configuration = null ;
        configuration = new ConfigSettings("D:\\GOBII\\gobii-web.xml");
//        String urlpath = "http://192.168.80.4:8090/ocs/v1.php/apps/files_sharing/api/v1/shares";
//        String username = "admin";
//        String password = "admin";
//        String path = "path=" + "/data/gobii_bundle/crops/dev/extractor/output/s.sivasubramani/hapmap/whole_dataset/2018_09_17_02_19_02/Dataset.hmp.txt" + "&shareType=3&permissions=1";
        String path = "/data/gobii_bundle/crops/dev/extractor/output/s.sivasubramani/hapmap/whole_dataset/2018_09_17_02_19_02/Dataset.hmp.txt";
        System.out.println(getLink(path, configuration));
    }

    /**
     * Simple parser to get the tag value from xml string (Strictly doesnt work for duplicated && nested tags)
     * @param xmlString
     * @param nodeName
     * @return
     */
    public static String getTAG(String xmlString, String nodeName){
        int start = xmlString.indexOf("<"+nodeName+">") + nodeName.length() + 2;
        int end = xmlString.indexOf("</"+nodeName+">");
        return xmlString.substring(start, end);
    }

    /**
     * Owncloud download URL from path
     * @param filePath
     * @param config
     * @return
     * @throws Exception
     */
    public static String getLink(String filePath, ConfigSettings config) throws Exception {
//        String urlPath, String username, String password, String path

        String username = config.getGlobalServer(GobiiServerType.OWN_CLOUD).getUserName();
        String password = config.getGlobalServer(GobiiServerType.OWN_CLOUD).getPassword();
        String host = config.getGlobalServer(GobiiServerType.OWN_CLOUD).getHost();
        String port = config.getGlobalServer(GobiiServerType.OWN_CLOUD).getPort().toString();
        String apiPath = config.getGlobalServer(GobiiServerType.OWN_CLOUD).getContextPath();
        String urlPath = "http://" + host + (port.equals("") ? "" : ":"+port) + apiPath.substring(0, apiPath.length()-1);
        String path = "path=" + filePath + "&shareType=3&permissions=1";
        String liveLink;
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        String userpass = username + ":" + password;
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
        connection.setRequestProperty ("Authorization", basicAuth);
        connection.setRequestMethod("POST");
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Content-length",
        String.valueOf(path.length()));
        connection.setDoOutput(true);
        DataOutputStream output = new DataOutputStream(connection.getOutputStream());
        output.writeBytes(path);
        output.close();
        DataInputStream input = new DataInputStream(connection.getInputStream());
        int c;
        StringBuilder resultBuf = new StringBuilder();
        while ((c = input.read()) != -1) {
            resultBuf.append((char) c);
        }
        input.close();
        System.out.println(path);
        System.out.println(urlPath);
        System.out.println(userpass);
        System.out.println(resultBuf.toString());
        if(getTAG(resultBuf.toString(),"status").equals("ok")){
            liveLink = getTAG(resultBuf.toString(),"url") + "/download";
        }
        else{
            liveLink = "NA";
        }
        return liveLink;
    }

    public static String getOwncloudURL(String path, ConfigSettings config) throws Exception {
        String host = config.getGlobalServer(GobiiServerType.OWN_CLOUD).getHost();
        String port = config.getGlobalServer(GobiiServerType.OWN_CLOUD).getPort().toString();
        String liveURL = "http://" + host + (port.equals("") ? "" : (":" + port)) + "/" + "/apps/files/?dir=" + path;
//        String liveURL = "http://" + "192.168.80.4:8090" + "/apps/files/?dir=" + "/gobiidev/gobii_bundle/crops/dev/files/2018-05-30_16-35-51-683";
        return liveURL;
    }
}