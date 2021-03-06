// ************************************************************************
// (c) 2016 GOBii Projects
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobiiweb.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.swagger.annotations.*;
import org.gobiiproject.gobidomain.services.PingService;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiibrapi.calls.calls.BrapiResponseCalls;
import org.gobiiproject.gobiibrapi.calls.calls.BrapiResponseMapCalls;
import org.gobiiproject.gobiibrapi.calls.germplasm.BrapiResponseGermplasmByDbId;
import org.gobiiproject.gobiibrapi.calls.germplasm.BrapiResponseMapGermplasmByDbId;
import org.gobiiproject.gobiibrapi.calls.login.BrapiRequestLogin;
import org.gobiiproject.gobiibrapi.calls.login.BrapiResponseLogin;
import org.gobiiproject.gobiibrapi.calls.login.BrapiResponseMapLogin;
import org.gobiiproject.gobiibrapi.calls.markerprofiles.allelematrices.BrapiResponseAlleleMatrices;
import org.gobiiproject.gobiibrapi.calls.markerprofiles.allelematrices.BrapiResponseMapAlleleMatrices;
import org.gobiiproject.gobiibrapi.calls.markerprofiles.allelematrixsearch.BrapiResponseMapAlleleMatrixSearch;
import org.gobiiproject.gobiibrapi.calls.markerprofiles.markerprofiles.BrapiResponseMapMarkerProfiles;
import org.gobiiproject.gobiibrapi.calls.markerprofiles.markerprofiles.BrapiResponseMarkerProfilesMaster;
import org.gobiiproject.gobiibrapi.calls.studies.observationvariables.BrapiResponseMapObservationVariables;
import org.gobiiproject.gobiibrapi.calls.studies.observationvariables.BrapiResponseObservationVariablesMaster;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiRequestStudiesSearch;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiResponseMapStudiesSearch;
import org.gobiiproject.gobiibrapi.calls.studies.search.BrapiResponseStudiesSearch;

import org.gobiiproject.gobiibrapi.core.common.BrapiPagination;
import org.gobiiproject.gobiibrapi.core.common.BrapiRequestReader;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelope;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelopeMaster;
import org.gobiiproject.gobiibrapi.core.responsemodel.BrapiResponseEnvelopeMasterDetail;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiiweb.CropRequestAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


/**
 * This controller is only for BRAPI v1. compliant calls. It consumes the gobii-brapi module.
 * <p>
 * BRAPI responses all contain a "metadata" and a "result" key. The "result" key's value can take
 * three forms:
 * 1) A "data" property that is an array of items of the same type (e.g., /studies-search)
 * 2) A set of arbitrary properties (i.e., a specific type in Java) (e.g., /germplasm/{id})
 * 3) A set of arbitrary properties and a "data" property (e.g., /studies/{studyDbId}/observationVariables)
 * <p>
 * Type 3) is for a master-detail scenario: you have a master record that is related to one or more detail
 * records. Type 2) is just the master. Type 1) is just the detail records.
 * <p>
 * The classes that support this API are as follows:
 * <p>
 * BrapiResponseEnvelope: This is the base class for all response envelopes: it is responsible for the metadata
 * key of the response
 * <p>
 * BrapiResponseEnvelopeMasterDetail and BrapiResponseEnvelopeMaster derive from BrapiResponseEnvelope.
 * They are responsible for the result key of the response.
 * <p>
 * BrapiResponseEnvelopeMasterDetail: This class is used for types 1) and 3). It is
 * type-parmaeterized for the pojo that will be the value of the result key; the pojo must extend  BrapiResponseDataList;
 * this way, the pojo will always have a setData() method for specifying the list content of the data key.
 * <p>
 * In the case of (1):
 * The result pojo will be a class with no properties that derives from BrapiResponseDataList. The maping
 * class will use the pojo's setData() method to specify the list that will constitutes the data key
 * of the result key.
 * <p>
 * In the case of (3):
 * Thee result pojo will have its own properties; the pojo's own properties will constitute the values
 * of the result key; the pojos setData() method will be used to specify the list that constitutes the
 * data key;
 * <p>
 * BrapiResponseEnvelopeMaster: This class is used for type 2). It is type-parameterized
 * for an arbitrary pojo. Because type 2 responses do not have a data key, the pojo does
 * _not_ extend BrapiResponseDataList. Its properties will be the values of the response's result key.
 * <p>
 * The calls namespace of gobii-brapi is organized as the brapi API is organized. In the descriptions
 * below, <CallName> refers to the BRAPI call name.
 * Each call contains several sorts of classes:
 * ---- POJOs named BrapiResponse<CallName>: these are the arbitrary pojos that
 * type-parameterize BrapiResponseElvelopeMasterDetail and BrapiResponseElvelopeMaster
 * ---- POJOs named BrapiRequest<CallName>: these are POST/PUT bodies for which the
 * relevant methods in here the controller have @RequestBody parameters (e.g., BrapiRequestStudiesSearch)
 * ---- POJOs named BrapiResponseMap<CallName>: Right now these clases create dummy responses; the real
 * implementations of these classes will consume classes from the gobii-domain project (i.e., the Service
 * classes): they will get data from gobii in terms of gobii DTOs and convert the DTOs in to the
 * BRAPI POJOs
 * <p>
 * The BrapiController does the following:
 * 0.   If there is a post body, deserializes it to the appropriate post pojo;
 * i.   Instantiates a BrapiResponseEnvelopeMaster or BrapiResponseEnvelopeMasterDetail;
 * ii.  Uses the map classes for each call to get the pojo that the call will use for its payload;
 * iii. Assigns the pojo to the respective respone envelope;
 * iv.  Serialies the content of the response envelope;
 * v.   Sets the reponse of the method to the serialized content.
 * <p>
 * Note that this controller receives and sends plain String json data. This approach is different
 * from the gobii api. The BrapiResponseEnvelopeList and BrapiResponseEnvelopeMaster are serialzied to
 * json and sent over the wire in this way rather than letting the Jackson embedded through Spring do
 * the job automatically. This approach is more traditionally the web service way of doing things.
 */
@Scope(value = "request")
@Controller
@Api()
@RequestMapping(GobiiControllerType.SERVICE_PATH_BRAPI)
public class BRAPIIControllerV1 {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BRAPIIControllerV1.class);

    @Autowired
    private PingService pingService = null;


    @Autowired
    private BrapiResponseMapStudiesSearch brapiResponseMapStudiesSearch = null;


    @Autowired
    private BrapiResponseMapAlleleMatrixSearch brapiResponseMapAlleleMatrixSearch = null;

    @Autowired
    private BrapiResponseMapMarkerProfiles brapiResponseMapMarkerProfiles = null;


    @Autowired
    private BrapiResponseMapAlleleMatrices brapiResponseMapAlleleMatrices = null;

    private ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);


    // *********************************************
    // *************************** CALLS
    // *********************************************
    @RequestMapping(value = "/calls",
            method = RequestMethod.GET,
            produces = "application/json")
    @ApiOperation(
            value = "List all calls.",
            notes = "List all calls",
            tags = {"BrAPI"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Calls"),
                            @ExtensionProperty(
                                    name="tag-description",
                                    value= "BrAPI is standards api calls. Below section list all" +
                                            " the BrAPI calls supported by GDM."
                            )
                    })
            }
    )
    @ResponseBody
    public String getCalls(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String returnVal;

        BrapiResponseEnvelopeMasterDetail<BrapiResponseCalls> brapiResponseEnvelopeMasterDetail =
                new BrapiResponseEnvelopeMasterDetail<>();
        try {

            BrapiResponseMapCalls brapiResponseMapCalls = new BrapiResponseMapCalls(request);

            BrapiResponseCalls brapiResponseCalls = brapiResponseMapCalls.getBrapiResponseCalls();
            brapiResponseEnvelopeMasterDetail.setResult(brapiResponseCalls);

        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelopeMasterDetail.getBrapiMetaData().addStatusMessage("exception", message);

        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelopeMasterDetail.getBrapiMetaData().addStatusMessage("exception", message);
        }

        returnVal = objectMapper.writeValueAsString(brapiResponseEnvelopeMasterDetail);

        return returnVal;
    }


    // *********************************************
    // *************************** LOGIN (MASTER ONLY)
    // *********************************************
    @RequestMapping(value = "/token",
            method = RequestMethod.POST,
            produces = "application/json")
    @ApiOperation(
            value = "List all tokens.",
            notes = "List all tokens",
            tags = {"BrAPI"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Tokens"),
                    })
            }
    )
    @ResponseBody
    public String postLogin(@RequestBody String loginRequestBody,
                            HttpServletRequest request,
                            HttpServletResponse response) throws Exception {

        String returnVal;


        BrapiResponseLogin brapiResponseLogin = null;
        try {

            BrapiRequestReader<BrapiRequestLogin> brapiRequestReader = new BrapiRequestReader<>(BrapiRequestLogin.class);
            BrapiRequestLogin brapiRequestLogin = brapiRequestReader.makeRequestObj(loginRequestBody);

            BrapiResponseMapLogin brapiResponseMapLogin = new BrapiResponseMapLogin();
            brapiResponseLogin = brapiResponseMapLogin.getLoginInfo(brapiRequestLogin, response);


            brapiResponseLogin.getBrapiMetaData().setPagination(new BrapiPagination(
                    1,
                    1,
                    1,
                    0
            ));

        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseLogin.getBrapiMetaData().addStatusMessage("exception", message);

        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseLogin.getBrapiMetaData().addStatusMessage("exception", message);
        }

        returnVal = objectMapper.writeValueAsString(brapiResponseLogin);

        return returnVal;
    }


    // *********************************************
    // *************************** STUDIES_SEARCH (DETAILS ONLY)
    // *************************** LIST ITEMS ONLY
    // *********************************************
    @RequestMapping(value = "/studies-search",
            method = RequestMethod.POST,
            produces = "application/json")
    @ApiOperation(
            value = "Search the studies.",
            notes = "Search studies.",
            tags = {"BrAPI"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="StudiesSearch"),
                    })
            }
    )
    @ResponseBody
    public String getStudies(@RequestBody String studiesRequestBody,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception {

        String returnVal;

        BrapiResponseEnvelopeMasterDetail<BrapiResponseStudiesSearch> BrapiResponseEnvelopeMasterDetail =
                new BrapiResponseEnvelopeMasterDetail<>();

        try {

            BrapiRequestReader<BrapiRequestStudiesSearch> brapiRequestReader = new BrapiRequestReader<>(BrapiRequestStudiesSearch.class);
            BrapiRequestStudiesSearch brapiRequestStudiesSearch = brapiRequestReader.makeRequestObj(studiesRequestBody);

            Integer requestedPageSize = brapiRequestStudiesSearch.getPageSize();

            BrapiResponseStudiesSearch brapiResponseStudySearch = brapiResponseMapStudiesSearch.getBrapiResponseStudySearch(brapiRequestStudiesSearch);

            BrapiResponseEnvelopeMasterDetail.setResult(brapiResponseStudySearch);

            Integer numberOfHits = BrapiResponseEnvelopeMasterDetail.getResult().getData().size();

            Integer reportedPageSize;
            Integer totalPages;
            if (requestedPageSize > numberOfHits) {
                reportedPageSize = numberOfHits;
                totalPages = 1;
            } else {
                reportedPageSize = requestedPageSize;
                totalPages = numberOfHits / reportedPageSize; // get the whole part of the result
                if (numberOfHits % reportedPageSize > 0) {   // if there's a remainder, there's an additional page
                    totalPages += 1;
                }
            }


            BrapiResponseEnvelopeMasterDetail.getBrapiMetaData().setPagination(new BrapiPagination(
                    numberOfHits,
                    reportedPageSize,
                    totalPages,
                    0
            ));

        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            BrapiResponseEnvelopeMasterDetail.getBrapiMetaData().addStatusMessage("exception", message);

        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            BrapiResponseEnvelopeMasterDetail.getBrapiMetaData().addStatusMessage("exception", message);
        }

        returnVal = objectMapper.writeValueAsString(BrapiResponseEnvelopeMasterDetail);

        return returnVal;
    }

    // *********************************************
    // *************************** Germplasm details [GET]
    // **************************** MASTER ONLY
    // *********************************************
    @RequestMapping(value = "/germplasm/{studyDbId}",
            method = RequestMethod.GET,
//            params = {"pageSize", "page"},
            produces = "application/json")
    @ApiOperation(
            value = "Get germplasm by study db id.",
            notes = "Get germplasm by study db id.",
            tags = {"BrAPI"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Germplasm : studyDbId"),
                    })
            }
    )
    @ResponseBody
    public String getGermplasmByDbId(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @ApiParam(value = "Study DB Id", required = true)
                                     @PathVariable Integer studyDbId
//            ,
//                               @RequestParam(value = "pageSize",required = false) Integer pageSize,
//                               @RequestParam(value = "page", required = false) Integer page
    ) throws Exception {


        BrapiResponseEnvelopeMaster<BrapiResponseGermplasmByDbId> responseEnvelope
                = new BrapiResponseEnvelopeMaster<>();

        String returnVal;

        try {

            BrapiResponseMapGermplasmByDbId brapiResponseMapGermplasmByDbId = new BrapiResponseMapGermplasmByDbId();

            // extends BrapiMetaData, no list items
            BrapiResponseGermplasmByDbId brapiResponseGermplasmByDbId = brapiResponseMapGermplasmByDbId.getGermplasmByDbid(studyDbId);

            responseEnvelope.setResult(brapiResponseGermplasmByDbId);


        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            responseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);

        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            responseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);

        }

        returnVal = objectMapper.writeValueAsString(responseEnvelope);

        return returnVal;

    }

    // *********************************************
    // *************************** Study obsefvation variables (GET)
    // **************************** MASTER AND DETAIL
    // *********************************************
    @RequestMapping(value = "/studies/{studyDbId}/observationVariables",
            method = RequestMethod.GET,
//            params = {"pageSize", "page"},
            produces = "application/json")
    @ApiOperation(
            value = "List all observation variables for given study db id",
            notes = "List all observation variables for given study db id",
            tags = {"BrAPI"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Studies.observationVariables"),
                    })
            }
    )
    @ResponseBody
    public String getObservationVariables(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @ApiParam(value = "Study DB Id", required = true)
                                          @PathVariable Integer studyDbId) throws Exception {

        BrapiResponseEnvelopeMasterDetail<BrapiResponseObservationVariablesMaster> responseEnvelope
                = new BrapiResponseEnvelopeMasterDetail<>();

        String returnVal;

        try {

            BrapiResponseMapObservationVariables brapiResponseMapObservationVariables = new BrapiResponseMapObservationVariables();

            BrapiResponseObservationVariablesMaster brapiResponseObservationVariablesMaster = brapiResponseMapObservationVariables.gerObservationVariablesByStudyId(studyDbId);

            responseEnvelope.setResult(brapiResponseObservationVariablesMaster);

        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            responseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);

        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            responseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);

        }

        returnVal = objectMapper.writeValueAsString(responseEnvelope);

        return returnVal;
    }


    // *********************************************
    // *************************** ALLELE MATRICES
    // *********************************************
    @RequestMapping(value = "/allelematrices",
            method = RequestMethod.GET,
            produces = "application/json")
    @ApiOperation(
            value = "Get Allele Matrices",
            notes = "Get allele matrices by given study db id",
            tags = {"BrAPI"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="AlleleMatrices"),
                    })
            }
    )
    @ResponseBody
    public String getAlleleMatrices(@ApiParam(value = "Study DB Id", required = false)
                                    @RequestParam("studyDbId") Optional<String> studyDbIdd,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {

        String returnVal;

        BrapiResponseEnvelopeMasterDetail<BrapiResponseAlleleMatrices> BrapiResponseEnvelopeMasterDetail =
                new BrapiResponseEnvelopeMasterDetail<>();

        try {

            BrapiResponseAlleleMatrices brapiResponseAlleleMatrices;
            if (studyDbIdd.isPresent()) {
                Integer studyDbIdAsInteger = Integer.parseInt(studyDbIdd.get());
                brapiResponseAlleleMatrices = brapiResponseMapAlleleMatrices.getBrapiResponseAlleleMatricesItemsByStudyDbId(studyDbIdAsInteger);
            } else {
                brapiResponseAlleleMatrices = brapiResponseMapAlleleMatrices.getBrapiResponseAlleleMatrices();
            }

            BrapiResponseEnvelopeMasterDetail.setResult(brapiResponseAlleleMatrices);

        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            BrapiResponseEnvelopeMasterDetail.getBrapiMetaData().addStatusMessage("exception", message);

        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            BrapiResponseEnvelopeMasterDetail.getBrapiMetaData().addStatusMessage("exception", message);
        }

        returnVal = objectMapper.writeValueAsString(BrapiResponseEnvelopeMasterDetail);

        return returnVal;
    }

    @RequestMapping(value = "/allelematrix-search",
            method = {RequestMethod.GET},
            produces = "application/json")
    @ApiOperation(
            value = "Search Allele Matrix",
            notes = "Search allele matrix using marker profiles",
            tags = {"BrAPI"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="AlleleMatrixSearch"),
                    })
            }
    )
    @ResponseBody
    public String getAlleleMatrix(@ApiParam(value = "Matrix DB Id", required = false)
                                  @RequestParam("matrixDbId") Optional<String> matrixDbId,
                                  @ApiParam(value = "Marker Profile Id", required = false)
                                  @RequestParam("markerprofileDbId") Optional<String> markerprofileDbId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws Exception {
        return this.alleleMatrix(matrixDbId, markerprofileDbId, request, response);

    }

    @RequestMapping(value = "/allelematrix-search",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ApiOperation(
            value = "Search Allele Matrix",
            notes = "Search allele matrix using marker profiles",
            tags = {"BrAPI"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="AlleleMatrixSearch"),
                    })
            }
    )
    @ResponseBody
    public String postAlleleMatrix(@ApiParam(value = "Matrix DB Id", required = false)
                                  @RequestParam("matrixDbId") Optional<String> matrixDbId,
                                  @ApiParam(value = "Marker Profile Id", required = false)
                                  @RequestParam("markerprofileDbId") Optional<String> markerprofileDbId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws Exception {
        return this.alleleMatrix(matrixDbId, markerprofileDbId, request, response);

    }

    public String alleleMatrix(Optional<String> matrixDbId,
                               Optional<String> markerprofileDbId,
                               HttpServletRequest request,
                               HttpServletResponse response) throws Exception {

        String returnVal = null;

        BrapiResponseEnvelope brapiResponseEnvelope = new BrapiResponseEnvelope();
        try {

            String cropType = CropRequestAnalyzer.getGobiiCropType(request);
            if (matrixDbId.isPresent() == markerprofileDbId.isPresent()) {
                String message = "Incorrect request format. At least one of matrixDbId or markerprofileDbId should be specified.";
                brapiResponseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);
            } else if (matrixDbId.isPresent()) {
                List<String> matrixDbIdList = Arrays.asList(matrixDbId.get().split(","));
                if (matrixDbIdList.size() > 1) {
                    String message = "Incorrect request format. Only one matrixDbId is supported at the moment.";
                    brapiResponseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);
                    return objectMapper.writeValueAsString(brapiResponseEnvelope);
                }
                brapiResponseEnvelope.setBrapiMetaData(brapiResponseMapAlleleMatrixSearch.searchByMatrixDbId(cropType, matrixDbIdList.get(0)));
            } else {
                List<String> externalCodes = Arrays.asList(markerprofileDbId.get().split(","));
                brapiResponseEnvelope.setBrapiMetaData(brapiResponseMapAlleleMatrixSearch.searchByExternalCode(cropType, externalCodes));
            }
        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);

        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);
        }

        returnVal = objectMapper.writeValueAsString(brapiResponseEnvelope);

        return returnVal;
    }


    @RequestMapping(value = "/allelematrix-search/status/{jobId}",
            method = RequestMethod.GET,
            produces = "application/json")
    @ApiOperation(
            value = "Get Allele Matrix Job status",
            notes = "Get allele matrix Job status",
            tags = {"BrAPI"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="AlleleMatrix.status : jobId"),
                    })
            }
    )
    @ResponseBody
    public String getAlleleMatrixStatus(@ApiParam(value = "Job Id", required = true)
                                        @PathVariable("jobId") String jobId,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {

        String returnVal = null;

        BrapiResponseEnvelope brapiResponseEnvelope = new BrapiResponseEnvelope();
        try {

            String cropType = CropRequestAnalyzer.getGobiiCropType(request);
            brapiResponseEnvelope.setBrapiMetaData(brapiResponseMapAlleleMatrixSearch.getStatus(cropType,
                    jobId,
                    request));


        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);

        } catch (Exception e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);
        }

        returnVal = objectMapper.writeValueAsString(brapiResponseEnvelope);

        return returnVal;
    }


    /***
     * Returns a stream for the at at the path specified by the query parameter. This method is not
     * defined by the BRAPI spec, nor should it be: the spec only stipulates that entries in the files
     * section of the metadata object be accessible to the client.
     *
     * @param fqpn: fully qualified path of file to download
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/files",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperation(
            value = "List all files",
            notes = "List all the files in a given path.",
            tags = {"BrAPI"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="Files"),
                    })
            }
    )
    @ResponseBody
    public void getFile(@ApiParam(value = "Fully qualified path name", required = true)
                        @RequestParam("fqpn") String fqpn,
                        HttpServletRequest request,
                        HttpServletResponse response) throws Exception {

        try {

            response.setContentType("application/text");
            InputStream inputStream = new FileInputStream(fqpn);
            // copy it to response's OutputStream
            org.apache.commons.io.IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();

        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file " + fqpn + "to output stream: " + ex.getMessage());
        }
    }


    @RequestMapping(value = "/markerprofiles",
            method = {RequestMethod.GET},
            produces = "application/json")
    @ApiOperation(
            value = "List all Marker Profiles",
            notes = "List all Marker Profiles in given germplasm db..",
            tags = {"BrAPI"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="MarkerProfiles"),
                    })
            }
    )
    @ResponseBody
    public String getMarkerProfile(@ApiParam(value = "Germplasm DB Id", required = true)
                                   @RequestParam("germplasmDbId") String germplasmDbId,
                                   HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        return this.markerProfile(germplasmDbId, request, response);
    }

    @RequestMapping(value = "/markerprofiles",
            method = {RequestMethod.POST},
            produces = "application/json")
    @ApiOperation(
            value = "List all Marker Profiles",
            notes = "List all Marker Profiles in given germplasm db..",
            tags = {"BrAPI"},
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name="summary", value="MarkerProfiles"),
                    })
            }
    )
    @ResponseBody
    public String postMarkerProfile(@ApiParam(value = "Germplasm DB Id", required = true)
                                    @RequestParam("germplasmDbId") String germplasmDbId,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
        return this.markerProfile(germplasmDbId, request, response);
    }

    public String markerProfile(String germplasmDbId,
                                HttpServletRequest request,
                                HttpServletResponse response) throws Exception {

        BrapiResponseEnvelopeMasterDetail<BrapiResponseMarkerProfilesMaster> brapiResponseEnvelope
                = new BrapiResponseEnvelopeMasterDetail<>();

        String returnVal;
        try {

            String cropType = CropRequestAnalyzer.getGobiiCropType(request);
            if (!LineUtils.isNullOrEmpty(germplasmDbId)) {

                brapiResponseEnvelope.setResult(brapiResponseMapMarkerProfiles.getBrapiResponseMarkerProfilesByGermplasmId(germplasmDbId));

            } else {
                String message = "Incorrect request format: germplasmDbId must be specified";
                brapiResponseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);
            }
        } catch (GobiiException e) {

            String message = e.getMessage() + ": " + e.getCause() + ": " + e.getStackTrace().toString();

            brapiResponseEnvelope.getBrapiMetaData().addStatusMessage("exception", message);

        }

        returnVal = objectMapper.writeValueAsString(brapiResponseEnvelope);

        return returnVal;
    }

}// BRAPIController
