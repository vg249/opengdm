// ************************************************************************
// (c) 2016 GOBii Projects
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiiweb.controllers;

import org.apache.commons.lang.ObjectUtils;
import org.gobiiproject.gobidomain.services.AnalysisService;
import org.gobiiproject.gobidomain.services.ConfigSettingsService;
import org.gobiiproject.gobidomain.services.ContactService;
import org.gobiiproject.gobidomain.services.CvService;
import org.gobiiproject.gobidomain.services.DataSetService;
import org.gobiiproject.gobidomain.services.DisplayService;
import org.gobiiproject.gobidomain.services.ExperimentService;
import org.gobiiproject.gobidomain.services.ExtractorInstructionFilesService;
import org.gobiiproject.gobidomain.services.LoaderFilesService;
import org.gobiiproject.gobidomain.services.LoaderInstructionFilesService;
import org.gobiiproject.gobidomain.services.ManifestService;
import org.gobiiproject.gobidomain.services.MapsetService;
import org.gobiiproject.gobidomain.services.MarkerGroupService;
import org.gobiiproject.gobidomain.services.MarkerService;
import org.gobiiproject.gobidomain.services.NameIdListService;
import org.gobiiproject.gobidomain.services.OrganizationService;
import org.gobiiproject.gobidomain.services.PingService;
import org.gobiiproject.gobidomain.services.PlatformService;
import org.gobiiproject.gobidomain.services.ProjectService;
import org.gobiiproject.gobidomain.services.ReferenceService;
import org.gobiiproject.gobiibrapi.calls.calls.BrapiResponseCallsItem;
import org.gobiiproject.gobiibrapi.calls.calls.BrapiResponseMapCalls;
import org.gobiiproject.gobiibrapi.core.BrapiResponseWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * Created by MrPhil on 7/6/2015.
 */
@Scope(value = "request")
@Controller
@RequestMapping("/brapi/v1")
public class BRAPIIControllerV1 {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BRAPIIControllerV1.class);

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
    private ExtractorInstructionFilesService extractorInstructionFilesService = null;

    @Autowired
    private LoaderFilesService loaderFilesService = null;

    @Autowired
    private DisplayService displayService = null;

    @Autowired
    private CvService cvService = null;

    @Autowired
    private MarkerService markerService = null;

    @Autowired
    private DataSetService dataSetService = null;

    @Autowired
    private PlatformService platformService = null;

    @Autowired
    private MapsetService mapsetService = null;

    @Autowired
    private ConfigSettingsService configSettingsService;


    // *********************************************
    // *************************** CALLS
    // *********************************************
    @RequestMapping(value = "/calls",
            method = RequestMethod.GET,
            produces = "application/json")
    @ResponseBody
    public String getCalls(
            HttpServletRequest request,
            HttpServletResponse response) {

        String returnVal;

        try {

            BrapiResponseMapCalls brapiResponseMapCalls = new BrapiResponseMapCalls();
            List<BrapiResponseCallsItem> brapiResponseCallsItems = brapiResponseMapCalls
                    .getBrapiResponseCallsItems();

            BrapiResponseWriter<BrapiResponseCallsItem, ObjectUtils.Null> brapiResponseWriter =
                    new BrapiResponseWriter<>(BrapiResponseCallsItem.class, ObjectUtils.Null.class);

            returnVal = brapiResponseWriter.makeBrapiResponse(brapiResponseCallsItems,
                    null,
                    null,
                    null,
                    null);


        } catch (Exception e) {

            returnVal = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();
        }

        return returnVal;
//        return ("{\n" +
//                "  \"metadata\" : {\n" +
//                "    \"pagination\" : {\n" +
//                "      \"pageSize\" : 0,\n" +
//                "      \"currentPage\" : 0,\n" +
//                "      \"totalCount\" : 0,\n" +
//                "      \"totalPages\" : 0\n" +
//                "    },\n" +
//                "    \"status\" : [ ],\n" +
//                "    \"datafiles\" : [ ]\n" +
//                "  },\n" +
//                "  \"result\" : {\n" +
//                "    \"data\" : [ {\n" +
//                "      \"call\" : \"calls\",\n" +
//                "      \"datatypes\" : [ \"json\" ],\n" +
//                "      \"methods\" : [ \"GET\" ]\n" +
//                "    }]\n" +
//                "  }\n" +
//                "}");

    }
}// BRAPIController
