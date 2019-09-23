package org.gobiiproject.gobiiweb.spring;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Created by Phil on 8/16/2016.
 */


public class OncePerRequestCaptureThreadLocal extends OncePerRequestFilter {

    private ThreadLocal<HttpServletRequest> currentRequest;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException  {
        currentRequest.set(request);
        filterChain.doFilter(request, response);
    }

    public ThreadLocal<HttpServletRequest> getCurrentRequest() {
        return currentRequest;
    }

    public void setCurrentRequest(ThreadLocal<HttpServletRequest> currentRequest) {
        this.currentRequest = currentRequest;
    }
}
