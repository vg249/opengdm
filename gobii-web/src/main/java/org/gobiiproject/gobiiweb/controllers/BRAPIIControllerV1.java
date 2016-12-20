// ************************************************************************
// (c) 2016 GOBii Projects
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiiweb.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.gobiiproject.gobiibrapi.calls.germplasm.BrapiResponseGermplasmSearch;
import org.gobiiproject.gobiibrapi.calls.germplasm.BrapiResponseMapGermplasmSearch;
import org.gobiiproject.gobiibrapi.calls.studies.observationvariables.BrapiResponseMapObservationVariables;
import org.gobiiproject.gobiibrapi.calls.studies.observationvariables.BrapiResponseObservationVariablesMaster;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiRequestStudiesSearch;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiResponseMapStudiesSearch;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiResponseStudiesSearchItem;
import org.gobiiproject.gobiibrapi.core.common.BrapiRequestReader;
import org.gobiiproject.gobiibrapi.core.derived.BrapiListResult;
import org.gobiiproject.gobiibrapi.core.derived.BrapiResponseEnvelopeList;
import org.gobiiproject.gobiibrapi.core.derived.BrapiResponseEnvelopeMaster;
import org.gobiiproject.gobiibrapi.core.json.BrapiResponseWriterJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

            BrapiResponseWriterJson<BrapiResponseCallsItem, ObjectUtils.Null> brapiResponseWriterJson =
                    new BrapiResponseWriterJson<>(BrapiResponseCallsItem.class, ObjectUtils.Null.class);

            returnVal = brapiResponseWriterJson.makeBrapiResponse(brapiResponseCallsItems,
                    null,
                    null,
                    null,
                    null);


        } catch (Exception e) {

            returnVal = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();
        }

        return returnVal;
    }

    // *********************************************
    // *************************** STUDIES_SEARCH (DETAILS ONLY)
    // *************************** LIST ITEMS ONLY
    // *********************************************
    @RequestMapping(value = "/studies-search",
            method = RequestMethod.POST,
            produces = "application/json")
    @ResponseBody
    public String getStudies(@RequestBody String studiesRequestBody,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception {

        String returnVal;

        BrapiResponseEnvelopeList<ObjectUtils.Null, BrapiResponseStudiesSearchItem> brapiResponseEnvelopeList =
                new BrapiResponseEnvelopeList<>(ObjectUtils.Null.class, BrapiResponseStudiesSearchItem.class);

        try {

            BrapiRequestReader<BrapiRequestStudiesSearch> brapiRequestReader = new BrapiRequestReader<>(BrapiRequestStudiesSearch.class);
            BrapiRequestStudiesSearch brapiRequestStudiesSearch = brapiRequestReader.makeRequestObj(studiesRequestBody);

            BrapiListResult<BrapiResponseStudiesSearchItem> brapiListResult = (new BrapiResponseMapStudiesSearch()).getBrapiResponseStudySearchItems(brapiRequestStudiesSearch);

            brapiResponseEnvelopeList.setData(brapiListResult);
            //returnVal = (new ObjectMapper()).writeValueAsString(brapiListResult);
////            BrapiResponseMapStudiesSearch brapiResponseMapStudiesSearch = new BrapiResponseMapStudiesSearch();
////            List<BrapiResponseStudiesSearchItem> searchItems = brapiResponseMapStudiesSearch.getBrapiJsonResponseStudySearchItems(brapiRequestStudiesSearch);
//
//
//            BrapiResponseWriterJson<BrapiResponseStudiesSearchItem, ObjectUtils.Null> brapiResponseWriterJson =
//                    new BrapiResponseWriterJson<>(BrapiResponseStudiesSearchItem.class, ObjectUtils.Null.class);
//
//            returnVal = brapiResponseWriterJson.makeBrapiResponse(searchItems,
//                    null,
//                    new Pagination(searchItems.size(), 1, 1, 0),
//                    null,
//                    null);


        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelopeList.getBrapiMetaData().addStatusMessage("exception", message);
        }

        returnVal = (new ObjectMapper()).writeValueAsString(brapiResponseEnvelopeList);
        return returnVal;
    }

    // *********************************************
    // *************************** Germplasm search by germplasmDbId [GET]
    // **************************** MASTER ONLY
    // *********************************************
    @RequestMapping(value = "/studies/{studyDbId}/germplasm",
            method = RequestMethod.GET,
//            params = {"pageSize", "page"},
            produces = "application/json")
    @ResponseBody
    public String getGermplasm(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable Integer studyDbId
//            ,
//                               @RequestParam(value = "pageSize",required = false) Integer pageSize,
//                               @RequestParam(value = "page", required = false) Integer page
    ) throws Exception {


        BrapiResponseEnvelopeMaster<BrapiResponseGermplasmSearch> responseEnvelope
                = new BrapiResponseEnvelopeMaster<>(BrapiResponseGermplasmSearch.class);

        String returnVal;

        try {

            BrapiResponseMapGermplasmSearch brapiResponseMapGermplasmSearch = new BrapiResponseMapGermplasmSearch();

            // extends BrapiMetaData, no list items
            BrapiResponseGermplasmSearch brapiResponseGermplasmSearch = brapiResponseMapGermplasmSearch.getGermplasmByDbid(studyDbId);

            responseEnvelope.setResult(brapiResponseGermplasmSearch);


        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            responseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);

        }

        returnVal = (new ObjectMapper()).writeValueAsString(responseEnvelope);
        return returnVal;
    }

    // *********************************************
    // *************************** Study obsefvation variables (GET)
    // **************************** MASTER AND DETAIL
    // *********************************************
    @RequestMapping(value = "/studies/{studyDbId}/observationVariables",
            method = RequestMethod.GET,
            params = {"pageSize", "page"},
            produces = "application/json")
    @ResponseBody
    public String getObservationVariables(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @PathVariable Integer studyDbId) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String returnVal = null;

        try {

            BrapiResponseMapObservationVariables brapiResponseMapObservationVariables = new BrapiResponseMapObservationVariables();

            // extends BrapiMetaData, no list items
            BrapiResponseObservationVariablesMaster brapiResponseObservationVariablesMaster = brapiResponseMapObservationVariables.gerObservationVariablesByStudyId(studyDbId);

            returnVal = objectMapper.writeValueAsString(brapiResponseObservationVariablesMaster);

        } catch (Exception e) {

//            BrapiListResult<ObjectUtils.Null> emptyResponse = new BrapiListResult<>(ObjectUtils.Null.class);
//            Map<String, String> statusMap = new HashMap<>();
//            statusMap.put("exception", e.getMessage());
//            emptyResponse.getStatus().add(statusMap);
//            returnVal = objectMapper.writeValueAsString(emptyResponse);

        }

        return returnVal;
    }


}// BRAPIController
