// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.core;

public class Urls {


    private final static String APP_ROOT = "/gobii-web/";
    private final static String CTRLR_EXTRACT = "extract/";
    private final static String CTRLR_LOAD = "load/";

    public final static String URL_MARKERS = APP_ROOT + CTRLR_EXTRACT  + "search/bycontenttype";
    public final static String URL_PING_EXTRACT =  APP_ROOT + CTRLR_EXTRACT + "ping";
    public final static String URL_PING_LOAD =  APP_ROOT + CTRLR_LOAD +  "ping";
    public final static String URL_PING_PROJECT =  APP_ROOT + CTRLR_LOAD +  "project";
    public final static String URL_NAME_ID_LIST =  APP_ROOT + CTRLR_LOAD +  "nameidlist";
    public final static String URL_FILE_LOAD_INSTRUCTIONS =  APP_ROOT + CTRLR_LOAD +  "instructions";
    public final static String URL_DISPLAY =  APP_ROOT + CTRLR_LOAD +  "display";
    public final static String URL_EXPERIMENT =  APP_ROOT + CTRLR_LOAD +  "experiment";
    public final static String URL_DATASET =  APP_ROOT + CTRLR_LOAD +  "dataset";
    public final static String URL_ANALYSIS =  APP_ROOT + CTRLR_LOAD +  "analysis";
    public final static String URL_PLATFORM =  APP_ROOT + CTRLR_LOAD +  "platform";
    public final static String URL_MAPSET =  APP_ROOT + CTRLR_LOAD +  "mapset";

} // Urls
