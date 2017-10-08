package org.gobiiproject.gobiiweb.spring;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiCropConfig;
import org.gobiiproject.gobiimodel.types.GobiiDbType;
import org.gobiiproject.gobiiweb.DataSourceSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
