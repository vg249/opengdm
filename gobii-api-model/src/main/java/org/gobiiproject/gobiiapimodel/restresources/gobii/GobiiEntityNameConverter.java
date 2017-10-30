package org.gobiiproject.gobiiapimodel.restresources.gobii;

import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.types.GobiiServiceRequestId;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;

/**
 * Created by Phil on 10/18/2016.
 */
public class GobiiEntityNameConverter {

    public static RestUri toServiceRequestId(String contextRoot,
                                             GobiiEntityNameType gobiiEntityNameType)
            throws GobiiException {


        RestUri returnVal;
        try {
            switch (gobiiEntityNameType) {

                case ANALYSIS:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_ANALYSIS);
                    break;

                case CONTACT:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_CONTACTS);
                    break;

                case DATASET:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_DATASETS);
                    break;

                case CV:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_CV);
                    break;

                case CVGROUP:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_CV);
                    break;

                case PROJECT:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_PROJECTS);
                    break;

                case ORGANIZATION:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_ORGANIZATION);
                    break;

                case PLATFORM:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_PLATFORM);
                    break;

                case MANIFEST:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_MANIFEST);
                    break;

                case MAPSET:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_MAPSET);
                    break;

                case MARKER_GROUP:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_MARKERGROUP);
                    break;

                case EXPERIMENT:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_EXPERIMENTS);
                    break;

                case REFERENCE:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_REFERENCE);
                    break;

                case ROLE:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_ROLES);
                    break;

                case PROTOCOL:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_PROTOCOL);
                    break;

                case VENDORS_PROTOCOL:
                    returnVal = GobiiUriFactory.resourceByUriIdParam(contextRoot,
                            GobiiServiceRequestId.URL_PROTOCOL)
                            .appendSegment(GobiiServiceRequestId.URL_VENDORS);
                    break;

                default:
                    throw new GobiiException("Unknown GobiiEntityTypeName: " + gobiiEntityNameType.toString());

            }
        } catch (Exception e) {
            throw new GobiiException(e);
        }

        return returnVal;
    }
}
