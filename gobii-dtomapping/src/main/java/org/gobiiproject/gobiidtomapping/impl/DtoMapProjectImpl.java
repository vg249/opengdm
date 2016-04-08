package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.entity.pojos.Contact;
import org.gobiiproject.gobiidao.entity.access.ContactEntityDao;
import org.gobiiproject.gobiidao.resultset.access.RsContact;
import org.gobiiproject.gobiidtomapping.DtoMapProject;
import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Phil on 4/6/2016.
 */
public class DtoMapProjectImpl implements DtoMapProject {

    @Autowired
    private RsContact rsContact = null;

    public ProjectDTO getProject() {

        ProjectDTO returnVal = new ProjectDTO();

        try {

            List<String> primaryInvestigators = rsContact.getContactNamesForRoleName("PI");
            returnVal.setPrincipleInvestigators(primaryInvestigators);
        } catch(GobiiDaoException e ) {

        }

        return returnVal;
    }
}
