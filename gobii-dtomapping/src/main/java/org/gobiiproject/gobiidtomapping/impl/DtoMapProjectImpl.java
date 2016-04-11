package org.gobiiproject.gobiidtomapping.impl;

import org.gobiiproject.gobiidao.GobiiDaoException;
import org.gobiiproject.gobiidao.entity.pojos.Contact;
import org.gobiiproject.gobiidao.entity.access.ContactEntityDao;
import org.gobiiproject.gobiidao.resultset.access.RsContact;
import org.gobiiproject.gobiidtomapping.DtoMapProject;
import org.gobiiproject.gobiimodel.dto.container.NameIdListDTO;
import org.gobiiproject.gobiimodel.dto.container.ProjectDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Phil on 4/6/2016.
 */
public class DtoMapProjectImpl implements DtoMapProject {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapNameIdListImpl.class);

    @Autowired
    private RsContact rsContact = null;

    public ProjectDTO getProject() {

        ProjectDTO returnVal = new ProjectDTO();

            Map<String,String> primaryInvestigators = new HashMap<>();

//            List<Map<String,Object>>  rows = rsContact.getContactNamesForRoleName("PI");

            returnVal.setPrincipleInvestigators(primaryInvestigators);


        return returnVal;
    }

}
