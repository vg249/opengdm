// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.core;

public class Urls {


    public final static String HOST = "localhost";
    public final static Integer PORT = 8080;

    private final static String APP_ROOT = "/gobii-web/";
    private final static String CTRLR_EXTRACT = "extract/";
    private final static String CTRLR_LOAD = "load/";

    public final static String URL_MARKERS = APP_ROOT + CTRLR_EXTRACT  + "search/bycontenttype";
    public final static String URL_PING_EXTRACT =  APP_ROOT + CTRLR_EXTRACT + "ping";
    public final static String URL_PING_LOAD =  APP_ROOT + CTRLR_LOAD +  "ping";
    public final static String URL_PING_PROJECT =  APP_ROOT + CTRLR_LOAD +  "project";
    public final static String URL_NAME_ID_LIST =  APP_ROOT + CTRLR_LOAD +  "nameidlist";

} // Urls
