// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.core;

public class Urls {


    public final static String HOST = "localhost";
    public final static Integer PORT = 8181;

    private final static String CTRLR_EXTRACT = "/extract/";
    private final static String CTRLR_LOAD = "/load/";

    public final static String URL_MARKERS = CTRLR_EXTRACT  + "search/bycontenttype";
    public final static String URL_PING_EXTRACT =  CTRLR_EXTRACT + "ping";
    public final static String URL_PING_LOAD =  CTRLR_LOAD +  "ping";

} // Urls
