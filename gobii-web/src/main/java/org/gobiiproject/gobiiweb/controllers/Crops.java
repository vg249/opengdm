package org.gobiiproject.gobiiweb.controllers;

import io.swagger.annotations.Api;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Scope(value = "request")
@Controller
@RequestMapping("/")
@CrossOrigin
@Api
public class Crops {



}
