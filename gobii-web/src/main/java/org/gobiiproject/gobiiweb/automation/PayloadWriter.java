package org.gobiiproject.gobiiweb.automation;

import org.gobiiproject.gobiiapimodel.hateos.Link;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiapimodel.payload.HeaderAuth;
import org.gobiiproject.gobiimodel.types.GobiiHttpHeaderNames;
import org.gobiiproject.gobiimodel.types.RestMethodTypes;
import org.gobiiproject.gobiimodel.dto.base.DTOBase;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiimodel.utils.LineUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Phil on 9/25/2016.
 */

public class PayloadWriter<T extends DTOBase> {

    private final Class<T> dtoType;
    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;
    private String gobiiWebVersion;

    public PayloadWriter(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         Class<T> dtoType) throws Exception {

        this.dtoType = dtoType;
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;

        this.gobiiWebVersion = GobiiVersionInfo.getVersion();
    }

    /***
     * For most DTOs, in the response we want to include HATEOS  links that are
     * in line with the DTOs permissions. the generic type for this class extends DTOBase, so we are
     * theoretically guaranteed it must have the getId() methd and the set of allowable actions.
     * That being said, there are DTOs in which the getId() is not meaningful. That it is sometimes
     * meaningful and sometimes not breaks inheritance and is at best clumsy OO design. However, the
     * overall shape of the generalization is sound. In the case where we the ID is not HATEOSable,
     * the id parameter can be set to null, in which case the HATEOS links are not created.
     * @param payloadEnvelope
     * @param restUri
     * @param itemToWrite
     * @param id
     * @throws Exception
     */
    public void writeSingleItemForId(PayloadEnvelope<T> payloadEnvelope,
                                     RestUri restUri,
                                     T itemToWrite,
                                     String id) throws Exception {

        if ((null != itemToWrite) &&
                !LineUtils.isNullOrEmpty(id) &&
                (itemToWrite.getId() != null) &&
                (itemToWrite.getId() > 0)) {

            if (itemToWrite.getClass() == this.dtoType) {

                payloadEnvelope.getPayload().getData().add(itemToWrite);

                if( restUri != null ) {
                    restUri.setParamValue("id", id);
                    //And hence we can create the link ehre

                    String uri = restUri.makeUrlPath();
                    Link link = new Link(uri, "Link to " + dtoType + ", id " + id);

                    for (GobiiProcessType currentProcessType : itemToWrite.getAllowedProcessTypes()) {

                        switch (currentProcessType) {

                            case CREATE:
                                link.getMethods().add(RestMethodTypes.POST);
                                break;
                            case READ:
                                link.getMethods().add(RestMethodTypes.GET);
                                break;
                            case UPDATE:
                                link.getMethods().add(RestMethodTypes.PUT);
                                // add PATCH when we support that
                                break;
                            case DELETE:
                                link.getMethods().add(RestMethodTypes.DELETE);
                        }
                    }
                    payloadEnvelope.getPayload().getLinkCollection().getLinksPerDataItem().add(link);
                }


                payloadEnvelope.getHeader().setGobiiVersion(this.gobiiWebVersion);
                setAuthHeader(payloadEnvelope.getHeader().getDtoHeaderAuth(),this.httpServletResponse);
                payloadEnvelope.getHeader().setCropType(payloadEnvelope.getHeader().getDtoHeaderAuth().getGobiiCropType());


            } else {
                throw new GobiiWebException(GobiiStatusLevel.VALIDATION,
                        GobiiValidationStatusType.BAD_REQUEST,
                        "The  data item type ("
                                + itemToWrite.getClass()
                                + ") does not match the intended type("
                                + this.dtoType
                                + ")");
            }
        }
    }

    public void writeSingleItemForDefaultId(PayloadEnvelope<T> payloadEnvelope,
                                            RestUri restUri,
                                            T itemToWrite) throws GobiiWebException, Exception {

        if ((null != itemToWrite) && (itemToWrite.getId() != null)) {
            String id = itemToWrite.getId().toString();

            this.writeSingleItemForId(payloadEnvelope,
                    restUri,
                    itemToWrite,
                    id);
        }


        payloadEnvelope.getHeader().setGobiiVersion(this.gobiiWebVersion);

    }

    public void writeList(PayloadEnvelope<T> payloadEnvelope,
                          RestUri restUri,
                          List<T> itemsToWrite) throws GobiiWebException, Exception {

        for (T currentItem : itemsToWrite) {
            this.writeSingleItemForDefaultId(payloadEnvelope, restUri, currentItem);
        }


        payloadEnvelope.getHeader().setGobiiVersion(this.gobiiWebVersion);
    }

    public void setAuthHeader(HeaderAuth headerAuth, HttpServletResponse response) {

        String userName = response.getHeader(GobiiHttpHeaderNames.HEADER_NAME_USERNAME);
        String token = response.getHeader(GobiiHttpHeaderNames.HEADER_NAME_TOKEN);
        String gobiiCropType = response.getHeader(GobiiHttpHeaderNames.HEADER_NAME_GOBII_CROP);

        headerAuth.setToken(token);
        headerAuth.setGobiiCropType(gobiiCropType);
        headerAuth.setUserName(userName);

    }
}
