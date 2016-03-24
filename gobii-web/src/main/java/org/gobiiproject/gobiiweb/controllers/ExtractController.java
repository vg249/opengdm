// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiiweb.controllers;

import org.gobiiproject.gobidomain.services.MarkerService;
import org.gobiiproject.gobiimodel.dto.container.ContentTypeDTO;
import org.gobiiproject.gobiimodel.dto.container.MarkerGroupDTO;
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
@RequestMapping("/resource")
public class ExtractController {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ExtractController.class);

    @Autowired
    private MarkerService markerService = null;


//    @Autowired
//    ResourceService resourceService;
//
//    public void setResourceService(ResourceService resourceService) {
//        this.resourceService = resourceService;
//    }
//

//    @RequestMapping(method = RequestMethod.POST)
//    @ResponseBody
//    public MarkerGroupDTO getByContentType(@RequestBody MarkerGroupDTO MarkerGroupDTO) {
//
//        MarkerGroupDTO returnVal = null;
//        try {
//            //returnVal = resourceService.addResource(MarkerGroupDTO);
//        } catch (AccessDeniedException e) {
//
//            String msg = e.getMessage();
//            String tmp = msg;
//            throw(e);
//        }
//
//        return (returnVal);
//
//    }//getByContentType()


    @RequestMapping(value = "/search/bycontenttype", method = RequestMethod.POST)
    @ResponseBody
    public MarkerGroupDTO getByContentType(@RequestBody ContentTypeDTO contentTypeDTO) {

//        MarkerGroupDTO returnVal = new MarkerGroupDTO();
        MarkerGroupDTO returnVal = null;
        try {
             //returnVal = resourceService.getResourcesByContentType(contentTypeDTO);
            returnVal = markerService.getMarkers(null);
        } catch (AccessDeniedException e) {

            String msg = e.getMessage();
            String tmp = msg;
            throw (e);
        }

        return (returnVal);

    }//getByContentType()

//    @RequestMapping(value = "/bar", method = RequestMethod.GET)
//    public ContentTypeDTO testGet() {
//        LOGGER.trace("Got to controller");
//        return (new ContentTypeDTO(0, "fooname", "fooscope"));
//    }//testGet()
//
//    @Autowired
//    private DumbDTO autoWiredDTO;
//
//    @RequestMapping(value = "/dumbPost", method = RequestMethod.POST)
//    @ResponseBody
//    public DumbDTO testDumbPost(@RequestBody ContentTypeDTO dumbDTO) {
//        LOGGER.trace("Got to controller");
//        //DumbDTO returnVal = new DumbDTO("name");
//        DumbDTO returnVal = autoWiredDTO;
//        returnVal.setName("Received -- " + dumbDTO.getName());
//        returnVal.setScope("Received -- " + dumbDTO.getScope());
//        returnVal.plainMessages.add("plain test mesage 01");
//        returnVal.plainMessages.add("plain test mesage 02");
//        returnVal.statusMessages.add(new HeaderStatusMessage(DtoHeaderResponse.StatusLevel.Ok, "Example message 1"));
//        returnVal.statusMessages.add(new HeaderStatusMessage(DtoHeaderResponse.StatusLevel.Ok, "Example message 1"));
//        returnVal.DTOHeaderResponseHere.addStatusMessage(DtoHeaderResponse.StatusLevel.Info, "In Message One");
//        returnVal.DTOHeaderResponseHere.addStatusMessage(DtoHeaderResponse.StatusLevel.Info, "In Message Two");
//        return (returnVal);
//    }//testPost()
//
//    @RequestMapping(value = "/dumbGet", method = RequestMethod.GET)
//    @ResponseBody
//    public DumbDTO tesDumbtGet() {
//        DumbDTO dumbDTO = new DumbDTO();
//        dumbDTO.setName("dumb name");
//        dumbDTO.setScope("dumb scope");
//        dumbDTO.statusMessages.add(new HeaderStatusMessage(DtoHeaderResponse.StatusLevel.Ok, "Example message 1"));
//        dumbDTO.statusMessages.add(new HeaderStatusMessage(DtoHeaderResponse.StatusLevel.Ok, "Example message 1"));
//
//
//        return (dumbDTO);
//    }//testGet()


}//ResourceController
