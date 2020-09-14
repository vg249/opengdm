package org.gobiiproject.gobiiweb.controllers.brapi.v2;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.gobiiproject.gobiidomain.services.brapi.SearchService;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.dto.brapi.envelope.BrApiMasterPayload;
import org.gobiiproject.gobiimodel.dto.noaudit.SearchResultDTO;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiiweb.CropRequestAnalyzer;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Scope(value = "request")
@Controller
@RequestMapping("/brapi/v2/search")
@CrossOrigin
@Api
@Slf4j
public class SearchController {

   protected final SearchService searchService;

   public SearchController(final SearchService searchService) {
       this.searchService = searchService;
   }

   public ResponseEntity<BrApiMasterPayload<SearchResultDTO>>
   submitSearchQuery(Object searchQueryObject, HttpServletRequest request) throws GobiiException {

       String cropType;
       try {
           cropType = CropRequestAnalyzer.getGobiiCropType(request);
       }
       catch (Exception e) {
           throw new GobiiException(
               GobiiStatusLevel.ERROR,
               GobiiValidationStatusType.NONE,
               "Internal Server Error " + e.getMessage()
           );
       }

       if (searchQueryObject != null) {

           SearchResultDTO searchResultDTO = searchService.createSearchQueryResource(
               cropType, searchQueryObject);

           BrApiMasterPayload<SearchResultDTO> payload =
               new BrApiMasterPayload<>(searchResultDTO);

           return  ResponseEntity.status(HttpStatus.CREATED).body(payload);

       }
       else {
           throw new GobiiException(
               GobiiStatusLevel.ERROR,
               GobiiValidationStatusType.BAD_REQUEST,
               "Missing Request body"
           );
       }

   }









}
