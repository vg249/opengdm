package org.gobiiproject.gobiimodel.utils.links;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

/**
 * Class to livelinks from Owncloud
 */
public class GetLinks {

    /**
     * Just for the develop
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String urlpath = "http://192.168.80.4:8090/ocs/v1.php/apps/files_sharing/api/v1/shares";
        String username = "admin";
        String password = "admin";
        String path = "path=" + "/gobiidev/gobii_bundle/crops/dev/extractor/output/s.sivasubramani/hapmap/whole_dataset/2018_09_17_02_19_02/Dataset.hmp.txt" + "&shareType=3&permissions=1";
        System.out.println(getLink(urlpath,username,password,path));
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
     * @param urlPath
     * @param username
     * @param password
     * @param path
     * @return
     * @throws IOException
     */
    public static String getLink(String urlPath, String username, String password, String path) throws IOException {
        String liveLink = "";
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
}