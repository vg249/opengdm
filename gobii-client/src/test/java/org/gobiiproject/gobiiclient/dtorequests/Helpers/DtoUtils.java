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

public class DtoUtils<T extends DTOBase> {

    private Class<T> dtoType;

    public DtoUtils(Class<T> dtoType) {
        this.dtoType = dtoType;
    }

    // this only works if all create() methods put their PK value into
    public Integer getMaxPkVal(ServiceRequestId serviceRequestId) throws Exception {

        Integer returnVal = 0;

        RestUri restUriContact = ClientContext.getInstance(null, false)
                .getUriFactory()
                .resourceColl(serviceRequestId);
        RestResource<T> restResource = new RestResource<>(restUriContact);
        PayloadEnvelope<T> resultEnvelope = restResource
                .get(dtoType);

        if (!resultEnvelope.getHeader().getStatus().isSucceeded()) {
            String message = "Request for collection of "
                    + dtoType.getClass()
                    + " did not succeded";

            for (HeaderStatusMessage headerStatusMessage : resultEnvelope.getHeader().getStatus().getStatusMessages()) {
                message += headerStatusMessage.getMessage();
            }

            throw new Exception(message);
        }

        if (resultEnvelope.getPayload().getData().size() > 0) {


            if ((resultEnvelope.getPayload().getData().size() == 1) &&
                    (resultEnvelope.getPayload().getData().get(0).getId() == null)) {

                String message = "When the collection "
                        + dtoType.getClass()
                        + " has no results, it should return an empty list!!!";

                throw (new Exception(message));

            }

            T dto = resultEnvelope
                    .getPayload()
                    .getData()
                    .stream()
                    .max((p1, p2) -> Integer.compare(p1.getId(), p2.getId()))
                    .orElse(null);

            if (dto != null) {
                returnVal = dto.getId();
            }
        }

        return returnVal;

    } //

}
