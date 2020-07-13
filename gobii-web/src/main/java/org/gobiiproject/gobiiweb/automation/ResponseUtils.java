package org.gobiiproject.gobiiweb.automation;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.ErrorPayload;

public class ResponseUtils {
    
    private static String UNAUTHORIZED_PAYLOAD;

    static{
        ErrorPayload payload = new ErrorPayload();
        payload.setError("Unauthorized");
        ObjectMapper mapper = new ObjectMapper();
        try {
            UNAUTHORIZED_PAYLOAD = mapper.writeValueAsString(payload);
        } catch (JsonProcessingException jpe) {
            UNAUTHORIZED_PAYLOAD = "Unauthorized";
        }
    }

    public static void sendUnauthorizedResponse(HttpServletResponse response) throws IOException {
        //throw unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        PrintWriter out = response.getWriter();
        out.println(UNAUTHORIZED_PAYLOAD);
       
    }
    
}