package org.gobiiproject.gobiiweb.controllers.gdm.v3;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.gobiiproject.gobiidomain.services.gdmv3.LoaderTemplateService;
import org.gobiiproject.gobiimodel.dto.brapi.envelope.BrApiMasterPayload;
import org.gobiiproject.gobiimodel.dto.gdmv3.LoaderTemplateDTO;
import org.gobiiproject.gobiiweb.security.CropAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.gobiiproject.gobiimodel.config.Roles.CURATOR;

@Scope(value = "request")
@Controller
@RequestMapping("/crops/{cropType}/gobii/v3/loader-templates/")
@CrossOrigin
@Api
@Slf4j
public class LoaderTemplateController {


    private final LoaderTemplateService loaderTemplateService;

    /**
     * Constructor.
     *
     * @param loaderTemplateService {@link LoaderTemplateService}
     */
    @Autowired
    public LoaderTemplateController(final LoaderTemplateService loaderTemplateService) {
        this.loaderTemplateService = loaderTemplateService;
    }

    /**
     * Adds the marker template to the GDM system
     * CropAuth annotation allows only CURATOR to access the endpoint
     * @param loaderTemplateDTO    marker file upload template to be saved
     * @return {@link LoaderTemplateDTO} created loader template
     * @throws Exception
     */
    @CropAuth(CURATOR)
    @PostMapping(value = "/marker", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<BrApiMasterPayload<LoaderTemplateDTO>> addMarkerTemplate(
        @RequestBody final LoaderTemplateDTO loaderTemplateDTO) throws Exception {

        LoaderTemplateDTO loaderTemplate = loaderTemplateService.addMarkerTemplate(
            loaderTemplateDTO);
        BrApiMasterPayload<LoaderTemplateDTO> payload = ControllerUtils.getMasterPayload(
            loaderTemplate);

        return ResponseEntity.created(null).body(payload);

    }

}
