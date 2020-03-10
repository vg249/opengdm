package org.gobiiproject.gobiidtomapping.entity.auditable.impl;

import java.util.List;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiidtomapping.entity.auditable.DtoMapV3Project;
import org.gobiiproject.gobiimodel.dto.auditable.V3ProjectDTO;
import org.gobiiproject.gobiimodel.entity.V3Project;
import org.gobiiproject.gobiisampletrackingdao.V3ProjectDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class DtoMapV3ProjectImpl implements DtoMapV3Project {

    Logger LOGGER = LoggerFactory.getLogger(DtoMapV3ProjectImpl.class);
    
    @Autowired
    private V3ProjectDao v3ProjectDao;

    @Override
    public List<V3ProjectDTO> getProjects(Integer pageNum, Integer pageSize) {
        // TODO Auto-generated method stub
        List<V3Project> projects = v3ProjectDao.getProjects(pageNum, pageSize);
        List<V3ProjectDTO> list = new java.util.ArrayList<>();
        projects.forEach( project -> {
            V3ProjectDTO dto = new V3ProjectDTO();
            dto.setId(project.getProjectId());
            dto.setProjectId(project.getProjectId());
            dto.setProjectName(project.getProjectName());

            list.add(dto);
        });
        return list;
    }

    @Override
    public V3ProjectDTO create(V3ProjectDTO dto) throws GobiiDtoMappingException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public V3ProjectDTO get(Integer dtoPkId) throws GobiiDtoMappingException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<V3ProjectDTO> getList() throws GobiiDtoMappingException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public V3ProjectDTO replace(Integer dtoPkId, V3ProjectDTO dto) throws GobiiDtoMappingException {
        // TODO Auto-generated method stub
        return null;
    }

    public V3ProjectDao getV3ProjectDao() {
        return v3ProjectDao;
    }

    public void setV3ProjectDao(V3ProjectDao v3ProjectDao) {
        this.v3ProjectDao = v3ProjectDao;
    }

}