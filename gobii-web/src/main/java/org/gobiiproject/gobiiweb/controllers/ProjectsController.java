/**
 * ProjectController.java
 * Gobii API endpoint controllers for projects
 * 
 * 
 * @author Rodolfo N. Duldulao, Jr. <rnduldulaojr@gmail.com>
 * @version 1.0
 * @since 2020-03-06
 */
package org.gobiiproject.gobiiweb.controllers;

import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;


@Scope(value = "request")
@RestController
@RequestMapping(GobiiControllerType.SERVICE_PATH_GOBII_V3)
@Api()
@CrossOrigin
public class ProjectsController {

    @GetMapping("/projects")
    public PayloadEnvelope<String> getProjectsList() {
        return new PayloadEnvelope<>("test", GobiiProcessType.NONE);
    }

}

