package org.gobiiproject.gobiimodel.dto.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phil on 9/6/2016.
 */
public class RequestEnvelope<T> {

    public RequestEnvelope() {}

    public RequestEnvelope(T requestData, Header.ProcessType processType) {
        this.requestDataItems.add(requestData);
        this.header.setProcessType(processType);
    }

    private List<T> requestDataItems = new ArrayList<>();

    Header header = new Header();

    public Header getHeader() {
        return header;
    }
    public void setHeader(Header header) {
        this.header = header;
    }

    public List<T> getRequestDataItems() {
        return requestDataItems;
    }

    public void setRequestDataItems(List<T> requestDataItems) {
        this.requestDataItems = requestDataItems;
    }
}
