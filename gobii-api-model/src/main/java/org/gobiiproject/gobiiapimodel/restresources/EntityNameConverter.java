package org.gobiiproject.gobiiapimodel.restresources;

import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

/**
 * Created by Phil on 10/18/2016.
 */
public class EntityNameConverter {

    public static ServiceRequestId toServiceRequestId(GobiiEntityNameType gobiiEntityNameType) throws GobiiException {


        ServiceRequestId returnVal = ServiceRequestId.UNKNOWN;

        switch(gobiiEntityNameType) {

            case ANALYSES:
                returnVal = ServiceRequestId.URL_ANALYSIS;
                break;

            case CONTACTS:
                returnVal = ServiceRequestId.URL_CONTACTS;
                break;

            case DATASETS:
                returnVal = ServiceRequestId.URL_DATASET;
                break;

            case CVTERMS:
                returnVal = ServiceRequestId.URL_CV;
                break;

            case CVGROUPS:
                returnVal = ServiceRequestId.URL_CV ;
                break;

            case PROJECTS:
                returnVal = ServiceRequestId.URL_PROJECTS;
                break;

            case ORGANIZATIONS:
                returnVal = ServiceRequestId.URL_ORGANIZATION;
                break;

            case PLATFORMS:
                returnVal = ServiceRequestId.URL_PLATFORM;
                break;

            case MANIFESTS:
                returnVal = ServiceRequestId.URL_MANIFEST;
                break;

            case MAPSETS:
                returnVal = ServiceRequestId.URL_MAPSET;
                break;

            case MARKERGROUPS:
                returnVal = ServiceRequestId.URL_MARKERGROUP ;
                break;

            case EXPERIMENTS:
                returnVal = ServiceRequestId.URL_EXPERIMENTS;
                break;

            case REFERENCES:
                returnVal = ServiceRequestId.URL_REFERENCE;
                break;

            case ROLES:
                returnVal = ServiceRequestId.URL_ROLES;
                break;

            default:
                throw new GobiiException("Unknown GobiiEntityTypeName: " + gobiiEntityNameType.toString());

        }

        return returnVal;
    }
}
