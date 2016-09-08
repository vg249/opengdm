package org.gobiiproject.gobiimodel.dto.response;

/**
 * Created by Phil on 9/6/2016.
 */
public class RequestEnvelope<T> {

    public RequestEnvelope() {}

    public RequestEnvelope(T requestData, Header.ProcessType processType) {
        this.requestData = requestData;
        this.header.setProcessType(processType);
    }

    private T requestData = null;

    Header header = new Header();

    public Header getHeader() {
        return header;
    }
    public void setHeader(Header header) {
        this.header = header;
    }

    public T getRequestData() {
        return requestData;
    }

    public void setRequestData(T requestData) {
        this.requestData = requestData;
    }
}
