// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-25
// ************************************************************************
package org.gobiiproject.gobiiclient.core;

public class Urls {

    public enum RequestId {
        URL_AUTH,
        URL_PING_EXTRACT,
        URL_PING_LOAD,
        URL_PING_PROJECT,
        URL_NAME_ID_LIST,
        URL_FILE_LOAD_INSTRUCTIONS,
        URL_DISPLAY,
        URL_CV,
        URL_CONTACT,
        URL_REFERENCE,
        URL_EXPERIMENT,
        URL_DATASET,
        URL_ANALYSIS,
        URL_MARKERGROUP,
        URL_PLATFORM,
        URL_MAPSET,
    }

    public static String getRequestUrl(RequestId requestId) throws  Exception {

        String returnVal = APP_ROOT + CTRLR_LOAD ;

        switch (requestId) {

            case URL_AUTH:
                returnVal += "auth";
                break;

            case URL_PING_LOAD:
                returnVal += "ping";
                break;

            case URL_PING_PROJECT:
                returnVal += "project";
                break;

            case URL_NAME_ID_LIST:
                returnVal += "nameidlist";
                break;

            case URL_FILE_LOAD_INSTRUCTIONS:
                returnVal += "instructions";
                break;

            case URL_DISPLAY:
                returnVal += "display";
                break;

            case URL_CV:
                returnVal += "cv";
                break;

            case URL_CONTACT:
                returnVal += "contact";
                break;

            case URL_REFERENCE:
                returnVal += "reference";
                break;

            case URL_EXPERIMENT:
                returnVal += "experiment";
                break;

            case URL_DATASET:
                returnVal += "dataset";
                break;

            case URL_ANALYSIS:
                returnVal += "analysis";
                break;

            case URL_MARKERGROUP:
                returnVal += "markergroup";
                break;

            case URL_PLATFORM:
                returnVal += "platform";
                break;

            case URL_MAPSET:
                returnVal += "mapset";
                break;

            default:
                throw new Exception("Unknown request id : " + requestId.toString());
        }

        return returnVal;
    }


    private final static String APP_ROOT = "/gobii-web/";
    private final static String CTRLR_EXTRACT = "extract/";
    private final static String CTRLR_LOAD = "load/";

    public final static String URL_AUTH = APP_ROOT + CTRLR_EXTRACT  + "auth";
    public final static String URL_PING_EXTRACT =  APP_ROOT + CTRLR_EXTRACT + "ping";
    public final static String URL_PING_LOAD =  APP_ROOT + CTRLR_LOAD +  "ping";
    public final static String URL_PING_PROJECT =  APP_ROOT + CTRLR_LOAD +  "project";
    public final static String URL_NAME_ID_LIST =  APP_ROOT + CTRLR_LOAD +  "nameidlist";
    public final static String URL_FILE_LOAD_INSTRUCTIONS =  APP_ROOT + CTRLR_LOAD +  "instructions";
    public final static String URL_DISPLAY =  APP_ROOT + CTRLR_LOAD +  "display";
    public final static String URL_CV =  APP_ROOT + CTRLR_LOAD +  "cv";
    public final static String URL_CONTACT=  APP_ROOT + CTRLR_LOAD +  "contact";
    public final static String URL_REFERENCE=  APP_ROOT + CTRLR_LOAD +  "reference";
    public final static String URL_EXPERIMENT =  APP_ROOT + CTRLR_LOAD +  "experiment";
    public final static String URL_DATASET =  APP_ROOT + CTRLR_LOAD +  "dataset";
    public final static String URL_ANALYSIS =  APP_ROOT + CTRLR_LOAD +  "analysis";
    public final static String URL_MARKERGROUP =  APP_ROOT + CTRLR_LOAD +  "markergroup";
    public final static String URL_PLATFORM =  APP_ROOT + CTRLR_LOAD +  "platform";
    public final static String URL_MAPSET =  APP_ROOT + CTRLR_LOAD +  "mapset";

} // Urls
