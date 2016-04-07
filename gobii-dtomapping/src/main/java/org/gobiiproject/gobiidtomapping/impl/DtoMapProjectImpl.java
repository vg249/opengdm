package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.entity.pojos.Contact;
import org.gobiiproject.gobiidao.entity.access.ContactEntityDao;
import org.gobiiproject.gobiidtomapping.DtoMapProject;
import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Phil on 4/6/2016.
 */
public class DtoMapProjectImpl implements DtoMapProject {

    @Autowired
    ContactEntityDao contactEntityDao = null;

    public ProjectDTO getProject() {

        ProjectDTO returnVal = new ProjectDTO();

        List<String> primaryInvestigators = contactEntityDao.getContactsByRoleType("PI")
                .stream()
                .map(Contact::getLastname)
                .collect(Collectors.toList());

        returnVal.setPrincipleInvestigators(primaryInvestigators);

        return returnVal;
    }
}
