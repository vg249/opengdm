package org.gobiiproject.gobiiweb.automation;

import org.gobiiproject.gobiiapimodel.hateos.Link;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.restresources.UriFactory;
import org.gobiiproject.gobiiapimodel.types.RestMethodTypes;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiimodel.headerlesscontainer.DTOBase;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Phil on 9/25/2016.
 */
//There fore, the generic type for this class extends DTOBase, so we are
    //guaranteed it must have the getId() methd and the set of allowable actions
public class PayloadWriter<T extends DTOBase> {

    private final Class<T> dtoType;
    private HttpServletRequest httpServletRequest;

    public PayloadWriter(HttpServletRequest httpServletRequest,
                         Class<T> dtoType) {
        this.dtoType = dtoType;
        this.httpServletRequest = httpServletRequest;
    }

    public void writeSingleItem(PayloadEnvelope<T> payloadEnvelope,
                                ServiceRequestId serviceRequestId,
                                T itemToWrite) throws GobiiWebException , Exception{



        if( null != itemToWrite) {

            if (itemToWrite.getClass() == this.dtoType) {

                payloadEnvelope.getPayload().getData().add(itemToWrite);

                String contextPath = this.httpServletRequest.getContextPath();
                UriFactory uriFactory = new UriFactory(contextPath);
                RestUri restUri = uriFactory.resourceByUriIdParam(serviceRequestId);
                restUri.setParamValue("id",itemToWrite.getId().toString());
                //And hence we can create the link ehre

                String uri = restUri.makeUrl();
                Link link = new Link(uri,"Link to " + dtoType + ", id # " + itemToWrite.getId().toString());

                for(GobiiProcessType currentProcessType : itemToWrite.getAllowedProcessTypes()) {

                    switch( currentProcessType) {

                        case CREATE:
                            link.getMethods().add(RestMethodTypes.POST);
                        case READ:
                            link.getMethods().add(RestMethodTypes.GET);
                        case UPDATE:
                            link.getMethods().add(RestMethodTypes.PUT);
                            // add PATCH when we support that
                        case DELETE:
                            link.getMethods().add(RestMethodTypes.DELETE);
                    }
                }

                payloadEnvelope.getPayload().getLinkCollection().getLinksPerDataItem().add(link);


            } else {
                throw new GobiiWebException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "The  data item type ("
                                + itemToWrite.getClass()
                                + ") does not match the intended type("
                                + this.dtoType
                                + ")");
            }
        } else {
            throw new GobiiWebException(GobiiStatusLevel.VALIDATION,
                    GobiiValidationStatusType.BAD_REQUEST,
                    "Null dto item");

        }

    }
}
