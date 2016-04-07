// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiiweb.controllers;

import org.gobiiproject.gobidomain.services.PingService;
import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobiimodel.dto.container.PingDTO;
import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;
import org.gobiiproject.gobiimodel.logutils.LineUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by MrPhil on 7/6/2015.
 */
@Scope(value = "request")
@Controller
@RequestMapping("/load")
public class LoadController {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(LoadController.class);

    @Autowired
    private PingService pingService = null;

    @Autowired
    private ProjectService projectService = null;


    @RequestMapping(value = "/ping", method = RequestMethod.POST)
    @ResponseBody
    public PingDTO getPingResponse(@RequestBody PingDTO pingDTORequest) {

        PingDTO returnVal = null;
        //PingDTO pingDTORequest = new PingDTO();
        try {
            returnVal = pingService.getPings(pingDTORequest);
            String newResponseString = LineUtils.wrapLine("Loader controller responded");
            returnVal.getPingResponses().add(newResponseString);
        } catch (AccessDeniedException e) {

            String msg = e.getMessage();
            String tmp = msg;
            throw (e);
        }
        return (returnVal);

    }//getPingResponse()


    @RequestMapping(value = "/project", method = RequestMethod.POST)
    @ResponseBody
    public ProjectDTO getPingResponse(@RequestBody ProjectDTO projectDTO) {

        ProjectDTO returnVal = null;
        //PingDTO pingDTORequest = new PingDTO();
        try {
            returnVal = projectService.getProject(projectDTO);
        } catch (AccessDeniedException e) {

            String msg = e.getMessage();
            String tmp = msg;
            throw (e);
        }
        return (returnVal);

    }//getPingResponse()



}// LoadController
