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
        return liveURL;
    }
}