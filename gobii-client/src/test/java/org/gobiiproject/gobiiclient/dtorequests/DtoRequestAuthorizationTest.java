package org.gobiiproject.gobiiclient.dtorequests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.Urls;
import org.gobiiproject.gobiimodel.dto.header.DtoHeaderAuth;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;
import org.gobiiproject.gobiimodel.types.GobiiHttpHeaderNames;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Phil on 5/31/2016.
 */
public class DtoRequestAuthorizationTest {


    private HttpPost makePostRequest(ServiceRequestId serviceRequestId) throws Exception {


        URI uri = new URIBuilder().setScheme("http")
                .setHost(ClientContext.getInstance().getCurrentCropDomain())
                .setPort(ClientContext.getInstance().getCurrentCropPort())
                .setPath(Urls.getRequestUrl(ControllerType.LOADER, serviceRequestId))
                .build();

        HttpPost returnVal = new HttpPost(uri);

        returnVal.addHeader("Content-Type", "application/json");
        returnVal.addHeader("Accept", "application/json");
        returnVal.addHeader(GobiiHttpHeaderNames.HEADER_GOBII_CROP,
                ClientContext.getInstance().getCurrentClientCropType().toString());

        return returnVal;
    }

    @Test
    public void testNoAuthHeaders() throws Exception {

        HttpPost postRequest = makePostRequest(ServiceRequestId.URL_AUTH);

        // WE ARE _NOT_ ADDING ANY OF THE AUTHENTICATION TOKENS
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(postRequest);
        Integer httpStatusCode = httpResponse.getStatusLine().getStatusCode();
        Assert.assertTrue("Request without authentication headers should have failed; "
                        + "status code received was "
                        + httpStatusCode
                        + "(" + httpResponse.getStatusLine().getReasonPhrase() + ")",
                httpStatusCode.equals(401));
    }

    @Test
    public void testBadCredentials() throws Exception {

        HttpPost postRequest = makePostRequest(ServiceRequestId.URL_AUTH);

        // add bogus credentials
        postRequest.addHeader(GobiiHttpHeaderNames.HEADER_USERNAME, "nobodyspecial");
        postRequest.addHeader(GobiiHttpHeaderNames.HEADER_PASSWORD, "unimaginative");

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(postRequest);


        Integer httpStatusCode = httpResponse.getStatusLine().getStatusCode();
        Assert.assertTrue("Request with bad user credentials should have failed; "
                        + "status code received was "
                        + httpStatusCode
                        + "(" + httpResponse.getStatusLine().getReasonPhrase() + ")",
                httpStatusCode.equals(401));


    }

    @Test
    public void testBadToken() throws Exception {

        HttpPost postRequest = makePostRequest(ServiceRequestId.URL_AUTH);

        // add bogus credentials
        postRequest.addHeader(GobiiHttpHeaderNames.HEADER_TOKEN, "11111111");


        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(postRequest);
        Integer httpStatusCode = httpResponse.getStatusLine().getStatusCode();
        Assert.assertTrue("Request with bad token should have failed; "
                        + "status code received was "
                        + httpStatusCode
                        + "(" + httpResponse.getStatusLine().getReasonPhrase() + ")",
                httpStatusCode.equals(401));
    }

    @Test
    public void testGoodCredentailsWithToken() throws Exception {
        HttpPost postRequestForToken = makePostRequest(ServiceRequestId.URL_AUTH);

        // add good credentials
        SystemUsers systemUsers = new SystemUsers();
        SystemUserDetail userDetail = systemUsers.getDetail(SystemUserNames.USER_READER.toString());

        postRequestForToken.addHeader(GobiiHttpHeaderNames.HEADER_USERNAME, userDetail.getUserName());
        postRequestForToken.addHeader(GobiiHttpHeaderNames.HEADER_PASSWORD, userDetail.getPassword());

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(postRequestForToken);
        Integer httpStatusCode = httpResponse.getStatusLine().getStatusCode();
        Assert.assertTrue("Request with good user credentials should have succeded; "
                        + "status code received was "
                        + httpStatusCode
                        + "(" + httpResponse.getStatusLine().getReasonPhrase() + ")",
                httpStatusCode.equals(200));

        // verify that token was sent back in the header (for clients that use that)
        List<Header> tokenHeaderist = Arrays.asList(httpResponse.getAllHeaders())
                .stream()
                .filter(header -> header.getName().equals(GobiiHttpHeaderNames.HEADER_TOKEN))
                .collect(Collectors.toList());
        Assert.assertTrue("No authentication token was returned",
                tokenHeaderist.size() == 1);

        // verify that token is also sent as part of the body (for clients that need that because
        // the headers are blocked by the browser
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader((httpResponse.getEntity().getContent())));
        StringBuilder stringBuilder = new StringBuilder();
        String currentLine = null;
        while ((currentLine = bufferedReader.readLine()) != null) {
            stringBuilder.append(currentLine);
        }

        JsonParser parser = new JsonParser();
        String jsonAsString = stringBuilder.toString();
        JsonObject jsonBodyForToken = parser.parse(jsonAsString).getAsJsonObject();
        ObjectMapper objectMapper = new ObjectMapper();

        DtoHeaderAuth dtoHeaderAuth = objectMapper.readValue(jsonBodyForToken.toString(),
                DtoHeaderAuth.class);
        Assert.assertNotNull("No dto header was returned in response body", dtoHeaderAuth);
        String tokenFromBodyResponse = dtoHeaderAuth.getToken();

        Header tokenHeader = tokenHeaderist.get(0);
        String tokenValue = tokenHeader.getValue();
        Assert.assertTrue("Token from header and token from body do not match",
                tokenFromBodyResponse.equals(tokenHeader.getValue()));

        // now test we can do a request with the token we got
        HttpPost postRequestForPing = makePostRequest(ServiceRequestId.URL_PING);
        postRequestForPing.addHeader(GobiiHttpHeaderNames.HEADER_TOKEN, tokenValue);

        HttpResponse httpResponseForToken = HttpClientBuilder.create().build().execute(postRequestForToken);
        Integer httpStatusCodeForToken = httpResponseForToken.getStatusLine().getStatusCode();
        Assert.assertTrue("Request with good user credentials should have succeded; "
                        + "status code received was "
                        + httpStatusCodeForToken
                        + "(" + httpResponse.getStatusLine().getReasonPhrase() + ")",
                httpStatusCodeForToken.equals(200));

    }

}
