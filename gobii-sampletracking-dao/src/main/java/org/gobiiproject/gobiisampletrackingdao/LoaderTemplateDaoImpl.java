package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.LoaderTemplate;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoaderTemplateDaoImpl implements LoaderTemplateDao {

    Logger LOGGER = LoggerFactory.getLogger(LinkageGroupDaoImpl.class);

    @PersistenceContext
    protected EntityManager em;

    /**
     * Dao implementation for creating loader template
     * @param loaderTemplate entity to create
     * @return Created loader template with Id
     */
    @Override
    public LoaderTemplate create(LoaderTemplate loaderTemplate) throws GobiiDaoException {
        try {
            em.persist(loaderTemplate);
            em.flush();
            em.refresh(loaderTemplate, Utils.getHints(em, "graph.loader_template"));
            return loaderTemplate;
        }
        catch (PersistenceException pe) {
            throw new GobiiDaoException("Unable to create loader template");
        }
    }

    @Override
    public LoaderTemplate getById(Integer templateId) throws GobiiDaoException {
        LoaderTemplate loaderTemplate;
        try {
            loaderTemplate = em.find(
                LoaderTemplate.class,
                templateId,
                Utils.getHints(em, "graph.loader_template"));
        }
        catch (IllegalArgumentException e) {
           throw new GobiiDaoException(
               GobiiStatusLevel.ERROR,
               GobiiValidationStatusType.BAD_REQUEST,
               "Invalid Request or system error");
        }

        if(Objects.isNull(loaderTemplate)) {
            throw new GobiiDaoException(
                GobiiStatusLevel.ERROR,
                GobiiValidationStatusType.BAD_REQUEST,
                "Entity does not exist");
        }
        return loaderTemplate;
    }


}
