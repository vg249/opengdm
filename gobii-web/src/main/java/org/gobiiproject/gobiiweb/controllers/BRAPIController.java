// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiiweb.controllers;

import org.gobiiproject.gobidomain.services.AnalysisService;
import org.gobiiproject.gobidomain.services.ConfigSettingsService;
import org.gobiiproject.gobidomain.services.ContactService;
import org.gobiiproject.gobidomain.services.CvService;
import org.gobiiproject.gobidomain.services.DataSetService;
import org.gobiiproject.gobidomain.services.DisplayService;
import org.gobiiproject.gobidomain.services.ExperimentService;
import org.gobiiproject.gobidomain.services.LoaderInstructionFilesService;
import org.gobiiproject.gobidomain.services.ManifestService;
import org.gobiiproject.gobidomain.services.MapsetService;
import org.gobiiproject.gobidomain.services.MarkerGroupService;
import org.gobiiproject.gobidomain.services.NameIdListService;
import org.gobiiproject.gobidomain.services.OrganizationService;
import org.gobiiproject.gobidomain.services.PingService;
import org.gobiiproject.gobidomain.services.PlatformService;
import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobidomain.services.ReferenceService;
import org.gobiiproject.gobiimodel.dto.response.Header;
import org.gobiiproject.gobiimodel.dto.container.ContactDTO;
import org.gobiiproject.gobiimodel.dto.container.PingDTO;
import org.gobiiproject.gobiimodel.dto.response.PayloadEnvelope;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


/**
 * Created by MrPhil on 7/6/2015.
 */
@Scope(value = "request")
@Controller
@RequestMapping("/brapi/v1")
public class BRAPIController {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BRAPIController.class);

    @Autowired
    private PingService pingService = null;

    @Autowired
    private ProjectService projectService = null;

    @Autowired
    private ContactService contactService = null;

    @Autowired
    private ReferenceService referenceService = null;

    @Autowired
    private AnalysisService analysisService = null;

    @Autowired
    private ManifestService manifestService = null;

    @Autowired
    private MarkerGroupService markerGroupService = null;

    @Autowired
    private OrganizationService organizationService = null;

    @Autowired
    private ExperimentService experimentService = null;

    @Autowired
    private NameIdListService nameIdListService = null;

    @Autowired
    private LoaderInstructionFilesService loaderInstructionFilesService = null;

    @Autowired
    private DisplayService displayService = null;

    @Autowired
    private CvService cvService = null;

    @Autowired
    private DataSetService dataSetService = null;

    @Autowired
    private PlatformService platformService = null;

    @Autowired
    private MapsetService mapsetService;

    @Autowired
    private ConfigSettingsService configSettingsService;

    @RequestMapping(value = "/ping", method = RequestMethod.POST)
    @ResponseBody
    public PingDTO getPingResponse(@RequestBody PingDTO pingDTORequest) {

        PingDTO returnVal = new PingDTO();

        try {
            returnVal = pingService.getPings(pingDTORequest);
            String newResponseString = LineUtils.wrapLine("Loader controller responded");
            returnVal.getPingResponses().add(newResponseString);
        } catch (Exception e) {

            returnVal.getStatus().addException(e);
            LOGGER.error(e.getMessage());
        }

        return (returnVal);

    }//getPingResponse()

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    @ResponseBody
    public String authenticate(@RequestBody String noContentExpected) {

        String returnVal = null;
        try {
            returnVal = "Authenticated: " + (new Date()).toString();
        } catch (Exception e) {
            String msg = e.getMessage();
            String tmp = msg;
            try {
                throw (e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        return (returnVal);

    }


    @RequestMapping(value = "/contacts", method = RequestMethod.POST)
    @ResponseBody
    public PayloadEnvelope<ContactDTO> createContact(@RequestBody PayloadEnvelope<ContactDTO> payloadEnvelope,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) {

        PayloadEnvelope<ContactDTO> returnVal = new PayloadEnvelope<>();
        try {

            returnVal = contactService.createContact(payloadEnvelope);

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(), response, HttpStatus.CREATED);

        return (returnVal);

    }

    @RequestMapping(value = "/contacts/{contactId:[\\d]+}", method = RequestMethod.PUT)
    @ResponseBody
    public PayloadEnvelope<ContactDTO> replaceContact(@RequestBody PayloadEnvelope<ContactDTO> payloadEnvelope,
                                                      @PathVariable Integer contactId,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) {

        PayloadEnvelope<ContactDTO> returnVal = new PayloadEnvelope<>();
        try {

            returnVal = contactService.replaceContact(contactId, payloadEnvelope);

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(), response, HttpStatus.OK);

        return (returnVal);

    }


    @RequestMapping(value = "/contacts/{contactId:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ContactDTO> getContactsById(@PathVariable Integer contactId,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response) {

        PayloadEnvelope<ContactDTO> returnVal = new PayloadEnvelope<>();
        try {

            returnVal = contactService.getContactById(contactId);

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(), response, HttpStatus.OK);

        return (returnVal);

    }

    //Technically, this regex specifies an email address format, and it actually works.
    //However, when you execute this, you get back an error "The resource identified by this request is only
    // capable of generating responses with characteristics not acceptable according to the request "accept" headers."
    // In other words, the email address is telling the server that you're asking for some other format
    // So for email based searches, you'll have to use the request parameter version
    @RequestMapping(value = "/contacts/{email:[a-zA-Z-]+@[a-zA-Z-]+.[a-zA-Z-]+}",
            method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ContactDTO> getContactsByEmail(@PathVariable String email,
                                                          HttpServletRequest request,
                                                          HttpServletResponse response) {

        PayloadEnvelope<ContactDTO> returnVal = new PayloadEnvelope<>();
        try {

            ContactDTO contactRequestDTO = new ContactDTO();
            contactRequestDTO.setContactId(1);
            //contactRequestDTO.setEmail(email);
            returnVal = contactService.processDml(new PayloadEnvelope<>(contactRequestDTO, Header.ProcessType.READ));

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }


        ControllerUtils.setHeaderResponse(returnVal.getHeader(), response, HttpStatus.OK);

        return (returnVal);

    }

    // Example: http://localhost:8282/gobii-dev/brapi/v1/contact-search?email=foo&lastName=bar&firstName=snot
    // all parameters must be present, but they don't all neeed a value
    @RequestMapping(value = "/contact-search",
            params = {"email", "lastName", "firstName"},
            method = RequestMethod.GET)
    @ResponseBody
    public PayloadEnvelope<ContactDTO> getContactsBySearch(@RequestParam("email") String email,
                                                           @RequestParam("lastName") String lastName,
                                                           @RequestParam("firstName") String firstName,
                                                           HttpServletRequest request,
                                                           HttpServletResponse response) {

        PayloadEnvelope<ContactDTO> returnVal = new PayloadEnvelope<>();
        try {

            if (false == LineUtils.isNullOrEmpty(email)) {
                returnVal = contactService.getContactByEmail(email);
                //returnVal = contactService.getContactById(1);
            }

        } catch (Exception e) {
            returnVal.getHeader().getStatus().addException(e);
        }

        ControllerUtils.setHeaderResponse(returnVal.getHeader(),
                response,
                HttpStatus.OK);

        return (returnVal);

    }


}// LoadController
