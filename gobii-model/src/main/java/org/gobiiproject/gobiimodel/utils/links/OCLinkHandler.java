package org.gobiiproject.gobiimodel.utils.links;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.ServerConfig;
import org.gobiiproject.gobiimodel.types.ServerType;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

/**
 * Class to livelinks from Owncloud
 */
public class OCLinkHandler {

    /**
     * Simple parser to get the tag value from xml string (Strictly doesnt work for duplicated && nested tags)
     * @param xmlString
     * @param nodeName
     * @return
     */
    public static String getTAG(String xmlString, String nodeName){
        int start = xmlString.indexOf("<"+nodeName+">") + nodeName.length() + 2;
        int end = xmlString.indexOf("</"+nodeName+">");
        if(start > end || start==-1){
        	return "";
        }
        return xmlString.substring(start, end);
    }

    /**
     * Owncloud download URL from path
     * @param filePath
     * @param config
     * @return
     * @throws Exception
     */
    public static String getLink(String filePath, ConfigSettings config, boolean publicUrl) throws Exception {

        if (filePath.contains("inprogress")) {
            filePath.replace("inprogress", "done");
        }

        ServerConfig ocConf = config.getGlobalServer(ServerType.OWN_CLOUD);
        String username = ocConf.getUserName();
        String password = ocConf.getPassword();
        String host = ocConf.getHost();
        String port = ocConf.getPort().toString();
        String contextPath = ocConf.getContextPath();
        String alternateContextPath = ocConf.getErrorContextPath();
        String urlPath = "http://" + host + (port.equals("") ? "" : ":" + port) + "/ocs/v1.php/apps/files_sharing/api/v1/shares";
        String relativeContext = contextPath;
        String path;
        if (filePath.contains("/logs/")) {
            relativeContext = alternateContextPath;
            path = "path=" + filePath.replace("/data/gobii_bundle/logs/", "/" + relativeContext) + "&shareType=3&permissions=1";

        } else {
            path = "path=" + filePath.replace("/data/gobii_bundle/crops/", "/" + relativeContext) + "&shareType=3&permissions=1";
        }
        String liveLink;
        try {
            URL url = new URL(urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String userpass = username + ":" + password;
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
            connection.setRequestProperty("Authorization", basicAuth);
            connection.setRequestMethod("POST");
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-length",
                    String.valueOf(path.length()));
            connection.setDoOutput(true);
            DataOutputStream output = new DataOutputStream(connection.getOutputStream());
            try {
                output.writeBytes(path);
            }finally {
                output.close();
            }
            DataInputStream input = new DataInputStream(connection.getInputStream());

            StringBuilder resultBuf = new StringBuilder();
            try {
                int c;
                while ((c = input.read()) != -1) {
                    resultBuf.append((char) c);
                }
            }
            finally {
                input.close();
            }
            String status = getTAG(resultBuf.toString(), "status");
            if ("ok".equals(status)) {
                liveLink = getTAG(resultBuf.toString(), "url") + "/download";
            } else {
                liveLink = "";
                ErrorLogger.logWarning("OWNCLOUD", "API request failed due to improper configurations, with status " + status);
                ErrorLogger.logWarning("OWNCLOUD","Could not request file information at path: " + urlPath);
            }
        } catch(IOException e){
            liveLink="";
            ErrorLogger.logWarning("OWNCLOUD", "API request failed due to improper configurations",e);
            ErrorLogger.logWarning("OWNCLOUD","Could not request file information at path: " + urlPath);
        }

        return liveLink;
    }

    public static String getOwncloudURL(String filePath, ConfigSettings config) throws Exception {
        ServerConfig ocConf = config.getGlobalServer(ServerType.OWN_CLOUD);
        String host = ocConf.getHost();
        String port = ocConf.getPort().toString();
        String contextPath = ocConf.getContextPath();
        String alternativeContextPath = ocConf.getErrorContextPath();
        if(filePath.contains("/logs/")){
            filePath = filePath.replace("/data/gobii_bundle/logs/", "/" + alternativeContextPath);

        }
        else {
            filePath = filePath.replace("/data/gobii_bundle/crops/", "/" + contextPath);
        }

        if(!filePath.endsWith("/")){
            int start = filePath.lastIndexOf("/");
            filePath = filePath.substring(0, start);
        }
        String liveURL = "http://" + host + (port.equals("") ? "" : (":" + port)) +  "/apps/files/?dir=" + filePath;
        return liveURL;
    }
}