package org.gobiiproject.gobiimodel.dto.response;

/**
 * Created by Phil on 9/6/2016.
 */
public class ResultEnvelope<T> {

    Header header = new Header();
    Result<T> result = new Result<>();

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Result<T> getResult() {
        return result;
    }

    public void setResult(Result<T> result) {
        this.result = result;
    }
}
