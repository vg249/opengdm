package org.gobiiproject.gobidomain.services.impl;

import java.util.List;

import org.gobiiproject.gobidomain.services.V3ProjectService;
import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapV3Project;
import org.gobiiproject.gobiimodel.dto.auditable.V3ProjectDTO;
import org.gobiiproject.gobiisampletrackingdao.V3ProjectDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class V3ProjectServiceImpl implements V3ProjectService<V3ProjectDTO> {
    Logger LOGGER = LoggerFactory.getLogger(V3ProjectServiceImpl.class);
    @Autowired
    private DtoMapV3Project dtoMapV3Project;

    @Override
    public List<V3ProjectDTO> getProjects(Integer pageNum, Integer pageSize) {
        // TODO Auto-generated method stub
        LOGGER.debug("Service getting projects");
        List<V3ProjectDTO> projects = dtoMapV3Project.getProjects(pageNum, pageSize);
        return projects;
    }

}