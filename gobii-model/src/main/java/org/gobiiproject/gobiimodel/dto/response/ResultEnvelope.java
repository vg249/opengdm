package org.gobiiproject.gobiimodel.dto.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Created by Phil on 9/6/2016.
 */
public class ResultEnvelope<T> {

    public ResultEnvelope() {
    }

    public ResultEnvelope<T> fromJson(JsonObject jsonObject,
                                      Class<T> dtoType) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        ResultEnvelope returnVal = objectMapper.readValue(jsonObject.toString(), ResultEnvelope.class);


        // The Jackson object mapper doesn't seem to have a means for knowing that the embedded list
        // is supposed to be cast to the DTO type. There's probably a more architectural way of doing
        // this -- e.g., a custom deserialization mechanism. But this gets the job done. Most importantly,
        // by properly casting this list of DTO objects, we prevent the Java client from caring too badly
        // about the envelope request semantics.
        JsonArray jsonArray = jsonObject.get("result").getAsJsonObject().get("data").getAsJsonArray();
        String arrayAsString = jsonArray.toString();
        List<T> resultItemList = objectMapper.readValue(arrayAsString,
                objectMapper.getTypeFactory().constructCollectionType(List.class, dtoType));

        returnVal.getResult().setData(resultItemList);

        return returnVal;

    } // fromJson

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
