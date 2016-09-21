package org.gobiiproject.gobiimodel.dto.response;

/**
 * Created by Phil on 9/6/2016.
 */
public class RequestEnvelope<T> {

    public RequestEnvelope() {
    }

    public RequestEnvelope(T requestData, Header.ProcessType processType) {
        this.header.setProcessType(processType);
        this.payload.getData().add(requestData);
    }

    private Payload<T> payload = new Payload<>();

    Header header = new Header();

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Payload<T> getPayload() {
        return payload;
    }

    public void setPayload(Payload<T> payload) {
        this.payload = payload;
    }
}
