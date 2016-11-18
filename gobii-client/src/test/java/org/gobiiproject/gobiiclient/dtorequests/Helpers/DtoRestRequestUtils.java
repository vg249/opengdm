package org.gobiiproject.gobiiclient.dtorequests.Helpers;

import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.RestUri;
import org.gobiiproject.gobiiapimodel.types.ServiceRequestId;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.restmethods.RestResource;
import org.gobiiproject.gobiimodel.headerlesscontainer.ContactDTO;
import org.gobiiproject.gobiimodel.headerlesscontainer.DTOBase;
import org.gobiiproject.gobiimodel.tobemovedtoapimodel.HeaderStatusMessage;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;


/**
 * This class encapsulates common standard functionality that can be used for retreiving and
 * getting statistics about entities. It is used with the modern RestRequest client
 * infrastructure only. Note that getEnvelopeResultList() is implicitly testing that
 * if there are no items to be retrieved, the list has 0 items. There is no easy deterministic
 * way to do this -- this is the best we can do.
 * @param <T>
 */
public class DtoRestRequestUtils<T extends DTOBase> {

    private Class<T> dtoType;
    private ServiceRequestId serviceRequestId;

    public DtoRestRequestUtils(Class<T> dtoType, ServiceRequestId serviceRequestId) {
        this.dtoType = dtoType;
        this.serviceRequestId = serviceRequestId;
    }

    public PayloadEnvelope<T> getEnvelopeResultList() throws Exception {

        PayloadEnvelope<T> returnVal;

        RestUri restUri = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(this.serviceRequestId);
        RestResource<T> restResource = new RestResource<>(restUri);

        returnVal = restResource.get(dtoType);

        if (!returnVal.getHeader().getStatus().isSucceeded()) {
            String message = "Request for collection of "
                    + dtoType.getClass()
                    + " with request "
                    + restUri.makeUrl()
                    + " did not succeded with URI "
                    + restUri.makeUrl()
                    + ": ";

            for (HeaderStatusMessage headerStatusMessage : returnVal.getHeader().getStatus().getStatusMessages()) {
                message += headerStatusMessage.getMessage();
            }

            throw new Exception(message);
        }

        if (returnVal.getPayload().getData().size() > 0) {

            boolean gotNullResult = ((returnVal
                    .getPayload()
                    .getData()
                    .stream()
                    .filter(i -> i.getId() == null))
                    .count() > 0);

            if (gotNullResult) {

                String message = "When the collection "
                        + dtoType.getClass()
                        + " with request "
                        + restUri.makeUrl()
                        + " has no results, it should return an empty list!!!";

                throw (new Exception(message));
            }
        }

        return returnVal;

    }


    // this only works if all create() methods put their PK value into
    public Integer getMaxPkVal() throws Exception {

        Integer returnVal = 0;

        PayloadEnvelope<T> resultEnvelope = this.getEnvelopeResultList();

            T dto = resultEnvelope
                    .getPayload()
                    .getData()
                    .stream()
                    .max((p1, p2) -> Integer.compare(p1.getId(), p2.getId()))
                    .orElse(null);

            if (dto != null) {
                returnVal = dto.getId();
            }

        return returnVal;

    } //


    public PayloadEnvelope<T> getResponseEnvelopeForEntityId(String id) throws Exception {

        PayloadEnvelope<T> returnVal;

        RestUri restUriContact = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceByUriIdParam(this.serviceRequestId);

        restUriContact.setParamValue("id", id);
        RestResource<T> restResource = new RestResource<>(restUriContact);

        returnVal = restResource
                .get(this.dtoType);

        return returnVal;

    }

}
