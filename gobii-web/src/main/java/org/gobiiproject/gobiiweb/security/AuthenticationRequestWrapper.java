package org.gobiiproject.gobiiweb.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.IOUtils;

import javax.naming.AuthenticationException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * With minor changes and many expressions of gratitude to the kind soul who posted this solution
 * from https://coderanch.com/t/364591/java/read-request-body-filter
 * Class which is used to wrap a request in order that the wrapped request's input stream can be
 * read once and later be read again in a pseudo fashion by virtue of keeping the original payload
 * as a string which is actually what is returned by subsequent calls to getInputStream().
 */
public class AuthenticationRequestWrapper
        extends HttpServletRequestWrapper {

    Logger LOGGER = LoggerFactory.getLogger(AuthenticationRequestWrapper.class);

    private String requestBody = null;

    private byte[] savedBytes = null;

    public AuthenticationRequestWrapper(HttpServletRequest request)
            throws AuthenticationException {

        super(request);

        try {
            this.savedBytes = org.apache.commons.io.IOUtils.toByteArray(request.getInputStream());
        } catch (Exception ex) {
            throw new AuthenticationException("Error creating saved byte array for request: " + ex.getMessage());
        }
    }

    /**
     * Override of the getInputStream() method which returns an InputStream that reads from the
     * stored XML payload string instead of from the request's actual InputStream.
     */
    @Override
    public ServletInputStream getInputStream()
            throws IOException {

        if (this.savedBytes != null) {

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.savedBytes);

            return new ServletInputStream() {
                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }
            };

        } else {
            return super.getInputStream();
        }
    }

    /***
     * Or just get the body that's already been read
     * @return the body of the request
     */
    public String getRequestBody() throws Exception {

        if (this.requestBody == null) {

            if (this.savedBytes == null) {

                if(super.getInputStream() == null ) {
                    throw new Exception("There is no input stream to create a body from");
                }

                this.savedBytes = org.apache.commons.io.IOUtils.toByteArray(super.getInputStream());
            }

            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = null;
            try {
                // read the payload into the StringBuilder
                InputStream inputStream = this.getInputStream();
                if (inputStream != null) {
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    char[] charBuffer = new char[128];
                    int bytesRead = -1;
                    while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                        stringBuilder.append(charBuffer, 0, bytesRead);
                    }
                } else {
                    // make an empty string since there is no payload
                    stringBuilder.append("");
                }
            } catch (Exception ex) {
                LOGGER.error("Error reading the request payload", ex);
                throw new Exception("Error reading request payload; see log file for exception details");
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException iox) {
                        // ignore
                    }
                }
            }

            this.requestBody = stringBuilder.toString();
        }

        return requestBody;
    }
}