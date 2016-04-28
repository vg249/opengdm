// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiiweb.controllers;

import org.gobiiproject.gobidomain.services.*;
import org.gobiiproject.gobiimodel.dto.container.*;
import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;
import org.gobiiproject.gobiimodel.utils.LineUtils;
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
    
    @Autowired
    private AnalysisService analysisService = null;
    
    @Autowired
    private ExperimentService experimentService = null;

    @Autowired
    private NameIdListService nameIdListService = null;

    @Autowired
    private LoaderInstructionFilesService loaderInstructionFilesService = null;

    @Autowired
    private DisplayService displayService = null;

    @Autowired
    private DataSetService dataSetService = null;

    @Autowired
    private PlatformService platformService = null;
    
    @Autowired 
    private MapsetService mapsetService;

    @RequestMapping(value = "/ping", method = RequestMethod.POST)
    @ResponseBody
    public PingDTO getPingResponse(@RequestBody PingDTO pingDTORequest) {

        PingDTO returnVal = null;
        //PingDTO pingDTORequest = new PingDTO();
        try {
            returnVal = pingService.getPings(pingDTORequest);
            String newResponseString = LineUtils.wrapLine("Loader controller responded");
            returnVal.getPingResponses().add(newResponseString);
        } catch (Exception e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return (returnVal);

    }//getPingResponse()


    @RequestMapping(value = "/experiment", method = RequestMethod.POST)
    @ResponseBody
    public ExperimentDTO getPingResponse(@RequestBody ExperimentDTO experimentDTO) {

        ExperimentDTO returnVal = null;
        //PingDTO pingDTORequest = new PingDTO();
        try {
            returnVal = experimentService.getExperiment(experimentDTO);
        } catch (AccessDeniedException e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
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

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }
        return (returnVal);

    }//getPingResponse()


    @RequestMapping(value = "/nameidlist", method = RequestMethod.POST)
    @ResponseBody
    public NameIdListDTO getNameIdList(@RequestBody NameIdListDTO nameIdListDTO) {

        NameIdListDTO returnVal = new NameIdListDTO();

        try {
            returnVal = nameIdListService.getNameIdList(nameIdListDTO);
        } catch (AccessDeniedException e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return (returnVal);

    }

    @RequestMapping(value = "/instructions", method = RequestMethod.POST)
    @ResponseBody
    public LoaderInstructionFilesDTO processDataset(@RequestBody LoaderInstructionFilesDTO loaderInstructionFilesDTO) {

        LoaderInstructionFilesDTO returnVal = new LoaderInstructionFilesDTO();

        try {
            returnVal = loaderInstructionFilesService.writeSampleLoaderFileInstructions(loaderInstructionFilesDTO);
        } catch (AccessDeniedException e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return (returnVal);

    }

    @RequestMapping(value = "/display", method = RequestMethod.POST)
    @ResponseBody
    public DisplayDTO processDataset(@RequestBody DisplayDTO displayDTO) {

        DisplayDTO returnVal = new DisplayDTO();

        try {
            returnVal = displayService.getDisplayNames(displayDTO);
        } catch (AccessDeniedException e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return (returnVal);

    }

    @RequestMapping(value = "/dataset", method = RequestMethod.POST)
    @ResponseBody
    public DataSetDTO processDataset(@RequestBody DataSetDTO dataSetDTO) {

        DataSetDTO returnVal = new DataSetDTO();

        try {
            returnVal = dataSetService.processDataSet(dataSetDTO);
        } catch (AccessDeniedException e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return (returnVal);

    }

    @RequestMapping(value = "/analysis", method = RequestMethod.POST)
    @ResponseBody
    public AnalysisDTO processDataset(@RequestBody AnalysisDTO analysisDTO) {

        AnalysisDTO returnVal = new AnalysisDTO();

        try {
            returnVal = analysisService.getAnalysisDetails(analysisDTO);
        } catch (AccessDeniedException e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return (returnVal);

    }

    @RequestMapping(value = "/platform", method = RequestMethod.POST)
    @ResponseBody
    public PlatformDTO processDataset(@RequestBody PlatformDTO platformDTO) {

        PlatformDTO returnVal = new PlatformDTO();

        try {
            returnVal = platformService.processPlatform(platformDTO);
        } catch (AccessDeniedException e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return (returnVal);

    }

    @RequestMapping(value = "/mapset", method = RequestMethod.POST)
    @ResponseBody
    public MapsetDTO processDataset(@RequestBody MapsetDTO MapsetDTO) {

        MapsetDTO returnVal = new MapsetDTO();

        try {
            returnVal = mapsetService.processMapset(MapsetDTO);
        } catch (AccessDeniedException e) {

            returnVal.getDtoHeaderResponse().addException(e);
            LOGGER.error(e.getMessage());
        }

        return (returnVal);

    }

}// LoadController
