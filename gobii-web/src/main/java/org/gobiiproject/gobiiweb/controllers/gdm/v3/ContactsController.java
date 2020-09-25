package org.gobiiproject.gobiiweb.controllers.gdm.v3;

import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.gobiiproject.gobiiapimodel.types.GobiiControllerType;
import org.gobiiproject.gobiidomain.services.gdmv3.ContactService;
import org.gobiiproject.gobiimodel.dto.brapi.envelope.BrApiMasterListPayload;
import org.gobiiproject.gobiimodel.dto.gdmv3.ContactDTO;
import org.gobiiproject.gobiimodel.dto.system.PagedResult;
import org.gobiiproject.gobiiweb.CropRequestAnalyzer;

import io.swagger.annotations.Api;
//import lombok.extern.slf4j.Slf4j;

@Scope(value = "request")
@RestController
@RequestMapping(GobiiControllerType.SERVICE_PATH_GOBII_V3)
@Api()
@CrossOrigin
//@Slf4j
public class ContactsController {
    
    private ContactService contactService;

    public ContactsController(
        ContactService contactService
    ) {
        this.contactService = contactService;
    }


    /**
     * List Contacts
     * @return
     */
    @GetMapping("/contacts")
    @ResponseBody
    public ResponseEntity<BrApiMasterListPayload<ContactDTO>> getContacts(
        @RequestParam(required=false, defaultValue = "0") Integer page,
        @RequestParam(required=false, defaultValue = "1000") Integer pageSize,
        //@RequestParam(required=false) Integer organizationId
        @RequestParam(required=false, defaultValue = "pi") String role
    ) throws Exception {
        Integer pageSizeToUse = ControllerUtils.getPageSize(pageSize);
        String cropType = CropRequestAnalyzer.getGobiiCropType();
        PagedResult<ContactDTO> pagedResult = contactService.getUsers(cropType, role.toLowerCase(), page, pageSizeToUse);
        BrApiMasterListPayload<ContactDTO> payload = ControllerUtils.getMasterListPayload(pagedResult);
        return ResponseEntity.ok(payload);
    }
}
