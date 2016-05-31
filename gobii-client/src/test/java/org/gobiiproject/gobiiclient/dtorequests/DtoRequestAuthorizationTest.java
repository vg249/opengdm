package org.gobiiproject.gobiiclient.dtorequests;

import org.apache.http.HttpResponse;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.gobiiproject.gobiiclient.core.ClientContext;
import org.gobiiproject.gobiiclient.core.Urls;
import org.gobiiproject.gobiimodel.dto.types.ControllerType;
import org.gobiiproject.gobiimodel.dto.types.ServiceRequestId;
import org.gobiiproject.gobiimodel.types.GobiiHttpHeaderNames;
import org.gobiiproject.gobiimodel.types.SystemUserDetail;
import org.gobiiproject.gobiimodel.types.SystemUserNames;
import org.gobiiproject.gobiimodel.types.SystemUsers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Phil on 5/31/2016.
 */
public class DtoRequestAuthorizationTest {


    private HttpPost makePostRequest(ServiceRequestId serviceRequestId ) throws Exception {


        URI uri = new URIBuilder().setScheme("http")
                .setHost(ClientContext.getInstance().getCurrentCropDomain())
                .setPort(ClientContext.getInstance().getCurrentCropPort())
                .setPath(Urls.getRequestUrl(ControllerType.LOADER, serviceRequestId ))
                .build();

        HttpPost returnVal = new HttpPost(uri);

        returnVal.addHeader("Content-Type", "application/json");
        returnVal.addHeader("Accept", "application/json");
        returnVal.addHeader(GobiiHttpHeaderNames.HEADER_GOBII_CROP,
                ClientContext.getInstance().getCurrentClientCropType().toString());

        return  returnVal;
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

        List<Header> tokenHeaderist = Arrays.asList( httpResponse.getAllHeaders() )
                .stream()
                .filter( header -> header.getName().equals(GobiiHttpHeaderNames.HEADER_TOKEN))
                .collect(Collectors.toList());


        Assert.assertTrue("No authentication token was returned",
                tokenHeaderist.size() == 1 );

        Header tokenHeader = tokenHeaderist.get(0);
        String tokenValue = tokenHeader.getValue();

        HttpPost postRequestForPing = makePostRequest(ServiceRequestId.URL_PING);
        postRequestForPing.addHeader(GobiiHttpHeaderNames.HEADER_TOKEN,tokenValue);

        HttpResponse httpResponseForToken = HttpClientBuilder.create().build().execute(postRequestForToken);
        Integer httpStatusCodeForToken = httpResponseForToken.getStatusLine().getStatusCode();
        Assert.assertTrue("Request with good user credentials should have succeded; "
                        + "status code received was "
                        + httpStatusCodeForToken
                        + "(" + httpResponse.getStatusLine().getReasonPhrase() + ")",
                httpStatusCodeForToken.equals(200));

    }

}
