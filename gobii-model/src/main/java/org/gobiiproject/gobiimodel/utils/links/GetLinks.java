package org.gobiiproject.gobiimodel.utils.links;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.types.GobiiServerType;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

/**
 * Class to livelinks from Owncloud
 */
public class GetLinks {

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

        if(filePath.contains("ïnprogress")){
            filePath.replace("inprogress", "done");
        }

        String username = config.getGlobalServer(GobiiServerType.OWN_CLOUD).getUserName();
        String password = config.getGlobalServer(GobiiServerType.OWN_CLOUD).getPassword();
        String host = config.getGlobalServer(GobiiServerType.OWN_CLOUD).getHost();
        String port = config.getGlobalServer(GobiiServerType.OWN_CLOUD).getPort().toString();
        String contextPath = config.getGlobalServer(GobiiServerType.OWN_CLOUD).getContextPath();
        String urlPath = "http://" + host + (port.equals("") ? "" : ":"+port) + "/owncloud/ocs/v1.php/apps/files_sharing/api/v1/shares";
        String path = "path=" + filePath.replace("/data/gobii_bundle/crops/", "/" + contextPath) + "&shareType=3&permissions=1";
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
        if(getTAG(resultBuf.toString(),"status").equals("ok")){
            liveLink = getTAG(resultBuf.toString(),"url") + "/download";
        }
        else{
            liveLink = "";
//            ErrorLogger.logError("OWNCLOUD", "API request failed due to improper configurations");
        }
        return liveLink.replace("http://","URL:");
    }

    public static String getOwncloudURL(String filePath, ConfigSettings config) throws Exception {
        String host = config.getGlobalServer(GobiiServerType.OWN_CLOUD).getHost();
        String port = config.getGlobalServer(GobiiServerType.OWN_CLOUD).getPort().toString();
        String contextPath = config.getGlobalServer(GobiiServerType.OWN_CLOUD).getContextPath();
        filePath = filePath.replace("/data/gobii_bundle/crops/", "/" + contextPath);
        if(!filePath.endsWith("/")){
            int start = filePath.lastIndexOf("/");
            filePath = filePath.substring(0, start);
        }
        System.out.println(filePath);
        String liveURL = "http://" + host + (port.equals("") ? "" : (":" + port)) +  "/owncloud/index.php/apps/files/?dir=" + filePath;
        return liveURL;
    }
}