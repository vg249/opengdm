package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.entities.Contact;
import org.gobiiproject.gobiidao.entityaccess.ContactDao;
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
    ContactDao contactDao = null;

    public ProjectDTO getProject() {

        ProjectDTO returnVal = new ProjectDTO();

        List<String> primaryInvestigators = contactDao.getContactsByRoleType("PI")
                .stream()
                .map(Contact::getLastname)
                .collect(Collectors.toList());

        returnVal.setPrincipleInvestigators(primaryInvestigators);

        return returnVal;
    }
}
