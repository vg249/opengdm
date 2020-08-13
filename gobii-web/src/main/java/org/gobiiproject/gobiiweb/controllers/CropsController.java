package org.gobiiproject.gobiiweb.controllers;

import io.swagger.annotations.Api;
import org.gobiiproject.gobidomain.services.gdmv3.CropService;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.payload.sampletracking.BrApiMasterListPayload;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.dto.gdmv3.CropsDTO;
import org.gobiiproject.gobiimodel.dto.system.ConfigSettingsDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiiweb.automation.ControllerUtils;
import org.gobiiproject.gobiiweb.automation.PayloadWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Scope(value = "request")
@Controller
@RequestMapping("/")
@CrossOrigin
@Api
public class Crops {


    @Autowired
    private CropService cropService;

    @RequestMapping(value = "crops", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<CropsDTO>> getCrops(HttpServletRequest request, HttpServletResponse response) throws Exception{
        PagedResult<CropsDTO> cropsPagedResult = cropService.getCrops();
        return ResponseEntity.ok(BrApiMasterListPayload.createPayload(cropsPagedResult));
    }


}
